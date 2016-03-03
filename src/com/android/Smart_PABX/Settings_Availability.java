package com.android.Smart_PABX;


import org.jwebsocket.api.WebSocketClientEvent;
import org.jwebsocket.api.WebSocketClientTokenListener;
import org.jwebsocket.api.WebSocketPacket;
import org.jwebsocket.client.plugins.rpc.Rpc;
import org.jwebsocket.client.plugins.rpc.RpcListener;
import org.jwebsocket.client.plugins.rpc.Rrpc;
import org.jwebsocket.kit.WebSocketException;
import org.jwebsocket.token.Token;
import org.jwebsocket.token.TokenFactory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TimePicker;
import android.widget.Toast;

@SuppressLint("ParserError")
public class Settings_Availability extends Activity implements WebSocketClientTokenListener{

	public Button Dialer;
	public Button EXT_Board;
	public Button Settings;
	public Button Settings_Password;
	public Button Settings_Availability;
	public Button Settings_Call_Routing;
	public TextView yourName;
	public TextView yourExtension;
	public CheckBox Available=null;
	public CheckBox unAvailable=null;
	public CheckBox mon=null;
	public CheckBox tue=null;
	public CheckBox wed=null;
	public CheckBox thu=null;
	public CheckBox fri=null;
	public CheckBox sat=null;
	public CheckBox sun=null;
	public String realNo =null;
    public int availabiltyType=0;
    public int startTime =0;
    public int endTime =0;
    public int monDay =-100; 
    public int tuesDay =-100; 
    public int wednesDay =-100; 
    public int thursDay =-100; 
    public int friDay =-100;
    public int saturDay =-100; 
    public int sunDay =-100;
    private TimePicker timePickerTo;
    private TimePicker timePickerFrom;
    public int start_time=0;
    public int end_time=0;
    public Button Save;
    public static Boolean connected=false;
    public String[] Name=null;
	public String[] Designation=null;
	public String[] extension=null;
	EventDataSQLHelper eventsData;
	public int DATA_SENT=0;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_availability);
        eventsData = new EventDataSQLHelper(this);
        Dialer = (Button) findViewById(R.id.Dialer_dialer_bottom_tabs);
        EXT_Board = (Button) findViewById(R.id.EXT_Board_dialer_bottom_tabs);
        Settings = (Button) findViewById(R.id.settings_dialer_bottom_tabs);
        Settings_Password = (Button) findViewById(R.id.password_settings_top_tabs);
        Settings_Availability= (Button) findViewById (R.id.Availability_settings_top_tabs);
        Settings_Call_Routing= (Button) findViewById (R.id.Call_routing_settings_top_tabs);
        Available = (CheckBox) findViewById (R.id.check_box_available_settings_availability);
        mon=(CheckBox) findViewById (R.id.mon_settings_availability);
        tue=(CheckBox) findViewById (R.id.tue_settings_availability);
        wed=(CheckBox) findViewById (R.id.wed_settings_availability);
        thu=(CheckBox) findViewById (R.id.thu_settings_availability);
        fri=(CheckBox) findViewById (R.id.fri_settings_availability);
        sat=(CheckBox) findViewById (R.id.Sat_settings_availability);
        sun=(CheckBox) findViewById (R.id.Sun_settings_availability);
        unAvailable = (CheckBox) findViewById (R.id.unavailable_settings_availability);
        timePickerTo=(TimePicker) findViewById (R.id.timePicker1);
        timePickerTo.setIs24HourView(true);
        timePickerFrom=(TimePicker) findViewById (R.id.timePicker2);
        timePickerFrom.setIs24HourView(true);
        Save=(Button) findViewById(R.id.save_settings_availability);
        Home.btc.addListener(this);//add this class as a listener
		Home.btc.addListener(new RpcListener());//add an rpc listener
		Rpc.setDefaultBaseTokenClient(Home.btc);//set it to the default btc
		Rrpc.setDefaultBaseTokenClient(Home.btc);//same here
		yourName=(TextView)findViewById (R.id.your_name_status_settings_availability);
        yourExtension=(TextView)findViewById(R.id.your_extension_status_settings_availability);
        yourName.setText(Home.yourName);
        yourExtension.setText(Home.yourExtension);
		Intent i=this.getIntent();
		Bundle b=i.getExtras();
        Name=b.getStringArray("Name");
        Designation=b.getStringArray("Designation");
        extension=b.getStringArray("Extension");
        for(int j=0;j<Name.length;j++)
        {
        Log.d("Data obtained in EXT_Board", "depName= "+Designation[j]+ "  "+"realNumber= "+extension[j]+"  "+"username= "+Name[j]+"  ");
        }
        checkBoxes();
        TimerPickers();
        Buttons();
    }
    
   
    
    public void TimerPickers()
    {
    	
    	Save.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("ParserError")
			@Override
			public void onClick(View v) {
				start_time=(timePickerTo.getCurrentHour()*60)+timePickerTo.getCurrentMinute();
				end_time=(timePickerFrom.getCurrentHour()*60)+timePickerFrom.getCurrentMinute();
				Token AvailabilityPrefToken = TokenFactory.createToken("com.ef.mobile_app.PabxServer","updateAvailabiltyPref");
				AvailabilityPrefToken.setString("real_no",Home.my_number_str);
				AvailabilityPrefToken.setInteger("start_time",start_time);
				AvailabilityPrefToken.setInteger("end_time",end_time);
				AvailabilityPrefToken.setInteger("availabilty_type",availabiltyType);
				AvailabilityPrefToken.setInteger("mon_day",monDay);
				AvailabilityPrefToken.setInteger("tues_day",tuesDay);
				AvailabilityPrefToken.setInteger("wed_day",wednesDay);
				AvailabilityPrefToken.setInteger("thurs_day",thursDay);
				AvailabilityPrefToken.setInteger("fri_day",friDay);
				AvailabilityPrefToken.setInteger("sat_day",saturDay);
				AvailabilityPrefToken.setInteger("sun_day",sunDay);
				try {
        	
					Home.btc.sendToken(AvailabilityPrefToken);//try to send the token to the server
					
				} catch (WebSocketException ex) {
					Log.i("Debug","+ex.getMessage()");//log errors
				}
			}
		});

    }
    
    ////////////checking the response
    /* Message handler is used to process incoming tokens from the server*/
    	private Handler messageHandler = new Handler() {
    		@Override
    		public void handleMessage(Message aMessage) {
    			if(aMessage.what==0){//if it's a token
		
    				Token aToken =(Token) aMessage.obj;//get the received token
    				Log.i("Token", aToken+"");
		
    				if(aToken.getType().equals("welcome"))
    				{
		    	 
		    	 
    				}else if(aToken.getType().equals("updateAvailabiltyPref")){
    					int status = aToken.getInteger("status");
				
    					if(status==1)
    					{
				
    						Toast.makeText(getApplicationContext(), "Availability Preference Updated Successfully", Toast.LENGTH_LONG).show();
    						DATA_SENT=1;
    
 		
    					}
				
    					if(status==-1)
    					{
				
    						Toast.makeText(getApplicationContext(), "Availability Preference not Updated", Toast.LENGTH_LONG).show();
    						DATA_SENT=2;
    					}
    				}
    				
    				else if (aToken.getType().equals("get_dialin_action"))
					{
						String caller_no= aToken.getString("caller_no");
						Toast.makeText(getApplicationContext(),"Caller_num= "+caller_no, Toast.LENGTH_LONG).show();
   						Intent intent = new Intent();
   						intent.setAction("com.android.Smart_PABX");
   						sendBroadcast(intent);
					}
			
    			}
	
    		}
    	};
    	
    	
    
    public void checkBoxes()
    {
    	Available.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
               {
                	availabiltyType=100;
                   
                }
                else
               {
                	availabiltyType=-100;
                   
                }
             }
           });
    	
   
    	unAvailable.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
               {
                	availabiltyType=-100;
                   
                }
                else
               {
                	availabiltyType=100;
                   
                }
             }
           });
    	
    	////mon
    	mon.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
               {
                	monDay=100;
                   
                }
                else
               {
                	monDay=-100;
                   
                }
             }
           });
    	
    	///tue
    	tue.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
               {
                	tuesDay=100;
                  
                }
                else
               {
                	tuesDay=-100;
                    
                }
             }
           });
    	
    	///wed
    	wed.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
               {
                	wednesDay=100;
                   
                }
                else
               {
                	wednesDay=-100;
                    
                }
             }
           });
    	
    	///thu
    	thu.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
               {
                	thursDay=100;
                  
                }
                else
               {
                	thursDay=-100;
                  
                }
             }
           });
    	
    	///fri
    	fri.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
               {
                	friDay=100;
                  
                }
                else
               {
                	friDay=-100;
                  
                }
             }
           });
    	
    	///sat
    	sat.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
               {
                	saturDay=100;
                   
                }
                else
               {
                	saturDay=-100;
                   
                }
             }
           });
    	
    	///sun
    	sun.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
               {
                	sunDay=100;
                  
                }
                else
               {
                	sunDay=-100;
                   
                }
             }
           });
    }
    /*
     * function to update availability pref
     */
    public Cursor update_availability()
    {
  	  /////
  	    SQLiteDatabase db = eventsData.getReadableDatabase();
  	    Cursor cursor = db.query(EventDataSQLHelper.TABLE, null, null, null, null, null, null);
  	    String sql="UPDATE"+" "+EventDataSQLHelper.TABLE+" "+"SET"+" "+EventDataSQLHelper.dndSchedType+"='"+availabiltyType+"',"+EventDataSQLHelper.dndSchedMon+"='"+monDay+"',"+EventDataSQLHelper.dndSchedTue+"='"+tuesDay+"',"+EventDataSQLHelper.dndSchedWed+"='"+wednesDay+"',"+EventDataSQLHelper.dndSchedThu+"='"+thursDay+"',"+EventDataSQLHelper.dndSchedFri+"='"+friDay+"',"+EventDataSQLHelper.dndSchedSat+"='"+saturDay+"',"+EventDataSQLHelper.dndSchedSun+"='"+sunDay+"',"+EventDataSQLHelper.dndSchedStartMin+"='"+start_time+"',"+EventDataSQLHelper.dndSchedEndMin+"='"+end_time+"' "+"WHERE"+" "+EventDataSQLHelper.KEY+"="+1;
  	    Log.e("query",sql.toString() );
  	    cursor = db.rawQuery(sql,null);
  	    ////
  	    return cursor;
    }
    
    /*
     * function to send data on reconnection
     */
   
    public void sendAvailabilityDataOnReConnection()
    {
    		if(DATA_SENT==2)
    		{
    			Cursor cursor = getEvents();
		        showEvents(cursor);
				Token AvailabilityPrefToken = TokenFactory.createToken("com.ef.mobile_app.PabxServer","updateAvailabiltyPref");
				AvailabilityPrefToken.setString("real_no",Home.my_number_str);//my number
				AvailabilityPrefToken.setInteger("start_time",start_time);
				AvailabilityPrefToken.setInteger("end_time",end_time);
				AvailabilityPrefToken.setInteger("availabilty_type",availabiltyType);
				AvailabilityPrefToken.setInteger("mon_day",monDay);
				AvailabilityPrefToken.setInteger("tues_day",tuesDay);
				AvailabilityPrefToken.setInteger("wed_day",wednesDay);
				AvailabilityPrefToken.setInteger("thurs_day",thursDay);
				AvailabilityPrefToken.setInteger("fri_day",friDay);
				AvailabilityPrefToken.setInteger("sat_day",saturDay);
				AvailabilityPrefToken.setInteger("sun_day",sunDay);
				try {
					
					Home.btc.sendToken(AvailabilityPrefToken);//try to send the token to the server
							
				} catch (WebSocketException ex) {
					Log.i("Debug","+ex.getMessage()");//log errors
				}
    		}
    	
    }
   
    
    /*
	 * function to check the network connectivity
	 */
	public boolean checkNetworkConnectivity(){
		boolean isConnected = false;
		final ConnectivityManager connMgr = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
		final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if(wifi.isAvailable() && wifi.isConnected()){
			
			isConnected =  true;
		}else if(mobile.isAvailable() && mobile.isConnected()){
			
			isConnected = true;
		}else{
			
			isConnected = false;
		}
		return isConnected;
	}
	
	 private Cursor getEvents() {
		    SQLiteDatabase db = eventsData.getReadableDatabase();
		    Cursor cursor = db.query(EventDataSQLHelper.TABLE, null, null, null, null,
		        null, null);
		    
		    startManagingCursor(cursor);
		    return cursor;
		  }

	 private void showEvents(Cursor cursor) {
		    StringBuilder ret = new StringBuilder("Saved Events:\n\n");
		    while (cursor.moveToNext()) {  
		    	  availabiltyType = cursor.getInt(26);
			      startTime = cursor.getInt(27);
			      endTime = cursor.getInt(28);
			      sunDay = cursor.getInt(29);
			      monDay = cursor.getInt(30);
			      tuesDay = cursor.getInt(31);
			      wednesDay = cursor.getInt(32);
			      thursDay = cursor.getInt(33);
			      friDay = cursor.getInt(34);
			      saturDay = cursor.getInt(35);
		     
		     
		      ret.append(availabiltyType + ": " + startTime + ": " + endTime +  ": " + sunDay +  ": " + monDay +  ": " + tuesDay +  ": " + wednesDay +  ": " + thursDay+ ":" +friDay+ " : "+saturDay + "\n");
		    }
		    Log.d("Availability Data= ", ret.toString());
		 
		  }
	
   
    public void Buttons()
    {
     	Dialer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {	
				launchChildActivity(1);
			}
		});
    	
    	EXT_Board.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				launchChildActivity(0);
			}
		});
    	
    	Settings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				launchChildActivity(2);
			}
		});
    	
    	Settings_Password.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Settings_Password.setBackgroundResource(R.drawable.orange_tab_hover);
				launchChildActivity(2);
			}
		});
    	
    	Settings_Availability.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Settings_Availability.setBackgroundResource(R.drawable.orange_tab_hover);
				launchChildActivity(3);
			}
		});
    	
    	Settings_Call_Routing.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Settings_Call_Routing.setBackgroundResource(R.drawable.orange_tab_hover);
				launchChildActivity(4);
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
   		else if(activity==3)
   		{
   			 i = new Intent(this, Settings_Availability.class);
   			 i.putExtra("Name", Name);
   		     i.putExtra("Designation", Designation);
   		     i.putExtra("Extension",extension);
   		    
   		}
   		else if(activity==4)
   		{
   			 i = new Intent(this, Settings_Call_Routingg.class);
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
    
    /* Method is called when the connection status has changed
     * connected <-> disconnected */
    public static void changeConnectionStatus(Boolean isConnected) {
    	connected=isConnected;//change variable
  		if(isConnected){//if connection established
  			Log.i("Debug","successfully connected to server");//log
  			//conBtn.setText("DISCONNECT");//change Buttontext
  		}else{
  			Log.i("Debug","disconnected from Server!");//log
  			//conBtn.setText("CONNECT");//change Buttontext
  		}
  	}

    @Override
	public void processOpened(WebSocketClientEvent aEvent) {
    	changeConnectionStatus(true);//when connected change the status
	}
    
    @Override
	public void processClosed(WebSocketClientEvent aEvent) {
    	changeConnectionStatus(false);//when disconnected change the status
	}
    
	@Override
	public void processToken(WebSocketClientEvent aEvent, Token aToken) {

		Message lMsg = new Message();
		lMsg.what = 0;
		lMsg.obj = aToken;
		messageHandler.sendMessage(lMsg);
	}
	

	@Override
	public void processPacket(WebSocketClientEvent aEvent, WebSocketPacket aPacket) {
	}
	
	@Override
	public void processOpening(WebSocketClientEvent aEvent) {
	}

	@Override
	public void processReconnecting(WebSocketClientEvent aEvent) {
	}
}