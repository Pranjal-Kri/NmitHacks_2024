package com.example.blocktrack;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EtherscanApiService {
    @GET("api?module=account&action=txlist")
    Call<EtherscanResponse> getTransactions(
            @Query("address") String address,
            @Query("startblock") int startBlock,
            @Query("endblock") int endBlock,
            @Query("page") int page,
            @Query("offset") int offset,
            @Query("sort") String sort,
            @Query("apikey") String apiKey
    );
}
