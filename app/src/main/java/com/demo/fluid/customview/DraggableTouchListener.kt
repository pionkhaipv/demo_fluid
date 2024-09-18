package com.demo.fluid.customview

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout

class DraggableTouchListener(
    context: Context,
    private val removeArea: View,    // View mà khi kéo đến thì sẽ loại bỏ TextView
    private val container: FrameLayout, // View chứa TextView
    private val onDoubleClick: () -> Unit,
    private val onDragStart: () -> Unit,
    private val onDragEnd: () -> Unit
) : View.OnTouchListener {

    private var dX = 0f
    private var dY = 0f
    private var isDragging = false

    // Tạo GestureDetector để nhận diện double tap
    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            // Xử lý sự kiện double tap
            onDoubleClick.invoke()
            return true
        }
    })

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        // Xử lý sự kiện của GestureDetector
        gestureDetector.onTouchEvent(event)

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
                onDragStart.invoke()
            }
            MotionEvent.ACTION_UP -> {
                // Lấy vị trí của view và removeArea
                val viewLocation = IntArray(2)
                val removeAreaLocation = IntArray(2)

                view.getLocationOnScreen(viewLocation)
                removeArea.getLocationOnScreen(removeAreaLocation)

                val viewLeft = viewLocation[0]
                val viewTop = viewLocation[1]
                val viewRight = viewLeft + view.width
                val viewBottom = viewTop + view.height

                val removeLeft = removeAreaLocation[0]
                val removeTop = removeAreaLocation[1]
                val removeRight = removeLeft + removeArea.width
                val removeBottom = removeTop + removeArea.height

                // Tính toán diện tích giao nhau
                val intersectionWidth = minOf(viewRight, removeRight) - maxOf(viewLeft, removeLeft)
                val intersectionHeight = minOf(viewBottom, removeBottom) - maxOf(viewTop, removeTop)

                val intersectionArea = maxOf(0, intersectionWidth) * maxOf(0, intersectionHeight)
                val viewArea = view.width * view.height

                // Kiểm tra xem diện tích giao nhau có lớn hơn 5% diện tích view không
                val isLargeEnoughIntersection = intersectionArea >= 0.1 * viewArea

                // Xóa TextView nếu có phần diện tích giao nhau với removeArea lớn hơn 5%
                if (isLargeEnoughIntersection) {
                    container.removeView(view)
                } else if (!isDragging) {
                    // Nếu không phải kéo thì gọi onDoubleClick
                    // Double tap đã được xử lý trong GestureDetector
                }
                onDragEnd()
            }

        }
        return true
    }
}



