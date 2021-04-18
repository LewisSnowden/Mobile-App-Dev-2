package com.example.prac3;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Random;
import java.math.*;
import com.example.prac3.SkylineSchema.*;

/**
 singleton for storage copied from prac 3
 */
public class MapData
{


    private static final int WATER = R.drawable.ic_water;
    private static final int[] GRASS = {R.drawable.ic_grass1, R.drawable.ic_grass2,
            R.drawable.ic_grass3, R.drawable.ic_grass4};

    private static final Random rng = new Random();


    private MapElement[][] grid; //store in its own table
    private Settings settings;  //store in its own table
    private int money; //store this in MapData table
    private int gameTime; //store this in MapData table

    private String curFunction;
    private Structure currStructure;
    private MapElement currMapElement;

    private static MapData instance = null;

    private int recentIncome;  //store this in MapData table
    private int population;  //store this in MapData table
    private double employmentRate; //store this in MapData table
    private int nResidential; //store this in MapData table
    private int  nCommercial; //store this in MapData table
    private boolean hasLoaded;

    private SQLiteDatabase db;


    public static MapData get()
    {
        if(instance == null)
        {
            instance = new MapData();

        }
        return instance;
    }


    private static MapElement[][] generateGrid(int HEIGHT,int WIDTH)
    {
        final int HEIGHT_RANGE = 256;
        final int WATER_LEVEL = 112;
        final int INLAND_BIAS = 24;
        final int AREA_SIZE = 1;
        final int SMOOTHING_ITERATIONS = 2;

        int[][] heightField = new int[HEIGHT][WIDTH];
        for(int i = 0; i < HEIGHT; i++)
        {
            for(int j = 0; j < WIDTH; j++)
            {
                heightField[i][j] =
                    rng.nextInt(HEIGHT_RANGE)
                    + INLAND_BIAS * (
                        Math.min(Math.min(i, j), Math.min(HEIGHT - i - 1, WIDTH - j - 1)) -
                        Math.min(HEIGHT, WIDTH) / 4);
            }
        }

        int[][] newHf = new int[HEIGHT][WIDTH];
        for(int s = 0; s < SMOOTHING_ITERATIONS; s++)
        {
            for(int i = 0; i < HEIGHT; i++)
            {
                for(int j = 0; j < WIDTH; j++)
                {
                    int areaSize = 0;
                    int heightSum = 0;

                    for(int areaI = Math.max(0, i - AREA_SIZE);
                            areaI < Math.min(HEIGHT, i + AREA_SIZE + 1);
                            areaI++)
                    {
                        for(int areaJ = Math.max(0, j - AREA_SIZE);
                                areaJ < Math.min(WIDTH, j + AREA_SIZE + 1);
                                areaJ++)
                        {
                            areaSize++;
                            heightSum += heightField[areaI][areaJ];
                        }
                    }

                    newHf[i][j] = heightSum / areaSize;
                }
            }

            int[][] tmpHf = heightField;
            heightField = newHf;
            newHf = tmpHf;
        }

        MapElement[][] grid = new MapElement[HEIGHT][WIDTH];
        for(int i = 0; i < HEIGHT; i++)
        {
            for(int j = 0; j < WIDTH; j++)
            {
                MapElement element;

                if(heightField[i][j] >= WATER_LEVEL)
                {
                    boolean waterN = (i == 0)          || (heightField[i - 1][j] < WATER_LEVEL);
                    boolean waterE = (j == WIDTH - 1)  || (heightField[i][j + 1] < WATER_LEVEL);
                    boolean waterS = (i == HEIGHT - 1) || (heightField[i + 1][j] < WATER_LEVEL);
                    boolean waterW = (j == 0)          || (heightField[i][j - 1] < WATER_LEVEL);

                    boolean waterNW = (i == 0) ||          (j == 0) ||         (heightField[i - 1][j - 1] < WATER_LEVEL);
                    boolean waterNE = (i == 0) ||          (j == WIDTH - 1) || (heightField[i - 1][j + 1] < WATER_LEVEL);
                    boolean waterSW = (i == HEIGHT - 1) || (j == 0) ||         (heightField[i + 1][j - 1] < WATER_LEVEL);
                    boolean waterSE = (i == HEIGHT - 1) || (j == WIDTH - 1) || (heightField[i + 1][j + 1] < WATER_LEVEL);

                    boolean coast = waterN || waterE || waterS || waterW ||
                                    waterNW || waterNE || waterSW || waterSE;

                    grid[i][j] = new MapElement(
                        !coast,
                        choose(waterN, waterW, waterNW,
                            R.drawable.ic_coast_north, R.drawable.ic_coast_west,
                            R.drawable.ic_coast_northwest, R.drawable.ic_coast_northwest_concave),
                        choose(waterN, waterE, waterNE,
                            R.drawable.ic_coast_north, R.drawable.ic_coast_east,
                            R.drawable.ic_coast_northeast, R.drawable.ic_coast_northeast_concave),
                        choose(waterS, waterW, waterSW,
                            R.drawable.ic_coast_south, R.drawable.ic_coast_west,
                            R.drawable.ic_coast_southwest, R.drawable.ic_coast_southwest_concave),
                        choose(waterS, waterE, waterSE,
                            R.drawable.ic_coast_south, R.drawable.ic_coast_east,
                            R.drawable.ic_coast_southeast, R.drawable.ic_coast_southeast_concave),
                        null,i,j);
                }
                else
                {
                    grid[i][j] = new MapElement(false, WATER, WATER, WATER, WATER, null,i,j);
                }
            }
        }
        return grid;
    }

    private static int choose(boolean nsWater, boolean ewWater, boolean diagWater,
        int nsCoastId, int ewCoastId, int convexCoastId, int concaveCoastId)
    {
        int id;
        if(nsWater)
        {
            if(ewWater)
            {
                id = convexCoastId;
            }
            else
            {
                id = nsCoastId;
            }
        }
        else
        {
            if(ewWater)
            {
                id = ewCoastId;
            }
            else if(diagWater)
            {
                id = concaveCoastId;
            }
            else
            {
                id = GRASS[rng.nextInt(GRASS.length)];
            }
        }
        return id;
    }

    private MapData()
    {
        this.settings = new Settings(); //generating the default settings
        this.grid = generateGrid(settings.getMapHeight(),settings.getMapWidth()); //generating the grid
        this.money = settings.getInitialMoney();
        this.gameTime=0;
        this.curFunction = null;

        this. recentIncome=0;
        this.population=0;
        this. employmentRate=0.0;
        this.nResidential=0;
        this. nCommercial=0;
        hasLoaded = false;
    }

    public void load(Context context) //database loading method
    {
        this.db = new SkylineDbHelper(context.getApplicationContext()).getWritableDatabase();
        SettingsCursor settingsCursor = new SettingsCursor(db.query(SettingsTable.NAME,null,null,null,null,null,null));

        try
        {
            if(settingsCursor.moveToFirst()) //row does exist so load settings
            {
                this.settings = settingsCursor.getSettings();
            }
            else //if db is not there store default values for settings generated before
            {
                ContentValues cv = new ContentValues();
                cv.put(SettingsTable.Cols.ID,1);
                cv.put(SettingsTable.Cols.WIDTH,this.settings.getMapWidth());
                cv.put(SettingsTable.Cols.HEIGHT,this.settings.getMapHeight());
                cv.put(SettingsTable.Cols.IMONEY,this.settings.getInitialMoney());
                cv.put(SettingsTable.Cols.CITYNAME,this.settings.getCityName());
                db.insert(SettingsTable.NAME,null,cv);
            }
        }
        finally
        {
            settingsCursor.close();
        }

        MapCursor mapCursor = new MapCursor(db.query(GameTable.NAME,null,null,null,null,null,null));
        try
        {
            if(mapCursor.moveToFirst()) //row does exist so load MapData
            {
                mapCursor.getMap(); //will access the singleton and set its fields on its end
            }
            else//if db is not there store default values for MapData
            {
                ContentValues cv = new ContentValues();
                cv.put(GameTable.Cols.ID,1);
                cv.put(GameTable.Cols.MONEY,this.money);
                cv.put(GameTable.Cols.GAMETIME,this.gameTime);
                cv.put(GameTable.Cols.RECENTINCOME,this.recentIncome);
                cv.put(GameTable.Cols.POPULATION,this.population);
                cv.put(GameTable.Cols.EMPLOYRATE,this.employmentRate);
                cv.put(GameTable.Cols.RESIDENTIAL,this.nResidential);
                cv.put(GameTable.Cols.COMMERCIAL,this.nCommercial);
                db.insert(GameTable.NAME,null,cv);
            }
        }
        finally
        {
            mapCursor.close();
        }

        MapElementCursor mapElementCursor = new MapElementCursor(db.query(GridTable.NAME,null,null,null,null,null,null));
        int ii =0;
        try
        {
            if(mapElementCursor.moveToFirst()) //rows exist so load the map elements
            {
                while(!mapElementCursor.isAfterLast())
                {
                    MapElement myMapElement;
                    myMapElement = mapElementCursor.getElement();
                    this.grid[myMapElement.getRow()][myMapElement.getCol()] =  myMapElement;
                    mapElementCursor.moveToNext();
                }
            }
            else //if db is not there store newly generated values into db
            {
                for (MapElement[] mapElements : this.grid)
                {
                    for(MapElement tempMapElement:mapElements)
                    {
                        ContentValues cv = new ContentValues();
                        String temp = tempMapElement.getRow() +"_"+ tempMapElement.getCol();
                        cv.put(GridTable.Cols.ID,temp);
                        cv.put(GridTable.Cols.BUILDABLE,tempMapElement.isBuildableNumber());
                        cv.put(GridTable.Cols.TERRAINNW,tempMapElement.getNorthWest());
                        cv.put(GridTable.Cols.TERRAINSW,tempMapElement.getSouthWest());
                        cv.put(GridTable.Cols.TERRAINNE,tempMapElement.getNorthEast());
                        cv.put(GridTable.Cols.TERRAINSE,tempMapElement.getSouthEast());
                        if(tempMapElement.getStructure()==null)
                        {
                            cv.put(GridTable.Cols.DRAWABLE,0);
                            cv.put(GridTable.Cols.LABEL,"BLANK");
                            cv.put(GridTable.Cols.TYPE,"BLANK");
                        }
                        else
                        {
                            cv.put(GridTable.Cols.DRAWABLE,tempMapElement.getStructure().getDrawableId());
                            cv.put(GridTable.Cols.LABEL,tempMapElement.getStructure().getLabel());
                            cv.put(GridTable.Cols.TYPE,tempMapElement.getStructure().getType());
                        }

                        cv.put(GridTable.Cols.NAME,tempMapElement.getStuctName());
                        db.insert(GridTable.NAME,null,cv);
                        ii++;
                    }

                }
                Log.v("LOOPCHECK","Peformed "+ii+" Inserts");
            }
        }
        finally
        {
            mapElementCursor.close();
        }

        hasLoaded = true;
    }

    public void updateDBGrid() //method for updating the grid database table
    {
        //number of rows, iterates through row numbers
        for (MapElement[] mapElements : this.grid)
        {
            for(MapElement tempMapElement:mapElements)
            {
                ContentValues cv = new ContentValues();
                String temp = tempMapElement.getRow()+ "_" + tempMapElement.getCol();
                cv.put(GridTable.Cols.ID, temp);
                cv.put(GridTable.Cols.BUILDABLE, tempMapElement.isBuildableNumber());
                cv.put(GridTable.Cols.TERRAINNW, tempMapElement.getNorthWest());
                cv.put(GridTable.Cols.TERRAINSW, tempMapElement.getSouthWest());
                cv.put(GridTable.Cols.TERRAINNE, tempMapElement.getNorthEast());
                cv.put(GridTable.Cols.TERRAINSE, tempMapElement.getSouthEast());
                if (tempMapElement.getStructure() == null) {
                    cv.put(GridTable.Cols.DRAWABLE, 0);
                    cv.put(GridTable.Cols.LABEL, "BLANK");
                    cv.put(GridTable.Cols.TYPE, "BLANK");
                } else {
                    cv.put(GridTable.Cols.DRAWABLE, tempMapElement.getStructure().getDrawableId());
                    cv.put(GridTable.Cols.LABEL, tempMapElement.getStructure().getLabel());
                    cv.put(GridTable.Cols.TYPE, tempMapElement.getStructure().getType());
                }
                cv.put(GridTable.Cols.NAME, tempMapElement.getStuctName());
                String[] whereValue = {temp};
                int eatmyentireanus= db.update(GridTable.NAME, cv, GridTable.Cols.ID + " = ?", whereValue);
                int anusagain = eatmyentireanus;
            }
        }
    }

    public void updateDBSettings() //method for updating the settings table
    {
        ContentValues cv = new ContentValues();
        cv.put(SettingsTable.Cols.ID,1);
        cv.put(SettingsTable.Cols.WIDTH,this.settings.getMapWidth());
        cv.put(SettingsTable.Cols.HEIGHT,this.settings.getMapHeight());
        cv.put(SettingsTable.Cols.IMONEY,this.settings.getInitialMoney());
        cv.put(SettingsTable.Cols.CITYNAME,this.settings.getCityName());
        String[] whereValue = {"1"};
        db.update(SettingsTable.NAME,cv,SettingsTable.Cols.ID + " = ?",whereValue);
    }

    public void updateDBMap() //method for updating singletons fields table
    {
        ContentValues cv = new ContentValues();
        cv.put(GameTable.Cols.ID,1);
        cv.put(GameTable.Cols.MONEY,this.money);
        cv.put(GameTable.Cols.GAMETIME,this.gameTime);
        cv.put(GameTable.Cols.RECENTINCOME,this.recentIncome);
        cv.put(GameTable.Cols.POPULATION,this.population);
        cv.put(GameTable.Cols.EMPLOYRATE,this.employmentRate);
        cv.put(GameTable.Cols.RESIDENTIAL,this.nResidential);
        cv.put(GameTable.Cols.COMMERCIAL,this.nCommercial);
        String[] whereValue = {"1"};
        db.update(GameTable.NAME,cv,GameTable.Cols.ID + " = ?",whereValue);

    }

    public void regenerateMap() //doesnt modify settings just regenerates the map and restores singelton fields
    {
        this.grid = generateGrid(settings.getMapHeight(),settings.getMapWidth());
        this.money = settings.getInitialMoney();
        this.gameTime=0;
        this. recentIncome=0;
        this.population=0;
        this. employmentRate=0.0;
        this.nResidential=0;
        this. nCommercial=0;
        curFunction=null;
        currStructure=null;
        currMapElement=null;
    }

    public MapElement get(int i, int j)
    {
        return grid[i][j];
    }

    public Settings getSettings() {
        return settings;
    }

    public int getMoney() {
        return money;
    }

    public int getGameTime() {
        return gameTime;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public MapElement getCurrMapElement() {
        return currMapElement;
    }

    public void setCurrMapElement(MapElement currMapElement) {
        this.currMapElement = currMapElement;
    }

    public void setGameTime(int gameTime) {
            this.gameTime = gameTime;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public Structure getCurrStructure() {
        return currStructure;
    }

    public void setCurrStructure(Structure currStructure) {
        this.currStructure = currStructure;
    }

    public String getCurFunction() {
        return curFunction;
    }

    public void setCurFunction(String curFunction) {
        this.curFunction = curFunction;
    }

    public int getHEIGHT()
    {
        return this.settings.getMapHeight();
    }
    public int getWIDTH()
    {
        return this.settings.getMapWidth();
    }

    public void timeUnitFinished() //time button has been pressed do the calculation
    {
        gameTime++;
        population = settings.getFamilySize()*nResidential;
        employmentRate = Math.min(1.0,(float)nCommercial*(float)settings.getShopSize()/(float)population);
        recentIncome = (int)(population*(employmentRate*settings.getSalary()*settings.getTaxRate()-settings.getServiceCost()));
        money+=recentIncome;
    }

    public int getRecentIncome() {
        return recentIncome;
    }

    public void setRecentIncome(int recentIncome) {
        this.recentIncome = recentIncome;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public double getEmploymentRate() {
        return employmentRate;
    }

    public void setEmploymentRate(double employmentRate) {
        this.employmentRate = employmentRate;
    }

    public int getnResidential() {
        return nResidential;
    }

    public void setnResidential(int nResidential) {
        this.nResidential = nResidential;
    }

    public int getnCommercial() {
        return nCommercial;
    }

    public void setnCommercial(int nCommercial) {
        this.nCommercial = nCommercial;
    }

    public boolean isHasLoaded() {
        return hasLoaded;
    }

    /*
            explaining numbering system on bellow algorithm, below is a 4x4 array an example of the map
            -----------------
            | 1 | 2 |   | 3 |
            -----------------
            | 4 | 5 |   |   |
            -----------------
            |   |   |   | 6 |
            -----------------
            | 7 | 8 |   | 9 |
            -----------------
            -each of the above numbers indicates a difference type of situation
            -depending on the situation certain checks should be skipped for example for situation 1
              the checking for above and to the left should be  because it is null
         */
    public boolean roadCheck(MapElement selectedElement)
    {
        boolean returnValue = false;
        int row = selectedElement.getRow();
        int col = selectedElement.getCol();
        int rowMax = grid.length;
        int colMax = grid[0].length;

        if(row!=0) //skipping above check as situation is either 1,2 or 3
        {
            if(grid[row-1][col].getStructure()!=null)
            {
                if(grid[row-1][col].getStructure().getType().equals("Road")) //checking above for road
                {
                    returnValue = true;
                }
            }

        }
        if(row!=rowMax-1) //skipping below check as situation is either 7,8 or 9
        {
            if(grid[row+1][col].getStructure()!=null)
            {
                if (grid[row + 1][col].getStructure().getType().equals("Road")) //checking below for road
                {
                    returnValue = true;
                }
            }
        }
       if(col!=0) //skipping left check as situation is either 1,4 or 7
       {
           if(grid[row][col-1].getStructure()!=null)
           {
               if (grid[row][col - 1].getStructure().getType().equals("Road")) //checking left for road
               {
                   returnValue = true;
               }
           }
       }
       if(col!=colMax-1) //skipping right check as situation is either 3,6 or 9
       {
           if(grid[row][col+1].getStructure()!=null)
           {
               if (grid[row][col + 1].getStructure().getType().equals("Road")) //checking right for road
               {
                   returnValue = true;
               }
           }
       }

       return returnValue;
    }
}
