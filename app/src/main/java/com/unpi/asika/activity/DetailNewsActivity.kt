package com.unpi.asika.activity

import android.graphics.drawable.Drawable
import android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.widget.Toast
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.unpi.asika.GlideApp
import com.unpi.asika.R
import com.unpi.asika.utils.Constant.NEWS_CONTENT
import com.unpi.asika.utils.Constant.NEWS_DATE
import com.unpi.asika.utils.Constant.NEWS_IMAGE
import com.unpi.asika.utils.Constant.NEWS_TITLE
import kotlinx.android.synthetic.main.activity_detail_news.*

class DetailNewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_news)

        val getImage = intent.getStringExtra(NEWS_IMAGE)
        val getTitle = intent.getStringExtra(NEWS_TITLE)
        val getDate = intent.getStringExtra(NEWS_DATE)
        val getContent = intent.getStringExtra(NEWS_CONTENT)?.replace("newLine", "\n\n")

        GlideApp.with(this)
            .load(getImage)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.visibility = GONE
                    Toast.makeText(
                        applicationContext,
                        "Gambar gagal dimuat",
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.visibility = GONE
                    return false
                }

            })
            .into(img_thumb)

        tv_title.text = getTitle.toString()
        tv_datetime.text = getDate.toString()
        tv_content.text = getContent.toString()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            tv_content.justificationMode = JUSTIFICATION_MODE_INTER_WORD
        }
    }
}