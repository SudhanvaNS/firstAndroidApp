package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherRVAdapter extends RecyclerView.Adapter<WeatherRVAdapter.ViewHold> {
    private Context context;
    private ArrayList<WeatherRVModal> WeatherRVModalArrayList;

    public WeatherRVAdapter(Context context, ArrayList<WeatherRVModal> weatherRVModalArrayList) {
        this.context = context;
        WeatherRVModalArrayList = weatherRVModalArrayList;
    }

    @NonNull
    @Override
    public WeatherRVAdapter.ViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.weather_rv_item,parent, attachToRoot: false);
        return  new  ViewHold(view);

    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WeatherRVAdapter.ViewHold holder, int position) {
        WeatherRVModal modal=WeatherRVModalArrayList.get(position);
        holder.temperatureTV.setText(modal.getTemperature()+"°c");
        Picasso.get().load("http:".concat(modal.getIcon())).into(holder.conditionIV);
        holder.windTV.setText(modal.getWindspeed()+"km/hr");
        SimpleDateFormat input=new SimpleDateFormat(pattern: "yyyy-MM-dd hh:mm" );
        SimpleDateFormat output=new SimpleDateFormat(pattern : "hh:mm aa");
        try{
            Date t=input.parse(modal.getTime());
            holder.timeTV.setText(output.format(t));
        }catch(ParseException e){
            e.printStackTrace();
        }


    }
    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHold extends  RecyclerView.ViewHolder{
        private TextView windTV,temperatureTV,timeTV;
        private ImageView conditionIV;


        public ViewHold(@NonNull View itemView) {
            super(itemView);
            windTV=itemView.findViewById(R.id.idTVWindspeed);
            temperatureTV=itemView.findViewById(R.id.idTVTemperature);
            timeTV=itemView.findViewById(R.id.idTVTime);
            conditionIV=itemView.findViewById(R.id.idIVCondition);
        }
    }
}
