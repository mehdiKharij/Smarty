package com.example.smarty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Supprimer le titre par défaut pour éviter les titres en double
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Centrer le titre
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ImageButton menuButton = toolbar.findViewById(R.id.menu_button);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        ImageButton notificationButton = toolbar.findViewById(R.id.notification_button);
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotificationsFragment.class);
                startActivity(intent);
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_Dashboard);
        }

        // Initialiser Firebase et créer le canal de notification
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        createNotificationChannel();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_Dashboard) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        } else if (id == R.id.nav_Devices) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DevicesFragment()).commit();
        } else if (id == R.id.nav_share) {
            shareApp(); // Appel de la méthode pour partager l'application
        } else if (id == R.id.nav_Profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
        } else if (id == R.id.nav_contacts) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ContactsFragment()).commit();
        } else if (id == R.id.nav_notif) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotificationsFragment()).commit();
        } else if (id == R.id.nav_logout) {
            // Déconnexion de l'utilisateur
            logoutUser();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void shareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey, check out this cool app: https://play.google.com/store/apps/details?id=" + getPackageName());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share via"));
    }

    private void logoutUser() {
        // Code pour déconnecter l'utilisateur, comme effacer les données de session

        // Redirection vers l'activité de connexion
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Effacer les activités précédentes
        startActivity(intent);
        finish(); // Terminer cette activité
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = "Default Channel";
            String channelDescription = "Channel for default notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
