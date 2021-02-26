package jp.osdn.gokigen.thetaview.brainwave

interface IDetectSensingReceiver
{
    fun startSensing()
    fun detectAttention()
    fun lostAttention()
    fun detectAttentionThreshold()
    fun detectMediation()
    fun lostMediation()
    fun detectMediationThreshold()
}
