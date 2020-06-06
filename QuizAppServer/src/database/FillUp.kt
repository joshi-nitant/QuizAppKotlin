package database

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import common.Question
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URI
import java.net.URISyntaxException
import java.sql.SQLException

class FillUp {
    fun fillData() {
        try {
            val uri = URI("https://opentdb.com/api.php?amount=20&category=18&difficulty=medium&type=multiple")
            val input = BufferedReader(InputStreamReader(uri.toURL().openStream()))
            var jsonString: String? = ""
            var line: String? = ""
            while (input.readLine().also { line = it } != null) {
                jsonString += line
            }
            val jsonParser = JsonParser()
            val jo = jsonParser.parse(jsonString) as JsonObject
            val jsonArr = jo.getAsJsonArray("results")
            val googleJson = Gson()
            val jsonObjList = googleJson.fromJson(jsonArr, Array<Question>::class.java)
            //System.out.println(jsonObjList[0].getIncorrect_answers().length);
            QuizDb().addQuestion(jsonObjList)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (throwables: SQLException) {
            throwables.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }
}