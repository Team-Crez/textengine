package io.github.cheesesand.textengine.loader

import java.awt.image.BufferedImage
import java.io.File
import java.io.InputStream
import java.nio.charset.Charset
import javax.imageio.ImageIO

object ResourceLoader {

    private var atlasCache = HashMap<Int, BufferedImage>()

    fun getResourceStream(path: String): InputStream? =
        javaClass.getResourceAsStream("/${path}")

    fun getObjectResource(path: String) = getResourceStream("object/${path}")?.bufferedReader(
            Charset.forName("UTF-8")
        )!!.readLines()

    fun loadTextAtlas(code: Int): BufferedImage {
        if (code / 256 !in atlasCache) {
            atlasCache[code / 256] = ImageIO.read(File("assets/text/atlas_${code / 256}.png"))
        }

        return atlasCache[code / 256]!!
    }

}