package id.go.bpkp.loginapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import id.go.bpkp.loginapp.http.ApiInterface;
import id.go.bpkp.loginapp.http.RestClient;
import id.go.bpkp.loginapp.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText txtUsername,txtPassword;
    Button btnLogin;
    //shared preferences (cookie)
    SharedPreferences setting;
    private ApiInterface api;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setting = PreferenceManager
                .getDefaultSharedPreferences(MainActivity.this);
        //jika islogin tidak null dan tidak ""
        //short sircuit and
        if(setting.getString("islogin","") != null &
                !setting.getString("islogin","")
                        .equals("")){
            Intent intent = new Intent(MainActivity.this,
                    HomeActivity.class);
            startActivity(intent);
            finish();
        }
        txtUsername = (EditText) findViewById(R.id.txt_username);
        txtPassword = (EditText) findViewById(R.id.txt_password);
        btnLogin = (Button) findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                if(username.isEmpty()){
                    txtUsername.setError("username harus diisi");
                    return;
                }
                if(password.isEmpty()){
                    txtPassword.setError("password harus diisi");
                    return;
                }
                login(username,password);
                /*
                //write preferences
                SharedPreferences.Editor editor = setting.edit();
                //key = islogin,value=1
                editor.putString("islogin","1");
                editor.commit();

                Toast.makeText(MainActivity.this,
                        username,Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MainActivity.this,
                        HomeActivity.class);
                startActivity(intent);
                finish();*/
            }
        });
        api = RestClient.getClient().create(ApiInterface.class);
    }

    private void login(String username,String password){
        Call<User> call = api.login(username,password);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("authenticating...");
        mProgressDialog.show();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User>
                    response) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }

                User result = response.body();
                if (response.isSuccessful()) {
                    SharedPreferences settings = PreferenceManager
                            .getDefaultSharedPreferences(MainActivity.this);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("token", result.token);
                    editor.putString("islogin", "1");
                    editor.putString("id", result.id);
                    editor.putString("email", result.email);
                    editor.putString("username", txtUsername.getText().toString());
                    editor.commit();
                    Intent intent = new Intent(MainActivity.this,
                            HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "cek username atau password anda", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
                Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }
        });
    }
}
