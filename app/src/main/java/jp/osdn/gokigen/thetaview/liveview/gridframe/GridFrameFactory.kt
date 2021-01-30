package jp.osdn.gokigen.thetaview.liveview.gridframe

class GridFrameFactory
{
    fun getGridFrameDrawer(id: Int): IGridFrameDrawer
    {
        return (GridFrameDrawerDefault())
    }
}
