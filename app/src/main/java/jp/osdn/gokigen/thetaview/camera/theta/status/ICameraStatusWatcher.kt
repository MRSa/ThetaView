package jp.osdn.gokigen.thetaview.camera.theta.status

import jp.osdn.gokigen.thetaview.scene.IIndicator

interface ICameraStatusWatcher
{
    fun startStatusWatch(indicator : IIndicator?)
    fun stopStatusWatch()
}
