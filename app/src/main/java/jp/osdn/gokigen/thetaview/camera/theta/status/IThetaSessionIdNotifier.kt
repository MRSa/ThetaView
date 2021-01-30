package jp.osdn.gokigen.thetaview.camera.theta.status

interface IThetaSessionIdNotifier
{
    fun receivedSessionId(sessionId: String?)
}
