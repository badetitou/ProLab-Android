package com.tbe.prolab.Fonctionnalities;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.tbe.prolab.Project.InfoProject;
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

public class EditFonctionnality extends ActionBarActivity {

    private EditText name;
    private EditText description;
    private EditText deadline;
    private SeekBar avancement;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_fonctionnality);
        name = (EditText) findViewById(R.id.edit_fonctionnality_name);
        description = (EditText) findViewById(R.id.edit_fonctionnality_description);
        deadline = (EditText) findViewById(R.id.edit_fonctionnality_date);
        avancement = (SeekBar) findViewById(R.id.edit_fonctionnality_avancement);
        avancement.setMax(100);

        Bundle bundle = getIntent().getExtras();
        name.setText(bundle.getString("name"));
        description.setText(bundle.getString("description"));
        deadline.setText("date");
        avancement.setProgress(bundle.getInt("progress"));
        id = bundle.getInt("id");
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
            if (!result.equals("1")) {
                callFail(result);
            } else {
                callOk();
            }
        }


        public String modifFonctionnality() throws IOException {
            InputStream inputStream = null;
            String result = "";
            try {
                // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();

                // 2. make Put request to the given URL
                HttpPut httpPut = new HttpPut(main.HOST + "/v1/fonctionnalities");

                String json = "";

                // 3. build jsonObject
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("id", id);
                jsonObject.accumulate("name", name.getText().toString());
                jsonObject.accumulate("description", description.getText().toString());
                jsonObject.accumulate("date", deadline.getText().toString());
                jsonObject.accumulate("avancement",  avancement.getProgress());


                // 4. convert JSONObject to JSON to String
                json = jsonObject.toString();

                // ** Alternative way to convert Person object to JSON string usin Jackson Lib
                // ObjectMapper mapper = new ObjectMapper();
                // json = mapper.writeValueAsString(person);

                // 5. set json to StringEntity
                StringEntity se = new StringEntity(json);

                // 6. set httpPost Entity
                httpPut.setEntity(se);

                // 7. Set some headers to inform server about the type of the content
                httpPut.setHeader("Content-type", "application/json");

                // 8. Execute POST request to the given URL
                HttpResponse httpResponse = httpclient.execute(httpPut);

                // 9. receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();

                // 10. convert inputstream to string
                if (inputStream != null)
                    result = ReadIt.ReadIt(inputStream);
                else
                    result = "Did not work!";


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
