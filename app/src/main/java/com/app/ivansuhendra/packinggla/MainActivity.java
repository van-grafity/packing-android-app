package com.app.ivansuhendra.packinggla;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import com.app.ivansuhendra.packinggla.model.User;
import com.app.ivansuhendra.packinggla.net.API;
import com.app.ivansuhendra.packinggla.ui.activity.LoginActivity;
import com.app.ivansuhendra.packinggla.ui.activity.ScanQrActivity;
import com.app.ivansuhendra.packinggla.utils.GlobalVars;
import com.app.ivansuhendra.packinggla.viewmodel.SharedViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import br.com.kots.mob.complex.preferences.ComplexPreferences;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private NavController navController; // Global NavController variable
    private SharedViewModel sharedViewModel;
    private User mUser;

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

        binding.tvVersion.setText("VERSION : " + BuildConfig.VERSION_NAME);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(this, GlobalVars.PREF_USER, MODE_PRIVATE);
        mUser = complexPreferences.getObject(GlobalVars.PREF_USER_KEY, User.class);
        if (mUser == null || mUser.getName() == null) {
            startActivity(new Intent(getBaseContext(), LoginActivity.class));
            finish();
            return;
        }  else {
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

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    navController.navigate(R.id.nav_home);
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                } else if (id == R.id.nav_logout) {
                    showLogoutDialog();
                    return true;
                } else {
                    // Navigate to other destinations
                    NavigationUI.onNavDestinationSelected(item, navController);
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
            }
        });

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

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to log out?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Perform logout actions here
                        // For example, navigate to login screen or clear session
                        // For now, let's just close the app
                        ComplexPreferences complexPrefenreces = ComplexPreferences.getComplexPreferences(MainActivity.this, GlobalVars.PREF_USER, MODE_PRIVATE);
                        complexPrefenreces.putObject(GlobalVars.PREF_USER_KEY, new User());
                        complexPrefenreces.commit();

                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
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
            MainActivity.super.onBackPressed();
        }
    }
}