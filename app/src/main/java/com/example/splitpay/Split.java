package com.example.splitpay;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Split extends AppCompatActivity {

    Button Add_Button, Save_Button, Reset_Button, Add1_Button, Okay_Button,Dash_Button;
    EditText personin, amountin,tablein;
    String person;
    ArrayList<String> list_people;
    Integer count=0, p=0;

    Float amount,split_amount;
    LinearLayout containerLayout;

    String selected_people;
    List<String> userNames;
    List<Float> amounts;

    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.split);
        list_people = new ArrayList<String>();

        Intent intent = getIntent();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8000/") // Replace with your FastAPI backend URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
        Call<List<User>> call = apiService.getUsers();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.isSuccessful()) {
                    List<User> users = response.body();
                    userNames = new ArrayList<>();
                    for (User user : users) {
                        userNames.add(user.getName());
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                Toast.makeText(Split.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });


        Dash_Button = (Button) findViewById(R.id.dash_button);
        Dash_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                openDash();
            }
        });







        Add_Button = (Button) findViewById(R.id.add_button);
        Add_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                amountin = (EditText) findViewById(R.id.editTextamount);
                amount = Float.parseFloat(amountin.getText().toString());
                openAdd_People();
            }
        });

        Save_Button = (Button) findViewById(R.id.save_button);
        Save_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openSave();
            }
        });


        Reset_Button = (Button) findViewById(R.id.reset_button);
        Reset_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReset();
            }
        });





        containerLayout = findViewById(R.id.linear_layout);

        // Loop to create and add TextView elements dynamically

    }


    private void openAdd_People() {

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_dialog);
        dialog.setCancelable(false);
        dialog.show();

        selected_people="";
        TextView list_text = dialog.findViewById(R.id.list_text);
        personin = (EditText) dialog.findViewById(R.id.editTextperson);
        Add1_Button = (Button) dialog.findViewById(R.id.add1_button);
        Add1_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                person = personin.getText().toString();
                if (userNames.contains(person)) {
                    if (list_people.contains(person)) {
                        Toast.makeText(Split.this, "Already added", Toast.LENGTH_SHORT).show();
                    } else {
                        list_people.add(person);
                        count += 1;
                        p += 1;
                        selected_people += "   " + person;
                        list_text.setText("List of people: " + selected_people);
                    }

                } else {
                    Toast.makeText(Split.this, "Person not in contact", Toast.LENGTH_SHORT).show();
                }
            }


        });
        Okay_Button = (Button) dialog.findViewById(R.id.okay_button);
        Okay_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                split_amount=amount/count;
                amounts = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    // Create a new TextView
                    TextView textView = new TextView(Split.this);
                    textView.setText(list_people.get(i) + "           " + split_amount);
                    textView.setTextColor(ContextCompat.getColor(Split.this, R.color.white));
                    textView.setTextSize(20f);
                    // Add the TextView to the container layout
                    containerLayout.addView(textView);
                    amounts.add(split_amount);
                }
                p=0;
                dialog.dismiss();
            }

        });





    }

    private void openSave() {

        // Assuming you have obtained the table name from an EditText or any other source
        tablein = (EditText) findViewById(R.id.editTextText3);
        String tableName = tablein.getText().toString();

// Create a list to hold PaymentData objects
        List<SplitData> splitDataList = new ArrayList<>();

// Iterate through the user names and amounts, and create PaymentData objects
        for (int i = 0; i < list_people.size(); i++) {
            String user_name = list_people.get(i);
            float amount_to_pay = split_amount;
            SplitData splitData = new SplitData(user_name, amount_to_pay);
            splitDataList.add(splitData);
        }

// Make the API call to store the payment data
        Call<Void> call = apiService.storeSplit(tableName, splitDataList);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {


                    Toast.makeText(Split.this, "Split data stored successfully", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(Split.this, "Failed to store split data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle network or other errors

                Toast.makeText(Split.this, "Failed to store payment data"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void openReset() {

        tablein.setText("");
        personin.setText("");
        amountin.setText("");
        count=0;
        list_people.clear();
        containerLayout.removeAllViews();
        Toast.makeText(Split.this, "Reset Successful", Toast.LENGTH_SHORT).show();

    }


    private void openDash() {

        tablein.setText("");
        personin.setText("");
        amountin.setText("");
        count=0;
        list_people.clear();
        containerLayout.removeAllViews();
        Intent intent11 = new Intent(Split.this, Dashboard.class);
        startActivity(intent11);


    }


    }
