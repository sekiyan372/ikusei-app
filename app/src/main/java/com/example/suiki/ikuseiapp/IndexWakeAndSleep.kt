package com.example.suiki.ikuseiapp

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class IndexWakeAndSleep: RealmObject() {
    @PrimaryKey
    var id: Int = 0
    var wakeUpTime: String = ""
    var sleepTime: String = ""
    var dateTime: Date = Date()
}
