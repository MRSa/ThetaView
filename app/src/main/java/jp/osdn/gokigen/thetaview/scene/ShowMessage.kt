package jp.osdn.gokigen.thetaview.scene

import android.graphics.Color
import android.graphics.Typeface
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import jp.osdn.gokigen.thetaview.R

class ShowMessage(private val activity : AppCompatActivity) : IInformationReceiver
{
    override fun updateMessage(message: String, isBold: Boolean, isColor: Boolean, color: Int)
    {
        val messageArea = activity.findViewById<TextView>(R.id.message)
        activity.runOnUiThread {
            messageArea?.text = message
            if (isBold)
            {
                messageArea?.typeface = Typeface.DEFAULT_BOLD
            }
            if (isColor)
            {
                messageArea?.setTextColor(color)
            }
            else
            {
                messageArea?.setTextColor(Color.DKGRAY)
            }
            messageArea?.invalidate()
        }
    }
}
