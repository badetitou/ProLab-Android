package com.tbe.prolab;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
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

    public void goRegisterActivity(View view){
        if (state == 2){
            TextView ev = (TextView)findViewById(R.id.firstname);
            ev.setVisibility(View.GONE);
            TextView ev2 = (TextView) findViewById(R.id.surname);
            ev2.setVisibility(View.GONE);
            TextView ev3 = (TextView) findViewById(R.id.email);
            ev3.setVisibility(View.GONE);
            Button b = (Button) view;
            b.setText(getString(R.string.login_register));
            state = 1;
        }
        else {
            TextView ev = (TextView)findViewById(R.id.firstname);
            ev.setVisibility(View.VISIBLE);
            TextView ev2 = (TextView) findViewById(R.id.surname);
            ev2.setVisibility(View.VISIBLE);
            TextView ev3 = (TextView) findViewById(R.id.email);
            ev3.setVisibility(View.VISIBLE);
            Button b = (Button) view;
            b.setText(getString(R.string.already_account));
            state = 2;
        }
    }

    protected void callMain(){
        Intent intent = new Intent(this, main.class);
        startActivity(intent);
        this.finish();
    }

    protected void callFail(){
        Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
    }

    public void onLogin(View view){
        TextView username = (TextView) findViewById(R.id.user_name);
        TextView password = (TextView) findViewById(R.id.password);
        TextView email = (TextView) findViewById(R.id.email);
        TextView firstname = (TextView) findViewById(R.id.firstname);
        TextView surname = (TextView) findViewById(R.id.surname);
        WebAccess wa = new WebAccess(username.getText().toString(), password.getText().toString(), email.getText().toString(), firstname.getText().toString(), surname.getText().toString());
        wa.execute();
    }


    private class WebAccess extends AsyncTask<String, Void, String> {

        String username;
        String password;
        String email;
        String firstname;
        String surname;


        public WebAccess(String username, String password, String email,String firstname, String surname){
            this.username = username;
            this.password = password;
            this.email = email;
            this.firstname = firstname;
            this.surname = surname;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                if (state == 2)
                    return createAccount(username, password, email, firstname, surname);
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
            if (result.equals("fail")){
              callFail();
            } else {
              callMain();
            }
        }

        private String testAccount(String username, String password) throws IOException {
            InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // web page content.
            int len = 500;

            try {
                URL url = new URL(main.HOST + "/v1/users/"+username + "&" + password);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                Log.d("Connection ", "The response is: " + response);
                if (response == 204){
                    return "fail";
                }

                is = conn.getInputStream();
                // Convert the InputStream into a string
                return readIt(is, len);
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }

        private String createAccount(String username, String password, String email,String firstname, String surname) throws IOException {
            InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // web page content.
            int len = 500;

            try {
                URL url = new URL(main.HOST + "/v1/users");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);

                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write("username="+ username+"&password="+password+"&email="+email+"&firstname="+firstname+"&surname="+surname);
                writer.flush();

                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                Log.d("Connection ", "The response is: " + response);
                is = conn.getInputStream();
                // Convert the InputStream into a string
                return readIt(is, len);
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (is != null) {
                    is.close();
                }
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
