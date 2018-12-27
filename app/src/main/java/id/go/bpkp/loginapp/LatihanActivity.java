package id.go.bpkp.loginapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class LatihanActivity extends AppCompatActivity {

    SharedPreferences setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latihan);
        setting = PreferenceManager
                .getDefaultSharedPreferences(LatihanActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_logout){
            SharedPreferences.Editor editor = setting.edit();
            editor.clear();
            editor.commit();
            Intent intent =
                    new Intent(LatihanActivity.this,
                            MainActivity.class);
            startActivity(intent);
            finish();
        }
        if(id == R.id.action_setting){
            //
        }
        return true;
    }
}
