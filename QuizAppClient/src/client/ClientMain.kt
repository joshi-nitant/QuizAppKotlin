package client

import common.Question
import ui.ClientGui
import ui.SearchingThread
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket
import java.net.UnknownHostException
import java.util.*

object ClientMain {
    private const val IP = "192.168.56.1"
    private const val PORT = 5000
    private val questionList = ArrayList<Question>()
    private var socket: Socket? = null
    private var SIZE: Int? = null
    private var outputStream: ObjectOutputStream? = null
    private var inputStream: ObjectInputStream? = null
    private var clientGui: ClientGui? = null
    private var myName: String? = null
    @JvmField
    var opponentName: String? = null

    @JvmStatic
    fun main(args: Array<String>) {
        var time = 0
        try {
            socket = Socket(IP, PORT)
            outputStream = ObjectOutputStream(socket!!.getOutputStream())
            inputStream = ObjectInputStream(socket!!.getInputStream())
            socket!!.soTimeout = 15000
            clientGui = ClientGui()
            clientGui!!.showNameDialog()
            while (clientGui!!.isNameEntered() == false);
            myName = clientGui!!.getNameEntered()
            //System.out.println(myName +"Entered");
            sendNameAndGetOpponent()
            while (SearchingThread.isStartPressed == false);
            questions
            time = time
            clientGui!!.questionList = questionList
            clientGui!!.tIME =  time
            clientGui!!.start()
        } catch (e: UnknownHostException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        //new ClientGui(questionList, time);
    }

    @get:Throws(IOException::class, ClassNotFoundException::class)
    private val time: Int
        private get() {
            var time = 0
            time = inputStream!!.readObject() as Int
            return time
        }

    @get:Throws(IOException::class, ClassNotFoundException::class)
    private val questions: Unit
        private get() {
            println("Starting to recv questions ")
            var question: Question
            SIZE = inputStream!!.readObject() as Int
            for (i in 0 until SIZE!!) {
                question = inputStream!!.readObject() as Question
                questionList.add(question)
                println(question.id)
            }
            println("Questions recevied")
        }

    @JvmStatic
    fun sendResult(): IntArray {
        try {
            val score = IntArray(3)
            val answerList: HashMap<Any?, Any?> = ClientGui.getQuestionAnswer()
            //System.out.println(answerList.size());
            outputStream!!.writeObject(answerList)
            score[0] = inputStream!!.readObject() as Int
            score[1] = inputStream!!.readObject() as Int
            score[2] = if (inputStream!!.readObject() as Boolean) 1 else 0
            println("Score =" + score[0] + " " + score[1] + " " + score[2])
            return score
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return intArrayOf(0)
    }

    fun sendNameAndGetOpponent() {
        try {
            outputStream!!.writeObject(myName)
            val searchingThread = clientGui?.let { SearchingThread(it) }
            searchingThread!!.start()
            opponentName = inputStream!!.readObject() as String
            println("Oppoenent = $opponentName")
            searchingThread!!.isOpponentFound = true
            searchingThread?.join()
            return
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return
    }
}