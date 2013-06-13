package com.fsck.k9.service.wappush;

import java.util.Date;

import com.fsck.k9.Account;
import com.fsck.k9.K9;
import com.fsck.k9.Preferences;
import com.fsck.k9.controller.MessagingController;
import com.fsck.k9.controller.MessagingListener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

// receive 'メール通知' application's notify intent.
// see 'http://orleaf.net/trac/wiki/android/android/mailnotify/developer'

public class MailNotifyReceiver extends BroadcastReceiver {
	private static final String ACTION_MAIL_PUSH_RECEIVED = "net.assemble.emailnotify.MAIL_PUSH_RECEIVED";
	private static final String EXTRA_MAILBOX = "mailbox";
	private static final String EXTRA_TIMESTAMP = "timestamp";
	private static final String EXTRA_COUNT = "count";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (!ACTION_MAIL_PUSH_RECEIVED.equals(intent.getAction())) {
			return;
		}
		if (!K9.receiveMailNotify()) {
			return;
		}
		String mailbox = intent.getStringExtra(EXTRA_MAILBOX);
		Date timestamp = intent.getParcelableExtra(EXTRA_TIMESTAMP);
		int count = intent.getIntExtra(EXTRA_COUNT, 0);

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
		MessagingController msgCtl = MessagingController.getInstance(K9.app) ;
		for (Account account : prefs.getAccounts()) {
			if (account.isMailCheckAtMailNotify()) {
				msgCtl.checkMail(context, account, true, true, new MessagingListener());
			}
		}
	}
}