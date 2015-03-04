package com.tbe.prolab;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class LogIn extends ActionBarActivity {

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
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void goRegisterActivity(View view){
        if (state == 2){
            TextView ev = (TextView)findViewById(R.id.firstname);
            ev.setVisibility(View.GONE);
            TextView ev2 = (TextView) findViewById(R.id.surname);
            ev2.setVisibility(View.GONE);
            Button b = (Button) view;
            b.setText(getString(R.string.login_register));
            state = 1;
        }
        else {
            TextView ev = (TextView)findViewById(R.id.firstname);
            ev.setVisibility(View.VISIBLE);
            TextView ev2 = (TextView) findViewById(R.id.surname);
            ev2.setVisibility(View.VISIBLE);
            Button b = (Button) view;
            b.setText(getString(R.string.already_account));
            state = 2;
        }
    }
}
