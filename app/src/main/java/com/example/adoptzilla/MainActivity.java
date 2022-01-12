package com.example.adoptzilla;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Entity.Ad;

public class MainActivity extends AppCompatActivity {
TextView ceva;
Button btn;
DatabaseReference reff;




    ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button newAd = findViewById(R.id.new_ad);
        newAd.setOnClickListener(v -> newAd());

        Button seeads = findViewById(R.id.see_ads);
        seeads.setOnClickListener(v -> seeAds());

    }

    public void logout(View view)
    {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    private void newAd() {
        Intent intent = new Intent(this, NewAd.class);
        startActivity(intent);
        finish();
    }

    public void seeAds()
    {
        Intent intent = new Intent(this, BrowseActivity.class);
        startActivity(intent);
        finish();
    }
}