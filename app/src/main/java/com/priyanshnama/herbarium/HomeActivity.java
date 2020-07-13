package com.priyanshnama.herbarium;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.io.IOException;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private static final int GET_FROM_GALLERY = 3, CAMERA_REQUEST = 1888, MY_CAMERA_PERMISSION_CODE = 100;
    private ImageView camera, gallery, photo;
    private TextView text_camera, text_gallery;
    private Button upload;
    private Bitmap bitmap;
    private FirebaseVisionImage firebaseVisionImage;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        gallery = findViewById(R.id.gallery);
        photo = findViewById(R.id.photo);
        camera = findViewById(R.id.camera);
        upload = findViewById(R.id.upload);
        text_camera = findViewById(R.id.text_camera);
        text_gallery = findViewById(R.id.text_gallery);

        photonotselected();

        gallery.setOnClickListener(v -> opengallery());
        camera.setOnClickListener(v -> opencamera());
        upload.setOnClickListener(v -> upload());
    }

    private void photonotselected() {
        photo.setVisibility(View.INVISIBLE);
        upload.setVisibility(View.INVISIBLE);
        camera.setVisibility(View.VISIBLE);
        gallery.setVisibility(View.VISIBLE);
        text_gallery.setVisibility(View.VISIBLE);
        text_camera.setVisibility(View.VISIBLE);
    }

    private void upload() {
        //firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
        //FirebaseVisionImageLabeler firebaseVisionImageLabeler = FirebaseVision.getInstance().getCloudImageLabeler();
        //firebaseVisionImageLabeler.processImage(firebaseVisionImage).addOnSuccessListener(v -> showPredictions());
        showPredictions();
    }

    private void showPredictions() {
        startActivity(new Intent(HomeActivity.this,ResultActivity.class));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void opencamera() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        }
        else
        {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void opengallery() {
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                photoselected();
                photo.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            photo.setImageBitmap(bitmap);
            photoselected();
        }
    }

    private void photoselected() {
        camera.setVisibility(View.INVISIBLE);
        gallery.setVisibility(View.INVISIBLE);
        photo.setVisibility(View.VISIBLE);
        upload.setVisibility(View.VISIBLE);
        text_gallery.setVisibility(View.INVISIBLE);
        text_camera.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        int visi = upload.getVisibility();
        if(visi==0)
            photonotselected();
        else
            finish();
    }
}