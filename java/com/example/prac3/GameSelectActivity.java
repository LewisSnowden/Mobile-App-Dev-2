package com.example.prac3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/*
Activity for selecting for either new game or load game
 */
public class GameSelectActivity extends AppCompatActivity {
    Button newGameBut,loadGameBut;
    MapData myMapData = MapData.get();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_select);
        newGameBut = findViewById(R.id.newGameBut);
        loadGameBut = findViewById(R.id.loadGameBut);

        newGameBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                myMapData.regenerateMap(); //regerates the map, updates the cash field, grid db is updated already in regenerate function
                myMapData.updateDBSettings(); //update settings db
                myMapData.updateDBGrid();
                myMapData.updateDBMap();
                Intent intent = new Intent(GameSelectActivity.this,MapActivity.class);
                startActivity(intent);
            }
        });
        loadGameBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                myMapData.load(GameSelectActivity.this); //calling load method to read database
                Intent intent = new Intent(GameSelectActivity.this,MapActivity.class);
                startActivity(intent);
            }
        });

    }


}