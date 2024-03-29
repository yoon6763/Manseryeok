package com.bikulwon.manseryeok.page.calendarname

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bikulwon.manseryeok.R
import com.bikulwon.manseryeok.utils.Utils
import com.bikulwon.manseryeok.adapter.LocationAdapter
import com.bikulwon.manseryeok.databinding.ActivityCalendarInputBinding
import com.bikulwon.manseryeok.models.AppDatabase
import com.bikulwon.manseryeok.models.UserInputViewModel
import com.bikulwon.manseryeok.models.user.User
import com.bikulwon.manseryeok.page.calendarname.popup.LocationPickerFragment
import com.bikulwon.manseryeok.utils.Extras
import com.bikulwon.manseryeok.utils.ParentActivity
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class CalendarInputActivity : ParentActivity() {

    enum class NextPageType { NAME, CALENDAR }

    private lateinit var userInputViewModel: UserInputViewModel
    private lateinit var binding: ActivityCalendarInputBinding

    private val userDao by lazy { AppDatabase.getInstance(applicationContext).userDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userInputViewModel = ViewModelProvider(this)[UserInputViewModel::class.java]
        binding = DataBindingUtil.setContentView<ActivityCalendarInputBinding?>(
            this,
            R.layout.activity_calendar_input
        ).apply {
            lifecycleOwner = this@CalendarInputActivity
            viewModel = userInputViewModel
        }

        binding.run {
            etInputBirthPlace.setOnClickListener { openBirthPlacePicker() }
            btnNameInputFinish.setOnClickListener { nextPage(NextPageType.NAME) }
            btnCalenderInputFinish.setOnClickListener { nextPage(NextPageType.CALENDAR) }
            btnEditFinish.setOnClickListener { saveUpdatedUser() }
        }

        toolbarSetting()
        setMode()
    }

    private fun saveUpdatedUser() {
        if (!userInputViewModel.isValid(applicationContext)) return

        val userId = intent.getLongExtra(Extras.INTENT_EXTRAS_USER_ID, -1)
        val user = getUserFromDB(userId)

        if(user == null) {
            showShortToast("유저 정보를 불러오는데 실패했습니다")
            finish()
        }

        user?.updateFromViewModel(userInputViewModel)

        runBlocking {
            launch(IO) {
                userDao.update(user!!)
            }
        }

        setResult(RESULT_OK)
        finish()
    }

    private fun openBirthPlacePicker() {
        val locationPickerFragment = LocationPickerFragment()

        locationPickerFragment.onLocationClickListener =
            object : LocationAdapter.OnLocationClickListener {
                override fun onLocationClick(location: String, timeDiff: Int) {
                    userInputViewModel.birthPlace.value = location
                    userInputViewModel.timeDiff.value = timeDiff
                    userInputViewModel.birthPlaceLabel.value = "$location (${timeDiff}분)"
                    locationPickerFragment.dismiss()
                }
            }

        locationPickerFragment.show(supportFragmentManager, "LocationPicker")
    }

    private fun setMode() {
        when (intent.getStringExtra(Extras.INTENT_EXTRAS_INFO_TYPE)) {
            Utils.InfoType.CREATE.value -> {
                binding.containerEdit.visibility = View.GONE
            }

            Utils.InfoType.EDIT.value -> {
                binding.containerNextPage.visibility = View.GONE
                initEditMode()
            }
        }
    }

    private fun initEditMode() {
        val userId = intent.getLongExtra(Extras.INTENT_EXTRAS_USER_ID, -1)
        val user = getUserFromDB(userId)

        if (user == null) {
            showShortToast("유저 정보를 불러오는데 실패했습니다")
            finish()
        }

        userInputViewModel.updateViewModel(user!!)
    }

    private fun toolbarSetting() {
        setSupportActionBar(binding.toolbarCalendarInput)
        supportActionBar?.run {
            // 앱 바 뒤로가기 버튼 설정
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun nextPage(type: NextPageType) = with(binding) {
        if (!viewModel!!.isValid(applicationContext)) return@with
        val user = viewModel!!.toUserEntity()

        runBlocking {
            launch(IO) {
                val savedUserId = userDao.insert(user)
                user.userId = savedUserId
            }
        }

        val intent = when (type) {
            NextPageType.NAME -> Intent(this@CalendarInputActivity, NameActivity::class.java)
            NextPageType.CALENDAR -> Intent(
                this@CalendarInputActivity,
                CalendarActivity::class.java
            )
        }

        intent.putExtra(Extras.INTENT_EXTRAS_USER_ID, user.userId)
        startActivity(intent)
        finish()
    }

    private fun getUserFromDB(userId: Long): User? {
        var user: User? = null
        runBlocking {
            launch(IO) {
                user = userDao.getUser(userId)
            }
        }
        return user
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