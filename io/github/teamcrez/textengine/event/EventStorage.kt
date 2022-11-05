package io.github.cheesesand.textengine.event

import io.github.cheesesand.textengine.graphics.GameFrame
import io.github.cheesesand.textengine.wrapper.FrameData
import io.github.cheesesand.textengine.wrapper.KeyStrokeEvent

object EventStorage {

    lateinit var frame: GameFrame

    val keyStrokeMap = HashMap<Int, ArrayList<KeyStrokeEvent.() -> Unit>>()
    val keyDownMap = HashMap<Int, ArrayList<KeyStrokeEvent.() -> Unit>>()

    val keyStrokeStatus = HashMap<Int, Boolean>()
    val keyDownStatus = HashMap<Int, Boolean>()

    var updateFunction: (FrameData.() -> Unit)? = null

}