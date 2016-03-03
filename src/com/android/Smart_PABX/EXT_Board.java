package com.android.Smart_PABX;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jwebsocket.api.WebSocketClientEvent;
import org.jwebsocket.api.WebSocketClientTokenListener;
import org.jwebsocket.api.WebSocketPacket;
import org.jwebsocket.kit.WebSocketException;
import org.jwebsocket.token.Token;
import org.jwebsocket.token.TokenFactory;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

public class EXT_Board extends ListActivity  implements WebSocketClientTokenListener{

	public Button Dialer;
	public Button EXT_Board;
	public Button Settings; 
	public Button Refresh;
	public TextView yourName;
	public TextView yourExtension;
	public ListView Ext_Board_List=null;
	private ArrayAdapter<String> listAdapter ;
	public String[] Name=null;
	public String[] Designation=null;
	public String[] extension=null;
	public String[] Name_list=null;
	public String[] Designation_list=null;
	public String[] extension_list=null;	
	public static Boolean connected=false;
	Token aToken;
	public Object[] mapData=new Object[39];
	EventDataSQLHelper eventsData;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ext_board); 
        Dialer = (Button) findViewById(R.id.Dialer_dialer_bottom_tabs);
        EXT_Board = (Button) findViewById(R.id.EXT_Board_dialer_bottom_tabs);
        Settings = (Button) findViewById(R.id.settings_dialer_bottom_tabs);
        Ext_Board_List = (ListView) findViewById( android.R.id.list);
        Refresh = (Button) findViewById (R.id.refresh_ext_board);
        Ext_Board_List.setFastScrollEnabled(true);
        yourName=(TextView)findViewById (R.id.your_name_status_ext_board);
        yourExtension=(TextView)findViewById(R.id.your_extension_status_ext_board);
        yourName.setText(Home.yourName);
        yourExtension.setText(Home.yourExtension);
        Buttons();
        
        /*
         * Getting the data passed from Home activity
         */
        Intent i=this.getIntent();
        Bundle b=i.getExtras();
        Name=b.getStringArray("Name");
        Designation=b.getStringArray("Designation");
        extension=b.getStringArray("Extension");
        for(int j=0;j<Name.length;j++)
        {
        Log.d("Data obtained in EXT_Board", "depName= "+Designation[j]+ "  "+"realNumber= "+extension[j]+"  "+"username= "+Name[j]+"  ");
        }
        LinkedList<String> mLinked = new LinkedList<String>();
		for (int l = 0; l < Name.length; l++) {
			mLinked.add(Name[l]+"     "+"                  "+Designation[l]+"                             "+extension[l]);
		}

		setListAdapter(new MyListAdaptor(this, mLinked));
		 Ext_Board_List.setFastScrollEnabled(true);

		 Ext_Board_List.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				if(Settings_Call_Routingg.CALL_ROUTING_FLAG==2)
				{
					Settings_Call_Routingg.busyExtension_value=extension[position];
					Toast.makeText(getApplicationContext(),Settings_Call_Routingg.busyExtension_value,Toast.LENGTH_LONG).show();
					Toast.makeText(getApplicationContext(),Settings_Call_Routingg.notAnswered_value,Toast.LENGTH_LONG).show();
					Toast.makeText(getApplicationContext(),Settings_Call_Routingg.notAvailable_value,Toast.LENGTH_LONG).show();
					launchChildActivity(3);
					finish();
				}
				else if(Settings_Call_Routingg.CALL_ROUTING_FLAG==3)
				{
					Settings_Call_Routingg.notAnswered_value=extension[position];
					Toast.makeText(getApplicationContext(),Settings_Call_Routingg.notAnswered_value,Toast.LENGTH_LONG).show();
					Toast.makeText(getApplicationContext(),Settings_Call_Routingg.busyExtension_value,Toast.LENGTH_LONG).show();
					Toast.makeText(getApplicationContext(),Settings_Call_Routingg.notAvailable_value,Toast.LENGTH_LONG).show();
					launchChildActivity(3);
					finish();
				}
				else if(Settings_Call_Routingg.CALL_ROUTING_FLAG==4)
				{
					Settings_Call_Routingg.notAvailable_value=extension[position];
					Toast.makeText(getApplicationContext(),Settings_Call_Routingg.notAvailable_value,Toast.LENGTH_LONG).show();
					Toast.makeText(getApplicationContext(),Settings_Call_Routingg.notAnswered_value,Toast.LENGTH_LONG).show();
					Toast.makeText(getApplicationContext(),Settings_Call_Routingg.busyExtension_value,Toast.LENGTH_LONG).show();
					
					launchChildActivity(3);
					finish();
				}
				else
				{
				Intent call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+extension[position]));
				 startActivity(call);
				}
			}
		});
        ///ends
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
    	
    	Refresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
                Token PersonalPrefToken = TokenFactory.createToken("com.ef.mobile_app.PabxServer","getPersonalPref");
				
                PersonalPrefToken.setString("real_no",Home.my_number_str);//my number
                PersonalPrefToken.setString("request", "getPersonalPref");
                	try {
                	
                		Home.btc.sendToken(PersonalPrefToken);//try to send the token to the server
                	} catch (WebSocketException ex) {
                		Log.i("Debug","+ex.getMessage()");//log errors
                	}
               
                Token ExtensionListToken = TokenFactory.createToken("com.ef.mobile_app.PabxServer","getExtenList");
				
                ExtensionListToken.setString("real_no",Home.my_number_str);//my number
                ExtensionListToken.setString("request", "getExtenList");
                	try {
                	
                		Home.btc.sendToken(ExtensionListToken);//try to send the token to the server
                	} catch (WebSocketException ex) {
                		Log.i("Debug","+ex.getMessage()");//log errors
                	}
                
			}
		});
    	
    	

    }
    
    /*
     * Adapter for quick alphabetical search
     */
    ////start/////////
    class MyListAdaptor extends ArrayAdapter<String> implements SectionIndexer {

    	HashMap<String, Integer> alphaIndexer;
    	String[] sections;

    	public MyListAdaptor(Context context, LinkedList<String> items) {
    		super(context, R.layout.row_in_listview, items);

		alphaIndexer = new HashMap<String, Integer>();
		int size = items.size();

		for (int x = 0; x < size; x++) {
			String s = items.get(x);

			// get the first letter of the store
			String ch = s.substring(0, 1);
			// convert to uppercase otherwise lowercase a -z will be sorted
			// after upper A-Z
			ch = ch.toUpperCase();

			// HashMap will prevent duplicates
			alphaIndexer.put(ch, x);
		}

		Set<String> sectionLetters = alphaIndexer.keySet();

		// create a list from the set to sort
		ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);

		Collections.sort(sectionList);

		sections = new String[sectionList.size()];

		sectionList.toArray(sections);
    	}

	public int getPositionForSection(int section) {
		return alphaIndexer.get(sections[section]);
	}

	public int getSectionForPosition(int position) {
		return 0;
	}

	public Object[] getSections() {
		return sections;
	}
  }
    
    ////end//////////
    
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
				        }
				       
					}
				     
					else if(aToken.getType().equals("getExtenList"))
					{
						List list= aToken.getList("extension_list");
						Log.d("List=", list+"");
						Iterator it = list.iterator();
					    Map m;
					    int count=list.size();
					    Log.e("Size of List=  ", list.size()+"");
					    Designation_list=new String[count];
					    extension_list=new String[count];
					    Name_list=new String[count];
					    int i=0;
						while(it.hasNext()){
							m = (Map) it.next();
							String virtExten = (String) m.get("virt_exten");
							Designation_list[i] = (String) m.get("dept_name");
							extension_list[i] = (String) m.get("real_no");
							Name_list[i] = (String) m.get("user_name");
						    Log.e("Iterated List Data","depName= "+Designation_list[i]+ "  "+"realNumber= "+extension_list[i]+"  "+"username= "+Name_list[i]+"  ");
	                        i++;
						}
						
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
	    /////
	    String sql="UPDATE"+" "+EventDataSQLHelper.TABLE+" "+"SET"+" "+EventDataSQLHelper.pwd+"="+"'Imrannn'"+" "+"WHERE"+" "+EventDataSQLHelper.KEY+"="+1;
	    
	    Log.e("query",sql.toString() );
	    startManagingCursor(cursor);
	    return cursor;
	  }
	
	private void showEvents(Cursor cursor) {
	    StringBuilder ret = new StringBuilder("Saved Events:\n\n");
	    while (cursor.moveToNext()) {
	      long id = cursor.getLong(1);
	      String pwd = cursor.getString(22);///pwd
	      long time = cursor.getLong(17);//status
	      long time1 = cursor.getLong(25);//availability start
	      long time2 = cursor.getLong(26);
	      long time3 = cursor.getLong(27);
	      long time4 = cursor.getLong(28);
	      long time5 = cursor.getLong(29);
	      long time6 = cursor.getLong(30);
	      long time7 = cursor.getLong(31);
	      long time8 = cursor.getLong(32);
	      long time9 = cursor.getLong(33);
	      long time10 = cursor.getLong(34);///availability end
	      
	      long time11 = cursor.getLong(11);
	      String title = cursor.getString(12);
	      long time12 = cursor.getLong(13);
	      String title1 = cursor.getString(14);
	      long time13 = cursor.getLong(15);
	      String title2 = cursor.getString(16);
	      ret.append(id+"  "+pwd + ": " + time + ": " + time1 + ": " + time2 + ": " + time3 + ": " + time4 + ": " + time5 
	    		     + ": " + time6 + ": " + time7  + ": " + time8  + ": "+ time9  + ": "+ time10  + "\n");
	      ret.append("\n\n\n");
	      ret.append(time11 + ": " + title + ": " + time12 + ": " + title1 + ": " + time13 + ": " + title2);
	    }
	    Log.d("DB DATA ", ret.toString());

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