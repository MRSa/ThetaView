package jp.osdn.gokigen.thetaview.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import jp.osdn.gokigen.thetaview.R
import jp.osdn.gokigen.thetaview.operation.ICameraControl

class PreviewFragment(private val contentLayoutId: Int = R.layout.camera_capture) : Fragment(contentLayoutId)
{
    private lateinit var previewView : View
    private lateinit var cameraControl: ICameraControl

    companion object
    {
        fun newInstance() = PreviewFragment().apply { }
    }

    fun setCameraControl(cameraControl : ICameraControl)
    {
        this.cameraControl = cameraControl
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        if (::previewView.isInitialized)
        {
            return (previewView)
        }
        previewView = inflater.inflate(contentLayoutId, null, false)

        if (::cameraControl.isInitialized)
        {
            previewView.findViewById<ImageButton>(R.id.camera_capture_button)?.setOnClickListener(cameraControl.captureButtonReceiver())
        }
        return (previewView)
    }
}
