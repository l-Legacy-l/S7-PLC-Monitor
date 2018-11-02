package com.example.fabio.plcmonitor.BDD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fabio.plcmonitor.BDD.User;
import com.example.fabio.plcmonitor.BDD.UserBddSqlite;

import java.util.ArrayList;

public class UserAccessDB {

    private static final int VERSION = 1;
    private static final String NAME_DB = "User.db";
    private static final String TABLE_USER = "table_user";
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_EMAIL = "EMAIL";
    private static final int NUM_COL_EMAIL = 1;
    private static final String COL_PASSWORD = "PASSWORD";
    private static final int NUM_COL_PASSWORD = 2;
    private static final String COL_NOM = "NOM";
    private static final int NUM_COL_NOM = 3;
    private static final String COL_PRENOM = "PRENOM";
    private static final int NUM_COL_PRENOM = 4;
    private static final String COL_ADMIN = "ADMIN";
    private static final int NUM_COL_ADMIN = 5;

    private SQLiteDatabase db;
    private UserBddSqlite userDb;

    public UserAccessDB (Context c){userDb = new UserBddSqlite(c, NAME_DB,null,VERSION);}

    public void openForWrite(){db = userDb.getWritableDatabase();}
    public void openForRead(){db = userDb.getReadableDatabase();}
    public void Close(){db.close();}

    public long InsertUser(User u){

        ContentValues content = new ContentValues();

        content.put(COL_NOM, u.getNom());
        content.put(COL_PRENOM, u.getPrenom());
        content.put(COL_PASSWORD, u.getMdp());
        content.put(COL_EMAIL, u.getEmail());
        content.put(COL_ADMIN, u.getIsAdmin());

        return db.insert(TABLE_USER, null, content);

    }

    public int updateUser(int i,User u){

        ContentValues content = new ContentValues();

        content.put(COL_EMAIL, u.getEmail());
        content.put(COL_PASSWORD, u.getMdp());
        content.put(COL_NOM, u.getNom());
        content.put(COL_PRENOM, u.getPrenom());
        content.put(COL_ADMIN, u.getIsAdmin());

        return db.update(TABLE_USER, content, COL_ID + " = " + i, null);
    }

    public int removeUser(int id){

        return db.delete(TABLE_USER, COL_ID + " = " + id, null);
    }


    public ArrayList<User> getAllUser(){
        Cursor c = db.query(TABLE_USER, new String[]{
                COL_ID, COL_EMAIL, COL_PASSWORD, COL_NOM, COL_PRENOM, COL_ADMIN},
                null, null, null, null, null,null);

        ArrayList<User> tabUser = new ArrayList<User>();

        if (c.getCount() == 0) {
            c.close();
            return null;
        }
        while (c.moveToNext()) {
            User user1 = new User();
            user1.setId(c.getInt(NUM_COL_ID));
            user1.setEmail(c.getString(NUM_COL_EMAIL));
            user1.setMdp(c.getString(NUM_COL_PASSWORD));
            user1.setNom(c.getString(NUM_COL_NOM));
            user1.setPrenom(c.getString(NUM_COL_PRENOM));

            Boolean isAdmin = Boolean.parseBoolean(c.getString(NUM_COL_ADMIN));
            user1.setIsAdmin(isAdmin);

            tabUser.add(user1);
        }
        c.close();
        return tabUser;
    }

    //A utiliser pour la recherche de doublons
    public String search(String value,String returnColumn)
    {
        openForRead();
        String query = "Select EMAIL, "+returnColumn+" from "+TABLE_USER;

        Cursor cursor = db.rawQuery(query, null);
        String a,b;
        b = "not found";

        if(cursor.moveToFirst())
        {
            do{
                a = cursor.getString(0);
                if(a.equals(value))
                {
                    b = cursor.getString(1);
                    break;
                }
            }while(cursor.moveToNext());
        }
        return b;
    }
}
