package com.example.k224111488_dokhanhlinh_practice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    // Declare ListView and Adapter
    ListView lvContact;
    ArrayAdapter<String> contactAdapter;
    String TAG = "FIREBASE";  // Tag for logging

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge support for the activity
        setContentView(R.layout.activity_main);

        // Set up window insets listener for padding based on system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize ListView and Adapter
        addViews();

        // Set up the Insert button to navigate to InsertContactActivity
        Button btnInsert = findViewById(R.id.btnInsert);
        btnInsert.setOnClickListener(v -> openInsertActivity());  // Open InsertContactActivity when clicked

        // Set up the event listener for item clicks on the ListView
        addEvents();
    }

    // Initialize views and ListView
    private void addViews() {
        lvContact = findViewById(R.id.lvContact);
        contactAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1);
        lvContact.setAdapter(contactAdapter);
        loadData();  // Load data from Firebase
    }

    // Load data from Firebase Realtime Database
    private void loadData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("contacts");

        // Add a listener to retrieve data from the Firebase database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contactAdapter.clear();  // Clear any existing data
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String key = data.getKey();  // Contact's key (ID)
                    String value = data.getValue().toString();  // Contact's value (data)
                    contactAdapter.add(key + "\n" + value);  // Add new data to the adapter
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Log any errors that occur during database query
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    // Open InsertContactActivity to insert a new contact
    private void openInsertActivity() {
        Intent intent = new Intent(MainActivity.this, InsertContactActivity.class);
        startActivity(intent);  // Start the InsertContactActivity
    }

    // Set up event listener for ListView item clicks
    private void addEvents() {
        lvContact.setOnItemClickListener((parent, view, position, id) -> {
            // Get the contact data based on the position clicked
            String data = contactAdapter.getItem(position);
            String key = data.split("\n")[0];  // The key is the first part of the string (before the newline)

            // Start DetailContactActivity and pass the contact's key as an extra in the intent
            Intent intent = new Intent(MainActivity.this, DetailContactActivity.class);
            intent.putExtra("KEY", key);  // Pass the key as an extra in the intent
            startActivity(intent);  // Start the activity to show the details of the selected contact
        });
    }
}
