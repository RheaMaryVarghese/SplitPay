package com.example.splitpay;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("/signup")
    Call<User> signUp(@Body User user);

    @POST("/login")
    Call<ResponseBody> login(@Body LoginRequest request);

    @GET("/users")
    Call<List<User>> getUsers();

    @POST("/store_split/{table_name}")
    Call<Void> storeSplit(@Path("table_name") String tableName, @Body List<SplitData> SplitDataList);

    @GET("/transactions")
    Call<List<TransactionModel>> getTransactions();

    @POST("settle_transaction/{user_name}")
    Call<Void> settleTransaction(@Path("user_name") String userName);


    @POST("/lent_transaction")
    Call<Void> addLentTransaction(@Body LentTransactionData data);

}
