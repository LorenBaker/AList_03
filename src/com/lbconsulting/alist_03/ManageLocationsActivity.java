package com.lbconsulting.alist_03;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lbconsulting.alist_03.adapters.ManageLocationsPagerAdaptor;
import com.lbconsulting.alist_03.database.GroupsTable;
import com.lbconsulting.alist_03.database.StoresTable;
import com.lbconsulting.alist_03.fragments.ManageLocationsFragment;
import com.lbconsulting.alist_03.utilities.MyLog;

public class ManageLocationsActivity extends FragmentActivity {

	private long mActiveListID = -1;
	private long mActiveStoreID = -1;
	private int mActiveStorePosition = 0;
	private boolean mTwoFragmentLayout;
	//private StoresPagerAdaptor mStoresPagerAdapter;
	private ManageLocationsPagerAdaptor mManageLocationsPagerAdaptor;
	private ViewPager mPager;
	private Cursor mAllStoresCursor;

	private String mActiveListTitle;
	private int mTitleBackgroundColor;
	private int mTitleTextColor;
	private String mRestartGroupsLoaderKey;

	/*private BroadcastReceiver mListTitleChanged;
	public static final String LIST_TITLE_CHANGE_BROADCAST_KEY = "listTitleChanged";*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyLog.i("ManageLocations_ACTIVITY", "onCreate");
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		mActiveListTitle = intent.getStringExtra("listTitle");
		mTitleBackgroundColor = intent.getIntExtra("titleBackgroundColor", 0);
		mTitleTextColor = intent.getIntExtra("titleTextColor", 0);

		SharedPreferences storedStates = getSharedPreferences("AList", MODE_PRIVATE);
		mActiveListID = storedStates.getLong("ActiveListID", -1);

		mRestartGroupsLoaderKey = String.valueOf(mActiveStoreID) + ManageLocationsFragment.RESART_GROUPS_LOADER_KEY;

		setContentView(R.layout.activity_manage_locations_pager);

		TextView tvListTitle = (TextView) findViewById(R.id.tvListTitle);
		if (tvListTitle != null) {
			tvListTitle.setText(mActiveListTitle);
			tvListTitle.setBackgroundColor(mTitleBackgroundColor);
			tvListTitle.setTextColor(mTitleTextColor);
		}

		View frag_stores_placeholder = this.findViewById(R.id.frag_stores_placeholder);
		mTwoFragmentLayout = frag_stores_placeholder != null && frag_stores_placeholder.getVisibility() == View.VISIBLE;

		mPager = (ViewPager) findViewById(R.id.manageLocationsPager);
		SetManageLocationsPagerAdaptor();

		// TODO save ActiveStoreID & ActiveStorePostion in the database
		// for now ... just start at position 0
		//*mActiveStoreID = storedStates.getLong("ActiveStoreID", -1);
		//mActiveStorePosition = storedStates.getInt("ActiveStorePosition", 0);*/
		if (mAllStoresCursor != null && mAllStoresCursor.getCount() > 0) {
			mActiveStorePosition = 0;
			SetActiveStoreID(mActiveStorePosition);
		} else {
			// there are no stores to show
			// TODO launch create new store dialog
		}

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
				SendRestartGroupsLoaderBroadCast();
				MyLog.d("ManageLocations_ACTIVITY", "onPageSelected() - position = " + position + " ; storeID = "
						+ mActiveStoreID);

				if (mTwoFragmentLayout) {
					LoadStoresFragment();
				}
			}
		});

		if (mTwoFragmentLayout) {
			LoadStoresFragment();
		}
	}

	/*	private void SetStoresPagerAdaptor() {
			mAllStoresCursor = StoresTable.getAllStoresInListCursor(this, mActiveListID, StoresTable.SORT_ORDER_STORE_NAME);
			mStoresPagerAdapter = new StoresPagerAdaptor(getSupportFragmentManager(), this, mActiveListID);
			mPager.setAdapter(mStoresPagerAdapter);
		}*/
	private void SetManageLocationsPagerAdaptor() {
		mAllStoresCursor = StoresTable.getAllStoresInListCursor(this, mActiveListID, StoresTable.SORT_ORDER_STORE_NAME);
		mManageLocationsPagerAdaptor = new ManageLocationsPagerAdaptor(getSupportFragmentManager(), this, mActiveListID);
		mPager.setAdapter(mManageLocationsPagerAdaptor);
	}

	private void SendRestartGroupsLoaderBroadCast() {
		Intent restartGroupsLoaderIntent = new Intent(mRestartGroupsLoaderKey);
		LocalBroadcastManager.getInstance(ManageLocationsActivity.this).sendBroadcast(restartGroupsLoaderIntent);
	}

	private void LoadStoresFragment() {
		// TODO code LoadStoresFragment

	}

	protected void SetActiveStoreID(int position) {
		if (mAllStoresCursor != null) {
			long storeID = -1;
			try {
				mAllStoresCursor.moveToPosition(position);
				storeID = mAllStoresCursor.getLong(mAllStoresCursor.getColumnIndexOrThrow(StoresTable.COL_STORE_ID));
			} catch (Exception e) {
				MyLog.d("ManageLocations_ACTIVITY", "Exception in SetActiveStoreID: " + e);
			}
			mActiveStoreID = storeID;
			mActiveStorePosition = position;
			mRestartGroupsLoaderKey = String.valueOf(mActiveStoreID) + ManageLocationsFragment.RESART_GROUPS_LOADER_KEY;
		}
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
		if (mAllStoresCursor != null && mAllStoresCursor.getCount() > 0) {
			mActiveStorePosition = 0;
			SetActiveStoreID(mActiveStorePosition);
		} else {
			// there are no stores to show
			// TODO launch create new store dialog
		}

		super.onResume();
	}

	@Override
	protected void onPause() {
		MyLog.i("ManageLocations_ACTIVITY", "onPause");
		/*		SharedPreferences preferences = getSharedPreferences("AList", MODE_PRIVATE);
				SharedPreferences.Editor applicationStates = preferences.edit();
				
				applicationStates.commit();*/
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
			SendRestartGroupsLoaderBroadCast();
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

	private void StartStoresActivity() {
		Intent intent = new Intent(this, StoresActivity.class);
		intent.putExtra("listTitle", mActiveListTitle);
		intent.putExtra("titleBackgroundColor", mTitleBackgroundColor);
		intent.putExtra("titleTextColor", mTitleTextColor);
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		MyLog.i("ManageLocations_ACTIVITY", "onDestroy");
		super.onDestroy();
	}

}
