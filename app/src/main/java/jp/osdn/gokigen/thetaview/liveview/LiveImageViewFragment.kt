package jp.osdn.gokigen.thetaview.liveview

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import jp.osdn.gokigen.thetaview.R
import jp.osdn.gokigen.thetaview.bluetooth.connection.IBluetoothStatusNotify
import jp.osdn.gokigen.thetaview.operation.ICameraControl
import jp.osdn.gokigen.thetaview.preference.IPreferencePropertyAccessor
import jp.osdn.gokigen.thetaview.preference.PreferenceAccessWrapper
import jp.osdn.gokigen.thetaview.scene.IIndicator

class LiveImageViewFragment(private val contentLayoutId: Int = R.layout.glsurface_view) : Fragment(contentLayoutId), View.OnTouchListener, GestureDetector.OnGestureListener, ScaleGestureDetector.OnScaleGestureListener, IIndicator
{
    private lateinit var liveViewView : View
    private lateinit var cameraControl: ICameraControl
    private lateinit var bluetoothStatusNotify : IBluetoothStatusNotify
    private lateinit var gestureDetector : GestureDetector
    private lateinit var scaleGestureDetector : ScaleGestureDetector
    private lateinit var imageView : GokigenGLView
    private lateinit var informationView : CanvasView

    companion object
    {
        private val TAG = toString()
        fun newInstance() = LiveImageViewFragment().apply { }
    }

    fun setCameraControl(cameraControl : ICameraControl)
    {
        this.cameraControl = cameraControl
    }

    fun setBluetoothStatusNotify(bluetoothStatusNotify : IBluetoothStatusNotify)
    {
        this.bluetoothStatusNotify = bluetoothStatusNotify
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        val context = context
        //　入力の初期化
        gestureDetector = GestureDetector(context, this)
        gestureDetector.setIsLongpressEnabled(true)
        if (context != null)
        {
            scaleGestureDetector = ScaleGestureDetector(context, this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        if (::liveViewView.isInitialized)
        {
            return (liveViewView)
        }
        liveViewView = inflater.inflate(contentLayoutId, null, false)
        liveViewView.setOnTouchListener(this)

        imageView = liveViewView.findViewById(R.id.liveViewFinder0)
        informationView = liveViewView.findViewById(R.id.canvasView)
        if (::cameraControl.isInitialized)
        {
            liveViewView.findViewById<ImageButton>(R.id.button_camera)?.setOnClickListener(cameraControl.captureButtonReceiver())

            //cameraControl.setRefresher(imageView, imageView)
            cameraControl.setRefresher(imageView, imageView)

            informationView.setShowCameraStatus(PreferenceAccessWrapper(requireContext()).getBoolean(IPreferencePropertyAccessor.SHOW_CAMERA_STATUS, IPreferencePropertyAccessor.SHOW_CAMERA_STATUS_DEFAULT_VALUE))
        }
        return (liveViewView)
    }

    override fun onResume()
    {
        super.onResume()
        Log.v(TAG, " onResume() : ")
        if (::informationView.isInitialized) {
            informationView.setShowCameraStatus(PreferenceAccessWrapper(requireContext()).getBoolean(IPreferencePropertyAccessor.SHOW_CAMERA_STATUS, IPreferencePropertyAccessor.SHOW_CAMERA_STATUS_DEFAULT_VALUE))
        }
        if (::bluetoothStatusNotify.isInitialized)
        {
            bluetoothStatusNotify.updateBluetoothStatus()
        }
    }

    override fun onPause()
    {
        super.onPause()
        Log.v(TAG, " onPause() : ")
    }

    // View.OnTouchListener
    override fun onTouch(v: View, event: MotionEvent): Boolean
    {
        //Log.v(TAG, " onTouch()")
        return (gestureDetector.onTouchEvent(event) || scaleGestureDetector.onTouchEvent(event))
    }

    // GestureDetector.OnGestureListener
    override fun onDown(e: MotionEvent): Boolean
    {
        //Log.v(TAG, " Gesture onDown")
        return (false)
    }

    // GestureDetector.OnGestureListener
    override fun onShowPress(e: MotionEvent)
    {
        //Log.v(TAG, " Gesture onShowPress")
    }

    // GestureDetector.OnGestureListener
    override fun onSingleTapUp(e: MotionEvent): Boolean
    {
        //Log.v(TAG, " Gesture onSingleTapUp")
        return (false)
    }

    // GestureDetector.OnGestureListener
    override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean
    {
        //Log.v(TAG, " Gesture onScroll")
        imageView.moveView(distanceX, distanceY)
        return (false)
    }

    // GestureDetector.OnGestureListener
    override fun onLongPress(e: MotionEvent)
    {
        //Log.v(TAG, " Gesture onLongPress")
        imageView.resetView()
    }

    // GestureDetector.OnGestureListener
    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean
    {
        //Log.v(TAG, " Gesture onFling")
        return (false)
    }

    // ScaleGestureDetector.OnScaleGestureListener
    override fun onScale(detector: ScaleGestureDetector): Boolean
    {
        //Log.v(TAG, " Gesture onScale")
        try
        {
            imageView.setScaleFactor(detector.scaleFactor)
            return (true)
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return (false)
    }

    // ScaleGestureDetector.OnScaleGestureListener
    override fun onScaleBegin(detector: ScaleGestureDetector): Boolean
    {
        //Log.v(TAG, " Gesture onScaleBegin")
        return (true)
    }

    // ScaleGestureDetector.OnScaleGestureListener
    override fun onScaleEnd(detector: ScaleGestureDetector)
    {
        //Log.v(TAG, " Gesture onScaleEnd")
    }


    override fun setMessage(area: IIndicator.Area, color: Int, message: String)
    {
        //Log.v(TAG, ">>> $area[$color] : $message")
        try
        {
            if (::informationView.isInitialized)
            {
                informationView.setMessage(area, color, message)
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun invalidate()
    {
        try
        {
            if (::informationView.isInitialized)
            {
                informationView.refresh()
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }
}
