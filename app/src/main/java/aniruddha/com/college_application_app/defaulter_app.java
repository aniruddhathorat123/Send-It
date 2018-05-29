package aniruddha.com.college_application_app;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
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
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;

public class defaulter_app extends AppCompatActivity
{
    private TextView cur_date;
    public EditText acd_year,course_name,attaindence,sem;
    public String todate,academic_year,user,pass,course,attan,semester,year,server_resp="";
    JSONObject jsonObject,jsondata;
    JSONArray jsonArray;
    ProgressDialog progressdialog;


    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(defaulter_app.this);
        builder.setMessage("Are you sure to leave, all fields will clear?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defaulter_app);

        progressdialog =new ProgressDialog(this);
        progressdialog.setIndeterminate(false);
        progressdialog.setMessage("Please Wait...");

        cur_date=(TextView) findViewById(R.id.today_date);
        acd_year = (EditText)findViewById(R.id.academic_year_id);
        course_name = (EditText) findViewById(R.id.course_name_id);
        attaindence = (EditText) findViewById(R.id.attan_percent_id);
        sem = (EditText) findViewById(R.id.semester_id);

        Calendar today = Calendar.getInstance();
        int year= today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH)+1;
        int day = today.get(Calendar.DAY_OF_MONTH);

        cur_date.setText(day+"/"+month+"/"+year);
    }

    public void defaulter_send_click(View view)
    {
        todate = cur_date.getText().toString().trim();
        academic_year = acd_year.getText().toString().trim();
        course = course_name.getText().toString().trim();
        attan = attaindence.getText().toString().trim();
        semester = sem.getText().toString().trim();
        int seme = Integer.parseInt(semester);

        Login_Activity.login_class lc = new Login_Activity.login_class();
        user= lc.get_log_uname();
        pass = lc.get_log_pass();

        if(!academic_year.equals("") && !course.equals("") && !attan.equals("") && Float.valueOf(attan)<=75.00 && seme<=8)
        {
            if(seme==1 || seme==2)
            {
               year = "1st year";
            }
            else if(seme==3 || seme==4)
            {
                year = "2nd year";
            }
            else if(seme==5 || seme==6)
            {
                year = "3rd year";
            }
            else
            {
                year = "4th year";
            }
            Send_defaulterapp send_defaulterapp = new Send_defaulterapp();
            send_defaulterapp.execute(user, pass, todate, academic_year,course,attan,semester,year);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please write correct details..."+todate+academic_year+course+attan+semester,Toast.LENGTH_LONG).show();
        }
    }

    public void defaulter_clear_click(View view)
    {
        acd_year.setText("");
        course_name.setText("");
        attaindence.setText("");
        sem.setText("");
    }

    public class Send_defaulterapp extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressdialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            URL defaulter_url = null;
            try {
                defaulter_url = new URL("http://matrix-site.000webhostapp.com/sendit/defaulter_app.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) defaulter_url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String defaulter_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(args[0], "UTF-8")
                        + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(args[1], "UTF-8")
                        + "&" + URLEncoder.encode("todate", "UTF-8") + "=" + URLEncoder.encode(args[2], "UTF-8")
                        + "&" + URLEncoder.encode("acd_year", "UTF-8") + "=" + URLEncoder.encode(args[3], "UTF-8")
                        + "&" + URLEncoder.encode("course", "UTF-8") + "=" + URLEncoder.encode(args[4], "UTF-8")
                        + "&" + URLEncoder.encode("attan", "UTF-8") + "=" + URLEncoder.encode(args[5], "UTF-8")
                        + "&" + URLEncoder.encode("semester", "UTF-8") + "=" + URLEncoder.encode(args[6], "UTF-8")
                        + "&" + URLEncoder.encode("year", "UTF-8") + "=" + URLEncoder.encode(args[7], "UTF-8");
                bufferedWriter.write(defaulter_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String resp = bufferedReader.readLine();

                stringBuilder.append(resp);
                String jcreate = stringBuilder.toString().trim();
                jsonObject = new JSONObject(jcreate);
                jsonArray = jsonObject.getJSONArray("server_response");
                jsondata = jsonArray.getJSONObject(0);
                server_resp = jsondata.getString("result");

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return server_resp;
        }

        @Override
        protected void onPostExecute(String resp) {
            super.onPostExecute(resp);
            progressdialog.dismiss();
            switch (resp) {
                case "00":
                    Toast.makeText(getApplicationContext(), "User login problem,please login again...", Toast.LENGTH_LONG).show();
                    break;
                case "10":
                    Toast.makeText(getApplicationContext(), "server or database error, please try later..", Toast.LENGTH_LONG).show();
                    break;
                case "11":
                    Toast.makeText(getApplicationContext(), "Application sent, Parent need to fill data!", Toast.LENGTH_LONG).show();
                    acd_year.setText("");
                    course_name.setText("");
                    attaindence.setText("");
                    sem.setText("");
                    finish();
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "Something went's wrong....", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
