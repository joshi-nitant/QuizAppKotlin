package ui

import client.ClientMain
import java.awt.Font
import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JTextField

class SearchingThread(var frame: ClientGui) : Thread() {
    private var startPressed: Boolean = false
    var isOpponentFound: Boolean = false

    override fun run() {
        val font = Font(Font.SANS_SERIF, Font.BOLD, 20)
        val nameTxt = JTextField()
        nameTxt.setBounds(15, 30, 280, 50)
        nameTxt.font = font
        nameTxt.border = null
        nameTxt.isEditable = false
        val buttonSubmit = JButton("Start")
        buttonSubmit.setBounds(90, 210, 100, 40)
        buttonSubmit.isEnabled = false
        val dialog = JDialog(frame, "Name")
        dialog.layout = null
        dialog.setSize(300, 300)
        dialog.add(nameTxt)
        dialog.add(buttonSubmit)
        dialog.setLocationRelativeTo(null)
        dialog.isVisible = true
        buttonSubmit.addActionListener {
            dialog.dispose()
            startPressed = true
            //frame.start();
        }
        while (!isOpponentFound) {
            nameTxt.text = "Searching for opponent............"
        }
        nameTxt.text = "Your Opponent = " + ClientMain.opponentName
        try {
            sleep(1000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        buttonSubmit.isEnabled = true
    }

    companion object {
        var isOpponentFound = false
        var isStartPressed = false
            get() {
                try {
                    sleep(30)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                return field
            }
            set(startPressed) {
                try {
                    sleep(30)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                field = startPressed
            }

    }

}