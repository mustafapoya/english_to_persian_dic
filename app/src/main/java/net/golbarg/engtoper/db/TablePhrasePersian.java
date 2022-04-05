package net.golbarg.engtoper.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.golbarg.engtoper.models.PhraseEnglish;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class TablePhrasePersian {
    public static final String TABLE_NAME = "phrase2";
    public static final String KEY_ID = "_id";
    public static final String KEY_LANGUAGE_FROM = "l_from";
    public static final String KEY_LANGUAGE_TO = "l_to";
    public static final String KEY_FAVORITE = "favorite";
    public static final String KEY_ARTICLE = "article";
    public static final String KEY_TYPE = "type";
    public static final String [] ALL_COLUMNS = {KEY_ID, KEY_LANGUAGE_FROM, KEY_LANGUAGE_TO, KEY_FAVORITE, KEY_ARTICLE, KEY_TYPE};
    private final OfflineDatabaseHandler offlineDatabaseHandler;

    public TablePhrasePersian(Context context) {
        offlineDatabaseHandler = new OfflineDatabaseHandler(context);
        offlineDatabaseHandler.openDatabase();
    }

    public PhraseEnglish get(int id) {
        SQLiteDatabase db = offlineDatabaseHandler.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, ALL_COLUMNS, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            return mapColumn(cursor);
        } else {
            return null;
        }
    }

    public ArrayList<PhraseEnglish> getAll() {
        ArrayList<PhraseEnglish> result = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = offlineDatabaseHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                result.add(mapColumn(cursor));
            }while(cursor.moveToNext());
        }
        return result;
    }


    public PhraseEnglish mapColumn(Cursor cursor) {
        String language_to = "";
        try {
            language_to = new String(cursor.getBlob(cursor.getColumnIndex(KEY_LANGUAGE_TO)), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return new PhraseEnglish(
                cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_LANGUAGE_FROM)),
                language_to,
                cursor.getInt(cursor.getColumnIndex(KEY_FAVORITE)),
                cursor.getString(cursor.getColumnIndex(KEY_ARTICLE)),
                cursor.getInt(cursor.getColumnIndex(KEY_TYPE))
        );
    }
}
