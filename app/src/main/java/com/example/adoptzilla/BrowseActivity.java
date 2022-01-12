package com.example.adoptzilla;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Entity.Upload;

public class BrowseActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private List<Upload> mUploads;
    FirebaseFirestore db;
    FirebaseAuth auth;
    private ImageView imageViewPet;
    Spinner petSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        Button back = findViewById(R.id.browseBackMain);
        back.setOnClickListener(v -> backToMain());
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }
        petSort = findViewById(R.id.spinner_sort);

        imageViewPet = findViewById(R.id.imageViewPet);
        mRecyclerView = findViewById(R.id.recycler_view_browse);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        mUploads = new ArrayList<>();
        mAdapter = new ImageAdapter(BrowseActivity.this,mUploads);
        mRecyclerView.setAdapter(mAdapter);
        EventChangeListener();

        sortPets();

    }
    private void EventChangeListener() {
        db.collection("uploads")
                .addSnapshotListener((value, error) -> {
                    if(error != null){
                        Log.e("Firestore error",error.getMessage());
                        return;
                    }
                    assert value != null;
                    for(DocumentChange dc : value.getDocumentChanges()){
                        if(dc.getType() == DocumentChange.Type.ADDED){
                            mUploads.add(dc.getDocument().toObject(Upload.class));
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void sortPets(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_age, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        petSort.setAdapter(adapter);
        petSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                if(position==1){
                    sortByAge();
                }
              else
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortByAge(){
        Collections.sort(mUploads, Comparator.comparing(Upload::getPetAge));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void backToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}