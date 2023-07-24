package com.example.splitpay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Home  extends AppCompatActivity {

    Button dashbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);



        dashbutton = (Button) findViewById(R.id.back_button);
        dashbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDash();

            }

        });

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("user")) {
            User user = (User) intent.getSerializableExtra("user");
            if (user != null) {
                displayUserDetails(user);
            }
        }
    }

    public void openDash() {
        Intent intent6 = new Intent(this, Dashboard.class);
        startActivity(intent6);
    }


            private void displayUserDetails(User user){
                // Update the TextViews or UI elements with user details
                TextView name1 = findViewById(R.id.name1);
                TextView age1 = findViewById(R.id.age1);
                TextView gender1 = findViewById(R.id.gender1);
                TextView emailid1 = findViewById(R.id.emailid1);
                TextView Phno1 = findViewById(R.id.Phno1);

                // Set the user details to the TextViews
                name1.setText("Name: " + user.getName());
                age1.setText("Age: " + user.getAge());
                gender1.setText("Gender: " + user.getGender());
                emailid1.setText("Email id: " + user.getEmail_id());
                Phno1.setText("Phone no: " + user.getPhone_no());

                // ... set other TextViews with other user details
            }

        }

