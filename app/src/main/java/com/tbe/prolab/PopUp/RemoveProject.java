package com.tbe.prolab.PopUp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.tbe.prolab.R;
import com.tbe.prolab.Tools.ReadIt;
import com.tbe.prolab.main;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;

/**
 * Created by badetitou on 25/03/15.
 */
public class RemoveProject extends DialogFragment{
        Context context;

        public RemoveProject(){}


        public RemoveProject(Context context){
            this.context = context;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.popup_remove_project)
                    .setPositiveButton(R.string.pop_up_accept, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            new WebAccessRemoveProject().execute();
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

        private class WebAccessRemoveProject extends AsyncTask<String, Void, String> {


            @Override
            protected String doInBackground(String... urls) {
                try {
                    return removeProject();
                } catch (Exception e) {
                    return "fail";
                }

            }


            public String removeProject() {
                String result = "";
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpDelete httpDelete = new HttpDelete(main.HOST + "/v1/projects/" + main.idProject);
                    HttpResponse httpResponse = httpclient.execute(httpDelete);
                    return ReadIt.ReadIt(httpResponse.getEntity().getContent());
                } catch (Exception e) {
                    Log.d("InputStream", e.getLocalizedMessage());
                }
                // 11. return result
                return result;
            }

            // onPostExecute displays the results of the AsyncTask.
            @Override
            protected void onPostExecute(String result) {
                ((Activity) context).finish();
            }
        }
    }
