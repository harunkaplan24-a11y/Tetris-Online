package com.harunkaplan.tetrisonline

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import kotlin.math.min
import kotlin.random.Random

class TetrisView(context: Context) : View(context) {
    private val cols = 10
    private val rows = 20
    private val board = Array(rows) { IntArray(cols) }
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var score = 0
    private var level = 1
    private var lines = 0
    private var gameOver = false
    private var lastDrop = 0L
    private var down = false
    private var touchX = 0f
    private var touchY = 0f

    private val colors = intArrayOf(
        Color.TRANSPARENT, Color.CYAN, Color.YELLOW, Color.rgb(170, 70, 230),
        Color.GREEN, Color.RED, Color.BLUE, Color.rgb(255, 150, 20)
    )

    private val shapes = arrayOf(
        arrayOf(intArrayOf(1, 1, 1, 1)),
        arrayOf(intArrayOf(1, 1), intArrayOf(1, 1)),
        arrayOf(intArrayOf(0, 1, 0), intArrayOf(1, 1, 1)),
        arrayOf(intArrayOf(0, 1, 1), intArrayOf(1, 1, 0)),
        arrayOf(intArrayOf(1, 1, 0), intArrayOf(0, 1, 1)),
        arrayOf(intArrayOf(1, 0, 0), intArrayOf(1, 1, 1)),
        arrayOf(intArrayOf(0, 0, 1), intArrayOf(1, 1, 1))
    )

    private var piece = shapes[Random.nextInt(shapes.size)].map { it.copyOf() }.toTypedArray()
    private var pieceColor = Random.nextInt(1, 8)
    private var px = 3
    private var py = 0

    init { isFocusable = true }

    private fun resetPiece() {
        piece = shapes[Random.nextInt(shapes.size)].map { it.copyOf() }.toTypedArray()
        pieceColor = Random.nextInt(1, 8)
        px = (cols - piece[0].size) / 2
        py = 0
        if (!valid(px, py, piece)) gameOver = true
    }

    private fun valid(x: Int, y: Int, p: Array<IntArray>): Boolean {
        for (r in p.indices) for (c in p[r].indices) if (p[r][c] != 0) {
            val bx = x + c; val by = y + r
            if (bx !in 0 until cols || by >= rows) return false
            if (by >= 0 && board[by][bx] != 0) return false
        }
        return true
    }

    private fun rotate() {
        val h = piece.size; val w = piece[0].size
        val rotated = Array(w) { IntArray(h) }
        for (r in 0 until h) for (c in 0 until w) rotated[c][h - 1 - r] = piece[r][c]
        if (valid(px, py, rotated)) piece = rotated
    }

    private fun move(dx: Int) { if (valid(px + dx, py, piece)) px += dx }

    private fun drop(): Boolean {
        if (valid(px, py + 1, piece)) { py++; return true }
        lockPiece(); return false
    }

    private fun lockPiece() {
        for (r in piece.indices) for (c in piece[r].indices) if (piece[r][c] != 0) {
            val by = py + r; val bx = px + c
            if (by in 0 until rows && bx in 0 until cols) board[by][bx] = pieceColor
        }
        clearLines()
        resetPiece()
    }

    private fun clearLines() {
        var cleared = 0
        var r = rows - 1
        while (r >= 0) {
            if (board[r].all { it != 0 }) {
                for (y in r downTo 1) board[y] = board[y - 1].copyOf()
                board[0] = IntArray(cols)
                cleared++
            } else r--
        }
        if (cleared > 0) {
            lines += cleared
            score += when (cleared) { 1 -> 100; 2 -> 300; 3 -> 500; else -> 800 } * level
            level = lines / 10 + 1
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.rgb(12, 12, 20))
        val cell = min(width / cols.toFloat(), height * .78f / rows)
        val left = (width - cell * cols) / 2f
        val top = height * .13f

        paint.textAlign = Paint.Align.CENTER
        paint.textSize = cell * .55f
        paint.color = Color.WHITE
        canvas.drawText("TETRIS ONLINE", width / 2f, cell * .7f, paint)
        paint.textSize = cell * .38f
        canvas.drawText("Puan: $score    Level: $level", width / 2f, top - cell * .25f, paint)

        paint.style = Paint.Style.STROKE; paint.strokeWidth = 2f; paint.color = Color.DKGRAY
        canvas.drawRect(left, top, left + cell * cols, top + cell * rows, paint)
        paint.style = Paint.Style.FILL

        for (r in 0 until rows) for (c in 0 until cols) if (board[r][c] != 0) drawCell(canvas, left, top, cell, c, r, board[r][c])
        for (r in piece.indices) for (c in piece[r].indices) if (piece[r][c] != 0) drawCell(canvas, left, top, cell, px + c, py + r, pieceColor)

        paint.textSize = cell * .34f
        paint.color = Color.LTGRAY
        canvas.drawText("◀     ↻     ▶     ▼", width / 2f, height - cell * .65f, paint)

        if (gameOver) {
            paint.color = Color.argb(210, 0, 0, 0); canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
            paint.color = Color.WHITE; paint.textSize = cell * .65f
            canvas.drawText("OYUN BİTTİ", width / 2f, height / 2f - cell, paint)
            paint.textSize = cell * .38f
            canvas.drawText("Dokun ve yeniden başla", width / 2f, height / 2f, paint)
        }
    }

    private fun drawCell(canvas: Canvas, left: Float, top: Float, cell: Float, x: Int, y: Int, color: Int) {
        if (x !in 0 until cols || y !in 0 until rows) return
        paint.color = colors[color]
        canvas.drawRect(left + x * cell + 1, top + y * cell + 1, left + (x + 1) * cell - 1, top + (y + 1) * cell - 1, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> { touchX = event.x; touchY = event.y; down = false; return true }
            MotionEvent.ACTION_UP -> {
                if (gameOver) {
                    for (r in board.indices) board[r].fill(0)
                    score = 0; level = 1; lines = 0; gameOver = false; resetPiece(); invalidate(); return true
                }
                val dx = event.x - touchX; val dy = event.y - touchY
                if (kotlin.math.abs(dx) > kotlin.math.abs(dy) && kotlin.math.abs(dx) > 30) move(if (dx > 0) 1 else -1)
                else if (dy > 40) { while (drop()) score += 2 }
                else rotate()
                invalidate(); return true
            }
        }
        return true
    }

    override fun computeScroll() {
        super.computeScroll()
        if (!gameOver) {
            val now = System.currentTimeMillis()
            val interval = (650L - (level - 1) * 55L).coerceAtLeast(100L)
            if (now - lastDrop >= interval) { drop(); lastDrop = now; invalidate() }
            postInvalidateDelayed(30)
        }
    }
}
