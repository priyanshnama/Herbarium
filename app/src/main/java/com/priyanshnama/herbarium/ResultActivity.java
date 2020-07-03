package com.priyanshnama.herbarium;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ResultActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView sample;
    private String Sample_Name = "sample";
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Best Predictions");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setVisibility(View.INVISIBLE);

        sample = findViewById(R.id.sample);
        sample.setText("" + Sample_Name  + "\n Probability : 98.5%");
        sample.setOnClickListener(v -> show(Sample_Name));
    }

    private void show(String sample_name) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Leaves")
                .document("Sample")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot data = task.getResult();
                        assert data != null;
                        name = (String)Objects.requireNonNull(data.getData()).get("Name");
                        Intent intent = new Intent(ResultActivity.this, InfoActivity.class);
                        intent.putExtra("Name",name);
                        startActivity(intent);
                    }
                });
    }
}