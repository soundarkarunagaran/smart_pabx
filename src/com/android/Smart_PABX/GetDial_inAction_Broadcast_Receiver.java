package com.android.Smart_PABX;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class GetDial_inAction_Broadcast_Receiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
			if(action.equalsIgnoreCase("com.android.Smart_PABX"))
			{

				 Intent GetDialinAction = new Intent(context,Calling_one.class);
				 GetDialinAction.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				 context.startActivity(GetDialinAction);
		    }
		
	}
} 