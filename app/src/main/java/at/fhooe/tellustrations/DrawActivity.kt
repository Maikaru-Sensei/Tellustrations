package at.fhooe.tellustrations

import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View

const val TAG : String = "Tellustrations"

class DrawActivity : AppCompatActivity(), SurfaceHolder.Callback, View.OnTouchListener {

    private lateinit var surfaceView: SurfaceView
    private lateinit var surfaceHolder: SurfaceHolder
    private lateinit var paint: Paint
    private lateinit var path: Path

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw)

        surfaceView = findViewById(R.id.surfaceViewDraw)
        surfaceHolder = surfaceView.holder

        surfaceHolder.setFormat(PixelFormat.TRANSPARENT)
        surfaceHolder.addCallback(this)

        surfaceView.setOnTouchListener(this)
        surfaceView.setZOrderOnTop(true)

        // set paint values
        paint = Paint()
        paint.isAntiAlias = true
        paint.isDither = true
        paint.color = Color.WHITE
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = 50.0f
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        surfaceHolder = holder
        path = Path()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        surfaceHolder = holder
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {

    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        Thread(
            Runnable {
                if (event != null) {
                    val rawX: Float = event.rawX
                    val rawY: Float = event.rawY
                    val x: Float = event.x
                    val y: Float = event.y
                    Log.e(TAG, "rawX: $rawX x: $x")

                    when (event.actionMasked) {
                        MotionEvent.ACTION_DOWN -> {
                            Log.e(TAG, "down")
                            path.reset()
                            path.moveTo(x, y)
                        }
                        MotionEvent.ACTION_MOVE -> {
                            Log.e(TAG, "move")
                            path.lineTo(x, y)
                        }
                        MotionEvent.ACTION_UP -> {
                            Log.e(TAG, "up")
                            path.lineTo(x, y)
                        }
                    }

                    val canvas: Canvas = surfaceHolder.lockCanvas()
                    canvas.drawPath(path, paint)
                    surfaceHolder.unlockCanvasAndPost(canvas)
                }
        }).start()

        return true;
    }
}

