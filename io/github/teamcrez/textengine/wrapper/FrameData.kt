package io.github.cheesesand.textengine.wrapper

import io.github.cheesesand.textengine.graphics.GameFrame
import io.github.cheesesand.textengine.graphics.Layer

class FrameData(
    val frame: GameFrame, val layers: Array<Layer>,
    val frameCount: Long, val targetFPS: Long
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FrameData

        if (frame != other.frame) return false
        if (!layers.contentEquals(other.layers)) return false
        if (frameCount != other.frameCount) return false
        if (targetFPS != other.targetFPS) return false

        return true
    }

    override fun hashCode(): Int {
        var result = frame.hashCode()
        result = 31 * result + layers.contentHashCode()
        result = 31 * result + frameCount.hashCode()
        result = 31 * result + targetFPS.hashCode()
        return result
    }
}