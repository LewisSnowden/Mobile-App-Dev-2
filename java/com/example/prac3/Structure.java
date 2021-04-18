package com.example.prac3;

/**
 * Represents a possible structure to be placed on the map. A structure simply contains a drawable
 * int reference, and a string label to be shown in the list_selection.
 *
 * strucure object container class, copied from prac 3
 */
public class Structure
{
    private final int drawableId;
    private String label;
    private String type;


    public Structure(int drawableId, String label,String type)
    {
        this.drawableId = drawableId;
        this.label = label;
        this.type = type;
    }

    public int getDrawableId()
    {
        return drawableId;
    }

    public String getLabel()
    {
        return label;
    }

    public String getType()
    {
        return type;
    }

    public boolean equals(Structure inStructure)
    {
        boolean returnVal = false;
        if(this.drawableId==inStructure.getDrawableId())
        {
            if(this.label.equals(inStructure.getLabel()))
            {
                returnVal = true;
            }
        }
        return returnVal;
    }
}
