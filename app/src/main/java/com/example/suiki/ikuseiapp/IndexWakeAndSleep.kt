package com.example.suiki.ikuseiapp

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class IndexWakeAndSleep: RealmObject() {
    @PrimaryKey
    var id: Int = 0
    var wakeUpTime: Date = Date()
    var sleepTime: Date = Date()
}
