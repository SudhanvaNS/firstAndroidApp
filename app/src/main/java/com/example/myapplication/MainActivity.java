package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout homeRL;
    private ProgressBar loadingPB;
    private TextView cityNameTV,temperatureTV,conditionTV;
    private TextInputEditText cityEdt;
    private ImageView backIV,iconIV,searchIV;
    private RecyclerView weatherRV;
    private ArrayList<WeatherRVModal> weatherRVModalArrayList;
    private WeatherRVAdapter weatherRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        homeRL= findViewById(R.id.idRLHome);
        loadingPB= findViewById(R.id.idPBLoading);
        cityNameTV= findViewById(R.id.idTVCityName);
        temperatureTV= findViewById(R.id.idTVTemperature);
        conditionTV= findViewById(R.id.idTVCondition);
        weatherRV= findViewById(R.id.idRVWeather);
        cityEdt= findViewById(R.id.idEDtCity);
        backIV= findViewById(R.id.idIVBack);
        iconIV= findViewById(R.id.idIVIcon);
        searchIV= findViewById(R.id.idIVSearch);
        weatherRVModalArrayList=new ArrayList<>();
        weatherRVAdapter=new WeatherRVAdapter(this,weatherRVModalArrayList);
        weatherRV.setAdapter(weatherRVAdapter);
    }
    private void getWeatherInfo(String cityName){
        String url="https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}"
    }
}