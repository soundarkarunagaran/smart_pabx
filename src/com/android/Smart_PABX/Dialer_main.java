package com.android.Smart_PABX;





import java.util.ArrayList;

import org.jwebsocket.api.WebSocketClientEvent;
import org.jwebsocket.api.WebSocketClientTokenListener;
import org.jwebsocket.api.WebSocketPacket;
import org.jwebsocket.token.Token;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Dialer_main extends Activity implements WebSocketClientTokenListener {

	public EditText numberField;
	public TextView yourName;
	public TextView yourExtension;
	public Button Call;
	public Button one;
	public Button two;
	public Button three;
	public Button four;
	public Button five;
	public Button six;
	public Button seven;
	public Button eight;
	public Button nine;
	public Button zero;
	public Button add;
	public Button delete;
	public Button hash;
	public Button star;
	public Button Contacts;
	public static String Number_entered="";
	StringBuilder buildNumberForCall = new StringBuilder("");
	public static String Extension="066";
	public static int userNumberAddedToPhoneBook=0;
	public String final_number=null;
	public static Boolean connected=false;
	Token aToken;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialer_main_layout);
        one = (Button) findViewById (R.id.one_dialer_pad_dialer_main_layout);
        two = (Button) findViewById (R.id.two_dialer_pad_dialer_main_layout);
        three = (Button) findViewById (R.id.three_dialer_pad_dialer_main_layout);
        four = (Button) findViewById (R.id.four_dialer_pad_dialer_main_layout);
        five = (Button) findViewById (R.id.five_dialer_pad_dialer_main_layout);
        six = (Button) findViewById (R.id.six_dialer_pad_dialer_main_layout);
        seven = (Button) findViewById (R.id.seven_dialer_pad_dialer_main_layout);
        eight = (Button) findViewById (R.id.eight_dialer_pad_dialer_main_layout);
        nine = (Button) findViewById (R.id.nine_dialer_pad_dialer_main_layout);
        zero = (Button) findViewById (R.id.zero_dialer_pad_dialer_main_layout);
        star = (Button) findViewById (R.id.star_dialer_pad_dialer_main_layout);
        hash = (Button) findViewById (R.id.hash_dialer_pad_dialer_main_layout);
        add = (Button) findViewById (R.id.add_dialer_pad_dialer_main_layout);
        delete = (Button) findViewById (R.id.delete_dialer_pad_dialer_main_layout);
        Call = (Button) findViewById (R.id.call_dialer_pad_dialer_main_layout);
        Contacts = (Button) findViewById (R.id.contacts_dialer_pad_dialer_main_layout);
        numberField=(EditText)findViewById(R.id.number_field_dialer_manin_layout);
        yourName=(TextView)findViewById (R.id.your_name_status_dialer_main_layout);
        yourExtension=(TextView)findViewById(R.id.your_extension_status_dialer_main_layout);
        yourName.setText(Home.yourName);
        yourExtension.setText(Home.yourExtension);
        Buttons();
        
    }
    @SuppressLint({ "ParserError", "ParserError", "ParserError" })
	public void Buttons()
    {
    	
    	
    	/*
    	 * Accessing contacts on clicking EditText
    	 */
    	Contacts.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				 Contacts.setBackgroundResource(R.drawable.dashboard_screen_button_1_hover);
				 Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
		         startActivityForResult(intent, 1);
			}
		});
    	/*
         * making call on button click
         * 
         */
        Call.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Call.setBackgroundResource(R.drawable.dashboard_screen_button_1_hover);
				 Number_entered=numberField.getText().toString(); 
			     final_number=Number_entered;
				 Intent call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+final_number));
				 startActivity(call);
			}
		});
        
        one.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				one.setBackgroundResource(R.drawable.dashboard_screen_button_1_hover);
				Number_entered=numberField.getText().toString(); 
				if(Number_entered.contains(Extension))
				{
					String one="1";
					buildNumberForCall.append(one);
				}
				else
				{
					String one="1";
				buildNumberForCall.append(Extension+one);
				}
				numberField.setText(buildNumberForCall);
			}
		});
        
        two.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				two.setBackgroundResource(R.drawable.dashboard_screen_button_1_hover);
				Number_entered=numberField.getText().toString(); 
				if(Number_entered.contains(Extension))
				{
					 String two="2";
					buildNumberForCall.append(two);
				}
				else
				{
					String two="2";
				buildNumberForCall.append(Extension+two);
				}
				numberField.setText(buildNumberForCall);
			}
		});
        
        three.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				three.setBackgroundResource(R.drawable.dashboard_screen_button_1_hover);
				Number_entered=numberField.getText().toString(); 
				if(Number_entered.contains(Extension))
				{
					String three="3";
					buildNumberForCall.append(three);
				}
				else
				{
					String three="3";
				    buildNumberForCall.append(Extension+three);
				}
				numberField.setText(buildNumberForCall);
			}
		});
        
        four.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				four.setBackgroundResource(R.drawable.dashboard_screen_button_1_hover);
				Number_entered=numberField.getText().toString(); 
				if(Number_entered.contains(Extension))
				{
					String four="4";
					buildNumberForCall.append(four);
				}
				else
				{
					String four="4";
				    buildNumberForCall.append(Extension+four);
				}
				numberField.setText(buildNumberForCall);
			}
		});
        
        five.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				five.setBackgroundResource(R.drawable.dashboard_screen_button_1_hover);
				Number_entered=numberField.getText().toString(); 
				if(Number_entered.contains(Extension))
				{
					String five="5";
					buildNumberForCall.append(five);
				}
				else
				{
					String five="5";
					buildNumberForCall.append(Extension+five);
				}
				numberField.setText(buildNumberForCall);
			}
		});
        
        six.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				six.setBackgroundResource(R.drawable.dashboard_screen_button_1_hover);
				Number_entered=numberField.getText().toString(); 
				if(Number_entered.contains(Extension))
				{
					String six="6";
					buildNumberForCall.append(six);
				}
				else
				{
					String six="6";
				    buildNumberForCall.append(Extension+six);
				}
				numberField.setText(buildNumberForCall);
			}
		});
        
        seven.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				seven.setBackgroundResource(R.drawable.dashboard_screen_button_1_hover);
				Number_entered=numberField.getText().toString(); 
				if(Number_entered.contains(Extension))
				{
					String seven="7";
					buildNumberForCall.append(seven);
				}
				else
				{
					String seven="7";
				    buildNumberForCall.append(Extension+seven);
				}
				numberField.setText(buildNumberForCall);
			}
		});
        
        eight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				eight.setBackgroundResource(R.drawable.dashboard_screen_button_1_hover);
				Number_entered=numberField.getText().toString(); 
				if(Number_entered.contains(Extension))
				{
					String eight= "8";
					buildNumberForCall.append(eight);
				}
				else
				{
					String eight="8";
					buildNumberForCall.append(Extension+eight);
				}
				numberField.setText(buildNumberForCall);
			}
		});
        
        nine.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				nine.setBackgroundResource(R.drawable.dashboard_screen_button_1_hover);
				Number_entered=numberField.getText().toString(); 
				if(Number_entered.contains(Extension))
				{
					String nine="9";
					buildNumberForCall.append(nine);
				}
				else
				{
					String nine="9";
					buildNumberForCall.append(Extension+nine);
				}
				numberField.setText(buildNumberForCall);
			}
		});
        
        zero.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				zero.setBackgroundResource(R.drawable.dashboard_screen_button_1_hover);
				Number_entered=numberField.getText().toString(); 
				if(Number_entered.contains(Extension))
				{
					String zero="0";
					buildNumberForCall.append(zero);
				}
				else
				{
					String zero="0";
				    buildNumberForCall.append(Extension+zero);
				}
				numberField.setText(buildNumberForCall);
			}
		});
        
        star.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				star.setBackgroundResource(R.drawable.dashboard_screen_button_1_hover);
				Number_entered=numberField.getText().toString(); 
				if(Number_entered.contains(Extension))
				{
					String star="*";
					buildNumberForCall.append(star);
				}
				else
				{
					String star="*";
					buildNumberForCall.append(Extension+star);
				}
				numberField.setText(buildNumberForCall);
			}
		});
        
        hash.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				hash.setBackgroundResource(R.drawable.dashboard_screen_button_1_hover);
				Number_entered=numberField.getText().toString(); 
				if(Number_entered.contains(Extension))
				{
					String hash="#";
					buildNumberForCall.append(hash);
				}
				else
				{
					String hash="#";
					buildNumberForCall.append(Extension+hash);
				}
				numberField.setText(buildNumberForCall);
			}
		});
        
        add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				add.setBackgroundResource(R.drawable.dashboard_screen_button_1_hover);
				String num_to_be_added = numberField.getText().toString();
				if(num_to_be_added.contains(Extension))
				{
					num_to_be_added=num_to_be_added.replace(Extension,"");
				}
				Intent intent = new Intent(ContactsContract.Intents.SHOW_OR_CREATE_CONTACT, Uri.parse("tel:" +num_to_be_added)); //currentNum is my TextView, you can replace it with the number directly such as Uri.parse("tel:1293827")
				intent.putExtra(ContactsContract.Intents.EXTRA_FORCE_CREATE, true); //skips the dialog box that asks the user to confirm creation of contacts
				startActivity(intent);

			}
		});
        
        delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				delete.setBackgroundResource(R.drawable.dashboard_screen_button_1_hover);
				/*
				 * delete digit from editText
				 */
				 if (numberField.getText().length() > 0) {
					 numberField.getText().delete(numberField.getText().length() - 1,
							 numberField.getText().length());
				    }
				 
				 if(numberField.getText().length()==0)
				 {
					 buildNumberForCall = new StringBuilder("");
				 }

			}
		});
        
    }
    
    /*
     * Accessing phone book function
     */
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        try {
            if (resultCode == Activity.RESULT_OK) {
            	 Log.e("Contacts", "first if");
                Uri contactData = data.getData();
                Cursor cur = managedQuery(contactData, null, null, null, null);
                ContentResolver contect_resolver = getContentResolver();

                if (cur.moveToFirst()) {
                	Log.e("Contacts", "2nd if");
                    String id = cur.getString(cur.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                    String name = "";
                    String no = "";

                    Cursor phoneCur = contect_resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);
                      
                    if (phoneCur.moveToFirst()) {
                        name = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        no = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }

                    Log.e("Phone no & name :***: ", name + " : " + no);
                    numberField.setText(Extension+no);
                    Toast.makeText(this,"number= "+no+"name="+name,Toast.LENGTH_LONG).show();
                    id = null;
                    name = null;
                    no = null;
                    phoneCur = null;
                }
                contect_resolver = null;
                cur = null;
          
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Log.e("IllegalArgumentException :: ", e.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error :: ", e.toString());
        }
    }
    
    /*
     *Adding contacts to phone Book 
     */
    private void createContact(String name, String phone) {
    	ContentResolver cr = getContentResolver();
    	
    	Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        
    	if (cur.getCount() > 0) {
        	while (cur.moveToNext()) {
        		String existName = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        		if (existName.contains(name)) {
                	Toast.makeText(Dialer_main.this,"The contact name: " + name + " already exists", Toast.LENGTH_SHORT).show();
                	return;        			
        		}
        	}
    	}
    	
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, "accountname@gmail.com")
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, "com.google")
                .build());
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
                .build());
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                .build());

        
        try {
			cr.applyBatch(ContactsContract.AUTHORITY, ops);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OperationApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	Toast.makeText(Dialer_main.this, "Created a new contact with name: " + name + " and Phone No: " + phone, Toast.LENGTH_SHORT).show();
    	
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