package aniruddha.com.college_application_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Aniruddha on 11-03-2018.
 */

public class app_choice extends AppCompatActivity
{

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(app_choice.this);
        builder.setMessage("Please click on Log Out button to leave");
        builder.setCancelable(true);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_choice);
    }

    public void log_out_click(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(app_choice.this);
        builder.setMessage("Are you sure to log off?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes, I want to log out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Login_Activity.login_class lc =new Login_Activity.login_class();
                Login_Activity.login_class.set_log_uname("");
                Login_Activity.login_class.set_log_pass("");
                Toast.makeText(getApplicationContext(),"Log out successful",Toast.LENGTH_LONG).show();
                finish();
            }
        });
        builder.setNegativeButton("No, I want to continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void leave_bt_click(View view)
    {
        Intent leave_intent = new Intent(app_choice.this,leave_app.class);
        startActivity(leave_intent);
    }

    public void event_bt_click(View view)
    {
        Intent event_intent = new Intent(app_choice.this,event_app.class);
        startActivity(event_intent);
    }
    public void defaulter_bt_click(View view)
    {
        Intent defaulter_intent = new Intent(app_choice.this,defaulter_app.class);
        startActivity(defaulter_intent);
    }
}
