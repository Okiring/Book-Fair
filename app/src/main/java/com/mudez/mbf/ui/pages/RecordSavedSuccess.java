package com.mudez.mbf.ui.pages;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mudez.mbf.R;

public class RecordSavedSuccess extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_success);
        TextView messageView = findViewById(R.id.message);
        String message = getIntent().getStringExtra("message");
        if(message != null){
            messageView.setText(message);

        }
        Button button = findViewById(R.id.back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
