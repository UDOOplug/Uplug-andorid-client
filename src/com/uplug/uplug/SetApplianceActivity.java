package com.uplug.uplug;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import com.erika.uplug.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class SetApplianceActivity extends Activity {
	
	private String UDOO_IP = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setappliance);
		
		Intent i = getIntent();
		UDOO_IP = i.getStringExtra("UDOO_IP");		
	}
	

	public void onFridgeClicked( View v ){
		new ClientTask().execute("21");
	}
	
	public void onWashingmachineClicked( View v ){
		new ClientTask().execute("22");
	}
	
	public void onDishwasherClicked( View v ){
		new ClientTask().execute("23");
	}
	
	public void onTvClicked( View v ){
		new ClientTask().execute("24");
	}
	
	public void onOtherClicked( View v ){
		new ClientTask().execute("20");
	}
	
	private class ClientTask extends AsyncTask<String, Integer, String> {
		   
	    protected String doInBackground(String... request) {
	   
	        Socket socket = null;
	        String line   = null;     
	       
	        try {
	        	Log.i("", "Opening socket connection ...");	        	
	            socket = new Socket (UDOO_IP, 3333);
	       
	            Log.i("", "Socket opened");	           
	            BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            PrintStream outputStream   = new PrintStream(new BufferedOutputStream(socket.getOutputStream()), true);
	            
	            Log.i("", "Request sent: " + request[0]);	            
	            outputStream.println(request[0]);

	            Log.i("", "Waiting answer...");	                    
	            line = new String(inputStream.readLine());          

	            Log.i("", "Server answer: " + line);	               
	            inputStream.close();
	            socket.close();
	            outputStream.close();
	        } catch (Exception e) {
	            Log.e("clientTask", e.toString());
	        }	       
	        return line;
	    }
	}

}
