package com.example.dermdetect;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity3 extends AppCompatActivity {
    TextView Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Text = findViewById(R.id.textView5);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://jsonplaceholder.typicode.com/posts/1", null, response -> {
            try {
                // Assuming the API response is a JSON object with a "title" field
                String textFromApi = response.getString("title");

                // Set the text to the TextView
                Text.setText("HEY"+textFromApi);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity3.this, "Error parsing API response", Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(MainActivity3.this, "FAILURE", Toast.LENGTH_SHORT).show());

        // Add the JsonObjectRequest to the requestQueue to make the API request
        requestQueue.add(jsonObjectRequest);
    }
}
