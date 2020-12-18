package com.mudez.mbf;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mudez.mbf.adapters.GridViewAdapter;
import com.mudez.mbf.models.GridObject;
import com.mudez.mbf.ui.pages.AdminLoginActivity;
import com.mudez.mbf.ui.pages.AttendantHomeActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    GridObject gridObject;
    SharedPreferences sharedPreferences;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        final ArrayList<GridObject> gridObjectArrayList = new ArrayList<>();
        Button proceedButton = findViewById(R.id.proceed);
        gridObjectArrayList.add(new GridObject("Attender",true,1));
        gridObjectArrayList.add(new GridObject("Organiser",false,2));
        GridView gridView = findViewById(R.id.gridView);
        gridObject = gridObjectArrayList.get(0);
        sharedPreferences = getApplication().getSharedPreferences("prefs",MODE_PRIVATE);
        final GridViewAdapter gridViewAdapter = new GridViewAdapter(this,gridObjectArrayList);
        gridView.setAdapter(gridViewAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               for(int i = 0; i < gridViewAdapter.getGridObjects().size();i++){
                   gridViewAdapter.getGridObjects().get(i).setTapped(false);
               }
               gridViewAdapter.getItem(position).setTapped(true);
               gridObject = gridObjectArrayList.get(position);
                gridViewAdapter.notifyDataSetChanged();
            }
        });
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

         if(gridObject.getId() ==1){
             showDialogue();
             FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                     remove();

                     if (task.isSuccessful()){
                         sharedPreferences.edit().putBoolean("is_admin",false).apply();
                         startActivity(new Intent(MainActivity.this, AttendantHomeActivity.class));
                     }else{

                         Toast.makeText(getApplicationContext(),"There was a problem connecting to database",Toast.LENGTH_LONG).show();
                     }

                 }
             });
         }else{
             startActivity(new Intent(MainActivity.this, AdminLoginActivity.class));
         }
            }
        });
    }

    private void showDialogue() {

        LayoutInflater layoutInflater = getLayoutInflater();
        @SuppressLint("InflateParams") View view = layoutInflater.inflate(R.layout.custom_dialogue,null);
        TextView textView = view.findViewById(R.id.alertTitle);
        textView.setText("Preparing");
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
