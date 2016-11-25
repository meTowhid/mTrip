package towhid.icurious.mtrip.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import towhid.icurious.mtrip.home.MainActivity;
import towhid.icurious.mtrip.R;

public class SplashScreen extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
                if (auth != null)
                    i = new Intent(SplashScreen.this, MainActivity.class);
                else
                    i = new Intent(SplashScreen.this, LoginActivity.class);

                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}