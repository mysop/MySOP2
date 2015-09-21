package com.example.kelly.mysop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class EmailVertifyError extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_vertify_error);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_email_vertify_error, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void backToEmailVerify(View v){
//        Intent it = new Intent(this,Emailverify.class);
//        startActivity(it);

       //  Intent i = new Intent(this, Search.class);
        Intent i = new Intent(this, Home.class);
        Bundle bundle = new Bundle();
        bundle.putString("TAG_Key", "");
        i.putExtras(bundle);	//將參數放入intent
        startActivity(i);
    }
}
