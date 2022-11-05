package io.github.cheesesand.textengine.generator

import io.github.cheesesand.textengine.loader.ResourceLoader
import io.github.cheesesand.textengine.manager.AssetManager
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.awt.Rectangle
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JLabel
import javax.swing.JPanel
import kotlin.math.ceil

object TextAtlasGenerator {

    fun generate(fontSize: Double = 30.0) {
        var currentChar = '\u0000'
        AssetManager.checkAssetDir("text")

        val atlasWidth = 16
        val atlasHeight = 16

        var defaultFont = Font.createFont(Font.TRUETYPE_FONT, ResourceLoader.getResourceStream("D2CodingBold-Ver1.3.2-20180524.ttf"))
        defaultFont = defaultFont.deriveFont(fontSize.toFloat())

        var atlasID = 0
        var isCharsEnded = false
        while (!isCharsEnded) {
            val panel = JPanel()

            panel.size = Dimension(ceil(fontSize * atlasWidth).toInt(), ceil(fontSize * atlasHeight).toInt())
            panel.layout = null

            for (y in 0 until atlasHeight) {
                for (x in 0 until atlasWidth) {
                    val newLabel = JLabel()
                    newLabel.text = currentChar.toString()
                    newLabel.font = defaultFont

                    newLabel.foreground = Color.WHITE

                    newLabel.bounds = Rectangle(
                        (x * fontSize).toInt(), (y * fontSize).toInt(), fontSize.toInt(), fontSize.toInt()
                    )

                    panel.add(newLabel)

                    if (currentChar >= Char.MAX_VALUE) {
                        isCharsEnded = true
                        break
                    } else {
                        currentChar = Char(currentChar.code + 1)
                    }
                }

                if (isCharsEnded) {
                    break
                }
            }

            panel.isVisible = true
            panel.isOpaque = false
            panel.background = Color(0, 0, 0, 0)

            val image = BufferedImage(panel.width, panel.height, BufferedImage.TYPE_INT_ARGB)
            val graphics = image.createGraphics()
            panel.printAll(graphics)

            graphics.dispose()

            ImageIO.write(image, "png", File("assets/text/atlas_${atlasID}.png"))
            atlasID++

            panel.isEnabled = false
        }
    }

    /*
    private val aliasTable = mapOf(
        '좄' to '임'
    )

    private fun checkAliasTable(char: Char): String {
        if (char in aliasTable)
            return aliasTable[char].toString()
        return char.toString()
    }
    */
}