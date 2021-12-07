package com.github.kamunyan.leftcrafterdead

import com.github.kamunyan.leftcrafterdead.campaign.Campaign
import com.github.kamunyan.leftcrafterdead.campaign.CampaignDifficulty
import com.github.kamunyan.leftcrafterdead.enemy.LCDEnemy
import com.github.kamunyan.leftcrafterdead.enemy.NormalEnemy
import com.github.kamunyan.leftcrafterdead.enemy.boss.MasterSmoker
import com.github.kamunyan.leftcrafterdead.enemy.specials.Boomer
import com.github.kamunyan.leftcrafterdead.enemy.specials.Charger
import com.github.kamunyan.leftcrafterdead.enemy.specials.Smoker
import com.github.kamunyan.leftcrafterdead.event.MatchReStartEvent
import com.github.kamunyan.leftcrafterdead.event.MatchStartEvent
import com.github.kamunyan.leftcrafterdead.event.RushStartEvent
import com.github.kamunyan.leftcrafterdead.player.LCDPlayer
import com.github.kamunyan.leftcrafterdead.weapons.WeaponType
import com.github.kamunyan.leftcrafterdead.weapons.secondary.HandGun
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarFlag
import org.bukkit.boss.BarStyle
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.jetbrains.annotations.NotNull
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.ArrayList

object MatchManager {
    private val plugin = LeftCrafterDead.instance

    //ロビーの初期スポーン
    lateinit var lobbySpawnLocation: Location

    //オンライン上のプレイヤーHashMap
    val onlineLCDPlayer = ConcurrentHashMap<String, LCDPlayer>()

    val campaignList: ArrayList<Campaign> = ArrayList()

    //使用するCampaign
    lateinit var campaign: Campaign

    val matchPlayer = mutableListOf<LCDPlayer>()

    var gameProgress = 0

    var campaignDifficulty = CampaignDifficulty.NORMAL

    var isBossParse = false

    val enemyHashMap = ConcurrentHashMap<UUID, LCDEnemy>()

    val bossList = listOf(MasterSmoker())

    var isPreparation = false

    var isMatch = false

    var isCheckPoint = false

    var isFinishing = false

    @Synchronized
    fun getLCDPlayer(@NotNull target: Player): LCDPlayer {
        val uuid = target.uniqueId.toString()
        if (!onlineLCDPlayer.containsKey(uuid)) {
            onlineLCDPlayer[uuid] = LCDPlayer(uuid)
            plugin.logger.info("[LCD]${ChatColor.GREEN}${target.displayName}'s ${ChatColor.AQUA}LCDPlayer created.")
        }
        return onlineLCDPlayer[uuid]!!
    }

    @Synchronized
    fun getLCDPlayer(@NotNull uuid: UUID): LCDPlayer {
        return getLCDPlayer(Bukkit.getPlayer(uuid)!!)
    }

    private fun startPreparation() {
        if (isMatch) {
            plugin.logger.info("${ChatColor.RED}[LCD]すでにマッチを開始しています")
            return
        } else if (isPreparation) {
            return
        }
        isPreparation = true
        object : BukkitRunnable() {
            var timeLeft = 10
            override fun run() {
                if (onlineLCDPlayer.isEmpty()) {
                    cancel()
                    isPreparation = false
                    return
                }

                if (timeLeft <= 10) {
                    if (timeLeft == 10) {
                        Bukkit.getOnlinePlayers().forEach { player ->
                            player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 2f, 0f)
                            player.sendMessage("[LCD]${ChatColor.AQUA}30秒後にゲームを開始します")
                        }
                    }
                    Bukkit.getOnlinePlayers().forEach { player ->
                        player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f)
                        if (timeLeft <= 5) {
                            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 24f)
                            player.sendTitle("$timeLeft", "", 5, 10, 5)
                        }
                    }
                }

                if (timeLeft <= 0) {
                    cancel()
                    isPreparation = false
                    Bukkit.getPluginManager().callEvent(MatchStartEvent())
                    return
                }
                timeLeft--
            }
        }.runTaskTimer(plugin, 0, 20)
    }

    @Synchronized
    fun startCampaign() {
        if (!isMatch) {
            plugin.logger.info("${ChatColor.RED}[LCD](startCampaign)isMatch isn't true.")
            return
        }

        //ランダムにマップを決定する
        campaign = getRandomCampaign()

        deleteEnemyMob()

        matchPlayer.forEach { lcdPlayer ->
            lcdPlayer.player.teleportAsync(campaign.startLocations[0])
            lcdPlayer.setPlayerStatus()
            lcdPlayer.perk.setFirstWeapon(lcdPlayer)
            lcdPlayer.perk.firstPrimaryWeapon()
            lcdPlayer.giveFirstSubGadget()
            lcdPlayer.gameMode = GameMode.ADVENTURE
            lcdPlayer.isMatchPlayer = true
            lcdPlayer.isSurvivor = true
            lcdPlayer.player.sendTitle("Stage ${gameProgress + 1} Start!", null, 20, 100, 20)
        }
        //難易度を設定
        determineDifficulty()
        //通常mobおよび特殊mobをスポーン
        spawnNormalEnemyMob()
        //ラッシュタイマースタート
        startRush()
    }

    @Synchronized
    fun startCheckPoint() {
        if (isCheckPoint) {
            plugin.logger.info("[startCheckPoint]isCheckPoint isn't false!")
            return
        }
        isCheckPoint = true
        if (numberOfSurvivors() > 0) {
            val addExp = numberOfSurvivors() * 5
            val getExp = 5 * campaignDifficulty.expRate + addExp
            matchPlayer.forEach { lcdPlayer ->
                lcdPlayer.player.teleport(campaign.restLocation)
                //復活処理
                if (!lcdPlayer.isSurvivor) {
                    lcdPlayer.perk.setFirstWeapon(lcdPlayer)
                    lcdPlayer.giveFirstSubGadget()
                }
                lcdPlayer.setPlayerStatus()
                lcdPlayer.isSurvivor = true
                lcdPlayer.gameMode = GameMode.ADVENTURE
                lcdPlayer.campaignData.exp += getExp
                lcdPlayer.player.sendMessage(Component.text("${ChatColor.GOLD}生存者数ボーナス ===> ${addExp}Exp"))
                lcdPlayer.player.sendMessage(Component.text("獲得経験値 ===> ${getExp}Exp"))
            }
        }

        deleteEnemyMob()
        gameProgress += 1
        if (campaign.gameProgressLimit == gameProgress) {
            isBossParse = true
        }
        Bukkit.broadcastMessage("[LCD]${ChatColor.GREEN}チェックポイントに到達しました！ 30秒後にゲームを再開します")
        object : BukkitRunnable() {
            var timeLeft = 30
            override fun run() {
                if (numberOfSurvivors() <= 0) {
                    cancel()
                    finishCampaign()
                    return
                }

                if (timeLeft <= 30) {
                    Bukkit.getOnlinePlayers().forEach { player ->
                        player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f)
                        if (timeLeft == 30) {
                            player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
                        }
                        if (timeLeft <= 5) {
                            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 24f)
                            player.sendTitle("$timeLeft", "", 5, 10, 5)
                        }
                    }
                }

                if (timeLeft <= 0) {
                    Bukkit.getPluginManager().callEvent(MatchReStartEvent())
                    this.cancel()
                    return
                }
                timeLeft--
            }
        }.runTaskTimer(plugin, 0, 20)
    }

    fun startRush() {
        val bossBar = Bukkit.createBossBar("Rush", BarColor.RED, BarStyle.SEGMENTED_10, BarFlag.CREATE_FOG)
        matchPlayer.forEach { lcdPlayer ->
            bossBar.addPlayer(lcdPlayer.player)
        }
        bossBar.isVisible = true
        object : BukkitRunnable() {
            var timeLeft = 1.0
            override fun run() {
                if (isCheckPoint || !isMatch) {
                    plugin.logger.info("[startRush]${ChatColor.AQUA}ラッシュのタイマーを停止しました")
                    bossBar.removeAll()
                    bossBar.isVisible = false
                    this.cancel()
                    return
                }

                if (timeLeft <= 0) {
                    Bukkit.getPluginManager().callEvent(RushStartEvent())
                    timeLeft = 1.0
                }

                timeLeft -= 0.02
                if (timeLeft < 0) return
                bossBar.progress = timeLeft
            }
        }.runTaskTimer(plugin, 0, 20)
    }


    @Synchronized
    fun finishCampaign() {
        if (!isMatch) {
            plugin.logger.info("[finishCampaign]isMatch isn't false!")
            return
        } else if (isFinishing) {
            plugin.logger.info("[finishCampaign]isFinishing isn't false!")
        }

        isFinishing = true

        object : BukkitRunnable() {
            var timeLeft = 15
            override fun run() {
                if (timeLeft <= 15) {
                    Bukkit.getOnlinePlayers().forEach { player ->
                        player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f)
                    }
                }
                if (timeLeft <= 5) {
                    Bukkit.broadcastMessage("[LCD]ゲーム終了まで${timeLeft}秒")
                }

                if (timeLeft <= 0) {
                    initializeGame()
                    this.cancel()
                    return
                }
                timeLeft--
            }
        }.runTaskTimer(plugin, 0, 20)
    }

    fun initializeGame() {
        isMatch = false
        gameProgress = 0
        isBossParse = false
        isCheckPoint = false
        isFinishing = false
        deleteEnemyMob()

        matchPlayer.forEach { lcdPlayer ->
            lcdPlayer.initialize()
        }

        Bukkit.getOnlinePlayers().forEach { player ->
            player.teleportAsync(lobbySpawnLocation)
        }
        if (matchPlayer.isNotEmpty()) {
            matchPlayer.clear()
        }
    }

    @Synchronized
    fun joinPlayer(player: Player) {
        if (isMatchPlayer(player.uniqueId)) {
            player.sendMessage("${ChatColor.RED}すでにゲームに参加しています")
            return
        }
        val lcdPlayer = getLCDPlayer(player)
        lcdPlayer.isMatchPlayer = true
        matchPlayer.add(lcdPlayer)
        Bukkit.broadcastMessage(
            "[LCD]${ChatColor.GREEN}${player.displayName}${ChatColor.AQUA}が" +
                    "ゲームに参加します"
        )

        if (!isMatch && !isPreparation && matchPlayer.size == 1) {
            startPreparation()
        }

        lcdPlayer.setPlayerStatus()

        if (!isMatch && isPreparation) {
            lcdPlayer.isSurvivor = true
        } else if (isCheckPoint) {
            lcdPlayer.isSurvivor = true
            lcdPlayer.gameMode = GameMode.ADVENTURE
            lcdPlayer.setPlayerStatus()
            lcdPlayer.perk.setFirstWeapon(lcdPlayer)
            lcdPlayer.giveFirstSubGadget()
            lcdPlayer.secondaryWeapon = HandGun("P226", WeaponType.Secondary)
            lcdPlayer.player.teleportAsync(campaign.restLocation)
        } else {
            //途中参加
            if (matchPlayer.isNotEmpty()) {
                for (p in matchPlayer) {
                    if (p.isSurvivor) {
                        lcdPlayer.player.teleportAsync(p.player.location)
                        lcdPlayer.isSurvivor = false
                        lcdPlayer.setSpectator()
                        break
                    }
                }
            }
        }
    }

    private fun leavePlayer(lcdPlayer: LCDPlayer) {
        lcdPlayer.isMatchPlayer = false
        lcdPlayer.isSurvivor = false
        matchPlayer.remove(lcdPlayer)
        if (matchPlayer.isEmpty() || numberOfSurvivors() <= 0) {
            finishCampaign()
        }
    }

    fun leavePlayer(player: Player) {
        leavePlayer(getLCDPlayer(player))
    }

    /**
     * マッチプレイヤーであるかを返す
     */
    fun isMatchPlayer(uuid: UUID): Boolean {
        val lcdPlayer = Bukkit.getPlayer(uuid)?.let { getLCDPlayer(it) }
        if (lcdPlayer != null) {
            return lcdPlayer.isMatchPlayer
        }
        return false
    }

    fun isMatchPlayer(lcdPlayer: LCDPlayer): Boolean {
        return lcdPlayer.isMatchPlayer
    }

    /**
     * ゲームの生存者数を返す
     */
    fun numberOfSurvivors(): Int {
        var count = 0
        matchPlayer.forEach { lcdPlayer ->
            if (lcdPlayer.isSurvivor) {
                count += 1
            }
        }
        return count
    }

    fun determineDifficulty() {
        val difficultyTable = listOf(15,30)
        var levels = 0
        matchPlayer.forEach {
            if (it.isSurvivor){
                levels += it.playerData.level
            }
        }
        val average = levels / numberOfSurvivors()
        var difficulty = CampaignDifficulty.NORMAL
        if (average >= difficultyTable[0] && average < difficultyTable[1]){
            difficulty = CampaignDifficulty.ADVANCED
        }else if (average >= difficultyTable[1]){
            difficulty = CampaignDifficulty.EXPERT
        }
        campaignDifficulty = difficulty
    }

    /**
     * 通常の敵性Mobを湧かせる
     */
    fun spawnNormalEnemyMob() {
        if (campaign.normalEnemyLocations[gameProgress] == null) {
            return
        }
        val normalEnemy = NormalEnemy
        campaign.normalEnemyLocations[gameProgress]?.forEach { location ->
            val location1 = location.clone().add(1.0, 0.0, 0.0)
            val location2 = location.clone().add(0.0, 0.0, 1.0)
            val location3 = location.clone().add(-1.0, 0.0, 0.0)
            val location4 = location.clone().add(0.0, 0.0, -1.0)

            var mobAmount = campaignDifficulty.normalMobSpawnAmount
            while (mobAmount > 0) {
                normalEnemy.spawnEnemy(location)
                normalEnemy.spawnEnemy(location1)
                normalEnemy.spawnEnemy(location2)
                normalEnemy.spawnEnemy(location3)
                normalEnemy.spawnEnemy(location4)
                mobAmount--
            }
            spawnSpecialEnemyMob(location)
        }
    }

    fun spawnSpecialEnemyMob(location: Location) {
        val specialEnemy = listOf(Boomer, Charger, Smoker)
        val rand = Random()
        specialEnemy[rand.nextInt(specialEnemy.size)].spawnEnemy(location)
    }

    /**
     * ゲームで発生した敵性Mobを削除する
     */
    fun deleteEnemyMob() {
        val world = Bukkit.getWorld(campaign.campaignTitle)
        if (world == null) {
            plugin.logger.info("${ChatColor.RED}Worldが読み込めませんでした")
            return
        }
        var count = 0
        world.livingEntities.forEach { livingEntity ->
            if (livingEntity.type == EntityType.PLAYER || livingEntity.type == EntityType.VILLAGER) {
                return@forEach
            }
            livingEntity.remove()
            count += 1
        }
        enemyHashMap.clear()
        plugin.logger.info("[deleteEnemyMob]${ChatColor.AQUA}${count}体のmobを削除しました")
    }

    private fun getRandomCampaign(): Campaign {
        campaignList.shuffle()
        val random = Random().nextInt(campaignList.size)
        return campaignList[random]
    }
}