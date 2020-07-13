package com.priyanshnama.herbarium;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Objects;

public class InfoActivity extends AppCompatActivity {
    private String link, name;
    private TextView Name, Botanical, Medicine, Family, Parts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Objects.requireNonNull(getSupportActionBar()).hide();
        name = Objects.requireNonNull(getIntent().getExtras()).getString("Name");

        FloatingActionButton wiki = findViewById(R.id.wiki);
        Name = findViewById(R.id.name);
        Medicine = findViewById(R.id.medicine);
        Family = findViewById(R.id.family);
        Parts = findViewById(R.id.parts);
        Botanical = findViewById(R.id.botanical);

        get_all_data();
        wiki.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link))));
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
                        Name.setText(String.format("%s\n%s", getString(R.string.name), name));
                        Family.setText(String.format("%s\n%s", getString(R.string.family), Objects.requireNonNull(data.getData()).get("Family")));
                        Medicine.setText(String.format("%s\n%s", getString(R.string.medicine), Objects.requireNonNull(data.getData()).get("Medical Use")));
                        Botanical.setText(String.format("%s\n%s", getString(R.string.botanical_name), Objects.requireNonNull(data.getData()).get("Botanical Name")));
                        Parts.setText(String.format("%s\n%s", getString(R.string.parts_used), Objects.requireNonNull(data.getData()).get("Parts Used")));
                    }
                });
    }
}