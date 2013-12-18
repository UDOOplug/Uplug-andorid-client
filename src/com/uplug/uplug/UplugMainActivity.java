package com.uplug.uplug;

import com.erika.uplug.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class UplugMainActivity extends Activity {

	// UDOO network address
	public static String IP_UDOO_ADDR = "192.168.0.110";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_uplug_main);	
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.menu_settings:
	            startSettingsActivity();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void startSettingsActivity() {
		showServerIpDialog();
	}

	public void onStatsClick(View v){
		Intent i = new Intent(this, StatsActivity.class);
		i.putExtra("UDOO_IP", IP_UDOO_ADDR);
		startActivity(i);		
	}

	public void onInstantConsumptionClick(View v){
		Intent i = new Intent(this, InstantConsumptionActivity.class);
		i.putExtra("UDOO_IP", IP_UDOO_ADDR);
		startActivity(i);		
	}
	
	public void onSetApplianceClick(View v){
		Intent i = new Intent(this, SetApplianceActivity.class);
		startActivity(i);		
	}
	
	public void onClassificationClick(View v){
		// TODO add server request
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.uplug_main, menu);
		return true;
	}
	

	
	private AlertDialog showServerIpDialog(){
		LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(R.layout.alert_dialog_setserverip, null);
		((EditText)textEntryView.findViewById(R.id.username_edit)).setText(IP_UDOO_ADDR);
		return new AlertDialog.Builder(UplugMainActivity.this)
		    .setTitle(R.string.alert_dialog_text_entry)
		    .setView(textEntryView)
		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
		        	EditText et = (EditText)textEntryView.findViewById(R.id.username_edit);
		        	UplugMainActivity.IP_UDOO_ADDR = et.getText().toString();
		        	
		        	Toast toast = Toast.makeText(UplugMainActivity.this,
		        			"New ip address: " + UplugMainActivity.IP_UDOO_ADDR,
		        			Toast.LENGTH_SHORT);
			    	toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 200);
			    	toast.show();
		        }
		    })
		    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
		
		        }
		    })
		    .show();
	}
	
	
	private AlertDialog showAnnResultDialog(){
		LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(R.layout.alert_dialog_ann_result, null);
		return new AlertDialog.Builder(UplugMainActivity.this)
		    .setTitle("ANN result")
		    .setView(textEntryView)
		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
		        	
		        }
		    })
		    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
		
		        }
		    })
		    .show();
	}

}
