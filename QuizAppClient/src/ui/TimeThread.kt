package ui

import client.ClientMain.sendResult
import java.awt.Font
import javax.swing.JFrame
import javax.swing.JTextField

class TimeThread internal constructor(private val parentFrame: JFrame, private val timeTxt: JTextField, private val time: Int) : Runnable {
    var isOver = false
        private set

    override fun run() {
        for (i in time downTo 0) {
            timeTxt.font = Font(Font.SANS_SERIF, Font.BOLD, 17)
            timeTxt.text = i.toString() + ""
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        isOver = true
        ClientGui.Companion.disableAll()
        val score = sendResult()
        ClientGui.Companion.printScore(parentFrame, score)
    }

    init {
        Thread(this).run()
    }
}