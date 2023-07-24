package com.example.splitpay;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUp extends AppCompatActivity {


    private EditText Namein,Agein,Genderin,Email_idin,Phone_noin,Usernamein,Passwordin;
    private ImageView Pic_urlin;

    private Button signUpButton;


    private ApiService apiService;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        Intent intent = getIntent();


        Namein = (EditText) findViewById(R.id.editTextname);
        Agein = (EditText) findViewById(R.id.editTextAge);
        Genderin = (EditText) findViewById(R.id.editTextGender);
        Email_idin = (EditText) findViewById(R.id.editTextTextEmailAddress);
        Phone_noin = (EditText) findViewById(R.id.editTextPhone);
        Pic_urlin = (ImageView) findViewById(R.id.imageView);
        Usernamein = (EditText) findViewById(R.id.editTextUsername2);
        Passwordin = (EditText) findViewById(R.id.editTextTextPassword2);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        signUpButton = (Button) findViewById(R.id.signup2_button);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Name = Namein.getText().toString();
                Integer Age = Integer.parseInt(Agein.getText().toString());
                String Gender = Genderin.getText().toString();
                String Email_id = Email_idin.getText().toString();
                String Phone_no = Phone_noin.getText().toString();
                String Pic_url = "url";
                String Username = Usernamein.getText().toString();
                String Password = Passwordin.getText().toString();


                User user = new User(Name, Age, Gender, Email_id, Phone_no, Pic_url, Username, Password);


                signUpUser(user);
            }
        });
    }


        private void signUpUser(User user) {
            // Call the sign-up API endpoint
            Call<User> call = apiService.signUp(user);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {

                        // Handle successful sign-up
                        Toast.makeText(SignUp.this, "Sign-up successful!", Toast.LENGTH_SHORT).show();

                        Intent intent8 = new Intent(SignUp.this, Dashboard.class);
                        intent8.putExtra("user", user);
                        startActivity(intent8);
                    } else {
                        if (response.errorBody() != null) {
                            try {
                                String errorResponse = response.errorBody().string();
                                Log.e("SignUp", "Error response: " + errorResponse);




                                // Display errorResponse using a Toast or other logging mechanism
                                Toast.makeText(SignUp.this, "Sign-up failed: " + errorResponse, Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(SignUp.this, "Sign-up failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(SignUp.this, "Sign-up failed.", Toast.LENGTH_SHORT).show();


                // Handle sign-up failure
            }
        });

    }

}


