package com.mudez.mbf.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class AppHelpers {



    public static void show_snackBar(View view, String message){

        final Snackbar snackbar = Snackbar.make(view,message, Snackbar.LENGTH_LONG);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                snackbar.dismiss();

            }
        });

        snackbar.show();


    }

    //shows custom alertDialogue

    public static void showAlertDialogue(Activity activity, String message){


        AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                    }
                })
                .create();

        alertDialog.show();






    }


}
