package com.mfa.rumeet.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mfa.rumeet.BuildConfig;
import com.mfa.rumeet.Model.SharedPrefManager;
import com.mfa.rumeet.R;
import com.mfa.rumeet.Rest.ApiClient;
import com.mfa.rumeet.Rest.ApiInterface;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {


    boolean isOnlyImageAllowed = true;
    private String filePath;
    private static final int PICK_PHOTO = 1958;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private Uri mImageUri;

    Context mContext;

    ImageView pict_foto, foto_profile;
    EditText etNama, etEmail, etDivisi, etTelepon;
    Button btnSimpan;
    Toolbar toolbar;
    SharedPrefManager sharedPrefManager;
    ApiInterface mApiInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mContext = this;

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        etNama = (EditText)findViewById(R.id.nama);
        etEmail = (EditText)findViewById(R.id.email);
        etDivisi = (EditText)findViewById(R.id.divisi);
        etTelepon = (EditText)findViewById(R.id.telepon);
        btnSimpan = (Button)findViewById(R.id.btnSimpan);
        pict_foto = (ImageView)findViewById(R.id.pict_foto);
        foto_profile = (ImageView)findViewById(R.id.foto_profile);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPrefManager = new SharedPrefManager(this);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);


        etNama.setText(sharedPrefManager.getSPNama());
        etEmail.setText(sharedPrefManager.getSPEmail());
        etDivisi.setText(sharedPrefManager.getSpDivisi());
        etTelepon.setText(sharedPrefManager.getSpTelepon());
        if(!sharedPrefManager.getSpFotoProfile().equals("null")){
            foto_profile.setPadding(0,0,0,0);
            Picasso.with(this).load(ApiClient.PROFILE_URL+sharedPrefManager.getSpFotoProfile())
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .error(R.drawable.ic_person)
                    .into(foto_profile);
        }

        pict_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPhoto(v);
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileUpload(filePath);
                edit_profile();
            }
        });

        verifyStoragePermissions(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                filePath = resultUri.getPath();
                mImageUri = resultUri;
                Picasso.with(this).load(mImageUri)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .error(R.mipmap.ic_launcher)
                        .into(foto_profile);

                Picasso.with(this).invalidate(mImageUri);
                foto_profile.setPadding(0,0,0,0);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                if (BuildConfig.DEBUG) error.printStackTrace();
            }
        }
    }

//    private String getPath(Uri uri) {
//        String[] projection = { MediaStore.Images.Media.DATA };
//        Cursor cursor = managedQuery(uri, projection, null, null, null);
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
//    }
//


    public void edit_profile(){
        Call<ResponseBody> editProfile = mApiInterface.editProfile(sharedPrefManager.getSpUniqueId(),
                etNama.getText().toString(),
                etEmail.getText().toString(),
                etDivisi.getText().toString(),
                etTelepon.getText().toString());
        editProfile.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        if (jsonRESULTS.getString("error").equals("false")) {
                            String error_message = jsonRESULTS.getString("message");
                            Toast.makeText(getApplicationContext(), error_message, Toast.LENGTH_SHORT).show();

                            //edit shared preference
                            sharedPrefManager.saveSPString(sharedPrefManager.SP_NAMA, etNama.getText().toString());
                            sharedPrefManager.saveSPString(sharedPrefManager.SP_EMAIL, etEmail.getText().toString());
                            sharedPrefManager.saveSPString(sharedPrefManager.SP_DIVISI, etDivisi.getText().toString());
                            sharedPrefManager.saveSPString(sharedPrefManager.SP_TELEPON, etTelepon.getText().toString());


                            Intent a = new Intent(EditProfileActivity.this, MainActivity.class);
                            a.putExtra("cek", "profile");
                            startActivity(a);
                            finish();
                        } else {

                            String error_message = jsonRESULTS.getString("message");
                            Toast.makeText(getApplicationContext(), error_message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public void addPhoto(View view) {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAutoZoomEnabled(true)
                .setAspectRatio(1, 1)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setRequestedSize(1280, 1280)
                .start(this);

    }


    public void fileUpload(String filePath) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        if (filePath != null){
            final File file = new File(filePath);
            //create RequestBody instance from file
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file); //allow image and any other file

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);


            // finally, execute the request
            Call<ResponseBody> call = apiInterface.fileUpload(sharedPrefManager.getSpUniqueId(),body, file.getName());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response) {
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        String error_message = jsonRESULTS.getString("message");
                        String foto = jsonRESULTS.getJSONObject("user").getString("foto");

                        Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();

                        sharedPrefManager.saveSPString(SharedPrefManager.SP_FOTO_PROFILE, foto);
                        Intent a = new Intent(EditProfileActivity.this, MainActivity.class);
                        a.putExtra("cek", "profile");
                        startActivity(a);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Toast.makeText(mContext, "gagal", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
