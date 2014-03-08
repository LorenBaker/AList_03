package com.lbconsulting.alist_03;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lbconsulting.alist_03.adapters.StoresPagerAdaptor;
import com.lbconsulting.alist_03.database.StoresTable;
import com.lbconsulting.alist_03.utilities.MyLog;

public class StoresActivity extends FragmentActivity {

	private long mActiveListID = -1;
	private long mActiveStoreID = -1;
	private int mActiveStorePosition = 0;
	private boolean mTwoFragmentLayout;
	private StoresPagerAdaptor mStoresPagerAdapter;
	private ViewPager mPager;
	private Cursor mAllStoresCursor;

	/*private BroadcastReceiver mListTitleChanged;
	public static final String LIST_TITLE_CHANGE_BROADCAST_KEY = "listTitleChanged";*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyLog.i("Stores_ACTIVITY", "onCreate");
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String activeListTitle = intent.getStringExtra("listTitle");
		int titleBackgroundColor = intent.getIntExtra("titleBackgroundColor", 0);
		int titleTextColor = intent.getIntExtra("titleTextColor", 0);

		setContentView(R.layout.activity_stores_pager);

		TextView tvListTitle = (TextView) findViewById(R.id.tvListTitle);
		if (tvListTitle != null) {
			tvListTitle.setText(activeListTitle);
			tvListTitle.setBackgroundColor(titleBackgroundColor);
			tvListTitle.setTextColor(titleTextColor);
		}

		View frag_locations_placeholder = this.findViewById(R.id.frag_locations_placeholder);
		mTwoFragmentLayout = frag_locations_placeholder != null
				&& frag_locations_placeholder.getVisibility() == View.VISIBLE;

		SharedPreferences storedStates = getSharedPreferences("AList", MODE_PRIVATE);
		mActiveListID = storedStates.getLong("ActiveListID", -1);

		mPager = (ViewPager) findViewById(R.id.storesPager);
		SetStoresPagerAdaptor();

		// TODO save ActoveStoreID & ActiveStorePostion in the database
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
				MyLog.d("Stores_ACTIVITY", "onPageSelected() - position = " + position + " ; storeID = "
						+ mActiveStoreID);

				if (mTwoFragmentLayout) {
					//LoadColorsFragment();
				}
			}
		});

		/*		mListTitleChanged = new BroadcastReceiver() {
					@Override
					public void onReceive(Context context, Intent intent) {
						// the list title has changed ...
						// restart activity to ensure that all lists are shown in alphabetical order
						ReStartStoresActivity();
					}
				};
				// Register to receive messages.
				// We are registering an observer (mPreferencesChangedBroadcastReceiver) to receive Intents
				// with actions named "list_preferences_changed".
				String key = String.valueOf(mActiveStoreID) + LIST_TITLE_CHANGE_BROADCAST_KEY;
				LocalBroadcastManager.getInstance(this).registerReceiver(mListTitleChanged, new IntentFilter(key));*/

		if (mTwoFragmentLayout) {
			//LoadColorsFragment();
		}
	}

	/*	private void ReStartStoresActivity() {
			mAllStoresCursor = StoresTable.getAllLists(this);
			mActiveStorePosition = AListUtilities.getListsCursorPositon(mAllStoresCursor, mActiveStoreID);
			Intent intent = new Intent(this, StoresActivity.class);
			// prohibit the back button from displaying previous version of this StoresActivity
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}*/

	/*	private long getStoreID(int position) {
			long storeID = -1;
			try {
				mAllStoresCursor.moveToPosition(position);
				storeID = mAllStoresCursor.getLong(mAllStoresCursor.getColumnIndexOrThrow(StoresTable.COL_STORE_ID));
			} catch (Exception e) {
				MyLog.d("Stores_ACTIVITY", "Exception in getStoreID: " + e);
			}
			return storeID;
		}*/

	private void SetStoresPagerAdaptor() {
		mAllStoresCursor = StoresTable.getAllStoresInListCursor(this, mActiveListID, StoresTable.SORT_ORDER_STORE_NAME);
		mStoresPagerAdapter = new StoresPagerAdaptor(getSupportFragmentManager(), this, mActiveListID);
		mPager.setAdapter(mStoresPagerAdapter);
	}

	private void LoadColorsFragment() {
		// TODO code LoadColorsFragment

	}

	protected void SetActiveStoreID(int position) {
		if (mAllStoresCursor != null) {
			long storeID = -1;
			try {
				mAllStoresCursor.moveToPosition(position);
				storeID = mAllStoresCursor.getLong(mAllStoresCursor.getColumnIndexOrThrow(StoresTable.COL_STORE_ID));
			} catch (Exception e) {
				MyLog.d("Stores_ACTIVITY", "Exception in SetActiveStoreID: " + e);
			}
			mActiveStoreID = storeID;
			mActiveStorePosition = position;
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
		if (mAllStoresCursor != null && mAllStoresCursor.getCount() > 0) {
			mActiveStorePosition = 0;
			SetActiveStoreID(mActiveStorePosition);
		} else {
			// there are no stores to show
			// TODO launch create new store dialog
		}

		/*mActiveStoreID = storedStates.getLong("ActiveStoreID", -1);
		mActiveStorePosition = storedStates.getInt("ActiveStorePosition", 0);
		mPager.setCurrentItem(mActiveStorePosition);*/
		super.onResume();
	}

	@Override
	protected void onPause() {
		MyLog.i("Stores_ACTIVITY", "onPause");
		SharedPreferences preferences = getSharedPreferences("AList", MODE_PRIVATE);
		SharedPreferences.Editor applicationStates = preferences.edit();
		/*applicationStates.putLong("ActiveStoreID", mActiveStoreID);
		applicationStates.putInt("ActiveStorePosition", mActiveStorePosition);*/
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
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
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

			Toast.makeText(this, "\"" + item.getTitle() + "\"" + " is under construction.", Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onMenuItemSelected(featureId, item);
		}
	}

	@Override
	protected void onDestroy() {
		MyLog.i("Stores_ACTIVITY", "onDestroy");
		// Unregister since the activity is about to be closed.
		//LocalBroadcastManager.getInstance(this).unregisterReceiver(mListTitleChanged);
		super.onDestroy();
	}

}
