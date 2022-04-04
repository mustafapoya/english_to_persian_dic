package net.golbarg.engtoper.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.golbarg.engtoper.models.Question;

import java.util.ArrayList;

public class TableQuestion implements CRUDHandler<Question> {
    public static final String TABLE_NAME = "questions";
    public static final String KEY_ID = "id";
    public static final String KEY_CATEGORY_ID = "category_id";
    public static final String KEY_NUMBER = "number";
    public static final String KEY_TITLE = "title";
    public static final String KEY_NUMBER_OF_CORRECT_ANSWER = "number_of_correct_answer";
    public static final String [] ALL_COLUMNS = { KEY_ID, KEY_CATEGORY_ID, KEY_NUMBER, KEY_TITLE, KEY_NUMBER_OF_CORRECT_ANSWER };
    private final DatabaseHandler dbHandler;

    public TableQuestion(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    public static String createTableQuery() {
        return String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s INTEGER, %s INTEGER, %s TEXT, %s INTEGER)",
                    TABLE_NAME, KEY_ID, KEY_CATEGORY_ID, KEY_NUMBER, KEY_TITLE, KEY_NUMBER_OF_CORRECT_ANSWER);
    }

    public static String dropTableQuery() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }


    @Override
    public void create(Question object) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        db.insert(TABLE_NAME, null, putValues(object));
        db.close();
    }

    public void create(ArrayList<Question> questions) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        for (int i = 0; i < questions.size(); i++) {
            db.insert(TABLE_NAME, null, putValues(questions.get(i)));
        }
        db.close();
    }

    @Override
    public Question get(int id) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, ALL_COLUMNS, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if(cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            return mapColumn(cursor);
        } else {
            return null;
        }
    }

    public Question getWithCorrectAnswer(int id) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, ALL_COLUMNS, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if(cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            Question question = mapColumn(cursor);
            TableQuestionAnswer tableQuestionAnswer = new TableQuestionAnswer(dbHandler);
            question.setAnswers(tableQuestionAnswer.getCorrectAnswersOf(question.getId()));
            return question;
        } else {
            return null;
        }
    }

    public ArrayList<Question> getQuestionsOf(int categoryId) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        ArrayList<Question> result = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + KEY_CATEGORY_ID + " = " + categoryId;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                result.add(mapColumn(cursor));
            }while(cursor.moveToNext());
        }
        return result;
    }

    @Override
    public ArrayList<Question> getAll() {
        ArrayList<Question> result = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                result.add(mapColumn(cursor));
            }while(cursor.moveToNext());
        }
        return result;
    }

    @Override
    public int update(Question object) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        return db.update(TABLE_NAME, putValues(object), KEY_ID + "=?", new String[]{String.valueOf(object.getId())});
    }

    @Override
    public void delete(Question object) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + "= ?", new String[]{String.valueOf(object.getId())});
    }

    public void deleteByCategoryId(int categoryId) {
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + KEY_CATEGORY_ID + " = " + categoryId;
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        db.execSQL(query);
    }

    @Override
    public int getCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int getCountOf(int categoryId) {
        String countQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_CATEGORY_ID + " = " + categoryId;
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    @Override
    public Question mapColumn(Cursor cursor) {
        return new Question(
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_ID))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_NUMBER))),
                cursor.getString(cursor.getColumnIndex(KEY_TITLE)),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_NUMBER_OF_CORRECT_ANSWER)))
        );
    }

    @Override
    public ContentValues putValues(Question object) {
        ContentValues values = new ContentValues();
        if (object.getId() != -1 && object.getId() != 0) {
            values.put(KEY_ID, object.getId());
        }
        values.put(KEY_CATEGORY_ID, object.getCategoryId());
        values.put(KEY_NUMBER, object.getNumber());
        values.put(KEY_TITLE, object.getTitle());
        values.put(KEY_NUMBER_OF_CORRECT_ANSWER, object.getNumberOfCorrectAnswer());

        return values;
    }

    @Override
    public void emptyTable() {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);

    }
}
