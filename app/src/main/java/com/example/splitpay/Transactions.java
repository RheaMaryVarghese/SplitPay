package com.example.splitpay;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Transactions extends AppCompatActivity {

    Button Owe_Button,Lent_Button,Refresh_Button,Back_Button,Save1_Button,Okay1_Button,Save2_Button,Okay2_Button;

    EditText namein1, amountin1,namein2,amountin2;

    String name1,name2;

    Float amount1,amount2;

    Retrofit retrofit;
    private LinearLayout containerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transactions);
        Intent intent = getIntent();

        containerLayout = findViewById(R.id.linear_layout2);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8000/") // Replace with your FastAPI backend URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<TransactionModel>> call = apiService.getTransactions();

        call.enqueue(new Callback<List<TransactionModel>>() {
            @Override
            public void onResponse(Call<List<TransactionModel>> call, Response<List<TransactionModel>> response) {
                if (response.isSuccessful()) {
                    List<TransactionModel> transactions = response.body();
                    displayTransactions(transactions);
                }
            }

            @Override
            public void onFailure(Call<List<TransactionModel>> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private void displayTransactions(List<TransactionModel> transactions) {
        for (TransactionModel transaction : transactions) {
            LinearLayout itemLayout = new LinearLayout(this);
            itemLayout.setOrientation(LinearLayout.HORIZONTAL);
            TextView textView = new TextView(this);
            String userName = transaction.getUserName();
            String amount = String.valueOf(transaction.getAmount());
            String amountText = transaction.getAmount() < 0 ? "" : "Owes You";
            String htmlText = "<font color='#FFFFFF'>" + userName + "</font>"
                    + "<font color='" + (transaction.getAmount() < 0 ? Color.RED : Color.GREEN) + "'>" + amount + "</font>"
                    + "<font color='#800080'>" + amountText + "</font>";
            textView.setText(HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY));


            if (transaction.getAmount() < 0) {
                textView.setTextColor(Color.RED);
                Button settleButton = new Button(this);
                settleButton.setText("Settle");
                settleButton.setTextColor(Color.BLACK);
                settleButton.setTextSize(16);
                settleButton.setBackgroundColor(Color.parseColor("#D86FEA"));
                int widthInDp = 113;
                int heightInDp = 55;
                int widthInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthInDp, getResources().getDisplayMetrics());
                int heightInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, heightInDp, getResources().getDisplayMetrics());
                settleButton.setLayoutParams(new LinearLayout.LayoutParams(widthInPixels, heightInPixels));






                // Add the click listener to handle settling
                settleButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textView.setTextColor(Color.GRAY);
                        textView.setText(HtmlCompat.fromHtml("<font color='#808080'>Settled</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                        itemLayout.removeView(settleButton);
                        settleTransaction(transaction.getUserName());
                    }
                });
                itemLayout.addView(textView);
                itemLayout.addView(settleButton);
            } else {

                itemLayout.addView(textView);
            }

            containerLayout.addView(itemLayout);
        }



        Owe_Button = (Button) findViewById(R.id.owe_button);
        Owe_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOwe_Dialog();
                }
    });


        Lent_Button = (Button) findViewById(R.id.lent_button);
        Lent_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLent_Dialog();
            }
        });



        Refresh_Button = (Button) findViewById(R.id.Refresh_button);
        Refresh_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        Back_Button = (Button) findViewById(R.id.Back_button);
        Back_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent10 = new Intent(Transactions.this, Dashboard.class);
                startActivity(intent10);

            }
        });
}


    private void openLent_Dialog() {
        Dialog dialog1 = new Dialog(this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.lent_dialog);
        dialog1.setCancelable(false);
        dialog1.show();



        namein1 = (EditText) dialog1.findViewById(R.id.editTextname1);
        amountin1 = (EditText) dialog1.findViewById(R.id.editTextamount1);
        Save1_Button = (Button) dialog1.findViewById(R.id.save1_button);
        Save1_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name1= namein1.getText().toString();
                amount1 = Float.parseFloat(amountin1.getText().toString());

                addLentTransaction(name1,amount1);


            }


        });

        Okay1_Button = (Button) dialog1.findViewById(R.id.okay1_button);
        Okay1_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }


        });




    }


    private void openOwe_Dialog() {

        Dialog dialog2 = new Dialog(this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.owe_dialog);
        dialog2.setCancelable(false);
        dialog2.show();


        namein2 = (EditText) dialog2.findViewById(R.id.editTextname2);
        amountin2 = (EditText) dialog2.findViewById(R.id.editTextamount2);
        Save2_Button = (Button) dialog2.findViewById(R.id.save2_button);
        Save2_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name2= namein2.getText().toString();
                amount2 = -(Float.parseFloat(amountin2.getText().toString()));

                addLentTransaction(name2,amount2);


            }


        });


        Okay2_Button = (Button) dialog2.findViewById(R.id.okay2_button);
        Okay2_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }


        });







    }




    private void settleTransaction(String Name) {

        ApiService apiService = retrofit.create(ApiService.class);
        Call<Void> call = apiService.settleTransaction(Name);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Transaction deleted successfully on the server
                    Toast.makeText(Transactions.this, "Transaction deleted", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle error response
                    Toast.makeText(Transactions.this, "Failed to delete transaction", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle network or other errors
                Toast.makeText(Transactions.this, "Failed to delete transaction", Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void addLentTransaction(String name, float amount){

        ApiService apiService = retrofit.create(ApiService.class);
        LentTransactionData data = new LentTransactionData(name, amount);
        Call<Void> call = apiService.addLentTransaction(data);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Lent transaction added successfully
                    Toast.makeText(Transactions.this, "Transaction added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle error response
                    Toast.makeText(Transactions.this, "Failed to add transaction", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle network or other errors
                Toast.makeText(Transactions.this, "Failed to add transaction", Toast.LENGTH_SHORT).show();
            }
        });

    }


}




