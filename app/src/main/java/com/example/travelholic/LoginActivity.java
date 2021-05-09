package com.example.travelholic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.travelholic.jsonmodel.LoginModel;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText txtAccount;
    private EditText txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        map();

        btnLogin.setOnClickListener(v -> {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("account", txtAccount.getText().toString())
                    .add("password", txtPassword.getText().toString())
                    .build();

            String url = "http://10.0.2.2/travelholic-app/server/index.php?controller=User&action=login";
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    Gson gson = new Gson();
                    LoginModel loginModel = gson.fromJson(response.body().string(), LoginModel.class);  // parse json into Java class
                    if (loginModel.getIsLoggedIn()) {   // if account and password are valid
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "Account or password incorrect!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        });
    }

    private void map() {
        btnLogin = findViewById(R.id.btn_login);
        txtAccount = findViewById(R.id.txt_account);
        txtPassword = findViewById(R.id.txt_password);
    }
}