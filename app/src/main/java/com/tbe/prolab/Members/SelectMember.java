package com.tbe.prolab.Members;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import com.tbe.prolab.DividerItemDecoration;
import com.tbe.prolab.PopUp.AddUser;
import com.tbe.prolab.R;
import com.tbe.prolab.Tools.ReadIt;
import com.tbe.prolab.main;
import org.json.JSONArray;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SelectMember extends Fragment implements View.OnClickListener {

    private RecyclerView listMember;
    private RecyclerView.Adapter listMemberAdapter;
    private RecyclerView.LayoutManager listMemberLayoutManager;

    public SelectMember() {
        // Required empty public constructor
    }

    public static SelectMember newInstance() {
        SelectMember fragment = new SelectMember();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_select_member, container, false);

        ImageButton imageButton = (ImageButton) v.findViewById(R.id.select_member_add_button);
        imageButton.setOnClickListener(this);

        RecyclerView listMember = (RecyclerView) v.findViewById(R.id.select_member_list);
        listMember.setHasFixedSize(true);
        listMember.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        listMemberLayoutManager = new LinearLayoutManager(getActivity());
        listMember.setLayoutManager(listMemberLayoutManager);

        listMemberAdapter = new MemberAdapter(new String[0]);
        listMember.setAdapter(listMemberAdapter);

        new WebAccessProjectUser(main.idProject).execute();

        return v;
    }

    protected void callFail(){
        Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
    }

    private void addMember(){

        new AddUser().show(this.getFragmentManager(), "addUser");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.select_member_add_button:
                addMember();
        }

    }

    private class WebAccessProjectUser extends AsyncTask<String, Void, String> {

        String idProject;

        public WebAccessProjectUser(String idProject) {
            this.idProject = idProject;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return getAllProjectMember(idProject);
            } catch (IOException e) {
                return "fail";
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                ArrayList<String> members = new ArrayList<>();
                for (int i = 0;i<jsonArray.length();++i){
                    members.add(jsonArray.getJSONObject(i).getString("username"));
                }
                ((MemberAdapter) listMemberAdapter).setData(members);
            } catch (Exception e){
                callFail();
            }
        }

        private String getAllProjectMember(String idProject) throws IOException {
            InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // web page content.
            int len = 500;

            try {
                URL url = new URL(main.HOST + "/v1/members/project/" + idProject);
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
    }
}