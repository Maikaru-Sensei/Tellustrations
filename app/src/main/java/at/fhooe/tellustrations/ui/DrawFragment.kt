package at.fhooe.tellustrations.ui

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
import androidx.fragment.app.Fragment
import at.fhooe.tellustrations.R
import at.fhooe.tellustrations.databinding.FragmentDrawBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DrawFragment : Fragment(), SurfaceHolder.Callback, View.OnTouchListener, View.OnClickListener,
    DialogInterface.OnClickListener {

    var _binding: FragmentDrawBinding? = null
    val binding get() = _binding!!
    
    private lateinit var surfaceView: SurfaceView
    private lateinit var surfaceHolder: SurfaceHolder
    private lateinit var paint: Paint
    private lateinit var path: Path
    private lateinit var seekBarEraser: SeekBar
    private var defaultBackgroundColor: Int = Color.WHITE
    private var defaultPaintWidth: Float = 50f
    private lateinit var drawBitmap : Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDrawBinding.inflate(inflater, container, false)

        surfaceView = binding.fragmentDrawSurfaceviewDraw

        surfaceHolder = surfaceView.holder

        surfaceHolder.setFormat(PixelFormat.TRANSPARENT)
        surfaceHolder.addCallback(this)

        surfaceView.setOnTouchListener(this)
        surfaceView.setZOrderOnTop(true)
        surfaceView.setBackgroundColor(defaultBackgroundColor)

        with(binding) {
            fragmentDrawPaletteBlue.setOnClickListener(this@DrawFragment)
            fragmentDrawPaletteGreen.setOnClickListener(this@DrawFragment)
            fragmentDrawPaletteYellow.setOnClickListener(this@DrawFragment)
            fragmentDrawPaletteRed.setOnClickListener(this@DrawFragment)
            fragmentDrawPaletteBlack.setOnClickListener(this@DrawFragment)
            fragmentDrawPalettePurple.setOnClickListener(this@DrawFragment)
            fragmentDrawPaletteOrange.setOnClickListener(this@DrawFragment)
            fragmentDrawPaletteCyan.setOnClickListener(this@DrawFragment)
            fragmentDrawPaletteWhite.setOnClickListener(this@DrawFragment)
            fragmentDrawBrush.setOnClickListener(this@DrawFragment)
            fragmentDrawPen.setOnClickListener(this@DrawFragment)
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
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
            R.id.fragment_draw_brush -> {
                paint.strokeWidth = 150f
            }

            R.id.fragment_draw_pen -> {
                paint.strokeWidth = 50f
            }

            /* COLORS */
            R.id.fragment_draw_palette_blue -> {
                paint.color = ContextCompat.getColor(requireContext(), R.color.color_palette_blue)
            }

            R.id.fragment_draw_palette_green -> {
                paint.color = ContextCompat.getColor(requireContext(), R.color.color_palette_green)
            }

            R.id.fragment_draw_palette_yellow -> {
                paint.color = ContextCompat.getColor(requireContext(), R.color.color_palette_yellow)
            }

            R.id.fragment_draw_palette_red -> {
                paint.color = ContextCompat.getColor(requireContext(), R.color.color_palette_red)
            }

            R.id.fragment_draw_palette_black -> {
                paint.color = ContextCompat.getColor(requireContext(), R.color.color_palette_black)
            }

            R.id.fragment_draw_palette_orange -> {
                paint.color = ContextCompat.getColor(requireContext(), R.color.color_palette_orange)
            }

            R.id.fragment_draw_palette_purple -> {
                paint.color = ContextCompat.getColor(requireContext(), R.color.color_palette_purple)
            }

            R.id.fragment_draw_palette_cyan -> {
                paint.color = ContextCompat.getColor(requireContext(), R.color.color_palette_cyan)
            }

            R.id.fragment_draw_palette_white -> {
                paint.color = ContextCompat.getColor(requireContext(), R.color.white)
            }
            
            /* ERASER */
            R.id.fragment_draw_eraser -> {
                val dialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
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
            
            /* WASTE */
            R.id.fragment_draw_waste -> {
                clearCanvas()
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