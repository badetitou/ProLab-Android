package com.tbe.prolab.PopUp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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


public class RemoveMember extends DialogFragment {
    private int idMember;
    private String username;
    public static int fonctionnality = 0;
    private Context context;

    public RemoveMember(Context context, int idMember, String username, int fonctionnality){
        this.context = context;
        this.idMember = idMember;
        this.username = username;
        this.fonctionnality = fonctionnality;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final TextView user = new TextView(this.getActivity());
        user.setText(username);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.popup_remove_user)
                .setPositiveButton(R.string.pop_up_accept, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (fonctionnality == 0) {
                            new WebAccessRemoveUser(idMember).execute();
                        } else {
                            new WebAccessRemoveTask(idMember, fonctionnality).execute();
                        }
                    }
                })
                .setNegativeButton(R.string.pop_up_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        builder.setView(user);

        // Create the AlertDialog object and return it
        return builder.create();
    }

    private class WebAccessRemoveTask extends AsyncTask<String, Void, String> {
        int idMember;
        int idFonctionnality;
        public WebAccessRemoveTask(int idMember, int idFonctionnality) {
            this.idMember = idMember;
            this.idFonctionnality = idFonctionnality;
        }
        @Override
        protected String doInBackground(String... urls) {
            try {
                return removeUser(idMember, idFonctionnality);
            } catch (Exception e) {
                return "fail";
            }

        }

        public String removeUser(int idMember, int idFonctionnality) {
            InputStream inputStream = null;
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpDelete httpDelete = new HttpDelete(main.HOST + "/v1/task/" + idFonctionnality + "&" + idMember);
                HttpResponse httpResponse = httpclient.execute(httpDelete);
                return ReadIt.ReadIt(httpResponse.getEntity().getContent());
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            // 11. return result
            return "fail";
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            callResult(result);
        }
    }

    private class WebAccessRemoveUser extends AsyncTask<String, Void, String> {
        int idMember;
        public WebAccessRemoveUser(int idMember) {
            this.idMember = idMember;
        }
        @Override
        protected String doInBackground(String... urls) {
            try {
                return removeUser(idMember);
            } catch (Exception e) {
                return "fail";
            }

        }

        public String removeUser(int idMember) {
            InputStream inputStream = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpDelete httpDelete = new HttpDelete(main.HOST + "/v1/members/" + idMember);
                HttpResponse httpResponse = httpclient.execute(httpDelete);
                return ReadIt.ReadIt(httpResponse.getEntity().getContent());
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            // 11. return result
            return "fail";
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            callResult(result);
        }
    }

    private void callResult(String result) {
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
    }
}

