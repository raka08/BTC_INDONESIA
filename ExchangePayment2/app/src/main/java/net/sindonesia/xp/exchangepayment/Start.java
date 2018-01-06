package net.sindonesia.xp.exchangepayment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Start extends AppCompatActivity {
    Button login;
    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Log.d("hoho", String.valueOf(preferences.getBoolean("Login", false)));
        if (preferences.getBoolean("Login", false) == false){


        setContentView(R.layout.activity_start);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        login = (Button)findViewById(R.id.Login);
        register = (Button)findViewById(R.id.Daftar);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Start.this, Login.class);
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Start.this, Utama.class);
                startActivity(intent);
            }
        });
    }else{
            Intent intent = new Intent (Start.this, Utama.class);
            startActivity(intent);
            finish();
        }
}
}
