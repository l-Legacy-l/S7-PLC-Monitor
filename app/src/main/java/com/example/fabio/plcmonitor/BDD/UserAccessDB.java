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
    private static final String COL_LASTNAME = "LASTNAME";
    private static final int NUM_COL_LASTNAME = 3;
    private static final String COL_FIRSTNAME = "FIRSTNAME";
    private static final int NUM_COL_FIRSTNAME = 4;
    private static final String COL_WRITE = "WRITE";
    private static final int NUM_COL_WRITE = 5;
    private static final String COL_ADMIN = "ADMIN";
    private static final int NUM_COL_ADMIN = 6;

    private SQLiteDatabase db;
    private UserBddSqlite userDb;

    public UserAccessDB (Context c){userDb = new UserBddSqlite(c, NAME_DB,null,VERSION);}

    public void openForWrite(){db = userDb.getWritableDatabase();}
    public void openForRead(){db = userDb.getReadableDatabase();}
    public void Close(){db.close();}

    public long insertUser(User u){

        ContentValues content = new ContentValues();

        content.put(COL_LASTNAME, u.getLastname());
        content.put(COL_FIRSTNAME, u.getFirstname());
        content.put(COL_PASSWORD, u.getMdp());
        content.put(COL_EMAIL, u.getEmail());
        content.put(COL_WRITE, u.getWriteAccess());
        content.put(COL_ADMIN, u.getIsAdmin());

        return db.insert(TABLE_USER, null, content);

    }

    public int updateUser(int i,User u){

        ContentValues content = new ContentValues();

        content.put(COL_EMAIL, u.getEmail());
        content.put(COL_PASSWORD, u.getMdp());
        content.put(COL_LASTNAME, u.getLastname());
        content.put(COL_FIRSTNAME, u.getFirstname());
        content.put(COL_WRITE, u.getWriteAccess());
        content.put(COL_ADMIN, u.getIsAdmin());

        return db.update(TABLE_USER, content, COL_ID + " = " + i, null);
    }

    public int removeUser(int id){

        return db.delete(TABLE_USER, COL_ID + " = " + id, null);
    }


    public ArrayList<User> getAllUser(){
        Cursor c = db.query(TABLE_USER, new String[]{
                COL_ID, COL_EMAIL, COL_PASSWORD, COL_LASTNAME, COL_FIRSTNAME, COL_WRITE, COL_ADMIN},
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
            user1.setLastname(c.getString(NUM_COL_LASTNAME));
            user1.setFirstname(c.getString(NUM_COL_FIRSTNAME));

            if(c.getInt(NUM_COL_WRITE) == 1)
            {
                user1.setWriteAccess(true);
            }
            else
            {
                user1.setWriteAccess(false);
            }

            if(c.getInt(NUM_COL_ADMIN) == 1)
            {
                user1.setIsAdmin(true);
            }
            else
            {
                user1.setIsAdmin(false);
            }

            tabUser.add(user1);
        }
        c.close();
        return tabUser;
    }

    public User getUser (String email)
    {
        Cursor c = db.query(TABLE_USER, new String[]{COL_ID, COL_EMAIL, COL_PASSWORD, COL_LASTNAME, COL_FIRSTNAME, COL_WRITE, COL_ADMIN},
                COL_EMAIL + " LIKE \"" + email + "\"", null, null, null, COL_EMAIL);

        //Si pas de résultat
        if(c.getCount() == 0)
        {
            c.close();
            return null;
        }
        //On a un résultat, on accède à la ligne
        c.moveToFirst();
        //On recrée l'user
        User user1 = new User();
        user1.setId(c.getInt(NUM_COL_ID));
        user1.setEmail(c.getString(NUM_COL_EMAIL));
        user1.setLastname(c.getString(NUM_COL_LASTNAME));
        user1.setFirstname(c.getString(NUM_COL_FIRSTNAME));
        user1.setMdp(c.getString(NUM_COL_PASSWORD));

        if(c.getInt(NUM_COL_WRITE) == 1)
        {
            user1.setWriteAccess(true);
        }
        else
        {
            user1.setWriteAccess(false);
        }

        if(c.getInt(NUM_COL_ADMIN) == 1)
        {
            user1.setIsAdmin(true);
        }
        else
        {
            user1.setIsAdmin(false);
        }
        c.close();

        return user1;
    }
}
