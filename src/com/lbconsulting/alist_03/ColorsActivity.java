package com.lbconsulting.alist_03;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.lbconsulting.alist_03.utilities.MyLog;

public class ColorsActivity extends Activity {
	private long mActiveListID = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyLog.i("Colors_ACTIVITY", "onCreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onStart() {
		MyLog.i("Colors_ACTIVITY", "onStart");
		super.onStart();
	}

	@Override
	protected void onRestart() {
		MyLog.i("Colors_ACTIVITY", "onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		MyLog.i("Colors_ACTIVITY", "onResume");
		SharedPreferences storedStates = getSharedPreferences("AList", MODE_PRIVATE);
		mActiveListID = storedStates.getLong("ActiveListID", -1);
		super.onResume();
	}

	@Override
	protected void onPause() {
		MyLog.i("Colors_ACTIVITY", "onPause");
		SharedPreferences preferences = getSharedPreferences("AList", MODE_PRIVATE);
		SharedPreferences.Editor applicationStates = preferences.edit();
		applicationStates.putLong("ActiveListID", mActiveListID);
		applicationStates.commit();
		super.onPause();
	}

	@Override
	protected void onStop() {
		MyLog.i("Colors_ACTIVITY", "onStop");
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MyLog.i("Colors_ACTIVITY", "onCreateOptionsMenu");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onDestroy() {
		MyLog.i("Colors_ACTIVITY", "onDestroy");
		super.onDestroy();
	}
}
