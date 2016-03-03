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

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class Settings_Call_Routingg extends Activity  implements WebSocketClientTokenListener,OnItemSelectedListener{

	public Button Dialer;
	public Button EXT_Board;
	public Button Settings;
	public Button Settings_Password;
	public Button Settings_Availability;
	public Button Settings_Call_Routing;
	public Spinner busyExtensionSpinner;
	public Spinner notAnsweredSpinner;
	public Spinner notAvailableSpinner;
	public String busyExtension_str=null;
	public String notAnswered_str=null;
	public String notAvailable_str=null;
	public int busyExtension_num=0;
	public static String busyExtension_value="";
	public int notAvailable_num=0;
	public static String notAvailable_value="";
	public int notAnswered_num=0;
	public static String notAnswered_value="";
	public Button submit;
	public static Boolean connected=false;
	public String[] Name=null;
	public String[] Designation=null;
	public String[] extension=null;
	public String[] busyExtensionstr={"When my extension is busy","Move Call to Extension","Take Caller to Voice Mail","Park Call in Queue"};
	public String[] notAnsweredstr={"When call is not Answered","Move Call to Extension","Take Caller to Voice Mail"};
	public String[] notAvailablestr={"When I am not Available","Move Call to Extension","Take Caller to Voice Mail"};
	
	EventDataSQLHelper eventsData;
	public int DATA_SENT =0;
	
	public static int CALL_ROUTING_FLAG=1;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_call_routingg);
        eventsData = new EventDataSQLHelper(this);
        Dialer = (Button) findViewById(R.id.Dialer_dialer_bottom_tabs);
        EXT_Board = (Button) findViewById(R.id.EXT_Board_dialer_bottom_tabs);
        Settings = (Button) findViewById(R.id.settings_dialer_bottom_tabs);
        Settings_Password = (Button) findViewById(R.id.password_settings_top_tabs);
        Settings_Availability= (Button) findViewById (R.id.Availability_settings_top_tabs);
        Settings_Call_Routing= (Button) findViewById (R.id.Call_routing_settings_top_tabs);
        busyExtensionSpinner = (Spinner)findViewById(R.id.spinner_busyExtension);
        notAnsweredSpinner = (Spinner)findViewById(R.id.spinner_notAnswered);
        notAvailableSpinner = (Spinner)findViewById(R.id.spinner_notAvailable);
        ///
        ArrayAdapter<String> busyExtension = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,busyExtensionstr);
        busyExtension.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //busyExtension.add("When my extension is busy");
        busyExtensionSpinner.setAdapter(busyExtension); 
        ///
        ///
        ArrayAdapter<String> notAnswered = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,notAnsweredstr);
        notAnswered.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //notAnswered.add("When call is not Answered");
        notAnsweredSpinner.setAdapter(notAnswered); 
        ///
        ArrayAdapter<String> notAvailable = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,notAvailablestr);
        notAvailable.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //notAvailable.add("When I am not Available");
        notAvailableSpinner.setAdapter(notAvailable); 
        ///
        
        busyExtensionSpinner.setOnItemSelectedListener(this);
        notAnsweredSpinner.setOnItemSelectedListener(this);
        notAvailableSpinner.setOnItemSelectedListener(this);
        
        //////////////////////////////////////////////
        submit = (Button) findViewById (R.id.submit_call_routingg);
        Home.btc.addListener(this);//add this class as a listener
		Home.btc.addListener(new RpcListener());//add an rpc listener
		Rpc.setDefaultBaseTokenClient(Home.btc);//set it to the default btc
		Rrpc.setDefaultBaseTokenClient(Home.btc);//same here
		Intent i=this.getIntent();
        Bundle b=i.getExtras();
        Name=b.getStringArray("Name");
        Designation=b.getStringArray("Designation");
        extension=b.getStringArray("Extension");
        for(int j=0;j<Name.length;j++)
        {
        Log.d("Data obtained in EXT_Board", "depName= "+Designation[j]+ "  "+"realNumber= "+extension[j]+"  "+"username= "+Name[j]+"  ");
        }
       // RadioButtons();
        Buttons();
    }
    
    public void onItemSelected(AdapterView<?> parent, View v, int position,long id) {

    	Spinner spnir = (Spinner)parent;
        if(spnir.getId() == R.id.spinner_busyExtension)
        {
        	
        	busyExtension_str=busyExtensionstr[position];
        	if(busyExtension_str.matches("Move Call to Extension"))
   			{
   				busyExtension_num=1;
   				CALL_ROUTING_FLAG=2;
			    launchChildActivity(0);
   			
   			}
   			if(busyExtension_str.matches("Take Caller to Voice Mail"))
   			{
   				busyExtension_num=2;
   			
   			}
   			if(busyExtension_str.matches("Park Call in Queue"))
   			{
   				busyExtension_num=3;
   			}
        }
        else if(spnir.getId() == R.id.spinner_notAnswered)
        {
        	//notAnsweredstr[0]="Move Call to Extension";
        	//Toast.makeText(getApplicationContext(),notAnsweredstr[position],Toast.LENGTH_LONG).show();
        	notAnswered_str=notAnsweredstr[position];
        	if(notAnswered_str.matches("Move Call to Extension"))
   			{
   				notAnswered_num=4;
   				CALL_ROUTING_FLAG=3;
			    launchChildActivity(0);
   			
   			}
   			if(notAnswered_str.matches("Take Caller to Voice Mail"))
   			{
   				notAnswered_num=5;
   				
   			}
        }
        else if(spnir.getId() == R.id.spinner_notAvailable)
        {
        	//notAvailablestr[0]="Move Call to Extension";
        	//Toast.makeText(getApplicationContext(),notAvailablestr[position],Toast.LENGTH_LONG).show();
        	notAvailable_str=notAvailablestr[position];
        	if(notAvailable_str.matches("Move Call to Extension"))
   			{
   				notAvailable_num=6;
   				CALL_ROUTING_FLAG=4;
			    launchChildActivity(0);
   			}
   			if(notAvailable_str.matches("Take Caller to Voice Mail"))
   			{
   				notAvailable_num=7;
   				
   			}
        }

	}

	public void onNothingSelected(AdapterView<?> parent) {
		//selection.setText("");
	}
    
    /*
	 * Click listener for RadioButtons
	 */
	
    /*RadioButton.OnClickListener myOptionOnClickListener =
			   new RadioButton.OnClickListener()
			  {

			  @Override
			  public void onClick(View v) {
				  if(parkCallBE.isChecked())
				  {
					  parkCallBE.setChecked(true);
					  takeCallerToVmNAn.setChecked(false);
					  takeCallerToVmNA.setChecked(false);
				  }
				  else
				  {
					  parkCallBE.setChecked(false);
				  }
				
				  
			   // TODO Auto-generated method stub
			   Toast.makeText(Settings_Call_Routingg.this,
			     "Option 1 : " + moveCallToNextExtensionBE.isChecked() + "\n"+
			     "Option 2 : " + takeCallerToVmBE.isChecked() + "\n" +
			     "Option 3 : " + parkCallBE.isChecked()+"\n"+
			     "Option 4 : " + moveCallToNextExtensionNAn.isChecked() + "\n"+
			     "Option 5 : " + takeCallerToVmNAn.isChecked() + "\n"+
			     "Option 6 : " + moveCallToNextExtensionNA.isChecked() + "\n"+
			     "Option 7 : " + takeCallerToVmNA.isChecked() + "\n" ,
			     Toast.LENGTH_LONG).show();
			   
			   if(moveCallToNextExtensionBE.isChecked()==true)
			   {
				    CALL_ROUTING_FLAG=2;
				    launchChildActivity(0);
			   }
			   if(moveCallToNextExtensionNAn.isChecked()==true)
			   {
				    CALL_ROUTING_FLAG=3;
				    launchChildActivity(0);
			   }
			   if(moveCallToNextExtensionNA.isChecked()==true)
			   {
				    CALL_ROUTING_FLAG=4;
				    launchChildActivity(0);
			   }
			   

			  }
			   
			   
			  };*/
    
    public void RadioButtons()
    {
    	
    	submit.setOnClickListener(new View.OnClickListener() {
   		 
   		@Override
   		public void onClick(View v) 
   			{
   		        // get selected radio button from radioGroup
   			
   			
   			/*
   			 * busy extension case
   			 */
   			
   			if(busyExtension_str.matches("Move Call to Extension"))
   			{
   				busyExtension_num=1;
   			
   			}
   			if(busyExtension_str.matches("Take Caller to Voice Mail"))
   			{
   				busyExtension_num=2;
   			
   			}
   			if(busyExtension_str.matches("Park Call in Queue"))
   			{
   				busyExtension_num=3;
   			}
   			
   			/*
   			 * Not Answer Case
   			 * 
   			 */
   			
   			if(notAnswered_str.matches("Move Call to Extension"))
   			{
   				notAnswered_num=4;
   			
   			}
   			if(notAnswered_str.matches("Take Caller to Voice Mail"))
   			{
   				notAnswered_num=5;
   				
   			}
   			
   			/*
   			 * Not Available Case
   			 */
   			
   			if(notAvailable_str.matches("Move Call to Extension"))
   			{
   				notAvailable_num=6;
   				
   			}
   			if(notAvailable_str.matches("Take Caller to Voice Mail"))
   			{
   				notAvailable_num=7;
   				
   			}
   			
   			
   			/*
   			 * Sending Command to server about call routing
   			 */
   			
   			Token callRoutingToken = TokenFactory.createToken("com.ef.mobile_app.PabxServer","updateDialinPref");
   			callRoutingToken.setString("real_no",Home.my_number_str);
   			callRoutingToken.setInteger("busy_action", busyExtension_num);
   			callRoutingToken.setString("busy_next_action", busyExtension_value);
   			callRoutingToken.setInteger("dnd_action", notAnswered_num);
   			callRoutingToken.setString("dnd_next_action", notAnswered_value);
   			callRoutingToken.setInteger("rej_action", notAvailable_num);
   			callRoutingToken.setString("rej_next_action", notAvailable_value);
			
            try {
            	
            	Home.btc.sendToken(callRoutingToken);//try to send the token to the server
            
    		} catch (WebSocketException ex) {
    			Log.i("Debug",ex.getMessage().toString());//log errors
    		}
   			}
    
    	});
    	
    }
    
    /*
     * function to update data in DB
     */
    public Cursor call_routing()
    {
  	/////
  	    SQLiteDatabase db = eventsData.getReadableDatabase();
  	    Cursor cursor = db.query(EventDataSQLHelper.TABLE, null, null, null, null, null, null);
  	    String sql="UPDATE"+" "+EventDataSQLHelper.TABLE+" "+"SET"+" "+EventDataSQLHelper.prefBusyAction+"='"+busyExtension_num+"',"+EventDataSQLHelper.prefBusyNextExtension+"='"+busyExtension_value+"',"+EventDataSQLHelper.prefDndAction+"='"+notAnswered_num+"',"+EventDataSQLHelper.prefDndNextExtension+"='"+notAnswered_value+"',"+EventDataSQLHelper.prefRejAction+"='"+notAvailable_num+"',"+EventDataSQLHelper.prefRejNextExtension+"='"+notAvailable_value+"' "+"WHERE"+" "+BaseColumns._ID+"="+1;
  	    Log.e("query",sql.toString() );
  	    return cursor;
    }
    public void sendCallRoutingDataOnReConnection()
    {
    		if(DATA_SENT==2)
    		{
    			Cursor cursor = getEvents();
		        showEvents(cursor);
		        Token callRoutingToken = TokenFactory.createToken("com.ef.mobile_app.PabxServer","updateDialinPref");
	   			callRoutingToken.setString("real_no",Home.my_number_str);
	   			callRoutingToken.setInteger("busy_action", busyExtension_num);
	   			callRoutingToken.setString("busy_next_action", busyExtension_value);
	   			callRoutingToken.setInteger("dnd_action", notAnswered_num);
	   			callRoutingToken.setString("dnd_next_action", notAnswered_value);
	   			callRoutingToken.setInteger("rej_action", notAvailable_num);
	   			callRoutingToken.setString("rej_next_action", notAvailable_value);
				
	            try {
	            	
	            	Home.btc.sendToken(callRoutingToken);//try to send the token to the server
	    		} catch (WebSocketException ex) {
	    			Log.i("Debug",ex.getMessage().toString());//log errors
	    		}
    		}
    	
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
	    	  
	    	  busyExtension_num = cursor.getInt(12);
	    	  busyExtension_value = cursor.getString(13);
	    	  notAnswered_num = cursor.getInt(14);
	    	  notAnswered_value = cursor.getString(15);
	    	  notAvailable_num = cursor.getInt(16);
	    	  notAvailable_value = cursor.getString(17);

	      ret.append(busyExtension_num + ": " + busyExtension_value + ": " + notAnswered_num +  ": " + notAnswered_value +  ": " + notAvailable_num +  ": " + notAvailable_value +   "\n");
	    }
	    Log.d("Availability Data= ", ret.toString());
	    //output.setText(ret);
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
    				
    				launchChildActivity(2);
    			}
    		});
        	
        	Settings_Availability.setOnClickListener(new View.OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				
    				launchChildActivity(3);
    			}
    		});
        	
        	Settings_Call_Routing.setOnClickListener(new View.OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				
    				launchChildActivity(4);
    			}
    		});

    }
    
    
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
    
    
   	private Handler messageHandler = new Handler() {
   		@Override
   		public void handleMessage(Message aMessage) {
   			if(aMessage.what==0){
   				
   				Token aToken =(Token) aMessage.obj;
   				Log.i("Token", aToken+"");
   				
   				if(aToken.getType().equals("welcome"))
				{
	    	 
	    	 
				}else if(aToken.getType().equals("updateDialinPref")){
					int status = aToken.getInteger("status");
			
					if(status==1)
					{
			
						Toast.makeText(getApplicationContext(), "Update Dialin Preference Updated Successfully", Toast.LENGTH_LONG).show();
						DATA_SENT=1;
				
					}
			
					if(status==-1)
					{
			
						Toast.makeText(getApplicationContext(), "Update Dialin Preference  not Updated ", Toast.LENGTH_LONG).show();
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
       
       //////end
    
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