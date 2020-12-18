package com.mudez.mbf.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.mudez.mbf.models.AgendaItemObject;
import com.mudez.mbf.models.AgendaObject;
import com.mudez.mbf.models.UserObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.ObservableEmitter;

public class Repository {


    private static Repository repository;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();



    public static Repository getRepositoryInstance() {

        if (repository == null) {

            repository = new Repository();

        }

        return repository;

    }


    //stores agenda object to database
    public void storeAgendaObject(final AgendaObject agendaObject, final ObservableEmitter<Integer> emitter){


        Map<String,Object> date = new HashMap<>();
        date.put("date",agendaObject.getDate());
        firebaseFirestore.collection("agendas").add(date).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    if(task.getResult() != null){
                        String id = task.getResult().getId();
                        WriteBatch writeBatch = firebaseFirestore.batch();

                        for(AgendaItemObject agendaItemObject : agendaObject.getItems()){

                  writeBatch.set(firebaseFirestore.collection("agendas").document(id).collection("items").document(),agendaItemObject);
                        }

                        writeBatch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){

                               emitter.onNext(0);
                               emitter.onComplete();

                                }else{
                                    emitter.onError(new Throwable("failed saving"));
                                }

                            }
                        });

                    }

                }else{
                    emitter.onError(new Throwable("failed saving"));
                }
            }
        });

    }

    //retrieves agenda items
    public void getAgendaItemList(final String id, final ObservableEmitter<List<AgendaItemObject>> emitter){


        firebaseFirestore.collection("agendas").document(id).collection("items").orderBy("createdAt", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    List<AgendaItemObject> agendaItemObjectList = new ArrayList<>();
                    for(DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult()).getDocuments()){
                        AgendaItemObject agendaItemObject = documentSnapshot.toObject(AgendaItemObject.class);
                        agendaItemObject.setId(documentSnapshot.getId());
                        agendaItemObjectList.add(agendaItemObject);
                    }

                    emitter.onNext(agendaItemObjectList);
                    emitter.onComplete();

                }else{
                    emitter.onError(new Throwable("Failed retrieving"));
                }

            }
        });

    }



}
