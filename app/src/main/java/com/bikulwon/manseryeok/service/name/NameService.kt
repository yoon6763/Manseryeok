package com.bikulwon.manseryeok.service.name

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.bikulwon.manseryeok.manseryeokdb.ManseryeokSQLHelper
import com.bikulwon.manseryeok.models.Manseryeok
import com.bikulwon.manseryeok.models.name.NameScoreChildItem
import com.bikulwon.manseryeok.models.name.NameScoreItem
import com.bikulwon.manseryeok.models.user.User
import com.bikulwon.manseryeok.utils.Utils

class NameService(val context: Context, var name: String, private val userModel: User) {
    private val userManseryeok by lazy {
        importCalendar(
            userModel.birthYear,
            userModel.birthMonth,
            userModel.birthDay
        )
    }

    companion object {
        const val EVEN = 0
        const val ODD = 1

        const val YEAR = 0
        const val MONTH = 1
        const val DAY = 2
    }

    /*₩
        한글 유니코드 규칙 - (초성 * 21 + 중성) * 28 + 종성 + 0xAC00
        전 -> ㅈ : 12, ㅓ : 4 ㄴ : 5
        (12 * 21 + 4) * 28 + 5 + 0xAC00
    */

    private val nameComponents = ArrayList<NameCharComponent>()

    init {
        for (nameChar in name) {
            if (nameChar.code !in NameCharConstants.HANGLE_UNICODE_START..NameCharConstants.HANGLE_UNICODE_END) {
                Toast.makeText(context, "한글만 입력해주세요.", Toast.LENGTH_SHORT).show()
                break
            }
            addNameToComponents(nameChar)
        }
    }

    private fun addNameToComponents(nameChar: Char) {
        // 김
        val uniVal = nameChar - NameCharConstants.HANGLE_UNICODE_START

        val initialSound = NameCharConstants.initialSound[(uniVal.code / 28 / 21)] // ㄱ
        val middleSound = NameCharConstants.middleSound[(uniVal.code / 28) % 21] // ㅣ
        val finalSound = NameCharConstants.finalSound[uniVal.code % 28] // ㅁ

        nameComponents.add(NameCharComponent(nameChar, initialSound, middleSound, finalSound))
    }


    fun calcYearGanji(year: Int, month: Int, day: Int): List<NameScoreItem> {
        val manseryeok = importCalendar(year, month, day)
        val yearGanji = manseryeok.cd_hyganjee!!
        val userYearGanji = userManseryeok.cd_hyganjee!!

        return calcGanji(yearGanji, userYearGanji)
    }

    fun calcMonthGanji(year: Int, month: Int, day: Int): List<NameScoreItem> {
        val manseryeok = importCalendar(year, month, day)
        val monthGanji = manseryeok.cd_hmganjee!!
        val userYearGanji = userManseryeok.cd_hyganjee!!

        return calcGanji(monthGanji, userYearGanji)
    }

    fun calcDayGanji(year: Int, month: Int, day: Int): List<NameScoreItem> {
        val manseryeok = importCalendar(year, month, day)
        val dayGanji = manseryeok.cd_hdganjee!!
        val userYearGanji = userManseryeok.cd_hyganjee!!

        return calcGanji(dayGanji, userYearGanji)
    }

    private fun calcGanji(luckGanji: String, birthYearGanji: String): List<NameScoreItem> {
        val nameScoreItems = ArrayList<NameScoreItem>()

        nameComponents.forEach { nameCharComponent ->
            val parity = nameCharComponent.score % 2 // 짝수 or 홀수
            val nameScoreItem = NameScoreItem(nameCharComponent.name)
            val initialAndFinalSound = arrayListOf(nameCharComponent.initialSound)

            if (nameCharComponent.finalSound != null &&
                nameCharComponent.finalSound != ' '
            ) {
                initialAndFinalSound.add(nameCharComponent.finalSound)
            }

            initialAndFinalSound.forEach { sound ->
                val soundGanji = getNameGanji(sound, parity)

                // 바깥쪽꺼
                val ganjiLuckTopLabel = Utils.getPillarLabel(luckGanji[0].toString(), soundGanji.toString(),)
                val ganjiLuckBottomLabel = Utils.getPillarLabel(luckGanji[1].toString(), soundGanji.toString(),)

                // 안쪽꺼
                val ganjiTopLabel = Utils.getPillarLabel(soundGanji.toString(), birthYearGanji[0].toString(),)
                val ganjiBottomLabel = Utils.getPillarLabel(soundGanji.toString(), birthYearGanji[1].toString(),)


                Log.d("NameService", "luckGanji: $luckGanji   userYearGanji: $birthYearGanji  soundGanji: $soundGanji")
                Log.d("NameService", "calcGanji: $ganjiTopLabel $ganjiBottomLabel $ganjiLuckTopLabel $ganjiLuckBottomLabel")

                nameScoreItem.nameScoreChildItems.add(
                    NameScoreChildItem(
                        nameHan = soundGanji.toString(),
                        ganjiTop = ganjiTopLabel,
                        ganjiBottom = ganjiBottomLabel,
                        luckTop = ganjiLuckTopLabel,
                        luckBottom = ganjiLuckBottomLabel,
                    )
                )
            }

            nameScoreItems.add(nameScoreItem)
        }

        return nameScoreItems
    }

    fun getGanjiLabel(year: Int, month: Int, day: Int, type: Int): String {
        val manseryeok = importCalendar(year, month, day)
        return when (type) {
            YEAR -> manseryeok.cd_hyganjee!!
            MONTH -> manseryeok.cd_hmganjee!!
            DAY -> manseryeok.cd_hdganjee!!
            else -> throw Exception("잘못된 타입입니다.")
        }
    }


    private fun importCalendar(year: Int, month: Int, day: Int): Manseryeok {
        val dbHelper = ManseryeokSQLHelper(context)
        dbHelper.createDataBase()
        dbHelper.open()

        val userCalendar = dbHelper.getDayData(year, month, day)

        dbHelper.close()
        return userCalendar
    }

    private fun getProperty(sound: Char): Int {
        return when (sound) {
            'ㄱ', 'ㅋ' -> 0 // 목
            'ㄴ', 'ㄷ', 'ㄹ', 'ㅌ' -> 1 // 화
            'ㅇ', 'ㅎ' -> 2 // 토
            'ㅅ', 'ㅈ', 'ㅊ' -> 3 // 금
            'ㅁ', 'ㅂ', 'ㅍ' -> 4 // 수
            else -> -1
        }
    }

    private fun getNameGanji(sound: Char, parity: Int): Char = when (getProperty(sound)) {
        0 -> if (parity == ODD) '甲' else '乙'
        1 -> if (parity == ODD) '丙' else '丁'
        2 -> if (parity == ODD) '戊' else '己'
        3 -> if (parity == ODD) '庚' else '辛'
        4 -> if (parity == ODD) '壬' else '癸'
        else -> ' '
    }

}