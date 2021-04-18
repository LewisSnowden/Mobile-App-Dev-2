package com.example.prac3;
/*
scheme for database
 */
public class SkylineSchema {
    public static class GameTable
    {
        public static final String NAME = "gameData";
        public static class Cols
        {
            public static final String ID = "game_data_id";
            public static final String MONEY = "money";
            public static final String GAMETIME = "game_time";
            public static final String RECENTINCOME = "recent_income";
            public static final String POPULATION = "population";
            public static final String EMPLOYRATE = "employment_rate";
            public static final String RESIDENTIAL = "number_residential";
            public static final String COMMERCIAL = "number_commercial";

        }
    }
    public static class SettingsTable
    {
        public static final String NAME = "settingsData";
        public static class Cols
        {
            public static final String ID = "setting_data_id";
            public static final String WIDTH = "map_width";
            public static final String HEIGHT = "map_height";
            public static final String IMONEY = "initial_money";
            public static final String CITYNAME = "city_name";
        }
    }
    public static class GridTable
    {
        public static final String NAME = "gridData";
        public static class Cols
        {
            public static final String ID = "grid_data_id";  // will be formated row_col eg 0_1 or 11_20
            public static final String BUILDABLE = "buildable";
            public static final String TERRAINNW = "terrain_nw";
            public static final String TERRAINSW = "terrain_sw";
            public static final String TERRAINNE = "terrain_ne";
            public static final String TERRAINSE = "terrain_se";
            public static final String DRAWABLE = "drawable";   //data of structure object
            public static final String LABEL = "label"; //data of structure object
            public static final String TYPE = "type"; //data of structure object
            public static final String NAME = "name";


        }
    }
}
