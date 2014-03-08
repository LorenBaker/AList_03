package com.lbconsulting.alist_03;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lbconsulting.alist_03.utilities.MyLog;

public class StoresActivity extends Activity {

	private long mActiveListID = -1;
	private Boolean mTwoFragmentLayout = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyLog.i("Stores_ACTIVITY", "onCreate");
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_temp);
		SharedPreferences storedStates = getSharedPreferences("AList", MODE_PRIVATE);
		mActiveListID = storedStates.getLong("ActiveListID", -1);
		TextView tvFragmentA = (TextView) this.findViewById(R.id.tvFragmentA);

		TextView tvFragmentB = (TextView) this.findViewById(R.id.tvFragmentB);
		mTwoFragmentLayout = tvFragmentB != null && tvFragmentB.getVisibility() == View.VISIBLE;

		if (mTwoFragmentLayout) {
			tvFragmentA.setText("Groups Fragment;\nListID = " + mActiveListID + ";\nStoreID = ?");
			tvFragmentB.setText("Stores Fragment;\nListID = " + mActiveListID + ";\nStoreID = ?");
		} else {
			tvFragmentA.setText("Stores Fragment;\nListID = " + mActiveListID + ";\nStoreID = ?");
		}
	}

	@Override
	protected void onStart() {
		MyLog.i("Stores_ACTIVITY", "onStart");
		super.onStart();
	}

	@Override
	protected void onRestart() {
		MyLog.i("Stores_ACTIVITY", "onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		MyLog.i("Stores_ACTIVITY", "onResume");
		SharedPreferences storedStates = getSharedPreferences("AList", MODE_PRIVATE);
		mActiveListID = storedStates.getLong("ActiveListID", -1);
		super.onResume();
	}

	@Override
	protected void onPause() {
		MyLog.i("Stores_ACTIVITY", "onPause");
		SharedPreferences preferences = getSharedPreferences("AList", MODE_PRIVATE);
		SharedPreferences.Editor applicationStates = preferences.edit();
		applicationStates.putLong("ActiveListID", mActiveListID);

		applicationStates.commit();
		super.onPause();
	}

	@Override
	protected void onStop() {
		MyLog.i("Stores_ACTIVITY", "onStop");
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MyLog.i("Stores_ACTIVITY", "onCreateOptionsMenu");
		getMenuInflater().inflate(R.menu.stores_1activity, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// handle item selection
		switch (item.getItemId()) {
		case R.id.action_newStore:
			Toast.makeText(this, "\"" + item.getTitle() + "\"" + " is under construction.", Toast.LENGTH_SHORT).show();
			return true;

		case R.id.action_showStoreLocation:
			Toast.makeText(this, "\"" + item.getTitle() + "\"" + " is under construction.", Toast.LENGTH_SHORT).show();
			return true;

		case R.id.action_editStoreName:
			Toast.makeText(this, "\"" + item.getTitle() + "\"" + " is under construction.", Toast.LENGTH_SHORT).show();
			return true;

		case R.id.action_manage_locations:
			/*Toast.makeText(this, "\"" + item.getTitle() + "\"" + " is under construction.", Toast.LENGTH_SHORT).show();*/
			StartManageLocationsActivity();
			return true;

		default:
			return super.onMenuItemSelected(featureId, item);
		}
	}

	@Override
	protected void onDestroy() {
		MyLog.i("Stores_ACTIVITY", "onDestroy");
		super.onDestroy();
	}

	private void StartManageLocationsActivity() {
		Intent intent = new Intent(this, ManageLocationsActivity.class);
		startActivity(intent);
	}
}