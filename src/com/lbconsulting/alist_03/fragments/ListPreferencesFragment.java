package com.lbconsulting.alist_03.fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.lbconsulting.alist_03.R;
import com.lbconsulting.alist_03.classes.ListSettings;
import com.lbconsulting.alist_03.database.ListsTable;
import com.lbconsulting.alist_03.dialogs.EditListTitleDialogFragment.EditListTitleDialogListener;
import com.lbconsulting.alist_03.utilities.AListUtilities;
import com.lbconsulting.alist_03.utilities.MyLog;

public class ListPreferencesFragment extends Fragment implements EditListTitleDialogListener {

	public static final int ALPHABETICAL_LIST_SORT_ORDER = 0;
	public static final int BY_GROUP_LIST_SORT_ORDER = 1;
	public static final int MANUAL_LIST_SORT_ORDER = 2;

	public static final int ALPHABETICAL_MASTER_LIST_SORT_ORDER = 0;
	public static final int BY_GROUP_MASTER_LIST_SORT_ORDER = 1;
	public static final int SELECTED_AT_TOP_MASTER_LIST_SORT_ORDER = 2;
	public static final int SELECTED_AT_BOTTOM_MASTER_LIST_SORT_ORDER = 3;
	public static final int LAST_USED_MASTER_LIST_SORT_ORDER = 4;

	private long mActiveListID;
	private ListSettings listSettings;

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

		super.onActivityCreated(savedInstanceState);
	}

	View.OnClickListener buttonClick = new View.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnEditListTitle:
				/*				Toast.makeText(getActivity(), "\"" + "btnEditListTitle" + "\"" + " is under construction.",
										Toast.LENGTH_SHORT).show();*/

				EditListTitle();

				break;

			case R.id.btnListSortOrder:
				Toast.makeText(getActivity(), "\"" + "btnListSortOrder" + "\"" + " is under construction.",
						Toast.LENGTH_SHORT).show();
				break;

			case R.id.btnMasterListSortOrder:
				Toast.makeText(getActivity(), "\"" + "btnMasterListSortOrder" + "\"" + " is under construction.",
						Toast.LENGTH_SHORT).show();
				break;

			case R.id.btnColors:
				Toast.makeText(getActivity(), "\"" + "btnColors" + "\"" + " is under construction.", Toast.LENGTH_SHORT)
						.show();
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

	private void fillListPreferencesViews() {
		if (listSettings != null) {
			if (llFragListPreferences != null) {
				llFragListPreferences.setBackgroundColor(listSettings.getListBackgroundColor());
			}

			if (tvListTitle != null) {
				tvListTitle.setText(listSettings.getListTitle());
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
				int listSortOrder = listSettings.getListSortOrder();
				StringBuilder sb = new StringBuilder();
				sb.append("List Sort Order (");
				switch (listSortOrder) {
				case BY_GROUP_LIST_SORT_ORDER:
					sb.append("By Group)");
					break;

				case MANUAL_LIST_SORT_ORDER:
					sb.append("Manual)");
					break;
				default:
					//ALPHABETICAL_LIST_SORT_ORDER
					sb.append("Alphabetical)");
					break;
				}
				btnListSortOrder.setText(sb.toString());
				btnListSortOrder.setTextColor(listSettings.getItemNormalTextColor());
			}

			if (btnMasterListSortOrder != null) {
				int masterListSortOrder = listSettings.getMasterListSortOrder();
				StringBuilder sb = new StringBuilder();
				sb.append("Master List Sort Order (");
				switch (masterListSortOrder) {
				case BY_GROUP_MASTER_LIST_SORT_ORDER:
					sb.append("By Group)");
					break;

				case SELECTED_AT_TOP_MASTER_LIST_SORT_ORDER:
					sb.append("Selected at Top)");
					break;

				case SELECTED_AT_BOTTOM_MASTER_LIST_SORT_ORDER:
					sb.append("Selected at Bottom)");
					break;

				case LAST_USED_MASTER_LIST_SORT_ORDER:
					sb.append("Last Used)");
					break;

				default:
					//ALPHABETICAL_LIST_SORT_ORDER
					sb.append("Alphabetical)");
					break;
				}
				btnMasterListSortOrder.setText(sb.toString());
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

	protected void EditListTitle() {
		/*// Remove any currently showing dialog
		FragmentManager fm = getActivity().getSupportFragmentManager();
		Fragment prev = fm.findFragmentByTag("dialog_edit_list_title");
		if (prev != null) {
			FragmentTransaction ft = fm.beginTransaction();
			ft.remove(prev);
			ft.commit();
		}
		EditListTitleDialogFragment editListTitleDialog = EditListTitleDialogFragment
				.newInstance(mActiveListID);
		//editListTitleDialog.setTargetFragment(this, 1);

		editListTitleDialog.show(fm, "dialog_edit_list_title");*/

		/*DialogFragment dialogFrag = MyDialogFragment.newInstance(123);
		dialogFrag.setTargetFragment(this, DIALOG_FRAGMENT);
		dialogFrag.show(getFragmentManager().beginTransaction(), "dialog");*/

	}

	@Override
	public void onStart() {
		checkListID("onStart");
		super.onStart();
	}

	@Override
	public void onResume() {
		checkListID("onResume");
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
	}

}
