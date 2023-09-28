package com.example.dermdetect;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class MainActivity2 extends AppCompatActivity {
    Button button;
    ImageView imageView;
    Button uploadButton; // Add this button
    Bitmap selectedImage; // Store the selected image
    public static final int REQUEST_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);
        button = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
        uploadButton = findViewById(R.id.button5); // Initialize the UPLOAD button

        ActivityResultLauncher<String> CAMP = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
            if (result) {
                // Permission granted, launch the camera
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_CODE);
            } else {
                Toast.makeText(MainActivity2.this, "ALLOW CAMERA permission required", Toast.LENGTH_SHORT).show();
            }
        });

        button.setOnClickListener(view -> {
            // Request camera permission
            CAMP.launch(Manifest.permission.CAMERA);
        });

        // Add click listener for the UPLOAD button
        uploadButton.setOnClickListener(view -> {
            // Check if an image is selected
            if (selectedImage != null) {
                // Convert the selected image to a JSON object
                JSONObject imageJson = convertImageToJson(selectedImage);

                // Send the JSON object as a POST request for image upload
                sendPostRequest(imageJson);
            } else {
                Toast.makeText(MainActivity2.this, "No image selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Uri photoUri = (data != null) ? data.getData() : null;
            selectedImage = (Bitmap) ((data != null) ? Objects.requireNonNull(data.getExtras()).get("data") : null);
            imageView.setImageBitmap(selectedImage);
        } else {
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sendPostRequest(JSONObject jsonObject) {
        // Define the API endpoint URL
        String postUrl = "https://jsonplaceholder.typicode.com/posts/1"; // Replace with your actual API URL

        // Create a RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Create a JSON object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                postUrl,
                jsonObject,
                response -> {
                    // Handle the response from the server
                    Toast.makeText(MainActivity2.this, "POST request successful", Toast.LENGTH_SHORT).show();
                    // You can process the server response here
                },
                error -> {
                    // Handle errors that occur during the request
                    Toast.makeText(MainActivity2.this, "POST request failed", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
        );

        // Add the request to the queue to execute it
        requestQueue.add(jsonObjectRequest);
    }

    private JSONObject convertImageToJson(Bitmap photo) {
        JSONObject imageJson = new JSONObject();
        try {
            // You can add more properties to the JSON object if needed
            imageJson.put("imageWidth", photo.getWidth());
            imageJson.put("imageHeight", photo.getHeight());

            // Convert the image data to a Base64-encoded string (optional)
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageData = baos.toByteArray();
            String base64Image = android.util.Base64.encodeToString(imageData, android.util.Base64.DEFAULT);
            imageJson.put("imageData", base64Image);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return imageJson;
    }

    public void redirecter(View view) {
        Toast.makeText(this, "REDIRECTING PLEASE WAIT.....", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity3.class);
        startActivity(intent);
    }
}
