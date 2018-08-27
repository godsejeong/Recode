package com.sttproject

import android.Manifest
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import pub.devrel.easypermissions.EasyPermissions
import android.widget.Toast
import android.support.annotation.NonNull
import android.Manifest.permission
import android.Manifest.permission.ACCESS_NETWORK_STATE
import android.Manifest.permission.RECEIVE_BOOT_COMPLETED
import android.Manifest.permission.READ_PHONE_STATE
import android.Manifest.permission.DISABLE_KEYGUARD
import android.Manifest.permission.RECORD_AUDIO
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.support.v4.app.ActivityCompat
import android.app.admin.DevicePolicyManager
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.content.ComponentName
import android.content.Context
import android.content.Context.DEVICE_POLICY_SERVICE




class MainActivity : AppCompatActivity() {
    val RESULT_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getPermission()

        mainFab.onClick {
            var intent = Intent(applicationContext, RecodeActivity::class.java)
            startActivityForResult(intent, RESULT_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_CODE) {

            }
        }
    }

    fun getPermission() {
        val devicePolicyManager = applicationContext.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager

//        val componentName = ComponentName(applicationContext, ShutdownConfigAdminReceiver::class.java!!)

        if (!devicePolicyManager.isAdminActive(componentName) || ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName)
            startActivityForResult(intent, 0)

            ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO), 0)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 0) {
            if (grantResults[0] == 0) {
                Toast.makeText(this, "권한이 승인됨", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "권한이 거절 되었습니다. 앱을 이용하려면 권한을 승낙하여야 합니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

}
