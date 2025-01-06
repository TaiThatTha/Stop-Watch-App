package com.itckc.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.os.SystemClock
import android.view.Gravity
import android.widget.Button
import android.widget.Chronometer
import android.widget.ImageView
import android.widget.TextView
import java.util.*

class MainActivity : ComponentActivity() {
    private var isRunning = false
    private var baseTime: Long = 0L
    private var elapsedTime: Long = 0L
    private val lapTimes = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Tạo layout trong code
        val layout = android.widget.LinearLayout(this)
        layout.orientation = android.widget.LinearLayout.VERTICAL
        layout.setPadding(16, 16, 16, 16)

        // ImageView để hiển thị hình ảnh
        val imageView = ImageView(this)
        imageView.setImageResource(R.drawable.stopwatch)
        imageView.adjustViewBounds = true
        imageView.layoutParams = android.widget.LinearLayout.LayoutParams(
            android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
            android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layout.addView(imageView) // Thêm ImageView vào layout chính

        // Chronometer để hiển thị thời gian
        val chronometer = Chronometer(this)
        chronometer.textSize = 48f
        chronometer.gravity = Gravity.CENTER
        layout.addView(chronometer)

        // Tạo LinearLayout chứa các nút điều khiển
        val buttonLayout = android.widget.LinearLayout(this)
        buttonLayout.orientation = android.widget.LinearLayout.HORIZONTAL
        buttonLayout.gravity = android.view.Gravity.CENTER_HORIZONTAL
        buttonLayout.setPadding(0, 16, 0, 16)

        // Nút Bắt đầu
        val btnStart = Button(this)
        btnStart.text = "Bắt đầu"
        buttonLayout.addView(btnStart)

        // Nút Tạm dừng
        val btnPause = Button(this)
        btnPause.text = "Tạm dừng"
        buttonLayout.addView(btnPause)

        // Nút Ghi mốc
        val btnLap = Button(this)
        btnLap.text = "Ghi mốc"
        buttonLayout.addView(btnLap)

        // Nút Đặt lại
        val btnReset = Button(this)
        btnReset.text = "Đặt lại"
        buttonLayout.addView(btnReset)

        // Thêm buttonLayout vào layout chính
        layout.addView(buttonLayout)

        // TextView để hiển thị mốc thời gian
        val lapTimeList = TextView(this)
        lapTimeList.textSize = 16f
        lapTimeList.setPadding(0, 16, 0, 0)
        layout.addView(lapTimeList)

        // Đặt layout vào content view của activity
        setContentView(layout)

        // Thiết lập hành động cho nút Bắt đầu
        btnStart.setOnClickListener {
            startTimer(chronometer)
        }

        // Thiết lập hành động cho nút Tạm dừng
        btnPause.setOnClickListener {
            pauseTimer(chronometer)
        }

        // Thiết lập hành động cho nút Đặt lại
        btnReset.setOnClickListener {
            resetTimer(chronometer, lapTimeList)
        }

        // Thiết lập hành động cho nút Ghi mốc
        btnLap.setOnClickListener {
            recordLapTime(chronometer, lapTimeList)
        }
    }

    private fun startTimer(chronometer: Chronometer) {
        if (!isRunning) {
            baseTime = SystemClock.elapsedRealtime() - elapsedTime
            chronometer.base = baseTime
            chronometer.start()
            chronometer.format = "00:%s" // Định dạng để hiển thị giờ, phút, giây và mili giây
            isRunning = true
        }
    }

    private fun pauseTimer(chronometer: Chronometer) {
        if (isRunning) {
            elapsedTime = SystemClock.elapsedRealtime() - chronometer.base
            chronometer.stop()
            isRunning = false
        }
    }

    private fun resetTimer(chronometer: Chronometer, lapTimeList: TextView) {
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.stop()
        isRunning = false
        elapsedTime = 0L
        lapTimes.clear()
        lapTimeList.text = ""
    }

    private fun recordLapTime(chronometer: Chronometer, lapTimeList: TextView) {
        if (isRunning) {
            val lapTime = SystemClock.elapsedRealtime() - baseTime
            val hours = (lapTime / 3600000).toInt()
            val minutes = (lapTime / 60000 % 60).toInt()
            val seconds = (lapTime / 1000 % 60).toInt()
            val milliseconds = (lapTime % 1000).toInt()
            val lapText = String.format(Locale.getDefault(), "%02d:%02d:%02d:%03d", hours, minutes, seconds, milliseconds)
            lapTimes.add(lapText)
            lapTimeList.append("Lap ${lapTimes.size}: $lapText\n")
        }
    }
}
