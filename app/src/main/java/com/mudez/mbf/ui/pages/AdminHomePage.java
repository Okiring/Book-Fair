package com.mudez.mbf.ui.pages;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mudez.mbf.R;
import com.mudez.mbf.adapters.CardViewAdapter;
import com.mudez.mbf.models.AgendaObject;
import com.mudez.mbf.repository.Repository;
import com.mudez.mbf.viewmodel.CollectionViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdminHomePage extends AppCompatActivity {

    CollectionViewModel collectionViewModel;
    CardViewAdapter cardViewAdapter;


    @Override
    public void onBackPressed() {
        final AlertDialog alertDialog = new AlertDialog.Builder(AdminHomePage.this).setTitle("Are you sure?").setMessage("Do you want to log Out")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseAuth.getInstance().signOut();
                        finish();


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })

                .create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));


            }
        });

        alertDialog.show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home_page);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        cardViewAdapter = new CardViewAdapter(this,new ArrayList<AgendaObject>());
        recyclerView.setAdapter(cardViewAdapter);
        FloatingActionButton floatingActionButton = findViewById(R.id.fabButton);
        collectionViewModel = ViewModelProviders.of(AdminHomePage.this).get(CollectionViewModel.class);
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

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHomePage.this,AdminAddAgenda.class));
            }
        });
    }
}
