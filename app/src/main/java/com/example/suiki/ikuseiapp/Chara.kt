package com.example.suiki.ikuseiapp

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class Chara: RealmObject() {
    @PrimaryKey
    var id: Int = 0
    @Required
    var name: String = ""
    var level: Int = 0
    var exp: Int = 0
    var nextExp: Int = 0
}
