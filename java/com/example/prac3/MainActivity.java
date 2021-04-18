package com.example.prac3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
/*
Activity for start screen
 */
public class MainActivity extends AppCompatActivity {
    Button settings;
    Button map;
    MapData myMapData = MapData.get();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings = findViewById(R.id.buttonSettings);
        if(!myMapData.isHasLoaded())
        {
            myMapData.load(MainActivity.this);
        }
        map = findViewById(R.id.buttonMap);

        settings.setOnClickListener(new View.OnClickListener()  //settings button listner
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent);
            }
        });

        map.setOnClickListener(new View.OnClickListener() //game select button
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this,GameSelectActivity.class);
                startActivity(intent);
            }
        });


    }

}