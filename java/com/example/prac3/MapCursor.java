package com.example.prac3;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.example.prac3.SkylineSchema.*;
/*
cursor for reading the singeltons fields like money etc
 */
public class MapCursor extends CursorWrapper {
    public MapCursor(Cursor cursor){super(cursor);}

    public void getMap()
    {
        MapData myMap = MapData.get();
        myMap.setMoney(getInt(getColumnIndex(GameTable.Cols.MONEY)));
        myMap.setGameTime(getInt(getColumnIndex(GameTable.Cols.GAMETIME)));
        myMap.setRecentIncome(getInt(getColumnIndex(GameTable.Cols.RECENTINCOME)));
        myMap.setPopulation(getInt(getColumnIndex(GameTable.Cols.POPULATION)));
        myMap.setEmploymentRate(getDouble(getColumnIndex(GameTable.Cols.EMPLOYRATE)));
        myMap.setnResidential(getInt(getColumnIndex(GameTable.Cols.RESIDENTIAL)));
        myMap.setnCommercial(getInt(getColumnIndex(GameTable.Cols.COMMERCIAL)));
    }

}
