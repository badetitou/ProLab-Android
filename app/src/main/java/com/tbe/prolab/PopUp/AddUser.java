package com.tbe.prolab.PopUp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.tbe.prolab.R;
import com.tbe.prolab.Tools.ReadIt;
import com.tbe.prolab.main;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class AddUser extends DialogFragment {

    public AddUser(){}

    public int toMember;
    public int idFonctionnality;

    public AddUser(int toMember, int idFonctionnality){
        this.toMember = toMember;
        this.idFonctionnality = idFonctionnality;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final EditText input = new EditText(this.getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.popup_add_user)
                .setPositiveButton(R.string.pop_up_accept, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (toMember == 0) {
                            new WebAccessAddUser(input.getText().toString()).execute();
                        } else if (toMember == 1) {
                            new WebAccessAddUserFonctionnality(input.getText().toString(), idFonctionnality).execute();
                        }
                    }
                })
                    .setNegativeButton(R.string.pop_up_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
        input.setHint(R.string.popup_add_username);
        builder.setView(input);

        // Create the AlertDialog object and return it
        return builder.create();
    }

    private class WebAccessAddUserFonctionnality extends AsyncTask<String, Void, String> {

        String username;
        int idFonctionnality;

        public WebAccessAddUserFonctionnality(String username, int idFonctionnality) {
            this.username = username;
            this.idFonctionnality = idFonctionnality;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return Verif(username);
            } catch (Exception e) {
                return "fail";
            }

        }

        public String Verif(String username) throws IOException, JSONException {
            InputStream is = null;
            try {
                URL url = new URL(main.HOST + "/v1/members/" + username + "&" + main.idProject);
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
                String result = ReadIt.ReadIt(conn.getInputStream());
                JSONObject jsonObject = new JSONObject(result);
                return POST(jsonObject.getString("idMember"));
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }


        public String POST(String idMember) {
            InputStream inputStream = null;
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(main.HOST + "/v1/task/");
                String json = "";
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("idMember", idMember);
                jsonObject.accumulate("idFonctionnality", idFonctionnality);
                json = jsonObject.toString();
                StringEntity se = new StringEntity(json);
                httpPost.setEntity(se);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = httpclient.execute(httpPost);
                inputStream = httpResponse.getEntity().getContent();
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

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {}
    }

    private class WebAccessAddUser extends AsyncTask<String, Void, String> {

        String username;


        public WebAccessAddUser(String username) {
            this.username = username;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return POST(username);
            } catch (Exception e) {
                return "fail";
            }

        }


        public String POST(String username) {
            InputStream inputStream = null;
            String result = "";
            try {
                // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();

                // 2. make POST request to the given URL
                HttpPost httpPost = new HttpPost(main.HOST + "/v1/members/");

                String json = "";

                // 3. build jsonObject
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("idProject", main.idProject);
                jsonObject.accumulate("username", username);
                jsonObject.accumulate("role", 0);

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

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {}
    }
}

