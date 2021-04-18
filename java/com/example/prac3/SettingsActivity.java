package com.example.prac3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    Button applyButton,returnBut;
    EditText money;
    EditText height;
    EditText width;
    EditText city;
    MapData myMapData = MapData.get();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        applyButton = findViewById(R.id.applyButton);
        money = findViewById(R.id.editTextMoney);
        height = findViewById(R.id.editTextHeight);
        width = findViewById(R.id.editTextWidth);
        city = findViewById(R.id.editTextCity);
        returnBut = findViewById(R.id.returnButton);

        money.setText(String.valueOf(myMapData.getSettings().getInitialMoney()));
        height.setText(String.valueOf(myMapData.getSettings().getMapHeight()));
        width.setText(String.valueOf(myMapData.getSettings().getMapWidth()));
        city.setText(myMapData.getSettings().getCityName());

        applyButton.setOnClickListener(new View.OnClickListener() //apply settings restart map
        {
            @Override
            public void onClick(View view)
            {
                myMapData.getSettings().setInitialMoney(Integer.parseInt(money.getText().toString())); //retrive settings from views in activity
                myMapData.getSettings().setMapHeight(Integer.parseInt(height.getText().toString()));
                myMapData.getSettings().setMapWidth(Integer.parseInt(width.getText().toString()));
                myMapData.getSettings().setCityName(city.getText().toString());
                myMapData.regenerateMap(); //regerates the map, updates the cash field, grid db is updated already in regenerate function
                myMapData.updateDBSettings(); //update settings db
                myMapData.updateDBGrid();
                myMapData.updateDBMap();
                Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        returnBut.setOnClickListener(new View.OnClickListener() //return from settings dont apply settings
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }
}