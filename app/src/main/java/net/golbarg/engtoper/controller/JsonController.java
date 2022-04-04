package net.golbarg.engtoper.controller;

import android.content.Context;

import net.golbarg.engtoper.db.DatabaseHandler;
import net.golbarg.engtoper.db.TableCategory;
import net.golbarg.engtoper.db.TableQuestion;
import net.golbarg.engtoper.db.TableQuestionAnswer;
import net.golbarg.engtoper.models.Category;
import net.golbarg.engtoper.models.Question;
import net.golbarg.engtoper.models.QuestionAnswer;
import net.golbarg.engtoper.util.JsonUtil;

import org.json.JSONArray;

import java.util.ArrayList;

public class JsonController {

    public static boolean insertCategoriesToDB(Context context) {
        DatabaseHandler handler = new DatabaseHandler(context);
        TableCategory tableCategory = new TableCategory(handler);
        JSONArray jsonArrayCategory = JsonUtil.getJsonCategories(context);
        ArrayList<Category> categoryArrayList = JsonUtil.mapCategoriesFromJson(jsonArrayCategory);
        tableCategory.emptyTable();

        for (int i = 0; i < categoryArrayList.size(); i++) {
            tableCategory.create(categoryArrayList.get(i));
        }

        return true;
    }

    public static boolean insertQuestionsToDB(Context context) {
        DatabaseHandler handler = new DatabaseHandler(context);
        TableQuestion tableQuestion = new TableQuestion(handler);
        JSONArray jsonArrayQuestion = JsonUtil.getJSONQuestion(context);
        ArrayList<Question> questionArrayList = JsonUtil.mapQuestionsFromJson(jsonArrayQuestion);
        tableQuestion.emptyTable();

        tableQuestion.create(questionArrayList);

        return true;
    }

    public static boolean insertAnswersToDB(Context context) {
        DatabaseHandler handler = new DatabaseHandler(context);
        TableQuestionAnswer tableQuestionAnswer = new TableQuestionAnswer(handler);
        JSONArray jsonArrayAnswer = JsonUtil.getJSONAnswers(context);
        ArrayList<QuestionAnswer> questionAnswerArrayList = JsonUtil.mapAnswersFromJson(jsonArrayAnswer);
        tableQuestionAnswer.emptyTable();

        tableQuestionAnswer.create(questionAnswerArrayList);

//        for (int i = 0; i < questionAnswerArrayList.size(); i++) {
//            tableQuestionAnswer.create(questionAnswerArrayList.get(i));
//        }

        return true;
    }
}
