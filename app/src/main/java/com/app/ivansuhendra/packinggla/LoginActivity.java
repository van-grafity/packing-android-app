package com.app.ivansuhendra.packinggla;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.app.ivansuhendra.packinggla.databinding.ActivityLoginBinding;
import com.app.ivansuhendra.packinggla.model.APIResponse;
import com.app.ivansuhendra.packinggla.utils.GlobalVars;
import com.app.ivansuhendra.packinggla.viewmodel.TransferViewModel;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private TransferViewModel transferViewModel;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());


        transferViewModel = new ViewModelProvider(this).get(TransferViewModel.class);

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = GlobalVars.pgDialog(LoginActivity.this);

                // Get the entered email and password from the EditText fields
                String email = binding.etEmail.getText().toString();
                String password = binding.etPassword.getText().toString();

                // Call the loginLiveData method from the ViewModel
                LiveData<APIResponse> loginLiveData = transferViewModel.loginLiveData(email, password);
                progressDialog.show();
                // Observe the LiveData for changes
                loginLiveData.observe(LoginActivity.this, new Observer<APIResponse>() {
                    @Override
                    public void onChanged(APIResponse apiResponse) {
                        // Check if the APIResponse indicates a successful login
                        if (apiResponse != null && apiResponse.getStatus().equals("success")) {
                            // Navigate to MainActivity or perform any other success action
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // Optional, to finish the current activity
                        } else {
                            // Handle unsuccessful login, show an error message, etc.
                            Toast.makeText(LoginActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.cancel();
                        }
                    }
                });
            }
        });
    }
}