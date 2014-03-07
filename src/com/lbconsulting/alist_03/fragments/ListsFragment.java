package com.lbconsulting.alist_03.fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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

	private String[] loaderNames = { "Lists_Loader", "Items_Loader", "Stores_Loader", "Groups_Loader" };

	private long mActiveListID = -9999;
	private long mActiveItemID = -9999;

	private ListSettings listSettings;

	//private Cursor mList;
	private TextView mListTitle;
	private ListView mItemsListView;
	private Spinner mStoreSpinner;

	private LoaderManager mLoaderManager = null;
	// The callbacks through which we will interact with the LoaderManager.
	private LoaderManager.LoaderCallbacks<Cursor> mListsFragmentCallbacks;
	private ItemsCursorAdaptor mItemsCursorAdaptor;
	private StoresSpinnerCursorAdapter mStoresSpinnerCursorAdapter;
	//private GroupsSpinnerCursorAdapter mGroupsSpinnerCursorAdapter;

	private boolean flag_FirstTimeLoadingItemDataSinceOnResume = false;

	private boolean checkListID(String method) {
		if (mActiveListID < 2) {
			MyLog.e("ListsFragment", method + "; listID = " + mActiveListID + " is less than 2!!!!");
		} else {
			MyLog.i("ListsFragment", method + "; listID = " + mActiveListID);
		}
		return (mActiveListID > 1);
	}

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
		checkListID("onAttach");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		checkListID("onCreate");
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Store our listID
		outState.putLong("listID", this.mActiveListID);
		super.onSaveInstanceState(outState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		// MTM The real issue was in this method.  You were using getActivity().findViewById to resolve the views to modify.  Problem is, there are going to be a couple
		// - other of those Fragments in the view hierarchy of the Activity - notably the ones before and after this Fragment.  So, to properly initialize this Fragment we need to treat
		// - it as the holder for the views based on the passed in container.  So we inflate this into a view and use findViewById on that smaller hierarchy to properly set the Spinner and ListView.

		checkListID("onCreateView");

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
		checkListID("onActivityCreated");
		mLoaderManager = getLoaderManager();
		mLoaderManager.initLoader(ITEMS_LOADER_ID, null, mListsFragmentCallbacks);
		mLoaderManager.initLoader(STORES_LOADER_ID, null, mListsFragmentCallbacks);
		super.onActivityCreated(savedInstanceState);
	}

	public void DeleteList() {
		ListsTable.DeleteList(getActivity(), mActiveListID);
	}

	@Override
	public void onStart() {
		super.onStart();
		checkListID("onStart");
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

		checkListID("onResume");

		// Set onResume flags
		flag_FirstTimeLoadingItemDataSinceOnResume = true;
	}

	@Override
	public void onPause() {
		super.onPause();

		MyLog.i("ListsFragment", "onPause()");

		checkListID("onPause");

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
		checkListID("onStop");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		checkListID("onDestroyView");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		checkListID("onDestroy");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		checkListID("onDetach");
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		checkListID("onViewCreated");
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
		String loaderName = loaderNames[id - 1];
		checkListID("onCreateLoader; " + loaderName);

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
					cursorLoader = ItemsTable.getAllItemsInListWithGroups(getActivity(), mActiveListID, null);

				} else if (listSettings.getShowStores()) {
					cursorLoader = ItemsTable.getAllItemsInListWithLocations(getActivity(), mActiveListID);

				} else {
					cursorLoader = ItemsTable.getAllItemsInList(getActivity(), mActiveListID, sortOrder);
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

		// Log out the loader information for comparison
		MyLog.i("ListsFragment", "onLoadFinished; loader: " + loader.toString());

		int id = loader.getId();
		String loaderName = loaderNames[id - 1];
		checkListID("onLoadFinished; " + loaderName);

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
		String loaderName = loaderNames[id - 1];
		checkListID("onLoaderReset; " + loaderName);

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
