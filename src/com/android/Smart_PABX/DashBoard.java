package com.android.Smart_PABX;




import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DashBoard extends Activity implements WebSocketClientTokenListener {
	public Button Dialer;
	public Button EXT_Board;
	public Button Settings;
	public String[] Name=null;
	public String[] Designation=null;
	public String[] extension=null;
	public String yourName=null;
	public String yourExtension=null;
	public static Boolean connected=false;
	static final int CUSTOM_DIALOG_ID = 0;
	public Button Accept=null;
	public Button Reject=null;
	public TextView Incoming_number=null;
	Dialog dialog = null;
	public static Token aToken;
	public static String caller_no;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dash_board);
        Dialer = (Button) findViewById(R.id.Dialer);
        EXT_Board = (Button) findViewById(R.id.EXT_Board);
        Settings = (Button) findViewById(R.id.settings);
        Home.btc.addListener(this);//add this class as a listener
		Home.btc.addListener(new RpcListener());//add an rpc listener
		Rpc.setDefaultBaseTokenClient(Home.btc);//set it to the default btc
		Rrpc.setDefaultBaseTokenClient(Home.btc);//same here
		Log.e("Connection Staten onCreate", Home.btc.getConnectionStatus()+"");
        Buttons();
        
        
        /*
         * Getting the data passed from Home activity
         */
        Intent i=this.getIntent();
        Bundle b=i.getExtras();
        Name=b.getStringArray("Name");
        Designation=b.getStringArray("Designation");
        extension=b.getStringArray("Extension");
        yourName=b.getString("yourName");
        yourExtension=b.getString("yourExtension");
        Log.e("name & extension", yourName+"   "+yourExtension);
        for(int j=0;j<Name.length;j++)
        {
        Log.d("Data obtained in Dashboard", "depName= "+Designation[j]+ "  "+"realNumber= "+extension[j]+"  "+"username= "+Name[j]+"  ");
        }
    }
    
  
    
    public void Buttons()
    {
    	
    	Dialer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Dialer.setBackgroundResource(R.drawable.dashboard_screen_button_1_hover);
				launchChildActivity(1);
				
			}
		});
    	
    	EXT_Board.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EXT_Board.setBackgroundResource(R.drawable.dashboard_screen_button_1_hover);
				launchChildActivity(0);
			}
		});
    	
    	Settings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Settings.setBackgroundResource(R.drawable.dashboard_screen_button_1_hover);
				launchChildActivity(2);
				
			}
		});
    	
    }
 
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
   					else if (aToken.getType().equals("get_dialin_action"))
   					{
   						caller_no= aToken.getString("caller_no");
   						Toast.makeText(getApplicationContext(),"Caller_num DashBoard= "+caller_no, Toast.LENGTH_LONG).show();
   						
   						
   						Intent intent = new Intent();
   						intent.setAction("com.android.Smart_PABX");
   						sendBroadcast(intent);
   					
   						
   					}
   					
   				
   			}
   			
   		}
   	};
   	
  
       
   
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
   		Message lMsg = new Message();//create a new mess
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