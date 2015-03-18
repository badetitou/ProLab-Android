package com.tbe.prolab.Project;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.plus.model.people.Person;
import com.tbe.prolab.R;
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
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;


public class CreateProject extends ActionBarActivity {

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void valid(View view) {
        new WebAccess(((TextView) findViewById(R.id.create_project_name)).getText().toString()
                , ((TextView) findViewById(R.id.create_project_punchline)).getText().toString()
                , ((TextView) findViewById(R.id.create_project_description)).getText().toString()
                , ((TextView) findViewById(R.id.create_project_url)).getText().toString()
                , username).execute();
    }

    protected void callFail() {
        Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
    }


    protected void callMain() {
        this.finish();
    }

    private class WebAccess extends AsyncTask<String, Void, String> {

        String projectName;
        String projectPunchLine;
        String projectDescription;
        String projectURL;
        String username;


        public WebAccess(String projectName, String projectPunchLine, String projectDescription, String projectURL, String username) {
            this.projectDescription = projectDescription;
            this.projectName = projectName;
            this.projectPunchLine = projectPunchLine;
            this.projectURL = projectURL;
            this.username = username;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return POST(new Project(0, projectName, projectDescription, projectURL, projectPunchLine));
            } catch (Exception e) {
                return "fail";
            }

        }


        public String POST(Project project){
            InputStream inputStream = null;
            String result = "";
            try {
                    // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();

                    // 2. make POST request to the given URL
                HttpPost httpPost = new HttpPost(main.HOST + "/v1/projects/" + username);

                String json = "";

                    // 3. build jsonObject
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("name", project.getName());
                jsonObject.accumulate("description", project.getDescription());
                jsonObject.accumulate("url", project.getUrl());
                jsonObject.accumulate("punchline", project.getPunchline());

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
                    result = readIt(inputStream, 500);
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
            if (result.equals("fail")) {
                callFail();
            } else {
                callMain();
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
