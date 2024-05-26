package com.example.blocktrack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button Logoutbtn;
    TextView textView;
    FirebaseUser user;
    EditText addressInput;
    ImageView searchButton;
    RecyclerView recyclerView;
    TransactionAdapter transactionAdapter;
    SharedPreferences sharedPreferences;
    private static final String API_KEY = "FH5V7JZZT5HQ7MMISAYFXHG77JZ2SN8AHA";
    private static final String BASE_URL = "https://api-sepolia.etherscan.io/";
    private static final String PREF_NAME = "BlockTrackPrefs";
    private static final String PREF_ADDRESS = "last_address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        Logoutbtn = findViewById(R.id.Logout_btn);
        textView = findViewById(R.id.user_details);
        addressInput = findViewById(R.id.address);
        searchButton = findViewById(R.id.searchbtn);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        user = auth.getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            String email = user.getEmail();
            String[] parts = email.split("@");
            if (parts.length > 0) {
                String username = parts[0];
                textView.setText(username);
            } else {
                textView.setText(email); // Fallback in case splitting fails
            }
        }

        String lastAddress = sharedPreferences.getString(PREF_ADDRESS, "");
        if (!lastAddress.isEmpty()) {
            addressInput.setText(lastAddress);
            fetchTransactions(lastAddress);
        }

        Logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = addressInput.getText().toString();
                if (!address.isEmpty()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(PREF_ADDRESS, address);
                    editor.apply();
                    fetchTransactions(address);
                }
            }
        });
    }

    private void fetchTransactions(String address) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EtherscanApiService service = retrofit.create(EtherscanApiService.class);
        Call<EtherscanResponse> call = service.getTransactions(address, 0, 99999999, 1, 20, "desc", API_KEY);
        call.enqueue(new Callback<EtherscanResponse>() {
            @Override
            public void onResponse(@NonNull Call<EtherscanResponse> call, @NonNull Response<EtherscanResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    List<EtherscanResponse.Transaction> transactions = response.body().result;
                    transactionAdapter = new TransactionAdapter(transactions, address);
                    recyclerView.setAdapter(transactionAdapter);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to Fetch", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<EtherscanResponse> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to Fetch", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
