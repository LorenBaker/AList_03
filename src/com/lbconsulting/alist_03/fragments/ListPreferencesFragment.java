package com.lbconsulting.alist_03.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.lbconsulting.alist_03.ColorsActivity;
import com.lbconsulting.alist_03.ListPreferencesActivity;
import com.lbconsulting.alist_03.R;
import com.lbconsulting.alist_03.classes.ListSettings;
import com.lbconsulting.alist_03.database.ListsTable;
import com.lbconsulting.alist_03.dialogs.ListsDialogFragment;
import com.lbconsulting.alist_03.utilities.AListUtilities;
import com.lbconsulting.alist_03.utilities.MyLog;

public class ListPreferencesFragment extends Fragment {

	public static final int ALPHABETICAL = 0;
	public static final int BY_GROUP = 1;
	public static final int MANUAL = 2;
	public static final int SELECTED_AT_TOP = 3;
	public static final int SELECTED_AT_BOTTOM = 4;
	public static final int LAST_USED = 5;

	private long mActiveListID;
	private ListSettings listSettings;
	public static final String LIST_PREFERENCES_CHANGED_BROADCAST_KEY = "list_preferences_changed";
	private BroadcastReceiver mPreferencesChangedBroadcastReceiver;

	private LinearLayout llFragListPreferences;
	private TextView tvListTitle;

	private Button btnEditListTitle;
	private Button btnListSortOrder;
	private Button btnMasterListSortOrder;
	private Button btnColors;
	private Button btnMakeDefaultPreferences;

	private Switch swShowGroupsInListsView;
	private Switch swShowGroupsInMasterListView;
	private Switch swShowStores;
	private Switch swDeleteNoteUponClearingItem;

	public ListPreferencesFragment() {
		// Empty constructor
	}

	public static ListPreferencesFragment newInstance(long newListID) {
		if (newListID < 2) {
			MyLog.e("ListPreferencesFragment: newInstance; listID = " + newListID, " is less than 2!!!!");
			return null;

		} else {
			ListPreferencesFragment f = new ListPreferencesFragment();
			// Supply listID input as an argument.
			Bundle args = new Bundle();
			args.putLong("listID", newListID);
			f.setArguments(args);
			return f;
		}
	}

	private boolean checkListID(String method) {
		if (mActiveListID < 2) {
			MyLog.e("ListPreferencesFragment", method + "; listID = " + mActiveListID + " is less than 2!!!!");
		} else {
			MyLog.i("ListPreferencesFragment", method + "; listID = " + mActiveListID);
		}
		return (mActiveListID > 1);
	}

	@Override
	public void onAttach(Activity activity) {
		checkListID("onAttach");
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		checkListID("onCreate");
		super.onCreate(savedInstanceState);
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
		View view = inflater.inflate(R.layout.frag_list_preferences, container, false);
		if (view != null && mActiveListID > 1) {

			listSettings = new ListSettings(getActivity(), mActiveListID);

			llFragListPreferences = (LinearLayout) view.findViewById(R.id.llFragListPreferences);

			tvListTitle = (TextView) view.findViewById(R.id.tvListTitle);

			btnEditListTitle = (Button) view.findViewById(R.id.btnEditListTitle);
			btnListSortOrder = (Button) view.findViewById(R.id.btnListSortOrder);
			btnMasterListSortOrder = (Button) view.findViewById(R.id.btnMasterListSortOrder);
			btnColors = (Button) view.findViewById(R.id.btnColors);
			btnMakeDefaultPreferences = (Button) view.findViewById(R.id.btnMakeDefaultPreferences);

			swShowGroupsInListsView = (Switch) view.findViewById(R.id.swShowGroupsInListsView);
			swShowGroupsInMasterListView = (Switch) view.findViewById(R.id.swShowGroupsInMasterListView);
			swShowStores = (Switch) view.findViewById(R.id.swShowStores);
			swDeleteNoteUponClearingItem = (Switch) view.findViewById(R.id.swDeleteNoteUponClearingItem);

			fillListPreferencesViews();

		} else {
			MyLog.e("ListPreferencesFragment: onActivityCreated after arguments set; listID = " + mActiveListID,
					" is less than 2!!!!");
		}

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		checkListID("onActivityCreated");

		btnEditListTitle.setOnClickListener(buttonClick);
		btnListSortOrder.setOnClickListener(buttonClick);
		btnMasterListSortOrder.setOnClickListener(buttonClick);
		btnColors.setOnClickListener(buttonClick);
		btnMakeDefaultPreferences.setOnClickListener(buttonClick);

		swShowGroupsInListsView.setOnCheckedChangeListener(switchOnCheckedChanged);
		swShowGroupsInMasterListView.setOnCheckedChangeListener(switchOnCheckedChanged);
		swShowStores.setOnCheckedChangeListener(switchOnCheckedChanged);
		swDeleteNoteUponClearingItem.setOnCheckedChangeListener(switchOnCheckedChanged);

		getActivity().getActionBar().setTitle("List Preferences");

		// Our handler for received Intents. This will be called whenever an Intent
		// with an action named "list_preferences_changed" is broadcasted.
		mPreferencesChangedBroadcastReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// there has been a changed in the ListsTable ... 
				// so refresh listSettings
				listSettings.RefreshListSettings();

				// Get extra data included in the Intent
				if (intent.hasExtra("editedListTitle")) {
					String newListTitle = intent.getStringExtra("editedListTitle");
					setListTitle(newListTitle);
					String key = String.valueOf(mActiveListID)
							+ ListPreferencesActivity.LIST_TITLE_CHANGE_BROADCAST_KEY;
					Intent intentForActivity = new Intent(key);
					LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intentForActivity);
				}
				if (intent.hasExtra("newListSortOrder")) {
					int newListSortOrder = intent.getIntExtra("newListSortOrder", 0);
					setListSortOrder(newListSortOrder);
				}
				if (intent.hasExtra("newMasterListSortOrder")) {
					int newMasterListSortOrder = intent.getIntExtra("newMasterListSortOrder", 0);
					setMasterListSortOrder(newMasterListSortOrder);
				}
			}
		};

		// Register to receive messages.
		// We are registering an observer (mPreferencesChangedBroadcastReceiver) to receive Intents
		// with actions named "list_preferences_changed".
		String key = String.valueOf(mActiveListID) + LIST_PREFERENCES_CHANGED_BROADCAST_KEY;
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mPreferencesChangedBroadcastReceiver,
				new IntentFilter(key));

		super.onActivityCreated(savedInstanceState);
	}

	View.OnClickListener buttonClick = new View.OnClickListener() {
		public void onClick(View v) {

			FragmentManager fm = getActivity().getSupportFragmentManager();
			// Remove any currently showing dialog
			Fragment prev = fm.findFragmentByTag("dialog_lists_table_update");
			if (prev != null) {
				FragmentTransaction ft = fm.beginTransaction();
				ft.remove(prev);
				ft.commit();
			}
			switch (v.getId()) {
			case R.id.btnEditListTitle:
				ListsDialogFragment editListTitleDialog = ListsDialogFragment
						.newInstance(mActiveListID, ListsDialogFragment.EDIT_LIST_TITLE);
				editListTitleDialog.show(fm, "dialog_lists_table_update");
				break;

			case R.id.btnListSortOrder:
				ListsDialogFragment editListSortOrderDialog = ListsDialogFragment
						.newInstance(mActiveListID, ListsDialogFragment.LIST_SORT_ORDER);
				editListSortOrderDialog.show(fm, "dialog_lists_table_update");

				/*Toast.makeText(getActivity(), "\"" + "btnListSortOrder" + "\"" + " is under construction.",
						Toast.LENGTH_SHORT).show();*/
				break;

			case R.id.btnMasterListSortOrder:
				ListsDialogFragment editMasterListSortOrderDialog = ListsDialogFragment
						.newInstance(mActiveListID, ListsDialogFragment.MASTER_LIST_SORT_ORDER);
				editMasterListSortOrderDialog.show(fm, "dialog_lists_table_update");

				/*Toast.makeText(getActivity(), "\"" + "btnMasterListSortOrder" + "\"" + " is under construction.",
						Toast.LENGTH_SHORT).show();*/
				break;

			case R.id.btnColors:
				StartColorsActivity();
				/*				Toast.makeText(getActivity(), "\"" + "btnColors" + "\"" + " is under construction.", Toast.LENGTH_SHORT)
										.show();*/
				break;

			case R.id.btnMakeDefaultPreferences:
				Toast.makeText(getActivity(), "\"" + "btnMakeDefaultPreferences" + "\"" + " is under construction.",
						Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}
	};

	Switch.OnCheckedChangeListener switchOnCheckedChanged = new Switch.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			ContentValues newFieldValues = new ContentValues();
			int checkedValue = AListUtilities.boolToInt(isChecked);
			switch (buttonView.getId()) {
			case R.id.swShowGroupsInListsView:
				newFieldValues.put(ListsTable.COL_SHOW_GROUPS_IN_LISTS_FRAGMENT, checkedValue);
				break;

			case R.id.swShowGroupsInMasterListView:
				newFieldValues.put(ListsTable.COL_SHOW_GROUPS_IN_MASTER_LIST_FRAGMENT, checkedValue);
				break;

			case R.id.swShowStores:
				newFieldValues.put(ListsTable.COL_SHOW_STORES, checkedValue);
				break;

			case R.id.swDeleteNoteUponClearingItem:
				newFieldValues.put(ListsTable.COL_DELETE_NOTE_UPON_DESELECTING_ITEM, checkedValue);
				break;

			default:
				newFieldValues = null;
				break;
			}
			if (newFieldValues != null) {
				listSettings.updateListsTableFieldValues(newFieldValues);
			}
		}
	};

	private void StartColorsActivity() {
		Intent colorsActivityIntent = new Intent(getActivity(), ColorsActivity.class);
		getActivity().startActivity(colorsActivityIntent);
	}

	private void fillListPreferencesViews() {
		if (listSettings != null) {
			if (llFragListPreferences != null) {
				llFragListPreferences.setBackgroundColor(listSettings.getListBackgroundColor());
			}

			if (tvListTitle != null) {
				setListTitle(listSettings.getListTitle());
				tvListTitle.setBackgroundColor(listSettings.getTitleBackgroundColor());
				tvListTitle.setTextColor(listSettings.getTitleTextColor());
			}

			if (swShowGroupsInListsView != null) {
				boolean checkedValue = listSettings.getShowGroupsInListsFragment();
				swShowGroupsInListsView.setChecked(listSettings.getShowGroupsInListsFragment());
				swShowGroupsInListsView.setTextColor(listSettings.getItemNormalTextColor());
			}

			if (swShowGroupsInMasterListView != null) {
				boolean checkedValue = listSettings.getShowGroupsInMasterListFragment();
				swShowGroupsInMasterListView.setChecked(listSettings.getShowGroupsInMasterListFragment());
				swShowGroupsInMasterListView.setTextColor(listSettings.getItemNormalTextColor());
			}

			if (swShowStores != null) {
				boolean checkedValue = listSettings.getShowStores();
				swShowStores.setChecked(listSettings.getShowStores());
				swShowStores.setTextColor(listSettings.getItemNormalTextColor());
			}

			if (swDeleteNoteUponClearingItem != null) {
				boolean checkedValue = listSettings.getDeleteNoteUponDeselectingItem();
				swDeleteNoteUponClearingItem.setChecked(listSettings.getDeleteNoteUponDeselectingItem());
				swDeleteNoteUponClearingItem.setTextColor(listSettings.getItemNormalTextColor());
			}

			if (btnListSortOrder != null) {
				setListSortOrder(listSettings.getListSortOrder());
				btnListSortOrder.setTextColor(listSettings.getItemNormalTextColor());
			}

			if (btnMasterListSortOrder != null) {
				setMasterListSortOrder(listSettings.getMasterListSortOrder());
				btnMasterListSortOrder.setTextColor(listSettings.getItemNormalTextColor());
			}

			if (btnEditListTitle != null) {
				btnEditListTitle.setTextColor(listSettings.getItemNormalTextColor());
			}

			if (btnColors != null) {
				btnColors.setTextColor(listSettings.getItemNormalTextColor());
			}

			if (btnMakeDefaultPreferences != null) {
				btnMakeDefaultPreferences.setTextColor(listSettings.getItemNormalTextColor());
			}
		}
	}

	private void setListTitle(String newListTitle) {
		if (tvListTitle != null) {
			tvListTitle.setText(newListTitle);
		}
	}

	private void setListSortOrder(int newListSortOrder) {
		if (btnListSortOrder != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("List Sort Order (");
			switch (newListSortOrder) {
			case BY_GROUP:
				sb.append("By Group)");
				break;

			case MANUAL:
				sb.append("Manual)");
				break;
			default:
				//ALPHABETICAL_LIST_SORT_ORDER
				sb.append("Alphabetical)");
				break;
			}
			btnListSortOrder.setText(sb.toString());
		}
	}

	private void setMasterListSortOrder(int newMasterListSortOrder) {
		if (btnMasterListSortOrder != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("Master List Sort Order (");
			switch (newMasterListSortOrder) {
			case BY_GROUP:
				sb.append("By Group)");
				break;

			case SELECTED_AT_TOP:
				sb.append("Selected at Top)");
				break;

			case SELECTED_AT_BOTTOM:
				sb.append("Selected at Bottom)");
				break;

			case LAST_USED:
				sb.append("Last Used)");
				break;

			default:
				//ALPHABETICAL_LIST_SORT_ORDER
				sb.append("Alphabetical)");
				break;
			}
			btnMasterListSortOrder.setText(sb.toString());
		}
	}

	@Override
	public void onStart() {
		checkListID("onStart");
		super.onStart();
	}

	@Override
	public void onResume() {
		checkListID("onResume");
		listSettings = new ListSettings(getActivity(), mActiveListID);
		fillListPreferencesViews();
		super.onResume();
	}

	@Override
	public void onPause() {
		checkListID("onPause");
		super.onPause();
	}

	@Override
	public void onStop() {
		checkListID("onStop");
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		checkListID("onDestroyView");
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		checkListID("onDestroy");
		// Unregister since the fragment is about to be closed.
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mPreferencesChangedBroadcastReceiver);
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		checkListID("onDetach");
		super.onDetach();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		checkListID("onViewCreated");
		super.onViewCreated(view, savedInstanceState);
	}
	/*
		@Override
		public void onApplyEditListTitleDialog(String newListTitle) {
			listSettings = new ListSettings(getActivity(), mActiveListID);
			if (tvListTitle != null) {
				tvListTitle.setText(newListTitle);
			}
		}

		@Override
		public void onCancelEditListTitleDialog() {
			// Do nothing
		}*/

}
