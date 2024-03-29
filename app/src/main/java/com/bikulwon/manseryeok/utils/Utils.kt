package com.bikulwon.manseryeok.utils

import android.icu.util.ChineseCalendar
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    enum class InfoType(val value: String) {
        CREATE("CREATE"), EDIT("EDIT")
    }

    val dateNumFormat = SimpleDateFormat("yyyyMMdd")
    val dateTimeNumFormat = SimpleDateFormat("yyyyMMddHHmm")
    val dateSlideFormat = SimpleDateFormat("yyyy-MM-dd")
    val dateTimeSlideFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
    val dateDotFormat = SimpleDateFormat("yyyy.MM.dd")
    val dateKorFormat = SimpleDateFormat("yyyy년 MM월 dd일")
    val dateTimeKorFormat = SimpleDateFormat("yyyy년 MM월 dd일 HH:mm")
    val timeFormat = SimpleDateFormat("a hh:mm")
    val degreeFormat = DecimalFormat("#.0")
    //const val DB_FILE_NAME = "Manseryeok.db"


    val sibgan = arrayOf(
        arrayOf("甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"),
        arrayOf("갑", "을", "병", "정", "무", "기", "경", "신", "임", "계"),
        arrayOf("목", "목", "화", "화", "토", "토", "금", "금", "수", "수")
    )


    val sibiji = arrayOf(
        arrayOf("子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"),
        arrayOf("자", "축", "인", "묘", "진", "사", "오", "미", "신", "유", "술", "해"),
        arrayOf("수", "토", "목", "목", "토", "화", "화", "토", "금", "금", "토", "수")
    )

    val tenGanForYear = arrayOf(arrayOf("庚", "辛", "壬", "癸", "甲", "乙", "丙", "丁", "戊", "己"))


    val twelveGanForYear =
        arrayOf(arrayOf("申", "酉", "戌", "亥", "子", "丑", "寅", "卯", "辰", "巳", "午", "未"))

//    십간
//    甲 乙 丙 丁 戊 己 庚 辛 壬 癸
//    갑 을 병 정 무 기 경 신 임 계
//    목 목 화 화 토 토 금 금 수 수

//    십이지
//    子 丑 寅 卯 辰 巳 午 未 申 酉 戌 亥
//    자 축 인 묘 진 사 오 미 신 유 술 해
//    수 토 목 목 토 화 화 토 금 금 토 수

//    丙丁巳午 화
//    壬癸子亥 수
//    甲乙寅卯 목
//    庚辛申酉 금
//    戊己丑辰未戌 토

    fun getProperty(c: Char): Int {
        // 화 수 목 금 토
        //  0 1 2 3 4
        return when (c) {
            '丙', '丁', '巳', '午' -> 0 // 화
            '壬', '癸', '子', '亥' -> 1 // 수
            '甲', '乙', '寅', '卯' -> 2 // 목
            '庚', '辛', '申', '酉' -> 3 // 금
            '戊', '己', '丑', '辰', '未', '戌' -> 4 // 토
            else -> 5
        }
    }

    val pillarLabelArr = arrayOf(
        //        甲      乙      丙      丁      戊      己      庚     辛      壬      癸
        arrayOf("편인", "정인", "편관", "정관", "편재", "정재", "식신", "상관", "비견", "겁재"), // 壬, 亥
        arrayOf("정인", "편인", "정관", "편관", "정재", "편재", "상관", "식신", "겁재", "비견"), // 癸, 子
        arrayOf("비견", "겁재", "편인", "정인", "편관", "정관", "편재", "정재", "식신", "상관"), // 甲, 寅
        arrayOf("겁재", "비견", "정인", "편인", "정관", "편관", "정재", "편재", "상관", "식신"), // 乙, 卯
        arrayOf("식신", "상관", "비견", "겁재", "편인", "정인", "편관", "정관", "편재", "정재"), // 丙, 巳
        arrayOf("상관", "식신", "겁재", "비견", "정인", "편인", "정관", "편관", "정재", "편재"), // 丁, 午
        arrayOf("편재", "정재", "식신", "상관", "비견", "겁재", "편인", "정인", "편관", "정관"), // 戊, 辰, 戌
        arrayOf("정재", "편재", "상관", "식신", "겁재", "비견", "정인", "편인", "정관", "편관"), // 己, 丑, 未
        arrayOf("편관", "정관", "편재", "정재", "식신", "상관", "비견", "겁재", "편인", "정인"), // 庚, 申
        arrayOf("정관", "편관", "정재", "편재", "상관", "식신", "겁재", "비견", "정인", "편인")  // 辛, 酉
    )


    // 육친 속견표
    fun getPillarLabel(me: String, target: String): String {
        return when (me) {
            "甲", "寅" -> when (target) {
                "壬", "亥" -> "편인"
                "癸", "子" -> "정인"
                "甲", "寅" -> "비견"
                "乙", "卯" -> "겁재"
                "丙", "巳" -> "식신"
                "丁", "午" -> "상관"
                "戊", "辰", "戌" -> "편재"
                "己", "丑", "未" -> "정재"
                "庚", "申" -> "편관"
                "辛", "酉" -> "정관"
                else -> "error/${me}/${target}"
            }

            "乙", "卯" -> when (target) {
                "癸", "子" -> "편인"
                "壬", "亥" -> "정인"
                "乙", "卯" -> "비견"
                "甲", "寅" -> "겁재"
                "丁", "午" -> "식신"
                "丙", "巳" -> "상관"
                "己", "丑", "未" -> "편재"
                "戊", "辰", "戌" -> "정재"
                "辛", "酉" -> "편관"
                "庚", "申" -> "정관"
                else -> "error/${me}/${target}"
            }

            "丙", "巳" -> when (target) {
                "甲", "寅" -> "편인"
                "乙", "卯" -> "정인"
                "丙", "巳" -> "비견"
                "丁", "午" -> "겁재"
                "戊", "辰", "戌" -> "식신"
                "己", "丑", "未" -> "상관"
                "庚", "申" -> "편재"
                "辛", "酉" -> "정재"
                "壬", "亥" -> "편관"
                "癸", "子" -> "정관"
                else -> "error/${me}/${target}"
            }

            "丁", "午" -> when (target) {
                "乙", "卯" -> "편인"
                "甲", "寅" -> "정인"
                "丁", "午" -> "비견"
                "丙", "巳" -> "겁재"
                "己", "丑", "未" -> "식신"
                "戊", "辰", "戌" -> "상관"
                "辛", "酉" -> "편재"
                "庚", "申" -> "정재"
                "癸", "子" -> "편관"
                "壬", "亥" -> "정관"
                else -> "error/${me}/${target}"
            }

            "戊", "辰", "戌" -> when (target) {
                "丙", "巳" -> "편인"
                "丁", "午" -> "정인"
                "戊", "辰", "戌" -> "비견"
                "己", "丑", "未" -> "겁재"
                "庚", "申" -> "식신"
                "辛", "酉" -> "상관"
                "壬", "亥" -> "편재"
                "癸", "子" -> "정재"
                "甲", "寅" -> "편관"
                "乙", "卯" -> "정관"
                else -> "error/${me}/${target}"
            }

            "己", "丑", "未" -> when (target) {
                "丁", "午" -> "편인"
                "丙", "巳" -> "정인"
                "己", "丑", "未" -> "비견"
                "戊", "辰", "戌" -> "겁재"
                "辛", "酉" -> "식신"
                "庚", "申" -> "상관"
                "癸", "子" -> "편재"
                "壬", "亥" -> "정재"
                "乙", "卯" -> "편관"
                "甲", "寅" -> "정관"
                else -> "error/${me}/${target}"
            }

            "庚", "申" -> when (target) {
                "戊", "辰", "戌" -> "편인"
                "己", "丑", "未" -> "정인"
                "庚", "申" -> "비견"
                "辛", "酉" -> "겁재"
                "壬", "亥" -> "식신"
                "癸", "子" -> "상관"
                "甲", "寅" -> "편재"
                "乙", "卯" -> "정재"
                "丙", "巳" -> "편관"
                "丁", "午" -> "정관"
                else -> "error/${me}/${target}"
            }

            "辛", "酉" -> when (target) {
                "己", "丑", "未" -> "편인"
                "戊", "辰", "戌" -> "정인"
                "辛", "酉" -> "비견"
                "庚", "申" -> "겁재"
                "癸", "子" -> "식신"
                "壬", "亥" -> "상관"
                "乙", "卯" -> "편재"
                "甲", "寅" -> "정재"
                "丁", "午" -> "편관"
                "丙", "巳" -> "정관"
                else -> "error/${me}/${target}"
            }

            "壬", "亥" -> when (target) {
                "庚", "申" -> "편인"
                "辛", "酉" -> "정인"
                "壬", "亥" -> "비견"
                "癸", "子" -> "겁재"
                "甲", "寅" -> "식신"
                "乙", "卯" -> "상관"
                "丙", "巳" -> "편재"
                "丁", "午" -> "정재"
                "戊", "辰", "戌" -> "편관"
                "己", "丑", "未" -> "정관"
                else -> "error/${me}/${target}"
            }

            "癸", "子" -> when (target) {
                "辛", "酉" -> "편인"
                "庚", "申" -> "정인"
                "癸", "子" -> "비견"
                "壬", "亥" -> "겁재"
                "乙", "卯" -> "식신"
                "甲", "寅" -> "상관"
                "丁", "午" -> "편재"
                "丙", "巳" -> "정재"
                "己", "丑", "未" -> "편관"
                "戊", "辰", "戌" -> "정관"
                else -> "error/${me}/${target}"
            }

            else -> "error/${me}/${target}"
        }
//        val meIdx = sibgan[0].indexOf(me)
//        val targetIdx = when (target) {
//            "壬", "亥" -> 0
//            "癸", "子" -> 1
//            "甲", "寅" -> 2
//            "乙", "卯" -> 3
//            "丙", "巳" -> 4
//            "丁", "午" -> 5
//            "戊", "辰", "戌" -> 6
//            "己", "丑", "未" -> 7
//            "庚", "申" -> 8
//            "辛", "酉" -> 9
//            else -> -1
//        }
//        if (meIdx == -1 || targetIdx == -1) return "error/${me}"
//        return pillarLabelArr[targetIdx][meIdx]
    }

    fun getYearGanji(year: Int): String {
        var result = ""
        result += tenGanForYear[0][year % 10]
        result += twelveGanForYear[0][year % 12]
        return result
    }

    fun getYearGanji(birth: Calendar): String {
        return getYearGanji(birth[Calendar.YEAR])
    }

    fun getMonthGanjiList(char: Char): String {
        return when (char) {
            '甲', '己' -> "乙丑 丙寅 丁卯 戊辰 己巳 庚午 辛未 壬申 癸酉 甲戌 乙亥 丙子"
            '乙', '庚' -> "丁丑 戊寅 己卯 庚辰 辛巳 壬午 癸未 甲申 乙酉 丙戌 丁亥 戊子"
            '丙', '辛' -> "己丑 庚寅 辛卯 壬辰 癸巳 甲午 乙未 丙申 丁酉 戊戌 己亥 庚子"
            '丁', '壬' -> "辛丑 壬寅 癸卯 甲辰 乙巳 丙午 丁未 戊申 己酉 庚戌 辛亥 壬子"
            '戊', '癸' -> "癸丑 甲寅 乙卯 丙辰 丁巳 戊午 己未 庚申 辛酉 壬戌 癸亥 甲子"
            else -> ""
        }
    }

    fun getMonthGanji(char: Char, month: Int): String {
        val monthGanjiList = getMonthGanjiList(char).split(" ")
        return monthGanjiList[month - 1]
    }

    fun getDayGanji(year: Int, month: Int, day: Int): String {
        val date = Calendar.getInstance().apply { set(1900, 0, 1) }
        var sibganIdx = 0
        var sibijiIdx = 10

        while (true) {
            if (date[Calendar.YEAR] == year && date[Calendar.MONTH] == month - 1 && date[Calendar.DAY_OF_MONTH] == day) {
                break
            }
            date.add(Calendar.DATE, 1)
            sibganIdx = (sibganIdx + 1) % 10
            sibijiIdx = (sibijiIdx + 1) % 12
        }

        return sibgan[0][sibganIdx] + sibiji[0][sibijiIdx]
    }

    fun isLeapYear(year: Int) = if (year % 4 == 0) {
        if (year % 100 == 0) {
            year % 400 == 0
        } else {
            true
        }
    } else {
        false
    }


    fun getTimeGanji(day: Char, hour: Int): String {
        val dayIdx = when (day) {
            '甲', '己' -> 0
            '乙', '庚' -> 1
            '丙', '辛' -> 2
            '丁', '壬' -> 3
            '戊', '癸' -> 4
            else -> -1
        }

        val timeIdx = when (hour) {
            in 1 until 3 -> 1
            in 3 until 5 -> 2
            in 5 until 7 -> 3
            in 7 until 9 -> 4
            in 9 until 11 -> 5
            in 11 until 13 -> 6
            in 13 until 15 -> 7
            in 15 until 17 -> 8
            in 17 until 19 -> 9
            in 19 until 21 -> 10
            in 21 until 23 -> 11
            else -> 0
        }

        return if (dayIdx == -1) "" else timeGanji[dayIdx][timeIdx]
    }

    // 오행
    fun getFiveProperty(ganji: String): String {
        return when (ganji) {
            "甲子", "乙丑" -> "海中金\n(해중금)"
            "甲戌", "乙亥" -> "山頭火\n(산두화)"
            "甲申", "乙酉" -> "泉中水\n(천중수)"
            "甲午", "乙未" -> "砂中金\n(사중금)"
            "甲辰", "乙巳" -> "覆燈火\n(복등화)"
            "甲寅", "乙卯" -> "大溪水\n(대계수)"
            "丙寅", "丁卯" -> "爐中火\n(노중화)"
            "丙子", "丁丑" -> "澗下水\n(간하수)"
            "丙戌", "丁亥" -> "屋上土\n(옥상토)"
            "丙申", "丁酉" -> "山下火\n(산하화)"
            "丙午", "丁未" -> "天河水\n(천하수)"
            "丙辰", "丁巳" -> "沙中土\n(사중토)"
            "戊辰", "己巳" -> "大林木\n(대림목)"
            "戊寅", "己卯" -> "城頭土\n(성두토)"
            "戊子", "己丑" -> "霹靂火\n(벽력화)"
            "戊戌", "己亥" -> "平地木\n(평지목)"
            "戊申", "己酉" -> "大驛土\n(대역토)"
            "戊午", "己未" -> "天上火\n(천상화)"
            "庚午", "辛未" -> "路傍土\n(노방토)"
            "庚辰", "辛巳" -> "白蠟金\n(백랍금)"
            "庚寅", "辛卯" -> "松柏木\n(송백목)"
            "庚子", "辛丑" -> "壁上土\n(벽상토)"
            "庚戌", "辛亥" -> "釵釧金\n(채천금)"
            "庚申", "辛酉" -> "石榴木\n(석류목)"
            "壬申", "癸酉" -> "劍鋒金\n(검봉금)"
            "壬午", "癸未" -> "楊柳木\n(양류목)"
            "壬辰", "癸巳" -> "長流水\n(장류수)"
            "壬寅", "癸卯" -> "金箔金\n(금박금)"
            "壬子", "癸丑" -> "桑柘木\n(상자목)"
            "壬戌", "癸亥" -> "大海水\n(대해수)"
            else -> ""
        }
    }


    val timeGanji = arrayOf(
//        시(時)  23~01 01~03 03~05 05~07 07~09 09~11 11~13 13~15 15~17 17~19 19~21 21~23
//    갑(甲), 기(己)일
        arrayOf(
            "甲子",
            "乙丑",
            "丙寅",
            "丁卯",
            "戊辰",
            "己巳",
            "庚午",
            "辛未",
            "壬申",
            "癸酉",
            "甲戌",
            "乙亥"
        ),
//    을(乙), 경(庚)일
        arrayOf(
            "丙子",
            "丁丑",
            "戊寅",
            "己卯",
            "庚辰",
            "辛巳",
            "壬午",
            "癸未",
            "甲申",
            "乙酉",
            "丙戌",
            "丁亥"
        ),
//    병(丙), 신(辛)일
        arrayOf(
            "戊子",
            "己丑",
            "庚寅",
            "辛卯",
            "壬辰",
            "癸巳",
            "甲午",
            "乙未",
            "丙申",
            "丁酉",
            "戊戌",
            "己亥"
        ),
//    정(丁), 임(壬)일
        arrayOf(
            "庚子",
            "辛丑",
            "壬寅",
            "癸卯",
            "甲辰",
            "乙巳",
            "丙午",
            "丁未",
            "戊申",
            "己酉",
            "庚戌",
            "辛亥"
        ),
//    무(戊), 계(癸)일
        arrayOf(
            "壬子",
            "癸丑",
            "甲寅",
            "乙卯",
            "丙辰",
            "丁巳",
            "戊午",
            "己未",
            "庚申",
            "辛酉",
            "壬戌",
            "癸亥"
        )
    )

    // 지지암장간
    fun getJijiAmJangan(char: Char): String {
        return when (char) {
            '子' -> "壬癸"
            '丑' -> "癸辛己"
            '寅' -> "戊丙甲"
            '卯' -> "甲乙"
            '辰' -> "乙癸戊"
            '巳' -> "戊庚丙"
            '午' -> "丙己丁"
            '未' -> "丁己"
            '申' -> "戊壬庚"
            '酉' -> "庚辛"
            '戌' -> "辛丁戊"
            '亥' -> "戊甲壬"
            else -> ""
        }
    }

    // 양음
    fun getSign(char: Char): Int {
        return if (char in "甲丙戊庚壬") 1 else -1
    }

    // 십이운성
    fun getTwelveShootingStar(day: Char, twelveGod: Char): String {

        val dayIdx =
            arrayOf('甲', '乙', '丙', '丁', '戊', '己', '庚', '辛', '壬', '癸').indexOf(day)
        val twelveGodIdx =
            arrayOf('亥', '子', '丑', '寅', '卯', '辰', '巳', '午', '未', '申', '酉', '戌').indexOf(
                twelveGod
            )

        return arrayOf(
            arrayOf(
                "生(생)",
                "死(사)",
                "絶(절)",
                "胎(태)",
                "絶(절)",
                "胎(태)",
                "病(병)",
                "浴(욕)",
                "官(관)",
                "旺(왕)",
            ),
            arrayOf(
                "浴(욕)",
                "病(병)",
                "胎(태)",
                "絶(절)",
                "胎(태)",
                "絶(절)",
                "死(사)",
                "生(생)",
                "旺(왕)",
                "官(관)",
            ),
            arrayOf(
                "帶(대)",
                "衰(쇠)",
                "養(양)",
                "墓(묘)",
                "養(양)",
                "墓(묘)",
                "墓(묘)",
                "養(양)",
                "衰(쇠)",
                "帶(대)",
            ),
            arrayOf(
                "官(관)",
                "旺(왕)",
                "生(생)",
                "死(사)",
                "生(생)",
                "死(사)",
                "絶(절)",
                "胎(태)",
                "病(병)",
                "浴(욕)",
            ),
            arrayOf(
                "旺(왕)",
                "官(관)",
                "浴(욕)",
                "病(병)",
                "浴(욕)",
                "病(병)",
                "胎(태)",
                "絶(절)",
                "死(사)",
                "生(생)",
            ),
            arrayOf(
                "衰(쇠)",
                "帶(대)",
                "帶(대)",
                "衰(쇠)",
                "帶(대)",
                "衰(쇠)",
                "養(양)",
                "墓(묘)",
                "墓(묘)",
                "養(양)",
            ),
            arrayOf(
                "病(병)",
                "浴(욕)",
                "官(관)",
                "旺(왕)",
                "官(관)",
                "旺(왕)",
                "生(생)",
                "死(사)",
                "絶(절)",
                "胎(태)",
            ),
            arrayOf(
                "死(사)",
                "生(생)",
                "旺(왕)",
                "官(관)",
                "旺(왕)",
                "官(관)",
                "浴(욕)",
                "病(병)",
                "胎(태)",
                "絶(절)",
            ),
            arrayOf(
                "墓(묘)",
                "養(양)",
                "衰(쇠)",
                "帶(대)",
                "衰(쇠)",
                "帶(대)",
                "帶(대)",
                "衰(쇠)",
                "養(양)",
                "墓(묘)",
            ),
            arrayOf(
                "絶(절)",
                "胎(태)",
                "病(병)",
                "浴(욕)",
                "病(병)",
                "浴(욕)",
                "官(관)",
                "旺(왕)",
                "生(생)",
                "死(사)",
            ),
            arrayOf(
                "胎(태)",
                "絶(절)",
                "死(사)",
                "生(생)",
                "死(사)",
                "生(생)",
                "旺(왕)",
                "官(관)",
                "浴(욕)",
                "病(병)",
            ),
            arrayOf(
                "養(양)",
                "墓(묘)",
                "墓(묘)",
                "養(양)",
                "墓(묘)",
                "養(양)",
                "衰(쇠)",
                "帶(대)",
                "帶(대)",
                "衰(쇠)",
            ),
        )[twelveGodIdx][dayIdx]
    }


    /***
     * 음력날짜를 양력날짜로 변환* @param 음력날짜 (yyyyMMdd)* @return 양력날짜 (yyyyMMdd)
     * */
    fun convertLunarToSolar(date: String): Long {
        val nDate = date.replace("-", "")
        val cc = ChineseCalendar()
        val cal = Calendar.getInstance()
        cc[ChineseCalendar.EXTENDED_YEAR] = nDate.substring(0, 4).toInt() + 2637
        cc[ChineseCalendar.MONTH] = nDate.substring(4, 6).toInt() - 1
        cc[ChineseCalendar.DAY_OF_MONTH] = nDate.substring(6).toInt()
        cal.timeInMillis = cc.timeInMillis
        return cal.timeInMillis
    }

    fun convertLunarToSolar(year: Int, month: Int, day: Int): Calendar {
        val cc = ChineseCalendar()
        val cal = Calendar.getInstance()
        cc[ChineseCalendar.EXTENDED_YEAR] = year + 2637
        cc[ChineseCalendar.MONTH] = month - 1
        cc[ChineseCalendar.DAY_OF_MONTH] = day
        cal.timeInMillis = cc.timeInMillis
        return cal
    }

    /***
     * 양력날짜를 음력날짜로 변환* @param 양력날짜 (yyyyMMdd)* @return 음력날짜 (yyyyMMdd)
     * */
    fun convertSolarToLunar(date: Calendar): Long {
        val cc = ChineseCalendar()
        val cal = Calendar.getInstance()
        cal[Calendar.YEAR] = date[Calendar.YEAR]
        cal[Calendar.MONTH] = date[Calendar.MONTH]
        cal[Calendar.DAY_OF_MONTH] = date[Calendar.DAY_OF_MONTH]
        cc.timeInMillis = cal.timeInMillis
        val y = cc[ChineseCalendar.EXTENDED_YEAR] - 2637
        val m = cc[ChineseCalendar.MONTH] + 1
        val d = cc[ChineseCalendar.DAY_OF_MONTH]
        val ret = StringBuffer()
        ret.append(String.format("%04d", y)).append("-")
        ret.append(String.format("%02d", m)).append("-")
        ret.append(String.format("%02d", d))
        return dateSlideFormat.parse(ret.toString())!!.time
    }

    fun convertSolarToLunarCalendar(date: Calendar): Calendar {
        val cc = ChineseCalendar()
        val cal = Calendar.getInstance()
        cal[Calendar.YEAR] = date[Calendar.YEAR]
        cal[Calendar.MONTH] = date[Calendar.MONTH]
        cal[Calendar.DAY_OF_MONTH] = date[Calendar.DAY_OF_MONTH]
        cc.timeInMillis = cal.timeInMillis
        val y = cc[ChineseCalendar.EXTENDED_YEAR] - 2637
        val m = cc[ChineseCalendar.MONTH] + 1
        val d = cc[ChineseCalendar.DAY_OF_MONTH]

        val ret = Calendar.getInstance()
        ret[Calendar.YEAR] = y
        ret[Calendar.MONTH] = m - 1
        ret[Calendar.DAY_OF_MONTH] = d
        ret[Calendar.HOUR_OF_DAY] = date[Calendar.HOUR_OF_DAY]
        ret[Calendar.MINUTE] = date[Calendar.MINUTE]

        return ret
    }

    // 층과 각도가 주어짐
    fun calcBearing(layer: Int, degree: Int): String? {
        return when (layer) {
            // 층수가 0일 경우
            0 -> when (degree) {
                // 각도가 0 이상 60 미만
                in 0 until 60 -> "寅"
                // 각도가 60 이상 120 미만
                in 60 until 120 -> "酉"
                // 각도가 120 이상 180 미만
                in 120 until 180 -> "亥"
                // 각도가 180 이상 240 미만
                in 180 until 240 -> "卯"
                // 각도가 240 이상 300 미만
                in 240 until 300 -> "巳"
                // 그 밖의 범위. 300 이상
                else -> "午"
            }

            1 -> when (degree) {
                in 0 until 10 -> "艮"
                in 10 until 20 -> "" // 빈 칸일 경우
                in 20 until 30 -> "申癸"
                in 30 until 40 -> ""
                in 40 until 50 -> "艮"

                in 340 until 350 -> "壬申"
                else -> "乾"
            }

            2 -> when (degree) {
                in 100 until 200 -> "火"
                in 200 until 300 -> "金"
                // 만약 300 ~ 360(0) ~ 100 이라면
                else -> "水"
            }

            else -> ""
        }
    }


}