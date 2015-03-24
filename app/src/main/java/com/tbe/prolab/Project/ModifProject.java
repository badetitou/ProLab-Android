package com.tbe.prolab.Project;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tbe.prolab.R;
import com.tbe.prolab.Tools.ReadIt;
import com.tbe.prolab.main;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class ModifProject extends ActionBarActivity {

    EditText name;
    EditText description;
    EditText punchline;
    String url;
    int idProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modif_project);
        Bundle bundle = this.getIntent().getExtras();
        idProject = bundle.getInt("projectNumber");
        url = bundle.getString("url");

        name = (EditText) findViewById(R.id.modif_project_name);
        punchline = (EditText) findViewById(R.id.modif_project_punchline);
        description = (EditText) findViewById(R.id.modif_project_description);
        name.setText(bundle.getString("name"));
        punchline.setText(bundle.getString("punchline"));
        description.setText(bundle.getString("description"));
    }

    public void update(View view){
        new WebAccessUpdate().execute();
        this.finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_modif_project, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private class WebAccessUpdate extends AsyncTask<String, Void, String>{

        @Override
        protected void onPostExecute(String result){
            if (!result.equals("1")){
                callFail(result);
            } else {
                callOk();
            }
        }


        public String createFonctionnality() throws IOException {
            InputStream inputStream = null;
            String result = "";
            try {
                // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();

                // 2. make Put request to the given URL
                HttpPut httpPut = new HttpPut(main.HOST + "/v1/projects");

                String json = "";

                // 3. build jsonObject
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("name", name.getText().toString());
                jsonObject.accumulate("description", description.getText().toString());
                jsonObject.accumulate("punchline", punchline.getText().toString());
                jsonObject.accumulate("id", idProject);
                jsonObject.accumulate("url", url);


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

        @Override
        protected String doInBackground(String... params) {
            try {
                return createFonctionnality();
            } catch (Exception e){
                return "fail";
            }
        }
    }

    private void callOk() {
        Toast.makeText(this, getString(R.string.update_ok),Toast.LENGTH_SHORT).show();
        this.finish();
    }

    private void callFail(String result) {
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
    }
}
