package com.uplug.uplug;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.erika.uplug.R;
import com.uplug.uplug.client.Messages;

public class InstantConsumptionActivity extends Activity{
	
	TextView instantConsumptionTextView = null;
	String UDOO_IP = null;
	
	boolean isOn = false;
	boolean play = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instantconsumption);
		
		UDOO_IP = getIntent().getStringExtra("UDOO_IP");
		
		instantConsumptionTextView=(TextView) findViewById(R.id.textView1);
		instantConsumptionTextView.setText("Waiting data...") ;
		
		new ClientTask().execute(Messages.instantConsumption);
		
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		play = false;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	public void onButtonClick(View v){
		new ClientTask().execute(Messages.on);	
		isOn = !isOn;
	}
	
	private float power = 0.0f;

	
	private class TVsym extends AsyncTask<URL, Integer, Long> {
	     protected Long doInBackground(URL... urls) {

	         while (play) {
	        	 try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	             power = (float) (120 + ((Math.random() * 40) - 20) + (Math.random()));
	        	 publishProgress();
	         }
	         return (long) 0;
	     }

	     protected void onProgressUpdate(Integer... progress) {
	         if(isOn){
	        	 instantConsumptionTextView.setText(power + " ");
	         }
	         else{
	        	 instantConsumptionTextView.setText("0.0");
	         }
	     }

	     protected void onPostExecute(Long result) {
	         
	     }
	 }

	
	
	private class ClientTask extends AsyncTask<String, Integer, String> {
		   
	    protected String doInBackground(String... request) {	   
	        Socket socket = null;
	        String line = null;     
	       		       
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


	    protected void onPostExecute(String result) {
	    	instantConsumptionTextView.setText(result);
	    }
	}
	
}
