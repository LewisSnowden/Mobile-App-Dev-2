package com.example.prac3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import com.example.prac3.SkylineSchema.*;
/*
helper class for the database containing 3 different tables
 */
public class SkylineDbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "skyline.db";

    public SkylineDbHelper(Context context){super(context, DATABASE_NAME,null,VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE "+ GameTable.NAME+"("+GameTable.Cols.ID+" INTEGER, "+GameTable.Cols.MONEY + " INTEGER, " + GameTable.Cols.GAMETIME + " INTEGER, "+GameTable.Cols.RECENTINCOME + " INTEGER, "+GameTable.Cols.POPULATION + " INTEGER, "+GameTable.Cols.EMPLOYRATE + " REAL, "+GameTable.Cols.RESIDENTIAL + " INTEGER, " + GameTable.Cols.COMMERCIAL + " INTEGER)");
        db.execSQL("CREATE TABLE "+ SettingsTable.NAME+"("+SettingsTable.Cols.ID+" INTEGER, "+SettingsTable.Cols.WIDTH + " INTEGER, " + SettingsTable.Cols.HEIGHT + " INTEGER, "+ SettingsTable.Cols.IMONEY + " INTEGER, "+SettingsTable.Cols.CITYNAME + " TEXT)");
        db.execSQL("CREATE TABLE "+ GridTable.NAME+"("+GridTable.Cols.ID+" TEXT, "+GridTable.Cols.BUILDABLE + " INTEGER, " + GridTable.Cols.TERRAINNW + " INTEGER, "+GridTable.Cols.TERRAINSW + " INTEGER, "+GridTable.Cols.TERRAINNE + " INTEGER, "+GridTable.Cols.TERRAINSE + " INTERGER, "+GridTable.Cols.DRAWABLE+ " INTEGER, "+GridTable.Cols.LABEL+ " TEXT, "+GridTable.Cols.TYPE+ " TEXT, " + GridTable.Cols.NAME + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int v1, int v2)
    {

    }
}
