package database;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import common.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

public class FillUp {

   public void fillData(){
        try {
            URI uri = new URI("https://opentdb.com/api.php?amount=20&category=18&difficulty=medium&type=multiple");
            BufferedReader input = new BufferedReader(new InputStreamReader(uri.toURL().openStream()));

            String jsonString = "";
            String line = "";
            while((line = input.readLine()) != null){
                jsonString += line;
            }
            JsonParser jsonParser = new JsonParser();
            JsonObject jo = (JsonObject)jsonParser.parse(jsonString);
            JsonArray jsonArr = jo.getAsJsonArray("results");
            Gson googleJson = new Gson();
            Question[] jsonObjList = googleJson.fromJson(jsonArr, Question[].class);
            //System.out.println(jsonObjList[0].getIncorrect_answers().length);
            new QuizDb().addQuestion(jsonObjList);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
