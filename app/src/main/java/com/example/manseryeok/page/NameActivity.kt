package com.example.manseryeok.page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.manseryeok.utils.Utils
import com.example.manseryeok.adapter.ManseryeokSQLAdapter
import com.example.manseryeok.adapter.NameScoreAdapter
import com.example.manseryeok.databinding.ActivityNameBinding
import com.example.manseryeok.models.NameScoreItem
import com.example.manseryeok.models.User
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class NameActivity : AppCompatActivity() {
    private val TAG = "NameActivity"

    private lateinit var userModel: User // 유저의 정보 DTO

    private val binding by lazy { ActivityNameBinding.inflate(layoutInflater) }

    private var yearGanji = ""
    private var monthGanji = ""

    private val nameItems = ArrayList<NameScoreItem>()
    private val nameAdapter by lazy { NameScoreAdapter(this@NameActivity, nameItems) }

    private var name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarName)
        supportActionBar?.run {
            // 앱 바 뒤로가기 버튼 설정
            setDisplayHomeAsUpEnabled(true)
        }

        userModel = intent.getParcelableExtra<User>(Utils.INTENT_EXTRAS_USER)!!
        name = userModel.firstName + userModel.lastName

        binding.run {
            rvNameScore.adapter = nameAdapter
        }

        importGanji()
        setUpGanji()
    }

    private fun setUpGanji() {
        binding.run {
            tvYearGanjiTop.text = yearGanji[0].toString()
            tvYearGanjiBottom.text = yearGanji[1].toString()
            tvMonthGanjiTop.text = monthGanji[0].toString()
            tvMonthGanjiBottom.text = monthGanji[1].toString()
        }


/*
        한글 유니코드 규칙 - (초성 * 21 + 중성) * 28 + 종성 + 0xAC00
        전 -> ㅈ : 12, ㅓ : 4 ㄴ : 5
        (12 * 21 + 4) * 28 + 5 + 0xAC00
*/

        // 초성
        val initialSound = arrayOf(
            'ㄱ',
            'ㄲ',
            'ㄴ',
            'ㄷ',
            'ㄸ',
            'ㄹ',
            'ㅁ',
            'ㅂ',
            'ㅃ',
            'ㅅ',
            'ㅆ',
            'ㅇ',
            'ㅈ',
            'ㅉ',
            'ㅊ',
            'ㅋ',
            'ㅌ',
            'ㅍ',
            'ㅎ'
        )

        // 중성
        val middleSound = arrayOf(
            'ㅏ',
            'ㅐ',
            'ㅑ',
            'ㅒ',
            'ㅓ',
            'ㅔ',
            'ㅕ',
            'ㅖ',
            'ㅗ',
            'ㅘ',
            'ㅙ',
            'ㅚ',
            'ㅛ',
            'ㅜ',
            'ㅝ',
            'ㅞ',
            'ㅟ',
            'ㅠ',
            'ㅡ',
            'ㅢ',
            'ㅣ'
        )

        // 종성, 종성이 없는 경우도 있기에 "" 값 포함
        val finalSound = arrayOf(
            ' ',
            'ㄱ',
            'ㄲ',
            'ㄳ',
            'ㄴ',
            'ㄵ',
            'ㄶ',
            'ㄷ',
            'ㄹ',
            'ㄺ',
            'ㄻ',
            'ㄼ',
            'ㄽ',
            'ㄾ',
            'ㄿ',
            'ㅀ',
            'ㅁ',
            'ㅂ',
            'ㅄ',
            'ㅅ',
            'ㅆ',
            'ㅇ',
            'ㅈ',
            'ㅊ',
            'ㅋ',
            'ㅌ',
            'ㅍ',
            'ㅎ'
        )

        val scoreHashMap = HashMap<Char, Int>()
        scoreHashMap[' '] = 0
        scoreHashMap['ㄱ'] = 2
        scoreHashMap['ㄲ'] = 4
        scoreHashMap['ㄴ'] = 2
        scoreHashMap['ㄷ'] = 3
        scoreHashMap['ㄸ'] = 6
        scoreHashMap['ㄹ'] = 5
        scoreHashMap['ㅁ'] = 4
        scoreHashMap['ㅂ'] = 4
        scoreHashMap['ㅃ'] = 8
        scoreHashMap['ㅅ'] = 2
        scoreHashMap['ㅆ'] = 4
        scoreHashMap['ㅇ'] = 1
        scoreHashMap['ㅈ'] = 3
        scoreHashMap['ㅉ'] = 6
        scoreHashMap['ㅊ'] = 4
        scoreHashMap['ㅋ'] = 3
        scoreHashMap['ㅌ'] = 4
        scoreHashMap['ㅍ'] = 4
        scoreHashMap['ㅎ'] = 3
        scoreHashMap['ㄳ'] = 4
        scoreHashMap['ㄵ'] = 5
        scoreHashMap['ㄶ'] = 5
        scoreHashMap['ㄺ'] = 7
        scoreHashMap['ㄻ'] = 9
        scoreHashMap['ㄼ'] = 9
        scoreHashMap['ㄽ'] = 7
        scoreHashMap['ㄾ'] = 9
        scoreHashMap['ㄿ'] = 9
        scoreHashMap['ㅀ'] = 8
        scoreHashMap['ㅄ'] = 6
        scoreHashMap['ㅏ'] = 2
        scoreHashMap['ㅐ'] = 3
        scoreHashMap['ㅑ'] = 3
        scoreHashMap['ㅒ'] = 4
        scoreHashMap['ㅓ'] = 2
        scoreHashMap['ㅔ'] = 3
        scoreHashMap['ㅕ'] = 3
        scoreHashMap['ㅖ'] = 4
        scoreHashMap['ㅗ'] = 2
        scoreHashMap['ㅘ'] = 4
        scoreHashMap['ㅙ'] = 5
        scoreHashMap['ㅚ'] = 3
        scoreHashMap['ㅛ'] = 3
        scoreHashMap['ㅜ'] = 2
        scoreHashMap['ㅝ'] = 4
        scoreHashMap['ㅞ'] = 5
        scoreHashMap['ㅟ'] = 3
        scoreHashMap['ㅠ'] = 3
        scoreHashMap['ㅡ'] = 1
        scoreHashMap['ㅢ'] = 2
        scoreHashMap['ㅣ'] = 1

        nameItems.clear()

        for (element in name) {
            if (element.code !in 0xAC00..0xD79F) {
                Toast.makeText(applicationContext, "한글이 아닙니다.", Toast.LENGTH_SHORT).show()
                continue
            }

            val uniVal = element - 0xAC00

            val initialVal = initialSound[(uniVal.code / 28 / 21)] // 초성
            val middleVal = middleSound[(uniVal.code / 28 % 21)] // 중성
            val finalVal = finalSound[(uniVal.code % 28)] // 종성 (받침)

            // result += "$element" + "\n" +
            //   "$initialVal : ${scoreHashMap[initialVal]}\t $middleVal : ${scoreHashMap[middleVal]}\t $finalVal : ${scoreHashMap[finalVal]}" + "\n\n"

            val property = when (initialVal) {
                'ㄱ', 'ㅋ' -> 0 // 목
                'ㄴ', 'ㄷ', 'ㄹ', 'ㅌ' -> 1 // 화
                'ㅇ', 'ㅎ' -> 2 // 토
                'ㅅ', 'ㅈ', 'ㅊ' -> 3 // 금
                'ㅁ', 'ㅂ', 'ㅍ' -> 4 // 수
                else -> -1
            }

            // 홀수면 +(true) 양수면 -(false)
            val sign =
                (scoreHashMap[initialVal]!! + scoreHashMap[middleVal]!! + scoreHashMap[finalVal]!!) % 2 != 0

            val ganji = when (property) {
                0 -> if (sign) '甲' else '乙'
                1 -> if (sign) '丙' else '丁'
                2 -> if (sign) '戊' else '己'
                3 -> if (sign) '庚' else '辛'
                4 -> if (sign) '壬' else '癸'
                else -> ' '
            }

            val ganjiYearTopLabel = Utils.getPillarLabel(
                ganji.toString(),
                yearGanji[0].toString()
            )
            val ganjiYearBottomLabel = Utils.getPillarLabel(
                ganji.toString(),
                yearGanji[1].toString()
            )
            val ganjiMonthTopLabel = Utils.getPillarLabel(
                ganji.toString(),
                monthGanji[0].toString()
            )
            val ganjiMonthBottomLabel = Utils.getPillarLabel(
                ganji.toString(),
                monthGanji[1].toString()
            )

            nameItems.add(
                NameScoreItem(
                    element.toString(),
                    ganji.toString(),
                    ganjiYearTopLabel,
                    ganjiYearBottomLabel,
                    ganjiMonthTopLabel,
                    ganjiMonthBottomLabel
                )
            )
        }

        nameAdapter.notifyDataSetChanged()
    }

    private fun importGanji() {
        val mDBHelper = ManseryeokSQLAdapter(applicationContext)
        mDBHelper.createDataBase()
        mDBHelper.open()

        val year = userModel.birth!!.substring(0, 4).toInt()
        val month = userModel.birth!!.substring(4, 6).toInt()
        val day = userModel.birth!!.substring(6, 8).toInt()

        // 유저의 생일 - 1년 부터 + 100년까지의 정보
        val userManseryeok = mDBHelper.getDayData(year, month, day)

        mDBHelper.close()

        yearGanji = userManseryeok.cd_hyganjee!!
        monthGanji = userManseryeok.cd_hmganjee!!

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 앱 바 클릭 이벤트
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}