package com.github.kamunyan.leftcrafterdead.skill

data class StatusData(
    /**
     * 体力上限
     */
    var healthScaleAmount: Double = 20.0,
    /**
     * アーマー上限
     */
    var armorLimit: Double = 6.0,
    /**
     * アーマーが回復するまでの時間増減倍率
     */
    var armorRecovery: Double = 1.0,
    /**
     * 移動スピード
     */
    var walkSpeed: Float = 0.2f,
    /**
     * ダメージ倍率
     */
    var weaponDamageMultiplier: Double = 1.0,
    /**
     * ショットガンダメージ増減倍率
     */
    var shotgunDamageMultiplier:Double = 1.0,
    /**
     * 回避率
     */
    var dodgeMultiplier:Int = 0,
    /**
     * ヘッドショットダメージ倍率
     */
    var headShotDamageMultiplier :Double = 1.0,
    /**
     * リロード速度。数値が大きいほど遅くなる
     */
    var reloadSpeedAcceleration: Double = 0.0,
    /**
     * フルオート武器のリロード速度
     */
    var fireRateReloadSpeedAcceleration:Double = 0.0,
    /**
     * ショットガンのリロード速度
     */
    var shotgunReloadSpeedAcceleration:Double = 0.0,
    /**
     * マガジン容量増減倍率
     */
    var magazineAmountMultiplier:Double = 1.0,
    /**
     * 携行弾薬量増減倍率
     */
    var ammunitionAmountMultiplier:Double = 1.0,
    /**
     * レート増減値
     */
    var rateAcceleration: Int = 0,
    /**
     * 初期で配布される追加グレネード個数
     */
    var addFirstGrenade: Int = 0,
    /**
     * クールダウン増減値
     */
    var mainGadgetCoolDown: Int = 0,
    /**
     * メインガジェットの性能倍率
     */
    var mainGadgetAddPerformance: Double = 1.0,
    /**
     * 回復ポーションの回復増加倍率
     */
    var healPotionMultiplier:Double = 1.0,
    /**
     * 回復ポーションを持てる数
     */
    var healPotionAmount :Int = 1,
    /**
     * 鎮痛剤を持てる数
     */
    var painKillerAmount:Int = 3,
    /**
     * 弾拡散増減値
     */
    var addBulletSpread: Double = 0.0,
    /**
     * スニーク時の弾拡散増減値
     */
    var sneakAddBulletSpread: Double = 0.0,
    /**
     * ファイアレート武器の弾拡散増減値
     */
    var fireRateAddBulletSpread:Double = 0.0,
    /**
     * グレネードダメージ倍率
     */
    var grenadeDamageMultiplier: Double = 1.0,
    /**
     * クリティカル確率
     */
    var criticalMultiplier: Int = 0,
    /**
     * クリティカルダメージ倍率
     */
    var criticalDamageMultiplier: Double = 1.5,
    /**
     * 獲得出来るMoney倍率
     */
    var addMoneyMultiplier:Double = 1.0,
    val specialSkillTypes: ArrayList<SpecialSkillType> = ArrayList()
) {
}