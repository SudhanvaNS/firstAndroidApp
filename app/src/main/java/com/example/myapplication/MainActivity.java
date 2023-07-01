package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout homeRL;
    private ProgressBar loadingPB;
    private TextView cityNameTV,temperatureTV,conditionTV;
    private TextInputEditText cityEdt;
    private ImageView backIV,iconIV,searchIV;
    private RecyclerView weatherRV;
    private ArrayList<WeatherRVModal> weatherRVModalArrayList;
    private WeatherRVAdapter weatherRVAdapter;
    private LocationManager locationManager;
    private int PERMISSION_CODE=1;
    private String cityName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

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
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_CODE );
        Location location=locationManager.getLastKnownLocation((LocationManager.NETWORK_PROVIDER));
        cityName=getCityName(location.getLongitude(),location.getLatitude());
        getWeatherInfo(cityName);
        searchIV.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String City=cityEdt.getText().toString();
                if(City.isEmpty()){
                    Toast.makeText(MainActivity.this, "please Enter City Name", Toast.LENGTH_SHORT).show();
                }else{
                    cityNameTV.setText(cityName);
                    getWeatherInfo(City);
                }
            }
        });


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode==PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted.....", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Please Provide the Permission", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    private String getCityName(double longitude,double latitude){
        String cityName="Not found";
        Geocoder gcd= new Geocoder(getBaseContext(), Locale.getDefault());
        try{
               List<Address> addressess= gcd.getFromLocation(latitude,longitude,10);
                for(Address adr:addressess){
                    if(adr!=null){
                        String city=adr.getLocality();
                        if(city!=null &&  !city.equals("")){
                            cityName=city;
                        }else{
                            Log.d("TAG","city not found");
                            Toast.makeText(this, "User city Not Found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        }catch(IOException e){
            e.printStackTrace();
        }
        return cityName;
    }
    private void getWeatherInfo(String cityName){

        String url="https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=48acc405f87a23cbba6f6d4d8913d210&units=metric";
        cityNameTV.setText(cityName);
        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override 
                    public void onResponse(JSONObject response) {
                        loadingPB.setVisibility(View.GONE);
                        homeRL.setVisibility(View.VISIBLE);
                        weatherRVModalArrayList.clear();
                        try{

                            String temperature=response.getJSONObject("main").getString("temp");
                            temperatureTV.setText(temperature+" °C");
                            String condition= response.getJSONArray("weather").getString(0);
                            String icon= response.getJSONObject("weather").getString("icon");
                            String windSpeed= response.getJSONObject("wind").getString("speed");

                            Picasso.get().load("http:".concat(icon)).into(iconIV);
                            WeatherRVModal weatherRVModal = new WeatherRVModal(condition, temperature,icon,windSpeed);
                            weatherRVModalArrayList.add(weatherRVModal);
                            weatherRVAdapter.notifyDataSetChanged();
                            JSONObject sysObject = response.getJSONObject("sys");
                            long sunriseTime = sysObject.getLong("sunrise");
                            long sunsetTime = sysObject.getLong("sunset");
                            long currentTime = System.currentTimeMillis() / 1000; // Convert to seconds
                            boolean isDay = currentTime >= sunriseTime && currentTime < sunsetTime;
                            if(isDay){
                                Picasso.get().load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVLTn0pfQdZcbMRwIJDtd6AaPhWZJfLRfuDd0aiI5G7Dz4rlltuPRg1s7hjnSH9a04nH8&usqp=CAU").into(backIV);
                            }else {
                                Picasso.get().load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR9r75bp0jcUKp2W9i7jseN9L16dGNdGmy8Bw&usqp=CAU").into(backIV);
                            }

                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Please Enter a Valid City Name...", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);

    }
}