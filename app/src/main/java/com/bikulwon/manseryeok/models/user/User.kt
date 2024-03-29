package com.bikulwon.manseryeok.models.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bikulwon.manseryeok.models.UserInputViewModel
import java.time.LocalDateTime
import java.util.Calendar

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    var userId: Long,
    var name: String?,
    var gender: Int, // 0 - 남자, 1 - 여자

    var birthYear: Int,
    var birthMonth: Int,
    var birthDay: Int,
    var birthHour: Int,
    var birthMinute: Int,
    var includeTime: Boolean,

    var birthPlace: String?,
    var timeDiff: Int,

    // 0 - 사용안함, 1 - 사용
    var useSummerTime: Int,
    var useTokyoTime: Int,

    var memo: String?
) {

    fun getBirthOrigin() = Calendar.getInstance().apply {
        set(Calendar.YEAR, birthYear)
        set(Calendar.MONTH, birthMonth - 1)
        set(Calendar.DAY_OF_MONTH, birthDay)

        if (includeTime) {
            set(Calendar.HOUR_OF_DAY, birthHour)
            set(Calendar.MINUTE, birthMinute)
        }
    }

    fun getBirthLocalDateTime(): LocalDateTime {
        if (!includeTime) return LocalDateTime.of(birthYear, birthMonth, birthDay, 0, 0, 0)
        return LocalDateTime.of(birthYear, birthMonth, birthDay, birthHour, birthMinute, 0)
    }

    fun getBirthCalculatedLocalDateTime(): LocalDateTime {
        var userBirth = LocalDateTime.of(birthYear, birthMonth, birthDay, 0, 0, 0)

        if (!includeTime) {
            userBirth = userBirth.withHour(23)
            userBirth = userBirth.withMinute(59)
            return userBirth
        }

        userBirth = userBirth.withHour(birthHour)
        userBirth = userBirth.withMinute(birthMinute)

        if (useSummerTime == 1) userBirth = userBirth.plusHours(1)
        if (useTokyoTime == 1) userBirth = userBirth.minusMinutes(30)

        return userBirth
    }

    fun getBirthCalculated(): Calendar {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, birthYear)
            set(Calendar.MONTH, birthMonth - 1)
            set(Calendar.DAY_OF_MONTH, birthDay)
        }

        if (includeTime) {
            calendar.set(Calendar.HOUR_OF_DAY, birthHour)
            calendar.set(Calendar.MINUTE, birthMinute)
            calendar.add(Calendar.MINUTE, timeDiff) // 시차 적용

            if (useSummerTime == 1) calendar.add(Calendar.HOUR_OF_DAY, 1)
            if (useTokyoTime == 1) calendar.add(Calendar.MINUTE, 30)
        }

        return calendar
    }

    fun updateFromViewModel(userInputViewModel: UserInputViewModel) {
        val parsedUserEntity = userInputViewModel.toUserEntity()
        this.name = userInputViewModel.name.value
        this.gender = userInputViewModel.gender.value!!
        this.birthYear = parsedUserEntity.birthYear
        this.birthMonth = parsedUserEntity.birthMonth
        this.birthDay = parsedUserEntity.birthDay
        this.includeTime = userInputViewModel.isIncludeTime.value!!

        if (this.includeTime) {
            this.birthHour = userInputViewModel.hourLabel!!.value!!.toInt()
            this.birthMinute = userInputViewModel.minuteLabel!!.value!!.toInt()
        } else {
            this.birthHour = -1
            this.birthMinute = -1
        }

        this.birthPlace = userInputViewModel.birthPlace.value
        this.timeDiff = userInputViewModel.timeDiff.value!!

        this.useSummerTime = if (userInputViewModel.useSummerTime.value!!) 1 else 0
        this.useTokyoTime = if (userInputViewModel.useTokyoTime.value!!) 1 else 0
    }
}