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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.lbconsulting.alist_03.R;
import com.lbconsulting.alist_03.adapters.CheckItemsCursorAdaptor;
import com.lbconsulting.alist_03.classes.ListSettings;
import com.lbconsulting.alist_03.database.ItemsTable;
import com.lbconsulting.alist_03.database.ListsTable;
import com.lbconsulting.alist_03.dialogs.EditItemDialogFragment;
import com.lbconsulting.alist_03.utilities.MyLog;

public class CheckItemsFragment extends Fragment
		implements LoaderManager.LoaderCallbacks<Cursor> {

	private static final int ITEMS_LOADER_ID = 2;

	private long mActiveListID = -9999;
	private long mActiveItemID = -9999;
	private int mActivePosition = -9999;

	private ListSettings mListSettings;

	private TextView mListTitle;
	private ListView mItemsListView;
	private Spinner mStoreSpinner;

	private LoaderManager mLoaderManager = null;
	// The callbacks through which we will interact with the LoaderManager.
	private LoaderManager.LoaderCallbacks<Cursor> mCheckItemsFragmentCallbacks;
	private CheckItemsCursorAdaptor mCheckItemsCursorAdaptor;

	private boolean flag_FirstTimeLoadingItemDataSinceOnResume = false;

	private boolean checkListID(String method) {
		if (mActiveListID < 2) {
			MyLog.e("CheckItemsFragment", method + "; listID = " + mActiveListID + " is less than 2!!!!");
		} else {
			MyLog.i("CheckItemsFragment", method + "; listID = " + mActiveListID);
		}
		return (mActiveListID > 1);
	}

	public CheckItemsFragment() {
		// Empty constructor
	}

	/**
	 * Create a new instance of EditItemDialogFragment
	 * 
	 * @param itemID
	 * @return EditItemDialogFragment
	 */
	public static CheckItemsFragment newInstance(long newListID) {

		if (newListID < 2) {
			MyLog.e("CheckItemsFragment: newInstance; listID = " + newListID, " is less than 2!!!!");
			return null;
		} else {

			CheckItemsFragment f = new CheckItemsFragment();

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
		checkListID("onCreateView");

		if (savedInstanceState != null && savedInstanceState.containsKey("listID")) {
			mActiveListID = savedInstanceState.getLong("listID", 0);
		} else {
			Bundle bundle = getArguments();
			if (bundle != null)
				mActiveListID = bundle.getLong("listID", 0);
		}

		View view = inflater.inflate(R.layout.frag_lists, container, false);

		mListSettings = new ListSettings(getActivity(), mActiveListID);

		mListTitle = (TextView) view.findViewById(R.id.tvListTitle);
		mListTitle.setText(mListSettings.getListTitle());

		mStoreSpinner = (Spinner) view.findViewById(R.id.spinStores);
		mStoreSpinner.setVisibility(View.GONE);

		mCheckItemsCursorAdaptor = new CheckItemsCursorAdaptor(getActivity(), null, 0, mListSettings);
		mItemsListView = (ListView) view.findViewById(R.id.itemsListView);
		mItemsListView.setAdapter(mCheckItemsCursorAdaptor);

		mCheckItemsFragmentCallbacks = this;

		mItemsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ItemsTable.ToggleCheckBox(getActivity(), id);
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

	private void setFragmentColors() {
		mListTitle.setBackgroundColor(this.mListSettings.getTitleBackgroundColor());
		mListTitle.setTextColor(this.mListSettings.getTitleTextColor());
		mItemsListView.setBackgroundColor(this.mListSettings.getListBackgroundColor());

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		checkListID("onActivityCreated");
		mLoaderManager = getLoaderManager();
		mLoaderManager.initLoader(ITEMS_LOADER_ID, null, mCheckItemsFragmentCallbacks);
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
		checkListID("onStart");
	}

	@Override
	public void onResume() {
		super.onResume();

		MyLog.i("CheckItemsFragment", "onResume()");

		Bundle bundle = this.getArguments();
		if (bundle != null) {
			mActiveListID = bundle.getLong("listID", 0);
		}
		mListSettings = new ListSettings(getActivity(), mActiveListID);
		setFragmentColors();
		checkListID("onResume");

		// Set onResume flags
		flag_FirstTimeLoadingItemDataSinceOnResume = true;
	}

	@Override
	public void onPause() {
		super.onPause();

		MyLog.i("CheckItemsFragment", "onPause()");

		checkListID("onPause");

		// save ItemsListView position
		View v = mItemsListView.getChildAt(0);
		int ListViewTop = (v == null) ? 0 : v.getTop();
		ContentValues newFieldValues = new ContentValues();
		newFieldValues.put(ListsTable.COL_MASTER_LISTVIEW_FIRST_VISIBLE_POSITION,
				mItemsListView.getFirstVisiblePosition());
		newFieldValues.put(ListsTable.COL_MASTER_LISTVIEW_TOP, ListViewTop);
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

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		checkListID("onCreateLoader; id = " + id);

		CursorLoader cursorLoader = null;
		String selection = null;

		switch (id) {

		case ITEMS_LOADER_ID:
			int masterListSortOrder = mListSettings.getMasterListSortOrder();
			String sortOrder = "";
			switch (masterListSortOrder) {
			case ListPreferencesFragment.ALPHABETICAL:
				sortOrder = ItemsTable.SORT_ORDER_ITEM_NAME;
				break;

			/*case ListPreferencesFragment.BY_GROUP:
				sortOrder = ItemsTable.SORT_ORDER_BY_GROUP;
				break;*/

			case ListPreferencesFragment.SELECTED_AT_TOP:
				sortOrder = ItemsTable.SORT_ORDER_SELECTED_AT_TOP;
				break;

			case ListPreferencesFragment.SELECTED_AT_BOTTOM:
				sortOrder = ItemsTable.SORT_ORDER_SELECTED_AT_BOTTOM;
				break;

			case ListPreferencesFragment.LAST_USED:
				sortOrder = ItemsTable.SORT_ORDER_LAST_USED;
				break;

			default:
				sortOrder = ItemsTable.SORT_ORDER_ITEM_NAME;
				break;
			}

			try {
				if (masterListSortOrder == ListPreferencesFragment.BY_GROUP) {
					cursorLoader = ItemsTable.getAllItemsInListByGroup(getActivity(), mActiveListID);
				} else {
					cursorLoader = ItemsTable.getAllItemsInList(getActivity(), mActiveListID, selection, sortOrder);
				}

			} catch (SQLiteException e) {
				MyLog.e("CheckItemsFragment: onCreateLoader SQLiteException: ", e.toString());
				return null;

			} catch (IllegalArgumentException e) {
				MyLog.e("CheckItemsFragment: onCreateLoader IllegalArgumentException: ", e.toString());
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
		MyLog.i("CheckItemsFragment: onLoadFinished; id = " + id, "; listID = " + mActiveListID);
		// The asynchronous load is complete and the newCursor is now available for use. 
		// Update the adapter to show the changed data.
		switch (loader.getId()) {
		case ITEMS_LOADER_ID:
			mCheckItemsCursorAdaptor.swapCursor(newCursor);
			if (flag_FirstTimeLoadingItemDataSinceOnResume) {
				mItemsListView.setSelectionFromTop(
						mListSettings.getMasterListViewFirstVisiblePosition(), mListSettings.getMasterListViewTop());
				flag_FirstTimeLoadingItemDataSinceOnResume = false;
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		int id = loader.getId();
		MyLog.i("CheckItemsFragment: onLoaderReset; id = " + id, "; listID = " + mActiveListID);

		switch (loader.getId()) {
		case ITEMS_LOADER_ID:
			mCheckItemsCursorAdaptor.swapCursor(null);
			break;

		default:
			break;
		}
	}
}
