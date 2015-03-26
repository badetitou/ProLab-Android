package com.tbe.prolab.PopUp;

/**
 * Created by badetitou on 25/03/15.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.tbe.prolab.R;
import com.tbe.prolab.Tools.ReadIt;
import com.tbe.prolab.main;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;


public class RemoveFonctionnality extends DialogFragment {
;
    public int fonctionnality = 0;
    private Context context;
    private String name;

    public RemoveFonctionnality(Context context,String name, int fonctionnality){
        this.context = context;
        this.fonctionnality = fonctionnality;
        this.name = name;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final TextView user = new TextView(this.getActivity());
        user.setText(name);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.popup_remove_user)
                .setPositiveButton(R.string.pop_up_accept, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new WebAccessRemoveFonctionnality(fonctionnality).execute();
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

    private class WebAccessRemoveFonctionnality extends AsyncTask<String, Void, String> {
        int idFonctionnality;
        public WebAccessRemoveFonctionnality(int idFonctionnality) {
            this.idFonctionnality = idFonctionnality;
        }
        @Override
        protected String doInBackground(String... urls) {
            try {
                return removeFonctionnality(idFonctionnality);
            } catch (Exception e) {
                return "fail";
            }

        }

        public String removeFonctionnality(int idFonctionnality) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpDelete httpDelete = new HttpDelete(main.HOST + "/v1/fonctionnalities/" + idFonctionnality);
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
        ((Activity) context).finish();
    }
}

