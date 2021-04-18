package com.example.prac3;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.text.DecimalFormat;
import java.util.Timer;

import javax.net.ssl.HttpsURLConnection;

public class MapActivity extends AppCompatActivity implements MapFragment.Callbacks{

    private ImageView construct,demolish,detail;
    private Button returnBut,timeBut;
    private MapData myMap = MapData.get();
    private TextView time,temperature,income,money,population,employment,city;
    private int timeAmmount = 60000; //this value will change the ammount of time between weather updates 60000 is 1 minute
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        FragmentManager fm = getSupportFragmentManager();
        Fragment mapFrag = fm.findFragmentById(R.id.map);
        Fragment selectorFrag = fm.findFragmentById(R.id.selector);
        construct = findViewById(R.id.constructImage);
        demolish = findViewById(R.id.deleteImage);
        detail = findViewById(R.id.detailsImage);
        returnBut = findViewById(R.id.buttonReturn);
        timeBut = findViewById(R.id.timeButton);
        time=findViewById(R.id.timeText);
        temperature=findViewById(R.id.tempText);
        income=findViewById(R.id.incomeText);
        money=findViewById(R.id.moneyText);
        population=findViewById(R.id.populationText);
        employment=findViewById(R.id.employmentText);
        city=findViewById(R.id.cityText);
        try {
            updateHud();
        } catch (IOException e) {
            e.printStackTrace();
        }


        if(selectorFrag==null){
            selectorFrag = new SelectorFragment();
            fm.beginTransaction().add(R.id.selector,selectorFrag).commit();
        }
        if (mapFrag==null){
            mapFrag = new MapFragment();
            fm.beginTransaction().add(R.id.map,mapFrag).commit();
        }
        mHandler = new Handler(); //handler for timer
        updateWeather(); //the call for update weather
        returnBut.setOnClickListener(new View.OnClickListener()  //settings button listner
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MapActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        construct.setOnClickListener(new View.OnClickListener()  //settings button listner
        {
            @Override
            public void onClick(View view)
            {
                construct.setBackgroundColor(0xFF99cc00);
                demolish.setBackgroundColor(0xFFFFFFFF);
                detail.setBackgroundColor(0xFFFFFFFF);
                myMap.setCurFunction("construct");
            }
        });
        demolish.setOnClickListener(new View.OnClickListener()  //settings button listner
        {
            @Override
            public void onClick(View view)
            {
                construct.setBackgroundColor(0xFFFFFFFF);
                demolish.setBackgroundColor(0xFF99cc00);
                detail.setBackgroundColor(0xFFFFFFFF);
                myMap.setCurFunction("demolish");

            }
        });
        detail.setOnClickListener(new View.OnClickListener()  //settings button listner
        {
            @Override
            public void onClick(View view)
            {
                construct.setBackgroundColor(0xFFFFFFFF);
                demolish.setBackgroundColor(0xFFFFFFFF);
                detail.setBackgroundColor(0xFF99cc00);
                myMap.setCurFunction("detail");

            }
        });
        final Fragment finalMapFrag = mapFrag;
        timeBut.setOnClickListener(new View.OnClickListener()  //settings button listner
        {
            @Override
            public void onClick(View view)
            {
                myMap.timeUnitFinished();
                try {
                    updateHud();
                    myMap.updateDBMap();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ((MapFragment) finalMapFrag).refresh();
            }
        });


    }
    /*
    timer code copied from
    https://stackoverflow.com/questions/6242268/repeat-a-task-with-a-time-delay
     */
    Runnable statusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                updateHud();
                Toast.makeText(MapActivity.this,"Weather Updated!",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                mHandler.postDelayed(statusChecker, timeAmmount);
            }
        }
    };

    void updateWeather() {
        statusChecker.run();
    }

    void stopWeatherUpdate() {
        mHandler.removeCallbacks(statusChecker);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopWeatherUpdate();
    }

    @Override
    public void onMapSelected() //callback for updating recycler views on tablets or launch activity for phone when question is selected
    {
        try {
            updateHud();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateHud() throws IOException {

        time.setText("Time: "+myMap.getGameTime());

        income.setText("Income: "+myMap.getRecentIncome()); //updating the user HUD above map
        money.setText("Money: "+myMap.getMoney());
        population.setText("Population: "+myMap.getPopulation());
        employment.setText("Employment: "+myMap.getEmploymentRate()*100+"%");
        city.setText("City: "+myMap.getSettings().getCityName());
        String urlString = Uri.parse("http://api.openweathermap.org/data/2.5/weather")
                .buildUpon()
                .appendQueryParameter("q", myMap.getSettings().getCityName())
                .appendQueryParameter("appid", "b07f9eb9de9d56d3616a0f4646139920")
                .build().toString();
        try {
            URL url = new URL(urlString);
            new GetWeather().execute(url);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    private class GetWeather extends AsyncTask<URL, Void, String> //A sync function for getting the weather
    {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(URL... params)
        {
            String data="";
            String reFormat = "";
            for(URL url : params)
            {
                try
                {
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    try
                    {
                        InputStream is = conn.getInputStream();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int bytesRead = is.read(buffer);
                        while(bytesRead > 0)
                        {
                            baos.write(buffer, 0, bytesRead);
                            bytesRead = is.read(buffer);
                        }
                        baos.close();
                        data = new String(baos.toByteArray());
                    }
                    finally
                    {
                        conn.disconnect();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            try {
                JSONObject jBase = new JSONObject(data);
                JSONObject jMain = jBase.getJSONObject("main");
                reFormat = jMain.getString("temp");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return reFormat;
        }
        @Override
        protected void onPostExecute(String result)
        {
            double temp;
            String actualResult="";
            if(!result.equals(""))
            {
                temp = Double.parseDouble(result);
                temp = temp -273.15;
                BigDecimal bd = new BigDecimal(temp).setScale(2, RoundingMode.HALF_UP);
                actualResult = Double.toString(bd.doubleValue());
            }

            temperature.setText("Temperature: "+actualResult);
        }
    }


}