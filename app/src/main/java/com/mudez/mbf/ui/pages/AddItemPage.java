package com.mudez.mbf.ui.pages;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;
import com.mudez.mbf.R;
import com.mudez.mbf.helpers.Event;
import com.mudez.mbf.models.AgendaItemObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class AddItemPage extends AppCompatActivity implements View.OnClickListener {
    TextView fromText;
    TextView toText;
    EditText titleEditText;
    EditText subTitleEditText;
    AgendaItemObject agendaItemObject;
    SimpleDateFormat simpleDateFormat;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);
        agendaItemObject = new AgendaItemObject();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        simpleDateFormat = new SimpleDateFormat("hh:mm a");

        Button from = findViewById(R.id.from);
        titleEditText = findViewById(R.id.title);
        subTitleEditText = findViewById(R.id.subTitle);
        Button to = findViewById(R.id.to);
        Button add = findViewById(R.id.saveItem);
         fromText = findViewById(R.id.fromTime);
         toText = findViewById(R.id.toTime);
         from.setOnClickListener(this);
         to.setOnClickListener(this);
         add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.from:
                final Calendar calendar = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddItemPage.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar1.set(Calendar.MINUTE,minute);
                        fromText.setText(simpleDateFormat.format(calendar1.getTime()));
                        agendaItemObject.setFrom(simpleDateFormat.format(calendar1.getTime()));

                    }
                },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false);
                timePickerDialog.show();
                break;

            case R.id.to:

                Calendar calendar2 = Calendar.getInstance();
                TimePickerDialog timePickerDialog2 = new TimePickerDialog(AddItemPage.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar calendar2 = Calendar.getInstance();
                        calendar2.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar2.set(Calendar.MINUTE,minute);
                        toText.setText(simpleDateFormat.format(calendar2.getTime()));
                        agendaItemObject.setTo(simpleDateFormat.format(calendar2.getTime()));
                    }
                },calendar2.get(Calendar.HOUR_OF_DAY),calendar2.get(Calendar.MINUTE),false);
                timePickerDialog2.show();
                break;

            case R.id.saveItem:

               if(titleEditText.getText().toString().isEmpty() || fromText.getText().toString().isEmpty() || toText.getText().toString().isEmpty()){

                   Toast.makeText(getApplicationContext(),"Please fill all the required fields",Toast.LENGTH_LONG).show();

               }else{
                   agendaItemObject.setTitle(titleEditText.getText().toString());
                   if(subTitleEditText.getText() != null){
                       agendaItemObject.setSubTitle(subTitleEditText.getText().toString());
                   }
                   agendaItemObject.setCreatedAt(Calendar.getInstance().getTimeInMillis());

                   Intent intent = new Intent();
                   intent.putExtra("agenda_item",agendaItemObject);
                   setResult(RESULT_OK,intent);
                   finish();
               }

                break;
        }

    }


}
