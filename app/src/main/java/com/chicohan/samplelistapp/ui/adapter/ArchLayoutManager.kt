package com.chicohan.samplelistapp.ui.adapter

import android.content.Context
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.*

class ArchLayoutManager(
    private val context: Context,
    private var horizontalOffset: Int = 0
) : RecyclerView.LayoutManager() {
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams =
        RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT)

    override fun canScrollVertically(): Boolean {
      return true
    }

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        horizontalOffset += dy
        fill(recycler, state)
        return dy
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)
        fill(recycler, state)
    }

    private fun fill(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        detachAndScrapAttachedViews(recycler ?: return)

        for (itemIndex in 0 until itemCount) {
            val view = recycler.getViewForPosition(itemIndex)
            addView(view)
            measureChildWithMargins(view, 0, 0)

            val viewWidth = getDecoratedMeasuredWidth(view)
            val viewHeight = getDecoratedMeasuredHeight(view)

            val top = (itemIndex * viewHeight) - horizontalOffset
            val bottom = top + viewHeight
            val left = computeXComponent(((top + bottom) / 2).toFloat(), viewWidth.toFloat())
            val right = left.first + viewWidth

            val alpha = left.second
//            view.rotation = (alpha * (180 / PI)).toFloat() - 0f

            layoutDecoratedWithMargins(
                view,
                left.first.toInt(),
                top.toInt(),
                right.toInt(),
                bottom.toInt()
            )
        }

        recycler.scrapList.toList().forEach {
            recycler.recycleView(it.itemView)
        }
    }

    private fun computeYComponent(viewCenterX: Float,
                                  h: Float): Pair<Int, Double> {
        val screenWidth = context.resources.displayMetrics.widthPixels
        val s = screenWidth.toDouble() / 2
        val radius = (h * h + s * s) / (h * 2)

        val xScreenFraction = viewCenterX.toDouble() / screenWidth.toDouble()
        val beta = acos(s / radius)

        val alpha = beta + (xScreenFraction * (Math.PI - (2 * beta)))
        val yComponent = radius - (radius * sin(alpha))

        return Pair(yComponent.toInt(), alpha)
    }

    private fun pxFromDp(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }
    private fun computeXComponent(viewCenterY: Float, w: Float): Pair<Int, Double> {
        val screenHeight = context.resources.displayMetrics.heightPixels
        val s = screenHeight.toDouble() / 3.5
        val radius = (w * w + s * s) / (w * 2)

        val yScreenFraction = viewCenterY.toDouble() / screenHeight.toDouble()
        val beta = acos(s / radius)

        val alpha = beta + (yScreenFraction * (Math.PI - (2 * beta)))
        val xComponent = radius - (radius * sin(alpha))

        return Pair(xComponent.toInt(), alpha)
    }
}