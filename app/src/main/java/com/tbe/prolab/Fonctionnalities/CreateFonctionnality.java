package com.tbe.prolab.Fonctionnalities;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.tbe.prolab.R;
import com.tbe.prolab.Tools.ReadIt;
import com.tbe.prolab.main;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import java.sql.Date;

public class CreateFonctionnality extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_fonctionnality);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_fonctionnality, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    public void onCreateFonctionnality(View view){
        Toast.makeText(this, "Create Fonctionnality", Toast.LENGTH_SHORT).show();
        EditText name = (EditText) findViewById(R.id.create_fonctionnality_name);
        EditText description = (EditText) findViewById(R.id.create_fonctionality_description);
        DatePicker datePicker = (DatePicker) findViewById(R.id.create_fonctionality_date_picker);
        java.util.Date dateUtil = new Date(datePicker.getYear() - 1900, datePicker.getMonth(), datePicker.getDayOfMonth());
        Date dateSql = new Date(dateUtil.getTime());
        new WebAccess(new Fonctionnality(name.getText().toString(), description.getText().toString(), dateSql)).execute();
    }

    private void callFail(){
        Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
    }

    private void callFinish(){
        this.finish();
    }

    private class WebAccess extends AsyncTask<String, Void, String> {

        Fonctionnality fonctionnality;


        public WebAccess(Fonctionnality fonctionnality) {
            this.fonctionnality = fonctionnality;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return createFonctionnality();
            } catch (Exception e) {
                return "fail";
            }

        }

        public String createFonctionnality() throws IOException {
            InputStream inputStream = null;
            String result = "";
            try {
                // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();

                // 2. make POST request to the given URL
                HttpPost httpPost = new HttpPost(main.HOST + "/v1/fonctionnalities/" +main.idMember);

                String json = "";

                // 3. build jsonObject
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("name", fonctionnality.getName());
                jsonObject.accumulate("description", fonctionnality.getDescription());
                jsonObject.accumulate("deadLine", fonctionnality.getDeadLine());

                // 4. convert JSONObject to JSON to String
                json = jsonObject.toString();

                // ** Alternative way to convert Person object to JSON string usin Jackson Lib
                // ObjectMapper mapper = new ObjectMapper();
                // json = mapper.writeValueAsString(person);

                // 5. set json to StringEntity
                StringEntity se = new StringEntity(json);

                // 6. set httpPost Entity
                httpPost.setEntity(se);

                // 7. Set some headers to inform server about the type of the content
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");

                // 8. Execute POST request to the given URL
                HttpResponse httpResponse = httpclient.execute(httpPost);

                // 9. receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();

                // 10. convert inputstream to string
                if(inputStream != null)
                    result = ReadIt.ReadIt(inputStream);
                else
                    result = "Did not work!";


            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            // 11. return result
            return result;
        }


        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if (result != null){
                callFinish();
            } else {
                callFail();
            }
        }

        public String readIt(InputStream stream, int len) throws IOException {
            Reader reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }
    }
}
