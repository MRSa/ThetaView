package jp.osdn.gokigen.thetaview.utils.logcat

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemLongClickListener
import jp.osdn.gokigen.thetaview.R
import jp.osdn.gokigen.thetaview.utils.ConfirmationDialog


class LogCatExporter(private val activity: Activity) : OnItemLongClickListener
{
    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean
    {
        Log.v(TAG, "onItemLongClick()")

        val confirm: ConfirmationDialog = ConfirmationDialog.newInstance(activity)

        confirm.show(
            R.string.dialog_confirm_title_output_log,
            R.string.dialog_confirm_message_output_log,
            object : ConfirmationDialog.Callback
            {
                override fun confirm()
                {
                    Log.v(TAG, "confirm()")
                    try
                    {
                        val buf = StringBuilder()
                        val adapter = parent?.adapter
                        if (adapter != null)
                        {

                            for (index in 0 until adapter.count)
                            {
                                buf.append(adapter.getItem(index))
                                buf.append("\r\n")
                            }
                        }
                        val intent = Intent()
                        intent.action = Intent.ACTION_SEND
                        intent.type = "text/plain"
                        intent.putExtra(Intent.EXTRA_TITLE, "debug log for " + activity.getString(R.string.app_name))
                        intent.putExtra(Intent.EXTRA_TEXT, String(buf))
                        activity.startActivity(intent)
                    }
                    catch (e: Exception)
                    {
                        Log.v(TAG, " Exception : " + e.localizedMessage)
                        e.printStackTrace()
                    }
                }
            })
        return true
    }

    companion object
    {
        private val  TAG: String = this.toString()
    }
}
