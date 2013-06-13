package com.fsck.k9.service.wappush;

import com.fsck.k9.Account;
import com.fsck.k9.K9;
import com.fsck.k9.Preferences;
import com.fsck.k9.controller.MessagingController;
import com.fsck.k9.controller.MessagingListener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WapPushReceiver extends BroadcastReceiver {

	public final static String ACTION_WAP_PUAH_RECEIVED = "android.provider.Telephony.WAP_PUSH_RECEIVED";
	@Override
	public void onReceive(Context context, Intent intent) {
		if (!ACTION_WAP_PUAH_RECEIVED.equals(intent.getAction())) {
			return;
		}
		if (!K9.receiveWapPush()) {
			return;
		}
		Preferences prefs = Preferences.getPreferences(context);
		MessagingController msgCtl = MessagingController.getInstance(K9.app) ;
		for (Account account : prefs.getAccounts()) {
			if (account.isMailCheckAtWapPush()) {
				msgCtl.checkMail(context, account, true, true, new MessagingListener());
			}
		}
	}
}
