package com.example.prac3;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

/*
Activity class for the Detail activity
 */
public class DetailActivity extends AppCompatActivity {
    TextView rowLabel,colLabel,structTypeText;
    EditText nameEditText;
    Button takePhoto,returnBut;
    MapData myMap = MapData.get();
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        rowLabel = findViewById(R.id.rowLabel);
        colLabel = findViewById(R.id.colLabel);
        structTypeText = findViewById(R.id.stuctTypeText);
        nameEditText = findViewById(R.id.nameEditText);
        takePhoto = findViewById(R.id.photoBut);
        returnBut = findViewById(R.id.returnButDetail);
        image = findViewById(R.id.thumbnail);

        rowLabel.setText(Integer.toString(myMap.getCurrMapElement().getRow()));
        colLabel.setText(Integer.toString(myMap.getCurrMapElement().getCol()));
        structTypeText.setText(myMap.getCurrMapElement().getStructure().getType());
        nameEditText.setText(myMap.getCurrMapElement().getStuctName());

        if(myMap.getCurrMapElement().getImage()!=null) //if the image stored in the selected map cell set it to the view
        {
            image.setImageBitmap(myMap.getCurrMapElement().getImage());
        }

        takePhoto.setOnClickListener(new View.OnClickListener()  //take photo button
        {
            @Override
            public void onClick(View view)
            {
                Intent thumbIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //camera activity intent
                startActivityForResult(thumbIntent,1);
            }
        });
        returnBut.setOnClickListener(new View.OnClickListener()  //return button changer
        {
            @Override
            public void onClick(View view)
            {
                myMap.getCurrMapElement().setStuctName(nameEditText.getText().toString()); //setting the strucutres name
                myMap.updateDBGrid(); //as name might of changed in stuct updating the db
                Intent intent = new Intent(DetailActivity.this,MapActivity.class);
                startActivity(intent);
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent resultIntent) //event handler for camera taken
    {
        super.onActivityResult(requestCode,resultCode,resultIntent);
        if(resultCode== Activity.RESULT_OK&&requestCode==1)
        {
            Bitmap result = (Bitmap) Objects.requireNonNull(resultIntent.getExtras()).get("data");
            image.setImageBitmap(result);
            myMap.getCurrMapElement().setImage(result);
        }
    }
}