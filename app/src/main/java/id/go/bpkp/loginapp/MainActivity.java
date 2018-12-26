package id.go.bpkp.loginapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText txtUsername,txtPassword;
    Button btnLogin;
    //shared preferences (cookie)
    SharedPreferences setting;

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
                finish();
            }
        });
    }
}
