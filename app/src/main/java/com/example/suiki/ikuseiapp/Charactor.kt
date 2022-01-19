package com.example.suiki.ikuseiapp

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class Charactor: RealmObject() {
    @PrimaryKey
    var id: Int = 0
    @Required
    var name: String = ""
    @Required
    var level: Int = 0
    @Required
    var exp: Long = 0
    @Required
    var nextExp: Long = 0
}
