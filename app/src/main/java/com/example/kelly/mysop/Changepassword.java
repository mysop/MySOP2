package com.example.kelly.mysop;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class Changepassword extends Activity {



    private EditText et1;
    private EditText et2;
    private EditText et3;
    String strHint1;
    String strHint2;
    String strHint3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);


        et1 = (EditText) findViewById(R.id.Originalpassword);
        et2 = (EditText) findViewById(R.id.NewPassword);
        et3 = (EditText) findViewById(R.id.Confirmnewpassward);

        strHint1 = getResources().getString(R.string.Changepassward_Originalpassword);
        strHint2 = getResources().getString(R.string.Changepassward_NewPassword);
        strHint3 = getResources().getString(R.string.Changepassward_Confirmnewpassward);

        final EditText[] mArray = new EditText[] { et1,et2,et3 };

        for (int i = 0; i < mArray.length; i++) {
            final int j = i;

            //EditText 获得焦点时hint消失，失去焦点时hint显示
            mArray[j].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                    if (!hasFocus) {
                        if(j == 0){
                            ((TextView) v).setHint(strHint1);
                        }
                        if(j == 1){
                            ((TextView) v).setHint(strHint2);
                        }
                        if(j == 2){
                            ((TextView) v).setHint(strHint3);
                        }

                    } else {
                        ((TextView) v).setHint("");
                    }

                }
            });
        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_changepassword, menu);
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
}
