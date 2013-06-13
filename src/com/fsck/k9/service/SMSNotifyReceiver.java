package com.fsck.k9.service;

import java.util.Date;

import com.fsck.k9.Account;
import com.fsck.k9.K9;
import com.fsck.k9.Preferences;
import com.fsck.k9.controller.MessagingController;
import com.fsck.k9.controller.MessagingListener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class SMSNotifyReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle extras = intent.getExtras();
		String mailbox = extras.getString("mailbox") ;
		Date timestamp = (Date)extras.get("timestamp") ;
		int count = extras.getInt("count",0) ;

		String email = "";
		if (mailbox != null) {
			String [] tmp = mailbox.split(":",2);
			if (tmp.length > 1) {
				email = tmp[1];
			}
			else {
				email = mailbox;
			}
		}
		Preferences prefs = Preferences.getPreferences(context);
		Account account = null;
		for (Account a : prefs.getAccounts()) {
			if (mailbox == null || a.getEmail().equals(email)) {
				account = a;
				break;
			}
		}
		MessagingController msgCtl = MessagingController.getInstance(K9.app) ;
		msgCtl.checkMail(context, account, true, true, new MessagingListener());
	}
}