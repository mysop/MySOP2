package com.example.kelly.mysop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import java.util.ArrayList;

public class Content extends Activity {

    private EditText inputText;
    private ListView listInput;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        inputText = (EditText)findViewById(R.id.inputText);
        listInput = (ListView)findViewById(R.id.listInputText);
        items = new ArrayList<String>();
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,items);
        listInput.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
/*        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
*/

        switch(item.getItemId()){
            case Menu.FIRST:
                if(!inputText.getText().toString().equals("")){
                    items.add(inputText.getText().toString());
                    listInput.setAdapter(adapter);
                    inputText.setText("");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent it) {
        if(resultCode == RESULT_OK) {
            items.set(requestCode, it.getStringExtra("備忘")); // �ϥζǦ^����Ƨ�s�}�C���e
            adapter.notifyDataSetChanged(); // �q�� Adapter �}�C���e����s
        }
    }

    //新增評論
    public void writeCommon (View v){
        if(!inputText.getText().toString().equals("")){
            items.add(inputText.getText().toString());
            listInput.setAdapter(adapter);
            inputText.setText("");
        }
    }
}
