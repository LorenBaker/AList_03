package com.lbconsulting.alist_03;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lbconsulting.alist_03.adapters.ManageLocationsPagerAdaptor;
import com.lbconsulting.alist_03.classes.ListSettings;
import com.lbconsulting.alist_03.database.GroupsTable;
import com.lbconsulting.alist_03.database.ListsTable;
import com.lbconsulting.alist_03.database.StoresTable;
import com.lbconsulting.alist_03.dialogs.LocationsDialogFragment;
import com.lbconsulting.alist_03.fragments.ManageLocationsFragment;
import com.lbconsulting.alist_03.utilities.AListUtilities;
import com.lbconsulting.alist_03.utilities.MyLog;

public class ManageLocationsActivity extends FragmentActivity {

	private long mActiveListID = -1;
	private long mActiveLocationID = -1;
	private long mActiveStoreID = -1;
	private int mActiveStorePosition = 0;
	private ListSettings mListSettings;

	private boolean mTwoFragmentLayout;

	private ManageLocationsPagerAdaptor mManageLocationsPagerAdaptor;
	private ViewPager mPager;
	private Cursor mAllStoresCursor;

	private BroadcastReceiver mActiveLocationIdReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyLog.i("ManageLocations_ACTIVITY", "onCreate");
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		mActiveListID = intent.getLongExtra("ActiveListID", -1);
		mListSettings = new ListSettings(this, mActiveListID);
		mActiveStoreID = mListSettings.getActiveStoreID();
		mAllStoresCursor = StoresTable.getAllStoresInListCursor(this, mActiveListID, StoresTable.SORT_ORDER_STORE_NAME);

		setContentView(R.layout.activity_manage_locations_pager);

		TextView tvListTitle = (TextView) findViewById(R.id.tvListTitle);
		if (tvListTitle != null) {
			tvListTitle.setText(mListSettings.getListTitle());
			tvListTitle.setBackgroundColor(mListSettings.getTitleBackgroundColor());
			tvListTitle.setTextColor(mListSettings.getTitleTextColor());
		}

		View frag_stores_placeholder = this.findViewById(R.id.frag_stores_placeholder);
		mTwoFragmentLayout = frag_stores_placeholder != null && frag_stores_placeholder.getVisibility() == View.VISIBLE;

		mPager = (ViewPager) findViewById(R.id.manageLocationsPager);
		SetManageLocationsPagerAdaptor();

		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int state) {
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageSelected(int position) {
				SetActiveStoreID(position);
				SetActiveListBroadcastReceivers();
				MyLog.d("ManageLocations_ACTIVITY", "onPageSelected() - position = " + position + " ; storeID = " + mActiveStoreID);

				if (mTwoFragmentLayout) {
					LoadStoresFragment();
				}
			}
		});

		mActiveLocationIdReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent activeLocatonIntent) {
				if (activeLocatonIntent.hasExtra("ActiveLocationID")) {
					mActiveLocationID = activeLocatonIntent.getLongExtra("ActiveLocationID", -1);
				}
			}
		};

		// Register local broadcast receivers.
		String activeLocationIdReceiverKey = String.valueOf(mActiveListID) + ManageLocationsFragment.ACTIVE_LOCATION_ID_BROADCAST_KEY;
		LocalBroadcastManager.getInstance(this).registerReceiver(mActiveLocationIdReceiver, new IntentFilter(activeLocationIdReceiverKey));

		if (mTwoFragmentLayout) {
			LoadStoresFragment();
		}
	}

	private void SetManageLocationsPagerAdaptor() {
		mManageLocationsPagerAdaptor = new ManageLocationsPagerAdaptor(getSupportFragmentManager(), this, mActiveListID);
		mPager.setAdapter(mManageLocationsPagerAdaptor);
	}

	private void LoadStoresFragment() {
		// TODO code LoadStoresFragment

	}

	protected void SetActiveStoreID(int position) {
		if (mAllStoresCursor != null) {
			try {
				mAllStoresCursor.moveToPosition(position);
				mActiveStoreID = mAllStoresCursor.getLong(mAllStoresCursor.getColumnIndexOrThrow(StoresTable.COL_STORE_ID));
				mActiveStorePosition = position;

				// update the lists table with the selected store ID
				ContentValues newFieldValues = new ContentValues();
				newFieldValues.put(ListsTable.COL_ACTIVE_STORE_ID, mActiveStoreID);
				mListSettings.updateListsTableFieldValues(newFieldValues);

			} catch (Exception e) {
				MyLog.d("ManageLocations_ACTIVITY", "Exception in SetActiveStoreID: " + e);
			}
		}
	}

	private void SetActiveListBroadcastReceivers() {
		// Unregister old receivers
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mActiveLocationIdReceiver);

		// Register new local broadcast receivers.
		String activeLocationIdReceiverKey = String.valueOf(mActiveListID) + ManageLocationsFragment.ACTIVE_LOCATION_ID_BROADCAST_KEY;
		LocalBroadcastManager.getInstance(this).registerReceiver(mActiveLocationIdReceiver, new IntentFilter(activeLocationIdReceiverKey));
	}

	@Override
	protected void onStart() {
		MyLog.i("ManageLocations_ACTIVITY", "onStart");
		super.onStart();
	}

	@Override
	protected void onRestart() {
		MyLog.i("ManageLocations_ACTIVITY", "onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		MyLog.i("ManageLocations_ACTIVITY", "onResume");
		SharedPreferences storedStates = getSharedPreferences("AList", MODE_PRIVATE);
		mActiveListID = storedStates.getLong("ActiveListID", -1);
		mListSettings = new ListSettings(this, mActiveListID);
		mActiveStoreID = mListSettings.getActiveStoreID();
		mAllStoresCursor = StoresTable.getAllStoresInListCursor(this, mActiveListID, StoresTable.SORT_ORDER_STORE_NAME);
		if (mAllStoresCursor != null && mAllStoresCursor.getCount() > 0) {
			if (mActiveStoreID > 1) {
				mActiveStorePosition = AListUtilities.getCursorPositon(mAllStoresCursor, mActiveStoreID);
			} else {
				// there are stores in the list, but we don't have an ActiveStoreID
				// so ... go to the first store
				mActiveStorePosition = 0;
				SetActiveStoreID(mActiveStorePosition);
			}
			mPager.setCurrentItem(mActiveStorePosition);
		} else {
			// there are no stores in this list
			// so... start the stores activity
			StartStoresActivity();
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		MyLog.i("ManageLocations_ACTIVITY", "onPause");
		super.onPause();
	}

	@Override
	protected void onStop() {
		MyLog.i("ManageLocations_ACTIVITY", "onStop");
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MyLog.i("ManageLocations_ACTIVITY", "onCreateOptionsMenu");
		getMenuInflater().inflate(R.menu.manage_locations_1activity, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch (item.getItemId()) {

		case R.id.action_clearAllCheckedGroups:
			GroupsTable.UnCheckAllCheckedGroups(this, mActiveListID);
			return true;

		case R.id.action_editLocationName:
			// Toast.makeText(this, "\"" + item.getTitle() + "\"" + " is under construction.", Toast.LENGTH_SHORT).show();
			EditLocationName();
			return true;

		case R.id.action_addNewLocation:
			// Toast.makeText(this, "\"" + item.getTitle() + "\"" + " is under construction.", Toast.LENGTH_SHORT).show();
			AddNewLocation();
			return true;

		case R.id.action_deleteLocation:
			// Toast.makeText(this, "\"" + item.getTitle() + "\"" + " is under construction.", Toast.LENGTH_SHORT).show();
			DeleteLocation();
			return true;

		case R.id.action_sortOrder:
			Toast.makeText(this, "\"" + item.getTitle() + "\"" + " is under construction.", Toast.LENGTH_SHORT).show();
			return true;

		case R.id.action_manageStores:
			StartStoresActivity();
			/*Toast.makeText(this, "\"" + item.getTitle() + "\"" + " is under construction.", Toast.LENGTH_SHORT).show();*/
			return true;

		default:
			return super.onMenuItemSelected(featureId, item);
		}
	}

	private void AddNewLocation() {
		FragmentManager fm = this.getSupportFragmentManager();
		// Remove any currently showing dialog
		Fragment prev = fm.findFragmentByTag("dialog_location_create_edit_delete");
		if (prev != null) {
			FragmentTransaction ft = fm.beginTransaction();
			ft.remove(prev);
			ft.commit();
		}
		LocationsDialogFragment newLocationDialog = LocationsDialogFragment.newInstance(mActiveListID, mActiveLocationID,
				LocationsDialogFragment.NEW_LOCATION);
		newLocationDialog.show(fm, "dialog_location_create_edit_delete");
	}

	private void DeleteLocation() {
		FragmentManager fm = this.getSupportFragmentManager();
		// Remove any currently showing dialog
		Fragment prev = fm.findFragmentByTag("dialog_location_create_edit_delete");
		if (prev != null) {
			FragmentTransaction ft = fm.beginTransaction();
			ft.remove(prev);
			ft.commit();
		}

		if (mActiveLocationID > 1) {
			// can't delete the default location
			LocationsDialogFragment deleteLocationDialog = LocationsDialogFragment.newInstance(mActiveListID, mActiveLocationID,
					LocationsDialogFragment.DELETE_LOCATION);
			deleteLocationDialog.show(fm, "dialog_location_create_edit_delete");
		}
	}

	private void EditLocationName() {
		FragmentManager fm = this.getSupportFragmentManager();
		// Remove any currently showing dialog
		Fragment prev = fm.findFragmentByTag("dialog_location_create_edit_delete");
		if (prev != null) {
			FragmentTransaction ft = fm.beginTransaction();
			ft.remove(prev);
			ft.commit();
		}

		if (mActiveLocationID > 1) {
			// can't edit the default location
			LocationsDialogFragment editLocationNameDialog = LocationsDialogFragment.newInstance(mActiveListID, mActiveLocationID,
					LocationsDialogFragment.EDIT_LOCATION_NAME);
			editLocationNameDialog.show(fm, "dialog_location_create_edit_delete");
		}
	}

	private void StartStoresActivity() {
		Intent intent = new Intent(this, StoresActivity.class);
		intent.putExtra("ActiveListID", mActiveListID);
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		// Unregister since the activity is about to be closed.
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mActiveLocationIdReceiver);
		MyLog.i("ManageLocations_ACTIVITY", "onDestroy");
		super.onDestroy();
	}

}
