package com.mudez.mbf.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.mudez.mbf.R;
import com.mudez.mbf.models.AgendaObject;
import com.mudez.mbf.ui.pages.AgendaViewActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {
    private Context context;
    private List<AgendaObject> agendaObjects;
    private SimpleDateFormat simpleDateFormat;


    @SuppressLint("SimpleDateFormat")
    public CardViewAdapter(Context context, List<AgendaObject> agendaObjectList){
        this.context = context;
        this.agendaObjects = agendaObjectList;
        simpleDateFormat = new SimpleDateFormat("MMM-yyyy");
    }

    public List<AgendaObject> getAgendaObjects() {
        return agendaObjects;
    }

    public void setAgendaObjects(List<AgendaObject> agendaObjects) {
        this.agendaObjects = agendaObjects;
    }

    @NonNull
    @Override
    public CardViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.card_item,parent,false);
        return new CardViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewAdapter.ViewHolder holder, final int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(agendaObjects.get(position).getDate());
        holder.getDayCount().setText("Day".concat(" ").concat(String.valueOf(position + 1)));
        holder.getMonthYear().setText(simpleDateFormat.format(calendar.getTime()));
        holder.getDay().setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        holder.getMaterialCardView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context.getApplicationContext(), AgendaViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("agenda_object",agendaObjects.get(position));
                bundle.putInt("position",position);
                intent.putExtra("bundle",bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return agendaObjects.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder{
         MaterialCardView materialCardView;
         TextView dayCount;
         TextView day;
         TextView monthYear;
         ViewHolder(@NonNull View itemView) {
            super(itemView);
            materialCardView = itemView.findViewById(R.id.card);
            dayCount = itemView.findViewById(R.id.day_count);
            day = itemView.findViewById(R.id.dayOfMonth);
            monthYear = itemView.findViewById(R.id.year_month);
        }

         public MaterialCardView getMaterialCardView() {
             return materialCardView;
         }

         public TextView getDayCount() {
             return dayCount;
         }

         public TextView getDay() {
             return day;
         }

         public TextView getMonthYear() {
             return monthYear;
         }
     }
}
