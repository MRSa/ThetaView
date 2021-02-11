package jp.osdn.gokigen.thetaview

interface IShowInformation
{
    enum class VibratePattern
    {
        NONE, SIMPLE_SHORT, SIMPLE_LONG
    }

    enum class Area
    {
        AREA_NONE,
        AREA_C,
        AREA_1,
        AREA_2,
        AREA_3,
        AREA_4,
        AREA_5,
        AREA_6,
        AREA_7,
        AREA_8,
        AREA_9,
        AREA_A,
        AREA_B,
        AREA_D,
    }

    fun showToast(rscId: Int, appendMessage: String, duration: Int)
    fun vibrate(vibratePattern: VibratePattern)
    fun invalidate()

    fun setMessage(area: Area, color: Int, message: String)
}
