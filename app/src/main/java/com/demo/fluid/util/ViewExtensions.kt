package com.demo.fluid.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.net.ConnectivityManager
import android.net.Uri
import android.os.SystemClock
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.demo.fluid.util.gl.InputBuffer
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun View.setBackgroundTint(color: Int) {
    ViewCompat.setBackgroundTintList(this, ColorStateList.valueOf(color))
}

fun Context.getActionBarHeight(): Int {
    val tv = TypedValue()
    if (this.theme?.resolveAttribute(
            android.R.attr.actionBarSize,
            tv,
            true
        ) == true
    ) {
        return TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
    }
    return 0
}

fun View.getBitmapFromView(): Bitmap {
    val bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    this.draw(canvas)
    return bitmap
}

fun Context.saveBitmapToFile(bitmap: Bitmap, fileName: String): String? {
    // Get the window manager
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

    // Get the display metrics
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getRealMetrics(displayMetrics)
    val screenWidth = displayMetrics.widthPixels
    val screenHeight = displayMetrics.heightPixels

    // Calculate the total height, including any system decorations (e.g., status bar, navigation bar)
    val totalHeight = displayMetrics.heightPixels

    // Get the action bar height
    var actionBarHeight: Int
    try {
        val actionBarStyle = TypedValue()
        theme.resolveAttribute(android.R.attr.actionBarSize, actionBarStyle, true)
        actionBarHeight = TypedValue.complexToDimensionPixelSize(actionBarStyle.data, resources.displayMetrics)
    } catch (e: Exception) {
        actionBarHeight = 0 // Default to 0 if action bar height is not available
    }

    // Create a new bitmap with the total height
    val resultBitmap = Bitmap.createBitmap(screenWidth, totalHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(resultBitmap)

    // Draw a transparent background
    canvas.drawColor(Color.TRANSPARENT)

    // Center the original bitmap on the new bitmap, considering the action bar
    val left = (screenWidth - bitmap.width) / 2
    val top = (totalHeight - bitmap.height) / 2
    val rect = Rect(left, top, left + bitmap.width, top + bitmap.height)
    canvas.drawBitmap(bitmap, null, rect, null)

    // Save the bitmap to file
    val fileDir = File(filesDir, "text_folder")
    if (!fileDir.exists()) {
        fileDir.mkdir() // Create directory if it does not exist
    }
    val fileName = "${fileName}.png" // PNG file name
    val file = File(fileDir, fileName)

    return try {
        FileOutputStream(file).use { outputStream ->
            resultBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            file.absolutePath
        }
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}





fun View.changeBackgroundColor(newColor: Int) {
    setBackgroundColor(
        ContextCompat.getColor(
            context,
            newColor
        )
    )
}

fun ImageView.setTintColor(@ColorRes color: Int) {
    imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, color))
}

fun TextView.changeTextColor(newColor: Int) {
    setTextColor(
        ContextCompat.getColor(
            context,
            newColor
        )
    )
}

fun View.animRotation() {
    val anim = RotateAnimation(
        0f, 360f,
        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
    )
    anim.interpolator = LinearInterpolator()
    anim.duration = 1500
    anim.isFillEnabled = true
    anim.repeatCount = Animation.INFINITE
    anim.fillAfter = true
    startAnimation(anim)
}

fun View.isShow() = visibility == View.VISIBLE

fun View.isGone() = visibility == View.GONE

fun View.isInvisible() = visibility == View.INVISIBLE

fun View.show() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.inv() {
    visibility = View.INVISIBLE
}

fun View.setPreventDoubleClick(debounceTime: Long = 500, action: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0
        override fun onClick(v: View?) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            action()
            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

fun View.setPreventDoubleClickScaleView(debounceTime: Long = 500, action: () -> Unit) {
    setOnTouchListener(object : View.OnTouchListener {
        private var lastClickTime: Long = 0
        private var rect: Rect? = null

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            fun setScale(scale: Float) {
                v.scaleX = scale
                v.scaleY = scale
            }

            if (event.action == MotionEvent.ACTION_DOWN) {
                //action down: scale view down
                rect = Rect(v.left, v.top, v.right, v.bottom)
                setScale(0.9f)
            } else if (rect != null && !rect!!.contains(
                    v.left + event.x.toInt(),
                    v.top + event.y.toInt()
                )
            ) {
                //action moved out
                setScale(1f)
                return false
            } else if (event.action == MotionEvent.ACTION_UP) {
                //action up
                setScale(1f)
                //handle click too fast
                if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) {
                } else {
                    lastClickTime = SystemClock.elapsedRealtime()
                    action()
                }
            } else {
                //other
            }

            return true
        }
    })
}
fun Activity.addActionUp(){
    val handler = CoroutineExceptionHandler { coroutineContext, throwable ->  }
    CoroutineScope(Dispatchers.IO).launch(handler) {
        delay(50)
        val downTime = System.currentTimeMillis()
        val eventTime = System.currentTimeMillis()

        val pointerProperties = MotionEvent.PointerProperties().apply {
            id = 0
            toolType = MotionEvent.TOOL_TYPE_FINGER
        }

        val pointerCoordsEnd = MotionEvent.PointerCoords().apply {
            this.x = 0f
            this.y = 0f
        }

        val upEvent = MotionEvent.obtain(
            downTime, eventTime + 500, MotionEvent.ACTION_UP,
            1, arrayOf(pointerProperties), arrayOf(pointerCoordsEnd),
            0, 0, 1.0f, 1.0f, 2, 0, 0x1002, 0
        )
        InputBuffer.Instance.addEvent(upEvent)

        upEvent.recycle()
    }
}

fun Activity.simulateSwipe(xStart: Float, yStart: Float, xEnd: Float, yEnd: Float) {
    val handler = CoroutineExceptionHandler { coroutineContext, throwable ->  }
    CoroutineScope(Dispatchers.IO).launch(handler) {
        val downTime = System.currentTimeMillis()
        val eventTime = System.currentTimeMillis()

        val pointerProperties = MotionEvent.PointerProperties().apply {
            id = 0
            toolType = MotionEvent.TOOL_TYPE_FINGER
        }

        val pointerCoordsStart = MotionEvent.PointerCoords().apply {
            this.x = xStart
            this.y = yStart
        }

        val downEvent = MotionEvent.obtain(
            downTime, eventTime, MotionEvent.ACTION_DOWN,
            1, arrayOf(pointerProperties), arrayOf(pointerCoordsStart),
            0, 0, 1.0f, 1.0f, 2, 0, 0x1002, 0
        )
        InputBuffer.Instance.addEvent(downEvent)

        delay(100)

        val steps = 10  // Số bước di chuyển (càng nhiều thì càng mượt)
        for (i in 1..steps) {
            val intermediateX = xStart + (xEnd - xStart) * i / steps
            val intermediateY = yStart + (yEnd - yStart) * i / steps

            val pointerCoordsMove = MotionEvent.PointerCoords().apply {
                this.x = intermediateX
                this.y = intermediateY
            }

            val moveEvent = MotionEvent.obtain(
                downTime, eventTime + i * 100, MotionEvent.ACTION_MOVE,
                1, arrayOf(pointerProperties), arrayOf(pointerCoordsMove),
                0, 0, 1.0f, 1.0f, 2, 0, 0x1002, 0
            )
            InputBuffer.Instance.addEvent(moveEvent)
            moveEvent.recycle()

           delay(100)
        }

        val pointerCoordsEnd = MotionEvent.PointerCoords().apply {
            this.x = xEnd
            this.y = yEnd
        }

        val upEvent = MotionEvent.obtain(
            downTime, eventTime + 1000, MotionEvent.ACTION_UP,
            1, arrayOf(pointerProperties), arrayOf(pointerCoordsEnd),
            0, 0, 1.0f, 1.0f, 2, 0, 0x1002, 0
        )
        InputBuffer.Instance.addEvent(upEvent)

        downEvent.recycle()
        upEvent.recycle()
    }
}

fun simulateClick(view: View) {
    val downTime = System.currentTimeMillis()
    val eventTime = System.currentTimeMillis()

    // Thiết lập PointerProperties với toolType là TOOL_TYPE_FINGER
    val pointerProperties = MotionEvent.PointerProperties().apply {
        id = 0
        toolType = MotionEvent.TOOL_TYPE_FINGER
    }

    // Thiết lập PointerCoords để chứa tọa độ x, y
    val pointerCoords = MotionEvent.PointerCoords().apply {
        this.x = 100f
        this.y = 100f
    }

    // Tạo MotionEvent "down" cho hành động click
    val downEvent = MotionEvent.obtain(
        downTime, eventTime, MotionEvent.ACTION_DOWN,
        1, arrayOf(pointerProperties), arrayOf(pointerCoords),
        0, 0, 1.0f, 1.0f, 0, 0, 0, 0
    )

    // Gửi sự kiện "down" đến view
    view.dispatchTouchEvent(downEvent)

    Thread.sleep(200)

    // Tạo MotionEvent "up" cho hành động thả
    val upEvent = MotionEvent.obtain(
        downTime, eventTime + 200, MotionEvent.ACTION_UP,
        1, arrayOf(pointerProperties), arrayOf(pointerCoords),
        0, 0, 1.0f, 1.0f, 0, 0, 0, 0
    )

    // Gửi sự kiện "up" đến view
    view.dispatchTouchEvent(upEvent)

    // Giải phóng tài nguyên của MotionEvent
    downEvent.recycle()
    upEvent.recycle()
}


fun Fragment.displayToast(msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun Fragment.displayToast(@StringRes msg: Int) {
    Toast.makeText(context, getString(msg), Toast.LENGTH_SHORT).show()
}

fun Fragment.convertDpToPx(dp: Int): Int {
    val dip = dp.toFloat()
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dip,
        resources.displayMetrics
    ).toInt()
}

fun Context.convertDpToPx(dp: Int): Int {
    val dip = dp.toFloat()
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dip,
        resources.displayMetrics
    ).toInt()
}

fun Context.haveNetworkConnection(): Boolean {
    return try {
        var haveConnectedWifi = false
        var haveConnectedMobile = false
        return try {
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.allNetworkInfo
            for (ni in netInfo) {
                if (ni.typeName
                        .equals("WIFI", ignoreCase = true)
                ) if (ni.isConnected) haveConnectedWifi = true
                if (ni.typeName
                        .equals("MOBILE", ignoreCase = true)
                ) if (ni.isConnected) haveConnectedMobile = true
            }
            haveConnectedWifi || haveConnectedMobile
        } catch (e: java.lang.Exception) {
            System.err.println(e.toString())
            false
        }
    } catch (e: java.lang.Exception) {
        System.err.println(e.toString())
        false
    }
}

fun Context.openBrowser(url: String) {
    var url = url
    if (!url.startsWith("http://") && !url.startsWith("https://")) {
        url = "http://$url"
    }
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    try {
        startActivity(browserIntent)
    } catch (ex: java.lang.Exception) {
        ex.printStackTrace()
    }
}

