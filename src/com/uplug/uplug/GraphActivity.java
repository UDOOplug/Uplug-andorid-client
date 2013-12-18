package com.uplug.uplug;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;
import com.erika.uplug.R;
import com.uplug.uplug.client.Messages;


public class GraphActivity extends Activity{
 
	// Fake datas used for debugging
	ArrayList<Float> last30DailyPowerConsumption = new ArrayList<Float>(
			Arrays.asList(36.99524026532989f,38.02884667133093f,36.861932938856015f,35.75025644922305f,32.462417709614414f,37.03702881252429f,35.32036775106082f,33.097592273663594f,36.65698669463921f,38.39864588370968f,37.19922914065469f,41.05674740484429f,30.527049609236535f,36.01644330932361f,33.0153115546467f,35.43637628424658f,29.510194354710585f,38.77186150850758f,35.349605522682445f,35.35156900860872f,36.41024093079414f,38.44252183406113f,38.87453769559033f,38.153449994462285f,35.43809316024872f,36.97576243980738f,36.53860551386898f,36.17952489920453f,36.62959780282734f,32.44362509351288f) );
	
	ArrayList<Float> last30DayTurnOnTime = new ArrayList<Float>(
			Arrays.asList(6.7195301027900145f,11.257951671061837f,11.062458908612754f,6.329394779833593f,9.769803949938508f,11.126630766668518f,10.501142421934501f,5.88998075169689f,10.763098327168333f,6.921617499348983f,11.259323598067423f,7.6871297155878135f,5.349087655925595f,6.443022354865555f,5.843538219736019f,10.727311643835616f,9.109145792511782f,7.115279776517396f,6.277172220492059f,10.589058594834768f,10.800724438834312f,11.426200873362445f,11.694452347083926f,11.172001329050836f,10.876840842151195f,6.594083925705114f,6.656706854396763f,11.054593004249755f,6.722539554162665f,9.91984610452068f) );
	
	ArrayList<Float> lastDayPowerConsumption = new ArrayList<Float>(
			Arrays.asList(41.347753494174455f,31.411722543484917f,35.43683895031231f,37.49235640887254f,30.599764976606735f,38.027153295551166f,31.40639331906231f,38.60986269009503f,28.282835627015455f,37.253861151345454f,26.11811556347808f,39.50436005459228f,27.761164824642556f,36.678745268695884f,37.40683921139533f,20.728558713102167f,50.52122172567785f,12.28663401782595f,54.927113828720145f,19.551117881836642f,31.070408890981714f,34.6154765384356f,30.09005955165082f,43.60152994804179f,33.62911841044285f,30.637009840795656f,41.91636406233682f,31.084896782718733f,31.854272649777787f,41.04508401077033f,30.644610007522186f,33.60109826422219f,41.53510957382154f,29.87550109886883f,47.53659270450344f,23.239090957982608f,54.20718020196783f,17.21268421017538f,60.2921415374172f,36.22318814566197f,49.6705766948502f,42.04626510931689f,33.99396843001716f,52.554492574328044f,27.598518043399256f,47.326104160043826f,39.37277825973183f,30.527446483544733f));
	
	
	TextView graphString = null;
	String UDOO_IP       = null;
	
	BarGraph barGraph  = null;
	BarGraph barGraph2 = null;
	
	int choose = -1;

	private boolean useDebugData = false;
	
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graph);
		
		graphString   = (TextView) findViewById(R.id.textView1);
		barGraph      = (BarGraph) findViewById(R.id.graph);
		barGraph2     = (BarGraph) findViewById(R.id.graph2);
		
		Intent i = getIntent();
		choose   = i.getIntExtra("CHOOSE", -1);
		UDOO_IP  = i.getStringExtra("UDOO_IP");
		
		if(useDebugData)
			updateGraph(choose + "");
		else
			new ClientTask().execute(choose + "");			
	}
	
	private void updateGraph(String s){
		ArrayList<Float> temp  = null;
		ArrayList<Float> temp2 = null;
		
		if(choose == 1){
			temp = (useDebugData) ? last30DailyPowerConsumption : Messages.stringToFloatArraylist(s);			
			barGraph.setShowBarText(false);
			graphString.setText("Last 30 days power consumption.");
		}else if(choose == 3){
			temp = (useDebugData) ? last30DayTurnOnTime : Messages.stringToFloatArraylist(s);	
			barGraph.setShowBarText(false);
			graphString.setText("How many hours the plug was turn on last month");
		}else if(choose == 5){
			temp = (useDebugData) ? lastDayPowerConsumption : Messages.stringToFloatArraylist(s);
			barGraph.setShowBarText(false);
			graphString.setText("Last day power consumption");
		}else if(choose == 7){
			temp = Messages.stringToFloatArraylist(s);
			barGraph.setShowBarText(true);
		} 
	
		ArrayList<Bar> points = new ArrayList<Bar>();
			
		for(int j=0; j<temp.size(); j++){
			Bar d = new Bar();
			d.setColor(Color.parseColor("#99CC00"));
			d.setName("");
			d.setValue(temp.get(j));
	
			points.add(d);
		}
		barGraph.setBars(points);	
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


	    protected void onPostExecute(String result) {
	    	updateGraph(result);
	    }
	}
	
}
