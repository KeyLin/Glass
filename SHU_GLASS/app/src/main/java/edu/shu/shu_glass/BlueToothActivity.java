package edu.shu.shu_glass;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;


public class BlueToothActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_tooth);

        ListView blueToothDevice = new ListView(this);

//        blueToothDevice.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,getDevice()));
        search();

        Button communiteBtn = (Button)findViewById(R.id.communiteBtn);

        communiteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BlueToothActivity.this,BlueToothCommuniteActivity.class);
                startActivity(intent);
            }
        });

    }

    private void search(){

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (!adapter.enable()) {
            adapter.enable();
        }
        Intent enable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);

        enable.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3600);
        startActivity(enable);

//        Intent searchIntent = new Intent(this, ComminuteActivity.class);
//         startActivity(searchIntent);
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_blue_tooth, menu);
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
