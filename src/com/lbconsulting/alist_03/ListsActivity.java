package com.lbconsulting.alist_03;

import java.util.ArrayList;
import java.util.Hashtable;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.lbconsulting.alist_03.adapters.ListsPagerAdapter;
import com.lbconsulting.alist_03.classes.ListSettings;
import com.lbconsulting.alist_03.database.BridgeTable;
import com.lbconsulting.alist_03.database.GroupsTable;
import com.lbconsulting.alist_03.database.ItemsTable;
import com.lbconsulting.alist_03.database.ListsTable;
import com.lbconsulting.alist_03.database.LocationsTable;
import com.lbconsulting.alist_03.database.StoresTable;
import com.lbconsulting.alist_03.dialogs.ListsDialogFragment;
import com.lbconsulting.alist_03.fragments.ListPreferencesFragment;
import com.lbconsulting.alist_03.fragments.ListsFragment;
import com.lbconsulting.alist_03.fragments.MasterListFragment;
import com.lbconsulting.alist_03.utilities.AListUtilities;
import com.lbconsulting.alist_03.utilities.MyLog;

public class ListsActivity extends FragmentActivity {

	private ListsPagerAdapter mListsPagerAdapter;
	private ViewPager mPager;

	private MasterListFragment mMasterListFragment;
	private Boolean mTwoFragmentLayout = false;

	private long NO_ACTIVE_LIST_ID = 0;
	private long mActiveListID = NO_ACTIVE_LIST_ID;
	private long mActiveItemID;
	private int mActiveListPosition = 0;
	private ListSettings mListSettings;

	private Cursor mAllListsCursor;
	private BroadcastReceiver mListTitleChanged;

	String mRestartStoresLoaderKey = "";
	String mRestartItemsLoaderKey = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lists_pager);

		SharedPreferences storedStates = getSharedPreferences("AList", MODE_PRIVATE);
		mActiveListID = storedStates.getLong("ActiveListID", -1);
		mActiveItemID = storedStates.getLong("ActiveItemID", -1);
		mActiveListPosition = storedStates.getInt("ActiveListPosition", -1);

		mListTitleChanged = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.hasExtra("editedListTitle")) {
					// the list title has changed ...
				}

				if (intent.hasExtra("newListID")) {
					// a new list has been created ...
					mActiveListID = intent.getLongExtra("newListID", 0);
				}

				// restart activity to ensure that all lists are shown in alphabetical order
				ReStartListsActivity();
			}
		};
		// Register to receive messages.
		String key = String.valueOf(mActiveListID) + ListPreferencesFragment.LIST_PREFERENCES_CHANGED_BROADCAST_KEY;
		LocalBroadcastManager.getInstance(this).registerReceiver(mListTitleChanged, new IntentFilter(key));

		if (mActiveListID < 2) {
			//SetToFirstList();
			return;
		}

		View frag_masterList_placeholder = this.findViewById(R.id.frag_masterList_placeholder);
		mTwoFragmentLayout = frag_masterList_placeholder != null
				&& frag_masterList_placeholder.getVisibility() == View.VISIBLE;

		MyLog.i("Lists_ACTIVITY", "onCreate - ViewPager");

		mAllListsCursor = ListsTable.getAllLists(this);
		mListSettings = new ListSettings(this, mActiveListID);

		mRestartStoresLoaderKey = String.valueOf(mActiveListID) + ListsFragment.RESART_STORES_LOADER_KEY;
		mRestartItemsLoaderKey = String.valueOf(mActiveListID) + ListsFragment.RESART_ITEMS_LOADER_KEY;

		mListsPagerAdapter = new ListsPagerAdapter(getSupportFragmentManager(), this);
		mPager = (ViewPager) findViewById(R.id.listsPager);
		mPager.setAdapter(mListsPagerAdapter);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int state) {
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageSelected(int position) {
				// A list page has been selected
				SetActiveListID(position);
				SetActiveListBroadcastReceivers();
				MyLog.d("Lists_ACTIVITY", "onPageSelected() - position = " + position + " ; listID = " + mActiveListID);

				if (mTwoFragmentLayout) {
					LoadMasterListFragment();
				}
			}
		});

		if (mTwoFragmentLayout) {
			LoadMasterListFragment();
		}
	}

	private void SetActiveListID(int position) {
		if (mAllListsCursor != null) {
			try {
				mAllListsCursor.moveToPosition(position);
				mActiveListID = mAllListsCursor.getLong(mAllListsCursor.getColumnIndexOrThrow(ListsTable.COL_LIST_ID));
				mListSettings = new ListSettings(this, mActiveListID);

				mRestartStoresLoaderKey = String.valueOf(mActiveListID) + ListsFragment.RESART_STORES_LOADER_KEY;
				mRestartItemsLoaderKey = String.valueOf(mActiveListID) + ListsFragment.RESART_ITEMS_LOADER_KEY;

				mActiveListPosition = position;
			} catch (Exception e) {
				MyLog.d("Lists_ACTIVITY", "Exception in getlistID: " + e);
			}
		}
	}

	private void SetActiveListBroadcastReceivers() {
		// Unregister old receiver
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mListTitleChanged);

		// Register new receiver
		String key = String.valueOf(mActiveListID) + ListPreferencesFragment.LIST_PREFERENCES_CHANGED_BROADCAST_KEY;
		LocalBroadcastManager.getInstance(this).registerReceiver(mListTitleChanged, new IntentFilter(key));

	}

	private void ReStartListsActivity() {
		mAllListsCursor = ListsTable.getAllLists(this);
		mActiveListPosition = AListUtilities.getListsCursorPositon(mAllListsCursor, mActiveListID);
		Intent intent = new Intent(this, ListsActivity.class);
		// prohibit the back button from displaying previous version of this ListPreferencesActivity
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	private void ReStartListsActivity(int position) {
		mAllListsCursor = ListsTable.getAllLists(this);
		if (mAllListsCursor.getCount() >= position + 1) {
			mActiveListPosition = position;
		} else {
			if (mAllListsCursor.getCount() > 0) {
				mActiveListPosition = 0;
			} else {
				// there are no lists in the ListsTable!
				mActiveListPosition = -1;
				mActiveListID = -1;
			}

		}
		if (mActiveListPosition > -1) {
			mActiveListID = AListUtilities.getIdByPosition(mAllListsCursor, mActiveListPosition);
		}
		Intent intent = new Intent(this, ListsActivity.class);
		// prohibit the back button from displaying previous version of this ListPreferencesActivity
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	private void SetToFirstList() {
		long firstListID = ListsTable.getFirstListID(this);
		if (firstListID > 1) {
			mActiveListID = firstListID;
		} else {
			CreatNewList();
		}
	}

	private void StartMasterListActivity() {
		Intent masterListActivityIntent = new Intent(this, MasterListActivity.class);
		//masterListActivityIntent.putExtra("ActiveListID", mActiveListID);
		startActivity(masterListActivityIntent);
	}

	/*	private void StartStoresActivity() {
			Intent intent = new Intent(this, StoresActivity.class);
			intent.putExtra("listTitle", mListSettings.getListTitle());
			intent.putExtra("titleBackgroundColor", mListSettings.getTitleBackgroundColor());
			intent.putExtra("titleTextColor", mListSettings.getTitleTextColor());
			startActivity(intent);
		}*/

	private void StartManageLocationsActivity() {
		Intent intent = new Intent(this, ManageLocationsActivity.class);
		intent.putExtra("listTitle", mListSettings.getListTitle());
		intent.putExtra("titleBackgroundColor", mListSettings.getTitleBackgroundColor());
		intent.putExtra("titleTextColor", mListSettings.getTitleTextColor());
		startActivity(intent);
	}

	private void StartListPreferencesActivity() {
		Intent intent = new Intent(this, ListPreferencesActivity.class);
		startActivity(intent);
	}

	private void StartAboutActivity() {
		Intent intent = new Intent(this, AboutActivity.class);
		startActivity(intent);
	}

	private void LoadMasterListFragment() {
		mMasterListFragment = (MasterListFragment) this.getSupportFragmentManager()
				.findFragmentByTag("MasterListFragment");
		if (mMasterListFragment == null) {
			// create MasterListFragment
			mMasterListFragment = MasterListFragment.newInstance(mActiveListID);

			MyLog.i("Lists_ACTIVITY", "LoadMasterListFragment. New MasterListFragment created. ListID = "
					+ mActiveListID);
			// add the fragment to the Activity
			this.getSupportFragmentManager().beginTransaction()
					.add(R.id.frag_masterList_placeholder, mMasterListFragment, "MasterListFragment")
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
					.commit();
			MyLog.i("Lists_ACTIVITY", "LoadMasterListFragment. MasterListFragment ADD. ListID = " + mActiveListID);
		} else {
			// MasterListFragment exists ... so replace it
			mMasterListFragment = MasterListFragment.newInstance(mActiveListID);

			MyLog.i("Lists_ACTIVITY", "LoadMasterListFragment. New MasterListFragment created. ListID = "
					+ mActiveListID);
			// add the fragment to the Activity
			this.getSupportFragmentManager().beginTransaction()
					.replace(R.id.frag_masterList_placeholder, mMasterListFragment, "MasterListFragment")
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
					.commit();
			MyLog.i("Lists_ACTIVITY", "LoadMasterListFragment. MasterListFragment REPLACE. ListID = " + mActiveListID);
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		MyLog.i("Lists_ACTIVITY", "onSaveInstanceState");
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		MyLog.i("Lists_ACTIVITY", "onRestoreInstanceState");
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onStart() {
		MyLog.i("Lists_ACTIVITY", "onStart");
		super.onStart();
	}

	@Override
	protected void onRestart() {
		MyLog.i("Lists_ACTIVITY", "onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		MyLog.i("Lists_ACTIVITY", "onResume");
		if (mActiveListID < 2) {
			CreatNewList();
		} else {
			mPager.setCurrentItem(mActiveListPosition);
		}

		super.onResume();
	}

	@Override
	protected void onPause() {
		MyLog.i("Lists_ACTIVITY", "onPause");

		// save activity state
		SharedPreferences preferences = getSharedPreferences("AList", MODE_PRIVATE);
		SharedPreferences.Editor applicationStates = preferences.edit();
		applicationStates.putLong("ActiveListID", mActiveListID);
		applicationStates.putLong("ActiveItemID", mActiveItemID);
		applicationStates.putInt("ActiveListPosition", mActiveListPosition);
		applicationStates.commit();
		super.onPause();
	}

	@Override
	protected void onStop() {
		MyLog.i("Lists_ACTIVITY", "onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		MyLog.i("Lists_ACTIVITY", "onDestroy");
		mAllListsCursor.close();
		// Unregister since the activity is about to be closed.
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mListTitleChanged);
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MyLog.i("Lists_ACTIVITY", "onCreateOptionsMenu");
		getMenuInflater().inflate(R.menu.lists_1activity, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// handle item selection
		switch (item.getItemId()) {
		case R.id.action_removeStruckOffItems:
			ItemsTable.UnStrikeAndDeselectAllStruckOutItems(this, mActiveListID,
					mListSettings.getDeleteNoteUponDeselectingItem());
			SendRestartItemsLoaderBroadCast();
			return true;

		case R.id.action_addItem:
			StartMasterListActivity();
			return true;

		case R.id.action_newList:
			CreatNewList();
			return true;

		case R.id.action_create_groceries_list:
			CreateGroceriesList();
			return true;

		case R.id.action_create_todo_list:
			CreateToDoList();
			return true;

		case R.id.action_clearList:
			ItemsTable
					.DeselectAllItemsInList(this, mActiveListID, mListSettings.getDeleteNoteUponDeselectingItem());
			return true;

		case R.id.action_emailList:
			Toast.makeText(this, "\"" + item.getTitle() + "\"" + " is under construction.", Toast.LENGTH_SHORT).show();
			return true;

		case R.id.action_editListTitle:
			EditListTitle();
			return true;

		case R.id.action_deleteList:
			DeleteList();
			/*Toast.makeText(this, "\"" + item.getTitle() + "\"" + " is under construction.", Toast.LENGTH_SHORT).show();*/
			return true;

		case R.id.action_manageLocations:
			StartManageLocationsActivity();
			return true;

		case R.id.action_Preferences:
			StartListPreferencesActivity();
			return true;

		case R.id.action_about:
			StartAboutActivity();
			return true;

		default:
			return super.onMenuItemSelected(featureId, item);
		}

	}

	private void SendRestartStoresLoaderBroadCast() {
		Intent restartStoresLoaderIntent = new Intent(mRestartStoresLoaderKey);
		LocalBroadcastManager.getInstance(this).sendBroadcast(restartStoresLoaderIntent);
	}

	private void SendRestartItemsLoaderBroadCast() {
		Intent restartItemsLoaderIntent = new Intent(mRestartItemsLoaderKey);
		LocalBroadcastManager.getInstance(this).sendBroadcast(restartItemsLoaderIntent);
	}

	private void CreateToDoList() {
		// create new list
		long todosListID = ListsTable.CreateNewList(this, "To Do");

		if (todosListID > 1) {
			ArrayList<Long> todoGroupIDs = new ArrayList<Long>();
			// create todo groups
			String[] todoGroups = this.getResources().getStringArray(R.array.todo_groups);
			for (int i = 0; i < todoGroups.length; i++) {
				todoGroupIDs.add(GroupsTable.CreateNewGroup(this, todosListID, todoGroups[i]));
			}

			// create todo items
			String[] todoItems = this.getResources().getStringArray(R.array.todo_items);
			for (int i = 0; i < todoItems.length; i++) {
				ItemsTable.CreateNewItem(this, todosListID, todoItems[i], todoGroupIDs.get(i));
			}

			mActiveListID = todosListID;
			ReStartListsActivity();

		}

	}

	private void CreateGroceriesList() {
		// create new list

		long groceriesListID = ListsTable.CreateNewList(this, "Groceries");

		if (groceriesListID > 1) {

			Hashtable<String, Long> groceryGroupsHashTable = new Hashtable<String, Long>();

			// create grocery groups
			String[] groceryGroups = this.getResources().getStringArray(R.array.grocery_groups);
			for (int i = 0; i < groceryGroups.length; i++) {
				long groupID = GroupsTable.CreateNewGroup(this, groceriesListID, groceryGroups[i]);
				groceryGroupsHashTable.put(groceryGroups[i], groupID);
			}

			// create grocery items
			// NOTE: this only works if R.array.grocery_items and R.array.grocery_items_groups
			// are in the proper order!!!!!!
			String[] groceryItems = this.getResources().getStringArray(R.array.grocery_items);
			String[] groceryItemGroups = this.getResources().getStringArray(R.array.grocery_items_groups);

			for (int i = 0; i < groceryItems.length; i++) {
				long groupID = groceryGroupsHashTable.get(groceryItemGroups[i]);
				ItemsTable.CreateNewItem(this, groceriesListID, groceryItems[i], groupID);
			}

			// create grocery stores
			Hashtable<String, Long> storesHashTable = new Hashtable<String, Long>();
			String[] groceryStores = this.getResources().getStringArray(R.array.grocery_stores);
			for (int i = 0; i < groceryStores.length; i++) {
				long storeID = StoresTable.CreateNewStore(this, groceriesListID, groceryStores[i]);
				storesHashTable.put(groceryStores[i], storeID);
			}

			// create locations
			Hashtable<String, Long> locationsHashTable = new Hashtable<String, Long>();
			String[] storeLocations = this.getResources().getStringArray(R.array.locations);
			for (int i = 0; i < storeLocations.length; i++) {
				long locationID = LocationsTable.CreateNewLocation(this, groceriesListID, storeLocations[i]);
				locationsHashTable.put(storeLocations[i], locationID);
			}

			// create Bridge table
			long locationID = -1;
			String[] Albertons = this.getResources().getStringArray(R.array.Albertsons_Eastgate_Locations);
			long storeID = storesHashTable.get(groceryStores[0]);
			for (int i = 0; i < Albertons.length; i++) {
				String groupLocation = Albertons[i];
				if (groupLocation.equals("[No LOCATION]")) {
					locationID = 1;
				} else {
					locationID = locationsHashTable.get(Albertons[i]);
				}
				long groupID = groceryGroupsHashTable.get(groceryGroups[i]);
				BridgeTable.CreateNewBridgeRow(this, groceriesListID, storeID, groupID, locationID);
			}

			String[] QFC = this.getResources().getStringArray(R.array.QFC_Factoria_Locations);
			storeID = storesHashTable.get(groceryStores[1]);
			for (int i = 0; i < QFC.length; i++) {
				String groupLocation = QFC[i];
				if (groupLocation.equals("[No LOCATION]")) {
					locationID = 1;
				} else {
					locationID = locationsHashTable.get(QFC[i]);
				}
				long groupID = groceryGroupsHashTable.get(groceryGroups[i]);
				BridgeTable.CreateNewBridgeRow(this, groceriesListID, storeID, groupID, locationID);
			}

			String[] sw_belfair = this.getResources().getStringArray(R.array.Safeway_Belfair_Locations);
			storeID = storesHashTable.get(groceryStores[2]);
			for (int i = 0; i < sw_belfair.length; i++) {
				String groupLocation = sw_belfair[i];
				if (groupLocation.equals("[No LOCATION]")) {
					locationID = 1;
				} else {
					locationID = locationsHashTable.get(sw_belfair[i]);
				}
				long groupID = groceryGroupsHashTable.get(groceryGroups[i]);
				BridgeTable.CreateNewBridgeRow(this, groceriesListID, storeID, groupID, locationID);
			}

			String[] sw_evergreen = this.getResources().getStringArray(R.array.Safeway_Evergreen_Village_Locations);
			storeID = storesHashTable.get(groceryStores[3]);
			for (int i = 0; i < sw_evergreen.length; i++) {
				String groupLocation = sw_evergreen[i];
				if (groupLocation.equals("[No LOCATION]")) {
					locationID = 1;
				} else {
					locationID = locationsHashTable.get(sw_evergreen[i]);
				}
				long groupID = groceryGroupsHashTable.get(groceryGroups[i]);
				BridgeTable.CreateNewBridgeRow(this, groceriesListID, storeID, groupID, locationID);
			}

			String[] sw_Issaquah = this.getResources().getStringArray(R.array.Safeway_Issaquah_Locations);
			storeID = storesHashTable.get(groceryStores[4]);
			for (int i = 0; i < sw_Issaquah.length; i++) {
				String groupLocation = sw_Issaquah[i];
				if (groupLocation.equals("[No LOCATION]")) {
					locationID = 1;
				} else {
					locationID = locationsHashTable.get(sw_Issaquah[i]);
				}
				long groupID = groceryGroupsHashTable.get(groceryGroups[i]);
				BridgeTable.CreateNewBridgeRow(this, groceriesListID, storeID, groupID, locationID);
			}

			mActiveListID = groceriesListID;
			ReStartListsActivity();

		}

	}

	private void DeleteList() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// set title
		builder.setTitle("Delete List");

		String msg = "Permanently delete " + "\"" + mListSettings.getListTitle() + "\" ?";
		// set dialog message
		builder
				.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						ListsTable.DeleteList(ListsActivity.this, mActiveListID);
						ReStartListsActivity(mActiveListPosition);
						finish();
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = builder.create();

		// show it
		alertDialog.show();
	}

	private void CreatNewList() {
		FragmentManager fm = this.getSupportFragmentManager();
		// Remove any currently showing dialog
		Fragment prev = fm.findFragmentByTag("dialog_lists_table_update");
		if (prev != null) {
			FragmentTransaction ft = fm.beginTransaction();
			ft.remove(prev);
			ft.commit();
		}

		ListsDialogFragment editListTitleDialog = ListsDialogFragment
				.newInstance(mActiveListID, ListsDialogFragment.NEW_LIST);
		editListTitleDialog.show(fm, "dialog_lists_table_update");

	}

	private void EditListTitle() {

		FragmentManager fm = this.getSupportFragmentManager();
		// Remove any currently showing dialog
		Fragment prev = fm.findFragmentByTag("dialog_lists_table_update");
		if (prev != null) {
			FragmentTransaction ft = fm.beginTransaction();
			ft.remove(prev);
			ft.commit();
		}

		ListsDialogFragment editListTitleDialog = ListsDialogFragment
				.newInstance(mActiveListID, ListsDialogFragment.EDIT_LIST_TITLE);
		editListTitleDialog.show(fm, "dialog_lists_table_update");
	}

}