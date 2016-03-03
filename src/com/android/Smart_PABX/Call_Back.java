package com.android.Smart_PABX;




import java.util.Timer;
import java.util.TimerTask;

import org.jwebsocket.client.plugins.rpc.Rpc;
import org.jwebsocket.client.plugins.rpc.RpcListener;
import org.jwebsocket.client.plugins.rpc.Rrpc;
import org.jwebsocket.kit.WebSocketException;
import org.jwebsocket.token.Token;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Call_Back extends Activity{
	//public Button Dialer;
	//public Button EXT_Board;
	//public Button Settings;
	public Button five_min;
	public Button ten_min;
	public Button fifteen_min;
	public Button twenty_min;
	public Button cancel_call_back;
	public TextView yourName;
	public TextView yourExtension;
	public String[] Name=null;
	public String[] Designation=null;
	public String[] extension=null;
	public TextView Incoming_number=null;
	public long time_for_timerTask;
	String caller_no;
	TimerTask mTimerTask;
	final Handler handler = new Handler();
	public static Timer timer = new Timer();	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_back);
       // Dialer = (Button) findViewById(R.id.Dialer);
       // EXT_Board = (Button) findViewById(R.id.EXT_Board);
       // Settings = (Button) findViewById(R.id.settings);
        five_min = (Button) findViewById(R.id.time_one_call_back);
        ten_min = (Button) findViewById(R.id.time_two_call_back);
        fifteen_min = (Button) findViewById(R.id.time_three_call_back);
        twenty_min = (Button) findViewById(R.id.time_four_call_back);
        Incoming_number = (TextView) findViewById(R.id.incoming_call_call_back);
        cancel_call_back = (Button) findViewById(R.id.cancel_callback_call_back);
        yourName=(TextView)findViewById (R.id.your_name_status_call_back);
        yourExtension=(TextView)findViewById(R.id.your_extension_status_call_back);
        yourName.setText(Home.yourName);
        yourExtension.setText(Home.yourExtension);
        Buttons();
    }
    
    
    public void Buttons()
    {
    	 five_min.setOnClickListener(new View.OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				five_min.setBackgroundResource(R.drawable.dashboard_screen_button_1_hover);
 				time_for_timerTask=5*60*1000;
 				doTimerTask(time_for_timerTask);
 				int status=1005;
 				DashBoard.aToken.setInteger("status",status);//my number
				DashBoard.aToken.setString("request","get_dialin_action");
                try {
                	
                	Home.btc.sendToken(DashBoard.aToken);//try to send the token to the server
        		} catch (WebSocketException ex) {
        			Log.i("Debug","+ex.getMessage()");//log errors
        		}
 			}
 		});
    	 
    	 ten_min.setOnClickListener(new View.OnClickListener() {
  			@Override
  			public void onClick(View v) {
  				ten_min.setBackgroundResource(R.drawable.dashboard_screen_button_1_hover);
  				time_for_timerTask=10*60*1000;
 				doTimerTask(time_for_timerTask);
 				int status=1010;
 				/*
 				 * sending token to server about the call back time
 				 */
 				DashBoard.aToken.setInteger("status",status);//my number
				DashBoard.aToken.setString("request","get_dialin_action");
                try {
                	
                	Home.btc.sendToken(DashBoard.aToken);//try to send the token to the server
        		} catch (WebSocketException ex) {
        			Log.i("Debug","+ex.getMessage()");//log errors
        		}
 				
  			}
  		});
    	 
    	 fifteen_min.setOnClickListener(new View.OnClickListener() {
  			@Override
  			public void onClick(View v) {
  				fifteen_min.setBackgroundResource(R.drawable.dashboard_screen_button_1_hover);
  				time_for_timerTask=15*60*1000;
 				doTimerTask(time_for_timerTask);
 				int status=1015;
 				/*
 				 * sending token to server about the call back time
 				 */
 				DashBoard.aToken.setInteger("status",status);//my number
				DashBoard.aToken.setString("request","get_dialin_action");
                try {
                	
                	Home.btc.sendToken(DashBoard.aToken);//try to send the token to the server
        		} catch (WebSocketException ex) {
        			Log.i("Debug","+ex.getMessage()");//log errors
        		}
  			}
  		});
    	 
    	 twenty_min.setOnClickListener(new View.OnClickListener() {
  			@Override
  			public void onClick(View v) {
  				twenty_min.setBackgroundResource(R.drawable.dashboard_screen_button_1_hover);
  				time_for_timerTask=20*60*1000;
 				doTimerTask(time_for_timerTask);
 				int status=1020;
 				/*
 				 * sending token to server about the call back time
 				 */
 				DashBoard.aToken.setInteger("status",status);//my number
				DashBoard.aToken.setString("request","get_dialin_action");
                try {
                	
                	Home.btc.sendToken(DashBoard.aToken);//try to send the token to the server
        		} catch (WebSocketException ex) {
        			Log.i("Debug","+ex.getMessage()");//log errors
        		}
  			}
  		});
    	 
    	 cancel_call_back.setOnClickListener(new View.OnClickListener() {
   			@Override
   			public void onClick(View v) {
   				cancel_call_back.setBackgroundResource(R.drawable.dashboard_screen_button_1_hover);
   				finish();	
   			}
   		});
    	
    }
    
    /*
     * Timer Task code
     */
    
    public void doTimerTask(long time){
    	 
    	mTimerTask = new TimerTask() {
    	        public void run() {
    	                handler.post(new Runnable() {
    	                        public void run() {
    	                        	Intent Call_Back_Reminder = new Intent(getApplicationContext(),Call_Back_Reminder.class);
    	            				startActivity(Call_Back_Reminder);
    	                        	Log.d("TIMER", "TimerTask run");
    	                        }
    	               });
    	        }};
    	    timer.schedule(mTimerTask, time, 24*60*60*1000);  // 
 
    	 }
    
    /*
	 * function to launch the next activity i-e Extension Board.It will send data to next activity 
	 */
	private Intent setIntentDataForActivities(int activity)
    {
		Intent i=new Intent();
		for(int j=0;j<Name.length;j++)
		{
		 Log.d("Data sent from DashBoard", "depName= "+Designation[j]+ "  "+"realNumber= "+extension[j]+"  "+"username= "+Name[j]+"  ");
		}
		if(activity==0)
		{
		 i = new Intent(this, EXT_Board.class);
		 i.putExtra("Name", Name);
	     i.putExtra("Designation", Designation);
	     i.putExtra("Extension",extension);
	
		}
		else if(activity==1)
		{
			 i = new Intent(this, Dialer_main.class);
			 i.putExtra("Name", Name);
		     i.putExtra("Designation", Designation);
		     i.putExtra("Extension",extension);
		    
		}
		else if(activity==2)
		{
			 i = new Intent(this, Settings_Password.class);
			 i.putExtra("Name", Name);
		     i.putExtra("Designation", Designation);
		     i.putExtra("Extension",extension);
		 
		}
		return i;
    } 
    private void launchChildActivity(int a)
    {
        startActivityForResult(setIntentDataForActivities(a), a);
    } 
    ///end

   
}