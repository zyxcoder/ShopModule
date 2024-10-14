package com.zyxcoder.mvvmroot.utils

/**
 *  Created by zyx on 2020-07-06
 **/
class MillisecondCalculator @JvmOverloads constructor(
    private val needCalculateDayTime: Boolean = true
) {
    var millisecond: Long = 0
        set(value) {
            field = value
            calculateToTime()
        }
    var dayText: String = ""
        private set
    var hourText: String = ""
        private set
    var minuteText: String = ""
        private set
    var secondText: String = ""
        private set


    private fun calculateToTime() {
        val perSecond = 1000
        val perMinute = perSecond * 60
        val perHour = perMinute * 60
        val perDay = perHour * 24

        val days = if (needCalculateDayTime) {
            millisecond / perDay
        } else {
            0
        }
        val hours = (millisecond - days * perDay) / perHour
        val minutes = (millisecond - days * perDay - hours * perHour) / perMinute
        val seconds =
            (millisecond - days * perDay - hours * perHour - minutes * perMinute) / perSecond

        dayText = if (days <= 0L) {
            ""
        } else {
            days.toString()
        }
        hourText = when {
            hours < 0 -> {
                "00"
            }
            hours < 10 -> {
                "0$hours"
            }
            else -> {
                hours.toString()
            }
        }
        minuteText = when {
            minutes < 0 -> {
                "00"
            }
            minutes < 10 -> {
                "0$minutes"
            }
            else -> {
                minutes.toString()
            }
        }
        secondText = when {
            seconds < 0 -> {
                "00"
            }
            seconds < 10 -> {
                "0$seconds"
            }
            else -> {
                seconds.toString()
            }
        }
    }
}