package com.app.ivansuhendra.packinggla;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.app.ivansuhendra.packinggla.databinding.ActivityMainBinding;
import com.app.ivansuhendra.packinggla.net.API;
import com.app.ivansuhendra.packinggla.viewmodel.SharedViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private NavController navController; // Global NavController variable
    private SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        binding.appBarMain.fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ScanQrActivity.class));
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle item selection manually
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        // Navigate to the home destination
                        navController.navigate(R.id.nav_home);
                        break;
                    case R.id.nav_transfer:
                        // Navigate to the transfer destination
                        navController.navigate(R.id.nav_transfer);
                        break;
                    case R.id.nav_receive:
                        // Navigate to the receive destination
                        navController.navigate(R.id.nav_receive);
                        break;
                    case R.id.nav_load:
                        // Navigate to the load destination
                        navController.navigate(R.id.nav_load);
                        break;
                    case R.id.nav_logout:
                        // Navigate to the logout destination
                        navController.navigate(R.id.nav_logout);
                        break;
                }

                // Close the drawer
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        if (API.currentUser(MainActivity.this) == null) {
            startActivity(new Intent(getBaseContext(), LoginActivity.class));
            finish();
            return;
        } else {
            View headerView = navigationView.getHeaderView(0);
            TextView tvName = headerView.findViewById(R.id.tvName);
            TextView tvEmail = headerView.findViewById(R.id.tvEmail);
            tvName.setText(API.currentUser(MainActivity.this).getName());
            tvEmail.setText(API.currentUser(MainActivity.this).getEmail());
        }

        // Initialize NavController globally
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top-level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_transfer, R.id.nav_receive, R.id.nav_load, R.id.nav_logout)
                .setOpenableLayout(drawer)
                .build();

        // Setup ActionBar and NavigationView with NavController
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        // Observe the flag to navigate to the transfer destination
        sharedViewModel.getNavigateToTransfer().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean navigate) {
                if (navigate) {
                    // Get the NavController and navigate to the transfer destination
                    NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
                    navController.navigate(R.id.nav_transfer);

                    // Reset the flag
                    sharedViewModel.setNavigateToTransfer(false);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = binding.drawerLayout;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("Apakah Anda yakin ingin keluar?")
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("Tidak", null)
                    .show();
        }
    }
}