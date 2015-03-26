package com.tbe.prolab.Fonctionnalities;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tbe.prolab.R;
import com.tbe.prolab.Tools.ReadIt;
import com.tbe.prolab.main;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Date;
import java.util.Locale;

public class EditFonctionnality extends ActionBarActivity {

    private EditText name;
    private EditText description;
    private TextView deadline;
    private SeekBar avancement;
    private int id;
    private DateFormat dateFormatter;
    private DatePickerDialog datePickerDialog;
    private Date dateDeadline;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_fonctionnality);
        name = (EditText) findViewById(R.id.edit_fonctionnality_name);
        description = (EditText) findViewById(R.id.edit_fonctionnality_description);
        deadline = (TextView) findViewById(R.id.edit_fonctionnality_date);
        avancement = (SeekBar) findViewById(R.id.edit_fonctionnality_avancement);
        avancement.setMax(100);

        Bundle bundle = getIntent().getExtras();
        name.setText(bundle.getString("name"));
        description.setText(bundle.getString("description"));
        deadline.setText(bundle.getString("date"));
        avancement.setProgress(bundle.getInt("progress"));
        id = bundle.getInt("id");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        java.util.Date dateUtil = null;
        try {
            dateUtil = dateFormat.parse(bundle.getString("date"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dateDeadline = new Date(dateUtil.getTime());

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        // DATE PICKER
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(EditFonctionnality.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                deadline.setText(dateFormatter.format(newDate.getTime()));
                dateDeadline = new Date(newDate.getTimeInMillis());
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_fonctionnality, menu);
        return true;
    }

    public void update(View view) {
        new WebAccessUpdateFonctionnality().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private class WebAccessUpdateFonctionnality extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String result) {
            if (!result.equals("update")) {
                callFail(result);
            } else {
                callOk();
            }
        }


        public String modifFonctionnality() throws IOException {
            InputStream inputStream = null;
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPut httpPut = new HttpPut(main.HOST + "/v1/fonctionnalities");
                String json = "";
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("id", id);
                jsonObject.accumulate("name", name.getText().toString());
                jsonObject.accumulate("description", description.getText().toString());
                jsonObject.accumulate("deadLine", dateDeadline.toString());
                jsonObject.accumulate("avancement",  avancement.getProgress());

                json = jsonObject.toString();
                StringEntity se = new StringEntity(json);
                httpPut.setEntity(se);
                httpPut.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = httpclient.execute(httpPut);
                return  ReadIt.ReadIt(httpResponse.getEntity().getContent());
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            // 11. return result
            return result;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return modifFonctionnality();
            } catch (Exception e) {
                return "fail";
            }
        }

    }

    private void callOk() {
        Toast.makeText(this, "update", Toast.LENGTH_SHORT).show();
    }

    private void callFail(String result) {
        Toast.makeText(this, "fail : " + result, Toast.LENGTH_SHORT).show();
    }
}
