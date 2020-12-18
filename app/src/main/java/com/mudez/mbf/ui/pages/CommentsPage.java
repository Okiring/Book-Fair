package com.mudez.mbf.ui.pages;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mudez.mbf.R;
import com.mudez.mbf.adapters.AgendaViewAdapter;
import com.mudez.mbf.adapters.CommentsAdapter;
import com.mudez.mbf.models.AgendaItemObject;
import com.mudez.mbf.models.AgendaObject;
import com.mudez.mbf.models.CommentObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommentsPage extends AppCompatActivity {
AlertDialog alertDialog;
CommentsAdapter commentsAdapter;
    String agenda_id;
    String doc_id;
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    EditText comment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments);
        Button post = findViewById(R.id.post);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        agenda_id = bundle.getString("agenda_id");
        doc_id = bundle.getString("doc_id");
        comment = findViewById(R.id.commentEditBox);
        comment.setImeOptions(EditorInfo.IME_ACTION_DONE);
        comment.setRawInputType(InputType.TYPE_CLASS_TEXT);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        commentsAdapter = new CommentsAdapter(this,new ArrayList<CommentObject>());
        recyclerView.setAdapter(commentsAdapter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        FirebaseFirestore.getInstance().collection("agendas").document(agenda_id).collection("items")
                .document(doc_id).collection("comments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                if(task.isSuccessful()){

               if(task.getResult() != null){
                   List<CommentObject> commentObjectList = new ArrayList<>();

                   for(DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                       commentObjectList.add(documentSnapshot.toObject(CommentObject.class));
                   }

                   commentsAdapter.setCommentObjectList(commentObjectList);
                   commentsAdapter.notifyDataSetChanged();
               }

                }

            }
        });


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comment.getText().toString().isEmpty()){

                    Toast.makeText(getApplicationContext(),"Comment cannot be empty",Toast.LENGTH_LONG).show();

                }else{
                    final CommentObject commentObject = new CommentObject(comment.getText().toString());
                    showDialogue();
                    FirebaseFirestore.getInstance().collection("agendas").document(agenda_id).collection("items")
                            .document(doc_id).collection("comments").add(commentObject).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            remove();

                            if(task.isSuccessful()){
                                comment.getText().clear();
                                commentsAdapter.getCommentObjectList().add(0,commentObject);
                                commentsAdapter.notifyDataSetChanged();

                            }else{
                                Toast.makeText(getApplicationContext(),"Failed submitting comment",Toast.LENGTH_LONG).show();
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
        textView.setText("Submitting");
        alertDialog = new android.app.AlertDialog.Builder(this)
                .setCancelable(false)
                .setView(view)
                .create();

        alertDialog.show();

    }




    private void remove(){

        alertDialog.dismiss();
    }


}
