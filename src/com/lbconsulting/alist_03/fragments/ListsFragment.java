package com.lbconsulting.alist_03.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.lbconsulting.alist_03.R;
import com.lbconsulting.alist_03.adapters.ItemsCursorAdaptor;
import com.lbconsulting.alist_03.adapters.StoresSpinnerCursorAdapter;
import com.lbconsulting.alist_03.classes.ListSettings;
import com.lbconsulting.alist_03.database.ItemsTable;
import com.lbconsulting.alist_03.database.ListsTable;
import com.lbconsulting.alist_03.database.StoresTable;
import com.lbconsulting.alist_03.dialogs.EditItemDialogFragment;
import com.lbconsulting.alist_03.utilities.AListUtilities;
import com.lbconsulting.alist_03.utilities.MyLog;

/*import android.app.Fragment;*/

public class ListsFragment extends Fragment
		implements LoaderManager.LoaderCallbacks<Cursor> {

	//OnListItemLongClickListener mListItemLongClickCallback;

	// Container Activity must implement this interface
	public interface OnListItemLongClickListener {
		public void onListItemLongClick(int position, long itemID);
	}

	//OnListItemSelectedListener mListsCallback;
	//private static final int LISTS_LOADER_ID = 1;
	private static final int ITEMS_LOADER_ID = 2;
	private static final int STORES_LOADER_ID = 3;
	//private static final int GROUPS_LOADER_ID = 4;
	//private static final int LOCATIONS_LOADER_ID = 5;

	//private String[] loaderNames = { "Lists_Loader", "Items_Loader", "Stores_Loader", "Groups_Loader" };

	private long mActiveListID = -9999;
	private long mActiveItemID = -9999;

	private ListSettings listSettings;

	//private Cursor mList;
	private TextView mListTitle;
	private ListView mItemsListView;
	private Spinner mStoreSpinner;

	public static final String RESART_STORES_LOADER_KEY = "RestartStoresLoaderKey";
	private BroadcastReceiver mRestartStoresLoaderReceiver;

	public static final String RESART_ITEMS_LOADER_KEY = "RestartItemsLoaderKey";
	private BroadcastReceiver mRestartItemsLoaderReceiver;

	private LoaderManager mLoaderManager = null;
	// The callbacks through which we will interact with the LoaderManager.
	private LoaderManager.LoaderCallbacks<Cursor> mListsFragmentCallbacks;
	private ItemsCursorAdaptor mItemsCursorAdaptor;
	private StoresSpinnerCursorAdapter mStoresSpinnerCursorAdapter;
	//private GroupsSpinnerCursorAdapter mGroupsSpinnerCursorAdapter;

	private boolean flag_FirstTimeLoadingItemDataSinceOnResume = false;

	/*	private boolean checkListID(String method) {
			if (mActiveListID < 2) {
				MyLog.e("ListsFragment", method + "; listID = " + mActiveListID + " is less than 2!!!!");
			} else {
				MyLog.i("ListsFragment", method + "; listID = " + mActiveListID);
			}
			return (mActiveListID > 1);
		}*/

	public ListsFragment() {
		// Empty constructor
	}

	/**
	 * Create a new instance of EditItemDialogFragment
	 * 
	 * @param itemID
	 * @return EditItemDialogFragment
	 */
	public static ListsFragment newInstance(long newListID) {

		if (newListID < 2) {
			MyLog.e("ListsFragment: newInstance; listID = " + newListID, " is less than 2!!!!");
			return null;
		} else {

			ListsFragment f = new ListsFragment();

			// Supply listID input as an argument.
			Bundle args = new Bundle();
			args.putLong("listID", newListID);
			f.setArguments(args);

			return f;
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		MyLog.i("ListsFragment", "onAttach");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MyLog.i("ListsFragment", "onCreate");
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		MyLog.i("ListsFragment", "onSaveInstanceState");
		// Store our listID
		outState.putLong("listID", this.mActiveListID);
		super.onSaveInstanceState(outState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		// MTM The real issue was in this method.  You were using getActivity().findViewById to resolve the views to modify.  Problem is, there are going to be a couple
		// - other of those Fragments in the view hierarchy of the Activity - notably the ones before and after this Fragment.  So, to properly initialize this Fragment we need to treat
		// - it as the holder for the views based on the passed in container.  So we inflate this into a view and use findViewById on that smaller hierarchy to properly set the Spinner and ListView.

		MyLog.i("ListsFragment", "onCreateView");

		if (savedInstanceState != null && savedInstanceState.containsKey("listID")) {
			mActiveListID = savedInstanceState.getLong("listID", 0);
		} else {
			Bundle bundle = getArguments();
			if (bundle != null)
				mActiveListID = bundle.getLong("listID", 0);
		}

		View view = inflater.inflate(R.layout.frag_lists, container, false);

		listSettings = new ListSettings(getActivity(), mActiveListID);

		mListTitle = (TextView) view.findViewById(R.id.tvListTitle);
		mListTitle.setText(listSettings.getListTitle());

		mStoreSpinner = (Spinner) view.findViewById(R.id.spinStores);
		mStoresSpinnerCursorAdapter = new StoresSpinnerCursorAdapter(getActivity(), null, 0, listSettings);
		mStoreSpinner.setAdapter(mStoresSpinnerCursorAdapter);
		if (listSettings.getShowStores()) {
			mStoreSpinner.setVisibility(View.VISIBLE);
		} else {
			mStoreSpinner.setVisibility(View.GONE);
		}

		mItemsCursorAdaptor = new ItemsCursorAdaptor(getActivity(), null, 0, listSettings);
		mItemsListView = (ListView) view.findViewById(R.id.itemsListView);
		mItemsListView.setAdapter(mItemsCursorAdaptor);

		setViewColors();

		mListsFragmentCallbacks = this;

		mItemsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ItemsTable.ToggleStrikeOut(getActivity(), id);
				// TODO restartLoader(ITEMS_LOADER_ID does not work correctly with 
				// the Groups Join table query.
				// Also ... I don't think that I should have to call the restart. 
				// Refreshing the data should be triggered by the content provider.
				// Also ... the issue may be associated with the pager
				// because under the "MasterListFragment" i don't user a pager
				// and the restartLoader works fine with the same Groups Join table query!!
				// Also ... The refresh works correctly when there is a single table query!
				mLoaderManager.restartLoader(ITEMS_LOADER_ID, null, mListsFragmentCallbacks);
			}
		});

		mStoreSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				ContentValues cv = new ContentValues();
				cv.put(ListsTable.COL_STORE_ID, id);
				listSettings.updateListsTableFieldValues(cv);
				//listSettings.setStoreID(id);

				long storeIDCheck = listSettings.getStoreID();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}

		});

		mItemsListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				mActiveItemID = id;
				FragmentManager fm = getFragmentManager();
				Fragment prev = fm.findFragmentByTag("dialog_edit_item");
				if (prev != null) {
					FragmentTransaction ft = fm.beginTransaction();
					ft.remove(prev);
					ft.commit();
				}
				EditItemDialogFragment editItemDialog = EditItemDialogFragment.newInstance(id);
				editItemDialog.show(fm, "dialog_edit_item");

				return true;
			}
		});

		mLoaderManager = getLoaderManager();
		mLoaderManager.initLoader(ITEMS_LOADER_ID, null, mListsFragmentCallbacks);
		mLoaderManager.initLoader(STORES_LOADER_ID, null, mListsFragmentCallbacks);

		return view;
	}

	private void setViewColors() {
		mListTitle.setBackgroundColor(this.listSettings.getTitleBackgroundColor());
		mListTitle.setTextColor(this.listSettings.getTitleTextColor());
		mStoreSpinner.setBackgroundColor(this.listSettings.getTitleBackgroundColor());
		mItemsListView.setBackgroundColor(this.listSettings.getListBackgroundColor());
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		MyLog.i("ListsFragment", "onActivityCreated");

		mRestartStoresLoaderReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				mLoaderManager.restartLoader(STORES_LOADER_ID, null, mListsFragmentCallbacks);
			}
		};

		mRestartItemsLoaderReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				mLoaderManager.restartLoader(ITEMS_LOADER_ID, null, mListsFragmentCallbacks);
			}
		};

		// Register local broadcast receivers.

		String restartGroupsLoaderKey = String.valueOf(mActiveListID) + RESART_STORES_LOADER_KEY;
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRestartStoresLoaderReceiver,
				new IntentFilter(restartGroupsLoaderKey));

		String restartItemsLoaderReceiver = String.valueOf(mActiveListID) + RESART_ITEMS_LOADER_KEY;
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRestartItemsLoaderReceiver,
				new IntentFilter(restartItemsLoaderReceiver));

		/*mLoaderManager = getLoaderManager();
		mLoaderManager.initLoader(ITEMS_LOADER_ID, null, mListsFragmentCallbacks);
		mLoaderManager.initLoader(STORES_LOADER_ID, null, mListsFragmentCallbacks);*/
		super.onActivityCreated(savedInstanceState);
	}

	public void DeleteList() {
		ListsTable.DeleteList(getActivity(), mActiveListID);
	}

	@Override
	public void onStart() {
		super.onStart();
		MyLog.i("ListsFragment", "onStart");
	}

	@Override
	public void onResume() {
		super.onResume();

		MyLog.i("ListsFragment", "onResume()");

		Bundle bundle = this.getArguments();
		if (bundle != null) {
			mActiveListID = bundle.getLong("listID", 0);
		}

		listSettings = new ListSettings(getActivity(), mActiveListID);
		setViewColors();

		// Set onResume flags
		flag_FirstTimeLoadingItemDataSinceOnResume = true;
	}

	@Override
	public void onPause() {
		super.onPause();

		MyLog.i("ListsFragment", "onPause()");

		// save ItemsListView position
		View v = mItemsListView.getChildAt(0);
		int ListViewTop = (v == null) ? 0 : v.getTop();
		ContentValues newFieldValues = new ContentValues();
		newFieldValues.put(ListsTable.COL_LISTVIEW_FIRST_VISIBLE_POSITION, mItemsListView.getFirstVisiblePosition());
		newFieldValues.put(ListsTable.COL_LISTVIEW_TOP, ListViewTop);
		ListsTable.UpdateListsTableFieldValues(getActivity(), mActiveListID, newFieldValues);
	}

	@Override
	public void onStop() {
		super.onStop();
		MyLog.i("ListsFragment", "onStop");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		MyLog.i("ListsFragment", "onDestroyView");
	}

	@Override
	public void onDestroy() {
		MyLog.i("ListsFragment", "onDestroy");
		// Unregister local broadcast receivers
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mRestartStoresLoaderReceiver);
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mRestartItemsLoaderReceiver);
		super.onDestroy();

	}

	@Override
	public void onDetach() {
		super.onDetach();
		MyLog.i("ListsFragment", "onDetach");
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		MyLog.i("ListsFragment", "onViewCreated");
	}

	/*	public void UnStrikeAndDeselectAllStruckOutItems() {
			ItemsTable.UnStrikeAndDeselectAllStruckOutItems(getActivity(), mActiveListID,
					listSettings.getDeleteNoteUponDeselectingItem());
		}*/

	/*	public void DeselectAllItemsInList() {
			ItemsTable
					.DeselectAllItemsInList(getActivity(), mActiveListID, listSettings.getDeleteNoteUponDeselectingItem());
		}*/

	public void showListTitle(String newListTitle) {
		mListTitle.setText(newListTitle);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		MyLog.i("ListsFragment", "onCreateLoader. LoaderId = " + id + "; listID = " + mActiveListID);

		CursorLoader cursorLoader = null;

		switch (id) {

		case ITEMS_LOADER_ID:

			int masterListSortOrder = listSettings.getMasterListSortOrder();
			String sortOrder = "";
			switch (masterListSortOrder) {
			case ListPreferencesFragment.ALPHABETICAL:
				sortOrder = ItemsTable.SORT_ORDER_ITEM_NAME;
				break;

			/*			case ListPreferencesFragment.BY_GROUP:
							sortOrder = ItemsTable.SORT_ORDER_BY_GROUP;
							break;*/

			/*			case ListPreferencesFragment.SELECTED_AT_TOP:
							sortOrder = ItemsTable.SORT_ORDER_SELECTED_AT_TOP;
							break;

						case ListPreferencesFragment.SELECTED_AT_BOTTOM:
							sortOrder = ItemsTable.SORT_ORDER_SELECTED_AT_BOTTOM;
							break;*/

			case ListPreferencesFragment.LAST_USED:
				sortOrder = ItemsTable.SORT_ORDER_LAST_USED;
				break;

			default:
				sortOrder = ItemsTable.SORT_ORDER_ITEM_NAME;
				break;
			}
			try {
				if (listSettings.getShowGroupsInListsFragment()) {
					cursorLoader = ItemsTable
							.getAllSelectedItemsInListWithGroups(getActivity(), mActiveListID, true);

				} else if (listSettings.getShowStores()) {
					cursorLoader = ItemsTable
							.getAllSelectedItemsInListWithLocations(getActivity(), mActiveListID, true);

				} else {
					cursorLoader = ItemsTable.getAllSelectedItemsInList(getActivity(), mActiveListID, true, sortOrder);
				}

			} catch (SQLiteException e) {
				MyLog.e("ListsFragment: onCreateLoader SQLiteException: ", e.toString());
				return null;

			} catch (IllegalArgumentException e) {
				MyLog.e("ListsFragment: onCreateLoader IllegalArgumentException: ", e.toString());
				return null;
			}
			break;

		case STORES_LOADER_ID:
			try {
				cursorLoader = StoresTable.getAllStoresInList(getActivity(), mActiveListID,
						StoresTable.SORT_ORDER_STORE_NAME);

			} catch (SQLiteException e) {
				MyLog.e("ListsFragment: onCreateLoader SQLiteException: ", e.toString());
				return null;

			} catch (IllegalArgumentException e) {
				MyLog.e("ListsFragment: onCreateLoader IllegalArgumentException: ", e.toString());
				return null;
			}
			break;

		default:
			break;
		}

		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor newCursor) {
		int id = loader.getId();
		MyLog.i("ListsFragment", "onLoadFinished. LoaderID = " + id + "; listID = " + mActiveListID);

		// The asynchronous load is complete and the newCursor is now available for use. 
		switch (loader.getId()) {
		case ITEMS_LOADER_ID:
			mItemsCursorAdaptor.swapCursor(newCursor);

			if (flag_FirstTimeLoadingItemDataSinceOnResume) {
				mItemsListView.setSelectionFromTop(listSettings.getListViewFirstVisiblePosition(),
						listSettings.getListViewTop());
				flag_FirstTimeLoadingItemDataSinceOnResume = false;
			}

			break;

		case STORES_LOADER_ID:
			mStoresSpinnerCursorAdapter.swapCursor(newCursor);
			mStoreSpinner.setSelection(AListUtilities.getIndex(mStoreSpinner, listSettings.getStoreID()));
			break;

		default:
			break;
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		int id = loader.getId();
		MyLog.i("ListsFragment", "onLoaderReset. LoaderID = " + id + "; listID = " + mActiveListID);

		switch (loader.getId()) {
		case ITEMS_LOADER_ID:
			mItemsCursorAdaptor.swapCursor(null);
			break;

		case STORES_LOADER_ID:
			mStoresSpinnerCursorAdapter.swapCursor(null);
			break;

		/*case GROUPS_LOADER_ID:
			mGroupsSpinnerCursorAdapter.swapCursor(null);
			break;*/

		default:
			break;
		}
	}
}
