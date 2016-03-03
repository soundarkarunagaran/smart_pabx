package com.android.Smart_PABX;





import org.jwebsocket.api.WebSocketClientEvent;
import org.jwebsocket.api.WebSocketClientTokenListener;
import org.jwebsocket.api.WebSocketPacket;
import org.jwebsocket.token.Token;

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
import android.widget.Toast;

public class Dialer extends Activity implements WebSocketClientTokenListener {

	public EditText Number;
	public Button Call;
	public String Number_entered=null;
	public static String Extension="066";
	public String final_number=null;
	public static Boolean connected=false;
	Token aToken;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialer);
        Number=(EditText)findViewById(R.id.number_field_dialer_manin_layout);
        Call = (Button) findViewById(R.id.call);
        /*
         * making call on button click
         * 
         */
        Call.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				 Number_entered=Number.getText().toString(); 
			     final_number=Extension+Number_entered;
				Intent sIntent = new Intent(Intent.ACTION_CALL, Uri
						.parse("tel:"+final_number));
				startActivity(sIntent);
			}
		});
        
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