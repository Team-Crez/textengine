package io.github.cheesesand.textengine.components

import io.github.cheesesand.textengine.graphics.Layer
import io.github.cheesesand.textengine.loader.ResourceLoader
import kotlin.math.ceil

class TextObject(path: String) {
    private val internalText: List<String>

    private val componentWidth: Int
    private val componentHeight: Int

    init {
        internalText = ResourceLoader.getObjectResource("$path.txt")

        var tempWidth = 0
        internalText.forEach {
            if (tempWidth < it.length)
                tempWidth = it.length
        }

        componentWidth = tempWidth
        componentHeight = internalText.size
    }

    fun draw(layer: Layer, pos: Pair<Int, Int>, centered: Boolean = false) {
        if (!centered) {
            internalDraw(layer, pos)
        } else {
            internalDraw(layer, Pair(pos.first - componentWidth / 2, pos.second - ceil(componentHeight / 2.0).toInt()))
        }
    }

    @JvmName("draw_d")
    fun draw(layer: Layer, pos: Pair<Double, Double>, centered: Boolean = false) {
        draw(layer, Pair(pos.first.toInt(), pos.second.toInt()), centered)
    }

    private fun internalDraw(layer: Layer, pos: Pair<Int, Int>) {
        if (layer.screen[0].length > pos.first) {
            for (y in 0 until componentHeight) {
                var x = 0
                for (i in 0 until componentWidth) {
                    val currX = x + pos.first
                    val currY = y + pos.second
                    if (currY >= 0 && layer.screen.size > currY) {
                        if (
                            (internalText[y].length > i && layer.screen[currY].length > currX)
                        ) {
                            if (currX >= 0) {
                                layer.screen[currY][currX] = internalText[y][i]

                                if (!internalText[y][i].isSingleSpaced()) {
                                    if (layer.screen[currY].length > currX + 1)
                                        layer.screen[currY][currX + 1] = ' '
                                    x++
                                } else if (currX > 0 && !layer.screen[currY][currX - 1].isSingleSpaced()) {
                                    layer.screen[currY][currX - 1] = ' '
                                }
                            }
                        } else {
                            break
                        }

                        x++
                    } else {
                        break
                    }
                }
            }
        }
    }

    private var singleSpacedSpecialsArray = arrayOf(
        ' ', '`', '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '_', '=', '+', '[', ']', '\\', '|', ':', ';',
        '\"', '\'', ',', '<', '.', '>', '/', '?'
    )

    private fun Char.isSingleSpaced() =
        this in 'a'..'z' || this in 'A'..'Z' || this in '0'..'9' || singleSpacedSpecialsArray.contains(this)
}