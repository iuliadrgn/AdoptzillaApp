package com.example.adoptzilla;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

public class NewAd extends AppCompatActivity {
    EditText petName, petAge, petDescription;
    Spinner petType;
    String choice;
    Member member;
    Button done;
    Button addPicture;
    private ImageView image;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ad);

        petName = findViewById(R.id.newAdPetName);
        petAge = findViewById(R.id.newAdPetAge);
        petDescription = findViewById(R.id.newAdPetDescription);
        petType = findViewById(R.id.petTypeChoiceBox);
        image = findViewById(R.id.petImg);

        List<String> categories = new ArrayList<>();

        categories.add(0, getString(R.string.PetType));
        categories.add("Cat");
        categories.add("Dog");
        categories.add("Fish");
        categories.add("Bird");
        categories.add("Hedgehog");

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        petType.setAdapter(adapter);



        Button backButton = findViewById(R.id.newAdBackButton);
        backButton.setOnClickListener(v -> backToMainPage());


    }

    private void saveEntry(String choice) {

        final String nm = petName.getText().toString().trim();
        final String ag = petAge.getText().toString().trim();
        final String dc = petDescription.getText().toString().trim();

        if (TextUtils.isEmpty(nm)) {
            petName.setError("name required\n");
            Toast.makeText(this, "Please select the pet's name\n", Toast.LENGTH_SHORT).show();
            return;
        }

        if (choice != null) {
            //trebuie adaugata
        } else {
            Toast.makeText(this, "Please select a pet type\n", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(ag)) {
            petAge.setError("age required\n");
            Toast.makeText(this, "Please select the pet's age\n", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(dc)) {
            petDescription.setError("description required\n");
            Toast.makeText(this, "Please provide a description\n", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void backToMainPage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}