package com.mudez.mbf.ui.pages;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mudez.mbf.R;
import com.mudez.mbf.adapters.AgendaViewAdapter;
import com.mudez.mbf.helpers.Event;
import com.mudez.mbf.models.AgendaItemObject;
import com.mudez.mbf.models.AgendaObject;
import com.mudez.mbf.viewmodel.CollectionViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AdminAddAgenda extends AppCompatActivity implements View.OnClickListener{

    Date pickedDate;
    TextView dateView;
    AgendaObject agendaObject;
    AgendaViewAdapter agendaViewAdapter;
    AlertDialog alertDialog;
    SimpleDateFormat simpleDateFormat;
    CollectionViewModel collectionViewModel;
    private Observer<Event<Integer>> saveResponseObserver;
    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_agenda);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        agendaObject = new AgendaObject();
        agendaViewAdapter = new AgendaViewAdapter(this,new ArrayList<AgendaItemObject>(),null);
        recyclerView.setAdapter(agendaViewAdapter);
        TextView cancelButton = findViewById(R.id.cancelButton);
        TextView saveButton = findViewById(R.id.saveButton);
        LinearLayout linearLayout = findViewById(R.id.addAgendaItem);
        Button chooseDate = findViewById(R.id.chooseDate);
         dateView = findViewById(R.id.dateView);
        cancelButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        chooseDate.setOnClickListener(this);
        simpleDateFormat = new SimpleDateFormat("yyyy-MMM-dd");
        collectionViewModel = ViewModelProviders.of(this).get(CollectionViewModel.class);


        linearLayout.setOnClickListener(this);

    }




    @Override
    protected void onStart() {
        super.onStart();

        //observer to capture changes coming from the view model
        saveResponseObserver = new Observer<Event<Integer>>() {

            @Override
            public void onChanged(Event<Integer> s) {
                remove();

                if(s != null){
                    if (!s.hasBeenHandled()) {
                        Integer response = s.getContentIfNotHandled();
                        Log.d("response",String.valueOf(response));

                       if(response != null){
                           if(response == 0){
                               startActivity(new Intent(AdminAddAgenda.this,RecordSavedSuccess.class));
                               finish();

                           }else{
                               Toast.makeText(getApplicationContext(),"Problem saving data, please retry.",Toast.LENGTH_LONG).show();
                           }
                       }else {
                           Toast.makeText(getApplicationContext(),"Problem saving data, please retry.",Toast.LENGTH_LONG).show();
                       }

                    }
                }


            }

        };


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0 && resultCode == RESULT_OK){
            if(data != null){
                AgendaItemObject agenda_object = (AgendaItemObject)data.getSerializableExtra("agenda_item");
                agendaViewAdapter.getAgendaItemObjectList().add(agenda_object);
                agendaViewAdapter.notifyDataSetChanged();

            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.cancelButton:
                onBackPressed();
                break;

            case R.id.saveButton:
                agendaObject.setItems(agendaViewAdapter.getAgendaItemObjectList());
                collectionViewModel.saveAgendaObject(agendaObject).observe(AdminAddAgenda.this,saveResponseObserver);
                showDialogue();

                break;

            case R.id.chooseDate:
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(AdminAddAgenda.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(Calendar.YEAR,year);
                        calendar1.set(Calendar.MONTH,month);
                        calendar1.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        pickedDate = calendar1.getTime();
                        dateView.setText(simpleDateFormat.format(pickedDate));
                        agendaObject.setDate(pickedDate.getTime());

                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                break;

            case R.id.addAgendaItem:
                startActivityForResult(new Intent(AdminAddAgenda.this,AddItemPage.class),0);
                break;
        }
    }

    private void showDialogue() {

        LayoutInflater layoutInflater = getLayoutInflater();
        @SuppressLint("InflateParams") View view = layoutInflater.inflate(R.layout.custom_dialogue,null);
        TextView textView = view.findViewById(R.id.alertTitle);
        textView.setText("Saving");
        alertDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setView(view)
                .create();

        alertDialog.show();

    }

    private void remove(){

        alertDialog.dismiss();
    }
}
