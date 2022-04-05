package net.golbarg.engtoper.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class OfflineDatabaseHandler extends SQLiteOpenHelper {
    public static  final String TAG = OfflineDatabaseHandler.class.getName();
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "eng_per.db";
    private static final String DB_PATH_SUFFIX = "/databases/";
    Context context;

    public OfflineDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void copyDatabaseFromAsset() throws IOException {
        InputStream inputStream = context.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = getDatabasePath();

        // if the path does not exist first, create it
        File f = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
        if(!f.exists()) {
            f.mkdir();
        }

        // Open the empty db as the output stream
        OutputStream outputStream = new FileOutputStream(outFileName);

        //transfer bytes from the input file to the output file
        byte[] buffer = new byte[1024];
        int length;

        while((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        // close the streams
        outputStream.flush();
        outputStream.close();
        inputStream.close();

    }

    public String getDatabasePath() {
        return context.getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }

    public SQLiteDatabase openDatabase() throws SQLException {
        File dbFile = context.getDatabasePath(DATABASE_NAME);

        if(!dbFile.exists()) {
            try {
                copyDatabaseFromAsset();
                Log.d(TAG, "openDatabase: copying success from assets folder");
            } catch (IOException e) {
                throw new RuntimeException("Error Creating source database", e);
            }
        }

        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TableConfig.createTableQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TableConfig.dropTableQuery());
        onCreate(db);
    }
}
