package com.tbe.prolab.PopUp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tbe.prolab.Project.Project;
import com.tbe.prolab.R;
import com.tbe.prolab.Tools.ReadIt;
import com.tbe.prolab.main;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.InputStream;


public class RemoveUser extends DialogFragment {
    private String idMember;
    private String username;

    public RemoveUser (String idMember, String username){
        this.idMember = idMember;
        this.username = username;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final TextView user = new TextView(this.getActivity());
        user.setText(username);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.popup_remove_user)
                .setPositiveButton(R.string.pop_up_accept, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new WebAccessRemoveUser(idMember).execute();
                    }
                })
                .setNegativeButton(R.string.pop_up_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    private class WebAccessRemoveUser extends AsyncTask<String, Void, String> {

        String idMember;


        public WebAccessRemoveUser(String idMember) {
            this.idMember = idMember;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return removeUser(username);
            } catch (Exception e) {
                return "fail";
            }

        }


        public String removeUser(String idMember) {
            InputStream inputStream = null;
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpDelete httpDelete = new HttpDelete(main.HOST + "/v1/members/");
                httpclient.execute(httpDelete);
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

