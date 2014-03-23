package com.lbconsulting.alist_03;

import android.content.BroadcastReceiver;
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
import android.widget.TextView;
import android.widget.Toast;

import com.lbconsulting.alist_03.adapters.StoresPagerAdaptor;
import com.lbconsulting.alist_03.database.StoresTable;
import com.lbconsulting.alist_03.dialogs.StoresDialogFragment;
import com.lbconsulting.alist_03.utilities.AListUtilities;
import com.lbconsulting.alist_03.utilities.MyLog;

public class StoresActivity extends FragmentActivity {

	private long mActiveListID = -1;
	private long mActiveStoreID = -1;
	private int mActiveStorePosition = 0;
	// private boolean mTwoFragmentLayout;
	private StoresPagerAdaptor mStoresPagerAdapter;
	private ViewPager mPager;
	private Cursor mAllStoresCursor;

	private BroadcastReceiver mRestartStoresActivityReceiver;
	public static final String RESTART_STORES_ACTIVITY_BROADCAST_KEY = "restartStoresActivityBroadcastKey";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyLog.i("Stores_ACTIVITY", "onCreate");
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_stores_pager);
		mPager = (ViewPager) findViewById(R.id.storesPager);
		if (mPager == null) {
			// not in portrait orientation ... so finish
			finish();
			return;
		}

		Intent intent = getIntent();
		String activeListTitle = intent.getStringExtra("listTitle");
		int titleBackgroundColor = intent.getIntExtra("titleBackgroundColor", 0);
		int titleTextColor = intent.getIntExtra("titleTextColor", 0);

		SharedPreferences storedStates = getSharedPreferences("AList", MODE_PRIVATE);
		mActiveListID = storedStates.getLong("ActiveListID", -1);
		mActiveStoreID = storedStates.getLong("ActiveStoreID", -1);
		mActiveStorePosition = storedStates.getInt("ActiveStorePosition", -1);

		TextView tvListTitle = (TextView) findViewById(R.id.tvListTitle);
		if (tvListTitle != null) {
			tvListTitle.setText(activeListTitle);
			tvListTitle.setBackgroundColor(titleBackgroundColor);
			tvListTitle.setTextColor(titleTextColor);
		}

		mRestartStoresActivityReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				RestartStoresActivity(mActiveStorePosition);
			}
		};

		// Register local broadcast receivers.
		String restartStoresActivityKey = String.valueOf(mActiveListID) + RESTART_STORES_ACTIVITY_BROADCAST_KEY;
		LocalBroadcastManager.getInstance(this).registerReceiver(mRestartStoresActivityReceiver, new IntentFilter(restartStoresActivityKey));

		/*		View frag_locations_placeholder = this.findViewById(R.id.frag_locations_placeholder);
				mTwoFragmentLayout = frag_locations_placeholder != null
						&& frag_locations_placeholder.getVisibility() == View.VISIBLE;*/

		SetStoresPagerAdaptor();

		// TODO save ActoveStoreID & ActiveStorePostion in the database
		// for now ... just start at position 0
		// *mActiveStoreID = storedStates.getLong("ActiveStoreID", -1);
		// mActiveStorePosition = storedStates.getInt("ActiveStorePosition", 0);*/
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

			}
		});
	}

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
		mActiveStoreID = storedStates.getLong("ActiveStoreID", -1);
		mActiveStorePosition = storedStates.getInt("ActiveStorePosition", -1);

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
		applicationStates.putLong("ActiveStoreID", mActiveStoreID);
		applicationStates.putInt("ActiveStorePosition", mActiveStorePosition);
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

		case R.id.action_showStoreLocation:
			Toast.makeText(this, "\"" + item.getTitle() + "\"" + " is under construction.", Toast.LENGTH_SHORT).show();
			return true;

		case R.id.action_newStore:
			AddNewStore();
			// Toast.makeText(this, "\"" + item.getTitle() + "\"" + " is under construction.", Toast.LENGTH_SHORT).show();
			return true;

		case R.id.action_deleteStore:
			DeleteStore();
			// Toast.makeText(this, "\"" + item.getTitle() + "\"" + " is under construction.", Toast.LENGTH_SHORT).show();
			return true;

		case R.id.action_editStoreName:
			EditStoreName();
			// Toast.makeText(this, "\"" + item.getTitle() + "\"" + " is under construction.", Toast.LENGTH_SHORT).show();
			return true;

		default:
			return super.onMenuItemSelected(featureId, item);
		}
	}

	private void AddNewStore() {
		FragmentManager fm = this.getSupportFragmentManager();
		// Remove any currently showing dialog
		Fragment prev = fm.findFragmentByTag("dialog_store_create_edit_delete");
		if (prev != null) {
			FragmentTransaction ft = fm.beginTransaction();
			ft.remove(prev);
			ft.commit();
		}

		if (mActiveStoreID > 1) {
			// can't edit the default store
			StoresDialogFragment addNewStoreNameDialog = StoresDialogFragment.newInstance(mActiveListID, mActiveStoreID, StoresDialogFragment.NEW_STORE);
			addNewStoreNameDialog.show(fm, "dialog_store_create_edit_delete");
		}
	}

	private void DeleteStore() {
		FragmentManager fm = this.getSupportFragmentManager();
		// Remove any currently showing dialog
		Fragment prev = fm.findFragmentByTag("dialog_store_create_edit_delete");
		if (prev != null) {
			FragmentTransaction ft = fm.beginTransaction();
			ft.remove(prev);
			ft.commit();
		}

		if (mActiveStoreID > 1) {
			// can't edit the default store
			StoresDialogFragment deleteStoreNameDialog = StoresDialogFragment.newInstance(mActiveListID, mActiveStoreID, StoresDialogFragment.DELETE_STORE);
			deleteStoreNameDialog.show(fm, "dialog_store_create_edit_delete");
		}
	}

	private void EditStoreName() {
		FragmentManager fm = this.getSupportFragmentManager();
		// Remove any currently showing dialog
		Fragment prev = fm.findFragmentByTag("dialog_store_create_edit_delete");
		if (prev != null) {
			FragmentTransaction ft = fm.beginTransaction();
			ft.remove(prev);
			ft.commit();
		}

		if (mActiveStoreID > 1) {
			// can't edit the default store
			StoresDialogFragment editStoreNameDialog = StoresDialogFragment.newInstance(mActiveListID, mActiveStoreID, StoresDialogFragment.EDIT_STORE_NAME);
			editStoreNameDialog.show(fm, "dialog_store_create_edit_delete");
		}
	}

	private void RestartStoresActivity(int position) {
		Cursor allStoresCursor = StoresTable.getAllStoresInListCursor(this, mActiveListID, StoresTable.SORT_ORDER_STORE_NAME);
		if (allStoresCursor != null) {
			if (allStoresCursor.getCount() >= position + 1) {
				mActiveStorePosition = position;
			} else {
				if (allStoresCursor.getCount() > 0) {
					mActiveStorePosition = 0;
				} else {
					// there are no stores in the StoresTable!
					mActiveStorePosition = -1;
					mActiveListID = -1;
				}
			}
			if (mActiveStorePosition > -1) {
				mActiveListID = AListUtilities.getIdByPosition(allStoresCursor, mActiveStorePosition);
			}
			allStoresCursor.close();
		}
		Intent intent = new Intent(this, StoresActivity.class);
		// prohibit the back button from displaying previous version of this StoresActivity
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		MyLog.i("Stores_ACTIVITY", "onDestroy");
		// Unregister since the activity is about to be closed.
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mRestartStoresActivityReceiver);
		super.onDestroy();
	}

}
