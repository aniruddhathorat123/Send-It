package aniruddha.com.college_application_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button b1,b2,b3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1=(Button)findViewById(R.id.signup);
        b2=(Button)findViewById(R.id.login);
        b3=(Button)findViewById(R.id.check);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected())
        {
            b3.setVisibility(View.GONE);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please Turn on Mobile Data",Toast.LENGTH_LONG).show();
            b1.setEnabled(false);
            b2.setEnabled(false);
            b3.setVisibility(View.VISIBLE);
        }
    }

    public void about_click(View view)
    {
        Intent about_intent = new Intent(MainActivity.this, about_page.class);
        startActivity(about_intent);
    }

    public void login_bt_click(View view)
    {
        Intent login_clk = new Intent(MainActivity.this,Login_Activity.class);
        startActivity(login_clk);
    }

    public void signup_bt_click(View view)
    {
        Intent signup_clk=new Intent(MainActivity.this,SignupActivity.class);
        startActivity(signup_clk);
    }

    public void net_check(View view)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected())
        {
            b1.setEnabled(true);
            b2.setEnabled(true);
            b3.setVisibility(View.GONE);
        }
    }
}
