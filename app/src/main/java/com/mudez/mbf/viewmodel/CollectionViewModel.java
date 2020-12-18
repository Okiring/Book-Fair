package com.mudez.mbf.viewmodel;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.mudez.mbf.helpers.Event;
import com.mudez.mbf.models.AgendaItemObject;
import com.mudez.mbf.models.AgendaObject;
import com.mudez.mbf.repository.Repository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CollectionViewModel extends ViewModel {
    private MutableLiveData<Event<Integer>> saveResponse;
    private  MutableLiveData<Event<List<AgendaItemObject>>> itemResponse;



   public CollectionViewModel(){

    }

    private CompositeDisposable disposable = new CompositeDisposable();


   public LiveData<QuerySnapshot> getQuerySnapshotLiveData(){
       Query query = FirebaseFirestore.getInstance().collection("agendas").orderBy("date", Query.Direction.ASCENDING);
       return new FirebaseQueryLiveData(query);
   }

    public MutableLiveData<Event<List<AgendaItemObject>>> getAgendaItems(final String id){
        itemResponse = new MutableLiveData<>();


        Observable<List<AgendaItemObject>> observable = Observable.create(new ObservableOnSubscribe<List<AgendaItemObject>>() {
            @Override
            public void subscribe(ObservableEmitter<List<AgendaItemObject>> emitter) {

                Repository.getRepositoryInstance().getAgendaItemList(id,emitter);


            }
        });


        Observer<List<AgendaItemObject>> observer = new Observer<List<AgendaItemObject>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onNext(List<AgendaItemObject> s) {

                itemResponse.setValue(new Event<>(s));

            }

            @Override
            public void onError(Throwable e) {

                //failed authenticating

                itemResponse.setValue(null);


            }

            @Override
            public void onComplete() {

                // Log.d(MainActivity.TAG,"completed");

            }
        };


        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);

        return itemResponse;


    }


    public MutableLiveData<Event<Integer>> saveAgendaObject(final AgendaObject agendaObject){
        saveResponse = new MutableLiveData<>();


        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) {

               Repository.getRepositoryInstance().storeAgendaObject(agendaObject,emitter);


            }
        });


        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onNext(Integer s) {

                saveResponse.setValue(new Event<>(s));

            }

            @Override
            public void onError(Throwable e) {

                //failed authenticating

                saveResponse.setValue(new Event<>(1));


            }

            @Override
            public void onComplete() {

                // Log.d(MainActivity.TAG,"completed");

            }
        };


        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);

        return saveResponse;


    }
    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
        disposable.dispose();


    }

}
