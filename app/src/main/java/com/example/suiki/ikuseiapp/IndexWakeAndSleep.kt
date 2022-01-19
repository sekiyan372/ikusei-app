package com.example.suiki.ikuseiapp

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class IndexWakeAndSleep: RealmObject() {
    @PrimaryKey
    var id: Int = 0
    var wakeUpTime: String = ""
    var sleepTime: String = ""
    @Required
    var dateTime: Date = Date()
}
