package com.example.contactsapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class Personsdao {

    public ArrayList<Persons> allPersons(Database db){

        ArrayList<Persons> personsArrayList = new ArrayList<>();
        SQLiteDatabase sdb = db.getWritableDatabase();

        Cursor c = sdb.rawQuery("SELECT * FROM persons",null);

        while (c.moveToNext()){

            @SuppressLint("Range")
            Persons person = new Persons(c.getInt(c.getColumnIndex("person_id"))
                    ,c.getString(c.getColumnIndex("person_name"))
                    ,c.getString(c.getColumnIndex("person_phone")));

            personsArrayList.add(person);
        }
        sdb.close();
        return personsArrayList;
    }

    public ArrayList<Persons> personSearch(Database db, String searchedWord){

        ArrayList<Persons> personsArrayList = new ArrayList<>();
        SQLiteDatabase sdb = db.getWritableDatabase();

        Cursor c = sdb.rawQuery("SELECT * FROM persons WHERE person_name like '%"+searchedWord+"%'",null);

        while (c.moveToNext()){

            @SuppressLint("Range")
            Persons person = new Persons(c.getInt(c.getColumnIndex("person_id"))
                    ,c.getString(c.getColumnIndex("person_name"))
                    ,c.getString(c.getColumnIndex("person_phone")));

            personsArrayList.add(person);
        }
        sdb.close();
        return personsArrayList;
    }

    public void personDelete(Database db,int person_id){
        SQLiteDatabase sdb = db.getWritableDatabase();
        sdb.delete("persons","person_id = ?",new String[]{String.valueOf(person_id)});
    }

    public void personAdd(Database db, String person_name, String person_phone){
        SQLiteDatabase sdb = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("person_name",person_name);
        values.put("person_phone",person_phone);
        sdb.insertOrThrow("persons",null,values);
        sdb.close();
    }

    public void personUpdate(Database db, int person_id, String person_name, String person_phone){
        SQLiteDatabase sdb = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("person_name",person_name);
        values.put("person_phone",person_phone);
        sdb.update("persons",values,"person_id = ?", new String[]{String.valueOf(person_id)});
        sdb.close();
    }
}
