package com.tbe.prolab.Project;

import android.os.AsyncTask;
import android.util.Log;

import com.tbe.prolab.main;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by badetitou on 13/03/15.
 */
public class WebAccessUserProject extends AsyncTask<String, Void, String> {


    public WebAccessUserProject() {
    }

    @Override
    protected String doInBackground(String... urls) {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(main.HOST + "/v1/projects/");
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
            return readIt(is, len);
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } catch (Exception e) {

        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {

    }

    public String readIt(InputStream stream, int len) throws IOException {
        Reader reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}
