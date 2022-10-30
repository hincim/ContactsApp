package com.example.contactsapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.contactsapp.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private ActivityMainBinding t;
    private PersonsAdapter adapter;
    private Database db;
    private ArrayList<Persons> personsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        t = DataBindingUtil.setContentView(this, R.layout.activity_main);

        t.toolbar.setTitle("Contacts App");
        setSupportActionBar(t.toolbar);

        t.rv.setHasFixedSize(true);
        t.rv.setLayoutManager(new LinearLayoutManager(this));

        db = new Database(this);
        personsArrayList = new Personsdao().allPersons(db);

        adapter = new PersonsAdapter(this, personsArrayList, db);

        t.rv.setAdapter(adapter);
        t.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertShow();
            }
        });

    }

        public void alertShow(){
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View view = layoutInflater.inflate(R.layout.alert_design,null);

            EditText editTextPersonName = view.findViewById(R.id.editTextPersonName);
            EditText editTextPersonPhone = view.findViewById(R.id.editTextPersonPhone);

            AlertDialog.Builder name = new AlertDialog.Builder(this);

            name.setTitle("Person Add");
            name.setView(view);
            name.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    String person_name = editTextPersonName.getText().toString().trim();
                    String person_phone = editTextPersonPhone.getText().toString().trim();

                    new Personsdao().personAdd(db,person_name,person_phone);

                    personsArrayList = new Personsdao().allPersons(db);
                    adapter = new PersonsAdapter(MainActivity.this,personsArrayList,db);
                    t.rv.setAdapter(adapter);
                }
            });
            name.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            name.create().show();
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        personsArrayList = new Personsdao().personSearch(db,newText);
        adapter = new PersonsAdapter(this,personsArrayList,db);
        t.rv.setAdapter(adapter);
        return false;
    }
}