package com.bikulwon.manseryeok.page.compass

import android.Manifest
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowInsetsController
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.bikulwon.manseryeok.R
import com.bikulwon.manseryeok.adapter.MapLayerListAdapter
import com.bikulwon.manseryeok.compassutils.CompassDirectionLabel
import com.bikulwon.manseryeok.databinding.ActivityCompassBinding
import com.bikulwon.manseryeok.models.AppDatabase
import com.bikulwon.manseryeok.models.user.User
import com.bikulwon.manseryeok.utils.ParentActivity
import com.bikulwon.manseryeok.utils.SharedPreferenceHelper
import com.bikulwon.manseryeok.utils.Utils
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class CompassActivity : ParentActivity(), SensorEventListener, OnMapReadyCallback {
    private val TAG = "CompassActivity"
    private val locationManager by lazy { getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    private val gpsListener = GPSListener()
    private val binding by lazy { ActivityCompassBinding.inflate(layoutInflater) }
    private val fm by lazy { supportFragmentManager }
    private val userDao by lazy { AppDatabase.getInstance(applicationContext).userDao() }
    private var userSelectedIndex = -1
    private val usernames = ArrayList<String>()
    private lateinit var users: List<User>
    private var currentPlaceInfoToastTime = 0L

    private lateinit var mapFragment: MapFragment
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private lateinit var btnLocationSource: FusedLocationSource
    private lateinit var mSensorManager: SensorManager
    private lateinit var mAccelerometer: Sensor
    private lateinit var mMagnetometer: Sensor
    private lateinit var mapLayerListAdapter: MapLayerListAdapter

    private var mR = FloatArray(9)
    private var mLastAccelerometer = FloatArray(3)
    private var mLastMagnetometer = FloatArray(3)
    private var mLastAccelerometerSet = false
    private var mLastMagnetometerSet = false
    private var mOrientation = FloatArray(3)
    private var mCurrentDegree = 0f
    private var mapIsReady = false
    private var isRotationFixed = false


    private val permissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
        }

        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            Toast.makeText(
                this@CompassActivity,
                getString(R.string.msg_permission_denied),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // android status bar color
        window.statusBarColor = getColor(R.color.navy)
        // set status bar icon color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }


        val tempList = ArrayList<String>()
        for (i in 0..16) {
            tempList.add("test")
        }

        mapLayerListAdapter = MapLayerListAdapter(this, tempList)

        mapLayerListAdapter.degree = mCurrentDegree
        mapLayerListAdapter.notifyDataSetChanged()

        binding.run {
            rvCompassInfo.adapter = mapLayerListAdapter
            rvCompassInfo.layoutManager = GridLayoutManager(this@CompassActivity, 3)

            rgInfoType.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.rb_compass_nagyeong -> {
                        rvCompassInfo.visibility = View.VISIBLE
                        llSinsal.visibility = View.INVISIBLE
                    }

                    R.id.rb_compass_sinsal -> {
                        rvCompassInfo.visibility = View.INVISIBLE
                        llSinsal.visibility = View.VISIBLE
                    }
                }
            }
        }

        checkPermission()
        mapInitialize()

        binding.btnCompassQuestion.setOnClickListener {
            val helpFragment = CompassHelpFragment.newInstance()
            helpFragment.show(supportFragmentManager, "HelpDialog")
        }

        binding.rgRotation.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                binding.rbRotation.id -> isRotationFixed = false
                binding.rbRotationFix.id -> isRotationFixed = true
            }
        }

        binding.ivClose.setOnClickListener {
            binding.flCompassInfo.visibility = View.GONE
            binding.btnCompassInfo.visibility = View.VISIBLE
        }

        binding.btnCompassInfo.setOnClickListener {
            binding.flCompassInfo.visibility = View.VISIBLE
            it.visibility = View.GONE
        }

        userLoad()
        binding.btnSelectFromDb.setOnClickListener {
            if (users.isEmpty()) {
                showShortToast("저장된 정보가 없습니다.")
                return@setOnClickListener
            }

            val builder = AlertDialog.Builder(this@CompassActivity).apply {
                title = "불러올 사람을 선택하세요"
                setItems(usernames.toTypedArray()) { dialog, index ->
                    if (index == -1) return@setItems
                    userSelectedIndex = index
                }
            }.create()

            builder.show()
        }
    }

    private fun setSatelliteMap() {
        val initialType = SharedPreferenceHelper.isSatelliteMapEnable(this)
        // satellite map
        if (initialType) {
            naverMap.mapType = NaverMap.MapType.Hybrid
            binding.btnMapTypeChange.setImageResource(R.drawable.ic_satellite_picture)

            // 각 건물 정보등이 보이도록
            naverMap.uiSettings.isIndoorLevelPickerEnabled = true
            naverMap.isIndoorEnabled = true

            // 각 건물 이름 등이 보이도록

        } else {
            naverMap.mapType = NaverMap.MapType.Basic
            binding.btnMapTypeChange.setImageResource(R.drawable.ic_satellite)
        }

        binding.btnMapTypeChange.setOnClickListener {
            if (SharedPreferenceHelper.isSatelliteMapEnable(this)) {
                naverMap.mapType = NaverMap.MapType.Basic
                SharedPreferenceHelper.setSatelliteMapEnable(this, false)
                binding.btnMapTypeChange.setImageResource(R.drawable.ic_satellite)
            } else {
                naverMap.mapType = NaverMap.MapType.Hybrid
                SharedPreferenceHelper.setSatelliteMapEnable(this, true)
                binding.btnMapTypeChange.setImageResource(R.drawable.ic_satellite_picture)
            }
        }
    }

    private fun userLoad() {
        runBlocking {
            launch(IO) {
                users = userDao.getAllUser()
            }
        }

        users.forEach { user ->
            var usernameLabel = user.name!!
            usernameLabel += " (${user.birthYear})"
            usernames.add(usernameLabel)
        }
    }

    private fun mapInitialize() {
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)!!

        mapFragment =
            fm.findFragmentById(R.id.frag_map) as MapFragment? ?: MapFragment.newInstance()
                .also { fm.beginTransaction().add(R.id.frag_map, it).commit() }

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        mapFragment.getMapAsync(this)

        binding.btnSearch.setOnClickListener {
            val mapSearchFragment = MapSearchFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .add(R.id.container_search, mapSearchFragment)
                .commit()

            mapSearchFragment.onSearchButtonClickListener = object : MapSearchFragment.OnSearchButtonClickListener {
                override fun onSearchButtonClick(lat: Double, lng: Double) {
                    supportFragmentManager.beginTransaction().remove(mapSearchFragment).commit()
                    binding.ivCompass.focusable = View.FOCUSABLE

                    val manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    manager.hideSoftInputFromWindow(
                        currentFocus!!.windowToken,
                        InputMethodManager.HIDE_NOT_ALWAYS
                    )

                    naverMap.moveCamera(CameraUpdate.scrollTo(LatLng(lat, lng)))
                }
            }


//            btnLocationSource =
//                FusedLocationSource(this@CompassActivity, LOCATION_PERMISSION_REQUEST_CODE)
//
//            naverMap.locationSource = btnLocationSource
//
//            if (locationSource.lastLocation != null) {
//                val cameraPosition = CameraPosition(
//                    LatLng(
//                        locationSource.lastLocation!!.latitude,
//                        locationSource.lastLocation!!.longitude
//                    ),
//                    naverMap.cameraPosition.zoom,
//                    naverMap.cameraPosition.tilt,
//                    naverMap.cameraPosition.bearing
//                )
//
//                naverMap.cameraPosition = cameraPosition
//            }
        }

    }

    private fun checkPermission() {
        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setRationaleMessage("지도 기능 사용을 위해서는 GPS 및 위치 접근 권한이 필요합니다")
            .setDeniedMessage("[설정] > [권한] 에서 권한 허용을 할 수 있습니다")
            .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION)
            .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
            .check()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(p0: NaverMap) {
        naverMap = p0
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
        naverMap.uiSettings.isZoomControlEnabled = false
        naverMap.uiSettings.isLocationButtonEnabled = true

        mapIsReady = true
        setSatelliteMap()

        naverMap.addOnCameraChangeListener { i, b ->
            if (isRotationFixed) {

                // 네이버 지도 각도 받아오기
                val rotation = naverMap.cameraPosition.bearing

                binding.tvCompassDegree.text = "${Utils.degreeFormat.format(rotation)}°"

                mapLayerListAdapter.degree = rotation.toFloat()
                mapLayerListAdapter.notifyDataSetChanged()

                updateSinsalBangwhi(rotation)
            }
        }
    }

    private fun updateSinsalBangwhi(rotation: Double) = with(binding) {
        if (userSelectedIndex == -1) return

        val user = users[userSelectedIndex]

        val birthYear = user.birthYear

        tvCompassSatek.visibility = View.VISIBLE
        tvCompassSatek.text = "${CompassDirectionLabel.huiduguk(birthYear)}\n${
            CompassDirectionLabel.bonmyeonggung(birthYear)[user.gender]
        }"
        tvUserName.text = "${user.name} (${user.birthYear})"

        val sinsal = CompassDirectionLabel.directionSinsal(birthYear, rotation.toInt())
        val content = CompassDirectionLabel.directionSinsalTheory(sinsal)

        tvSinsalContent.text = content[0]

    }

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME)
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(this, mAccelerometer)
        mSensorManager.unregisterListener(this, mMagnetometer)
    }

    private var currentTime = 0L

    override fun onSensorChanged(event: SensorEvent) {
//        val nowTime = System.currentTimeMillis()
//        if (nowTime - currentTime < 5) {
//            return
//        }
//        currentTime = nowTime


        if (isRotationFixed) {
            val rotation = -naverMap.cameraPosition.bearing.toInt().toFloat()
            binding.ivCompass.rotation = rotation
            return
        }

        if (mapIsReady) {

            if (event.sensor == mAccelerometer) {
                System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.size)
                mLastAccelerometerSet = true
            } else if (event.sensor == mMagnetometer) {
                System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.size)
                mLastMagnetometerSet = true
            }
            if (mLastAccelerometerSet && mLastMagnetometerSet) {
                SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer)

                var azimuthinDegress = ((Math.toDegrees(
                    SensorManager.getOrientation(
                        mR,
                        mOrientation
                    )[0].toDouble()
                ) + 360) % 360).toFloat()
                azimuthinDegress = (azimuthinDegress * 10).toInt().toFloat() / 10

//                val ra = RotateAnimation(
//                    mCurrentDegree,
//                    -azimuthinDegress,
//                    Animation.RELATIVE_TO_SELF, 0.5f,
//                    Animation.RELATIVE_TO_SELF, 0.5f
//                )
//                ra.duration = 500
//                ra.fillAfter = true
//                binding.ivCompass.startAnimation(ra)

                binding.tvCompassDegree.text = "${Utils.degreeFormat.format(azimuthinDegress)}°"

                mapLayerListAdapter.degree = azimuthinDegress
                mapLayerListAdapter.notifyDataSetChanged()

                binding.ivCompass.rotation = -azimuthinDegress


                // target, zoom, tilt, bearing
                val cameraPosition = CameraPosition(
                    naverMap.cameraPosition.target,
                    naverMap.cameraPosition.zoom,
                    naverMap.cameraPosition.tilt,
                    azimuthinDegress.toDouble()
                )

                val camera = CameraUpdate.toCameraPosition(cameraPosition)
                    .animate(CameraAnimation.Easing, 500)
                naverMap.moveCamera(camera)
                //naverMap.cameraPosition = cameraPosition

                mCurrentDegree = -azimuthinDegress

                updateSinsalBangwhi(azimuthinDegress.toDouble())
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

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

    override fun onBackPressed() {
        // 만약 검색 프래그먼트가 띄워져있다면
        if (supportFragmentManager.findFragmentById(R.id.container_search) != null) {
            supportFragmentManager.beginTransaction().remove(supportFragmentManager.findFragmentById(R.id.container_search)!!).commit()
            return
        } else {
            super.onBackPressed()
        }
    }


    class GPSListener : LocationListener {
        override fun onLocationChanged(p0: Location) {
            val latitude = p0.latitude
            val longitude = p0.longitude
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        private const val BTN_LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

}