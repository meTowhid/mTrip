package towhid.icurious.mtrip.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import towhid.icurious.mtrip.R;
import towhid.icurious.mtrip.databinding.ActivityLoginBinding;
import towhid.icurious.mtrip.home.MainActivity;
import towhid.icurious.mtrip.utils.mHelper;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        // Set up the login form.

    }


    private void attemptLogin() {

        // Reset errors.
        binding.email.setError(null);
        binding.password.setError(null);

        // Store values at the time of the login attempt.
        final String email = binding.email.getText().toString();
        final String password = binding.password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            binding.password.setError(getString(R.string.error_invalid_password));
            focusView = binding.password;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            binding.email.setError(getString(R.string.error_field_required));
            focusView = binding.email;
            cancel = true;
        } else if (!isEmailValid(email)) {
            binding.email.setError(getString(R.string.error_invalid_email));
            focusView = binding.email;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            binding.email.setError(getString(R.string.error_field_required));
            focusView = binding.email;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            if (mHelper.isOnline(this)) {
                mProgress = ProgressDialog.show(this, "", "Verifying information", true);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // do the thing that takes a long time

                        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
//                                            Log.d("LoginError", "onComplete: "+task.getException().toString());
                                            if (task.getException().toString().toLowerCase().contains("password"))
                                                Toast.makeText(LoginActivity.this, "Wrong password", Toast.LENGTH_LONG).show();
                                            else
                                                Toast.makeText(LoginActivity.this, "User doesn't exists!", Toast.LENGTH_LONG).show();

                                        } else {
                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            finish();
                                        }
                                        mProgress.dismiss();
                                    }
                                });
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                            progress.dismiss();
                            }
                        });
                    }
                }).start();

            }
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    public void loginClicked(View view) {
        attemptLogin();
    }

    public void gotoRegister(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }
}

