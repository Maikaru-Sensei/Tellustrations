package at.fhooe.tellustrations

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import at.fhooe.tellustrations.databinding.ActivityDrawBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


const val TAG : String = "Tellustrations"

class DrawActivity : AppCompatActivity(), SurfaceHolder.Callback, View.OnTouchListener, View.OnClickListener,
                     DialogInterface.OnClickListener {

    private lateinit var surfaceView: SurfaceView
    private lateinit var surfaceHolder: SurfaceHolder
    private lateinit var paint: Paint
    private lateinit var path: Path
    private lateinit var seekBarEraser: SeekBar
    private var defaultBackgroundColor: Int = Color.WHITE
    private var defaultPaintWidth: Float = 50f
    private lateinit var drawBitmap : Bitmap

    private lateinit var binding: ActivityDrawBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawBinding.inflate(layoutInflater)
        setContentView(binding.root)

        surfaceView = binding.activityDrawSurfaceviewDraw

        surfaceHolder = surfaceView.holder

        surfaceHolder.setFormat(PixelFormat.TRANSPARENT)
        surfaceHolder.addCallback(this)

        surfaceView.setOnTouchListener(this)
        surfaceView.setZOrderOnTop(true)
        surfaceView.setBackgroundColor(defaultBackgroundColor)

        setSupportActionBar(binding.activityDrawToolbarDraw)

        with(binding) {
            activityDrawPaletteBlue.setOnClickListener(this@DrawActivity)
            activityDrawPaletteGreen.setOnClickListener(this@DrawActivity)
            activityDrawPaletteYellow.setOnClickListener(this@DrawActivity)
            activityDrawPaletteRed.setOnClickListener(this@DrawActivity)
            activityDrawPaletteBlack.setOnClickListener(this@DrawActivity)
            activityDrawPalettePurple.setOnClickListener(this@DrawActivity)
            activityDrawPaletteOrange.setOnClickListener(this@DrawActivity)
            activityDrawBrush.setOnClickListener(this@DrawActivity)
            activityDrawPen.setOnClickListener(this@DrawActivity)
        }

        // init Paint with default values
        paint = Paint()
        paint.apply {
            isAntiAlias = true
            isDither = true
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            strokeWidth = defaultPaintWidth
        }
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

            // write path to canvas (surfaceView)
            GlobalScope.launch {
                writePathToCanvas(path)
            }
        }

        surfaceView.performClick()
        return true
    }


    /**
     * will be called when an item inside the toolbar was clicked
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            /* open alertDialog and select size of the eraser
            *  eraser has the same color like the background */
            R.id.menu_toolbar_draw_erase -> {
                val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
                val inflater: LayoutInflater = this.layoutInflater
                val view: View = inflater.inflate(R.layout.dialog_draw_erase, null)
                seekBarEraser = view.findViewById(R.id.dialog_draw_erase_seekbar)
                val circleBackground: FrameLayout = view.findViewById(R.id.dialog_draw_erase_size)

                val startCircle = GradientDrawable()
                startCircle.shape = GradientDrawable.OVAL
                startCircle.setColor(Color.BLACK)
                startCircle.setSize(paint.strokeWidth.toInt(), paint.strokeWidth.toInt())
                circleBackground.background = startCircle

                dialog.setView(view)
                dialog.setPositiveButton(R.string.dialog_draw_erase_ok, this)
                dialog.show()

                seekBarEraser.progress = paint.strokeWidth.toInt()
                seekBarEraser.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        // need to recreate a new Object to set another background?
                        val circle = GradientDrawable()
                        circle.shape = GradientDrawable.OVAL
                        circle.setColor(Color.BLACK)
                        circle.setSize(progress, progress)
                        circleBackground.background = circle
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {

                    }
                })

                paint.color = defaultBackgroundColor
                path.reset()
            }

            R.id.menu_toolbar_draw_check -> {
            }

            R.id.menu_toolbar_draw_delete -> {
                clearCanvas()
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
            var canvas = Canvas(drawBitmap)
            canvas.drawPath(path, paint)
            canvas = surfaceHolder.lockCanvas(null)
            canvas.drawBitmap(drawBitmap, 0f, 0f, null)
            surfaceHolder.unlockCanvasAndPost(canvas)
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        surfaceHolder = holder
        path = Path()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        surfaceHolder = holder
        drawBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) { }

    /**
     * set eraser size from seekBar
     */
    override fun onClick(dialog: DialogInterface?, which: Int) {
        paint.color = defaultBackgroundColor
        paint.strokeWidth = seekBarEraser.progress.toFloat()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            /* PENS */
            R.id.activity_draw_brush -> {
                paint.strokeWidth = 150f
            }

            R.id.activity_draw_pen -> {
                paint.strokeWidth = 50f
            }

            /* COLORS */
            R.id.activity_draw_palette_blue -> {
                paint.color = ContextCompat.getColor(this, R.color.color_palette_blue)
            }

            R.id.activity_draw_palette_green -> {
                paint.color = ContextCompat.getColor(this, R.color.color_palette_green)
            }

            R.id.activity_draw_palette_yellow -> {
                paint.color = ContextCompat.getColor(this, R.color.color_palette_yellow)
            }

            R.id.activity_draw_palette_red -> {
                paint.color = ContextCompat.getColor(this, R.color.color_palette_red)
            }

            R.id.activity_draw_palette_black -> {
                paint.color = ContextCompat.getColor(this, R.color.color_palette_black)
            }

            R.id.activity_draw_palette_orange -> {
                paint.color = ContextCompat.getColor(this, R.color.color_palette_orange)
            }

            R.id.activity_draw_palette_purple -> {
                paint.color = ContextCompat.getColor(this, R.color.color_palette_purple)
            }

            else -> {

            }
        }

        path.reset()
    }

    /**
     * "clears" the canvas; just paint white ;)
     * */
    private fun clearCanvas() {
        var canvas = Canvas(drawBitmap)
        canvas.drawColor(Color.WHITE)
        canvas = surfaceHolder.lockCanvas(null)
        canvas.drawBitmap(drawBitmap, 0f, 0f, null)
        surfaceHolder.unlockCanvasAndPost(canvas)
        path.reset()
    }
}




