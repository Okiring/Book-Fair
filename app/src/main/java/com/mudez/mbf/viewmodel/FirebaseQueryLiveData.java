package com.mudez.mbf.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class FirebaseQueryLiveData extends LiveData<QuerySnapshot> {
   private CustomEventListener customEventListener = new CustomEventListener();
   private Query query;
   private ListenerRegistration listenerRegistration;

    FirebaseQueryLiveData(Query query){
       this.query = query;
    }

    @Override
    protected void onActive() {
        listenerRegistration = query.addSnapshotListener(customEventListener);
        Log.d("firebase","listener has been added");
        super.onActive();
    }

    @Override
    protected void onInactive() {
        Log.d("firebase","listener has been removed");
        listenerRegistration.remove();
        super.onInactive();
    }

    public class CustomEventListener implements EventListener<QuerySnapshot> {

        @Override
        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {


           if(error != null){
               Log.d("firebase","There was an error");
           }else{
               if(value != null){
                   setValue(value);
               }
           }

        }
    }
}
