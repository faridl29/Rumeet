package com.mfa.rumeet.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mfa.rumeet.R;
import com.mfa.rumeet.Rest.ApiClient;
import com.mfa.rumeet.Rest.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText etNama, etEmail, etTelepon, etDivisi, etPassword, etKonfirmasiPassword;
    Button btnRegister;
    ProgressDialog loading;

    Context mContext;
    ApiInterface mApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mContext = this;
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        initComponent();
    }

    public void initComponent(){

        etNama = (EditText) findViewById(R.id.etNama);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etTelepon = (EditText) findViewById(R.id.etTelepon);
        etDivisi = (EditText) findViewById(R.id.etDivisi);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etKonfirmasiPassword = (EditText) findViewById(R.id.etKonfirmasiPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                requestRegister();
            }
        });
    }

    public void requestRegister(){
        if(!etPassword.getText().toString().equals("") && !etKonfirmasiPassword.getText().toString().equals("")){
            if(!etPassword.getText().toString().equals(etKonfirmasiPassword.getText().toString())){
                loading.dismiss();
                Toast.makeText(getApplicationContext(), "Cek kembali password anda!", Toast.LENGTH_SHORT).show();
            }else{
                mApiInterface.registerRequest(etNama.getText().toString(),
                        etEmail.getText().toString(),
                        etTelepon.getText().toString(),
                        etDivisi.getText().toString(),
                        etPassword.getText().toString())
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()){
                                    Log.i("debug", "onResponse: BERHASIL");
                                    loading.dismiss();
                                    try {
                                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                        if (jsonRESULTS.getString("error").equals("false")){
                                            Toast.makeText(mContext, "BERHASIL REGISTRASI", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(mContext, LoginActivity.class));
                                        } else {
                                            String error_message = jsonRESULTS.getString("error_msg");
                                            Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Log.i("debug", "onResponse: GA BERHASIL");
                                    loading.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                                Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

        }else{
            loading.dismiss();
            Toast.makeText(getApplicationContext(), "Isi semua kolom!", Toast.LENGTH_SHORT).show();
        }


    }

}
