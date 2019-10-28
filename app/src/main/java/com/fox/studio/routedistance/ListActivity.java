package com.fox.studio.routedistance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import io.realm.Realm;
import io.realm.RealmResults;


public class ListActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        RealmResults<RouteModel> results = Realm.getDefaultInstance().where(RouteModel.class).sort("date").findAll();
        Log.d("ListActivity", results.size() + " ");

        MyListAdapter adapter = new MyListAdapter(results);
        ListView listView =  findViewById(R.id.list);

        listView.setAdapter(adapter);
    }
}
