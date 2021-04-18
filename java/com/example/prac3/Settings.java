package com.example.prac3;

/*
settings storage object
 */
public class Settings {

    private int mapWidth;
    private int mapHeight;
    private int initialMoney;
    private int familySize;
    private int shopSize;
    private int salary;
    private Double taxRate;
    private int serviceCost;
    private int houseBuildingCost;
    private int commBuildingCost;
    private int roadBuildingCost;
    private String cityName;

    public Settings()
    {
        mapWidth=50;
        mapHeight=10;
        initialMoney=1000;
        familySize=4;
        shopSize=6;
        salary=10;
        taxRate=0.3;
        serviceCost=2;
        houseBuildingCost=100;
        commBuildingCost=500;
        roadBuildingCost=20;
        cityName="Perth";
    }

    public Settings(int inMapWidth,int inMapHeight,int inInitialMoney,String inCityName)
    {
        mapWidth=inMapWidth;
        mapHeight=inMapHeight;
        initialMoney=inInitialMoney;
        familySize=4;
        shopSize=6;
        salary=10;
        taxRate=0.3;
        serviceCost=2;
        houseBuildingCost=100;
        commBuildingCost=500;
        roadBuildingCost=20;
        cityName=inCityName;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getInitialMoney() {
        return initialMoney;
    }

    public int getFamilySize() {
        return familySize;
    }

    public int getShopSize() {
        return shopSize;
    }

    public int getSalary() {
        return salary;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public int getServiceCost() {
        return serviceCost;
    }

    public int getHouseBuildingCost() {
        return houseBuildingCost;
    }

    public int getCommBuildingCost() {
        return commBuildingCost;
    }

    public int getRoadBuildingCost() {
        return roadBuildingCost;
    }

    public String getCityName() {
        return cityName;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    public void setInitialMoney(int initialMoney) {
        this.initialMoney = initialMoney;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
