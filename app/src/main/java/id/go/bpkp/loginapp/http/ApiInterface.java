package id.go.bpkp.loginapp.http;

import java.util.List;

import id.go.bpkp.loginapp.model.Divisi;
import id.go.bpkp.loginapp.model.Karyawan;
import id.go.bpkp.loginapp.model.User;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("divisi")
    Call<List<Divisi>> getDivisi();

    @GET("karyawan")
    Call<List<Karyawan>> getKaryawan();

    @POST("karyawan/hapus/{id}")
    Call<String> deleteKaryawan(@Path("id") String id);

    @Multipart
    @POST("karyawan/insert")
    Call<String> insertKaryawan(@Part MultipartBody.Part image,
                                @Part("nama") RequestBody nama,
                                @Part("tgllahir") RequestBody tgllahir,
                                @Part("email") RequestBody email,
                                @Part("telpon") RequestBody telpon,
                                @Part("jeniskelamin") RequestBody jeniskelamin,
                                @Part("iddivisi") RequestBody iddivisi
    );

    @Multipart
    @POST("karyawan/update")
    Call<String> updateKaryawan(@Part MultipartBody.Part image,
                                @Part("nama") RequestBody nama,
                                @Part("tgllahir") RequestBody tgllahir,
                                @Part("email") RequestBody email,
                                @Part("telpon") RequestBody telpon,
                                @Part("jeniskelamin") RequestBody jeniskelamin,
                                @Part("iddivisi") RequestBody iddivisi,
                                @Part("id") RequestBody id
    );

    @FormUrlEncoded
    @POST("Login/login")
    Call<User> login(@Field("identity") String username, @Field("password") String password);
}
