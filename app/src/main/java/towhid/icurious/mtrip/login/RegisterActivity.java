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

import java.util.HashMap;
import java.util.Map;

import towhid.icurious.mtrip.R;
import towhid.icurious.mtrip.databinding.ActivityRegisterBinding;
import towhid.icurious.mtrip.home.MainActivity;
import towhid.icurious.mtrip.utils.mFireData;
import towhid.icurious.mtrip.utils.mHelper;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private String name, email, password;
    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
    }

    public void registerClicked(View view) {
        attemptRegister();
    }

    private void attemptRegister() {

        // Reset errors.
        binding.email.setError(null);
        binding.password.setError(null);

        // Store values at the time of the login attempt.
        name = binding.name.getText().toString().trim();
        email = binding.email.getText().toString().trim();
        password = binding.password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(name)) {
            binding.password.setError("Name can't be empty!");
            focusView = binding.name;
            cancel = true;
        }

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

                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, "User exists!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Map<String, String> map = new HashMap<String, String>();
                                            map.put("name", name);
                                            map.put("email", email);
                                            mFireData.getDatabase().getReference().getRoot()
                                                    .child("users/" + mAuth.getCurrentUser().getUid()).setValue(map);
                                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                            finishAffinity();
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

}
