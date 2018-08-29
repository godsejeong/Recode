package com.sttproject

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.LinearLayout
import android.widget.PopupWindow
import kotlinx.android.synthetic.main.activity_edi_title_dialog.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

class EdiTitleDialog : Activity() {
    var imgurl: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_edi_title_dialog)
        val popupView = layoutInflater.inflate(R.layout.activity_edi_title_dialog, null)
        var mPopupWindow = PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//        mPopupWindow.

        dialogClear.onClick {
            finish()
        }

        dialogGellery.onClick {
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent, 200)
        }

        dialogOk.onClick {
            var intent = intent
            if (dialogText.text.toString().isNotEmpty()) {
                intent.putExtra("title", dialogText.text.toString())
            }else{
                toast("녹음제목을 입력해주세요")
            }

            if (imgurl != null) {
                intent.putExtra("img",imgurl.toString())
            }else{
                toast("이미지를 등록해주세요")
            }

            if(imgurl != null && dialogText.text.toString().isNotEmpty()){
                setResult(RESULT_OK,intent)
                finish()
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200) {
            imgurl = data!!.data
        }
    }
}
