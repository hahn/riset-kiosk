package id.husna.risetkiosk

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mAdminComponentName: ComponentName
    private lateinit var mDevicePolicyManager: DevicePolicyManager

    private val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.decorView.systemUiVisibility = flags

        mAdminComponentName = MyDeviceAdminReceiver.getComponentName(this)
        mDevicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        if (mDevicePolicyManager.isDeviceOwnerApp(packageName)) {
            // You are the owner!
            setKioskPolicies()
            Toast.makeText(this, "silakan disetting bang", Toast.LENGTH_LONG).show()
        } else {
            // Please contact your system administrator
            Toast.makeText(this, "silakan hubungi admin bang", Toast.LENGTH_LONG).show()
        }

        btnDeactivate.setOnClickListener {
            stopKioskPolicies()
        }

        btnActive.setOnClickListener {
            setKioskPolicies()
        }
    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }

    private fun setKioskPolicies() {
        mDevicePolicyManager.setLockTaskPackages(mAdminComponentName, arrayOf(packageName))
        startLockTask()
//
//        val intentFilter = IntentFilter(Intent.ACTION_MAIN)
//        intentFilter.addCategory(Intent.CATEGORY_HOME)
//        intentFilter.addCategory(Intent.CATEGORY_DEFAULT)
//        mDevicePolicyManager.addPersistentPreferredActivity(mAdminComponentName,
//            intentFilter, ComponentName(packageName, MainActivity::class.java.name))
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            mDevicePolicyManager.setKeyguardDisabled(mAdminComponentName, true)
//        }

//        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

//        mDevicePolicyManager.setGlobalSetting(mAdminComponentName,
//            Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
//            Integer.toString(
//                BatteryManager.BATTERY_PLUGGED_AC
//                    or BatteryManager.BATTERY_PLUGGED_USB
//                    or BatteryManager.BATTERY_PLUGGED_WIRELESS))

    }

    private fun stopKioskPolicies() {
        if(mDevicePolicyManager.isLockTaskPermitted(packageName))
            stopLockTask()
    }
}
