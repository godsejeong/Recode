package com.sttproject

import android.Manifest
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import android.widget.Toast
import android.support.v4.app.ActivityCompat
import android.app.admin.DevicePolicyManager
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.content.Context
import android.net.Uri
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.sttproject.MainActivity.Companion.todaycode
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import android.R.id.edit
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    companion object {
        var todaycode = "10"
    }

    val RESULT_CODE = 100
    var image = ""
    var items : ArrayList<RecordListData> = ArrayList()
    lateinit var adapter : RecordRecylcerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getPermission()
        var layoutmanager = LinearLayoutManager(this)
        mainList.layoutManager = layoutmanager
        loadData()
        adapter = RecordRecylcerAdapter(items,this)
        mainList.adapter = adapter


        mainFab.onClick {
            var intent = Intent(applicationContext,EdiTitleDialog::class.java)
            startActivityForResult(intent,200)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_CODE) {
                var date = SimpleDateFormat("yyyy-MM-dd")
                var mNow = System.currentTimeMillis()
                var mDate = Date(mNow)
                var mday = date.format(mDate)

                Log.e("today", MainActivity.todaycode)
                Log.e("date",mday.toString())
                if(MainActivity.todaycode != mday){
                    Log.e("today", MainActivity.todaycode)
                    MainActivity.todaycode = mday
                    items.add(RecordListData(null,null,null,null,mday,1))
                }

                var title = data!!.getStringExtra("title")
                var content = data!!.getStringExtra("content")
                var path = data!!.getStringExtra("record")

                items.add(RecordListData(title,content,path,image,null,0))
                adapter.notifyDataSetChanged()
                saveData()
            }

            if(requestCode == 200){
                var intent = Intent(applicationContext, RecordActivity::class.java)
                intent.putExtra("title",data!!.getStringExtra("title"))
                image = data!!.getStringExtra("img")
                startActivityForResult(intent, RESULT_CODE)
            }
        }
    }

    fun getPermission() {
        val devicePolicyManager = applicationContext.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager

        if (!devicePolicyManager.isAdminActive(componentName) || ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName)
            startActivityForResult(intent, 0)

            ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO),
                    0)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 0) {
            if (grantResults[0] == 0) {

            } else {
                Toast.makeText(this, "권한이 거절 되었습니다. 앱을 이용하려면 권한을 승낙하여야 합니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }


    fun loadData() {
        val gson = Gson()
        val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
        val json = pref.getString("save", "")
        var shareditems: ArrayList<RecordListData>?
        shareditems = gson.fromJson(json, object : TypeToken<ArrayList<RecordListData>>() {}.type)
        if (shareditems != null) items.addAll(shareditems)
    }
    fun saveData() { //items 안의 내용이 저장됨
        val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = pref.edit()
        val json = Gson().toJson(items)
        editor.putString("save", json)
        Log.d("asdf", json)
        editor.commit()
    }

}
