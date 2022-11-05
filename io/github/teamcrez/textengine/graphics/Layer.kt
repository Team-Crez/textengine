package io.github.cheesesand.textengine.graphics

import io.github.cheesesand.textengine.loader.ResourceLoader
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JPanel

class Layer(val width: Int, height: Int, private val fontSize: Double) {
    val atlasFontSize = fontSize
    val internalLayer = LayerPanel(this)

    val transparent = Color(0, 0, 0, 0)
    init {
        internalLayer.size = Dimension(
            (width * fontSize).toInt(), (height * fontSize).toInt()
        )

        internalLayer.background = transparent
        internalLayer.isDoubleBuffered = true
        internalLayer.isOpaque = true
    }

    val screen = Array(height) { StringBuilder(" ".repeat(width + 1)) }

    class LayerPanel(private val layer: Layer) : JPanel() {
        override fun paintComponent(g: Graphics?) {
            val graphics = g as Graphics2D

            if (background != layer.transparent) {
                graphics.color = background
                graphics.fillRect(0, 0, width, height)
            }

            val defaultLine = " ".repeat(layer.width + 1)
            layer.screen.forEachIndexed { y, line ->
                line.forEachIndexed { x, char ->
                    if (char != ' ') {
                        val atlas = ResourceLoader.loadTextAtlas(char.code)

                        val atlasCharID = char.code % 256
                        val atlasX = atlasCharID % 16
                        val atlasY = atlasCharID / 16

                        graphics.drawImage(
                            atlas,
                            (layer.fontSize / 2 * x).toInt(), (layer.fontSize * y).toInt(),
                            (layer.fontSize / 2 * x + layer.fontSize).toInt(), (layer.fontSize * (y + 1)).toInt(),
                            (atlasX * layer.atlasFontSize).toInt(), (atlasY * layer.atlasFontSize).toInt(),
                            ((atlasX + 1) * layer.atlasFontSize).toInt(), ((atlasY + 1) * layer.atlasFontSize).toInt(),
                            null
                        )
                    }
                }

                layer.screen[y] = StringBuilder(defaultLine)
            }
        }
    }

}