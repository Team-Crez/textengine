package io.github.cheesesand.textengine.wrapper

import io.github.cheesesand.textengine.event.EventStorage
import io.github.cheesesand.textengine.graphics.GameFrame

class FrameContainer(private val frame: GameFrame) {
    val deltaTime: Long
        get() = frame.deltaTime

    fun update(init: FrameData.() -> Unit) {
        EventStorage.updateFunction = init
        EventStorage.frame.launch()
    }

    fun keyStroke(key: Int, init: KeyStrokeEvent.() -> Unit) {
        if (key !in EventStorage.keyStrokeMap) {
            EventStorage.keyStrokeMap[key] = ArrayList()
        }

        EventStorage.keyStrokeMap[key]!!.add(init)
    }

    fun keyDown(key: Int, init: KeyStrokeEvent.() -> Unit) {
        if (key !in EventStorage.keyDownMap) {
            EventStorage.keyDownMap[key] = ArrayList()
        }

        EventStorage.keyDownMap[key]!!.add(init)
    }

}