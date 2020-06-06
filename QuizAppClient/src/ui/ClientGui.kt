package ui

import client.ClientMain
import client.ClientMain.sendResult
import common.Question
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Font
import java.awt.GridLayout
import java.awt.event.*
import java.util.*
import javax.swing.*

class ClientGui : JFrame(), ActionListener, ItemListener {
    private var questionTxt: JTextArea? = null
    private var answer = ""
    var questionList = ArrayList<Question>()
    private var current: Question? = null
    var tIME: Int? = null
    //var font: Font? = null
    private var timeTxt: JTextField? = null
    var isOpponentFound = false
        get() {
            try {
                Thread.sleep(30)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            return field
        }
    private var isNameEntered = false
    private var nameEntered: String? = null
    fun getNameEntered(): String? {
        //System.out.println("In name entered");
        return nameEntered
    }

    fun setNameEntered(nameEntered: String?) {
        this.nameEntered = nameEntered
    }

    fun isNameEntered(): Boolean {
        try {
            Thread.sleep(100)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return isNameEntered
    }

    fun start() {
        println("In start")
        font = Font(Font.SANS_SERIF, Font.PLAIN, 18)
        layout = BorderLayout(10, 10)
        title = "Quiz"
        questionTxt = JTextArea()
        questionTxt!!.font = font
        questionTxt!!.rows = 7
        questionTxt!!.isEditable = false
        questionTxt!!.wrapStyleWord = true
        questionTxt!!.lineWrap = true
        buttonGroup = ButtonGroup()
        button1 = JButton("Next")
        button1!!.setFont(font)
        button1!!.addActionListener(this)
        val jPanel = JPanel(GridLayout(4, 1))
        option1 = JRadioButton("option 1")
        option1!!.setFont(font)
        option1!!.addItemListener(this)
        option2 = JRadioButton("option 1")
        option2!!.addItemListener(this)
        option2!!.setFont(font)
        option3 = JRadioButton("option 1")
        option3!!.addItemListener(this)
        option3!!.setFont(font)
        option4 = JRadioButton("option 1")
        option4!!.addItemListener(this)
        option4!!.setFont(font)
        buttonGroup!!.add(option1)
        buttonGroup!!.add(option2)
        buttonGroup!!.add(option3)
        buttonGroup!!.add(option4)
        jPanel.add(option1)
        jPanel.add(option2)
        jPanel.add(option3)
        jPanel.add(option4)


        //add(questionTxt, BorderLayout.NORTH);
        add(jPanel, BorderLayout.CENTER)
        add(button1, BorderLayout.SOUTH)
        val upperPanel = JPanel()
        questionTxt!!.setBounds(0, 50, 350, 100)
        timeTxt = JTextField()
        timeTxt!!.setBounds(350, 0, 70, 70)
        upperPanel.add(questionTxt)
        upperPanel.add(timeTxt)
        add(upperPanel, BorderLayout.NORTH)
        setSize(500, 500)
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        isVisible = true
        setLocationRelativeTo(null)


        //add(timeTxt, BorderLayout.EAST);
        nextQuestion()
        TimeThread(this, timeTxt!!, tIME!!)
    }

    override fun actionPerformed(e: ActionEvent) {
        nextQuestion()
        //setAnswer();
    }

    override fun itemStateChanged(e: ItemEvent) {
        answer = (e.source as JRadioButton).text
        questioAnswer[current!!.id] = answer
        button1!!.isEnabled = true
    }

    fun setAnswer() {
        questioAnswer[current!!.id] = answer
    }

    fun nextQuestion() {
        //printScore(0);
        if (index < questionList.size) {
            current = questionList[index]
            //System.out.println(current.getId());
            questionTxt!!.text = current!!.question
            val options = current!!.incorrect_answers
            option1!!.text = options[0]
            option2!!.text = options[1]
            option3!!.text = options[2]
            option4!!.text = options[3]
            buttonGroup!!.clearSelection()
            if (index == questionList.size - 1) {
                button1!!.text = "Submit"
                button1!!.isEnabled = false
            } else {
                button1!!.text = "Next"
                button1!!.isEnabled = false
            }
        } else if (index == questionList.size) {
            //System.out.println("here");
            //button1.setEnabled(false);
            disableAll()
            val score = sendResult()
            //System.out.println("Score = "+score);
            printScore(this, score)
        }
        index++
        //current = questionList.get(index);
    }

    fun showNameDialog() {
        val fontTxt = Font(Font.SANS_SERIF, Font.BOLD, 20)
        val nameTxt = JTextField("Enter Your Name")
        nameTxt.setBounds(15, 30, 280, 50)
        nameTxt.font = fontTxt
        nameTxt.border = null
        /*Adding Placeholder*/nameTxt.addFocusListener(object : FocusListener {
            override fun focusGained(e: FocusEvent) {
                if (nameTxt.text == "Enter Your Name") {
                    nameTxt.text = ""
                    nameTxt.foreground = Color.BLACK
                }
            }

            override fun focusLost(e: FocusEvent) {
                if (nameTxt.text.isEmpty()) {
                    nameTxt.foreground = Color.GRAY
                    nameTxt.text = "Enter Your Name"
                }
            }
        })
        val buttonSubmit = JButton("Submit!!")
        buttonSubmit.setBounds(90, 210, 100, 40)
        val dialog = JDialog(this, "Name")
        dialog.layout = null
        dialog.setSize(300, 300)
        dialog.add(nameTxt)
        dialog.add(buttonSubmit)
        dialog.setLocationRelativeTo(null)
        dialog.isVisible = true
        dialog.requestFocus()
        //dialog.setResizable(true);
        buttonSubmit.addActionListener {
            dialog.isVisible = false
            isNameEntered = true
            setNameEntered(nameTxt.text)
            println(nameEntered)
            dialog.isVisible = false
        }
    }

    companion object {
        private var button1: JButton? = null
        private var option1: JRadioButton? = null
        private var option2: JRadioButton? = null
        private var option3: JRadioButton? = null
        private var option4: JRadioButton? = null
        private var buttonGroup: ButtonGroup? = null
        private var index = 0
        val questioAnswer: HashMap<Any?, Any?> = HashMap<Any?, Any?>()

        fun disableAll() {
            button1!!.isEnabled = false
            buttonGroup!!.clearSelection()
            option1!!.isEnabled = false
            option2!!.isEnabled = false
            option3!!.isEnabled = false
            option4!!.isEnabled = false
        }

        fun printScore(parentFrame: JFrame, score: IntArray) {
            val font = Font(Font.SANS_SERIF, Font.BOLD, 20)
            val highlightTxt = JTextField()
            highlightTxt.isEditable = false
            highlightTxt.font = font
            highlightTxt.border = null
            if (score[2] == 1) {
                highlightTxt.setBounds(60, 30, 300, 50)
                highlightTxt.text = "Congratulations you won!!!!"
            } else {
                highlightTxt.setBounds(110, 30, 300, 50)
                highlightTxt.text = "Sorry You Lost!!!!"
            }
            val myscoreTxt = JTextField("Your Score = " + score[0])
            myscoreTxt.setBounds(120, 90, 200, 50)
            myscoreTxt.isEditable = false
            myscoreTxt.font = font
            myscoreTxt.border = null
            val opponentTxt = JTextField(ClientMain.opponentName + "'s Score = " + score[1])
            opponentTxt.setBounds(120, 150, 200, 50)
            opponentTxt.isEditable = false
            opponentTxt.font = font
            opponentTxt.border = null
            val buttonExit = JButton("OK!!")
            buttonExit.setBounds(150, 320, 100, 40)
            val dialog = JDialog(parentFrame, "Score")
            dialog.layout = null
            dialog.setSize(400, 400)
            dialog.add(highlightTxt)
            dialog.add(myscoreTxt)
            dialog.add(opponentTxt)
            dialog.add(buttonExit)
            dialog.setLocationRelativeTo(null)
            dialog.isVisible = true
            buttonExit.addActionListener {
                dialog.isVisible = false
                parentFrame.dispose()
            }
        }

        fun getQuestionAnswer(): HashMap<Any?, Any?> {
            return this.questioAnswer;
        }
    }
}