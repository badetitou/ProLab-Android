package com.tbe.prolab.Users;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tbe.prolab.Project.SelectProject;
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
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LogIn extends ActionBarActivity {

    // 1 == EXIST
    // 2 == NO EXIST
    private int state = 1;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar =(ProgressBar) findViewById(R.id.login_progressbar);
        progressBar.setMax(100);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    public void goRegisterActivity(View view) {
        if (state == 2) {
            TextView ev = (TextView) findViewById(R.id.firstname);
            ev.setVisibility(View.GONE);
            TextView ev2 = (TextView) findViewById(R.id.lastname);
            ev2.setVisibility(View.GONE);
            TextView ev3 = (TextView) findViewById(R.id.email);
            ev3.setVisibility(View.GONE);
            Button b = (Button) view;
            b.setText(getString(R.string.login_register));
            state = 1;
        } else {
            TextView ev = (TextView) findViewById(R.id.firstname);
            ev.setVisibility(View.VISIBLE);
            TextView ev2 = (TextView) findViewById(R.id.lastname);
            ev2.setVisibility(View.VISIBLE);
            TextView ev3 = (TextView) findViewById(R.id.email);
            ev3.setVisibility(View.VISIBLE);
            Button b = (Button) view;
            b.setText(getString(R.string.already_account));
            state = 2;
        }
    }

    protected void callSelectProject(String username) {
        Intent intent = new Intent(this, SelectProject.class);
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        intent.putExtras(bundle);
        startActivity(intent);
        this.finish();
    }

    protected void callFail() {
        TextView username = (TextView) findViewById(R.id.user_name);
        TextView password = (TextView) findViewById(R.id.password);
    }


    public void onLogin(View view) {
        TextView username = (TextView) findViewById(R.id.user_name);
        TextView password = (TextView) findViewById(R.id.password);
        TextView email = (TextView) findViewById(R.id.email);
        TextView firstname = (TextView) findViewById(R.id.firstname);
        TextView lastname = (TextView) findViewById(R.id.lastname);
        new WebAccess(username.getText().toString(), password.getText().toString(), email.getText().toString(), firstname.getText().toString(), lastname.getText().toString()).execute();
    }


    private class WebAccess extends AsyncTask<String, Void, String> {

        String username;
        String password;
        String email;
        String firstname;
        String lastname;


        public WebAccess(String username, String password, String email, String firstname, String lastname) {
            this.username = username;
            this.password = password;
            this.email = email;
            this.firstname = firstname;
            this.lastname = lastname;
        }

        @Override
        protected String doInBackground(String... urls) {
            progressBar.setIndeterminate(true);
            try {
                if (state == 2)
                    return createAccount(new User(username, password, email, firstname, lastname));
                else if (state == 1)
                    return testAccount(username, password);
                else {
                    return "fail";
                }
            } catch (IOException e) {
                return "fail";
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            progressBar.setIndeterminate(false);
            try {
                JSONObject user = new JSONObject(result);
                callSelectProject(user.getString("username"));
            } catch (Exception e){
                callFail();
            }
        }

        private String testAccount(String username, String password) throws IOException {
            InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // web page content.

            try {
                URL url = new URL(main.HOST + "/v1/users/" + username + "&" + password);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                Log.d("Connection ", "The response is: " + response);
                if (response == 204) {
                    return "fail";
                }

                is = conn.getInputStream();
                // Convert the InputStream into a string
                return ReadIt.ReadIt(is);
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }

        private String createAccount(User user) throws IOException {
            InputStream inputStream = null;
            String result = "";
            try {
                // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();

                // 2. make POST request to the given URL
                HttpPost httpPost = new HttpPost(main.HOST + "/v1/users");

                String json = "";

                // 3. build jsonObject
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("username", user.getUsername());
                jsonObject.accumulate("password", user.getPassword());
                jsonObject.accumulate("firstname", user.getFirstname());
                jsonObject.accumulate("lastname", user.getLastname());
                jsonObject.accumulate("email", user.getEmail());

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
                    result = "fail";


            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            // 11. return result
            return result;
        }
    }

}
