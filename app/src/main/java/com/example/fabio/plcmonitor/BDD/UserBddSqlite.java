package com.example.fabio.plcmonitor.BDD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserBddSqlite extends SQLiteOpenHelper {

    private static final String TABLE_USER = "table_user";
    private static final String COL_ID = "ID";
    private static final String COL_EMAIL = "EMAIL";
    private static final String COL_PASSWORD = "PASSWORD";
    private static final String COL_LASTNAME = "LASTNAME";
    private static final String COL_FIRSTNAME = "FIRSTNAME";
    private static final String COL_WRITE = "WRITE";
    private static final String COL_ADMIN = "ADMIN";

    private static final String CREATE_BDD =
            "CREATE TABLE "+TABLE_USER+
                    " ("+COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COL_EMAIL+" TEXT NOT NULL, "
                    +COL_PASSWORD+" TEXT NOT NULL, "+COL_LASTNAME+" TEXT NOT NULL, "+COL_FIRSTNAME+" TEXT NOT NULL, "+COL_WRITE+" TEXT NOT NULL, "+COL_ADMIN+" TEXT NOT NULL);";

    public UserBddSqlite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BDD);
        //utilisateur admin
        db.execSQL("INSERT INTO table_user (EMAIL, LASTNAME, FIRSTNAME, PASSWORD, WRITE, ADMIN) VALUES ('admin@gmail.com', 'root', 'user', 'Test123*', 1, 1)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_USER);
        onCreate(db);
    }
}
