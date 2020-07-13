package com.priyanshnama.herbarium;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    static final int GOOGLE_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton google_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null)  open_home();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        progressBar = findViewById(R.id.progressBar);
        google_button = findViewById(R.id.google_sign_in);
        progressBar.setVisibility(View.INVISIBLE);
        google_button.setVisibility(View.VISIBLE);

        google_button.setOnClickListener(v -> GoogleSignIn());
    }

    private void GoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        progressBar.setVisibility(View.VISIBLE);
        google_button.setVisibility(View.INVISIBLE);
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), GOOGLE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this, new_task -> {
                            if (new_task.isSuccessful()) {
                                progressBar.setVisibility(View.INVISIBLE);
                                open_home();
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (ApiException ignored){}
        }
    }

    private void open_home() {
        startActivity(new Intent(MainActivity.this,HomeActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        progressBar.setVisibility(View.INVISIBLE);
        google_button.setVisibility(View.VISIBLE);
    }
}
