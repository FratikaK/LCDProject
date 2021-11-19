package com.github.kamunyan.leftcrafterdead.skill

data class StatusData(
    /**
     * 体力上限
     */
    var healthScaleAmount: Double = 20.0,
    /**
     * アーマー上限
     */
    var armorLimit: Double = 20.0,
    /**
     * 移動スピード
     */
    var walkSpeed: Float = 0.2f,
    /**
     * ダメージ倍率
     */
    var weaponDamageMultiplier: Double = 1.0,
    /**
     * ヘッドショットダメージ倍率
     */
    var headShotDamageMultiplier :Double = 1.0,
    /**
     * リロード速度
     */
    var reloadSpeedAcceleration: Double = 1.0,
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
     * 弾拡散増減値
     */
    var addBulletSpread: Int = 0,
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
    var criticalDamageMultiplier: Double = 1.1,
    /**
     * 獲得出来るMoney倍率
     */
    var addMoneyMultiplier:Double = 1.0,
    val specialSkillTypes: ArrayList<SpecialSkillType> = ArrayList()
) {
}