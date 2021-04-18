package com.example.prac3;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.example.prac3.SkylineSchema.*;
/*
cursor for settings table in database
 */
public class SettingsCursor extends CursorWrapper {
    public SettingsCursor(Cursor cursor){super(cursor);}

    public Settings getSettings()
    {
        int width = getInt(getColumnIndex(SettingsTable.Cols.WIDTH));
        int height = getInt(getColumnIndex(SettingsTable.Cols.HEIGHT));
        int money = getInt(getColumnIndex(SettingsTable.Cols.IMONEY));
        String city = getString(getColumnIndex(SettingsTable.Cols.CITYNAME));
        return new Settings(width,height,money,city);
    }

}
