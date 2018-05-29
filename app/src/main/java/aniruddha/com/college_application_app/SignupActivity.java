package aniruddha.com.college_application_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class SignupActivity extends AppCompatActivity {

    EditText nm,rno,eml,mno,unme,pass;
    String name,email,mobile_no,rollno,username,password,res;
    String add_user_url = "http://matrix-site.000webhostapp.com/sendit/add_user.php";
    JSONObject jsonObject,jsondata;
    JSONArray jsonArray;
    String server_res;
    ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        progressdialog =new ProgressDialog(this);
        progressdialog.setIndeterminate(false);
        progressdialog.setMessage("Please Wait...");
        nm=(EditText)findViewById(R.id.stdname);
        rno=(EditText)findViewById(R.id.stdrno);
        eml=(EditText)findViewById(R.id.email);
        mno=(EditText)findViewById(R.id.mobno);
        unme=(EditText)findViewById(R.id.signup_username);
        pass=(EditText)findViewById(R.id.password);
    }

    public void signup_call(View view)
    {
        name=nm.getText().toString();
        rollno=rno.getText().toString();
        email=eml.getText().toString();
        mobile_no=mno.getText().toString();
        username=unme.getText().toString();
        password=pass.getText().toString();

        BackgroundTask backgroundTask = new BackgroundTask();
        backgroundTask.execute(name,rollno,email,mobile_no,username,password);
    }

    class BackgroundTask extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressdialog.show();
        }

        @Override
        protected String doInBackground(String... args)
        {
            try
            {
                URL url = new URL(add_user_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data_string = URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(args[0],"UTF-8")+"&"+
                        URLEncoder.encode("rollno","UTF-8")+"="+URLEncoder.encode(args[1],"UTF-8")+"&"+
                        URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(args[2],"UTF-8")+"&"+
                        URLEncoder.encode("mobile_no","UTF-8")+"="+URLEncoder.encode(args[3],"UTF-8")+"&"+
                        URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(args[4],"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(args[5],"UTF-8");
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

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
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Toast.makeText(getApplicationContext(),"Sending Data....",Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(String res)
        {
            progressdialog.show();
            Toast.makeText(getApplicationContext(),res,Toast.LENGTH_LONG).show();
            if(res.contains("success")) {
                nm.setText("");
                rno.setText("");
                eml.setText("");
                mno.setText("");
                unme.setText("");
                pass.setText("");
            }
        }
    }
}
