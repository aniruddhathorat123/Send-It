package aniruddha.com.college_application_app;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class Login_Activity extends AppCompatActivity {
    EditText username,password;
    String uname,pass;
    String login_url="http://matrix-site.000webhostapp.com/sendit/login_user.php";
    JSONObject jsonObject,jsondata;
    JSONArray jsonArray;
    String server_res;
    ProgressDialog progressdialog;

    int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username=(EditText) findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.login_password);
    }

    public void login_run(View view)
    {
        progressdialog =new ProgressDialog(this);
        progressdialog.setIndeterminate(false);
        progressdialog.setMessage("Please Wait...");
           uname=username.getText().toString().trim();
           pass=password.getText().toString().trim();
           Login_backgroundTask login_backgroundTask = new Login_backgroundTask();
           login_backgroundTask.execute(uname,pass);
    }


    public static class login_class
    {
        /*public login_class()
        {

        }

        public login_class(String log_uname,String log_pass)
        {
            this.set_log_uname(log_uname);
            this.set_log_pass(log_pass);
        }*/

        public static String log_username, log_password;

        public String get_log_uname()
        {
            return log_username;
        }

        public String get_log_pass()
        {
            return log_password;
        }

        public static void set_log_uname(String log_uname)
        {
            log_username=log_uname;
        }

        public static void set_log_pass(String log_pass)
        {
            log_password=log_pass;
        }

    }

    class Login_backgroundTask extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressdialog.show();
        }

        @Override
        protected String doInBackground(String... args)
        {
            try
            {
                URL login_user_url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)login_user_url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String login_data = URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(args[0],"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(args[1],"UTF-8");
                bufferedWriter.write(login_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                //Toast.makeText(getApplicationContext(),"Sending data",Toast.LENGTH_LONG).show();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder=new StringBuilder();
                String resp = bufferedReader.readLine();

                stringBuilder.append(resp);
                String jcreate = stringBuilder.toString().trim();
                jsonObject = new JSONObject(jcreate);
                jsonArray = jsonObject.getJSONArray("server_response");
                jsondata = jsonArray.getJSONObject(0);
                server_res = jsondata.getString("result");

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return server_res;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);

            progressdialog.dismiss();
            if (s.equals("true")) {
                Toast.makeText(getApplicationContext(),"Welcome...",Toast.LENGTH_LONG).show();
                Login_Activity.login_class.set_log_pass(pass);
                Login_Activity.login_class.set_log_uname(uname);
                username.setText("");
                password.setText("");
                Intent app_intent = new Intent(Login_Activity.this, app_choice.class);
                startActivity(app_intent);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Login Error,Please try again", Toast.LENGTH_LONG).show();
            }
        }
    }
}
