package com.lbconsulting.alist_03.classes;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

public class ListsTableObserver extends ContentObserver {

	public ListsTableObserver(Handler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
	}

	// The second method is only available from API level 16 onwards. 
	// The code works fine on older devices, but in this case Android 
	// obviously always calls the old one. 
	// So your code should not rely on a URI to work properly.

	@Override
	public void onChange(boolean selfChange) {
		this.onChange(selfChange, null);

	}

	@Override
	public void onChange(boolean selfChange, Uri uri) {
		// Post API level 16 call
	}
}
