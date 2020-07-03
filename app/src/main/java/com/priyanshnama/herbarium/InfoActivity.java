package com.priyanshnama.herbarium;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class InfoActivity extends AppCompatActivity {
    private FloatingActionButton wiki;
    private String link, name, botanical, family, medicine, parts;
    private TextView Name, Botanical, Medicine, Family, Parts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Objects.requireNonNull(getSupportActionBar()).hide();
        name = Objects.requireNonNull(getIntent().getExtras()).getString("Name");

        wiki = findViewById(R.id.wiki);
        Name = findViewById(R.id.name);
        Medicine = findViewById(R.id.medicine);
        Family = findViewById(R.id.family);
        Parts = findViewById(R.id.parts);
        Botanical = findViewById(R.id.botanical);

        Log.d("D","name is "+ name);
        get_all_data();

        wiki.setOnClickListener(v -> open_wiki(link));
    }

    private void get_all_data() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Leaves")
                .document(name)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot data = task.getResult();
                        assert data != null;
                        link = (String) Objects.requireNonNull(data.getData()).get("Wiki Link");
                        botanical = (String) Objects.requireNonNull(data.getData()).get("Botanical Name");
                        family = (String) Objects.requireNonNull(data.getData()).get("Family");
                        medicine = (String) Objects.requireNonNull(data.getData()).get("Medical Use");
                        parts = (String) Objects.requireNonNull(data.getData()).get("Parts Used");

                        Name.setText(Name.getText() + "\n" + name);
                        Family.setText(Family.getText() + "\n" + family);
                        Medicine.setText(Medicine.getText() + "\n" + medicine);
                        Botanical.setText(Botanical.getText() + "\n" + botanical);
                        Parts.setText(Parts.getText() + "\n" + parts);
                        Log.d("URI","Url is "+ link);
                    }
                });
    }

    private void open_wiki(String link) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browserIntent);
    }
}