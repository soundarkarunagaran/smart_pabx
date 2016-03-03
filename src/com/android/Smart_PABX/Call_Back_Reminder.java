package com.android.Smart_PABX;




import java.util.Timer;
import java.util.TimerTask;

import org.jwebsocket.kit.WebSocketException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Call_Back_Reminder extends Activity{
	//public Button Dialer;
	//public Button EXT_Board;
	//public Button Settings;
	public Button Call;
	public Button Snooze;
	public Button cancel_reminder;
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
	Timer timer = new Timer();	
	
    /** Called when the activity is first created. */
    @SuppressLint("ParserError")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_back_reminder);
       // Dialer = (Button) findViewById(R.id.Dialer);
       // EXT_Board = (Button) findViewById(R.id.EXT_Board);
       // Settings = (Button) findViewById(R.id.settings);
        Call = (Button)findViewById(R.id.call_call_back_reminder);
        Snooze = (Button) findViewById (R.id.snooze_call_back_reminder);
        cancel_reminder = (Button) findViewById (R.id.cancel_callback_call_back_reminder);
        yourName=(TextView)findViewById (R.id.your_name_status_call_back_reminder);
        yourExtension=(TextView)findViewById(R.id.your_extension_status_call_back_reminder);
        yourName.setText(Home.yourName);
        yourExtension.setText(Home.yourExtension);
        Buttons();
    }
    
    
    public void Buttons()
    {
    	
    	Snooze.setOnClickListener(new View.OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				Snooze.setBackgroundResource(R.drawable.dashboard_screen_button_1_hover);
 				Intent  Call_Back= new Intent(v.getContext(),Call_Back.class);
				startActivity(Call_Back);
 			}
 		});
    	
    	Call.setOnClickListener(new View.OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				 Intent call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+DashBoard.caller_no));
				 startActivity(call);
 			}
 		});
    	
    	cancel_reminder.setOnClickListener(new View.OnClickListener() {
 			@Override
 			public void onClick(View v) {
 			  cancel_reminder.setBackgroundResource(R.drawable.dashboard_screen_button_1_hover);
 			  Call_Back.timer.cancel();
 			}
 		});
    }
    
   
    
    /*
	 * function to launch the next activity i-e Extension Board.It will send data to next activity 
	 */
	///start
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