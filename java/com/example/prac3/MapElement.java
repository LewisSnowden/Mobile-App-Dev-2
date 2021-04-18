package com.example.prac3;

import android.graphics.Bitmap;

/**
 * Represents a single grid square in the map. Each map element has both terrain and an optional
 * structure.
 *
 * The terrain comes in four pieces, as if each grid square was further divided into its own tiny
 * 2x2 grid (north-west, north-east, south-west and south-east). Each piece of the terrain is
 * represented as an int, which is actually a drawable reference. That is, if you have both an
 * ImageView and a MapElement, you can do this:
 *
 * ImageView iv = ...;
 * MapElement me = ...;
 * iv.setImageResource(me.getNorthWest());
 *
 * This will cause the ImageView to display the grid square's north-western terrain image,
 * whatever it is.
 *
 * (The terrain is broken up like this because there are a lot of possible combinations of terrain
 * images for each grid square. If we had a single terrain image for each square, we'd need to
 * manually combine all the possible combinations of images, and we'd get a small explosion of
 * image files.)
 *
 * Meanwhile, the structure is something we want to display over the top of the terrain. Each
 * MapElement has either zero or one Structure} objects. For each grid square, we can also change
 * which structure is built on it.
 *
 * code is re used from prac 3 and extended
 */
public class MapElement
{
    private final boolean buildable;
    private final int terrainNorthWest;
    private final int terrainSouthWest;
    private final int terrainNorthEast;
    private final int terrainSouthEast;
    private int col;
    private int row;
    private Structure structure;
    private String stuctName;
    private Bitmap image;

    public MapElement(boolean buildable, int northWest, int northEast,
                      int southWest, int southEast, Structure structure,int row,int col)
    {
        this.buildable = buildable;
        this.terrainNorthWest = northWest;
        this.terrainNorthEast = northEast;
        this.terrainSouthWest = southWest;
        this.terrainSouthEast = southEast;
        this.structure = structure;
        this.stuctName = "";
        image = null;
        this.row = row;
        this.col = col;
    }

    public MapElement(int row,int col,boolean buildable, int northWest,int southWest, int northEast,  int southEast, int inDrawableID,String label,String type, String structName)
    {
        this.row = row;
        this.col = col;
        this.buildable = buildable;
        this.terrainNorthWest = northWest;
        this.terrainNorthEast = northEast;
        this.terrainSouthWest = southWest;
        this.terrainSouthEast = southEast;
        this.structure = new Structure(inDrawableID,label,type);
        this.stuctName = structName;
        image = null;
    }

    public boolean isBuildable()
    {
        return buildable;
    }

    public int isBuildableNumber()
    {
        if(buildable)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    public int getNorthWest()
    {
        return terrainNorthWest;
    }

    public int getSouthWest()
    {
        return terrainSouthWest;
    }

    public int getNorthEast()
    {
        return terrainNorthEast;
    }

    public int getSouthEast()
    {
        return terrainSouthEast;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getStuctName() {
        return stuctName;
    }

    public void setStuctName(String stuctName) {
        this.stuctName = stuctName;
    }

    /**
     * Retrieves the structure built on this map element.
     * @return The structure, or null if one is not present.
     */
    public Structure getStructure()
    {
        return structure;
    }

    public void setStructure(Structure structure)
    {
        this.structure = structure;
        if(structure!=null)
        {
            this.stuctName = structure.getLabel();
        }
        else
        {
            this.stuctName="";
        }
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
