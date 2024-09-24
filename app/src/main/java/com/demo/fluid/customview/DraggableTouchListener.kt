package com.demo.fluid.customview

import android.view.MotionEvent
import android.view.View

class DraggableTouchListener(
    private val onClick: () -> Unit,   // Hàm gọi khi click
) : View.OnTouchListener {

    private var dX = 0f
    private var dY = 0f
    private var isDragging = false

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                dX = view.x - event.rawX
                dY = view.y - event.rawY
                isDragging = false // Đặt trạng thái kéo là false khi bắt đầu
            }
            MotionEvent.ACTION_MOVE -> {
                view.animate()
                    .x(event.rawX + dX)
                    .y(event.rawY + dY)
                    .setDuration(0)
                    .start()
                isDragging = true // Đặt trạng thái kéo là true khi đang kéo
            }
            MotionEvent.ACTION_UP -> {
                if (!isDragging) {
                    // Xử lý sự kiện click khi không có kéo
                    onClick.invoke()
                }
            }
        }
        return true
    }
}




