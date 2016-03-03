package com.android.Smart_PABX;





import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Settings_Password extends Activity  implements WebSocketClientTokenListener{

	public Button Dialer;
	public Button EXT_Board;
	public Button Settings;    
	public Button Settings_Password;
	public Button Settings_Availability;
	public Button Settings_Call_Routing;
	Button Retry;
	Button Cancel;
	public Button Change_password;
	public EditText old_password;
	public EditText new_password;
	public EditText confirm_password;
	public TextView yourName;
	public TextView yourExtension;
	public String old_password_str=null;
	public String new_password_str=null;
	public String confirm_password_str=null;
	public String hashed_old_password=null;
	public String hashed_new_password=null;
	public String real_number="123";
	public static Boolean connected=false;
	static final int CUSTOM_DIALOG_ID = 0;
	Dialog dialog = null;
	public String[] Name=null;
	public String[] Designation=null;
	public String[] extension=null;
	EventDataSQLHelper eventsData;
	public int DATA_SENT = 0;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        eventsData = new EventDataSQLHelper(this);
        Dialer = (Button) findViewById(R.id.Dialer_dialer_bottom_tabs);
        EXT_Board = (Button) findViewById(R.id.EXT_Board_dialer_bottom_tabs);
        Settings = (Button) findViewById(R.id.settings_dialer_bottom_tabs);
        Settings_Password = (Button) findViewById(R.id.password_settings_top_tabs);
        Settings_Availability= (Button) findViewById (R.id.Availability_settings_top_tabs);
        Settings_Call_Routing= (Button) findViewById (R.id.Call_routing_settings_top_tabs);
        Change_password = (Button) findViewById (R.id.change_password_settings);
        old_password = (EditText) findViewById(R.id.old_password_settings);
        new_password = (EditText) findViewById(R.id.new_password_settings);
        confirm_password=(EditText)findViewById(R.id.confirm_new_password_settings);
        Home.btc.addListener(this);//add this class as a listener
		Home.btc.addListener(new RpcListener());//add an rpc listener
		Rpc.setDefaultBaseTokenClient(Home.btc);//set it to the default btc
		Rrpc.setDefaultBaseTokenClient(Home.btc);//same here
		yourName=(TextView)findViewById (R.id.your_name_status_settings);
        yourExtension=(TextView)findViewById(R.id.your_extension_status_settings);
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
		//editTextDynamicSettings();
        buttons();
    }
  
    public void editTextDynamicSettings()
    {
    	old_password.setOnTouchListener(new View.OnTouchListener(){
    	    public boolean onTouch(View view, MotionEvent motionEvent) {
    	    	
    	    	old_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    	        return false;
    	    }
    	});
    	
    	new_password.setOnTouchListener(new View.OnTouchListener(){
    	    public boolean onTouch(View view, MotionEvent motionEvent) {
    	
    	    	new_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    	        return false;
    	    }
    	});
    	
    	confirm_password.setOnTouchListener(new View.OnTouchListener(){
    	    public boolean onTouch(View view, MotionEvent motionEvent) {
    	    	//old_password.setText("");
    	    	confirm_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    	        return false;
    	    }
    	});


    }
    public void buttons()
    {
    	Change_password.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				old_password_str=old_password.getText().toString();
				new_password_str=new_password.getText().toString();
				confirm_password_str=confirm_password.getText().toString();
				
				if(!old_password_str.matches(Home.password_str))
				{
					
					showDialog(CUSTOM_DIALOG_ID);
				}
					if(new_password_str.matches(confirm_password_str))
						{
							hashed_old_password=md5(old_password_str);
							hashed_new_password=md5(new_password_str);
					
							Token ChangePasswordToken = TokenFactory.createToken("com.ef.mobile_app.PabxServer","updatePwd");
					
							ChangePasswordToken.setString("real_no",Home.my_number_str);//my number
							ChangePasswordToken.setString("old_pwd",hashed_old_password);//old password
							ChangePasswordToken.setString("new_pwd",hashed_new_password);//old password
							ChangePasswordToken.setString("request", "updatePwd");
							try {
	                	
								Home.btc.sendToken(ChangePasswordToken);//try to send the token to the server
							} catch (WebSocketException ex) {
								Log.i("Debug","+ex.getMessage()");//log errors
							}
						}
					else
						{
							Toast.makeText(getApplicationContext(), "New password does not match Confirm password", Toast.LENGTH_LONG).show();
						}
			
			}
		});
    	
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
 			Log.i("Debug","successfully connected to server");//log
 			//conBtn.setText("DISCONNECT");//change Buttontext
 		}else{
 			Log.i("Debug","disconnected from Server!");//log
 			//conBtn.setText("CONNECT");//change Buttontext
 		}
 	}
   
 
	private Handler messageHandler = new Handler() {
		@Override
		public void handleMessage(Message aMessage) {
			if(aMessage.what==0){
				
				Token aToken =(Token) aMessage.obj;
				Log.i("Token", aToken+"");
				
				     if(aToken.getType().equals("welcome"))
				     {
				    	 
				    	 
				     }					
				     else if(aToken.getType().equals("updatePwd"))					
				     {
						int status = aToken.getInteger("status");
						
						if(status==1)
						{
						    
							Toast.makeText(getApplicationContext(), "Password Updated Successfully", Toast.LENGTH_LONG).show();
					        Cursor cursor =updatePassword();
					        showEvents(cursor);
						}
						
						if(status==-1)
						{
						
							Toast.makeText(getApplicationContext(), "Password  not Updated", Toast.LENGTH_LONG).show();
					
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
	
	
    
	/*
	 * Function for updating password
	 */
	
	   private Cursor updatePassword() {
		    SQLiteDatabase db = eventsData.getReadableDatabase();
		    Cursor cursor = db.query(EventDataSQLHelper.TABLE, null, null, null, null, null, null);
		    String sql="UPDATE"+" "+EventDataSQLHelper.TABLE+" "+"SET"+" "+EventDataSQLHelper.oldPwd+"='"+hashed_old_password+"',"+EventDataSQLHelper.pwd+"='"+hashed_new_password+"' "+"WHERE"+" "+EventDataSQLHelper.KEY+"="+1;
		    Log.e("query",sql.toString() );
		    cursor = db.rawQuery(sql,null);
		    startManagingCursor(cursor);
		    return cursor;
		  }
	   
	   public void sendPasswordDataOnReConnection()
	    {
	    		if(DATA_SENT==2)
	    		{
	    			Cursor cursor = getEvents();
			        showEvents(cursor);
			        Token ChangePasswordToken = TokenFactory.createToken("com.ef.mobile_app.PabxServer","updatePwd");
					
					ChangePasswordToken.setString("real_no",Home.my_number_str);//my number
					ChangePasswordToken.setString("old_pwd",hashed_old_password);//old password
					ChangePasswordToken.setString("new_pwd",hashed_new_password);//old password
					ChangePasswordToken.setString("request", "updatePwd");
					try {
            	
						Home.btc.sendToken(ChangePasswordToken);//try to send the token to the server
					} catch (WebSocketException ex) {
						Log.i("Debug","+ex.getMessage()");//log errors
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
		      long id = cursor.getLong(1);
		     
		      hashed_new_password=cursor.getString(22);///pwd
		      hashed_old_password=cursor.getString(41);
		      ret.append(id+"  "+hashed_new_password + ": ");
		      ret.append("\n\n\n");
		    }
		    Log.d("Updated Password", ret.toString());
		  }
	/*
	 * Displaying Error message dialog
	 */
   ///start
	@Override
	protected Dialog onCreateDialog(int id) {
	 // TODO Auto-generated method stub
		 //Dialog dialog = null;
		 switch(id) {
		 case CUSTOM_DIALOG_ID:
	     dialog = new Dialog(Settings_Password.this);
	 
	     dialog.setContentView(R.layout.error_message);
	     dialog.setTitle("                  Login Failer !");
	     
	     Retry = (Button) dialog.findViewById (R.id.retry_error_message);
	     Cancel = (Button) dialog.findViewById (R.id.cancel_error_message);
	    
	     
	     Retry.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					
				}
			});
	     Cancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
	     
	        break;
	    }
	    return dialog;
	}
   ///end	
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