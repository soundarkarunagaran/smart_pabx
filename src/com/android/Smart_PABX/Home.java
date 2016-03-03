package com.android.Smart_PABX;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;



import org.jwebsocket.api.WebSocketClientEvent;
import org.jwebsocket.api.WebSocketClientTokenListener;
import org.jwebsocket.api.WebSocketPacket;
import org.jwebsocket.client.plugins.rpc.Rpc;
import org.jwebsocket.client.plugins.rpc.RpcListener;
import org.jwebsocket.client.plugins.rpc.Rrpc;
import org.jwebsocket.client.token.BaseTokenClient;
import org.jwebsocket.kit.WebSocketException;
import org.jwebsocket.token.Token;
import org.jwebsocket.token.TokenFactory;







import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Home extends Activity implements WebSocketClientTokenListener{
	
	
	public EditText my_number=null;
	public EditText password=null;
	public CheckBox remember_me=null;
	public Button login=null;
	public Button Accept=null;
	public Button Reject=null;
	public TextView Incoming_number=null;
	public static String my_number_str=null;
	public static String password_str=null;
	public String remember_me_str=null;
	public String hashed_password=null;
	public  int CHECK_STATE=0;
	public String[] Name=null;
	public String[] Designation=null;
	public String[] extension=null;
	public static String yourName=null;
	public static String yourExtension=null;
	//stores the connection status
	public static Boolean connected=false;
	static final int CUSTOM_DIALOG_ID = 0;
	Dialog dialog = null;
	static public Token aToken;
	String caller_no;
	public Object[] mapData=new Object[39];
	EventDataSQLHelper eventsData;
	public Settings_Availability availability_obj;
	public Settings_Call_Routingg callRouting_obj;
	public Settings_Password password_obj;
	TimerTask mTimerTask;
	final Handler handler = new Handler();
	public static Timer timer = new Timer();	
	public static BaseTokenClient btc = new BaseTokenClient();//create a new instance of the base token client
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        my_number=(EditText)findViewById(R.id.home_screen_icon_my_number_e);
        password=(EditText)findViewById(R.id.home_screen_icon_password);
        remember_me=(CheckBox)findViewById(R.id.remember_me);
        login=(Button)findViewById(R.id.Login_button);
        eventsData = new EventDataSQLHelper(this);
        availability_obj = new Settings_Availability();
        callRouting_obj = new Settings_Call_Routingg();
        password_obj = new Settings_Password();
        deleteAll();
        
        
        //add the listener for the websocket connection
		btc.addListener(this);//add this class as a listener
		btc.addListener(new RpcListener());//add an rpc listener
		Rpc.setDefaultBaseTokenClient(btc);//set it to the default btc
		Rrpc.setDefaultBaseTokenClient(btc);//same here
		
		//set the connection status to "not connected" on startup
		changeConnectionStatus(false);
		connect();
		login();
        
    }
    
   
  	 @Override
  	 public boolean onKeyDown(int keyCode, KeyEvent event)  {
  	     if (Integer.parseInt(android.os.Build.VERSION.SDK) < 5
  	             && keyCode == KeyEvent.KEYCODE_BACK
  	             && event.getRepeatCount() == 0) {
  	         Log.d("CDA", "onKeyDown Called");
  	         onBackPressed();
  	     }

  	     return super.onKeyDown(keyCode, event);
  	 }

  	 @Override
  	 public void onBackPressed() {
  	    Log.d("CDA", "onBackPressed Called");
  	    Intent setIntent = new Intent(Intent.ACTION_MAIN);
  	    setIntent.addCategory(Intent.CATEGORY_HOME);
  	    setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
  	    startActivity(setIntent);
  	 }
  	
    
    @Override
	protected void onResume() {
		super.onResume();
		
		Log.e("Connection Staten onResume", btc.getConnectionStatus()+"");
	}

	@Override
	protected void onPause() {
		
		Log.e("Connection State onPause", btc.getConnectionStatus()+"");
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		super.onPause();
		
		Log.e("Connection State onStop", btc.getConnectionStatus()+"");
		
	}
	
	@Override
	protected void onDestroy()
	{
		super.onPause();
		
		Log.e("Connection State onDestroy", btc.getConnectionStatus()+"");
	}
	
	public void sendDbDataOnReconnection()
	{
		availability_obj.sendAvailabilityDataOnReConnection();
		callRouting_obj.sendCallRoutingDataOnReConnection();
		password_obj.sendPasswordDataOnReConnection();
	}
	
    
    public void login()
    {
    	///check box code starts
  		remember_me.setOnCheckedChangeListener(new OnCheckedChangeListener()
       {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
           {
               if ( isChecked )
              {
            	   CHECK_STATE=1;
                  // We know the Checkbox is selected or has been checked.
                  // Do something    
               }
               else
              {
            	   CHECK_STATE=0;
                   // We know the Checkbox is NOT selected or has been de-selected.
                  // Do something
               }
            }
          });
  		
  		
    	login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
		        my_number_str=my_number.getText().toString();
		        password_str=password.getText().toString();
		        
		        /*
		         * conversion of password to MD 5
		         */
		        hashed_password=md5(password_str);
				Token LoginToken = TokenFactory.createToken("com.ef.mobile_app.PabxServer","login");
				
                LoginToken.setString("real_no",my_number_str);//my number
                LoginToken.setString("pwd",hashed_password);//password
              
                LoginToken.setString("request", "login");
                try {
                	
                	btc.sendToken(LoginToken);//try to send the token to the server
        		} catch (WebSocketException ex) {
        			Log.i("Debug","+ex.getMessage()");//log errors
        		}
			}
		});
    }
    
    /*
     * Method to calculate MD5 
     *
     */
    
    public static String md5(String input) {
        
        String md5 = null;
         
        if(null == input) return null;
         
        try {
             
        //Create MessageDigest object for MD5
        MessageDigest digest = MessageDigest.getInstance("MD5");
         
        //Update input string in message digest
        digest.update(input.getBytes(), 0, input.length());
 
        //Converts message digest value in base 16 (hex)
        md5 = new BigInteger(1, digest.digest()).toString(16);
 
        } catch (NoSuchAlgorithmException e) {
 
            e.printStackTrace();
        }
        return md5;
    }
    
    /* Method is called when the connection status has changed
     * connected <-> disconnected */
    public static void changeConnectionStatus(Boolean isConnected) {
    	connected=isConnected;//change variable
  		if(isConnected){//if connection established
  			Log.i("Debug","successfully connected to server");
  			
  		}else{
  			Log.i("Debug","disconnected from Server!");
  			
  		}
  	}
    
    
    
    public void connect()
    {
    	//if(!connected){//if not connected
    		try{
    			
    			Log.i("Debug","connecting to "+"wss://192.168.10.121:9797/jWebSocket/jWebSocket"+" ...");
    			btc.open("wss://192.168.10.121:9797/jWebSocket/jWebSocket");//try to open the connection to your server
    		}catch(Exception e){
    			Log.i("Debug","Error while connecting...");//log errors
    		}
    	
    }
    
  
    
    ////////////checking the response
	/* Message handler is used to process incoming tokens from the server*/
	private Handler messageHandler = new Handler() {
		@Override
		public void handleMessage(Message aMessage) {
			if(aMessage.what==0){//if it's a token
				
				aToken =(Token) aMessage.obj;//get the received token
				Log.i("Token", aToken+"");
				
				     if(aToken.getType().equals("welcome"))
				     {
				    	 //Toast.makeText(getApplicationContext(), "hand shaking Done successfully", Toast.LENGTH_LONG).show();
				     }
					else if(aToken.getType().equals("login"))
					{
						int status = aToken.getInteger("status");
					
			    	    Log.i("VM",status+"");
						if(status==-2)
						{
						
						Toast.makeText(getApplicationContext(), "Invalid number entered", Toast.LENGTH_LONG).show();
						
						}
					
						else if(status==-3)
						{
						
						Toast.makeText(getApplicationContext(), "Invalid Password Entered", Toast.LENGTH_LONG).show();
						
						}
					
						else if(status==1)
						{
						
						Toast.makeText(getApplicationContext(), "Validated Successfully", Toast.LENGTH_LONG).show();
						
		                
		                /*
		                 * sending token to get personal pref
		                 */
		                
						boolean isNetworkConnected = checkNetworkConnectivity();
		    			if(isNetworkConnected)
		    			{
		                Token PersonalPrefToken = TokenFactory.createToken("com.ef.mobile_app.PabxServer","getPersonalPref");
						
		                PersonalPrefToken.setString("real_no",my_number_str);//my number
		                PersonalPrefToken.setString("request", "getPersonalPref");
		                	try {
		                	
		                		btc.sendToken(PersonalPrefToken);//try to send the token to the server
		                	} catch (WebSocketException ex) {
		                		Log.i("Debug","+ex.getMessage()");//log errors
		                	}
		    			}
		    			else
		    			{
		    				AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
		           		 	builder.setTitle("Alert");
		           		 	builder.setMessage("No Internet Connection");
		           		 	builder.setPositiveButton("OK", null);
		           		 	builder.setCancelable(true);
		           		 	builder.show();
		    			}
		               
		                
		                /*
		                 * sending token to get "Extension List"
		                 */
		             
		    			if(isNetworkConnected)
		    				{
		    					Token ExtensionListToken = TokenFactory.createToken("com.ef.mobile_app.PabxServer","getExtenList");
		    					ExtensionListToken.setString("real_no",my_number_str);//my number
		    					ExtensionListToken.setString("request", "getExtenList");
		                			try {
		                	
		                				btc.sendToken(ExtensionListToken);//try to send the token to the server
		                			} catch (WebSocketException ex) {
		                				Log.i("Debug","+ex.getMessage()");//log errors
		                			}
		                		
		    				}
		    				else
		    				{
		    				AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
	           		 		builder.setTitle("Alert");
	           		 		builder.setMessage("No Internet Connection");
	           		 		builder.setPositiveButton("OK", null);
	           		 		builder.setCancelable(true);
	           		 		builder.show();
		    				}
						
						}
					
						else if(status==-1)
						{
						
						Toast.makeText(getApplicationContext(), "Login Failed. Please try again", Toast.LENGTH_LONG).show();
						
						}
						
						
					}
					else if(aToken.getType().equals("getPersonalPref"))
					{
						
						Map<String,String> map = new HashMap<String,String>();
					     map = aToken.getMap("personal_pref");
						 int i=0;
						Log.d("Map=", map+"");
						Iterator iterator=map.entrySet().iterator();
				        while(iterator.hasNext()){
				            Map.Entry mapEntry=(Map.Entry)iterator.next();
				            Log.d("Map Data=","The key is: "+mapEntry.getKey()
				            		+ ",value is :"+mapEntry.getValue());
				            mapData[i]=mapEntry.getValue();
				            i++;
				        }
				        
				        for (int j=0;j<mapData.length;j++)
				        {
				        	Log.i("Map Data= ", mapData[j]+"");
				        	if(mapData[j]==null)
				        	{
				        		mapData[j]="null";
				        	}
				        }
				       
				        SQLiteDatabase db = eventsData.getWritableDatabase();
				        ContentValues values = new ContentValues();
				        Log.d("======", "");
				        values.put(EventDataSQLHelper.KEY,"1");
				        values.put(EventDataSQLHelper.virtExtension,mapData[0].toString());
				        values.put(EventDataSQLHelper.virtNumber, mapData[1].toString());
				        values.put(EventDataSQLHelper.realNumber,mapData[2].toString());
				        values.put(EventDataSQLHelper.userName,mapData[3].toString());
				        values.put(EventDataSQLHelper.userDesig,mapData[4].toString());
				        values.put(EventDataSQLHelper.deptID, mapData[5].toString());
				        values.put(EventDataSQLHelper.allowAdminCompanyVm,mapData[6].toString());
				        values.put(EventDataSQLHelper.allowAdminFeedback,mapData[7].toString());
				        values.put(EventDataSQLHelper.allowVM,mapData[8].toString());
				        values.put(EventDataSQLHelper.allowOutdial,mapData[9].toString());
				        values.put(EventDataSQLHelper.prefBusyAction, mapData[10].toString());
				        values.put(EventDataSQLHelper.prefBusyNextExtension,mapData[11].toString());
				        values.put(EventDataSQLHelper.prefDndAction, mapData[12].toString());
				        values.put(EventDataSQLHelper.prefDndNextExtension,mapData[13].toString());
				        values.put(EventDataSQLHelper.prefRejAction,mapData[14].toString());
				        values.put(EventDataSQLHelper.prefRejNextExtension,mapData[15].toString());
				        values.put(EventDataSQLHelper.status,mapData[16].toString());
				        values.put(EventDataSQLHelper.dt,mapData[17].toString());
				        values.put(EventDataSQLHelper.login,mapData[18].toString());
				        values.put(EventDataSQLHelper.allowObdMsgRec,mapData[19].toString());
				        values.put(EventDataSQLHelper.pwd,mapData[20].toString());
				        values.put(EventDataSQLHelper.isOwner,mapData[21].toString());
				        values.put(EventDataSQLHelper.role,mapData[22].toString());
				        Log.d("======", "");
				        values.put(EventDataSQLHelper.sessionID,mapData[23].toString());
				        values.put(EventDataSQLHelper.dndSchedType,mapData[24].toString());
				        values.put(EventDataSQLHelper.dndSchedStartMin,mapData[25].toString());
				        values.put(EventDataSQLHelper.dndSchedEndMin,mapData[26].toString());
				        values.put(EventDataSQLHelper.dndSchedSun,mapData[27].toString());
				        values.put(EventDataSQLHelper.dndSchedMon,mapData[28].toString());
				        values.put(EventDataSQLHelper.dndSchedTue,mapData[29].toString());
				        values.put(EventDataSQLHelper.dndSchedWed,mapData[30].toString());
				        values.put(EventDataSQLHelper.dndSchedThu,mapData[31].toString());
				        values.put(EventDataSQLHelper.dndSchedFri,mapData[32].toString());
				        values.put(EventDataSQLHelper.dndSchedSat,mapData[33].toString());
				        values.put(EventDataSQLHelper.chnlID,mapData[34].toString());
				        values.put(EventDataSQLHelper.asterikHost,mapData[35].toString());
				        values.put(EventDataSQLHelper.dialinReqStartTime,mapData[36].toString());
				        values.put(EventDataSQLHelper.allowIntercom,mapData[37].toString());
				        values.put(EventDataSQLHelper.allowFreeIntercom,mapData[38].toString());
				        db.insert(EventDataSQLHelper.TABLE, null, values);
				        Log.d("======", "");
				        Cursor cursor = getEvents();
				       
				        showEvents(cursor);
			
					}
				     
					else if(aToken.getType().equals("getExtenList"))
					{
						List list= aToken.getList("extension_list");
						Log.d("List=", list+"");
						Iterator it = list.iterator();
					    Map m;
					    int count=list.size();
					    Log.e("Size of List=  ", list.size()+"");
					    Designation=new String[count];
					    extension=new String[count];
					    Name=new String[count];
					    int i=0;
						while(it.hasNext()){
							m = (Map) it.next();
							String virtExten = (String) m.get("virt_exten");
							Designation[i] = (String) m.get("dept_name");
							extension[i] = (String) m.get("virt_exten");
							Name[i] = (String) m.get("user_name");
						   Log.e("Iterated List Data","depName= "+Designation[i]+ "  "+"realNumber= "+extension[i]+"  "+"username= "+Name[i]+"  ");
	                        i++;
						}
						
						
						try {
							Thread.sleep(4000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						launchChildActivity(0);
					
					}
					else if (aToken.getType().equals("get_dialin_action"))
					{
						caller_no= aToken.getString("caller_no");
						Toast.makeText(getApplicationContext(),"Caller_num= "+caller_no, Toast.LENGTH_LONG).show();
						
   						Intent intent = new Intent();
   						intent.setAction("com.android.Smart_PABX");
   						sendBroadcast(intent);
   						
					}
					
				
			}
			
		}
	};
    
 
	
	
	  void deleteAll()
	  {
	      SQLiteDatabase db= eventsData.getWritableDatabase();
	      db.delete(EventDataSQLHelper.TABLE, null, null);

	  }
	private Cursor getEvents() {
	    SQLiteDatabase db = eventsData.getReadableDatabase();
	    Cursor cursor = db.query(EventDataSQLHelper.TABLE, null, null, null, null, null, null);
	   // String sql="UPDATE"+" "+EventDataSQLHelper.TABLE+" "+"SET"+" "+EventDataSQLHelper.pwd+"="+"'Imrannn'"+" "+"WHERE"+" "+EventDataSQLHelper.KEY+"="+1;
	    //Log.e("query",sql.toString() );
	    startManagingCursor(cursor);
	    return cursor;
	  }
	
	private void showEvents(Cursor cursor) {
	    StringBuilder ret = new StringBuilder("Saved Events:\n\n");
	    while (cursor.moveToNext()) {
	      long id = cursor.getLong(1);
	      String pwd = cursor.getString(22);///pwd
	      long time = cursor.getLong(17);//status
	      long time1 = cursor.getLong(26);//availability start
	      long time2 = cursor.getLong(27);
	      long time3 = cursor.getLong(28);
	      long time4 = cursor.getLong(29);
	      long time5 = cursor.getLong(30);
	      long time6 = cursor.getLong(31);
	      long time7 = cursor.getLong(32);
	      long time8 = cursor.getLong(33);
	      long time9 = cursor.getLong(34);
	      long time10 = cursor.getLong(35);///availability end
	      
	      long time11 = cursor.getLong(12);
	      String title = cursor.getString(13);
	      long time12 = cursor.getLong(14);
	      String title1 = cursor.getString(15);
	      long time13 = cursor.getLong(16);
	      String title2 = cursor.getString(17);
	      yourName=cursor.getString(5);
	      yourExtension=cursor.getString(2);
	      //String old_pwd = cursor.getString(41);
	      ret.append(id+"  "+pwd + ": " + time + ": " + time1 + ": " + time2 + ": " + time3 + ": " + time4 + ": " + time5 
	    		     + ": " + time6 + ": " + time7  + ": " + time8  + ": "+ time9  + ": "+ time10  + "\n");
	      ret.append("\n\n\n");
	      ret.append(time11 + ": " + title + ": " + time12 + ": " + title1 + ": " + time13 + ": " + title2+":  ");
	      ret.append("\n\n\n");
	      ret.append("name= "+yourName+" extension= "+yourExtension);
	    }
	    Log.d("DB DATA ", ret.toString());
	    //output.setText(ret);
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
			//Toast.makeText(this, "wifi connection", Toast.LENGTH_LONG).show();
			isConnected =  true;
		}else if(mobile.isAvailable() && mobile.isConnected()){
			//Toast.makeText(this, "mobile connection", Toast.LENGTH_SHORT).show();
			isConnected = true;
		}else{
			//Toast.makeText(this, "no network avaialable", Toast.LENGTH_LONG).show();
			isConnected = false;
		}
		return isConnected;
	}
	
	/*
	 * function to launch the next activity i-e Dashboard.It will send data to next activity 
	 */
	private Intent setIntentDataForActivities(int activity)
    {
		for(int j=0;j<Name.length;j++)
		{
		 Log.d("Data sent from Home", "depName= "+Designation[j]+ "  "+"realNumber= "+extension[j]+"  "+"username= "+Name[j]+"  ");
		}
		 Intent i = new Intent(this, DashBoard.class);
        i.putExtra("Name", Name);
        i.putExtra("Designation", Designation);
        i.putExtra("Extension",extension);
        i.putExtra("yourName", yourName);
        i.putExtra("yourExtension", yourExtension);
        return(i);
    } 
    private void launchChildActivity(int a)
    {
        startActivityForResult(setIntentDataForActivities(a), a);
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