package com.helper.develop.util

import java.util.Calendar

var Calendar.year: Int
    get() = get(Calendar.YEAR)
    set(value) {
        set(Calendar.YEAR, value)
    }

var Calendar.month: Int
    get() = get(Calendar.MONTH)
    set(value) {
        set(Calendar.MONTH, value)
    }

var Calendar.dayOfMonth: Int
    get() = get(Calendar.DAY_OF_MONTH)
    set(value) {
        set(Calendar.DAY_OF_MONTH, value)
    }

var Calendar.weekOfMonth: Int
    get() = get(Calendar.WEEK_OF_MONTH)
    set(value) {
        set(Calendar.WEEK_OF_MONTH, value)
    }

var Calendar.dayOfWeek: Int
    get() = get(Calendar.DAY_OF_WEEK)
    set(value) {
        set(Calendar.DAY_OF_WEEK, value)
    }

val Calendar.monthString: String
    get() = when (month) {
        Calendar.JANUARY -> "一月"
        Calendar.FEBRUARY -> "二月"
        Calendar.MARCH -> "三月"
        Calendar.APRIL -> "四月"
        Calendar.MAY -> "五月"
        Calendar.JUNE -> "六月"
        Calendar.JULY -> "七月"
        Calendar.AUGUST -> "八月"
        Calendar.SEPTEMBER -> "九月"
        Calendar.OCTOBER -> "十月"
        Calendar.NOVEMBER -> "十一月"
        Calendar.DECEMBER -> "十二月"
        else -> error("Invalid month")
    }

val Calendar.dayOfWeekString: String
    get() = when (dayOfWeek) {
        Calendar.SUNDAY -> "周日"
        Calendar.MONDAY -> "周一"
        Calendar.TUESDAY -> "周二"
        Calendar.WEDNESDAY -> "周三"
        Calendar.THURSDAY -> "周四"
        Calendar.FRIDAY -> "周五"
        Calendar.SATURDAY -> "周六"
        else -> error("Invalid dayOfWeek")
    }

val Calendar.weekOfMonthByCN: Int
    get() {
        weekOfMonth.run {
            return if (dayOfWeek == Calendar.SUNDAY && this > 1) {
                this - 1
            } else {
                this
            }
        }
    }

var Calendar.dayOfYear: Int
    get() = get(Calendar.DAY_OF_YEAR)
    set(value) {
        set(Calendar.DAY_OF_YEAR, value)
    }

var Calendar.hourOfDay: Int
    get() = get(Calendar.HOUR_OF_DAY)
    set(value) {
        set(Calendar.HOUR_OF_DAY, value)
    }

var Calendar.hour: Int
    get() = get(Calendar.HOUR)
    set(value) {
        set(Calendar.HOUR, value)
    }

var Calendar.minute: Int
    get() = get(Calendar.MINUTE)
    set(value) {
        set(Calendar.MINUTE, value)
    }

var Calendar.second: Int
    get() = get(Calendar.SECOND)
    set(value) {
        set(Calendar.SECOND, value)
    }

var Calendar.millisecond: Int
    get() = get(Calendar.MILLISECOND)
    set(value) {
        set(Calendar.MILLISECOND, value)
    }

val Calendar.minDayInMonth
    get() = getActualMinimum(Calendar.DAY_OF_MONTH)

val Calendar.maxDayInMonth
    get() = getActualMaximum(Calendar.DAY_OF_MONTH)

val Calendar.dayRangeInMonth
    get() = minDayInMonth..maxDayInMonth

val Calendar.minMonth
    get() = getActualMinimum(Calendar.MONTH)

val Calendar.maxMonth
    get() = getActualMaximum(Calendar.MONTH)

val Calendar.monthRange
    get() = minMonth..maxMonth

val Calendar.minDayInYear
    get() = getActualMinimum(Calendar.DAY_OF_YEAR)

val Calendar.maxDayInYear
    get() = getActualMaximum(Calendar.DAY_OF_YEAR)

val Calendar.dayRangeInYear
    get() = minDayInYear..maxDayInYear

fun Calendar.isSomeDay(calendar: Calendar): Boolean {
    return year == calendar.year
            && dayOfYear == calendar.dayOfYear
}

fun Calendar.isYesterday(calendar: Calendar): Boolean {
    return year == calendar.year
            && dayOfYear + 1 == calendar.dayOfYear
}

fun Calendar.isSomeWeek(calendar: Calendar): Boolean {
    return year == calendar.year
            && month == calendar.month
            && weekOfMonth == weekOfMonth
}

fun Calendar.isSomeWeekByCN(calendar: Calendar): Boolean {
    return year == calendar.year
            && month == calendar.month
            && weekOfMonthByCN == weekOfMonthByCN
}

fun Calendar.isSomeMonth(calendar: Calendar): Boolean {
    return year == calendar.year
            && month == calendar.month
}

fun Calendar.isSomeYear(calendar: Calendar): Boolean {
    return year == calendar.year
}
