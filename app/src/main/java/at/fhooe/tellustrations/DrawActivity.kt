package at.fhooe.tellustrations

import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.Toolbar
import at.fhooe.tellustrations.databinding.ActivityDrawBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val TAG : String = "Tellustrations"

class DrawActivity : AppCompatActivity(), SurfaceHolder.Callback, View.OnTouchListener {

    private lateinit var surfaceView: SurfaceView
    private lateinit var surfaceHolder: SurfaceHolder
    private lateinit var paint: Paint
    private lateinit var path: Path
    private lateinit var toolbar: Toolbar
    private var defaultBackgroundColor: Int = Color.WHITE

    private lateinit var binding: ActivityDrawBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawBinding.inflate(layoutInflater)
        setContentView(binding.root)

        surfaceView = binding.activityDrawSurfaceviewDraw
        toolbar = binding.activityDrawToolbarDraw

        surfaceHolder = surfaceView.holder

        surfaceHolder.setFormat(PixelFormat.TRANSPARENT)
        surfaceHolder.addCallback(this)

        surfaceView.setOnTouchListener(this)
        surfaceView.setZOrderOnTop(true)
        surfaceView.setBackgroundColor(defaultBackgroundColor)

        setSupportActionBar(toolbar)

        // paint values will be set dynamically via color palette
        paint = Paint()
        paint.isAntiAlias = true
        paint.isDither = true
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = 50.0f
    }

    /**
     * set toolbar menu
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_draw, menu)
        return true
    }

    /**
     * handles the onTouch event on the surfaceView
     *
     * connect the "touched" points and draw a line on the canvas
     */
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event != null) {
            val x: Float = event.x
            val y: Float = event.y

            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    path.moveTo(x, y)
                }
                MotionEvent.ACTION_UP -> {
                    path.lineTo(x, y)
                }
                MotionEvent.ACTION_MOVE -> {
                    path.lineTo(x, y)
                }
            }

            GlobalScope.launch {
                writePathToCanvas(path)
            }
        }

        return true;
    }

    /**
     * will be called when an item inside the toolbar was clicked
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_toolbar_draw_erase -> {
                // TODO 22.04.2021: open seekbar dialog for selection width of the eraser
                if (paint.color == defaultBackgroundColor) {
                    paint.color = Color.RED
                }
                else {
                    paint.color = defaultBackgroundColor
                }

                path.reset()
            }

            R.id.menu_toolbar_draw_check -> {
                // drawing is finished; move on to next Player
            }

            else -> {
                return super.onOptionsItemSelected(item)
            }
        }

        return true
    }

    /** writes path to canvas inside surfaceView
     *  using coroutines otherwise we will block the UI thread */
    private suspend fun writePathToCanvas(path: Path) {
        withContext(Dispatchers.IO) {
            val canvas: Canvas = surfaceHolder.lockCanvas()
            canvas.drawPath(path, paint)
            surfaceHolder.unlockCanvasAndPost(canvas)
        }
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
}


