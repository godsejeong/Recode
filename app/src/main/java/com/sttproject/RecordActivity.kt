package com.sttproject

import android.annotation.SuppressLint
import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.media.MediaRecorder
import android.os.Environment
import kotlinx.android.synthetic.main.activity_recode.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import android.media.MediaPlayer
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import org.jetbrains.anko.toast
import android.speech.SpeechRecognizer
import android.speech.RecognizerIntent
import android.content.Intent
import android.speech.RecognitionListener
import android.view.MenuItem


class RecordActivity : AppCompatActivity() {
    var player: MediaPlayer? = null
    var recorder: MediaRecorder? = null
    var path: String? = null
    var recordcheck = true
    var title = ""
    var time = timmer()
    var recognizer: SpeechRecognizer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recode)
        title = intent.getStringExtra("title")
        recordTitle.text = title

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recordStart.onClick {
            if (recordcheck) {
                recordStart.setImageResource(R.drawable.ic_pause_black_24dp)
                recordStartText.text = "저장하기"
                recordcheck = false

                recordRemoveLayout.visibility = View.INVISIBLE
                recordReadLayout.visibility = View.INVISIBLE
                recordstart()
                time = timmer()
                Timer().schedule(time, 0, 1000)


            } else if (!recordcheck) {
                recordcheck = true
                recordStart.setImageResource(R.drawable.ic_keyboard_voice_black_24dp)
                recordStartText.text = "녹음하기"
                recordRemoveLayout.visibility = View.VISIBLE
                recordReadLayout.visibility = View.VISIBLE
                recordstop()
                time.cancel()
            }
        }

        recordRead.onClick {
            if (path != null) {
                recordread()
            } else {
                toast("녹음파일이 존재하지 않습니다.")
            }
        }

        recordRemove.onClick {
            if (path != null) {
                path = null
                recordTimer.text = "00:00:00"
                toast("삭제완료")
            } else {
                toast("녹음파일이 존재하지 않습니다.")
            }
        }

        recordFab.onClick {
            var recordintent = intent
            recordintent.putExtra("record",path)
            recordintent.putExtra("title",title)
            recordintent.putExtra("content",recordSttText.text)
            setResult(Activity.RESULT_OK,recordintent)
            finish()
        }

    }


    fun timmer(): TimerTask {

        var h = 0
        var m = 0
        var s = 0

        var tt = object : TimerTask() {

            val handler = @SuppressLint("HandlerLeak")
            object : Handler() {
                @SuppressLint("SetTextI18n")
                override fun handleMessage(msg: Message) {
                    recordTimer.text = makeTimerText(h) + ":" + makeTimerText(m) + ":" + makeTimerText(s)
                }
            }

            override fun run() {

                if (s >= 60) {
                    m++
                    s = 0
                }

                if (m >= 60) {
                    h++
                    m = 0
                }

                val msg = handler.obtainMessage()
                handler.sendMessage(msg)

                s++
            }

        }

        return tt
    }

    fun SoundOn() {
        intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
        recognizer = SpeechRecognizer.createSpeechRecognizer(this)
        recognizer!!.setRecognitionListener(listener)
        recognizer!!.startListening(intent)
    }

    //음성인식 리스너
    private val listener = object : RecognitionListener {
        //음성 인식 준비가 되었으면
        override fun onReadyForSpeech(bundle: Bundle) {
            Log.e("ㅁㄴㅇㄹ", "start")
        }

        //입력이 시작되면
        override fun onBeginningOfSpeech() {

        }

        //입력 소리 변경 시
        override fun onRmsChanged(v: Float) {

        }

        //더 많은 소리를 받을 때
        override fun onBufferReceived(bytes: ByteArray) {

        }

        //음성 입력이 끝났으면
        override fun onEndOfSpeech() {
        }

        //에러가 발생하면
        override fun onError(i: Int) {
            toast("음성인식에 실패하였습니다.녹음을 다시 틀어주세요")
            Log.e("ㅁㄴㅇㄹ", "asdfasdferrr${i}")
        }

        //음성 인식 결과 받음
        override fun onResults(results: Bundle) {
            Log.e("음성인식", "성공")
            val key = SpeechRecognizer.RESULTS_RECOGNITION
            val result = results.getStringArrayList(key)
            val rs = arrayOfNulls<String>(result!!.size)
            result.toArray(rs)
            recordSttText.text = "" + rs[0]
            recordFab.visibility = View.VISIBLE
        }

        //인식 결과의 일부가 유효할 때
        override fun onPartialResults(bundle: Bundle) {

        }

        //미래의 이벤트를 추가하기 위해 미리 예약되어진 함수
        override fun onEvent(i: Int, bundle: Bundle) {

        }
    }


    fun makeTimerText(time: Int): String =
            if (time < 10)
                "0$time"
            else
                "$time"

    fun recordstop() {
        recorder!!.release()
        recorder = null
    }

    fun recordread() {
        playAudio(path!!)
        SoundOn()
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

    fun recordstart() {
        val dirPath = Environment.getExternalStorageDirectory().absolutePath + "/Recode/"
        val file = File(dirPath)

        if (!file.exists()) file.mkdirs()
        val recodePath = "RECODE_" + SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        path = "$dirPath$recodePath.3gp"
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
            setOutputFile(path)
            prepare()
            start()
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
