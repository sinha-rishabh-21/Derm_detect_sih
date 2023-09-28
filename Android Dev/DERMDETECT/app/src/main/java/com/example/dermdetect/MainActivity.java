package com.example.dermdetect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    EditText Name;
    EditText Age;
    Button save;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Name = findViewById(R.id.editTextText);
        Age = findViewById(R.id.editTextPhone);
        save = findViewById(R.id.button4);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        save.setOnClickListener(view -> {
            String name = Name.getText().toString().trim(); // Trim to remove leading/trailing spaces
            String age = Age.getText().toString().trim();

            if (name.isEmpty()) {
                Name.setError("Name is required");
            } else if (age.isEmpty()) {
                Age.setError("Age is required");
            } else {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("Name", name);
                hashMap.put("Age", age);

                databaseReference.child("User")
                        .child(name)
                        .setValue(hashMap)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(MainActivity.this, "UPLOADED", Toast.LENGTH_SHORT).show();
                            // Clear the input fields after successful upload
                            Name.setText("");
                            Age.setText("");
                        });
            }
        });
    }

    public void redirect(View view) {
        Toast.makeText(this, "REDIRECTING PLEASE WAIT.....", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }
}
