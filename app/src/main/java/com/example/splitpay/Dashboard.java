package com.example.splitpay;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Dashboard extends AppCompatActivity {

    Button splitbutton, transactionbutton,homebutton;
    User user;
    Retrofit retrofit;
    float totalLentAmount = 0;
    float totalOweAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<TransactionModel>> call = apiService.getTransactions();

        call.enqueue(new Callback<List<TransactionModel>>() {
            @Override
            public void onResponse(Call<List<TransactionModel>> call, Response<List<TransactionModel>> response) {
                if (response.isSuccessful()) {
                    List<TransactionModel> transactions = response.body();
                    displayAmounts(transactions);
                }
            }

            @Override
            public void onFailure(Call<List<TransactionModel>> call, Throwable t) {
                // Handle failure
            }
        });
    }


        private void displayAmounts(List<TransactionModel> transactions) {


        for (TransactionModel transaction : transactions) {

            if (transaction.getAmount() < 0) {

                totalOweAmount += -1*(transaction.getAmount());
            } else {

                totalLentAmount += transaction.getAmount();
            }

        }






        TextView hello = findViewById(R.id.textView);
        hello.setText("Hello, " + user.getName() + "!");
        hello.setTextColor(Color.WHITE);


        TextView owe_txt = findViewById(R.id.textView2);
        owe_txt.setText(Float.toString(totalOweAmount));
        owe_txt.setTextColor(Color.RED);




        TextView lent_txt = findViewById(R.id.textView3);
        lent_txt.setText(Float.toString(totalLentAmount));
        lent_txt.setTextColor(Color.GREEN);


        splitbutton = (Button) findViewById(R.id.equalsplit_button);
        splitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSplit();

            }

        });

        transactionbutton = (Button) findViewById(R.id.transactions_button);
        transactionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTransactions();

            }
        });




        homebutton = (Button) findViewById(R.id.home_button);
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHome();

            }
        });
    }


    public void openSplit() {
        Intent intent3 = new Intent(this, Split.class);
        startActivity(intent3);
    }

    public void openTransactions() {
        Intent intent4 = new Intent(this, Transactions.class);
        startActivity(intent4);
    }

    public void openHome() {

        Intent intent5 = new Intent(this, Home.class);
        intent5.putExtra("user", user);
        startActivity(intent5);
    }



}
