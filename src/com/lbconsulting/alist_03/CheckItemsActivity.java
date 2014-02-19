package com.lbconsulting.alist_03;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.lbconsulting.alist_03.utilities.MyLog;

public class CheckItemsActivity extends Activity {
	private long mActiveListID = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyLog.i("CheckItems_ACTIVITY", "onCreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onStart() {
		MyLog.i("CheckItems_ACTIVITY", "onStart");
		super.onStart();
	}

	@Override
	protected void onRestart() {
		MyLog.i("CheckItems_ACTIVITY", "onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		MyLog.i("CheckItems_ACTIVITY", "onResume");
		SharedPreferences storedStates = getSharedPreferences("AList", MODE_PRIVATE);
		mActiveListID = storedStates.getLong("ActiveListID", -1);
		super.onResume();
	}

	@Override
	protected void onPause() {
		MyLog.i("CheckItems_ACTIVITY", "onPause");
		SharedPreferences preferences = getSharedPreferences("AList", MODE_PRIVATE);
		SharedPreferences.Editor applicationStates = preferences.edit();
		applicationStates.putLong("ActiveListID", mActiveListID);
		applicationStates.commit();
		super.onPause();
	}

	@Override
	protected void onStop() {
		MyLog.i("CheckItems_ACTIVITY", "onStop");
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MyLog.i("CheckItems_ACTIVITY", "onCreateOptionsMenu");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onDestroy() {
		MyLog.i("CheckItems_ACTIVITY", "onDestroy");
		super.onDestroy();
	}
}