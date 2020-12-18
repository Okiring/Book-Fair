package com.mudez.mbf.ui.pages;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mudez.mbf.R;
import com.mudez.mbf.adapters.AgendaViewAdapter;
import com.mudez.mbf.helpers.Event;
import com.mudez.mbf.models.AgendaItemObject;
import com.mudez.mbf.models.AgendaObject;
import com.mudez.mbf.viewmodel.CollectionViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AgendaViewActivity extends AppCompatActivity {

    AgendaObject agendaObject;
    AlertDialog alertDialog;
    SharedPreferences sharedPreferences;
    List<AgendaItemObject> itemObjectList;
    AgendaViewAdapter agendaViewAdapter;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agenda_view_screen);
        ImageView banner = findViewById(R.id.banner);
        Button deleteButton = findViewById(R.id.delete);
        sharedPreferences = getApplication().getSharedPreferences("prefs",MODE_PRIVATE);
       boolean isAdmin = sharedPreferences.getBoolean("is_admin",false);
        if(!isAdmin){
            deleteButton.setVisibility(View.GONE);
        }

        Bundle bundle = getIntent().getBundleExtra("bundle");
        Toolbar toolbar = findViewById(R.id.toolbar);
        if(bundle != null){
            agendaObject = (AgendaObject)bundle.getSerializable("agenda_object");
            int position = bundle.getInt("position");
            toolbar.setTitle("Day".concat(" ").concat(String.valueOf(position +1)));
            if(position == 0){
                banner.setImageResource(R.drawable.banner_b_2);


            }else if(position == 1){
                banner.setImageResource(R.drawable.banner_b_1);

            }else{
                banner.setImageResource(R.drawable.banner_b_3);
            }
        }

        CollectionViewModel collectionViewModel = ViewModelProviders.of(this).get(CollectionViewModel.class);
        collectionViewModel.getAgendaItems(agendaObject.getId()).observe(this,new Observer<Event<List<AgendaItemObject>>>() {
            @Override
            public void onChanged(Event<List<AgendaItemObject>> s) {

                if(s != null){
                    if (!s.hasBeenHandled()) {
                        List<AgendaItemObject> response = s.getContentIfNotHandled();
                        if(response != null){
                            itemObjectList = response;
                            agendaViewAdapter.setAgendaItemObjectList(itemObjectList);
                            agendaViewAdapter.notifyDataSetChanged();


                        }else {
                            Toast.makeText(getApplicationContext(),"Problem saving data, please retry.",Toast.LENGTH_LONG).show();
                        }

                    }
                }

            }
        });
        TextView textView = findViewById(R.id.date);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(agendaObject.getDate());
        textView.setText(simpleDateFormat.format(calendar.getTime()));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        agendaViewAdapter = new AgendaViewAdapter(this,new ArrayList<AgendaItemObject>(),agendaObject.getId());
        recyclerView.setAdapter(agendaViewAdapter);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialogue();

                FirebaseFirestore.getInstance().collection("agendas").document(agendaObject.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                           remove();
                        if(task.isSuccessful()){

                            Intent intent = new Intent(AgendaViewActivity.this,RecordSavedSuccess.class);
                            intent.putExtra("message","Record deleted successfully");
                            startActivity(intent);
                            finish();

                        }else{
                            Toast.makeText(getApplicationContext(),"Problem deleting item",Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        });

    }

    private void showDialogue() {

        LayoutInflater layoutInflater = getLayoutInflater();
        @SuppressLint("InflateParams") View view = layoutInflater.inflate(R.layout.custom_dialogue,null);
        TextView textView = view.findViewById(R.id.alertTitle);
        textView.setText("Deleting");
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
