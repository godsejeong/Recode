package com.sttproject

import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_write_recode.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

class WriteRecode : AppCompatActivity() {
    var player: MediaPlayer? = null
    var title = ""
    var path = ""
    var content = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_recode)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        title = intent.getStringExtra("title")
        path = intent.getStringExtra("path")
        content = intent.getStringExtra("content")

        writeTitle.text = title

        writeSttText.text =content

        writeRead.onClick {
            playAudio(path!!)
        }
    }

    fun playAudio(url: String) {
        try {
            killMediaPlayer()
            player = MediaPlayer()
            Log.e("url", url)
            player!!.setDataSource(url)
            player!!.prepare()
            player!!.start()
        } catch (e: NullPointerException) {
            toast("녹음 파일이 존재하지 않습니다.")
        }
    }

    fun killMediaPlayer() {
        if (player != null) {
            player!!.release()
        }
    }

    override fun onOptionsItemSelected(item : MenuItem) : Boolean{
        when (item.itemId){
            android.R.id.home->{
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
