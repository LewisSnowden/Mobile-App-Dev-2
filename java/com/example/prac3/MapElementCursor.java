package com.example.prac3;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.example.prac3.SkylineSchema.*;
//cursor for the map grid
public class MapElementCursor extends CursorWrapper {
    public MapElementCursor(Cursor cursor){super(cursor);}

    public MapElement getElement()
    {
        String ID = getString(getColumnIndex(GridTable.Cols.ID));
        int buildable = getInt(getColumnIndex(GridTable.Cols.BUILDABLE));
        int terrainNorthWest=getInt(getColumnIndex(GridTable.Cols.TERRAINNW));
        int terrainSouthWest=getInt(getColumnIndex(GridTable.Cols.TERRAINSW));
        int terrainNorthEast=getInt(getColumnIndex(GridTable.Cols.TERRAINNE));
        int terrainSouthEast=getInt(getColumnIndex(GridTable.Cols.TERRAINSE));
        int drawableID = getInt(getColumnIndex(GridTable.Cols.DRAWABLE));
        String label  = getString(getColumnIndex(GridTable.Cols.LABEL));
        String type = getString(getColumnIndex(GridTable.Cols.TYPE));
        String name = getString(getColumnIndex(GridTable.Cols.NAME));
        String[] split = ID.split("_");
        int row = Integer.parseInt(split[0]);
        int col = Integer.parseInt(split[1]);
        boolean buildableBool;
        if(buildable==0) //cant store boolean in database so in database 0 is false, else is true
        {
            buildableBool = false;
        }
        else
        {
            buildableBool = true;
        }
        MapElement temp = new MapElement(row,col,buildableBool,terrainNorthWest,terrainSouthWest,terrainNorthEast,terrainSouthEast,drawableID,label,type,name);
        if(drawableID==0&&label.equals("BLANK")&&type.equals("BLANK")) //cant store a null strucutre object in database so if both are marked as BLANK in db means structure is null
        {
            temp.setStructure(null);
        }
        return  temp;
    }

}
