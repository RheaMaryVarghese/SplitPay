package com.example.splitpay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button loginbutton, signupbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginbutton = (Button) findViewById(R.id.login_button);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();

            }

        });

        signupbutton = (Button) findViewById(R.id.signup_button);
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUp();

            }
        });
    }


        public void openLogin() {
            Intent intent1 = new Intent(this, Login.class);
            startActivity(intent1);
        }

        public void openSignUp() {
        Intent intent2 = new Intent(this, SignUp.class);
        startActivity(intent2);
    }

}