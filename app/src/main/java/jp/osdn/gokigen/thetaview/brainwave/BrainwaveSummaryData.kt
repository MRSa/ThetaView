package jp.osdn.gokigen.thetaview.brainwave

import kotlin.experimental.and

@ExperimentalUnsignedTypes
class BrainwaveSummaryData
{
    //  3-byte value : delta (0.5 - 2.75Hz), theta (3.5 - 6.75Hz), low-alpha (7.5 - 9.25Hz), high-alpha (10 - 11.75Hz), low-beta (13 - 16.75Hz), high-beta (18 - 29.75Hz), low-gamma (31 - 39.75Hz), and mid-gamma (41 - 49.75Hz).
    private var delta : ULong = 0u
    private var theta : ULong = 0u
    private var lowAlpha : ULong = 0u
    private var highAlpha : ULong = 0u
    private var lowBeta  : ULong= 0u
    private var highBeta : ULong = 0u
    private var lowGamma : ULong = 0u
    private var midGamma  : ULong = 0u
    private var poorSignal = 0
    private var attention = 0
    private var mediation = 0

    fun update(packet: ByteArray): Boolean
    {
        var ret = false
        try
        {
            val length = packet.size
            if (length < 36)
            {
                return ret
            }
            poorSignal = packet[4].toInt()
            delta = ((packet[7].toUByte() and 0xff.toUByte()).toULong() * 65536u + (packet[8].toUByte() and 0xff.toUByte()).toULong() * 256u + (packet[9].toUByte() and 0xff.toUByte()).toULong())
            theta = ((packet[10].toUByte() and 0xff.toUByte()).toULong() * 65536u + (packet[11].toUByte() and 0xff.toUByte()).toULong() * 256u + (packet[12].toUByte() and 0xff.toUByte()).toULong())
            lowAlpha = ((packet[13].toUByte() and 0xff.toUByte()).toULong() * 65536u + (packet[14].toUByte() and 0xff.toUByte()).toULong() * 256u + (packet[15].toUByte() and 0xff.toUByte()).toULong())
            highAlpha = ((packet[16].toUByte() and 0xff.toUByte()).toULong() * 65536u + (packet[17].toUByte() and 0xff.toUByte()).toULong() * 256u + (packet[18].toUByte() and 0xff.toUByte()).toULong())
            lowBeta = ((packet[19].toUByte() and 0xff.toUByte()).toULong() * 65536u + (packet[20].toUByte() and 0xff.toUByte()).toULong() * 256u + (packet[21].toUByte() and 0xff.toUByte()).toULong())
            highBeta = ((packet[22].toUByte() and 0xff.toUByte()).toULong() * 65536u + (packet[23].toUByte() and 0xff.toUByte()).toULong() * 256u + (packet[24].toUByte() and 0xff.toUByte()).toULong())
            lowGamma = ((packet[25].toUByte() and 0xff.toUByte()).toULong() * 65536u + (packet[26].toUByte() and 0xff.toUByte()).toULong() * 256u + (packet[27].toUByte() and 0xff.toUByte()).toULong())
            midGamma = ((packet[28].toUByte() and 0xff.toUByte()).toULong() * 65536u + (packet[29].toUByte() and 0xff.toUByte()).toULong() * 256u + (packet[30].toUByte() and 0xff.toUByte()).toULong())
            attention = ((packet[32] and 0xff.toByte()).toInt())
            mediation = ((packet[34] and 0xff.toByte()).toInt())
            ret = true
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return ret
    }

    fun isSkinConnected(): Boolean
    {
        return poorSignal != 200
    }

    fun getPoorSignal(): Int
    {
        return poorSignal
    }

    fun getDelta(): Long
    {
        return delta.toLong()
    }

    fun getTheta(): Long
    {
        return theta.toLong()
    }

    fun getLowAlpha(): Long
    {
        return lowAlpha.toLong()
    }

    fun getHighAlpha(): Long
    {
        return highAlpha.toLong()
    }

    fun getLowBeta(): Long
    {
        return lowBeta.toLong()
    }

    fun getHighBeta(): Long
    {
        return highBeta.toLong()
    }

    fun getLowGamma(): Long
    {
        return lowGamma.toLong()
    }

    fun getMidGamma(): Long
    {
        return midGamma.toLong()
    }

    fun getAttention(): Int
    {
        return attention
    }

    fun getMediation(): Int
    {
        return mediation
    }
}