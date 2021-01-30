package jp.osdn.gokigen.thetaview.liveview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import jp.osdn.gokigen.thetaview.R
import jp.osdn.gokigen.thetaview.operation.ICameraControl

//class LiveImageViewFragment(private val contentLayoutId: Int = R.layout.liveimage_view) : Fragment(contentLayoutId)
class LiveImageViewFragment(private val contentLayoutId: Int = R.layout.glsurface_view) : Fragment(contentLayoutId)
{
    private lateinit var liveviewView : View
    private lateinit var cameraControl: ICameraControl

    companion object
    {
        private val TAG = toString()
        fun newInstance() = LiveImageViewFragment().apply { }
    }

    fun setCameraControl(cameraControl : ICameraControl)
    {
        this.cameraControl = cameraControl
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        if (::liveviewView.isInitialized)
        {
            return (liveviewView)
        }
        liveviewView = inflater.inflate(contentLayoutId, null, false)
        //val imageView = liveviewView.findViewById<LiveImageView>(R.id.liveViewFinder0)
        val imageView = liveviewView.findViewById<GokigenGLView>(R.id.liveViewFinder0)
        if (::cameraControl.isInitialized)
        {
            liveviewView.findViewById<ImageButton>(R.id.button_camera)?.setOnClickListener(cameraControl.captureButtonReceiver())
        }
        //cameraControl.setRefresher(imageView, imageView)
        cameraControl.setRefresher(imageView, imageView)

        return (liveviewView)
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
}
