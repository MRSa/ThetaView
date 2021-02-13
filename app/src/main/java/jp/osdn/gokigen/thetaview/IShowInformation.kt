package jp.osdn.gokigen.thetaview

interface IShowInformation
{
    enum class VibratePattern
    {
        NONE, SIMPLE_SHORT, SIMPLE_LONG
    }

    fun showToast(rscId: Int, appendMessage: String, duration: Int)
    fun vibrate(vibratePattern: VibratePattern)
    fun invalidate()

}
