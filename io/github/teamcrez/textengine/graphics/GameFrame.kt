package io.github.cheesesand.textengine.graphics

import io.github.cheesesand.textengine.event.EventStorage
import io.github.cheesesand.textengine.wrapper.FrameContainer
import io.github.cheesesand.textengine.wrapper.FrameData
import io.github.cheesesand.textengine.wrapper.KeyStrokeEvent
import kotlinx.coroutines.*
import java.awt.Color
import java.awt.Dimension
import java.awt.GraphicsEnvironment
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame

@OptIn(DelicateCoroutinesApi::class)
class GameFrame(gameWidth: Int, gameHeight: Int, fontSize: Double, layer: UShort = 1u,
                val targetFPS: Long = 60, init: FrameContainer.() -> Unit) : JFrame(), KeyListener {

    private val mainLayer = Layer(gameWidth, gameHeight, fontSize)
    private val layers: Array<Layer> = Array(layer.toInt()) { Layer(gameWidth, gameHeight, fontSize) }
    private var currentUpdateTime: Long = System.nanoTime()
    private var currentDrawTime: Long = System.nanoTime()

    var deltaTime = 0L
    var frameCount = 0L
    private val frameContainer: FrameContainer

    constructor(
        gameWidth: Int, gameHeight: Int, fontSize: Double, layer: UShort = 1u,
        targetFPS: Int = 60, init: FrameContainer.() -> Unit
    ) : this(
        gameWidth, gameHeight, fontSize, layer, targetFPS.toLong(), init
    )

    val defaultRefreshRate = GraphicsEnvironment.getLocalGraphicsEnvironment()
        .defaultScreenDevice.displayMode.refreshRate

    init {
        // this.size = Dimension(((gameWidth + 1) * fontSize / 2).toInt(), (gameHeight * fontSize).toInt())
        this.layout = null
        this.isFocusable = true
        this.addKeyListener(this)

        this.mainLayer.internalLayer.layout = null
        layers.forEach {
            mainLayer.internalLayer.add(it.internalLayer)
        }

        contentPane.add(mainLayer.internalLayer)

        this.defaultCloseOperation = EXIT_ON_CLOSE

        this.isUndecorated = true
        this.size = Dimension(((gameWidth + 1) * fontSize / 2).toInt(), (gameHeight * fontSize).toInt())
        this.background = Color(0, 0, 0, 255)

        this.setLocationRelativeTo(null)
        this.isVisible = true

        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent) {
                // 창이 닫힐 때 호출됨
            }
        })

        EventStorage.frame = this

        frameContainer = FrameContainer(this)
        init(frameContainer)
    }

    fun launch() {
        GlobalScope.launch(Dispatchers.IO) {
            val targetDelay = 1000000000.0 / targetFPS
            val targetDrawDelay = 1000000000.0 / defaultRefreshRate

            while (isActive) {
                val tempTime = System.nanoTime()

                if (tempTime - currentUpdateTime >= targetDelay) {
                    deltaTime = tempTime - currentUpdateTime
                    currentUpdateTime = tempTime

                    launch(Dispatchers.Default) {
                        EventStorage.keyStrokeStatus.keys.forEach { key ->
                            if (EventStorage.keyStrokeStatus[key] == true) {
                                val event = KeyStrokeEvent(this@GameFrame, key)
                                EventStorage.keyStrokeMap[key]?.forEach { it(event) }

                                if (EventStorage.keyDownStatus[key] == true) {
                                    EventStorage.keyDownMap[key]?.forEach { it(event) }
                                    EventStorage.keyDownStatus[key] = false
                                }
                            }


                        }
                    }

                    EventStorage.updateFunction?.let {
                        it(
                            FrameData(this@GameFrame, layers, frameCount, targetFPS)
                        )
                    }

                    if (tempTime - currentDrawTime >= targetDrawDelay) {
                        currentDrawTime = tempTime
                        this@GameFrame.repaint()
                    }

                    frameCount++
                }


            }
        }
    }

    override fun keyTyped(e: KeyEvent?) {

    }

    override fun keyPressed(e: KeyEvent?) {
        if (EventStorage.keyStrokeStatus[e!!.keyCode] != true) {
            EventStorage.keyDownStatus[e.keyCode] = true
        }

        EventStorage.keyStrokeStatus[e.keyCode] = true
    }

    override fun keyReleased(e: KeyEvent?) {
        EventStorage.keyStrokeStatus[e!!.keyCode] = false
    }
}