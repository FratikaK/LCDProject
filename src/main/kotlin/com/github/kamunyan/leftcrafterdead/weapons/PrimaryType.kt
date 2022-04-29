package com.github.kamunyan.leftcrafterdead.weapons

enum class PrimaryType(val weaponTitle:String, val category: GunCategory, val coin:Int) {
    M4_CARBINE("M4 CARBINE",GunCategory.ASSAULT_RIFLE,300),
    M16("M16",GunCategory.ASSAULT_RIFLE,300),
    AK47("AK47",GunCategory.ASSAULT_RIFLE,800),

    SUPER_90("SUPER 90",GunCategory.SHOTGUN,300),
    EXPRESS("870 EXPRESS",GunCategory.SHOTGUN,600),
    TAC14("TAC14",GunCategory.SHOTGUN,800),

    UZI("UZI",GunCategory.SUB_MACHINE_GUN,300),
    MP5("MP5",GunCategory.SUB_MACHINE_GUN,300),
    UMP45("UMP45",GunCategory.SUB_MACHINE_GUN,800),

    PHOENIX_350L("PHOENIX 350L",GunCategory.SNIPER,800),

    M249("M249",GunCategory.LMG,1200),
    ;
}