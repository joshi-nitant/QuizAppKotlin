package server

import java.io.IOException
import java.net.ServerSocket
import java.util.*

object ServerMain {
    private const val IP = "127.0.0.1"
    private const val PORT = 5000
    var clientThreadPools: ArrayList<ClientThread?>? = null

    @JvmStatic
    fun main(args: Array<String>) {
        //new FillUp().fillData();
        clientThreadPools = ArrayList()
        try {
            ServerSocket(PORT).use { serverSocket ->
                while (true) {
                    val clientThread = ClientThread(serverSocket.accept())
                    //oppenentSearch(clientThread);
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(InterruptedException::class)
    fun oppenentSearch(clientThread: ClientThread) {
        for (i in clientThreadPools!!.indices) {
            val opponent = clientThreadPools!![i]
            if (opponent!!.isOpponentAssigned == false) {
                val randQuestions = RandQuestions()
                opponent.isOpponentAssigned = true
                opponent.setOpponetThread(clientThread)
                opponent.setRandQuestions(randQuestions)
                Thread.sleep(30)
                clientThread.isOpponentAssigned = true
                clientThread.setOpponetThread(opponent)
                clientThread.setRandQuestions(randQuestions)
                break
            }
        }
        clientThreadPools!!.add(clientThread)
    }
}