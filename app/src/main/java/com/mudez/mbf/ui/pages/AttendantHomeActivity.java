package com.mudez.mbf.ui.pages;

import android.os.Bundle;
import android.util.Log;
;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mudez.mbf.R;
import com.mudez.mbf.adapters.CardViewAdapter;
import com.mudez.mbf.models.AgendaObject;
import com.mudez.mbf.viewmodel.CollectionViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AttendantHomeActivity extends AppCompatActivity {

    CollectionViewModel collectionViewModel;
    CardViewAdapter cardViewAdapter;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendant_home_screen);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(cardViewAdapter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        cardViewAdapter = new CardViewAdapter(this,new ArrayList<AgendaObject>());
        recyclerView.setAdapter(cardViewAdapter);

        collectionViewModel = ViewModelProviders.of(AttendantHomeActivity.this).get(CollectionViewModel.class);
        collectionViewModel.getQuerySnapshotLiveData().observe(this,new Observer<QuerySnapshot>() {
            @Override
            public void onChanged(QuerySnapshot queryDocumentSnapshots) {


                List<AgendaObject> agendaObjectArrayList = new ArrayList<>();
                for(DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                    AgendaObject agendaObject = snapshot.toObject(AgendaObject.class);
                    Objects.requireNonNull(agendaObject).setId(snapshot.getId());
                    agendaObjectArrayList.add(agendaObject);
                }
                cardViewAdapter.setAgendaObjects(agendaObjectArrayList);

                cardViewAdapter.notifyDataSetChanged();


            }
        });


    }
}
