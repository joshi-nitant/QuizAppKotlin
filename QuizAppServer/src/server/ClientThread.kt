package server

import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket
import java.sql.SQLException
import java.util.*

class ClientThread(var socket: Socket) : Runnable {
    private var opponentAssigned: Boolean = false
    val SIZE = 10
    var TIME = 20

    //Thread.sleep(10);
    @get:Throws(InterruptedException::class)
    var myName: String? = null
        private set

    //private String opponentName;
    var inputStream: ObjectInputStream? = null
    var outputStream: ObjectOutputStream? = null
    var rightAnswer: HashMap<Int, String>? = null
    var answerClient: HashMap<Int?, String?>? = null
    private var isNameEntered = false

    @set:Throws(InterruptedException::class)
    var isOpponentAssigned = false
        set(opponentAssigned) {
            field = opponentAssigned
            Thread.sleep(10)
        }
    private var opponetThread: ClientThread? = null
    private var answerFound = false
    var score: Int? = null
    var isOppenentSend = false
        private set

    @Throws(InterruptedException::class)
    fun setRandQuestions(randQuestions: RandQuestions?) {
        Thread.sleep(10)
        this.randQuestions = randQuestions
    }

    private var randQuestions: RandQuestions? = null
    fun getOpponetThread(): ClientThread? {
        return opponetThread
    }

    @Throws(InterruptedException::class)
    fun setOpponetThread(opponetThread: ClientThread?) {
        this.opponetThread = opponetThread
        Thread.sleep(10)
    }

    fun isNameEntered(): Boolean {
        try {
            Thread.sleep(30)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return isNameEntered
    }

    override fun run() {
        try {
            //Thread.sleep(50);
            inputStream = ObjectInputStream(socket.getInputStream())
            outputStream = ObjectOutputStream(socket.getOutputStream())
            while (myName == null) {
                myName = name
            }
            isNameEntered = true
            println("Name Entered......=$myName")
            //            System.out.println("Name = "+myName);
//            opponentName = lookForOpponent();
            println("Searching opponent......for$myName")
            while (isOpponentAssigned == false) {
                oppenentSearch()
                Thread.sleep(10)
            }
            while (!isOppenentSend) {
                sendOpponent()
            }
            sendQuestion()
            Thread.sleep(5)
            //sendQuestion();
            sendTime()
            Thread.sleep(5)
            answer
            Thread.sleep(5)
            sendScore()
            Thread.sleep(5)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (throwables: SQLException) {
            throwables.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } finally {
//            try {
//                //socket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    @Throws(IOException::class, InterruptedException::class)
    private fun sendScore() {
        println("Sending score......to $myName")
        score = randQuestions!!.compare(answerClient)
        outputStream!!.writeObject(score)
        while (opponetThread!!.answerFound == false || opponetThread!!.score == null) {
            Thread.sleep(5)
        }
        outputStream!!.writeObject(opponetThread!!.score)
        outputStream!!.writeObject(score!! > opponetThread!!.score!!)
        println("Score = " + opponetThread!!.score + " " + score)
        println("Score Sent......to $myName")
    }

    @Throws(SQLException::class, ClassNotFoundException::class, IOException::class, InterruptedException::class)
    private fun sendQuestion() {
        println("Questions sending to $myName")
        outputStream!!.writeObject(randQuestions!!.number)
        for (i in randQuestions!!.sendQuestion) {
            outputStream!!.writeObject(randQuestions!!.questionArrayList[i])
        }
        println("Questions sent to$myName")
    }

    @Throws(IOException::class, ClassNotFoundException::class, InterruptedException::class)
    private fun sendOpponent() {
        println("Sending opponent......to $myName")
        if (myName != null && isOpponentAssigned) {
            outputStream!!.writeObject(opponetThread!!.myName)
            println("Opponent sent =" + opponetThread!!.myName)
            isOppenentSend = true
            Thread.sleep(20)
        } else {
            println("No opponent......of $myName")
            return
        }
    }

    @Throws(IOException::class, InterruptedException::class)
    private fun sendTime() {
        println("Sending time......to $myName")
        outputStream!!.writeObject(TIME)
        println("time sent......$TIME to $myName")
    }

    @get:Throws(IOException::class, ClassNotFoundException::class)
    private val name: String
        private get() {
            println("Getting name......")
            return inputStream!!.readObject() as String
        }

    private fun lookForOpponent(): String {
        return "Static"
    }

    @get:Throws(IOException::class, ClassNotFoundException::class, InterruptedException::class)
    private val answer: Unit
        private get() {
            println("Getting answer......of $myName")
            answerClient = inputStream!!.readObject() as HashMap<Int?, String?>
            answerFound = true
            println("Answer Found......of $myName")
        }

    @Throws(InterruptedException::class)
    fun oppenentSearch() {
        for (i in ServerMain.clientThreadPools!!.indices) {
            val opponent = ServerMain.clientThreadPools!![i]
            if (opponent == this == false && opponent!!.isOpponentAssigned == false) {
                val randQuestions = RandQuestions()
                opponent.opponentAssigned= true
                opponent.setOpponetThread(this)
                opponent.setRandQuestions(randQuestions)
                //Thread.sleep(30);
                this.opponentAssigned = true
                setOpponetThread(opponent)
                setRandQuestions(randQuestions)
                break
            }
        }
        ServerMain.clientThreadPools!!.add(this)
    }

    init {
        Thread(this).start()
    }
}