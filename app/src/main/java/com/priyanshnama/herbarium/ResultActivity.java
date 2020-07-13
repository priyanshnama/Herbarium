package com.priyanshnama.herbarium;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Objects;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Best Predictions");

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setVisibility(View.INVISIBLE);

        TextView sample = findViewById(R.id.sample);
        sample.setText(String.format("%s\n Probability : 98.5%%", "sample"));
        sample.setOnClickListener(v -> show());
    }

    private void show() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Leaves")
                .document("Sample")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(ResultActivity.this, InfoActivity.class).putExtra("Name", Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getData()).get("Name")).toString()));
                    }
                });
    }
}