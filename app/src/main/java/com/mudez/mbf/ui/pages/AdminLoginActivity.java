package com.mudez.mbf.ui.pages;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mudez.mbf.R;
import com.mudez.mbf.helpers.AppHelpers;

import java.util.Objects;

public class AdminLoginActivity extends AppCompatActivity {
    private TextInputLayout email_layout;
    private TextInputLayout password_layout;
    private LinearLayout linearLayout;
    private AlertDialog alertDialog;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        Button loginButton = findViewById(R.id.loginbutton);
        sharedPreferences = getApplication().getSharedPreferences("prefs",MODE_PRIVATE);

        //layout

        email_layout = findViewById(R.id.emaillayout);
        password_layout = findViewById(R.id.passwordlayout);
        linearLayout = findViewById(R.id.linearLayout);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                if(Objects.requireNonNull(email_layout.getEditText()).getText().toString().isEmpty()
                        || Objects.requireNonNull(password_layout.getEditText()).getText().toString().isEmpty()){

                    AppHelpers.show_snackBar(linearLayout,"Please fill in all the required fields");

                }else{

                    showDialogue();


                    FirebaseAuth.getInstance().signInWithEmailAndPassword(Objects.requireNonNull(email_layout.getEditText()).getText().toString(), Objects.requireNonNull(password_layout.getEditText()).getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            remove();

                            if (task.isSuccessful()) {
                                if(task.getResult()!= null && task.getResult().getUser() != null){
                                    sharedPreferences.edit().putBoolean("is_admin",true).apply();
                                    startActivity(new Intent(AdminLoginActivity.this,AdminHomePage.class));
                                }else{
                                    try {
                                        throw Objects.requireNonNull(task.getException());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }






                            } else {


                                try {
                                    throw Objects.requireNonNull(task.getException());

                                } catch (Exception e) {

                                    if (e instanceof FirebaseAuthInvalidCredentialsException) {



                                        AppHelpers.show_snackBar(linearLayout,"Email or password is incorrect");


                                    } else if (e instanceof FirebaseAuthInvalidUserException) {

                                        if (Objects.requireNonNull(e.getMessage()).equals("ERROR_USER_NOT_FOUND")) {


                                            AppHelpers.show_snackBar(linearLayout,"This user does't exist in our database");


                                        } else {


                                            AppHelpers.show_snackBar(linearLayout,"This user does't exist in our database");


                                        }


                                    } else {

                                        AppHelpers.show_snackBar(linearLayout,"Oops, problem logging in, please try again");


                                    }


                                }


                            }

                        }
                    });



                }


            }
        });
    }

    private void showDialogue() {

        LayoutInflater layoutInflater = getLayoutInflater();
        @SuppressLint("InflateParams") View view = layoutInflater.inflate(R.layout.custom_dialogue,null);
        TextView textView = view.findViewById(R.id.alertTitle);
        textView.setText("Signing in");
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
