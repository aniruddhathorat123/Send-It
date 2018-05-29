package aniruddha.com.college_application_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;

public class event_app extends Activity
{
    private TextView cur_date,from_date,to_date;
    public EditText event_nm,clg_nm;
    public String todate,from,to,event,user,pass,college,server_resp="";
    JSONObject jsonObject,jsondata;
    JSONArray jsonArray;
    private DatePickerDialog.OnDateSetListener from_date_listener;
    private DatePickerDialog.OnDateSetListener to_date_listener;
    ProgressDialog progressdialog;


    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(event_app.this);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_app);

        progressdialog =new ProgressDialog(this);
        progressdialog.setIndeterminate(false);
        progressdialog.setMessage("Please Wait...");

        cur_date=(TextView) findViewById(R.id.today_date);
        from_date=(TextView) findViewById(R.id.event_from_date);
        to_date=(TextView) findViewById(R.id.event_to_date);
        event_nm = (EditText)findViewById(R.id.event_name);
        clg_nm = (EditText) findViewById(R.id.host_clg_name);

        Calendar today = Calendar.getInstance();
        int year= today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH)+1;
        int day = today.get(Calendar.DAY_OF_MONTH);

        cur_date.setText(day+"/"+month+"/"+year);

        from_date.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Calendar cal=Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog from_dialog = new DatePickerDialog(event_app.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,from_date_listener,year,month,day);
                from_dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                from_dialog.show();
            }
        });

        from_date_listener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year,int month, int day)
            {
                month++;
                String date = day+"/"+month+"/"+year;
                from_date.setText(date);
            }
        };

        to_date.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Calendar cal=Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog to_dialog = new DatePickerDialog(event_app.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,to_date_listener,year,month,day);
                to_dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                to_dialog.show();
            }
        });

        to_date_listener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year,int month, int day)
            {
                month++;
                String date = day+"/"+month+"/"+year;
                to_date.setText(date);
            }
        };
    }

    public void leave_send_click(View view)
    {
        todate = cur_date.getText().toString().trim();
        from = from_date.getText().toString().trim();
        to = to_date.getText().toString().trim();
        event = event_nm.getText().toString().trim();
        college = clg_nm.getText().toString().trim();
        Login_Activity.login_class lc = new Login_Activity.login_class();
        user= lc.get_log_uname();
        pass = lc.get_log_pass();

        if(from!="Date" && !to.equals("Date") && !event.equals(""))
        {
            //Toast.makeText(getApplicationContext(),"Correct data..."+todate+from+to+rson+pno+user+pass,Toast.LENGTH_LONG).show();
            Send_eventapp send_eventapp = new Send_eventapp();
            send_eventapp.execute(user, pass, todate, from, to, event,college);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please write correct details...",Toast.LENGTH_LONG).show();
        }
    }

    public void leave_clear_click(View view)
    {
        from_date.setText("Date");
        to_date.setText("Date");
        event_nm.setText("");
    }

    public class Send_eventapp extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressdialog.show();
        }

        @Override
        protected String doInBackground(String... args)
        {
            URL login_user_url = null;
            try
            {
                login_user_url = new URL("http://matrix-site.000webhostapp.com/sendit/event_app.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection)login_user_url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String leave_data = URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(args[0],"UTF-8")
                        +"&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(args[1],"UTF-8")
                        +"&"+URLEncoder.encode("todate","UTF-8")+"="+URLEncoder.encode(args[2],"UTF-8")
                        +"&"+URLEncoder.encode("from","UTF-8")+"="+URLEncoder.encode(args[3],"UTF-8")
                        +"&"+URLEncoder.encode("to","UTF-8")+"="+URLEncoder.encode(args[4],"UTF-8")
                        +"&"+URLEncoder.encode("event","UTF-8")+"="+URLEncoder.encode(args[5],"UTF-8")
                        +"&"+URLEncoder.encode("college","UTF-8")+"="+URLEncoder.encode(args[6],"UTF-8");
                bufferedWriter.write(leave_data);
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
                server_resp = jsondata.getString("result");

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return server_resp;
        }

        @Override
        protected void onPostExecute(String resp)
        {
            super.onPostExecute(resp);
            progressdialog.dismiss();
            switch (resp)
            {
                case "00":
                    Toast.makeText(getApplicationContext(),"User login not found,please login again...",Toast.LENGTH_LONG).show();
                    break;
                case "10":
                    Toast.makeText(getApplicationContext(),"server or database error, please try later..",Toast.LENGTH_LONG).show();
                    break;
                case "11":
                    Toast.makeText(getApplicationContext(),"Application sent successfully!",Toast.LENGTH_LONG).show();
                    from_date.setText("Date");
                    to_date.setText("Date");
                    event_nm.setText("");
                    finish();
                    break;
                default:
                    Toast.makeText(getApplicationContext(),"something went wrong!",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
