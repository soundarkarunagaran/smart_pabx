package com.android.Smart_PABX;





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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Calling_one extends Activity implements WebSocketClientTokenListener {

	//public Button Dialer;
	//public Button EXT_Board;
	//public Button Settings;
	public Button Accept;
	public Button Reject;
	public Button Silent;
	public Button Forward_to_next_extension;
	public Button Forward_to_VM;
	public Button Forwrd_Operator;
	public Button Call_Back;
	public TextView yourName;
	public TextView yourExtension;
	@SuppressLint("ParserError")
	public int status=0;
	public static Boolean connected=false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calling_one);
        //Dialer = (Button) findViewById(R.id.Dialer_dialer_bottom_tabs);
        //EXT_Board = (Button) findViewById(R.id.EXT_Board_dialer_bottom_tabs);
        //Settings = (Button) findViewById(R.id.settings_dialer_bottom_tabs);
        Accept = (Button) findViewById (R.id.Accept_call_one);
        Reject = (Button) findViewById (R.id.Reject_call_one);
        Silent = (Button) findViewById (R.id.Silent_call_one);
        Forward_to_next_extension = (Button) findViewById (R.id.forward_to_next_extension_call_one);
        Forward_to_VM = (Button) findViewById (R.id.forward_to_VM_call_one);
        Forwrd_Operator = (Button) findViewById (R.id.forward_to_operator_call_one);
        Call_Back = (Button) findViewById (R.id.call_back_call_one);
        Home.btc.addListener(this);//add this class as a listener
		Home.btc.addListener(new RpcListener());//add an rpc listener
		Rpc.setDefaultBaseTokenClient(Home.btc);//set it to the default btc
		Rrpc.setDefaultBaseTokenClient(Home.btc);//same here
		yourName=(TextView)findViewById (R.id.your_name_status_calling_one);
        yourExtension=(TextView)findViewById(R.id.your_extension_status_calling_one);
        yourName.setText(Home.yourName);
        yourExtension.setText(Home.yourExtension);
        Buttons();
    }
   
    public void Buttons()
    {
    	/*
    	Dialer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent  Dialer= new Intent(v.getContext(),Dialer.class);
				startActivity(Dialer);
			}
		});
    	
    	EXT_Board.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent EXT_Board = new Intent(v.getContext(),EXT_Board.class);
				startActivity(EXT_Board);
			}
		});
    	
    	Settings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent Settings = new Intent(v.getContext(),Settings_Password.class);
				startActivity(Settings);
			}
		});
    	*/
    	////////////////////////////////////////
    	Accept.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				status=0;
				Token getDialInToken = TokenFactory.createToken("com.ef.mobile_app.PabxServer","get_dialin_action");
				
				//getDialInToken.setInteger("status",status);//my number
				//getDialInToken.setString("request","get_dialin_action");
				DashBoard.aToken.setInteger("status",status);//my number
				DashBoard.aToken.setString("request","get_dialin_action");
                try {
                	
                	//Home.btc.sendToken(getDialInToken);//try to send the token to the server
                	Home.btc.sendToken(DashBoard.aToken);
                	//finish();
        		} catch (WebSocketException ex) {
        			Log.i("Debug","+ex.getMessage()");//log errors
        		}
			}
		});
    	
    	Reject.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Reject.setBackgroundResource(R.drawable.dashboard_screen_button_1_hover);
				status=10;
				Token getDialInToken = TokenFactory.createToken("com.ef.mobile_app.PabxServer","get_dialin_action");
				
				DashBoard.aToken.setInteger("status",status);//my number
				DashBoard.aToken.setString("request","get_dialin_action");
                try {
                	
                	Home.btc.sendToken(DashBoard.aToken);//try to send the token to the server
                	//finish();
        		} catch (WebSocketException ex) {
        			Log.i("Debug","+ex.getMessage()");//log errors
        		}
			}
		});
    	Silent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Silent.setBackgroundResource(R.drawable.dashboard_screen_button_1_hover);
				status=11;
				Token getDialInToken = TokenFactory.createToken("com.ef.mobile_app.PabxServer","get_dialin_action");
				
				DashBoard.aToken.setInteger("status",status);//my number
				DashBoard.aToken.setString("request","get_dialin_action");
                try {
                	
                	Home.btc.sendToken(DashBoard.aToken);//try to send the token to the server
                	//finish();
        		} catch (WebSocketException ex) {
        			Log.i("Debug","+ex.getMessage()");//log errors
        		}
			}
		});
    	Forward_to_next_extension.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Forward_to_next_extension.setBackgroundResource(R.drawable.dashboard_screen_button_1_hover);
				status=12;
				Token getDialInToken = TokenFactory.createToken("com.ef.mobile_app.PabxServer","get_dialin_action");
				
				DashBoard.aToken.setInteger("status",status);//my number
				DashBoard.aToken.setString("request","get_dialin_action");
                try {
                	
                	Home.btc.sendToken(DashBoard.aToken);//try to send the token to the server
                	//finish();
        		} catch (WebSocketException ex) {
        			Log.i("Debug","+ex.getMessage()");//log errors
        		}
			}
		});
    	Forward_to_VM.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Forward_to_VM.setBackgroundResource(R.drawable.dashboard_screen_button_1_hover);
				status=13;
				Token getDialInToken = TokenFactory.createToken("com.ef.mobile_app.PabxServer","get_dialin_action");
				
				DashBoard.aToken.setInteger("status",status);//my number
				DashBoard.aToken.setString("request","get_dialin_action");
                try {
                	
                	Home.btc.sendToken(DashBoard.aToken);//try to send the token to the server
                	//finish();
        		} catch (WebSocketException ex) {
        			Log.i("Debug","+ex.getMessage()");//log errors
        		}
			}
		});
    	
    	Forwrd_Operator.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Forwrd_Operator.setBackgroundResource(R.drawable.dashboard_screen_button_1_hover);
				status=14;
				Token getDialInToken = TokenFactory.createToken("com.ef.mobile_app.PabxServer","get_dialin_action");
				
				DashBoard.aToken.setInteger("status",status);//my number
				DashBoard.aToken.setString("request","get_dialin_action");
                try {
                	
                	Home.btc.sendToken(DashBoard.aToken);//try to send the token to the server
                	//finish();
        		} catch (WebSocketException ex) {
        			Log.i("Debug","+ex.getMessage()");//log errors
        		}
			}
		});
    	
    	Call_Back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Call_Back.setBackgroundResource(R.drawable.dashboard_screen_button_1_hover);
				status=1000;
				Intent  Call_Back= new Intent(v.getContext(),Call_Back.class);
				startActivity(Call_Back);
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
			
    				if (aToken.getType().equals("get_dialin_action"))
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