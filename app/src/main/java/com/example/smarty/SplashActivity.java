package com.example.smarty;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    // Durée d'attente en millisecondes
    private static final int SPLASH_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Utilisation d'un Handler pour retarder le lancement de LoginActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Création d'une intention pour démarrer LoginActivity
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                // Fermeture de cette activité pour éviter qu'elle ne soit affichée lors du retour en arrière
                finish();
            }
        }, SPLASH_DELAY);
    }
}