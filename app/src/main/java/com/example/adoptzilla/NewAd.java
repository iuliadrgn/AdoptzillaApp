package com.example.adoptzilla;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import Entity.Member;
import Entity.Upload;

public class NewAd extends AppCompatActivity {
    EditText petName, petAge, petDescription, petAddress;
    Spinner petType;
    Member member;
    Button done;
    Button addPicture;
    private ImageView image;
    private StorageTask storageTask;
    private Uri imageUri;
    ActivityResultLauncher<Intent> activityResultLauncher;
    private StorageReference storageReference;
    FirebaseAuth auth;

    FirebaseFirestore fStore;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ad);

        petName = findViewById(R.id.newAdPetName);
        petAge = findViewById(R.id.newAdPetAge);
        petDescription = findViewById(R.id.newAdPetDescription);
        petType = findViewById(R.id.petTypeChoiceBox);
        petAddress = findViewById(R.id.newAdPetAddress);
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


        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }

        fStore = FirebaseFirestore.getInstance();
        member = new Member();

        Button backButton = findViewById(R.id.newAdBackButton);
        backButton.setOnClickListener(v -> backToMainPage());

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Intent data = result.getData();
                imageUri = data.getData();
                image.setImageURI(imageUri);
            }
        });

        addPicture = findViewById(R.id.addImage);
        addPicture.setOnClickListener(this::addPicture);
        done = findViewById(R.id.done);
        done.setOnClickListener(v -> {
            if (storageTask != null && storageTask.isInProgress()) {
                Toast.makeText(NewAd.this, "Upload in progress\n", Toast.LENGTH_SHORT).show();
            } else {
                saveEntry();
            }
        });
    }

    private void addPicture(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        activityResultLauncher.launch(intent);
    }


    private void saveEntry() {

        final String nm = petName.getText().toString().trim();
        final String ag = petAge.getText().toString().trim();
        final String dc = petDescription.getText().toString().trim();
        final String adr = petAddress.getText().toString().trim();

        if (TextUtils.isEmpty(nm)) {
            petName.setError("name required\n");
            Toast.makeText(this, "Please select the pet's name\n", Toast.LENGTH_SHORT).show();
            return;
        }

        if (petType.getSelectedItem().toString() == null) {
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

        if (TextUtils.isEmpty(adr)) {
            petDescription.setError("address required\n");
            Toast.makeText(this, "Please provide a address\n", Toast.LENGTH_SHORT).show();
            return;
        }

        String uploadId = databaseReference.push().getKey();

        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getImageExtension(imageUri));
            storageTask = fileReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                Toast.makeText(NewAd.this, "Ad saved successfully\n", Toast.LENGTH_SHORT).show();
                Upload upload = new Upload(petName.getText().toString().trim(),
                        petType.getSelectedItem().toString(),
                        petAge.getText().toString().trim(),
                        petDescription.getText().toString().trim(),
                        fileReference.getName(),
                        Objects.requireNonNull(auth.getCurrentUser()).getEmail(),
                        petAddress.getText().toString().trim(),
                        uploadId
                );
                DocumentReference documentReference = fStore.collection("uploads").document(uploadId);
                Map<String, String> upl = new HashMap<>();
                upl.put("petName", upload.getPetName());
                upl.put("petType", upload.getPetType());
                upl.put("petAge", upload.getPetAge());
                upl.put("petDescription", upload.getDescription());
                upl.put("imgName", upload.getImgName());
                upl.put("currentUser", upload.getCurrentUser());
                upl.put("address", upload.getAddress());
                documentReference.set(upl).addOnSuccessListener(unused -> Log.d("TAG", "onSuccess: entry is created for : " + "aminal" + "\n")).addOnFailureListener(e -> Log.d("TAG", "onFailure: " + e.toString()));
                startActivity(new Intent(getApplicationContext(), NewAd.class));
                finish();
            });
        }
    }



    private void backToMainPage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

        private String getImageExtension(Uri img) {
            ContentResolver cr = getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            return mime.getExtensionFromMimeType(cr.getType(img));
        }

    @SuppressLint("Range")
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}