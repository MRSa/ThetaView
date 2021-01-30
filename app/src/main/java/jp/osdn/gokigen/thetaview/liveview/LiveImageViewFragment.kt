package jp.osdn.gokigen.thetaview.liveview

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import jp.osdn.gokigen.thetaview.R
import jp.osdn.gokigen.thetaview.operation.ICameraControl

//class LiveImageViewFragment(private val contentLayoutId: Int = R.layout.liveimage_view) : Fragment(contentLayoutId)
class LiveImageViewFragment(private val contentLayoutId: Int = R.layout.glsurface_view) : Fragment(contentLayoutId), View.OnTouchListener, GestureDetector.OnGestureListener, ScaleGestureDetector.OnScaleGestureListener
{
    private lateinit var liveViewView : View
    private lateinit var cameraControl: ICameraControl
    private lateinit var gestureDetector : GestureDetector
    private lateinit var scaleGestureDetector : ScaleGestureDetector
    private lateinit var imageView : GokigenGLView

    companion object
    {
        private val TAG = toString()
        fun newInstance() = LiveImageViewFragment().apply { }
    }

    fun setCameraControl(cameraControl : ICameraControl)
    {
        this.cameraControl = cameraControl
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        //　入力の初期化
        gestureDetector = GestureDetector(context, this)
        scaleGestureDetector = ScaleGestureDetector(context, this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        if (::liveViewView.isInitialized)
        {
            return (liveViewView)
        }
        liveViewView = inflater.inflate(contentLayoutId, null, false)
        liveViewView.setOnTouchListener(this)

        //val imageView = liveviewView.findViewById<LiveImageView>(R.id.liveViewFinder0)
        imageView = liveViewView.findViewById<GokigenGLView>(R.id.liveViewFinder0)
        if (::cameraControl.isInitialized)
        {
            liveViewView.findViewById<ImageButton>(R.id.button_camera)?.setOnClickListener(cameraControl.captureButtonReceiver())
        }
        //cameraControl.setRefresher(imageView, imageView)
        cameraControl.setRefresher(imageView, imageView)

        return (liveViewView)
    }

    override fun onResume()
    {
        super.onResume()
        Log.v(TAG, " onResume() : ")
    }

    override fun onPause()
    {
        super.onPause()
        Log.v(TAG, " onPause() : ")
    }

    // View.OnTouchListener
    override fun onTouch(v: View?, event: MotionEvent?): Boolean
    {
        //Log.v(TAG, " onTouch()")
        if (event == null)
        {
            v?.performClick()
            return (false)
        }
        return (scaleGestureDetector.onTouchEvent(event) || gestureDetector.onTouchEvent(event))
    }

    // GestureDetector.OnGestureListener
    override fun onDown(e: MotionEvent?): Boolean
    {
        Log.v(TAG, " Gesture onDown")
        return (false)
    }

    // GestureDetector.OnGestureListener
    override fun onShowPress(e: MotionEvent?)
    {
        Log.v(TAG, " Gesture onShowPress")
    }

    // GestureDetector.OnGestureListener
    override fun onSingleTapUp(e: MotionEvent?): Boolean
    {
        Log.v(TAG, " Gesture onSingleTapUp")
        return (false)
    }

    // GestureDetector.OnGestureListener
    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean
    {
        Log.v(TAG, " Gesture onScroll")
        return (false)
    }

    // GestureDetector.OnGestureListener
    override fun onLongPress(e: MotionEvent?)
    {
        Log.v(TAG, " Gesture onLongPress")
    }

    // GestureDetector.OnGestureListener
    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean
    {
        Log.v(TAG, " Gesture onFling")
        return (false)
    }

    // ScaleGestureDetector.OnScaleGestureListener
    override fun onScale(detector: ScaleGestureDetector?): Boolean
    {
        Log.v(TAG, " Gesture onScale")
        try
        {
            if (detector != null)
            {
                imageView.setScaleFactor(detector.scaleFactor)
                return (true)
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return (false)
    }

    // ScaleGestureDetector.OnScaleGestureListener
    override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean
    {
        Log.v(TAG, " Gesture onScaleBegin")
        return (true)
    }

    // ScaleGestureDetector.OnScaleGestureListener
    override fun onScaleEnd(detector: ScaleGestureDetector?)
    {
        Log.v(TAG, " Gesture onScaleEnd")
    }
}
