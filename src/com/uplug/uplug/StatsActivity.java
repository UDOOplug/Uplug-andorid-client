package com.uplug.uplug;
import com.erika.uplug.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


public class StatsActivity extends Activity{
	
	private String UDOO_IP = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stats);
		
		UDOO_IP = getIntent().getStringExtra("UDOO_IP");		
		Log.i("", "Ip server " + UDOO_IP);
	}
    
	public void onHalfHoursAvgClick(View v){
		Intent i = new Intent(this, GraphActivity.class);
		i.putExtra("CHOOSE", 3);
		i.putExtra("UDOO_IP", UDOO_IP);
		startActivity(i);
	}
    public void onComparisonClick(View v){
    	Intent i = new Intent(this, GraphActivity.class);
    	i.putExtra("CHOOSE", 7);
		i.putExtra("UDOO_IP", UDOO_IP);
		startActivity(i);
	}
    public void onDailyAvgClick(View v){
    	Intent i = new Intent(this, GraphActivity.class);
    	i.putExtra("CHOOSE", 1);
		i.putExtra("UDOO_IP", UDOO_IP);
		startActivity(i);
	}
    public void onTurnOnTimeClick(View v){
    	Intent i = new Intent(this, GraphActivity.class);
    	i.putExtra("CHOOSE", 5);
		i.putExtra("UDOO_IP", UDOO_IP);
		startActivity(i);
	}
	
}
