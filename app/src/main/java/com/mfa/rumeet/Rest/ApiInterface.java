package com.mfa.rumeet.Rest;

import com.mfa.rumeet.Model.GetBooked;
import com.mfa.rumeet.Model.GetFasilitas;
import com.mfa.rumeet.Model.GetFoto;
import com.mfa.rumeet.Model.GetNotifikasi;
import com.mfa.rumeet.Model.GetRuangan;
import com.mfa.rumeet.Model.PostPutDelBooking;
import com.mfa.rumeet.Model.PostToken;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("api/List_ruangan")
    Call<GetRuangan> getRuangan();

    @GET("api/List_ruangan/search")
    Call<GetRuangan> getSearch(@Query("query") String query);

    @GET("api/Booked/accepted")
    Call<GetBooked> getBookedAccepted(@Query("id_ruangan") String id);

    @GET("api/Booked/pending")
    Call<GetBooked> getBookedPending(@Query("id_ruangan") String id);

    @GET("api/Booking_today")
    Call<GetBooked> getBookingToday(@Query("id_pengguna") String id);

    @GET("api/Jadwal")
    Call<GetBooked> getJadwal(@Query("unique_id") String id,
                              @Query("status") String status);


    @FormUrlEncoded
    @POST("api/Login")
    Call<ResponseBody> loginRequest(@Field("email") String email,
                                    @Field("password") String password);

    @FormUrlEncoded
    @POST("api/Register")
    Call<ResponseBody> registerRequest(@Field("nama") String nama,
                                       @Field("email") String email,
                                       @Field("telepon") String telepon,
                                       @Field("divisi") String divisi,
                                       @Field("password") String password);

    @FormUrlEncoded
    @POST("api/Booked")
    Call<ResponseBody> bookingRequest(@Field("id_pengguna") String id_pengguna,
                                           @Field("tanggal") String tanggal,
                                           @Field("dari_jam") String dari_jam,
                                           @Field("sampai_jam") String sampai_jam,
                                           @Field("keterangan") String keterangan,
                                           @Field("id_ruangan") String id_ruangan);

    @FormUrlEncoded
    @POST("api/Notifikasi")
    Call<PostToken> kirimToken(@Field("id") String id,
                               @Field("email") String email,
                               @Field("token") String token);

    @GET("api/Notifikasi")
    Call<GetNotifikasi> getNotifikasi(@Query("id_pengguna") String id);

    @GET("api/Notifikasi/notif")
    Call<GetNotifikasi> getNotif(@Query("id_pengguna") String id);

    @FormUrlEncoded
    @POST("api/Notifikasi/clicked")
    Call<ResponseBody> editClicked(@Field("id") String id);

    @GET("api/List_ruangan/foto_ruangan")
    Call<GetFoto> getFotoRuangan(@Query("id_ruangan") String id);

    @GET("api/Fasilitas")
    Call<GetFasilitas> getFasilitas(@Query("id_ruangan") String id);

    @FormUrlEncoded
    @PUT("api/Profile")
    Call<ResponseBody> editProfile(@Field("id_pengguna") String id,
                                   @Field("nama") String nama,
                                   @Field("email") String email,
                                   @Field("divisi") String divisi,
                                   @Field("telepon") String telepon);

    @Multipart
    @POST("api/Profile")
    Call<ResponseBody> fileUpload(@Part("id_pengguna") String id,
                                  @Part MultipartBody.Part file,
                                  @Part("nama_file") String nama);

    @GET("api/List_ruangan/favorite")
    Call<GetRuangan> getFavorite(@Query("id_pengguna") String id);

    @FormUrlEncoded
    @POST("api/List_ruangan/cek_favorite")
    Call<ResponseBody> cekFavorite(@Field("id_pengguna") String id_pengguna,
                                   @Field("id_ruangan") String id_ruangan);

    @GET("api/List_ruangan/cek_button_favorite")
    Call<ResponseBody> cekButtonFavorite(@Query("id_pengguna") String id_pengguna,
                                         @Query("id_ruangan") String id_ruangan);

    @GET("api/History")
    Call<GetBooked> getHistory(@Query("id_pengguna") String id_pengguna);

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "api/notifikasi", hasBody = true)
    Call<ResponseBody> delNotifikasi(@Field("id_pengguna") String id);

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "api/Jadwal", hasBody = true)
    Call<ResponseBody> cancelBooking(@Field("id_book") String id);

}
