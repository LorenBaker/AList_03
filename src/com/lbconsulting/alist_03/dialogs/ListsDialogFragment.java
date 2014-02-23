package com.lbconsulting.alist_03.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.lbconsulting.alist_03.R;
import com.lbconsulting.alist_03.classes.ListSettings;
import com.lbconsulting.alist_03.database.ListsTable;
import com.lbconsulting.alist_03.fragments.ListPreferencesFragment;
import com.lbconsulting.alist_03.utilities.MyLog;

public class ListsDialogFragment extends DialogFragment {

	public static final int LIST_SORT_ORDER = 10;
	public static final int MASTER_LIST_SORT_ORDER = 20;
	public static final int EDIT_LIST_TITLE = 30;
	public static final int NEW_LIST = 40;

	private Button btnApply;
	private Button btnCancel;
	private EditText txtEditListTitle;

	private RadioGroup radioGroup_list_sort_order;
	private RadioGroup radioGroup_master_list_sort_order;

	private long mActiveListID;
	private ListSettings listSettings;
	private int mDialogType;
	private int mSortOrderResult;
	private String mListTitle;

	public interface SortOrderDialogListener {
		void onApplySortOrderDialog(int sortOrderResult);
	}

	public ListsDialogFragment() {
		// Empty constructor required for DialogFragment
	}

	/**
	 * Create a new instance of SortOrderDialogFragment
	 * 
	 * @param itemID
	 * @return SortOrderDialogFragment
	 */
	public static ListsDialogFragment newInstance(long listID, int dialogType) {
		ListsDialogFragment f = new ListsDialogFragment();
		// Supply itemID input as an argument.
		Bundle args = new Bundle();
		args.putLong("listID", listID);
		args.putInt("dialogType", dialogType);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		MyLog.i("SortOrderDialogFragment", "onAttach");
		super.onAttach(activity);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Store our listID
		outState.putLong("listID", this.mActiveListID);
		outState.putLong("dialogType", this.mDialogType);
		super.onSaveInstanceState(outState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState != null && savedInstanceState.containsKey("listID")) {
			mActiveListID = savedInstanceState.getLong("listID", 0);
			mDialogType = savedInstanceState.getInt("dialogType", 0);
		} else {
			Bundle bundle = getArguments();
			if (bundle != null) {
				mActiveListID = bundle.getLong("listID", 0);
				mDialogType = bundle.getInt("dialogType", 0);
			}
		}

		if (mActiveListID > 0) {
			listSettings = new ListSettings(getActivity(), mActiveListID);
		}

		// inflate view
		View view = null;
		switch (mDialogType) {
		case LIST_SORT_ORDER:
			MyLog.i("SortOrderDialogFragment", "onCreateView: List Sort Order");
			view = inflater.inflate(R.layout.dialog_list_sort_order, container);
			getDialog().setTitle(R.string.dialog_title_list_sort_order);
			break;

		case MASTER_LIST_SORT_ORDER:
			MyLog.i("SortOrderDialogFragment", "onCreateView: Master List Sort Order");
			view = inflater.inflate(R.layout.dialog_master_list_sort_order, container);
			getDialog().setTitle(R.string.dialog_title_master_list_sort_order);
			break;

		case EDIT_LIST_TITLE:
			MyLog.i("SortOrderDialogFragment", "onCreateView: Edit List Title");
			view = inflater.inflate(R.layout.dialog_edit_list_title, container);
			getDialog().setTitle(R.string.dialog_edit_list_title);
			break;

		case NEW_LIST:
			MyLog.i("SortOrderDialogFragment", "onCreateView: New List");
			view = inflater.inflate(R.layout.dialog_edit_list_title, container);
			getDialog().setTitle(R.string.dialog_new_list_title);
			break;

		default:
			break;
		}

		if (view != null) {
			btnApply = (Button) view.findViewById(R.id.btnApply);
			btnApply.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					ContentValues newFieldValues = new ContentValues();
					String key = String.valueOf(mActiveListID)
							+ ListPreferencesFragment.LIST_PREFERENCES_CHANGED_BROADCAST_KEY;
					Intent intent = new Intent(key);
					intent.putExtra("listID", mActiveListID);
					switch (mDialogType) {
					case LIST_SORT_ORDER:
						newFieldValues.put(ListsTable.COL_LIST_SORT_ORDER, mSortOrderResult);
						listSettings.updateListsTableFieldValues(newFieldValues);
						intent.putExtra("newListSortOrder", mSortOrderResult);
						LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
						break;

					case MASTER_LIST_SORT_ORDER:
						newFieldValues.put(ListsTable.COL_MASTER_LIST_SORT_ORDER, mSortOrderResult);
						listSettings.updateListsTableFieldValues(newFieldValues);
						intent.putExtra("newMasterListSortOrder", mSortOrderResult);
						LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
						break;

					case EDIT_LIST_TITLE:
						String newListTitle = txtEditListTitle.getText().toString();
						newListTitle = newListTitle.trim();
						newFieldValues.put(ListsTable.COL_LIST_TITLE, newListTitle);
						listSettings.updateListsTableFieldValues(newFieldValues);
						intent.putExtra("editedListTitle", newListTitle);
						LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
						break;

					case NEW_LIST:
						newListTitle = txtEditListTitle.getText().toString();
						newListTitle = newListTitle.trim();
						long newListID = ListsTable.CreateNewList(getActivity(), newListTitle);
						intent.putExtra("newListID", newListID);
						LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
						break;

					default:
						break;
					}

					getDialog().dismiss();
				}
			});

			btnCancel = (Button) view.findViewById(R.id.btnCancel);
			btnCancel.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					getDialog().dismiss();
				}
			});

			radioGroup_list_sort_order = (RadioGroup) view.findViewById(R.id.radioGroup_list_sort_order);
			if (radioGroup_list_sort_order != null) {
				// We're Displaying the List Sort Order dialog
				mSortOrderResult = listSettings.getListSortOrder();
				RadioButton rb;
				switch (mSortOrderResult) {
				case ListPreferencesFragment.ALPHABETICAL:
					rb = (RadioButton) view.findViewById(R.id.rbAlphabetical_list);
					if (rb != null) {
						rb.setChecked(true);
					}
					break;
				case ListPreferencesFragment.BY_GROUP:
					rb = (RadioButton) view.findViewById(R.id.rbByGroup_list);
					if (rb != null) {
						rb.setChecked(true);
					}
					break;
				case ListPreferencesFragment.MANUAL:
					rb = (RadioButton) view.findViewById(R.id.rbManual);
					if (rb != null) {
						rb.setChecked(true);
					}
					break;
				default:
					break;
				}

				radioGroup_list_sort_order.setOnCheckedChangeListener(new OnCheckedChangeListener()
				{
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.rbAlphabetical_list:
							mSortOrderResult = ListPreferencesFragment.ALPHABETICAL;
							break;
						case R.id.rbByGroup_list:
							mSortOrderResult = ListPreferencesFragment.BY_GROUP;
							break;
						case R.id.rbManual:
							mSortOrderResult = ListPreferencesFragment.MANUAL;
							break;
						default:
							mSortOrderResult = ListPreferencesFragment.ALPHABETICAL;
							break;
						}
					}
				});
			}

			radioGroup_master_list_sort_order = (RadioGroup) view
					.findViewById(R.id.radioGroup_master_list_sort_order);
			if (radioGroup_master_list_sort_order != null) {
				// We're Displaying the Master List Sort Order dialog
				mSortOrderResult = listSettings.getMasterListSortOrder();
				RadioButton rb;
				switch (mSortOrderResult) {
				case ListPreferencesFragment.ALPHABETICAL:
					rb = (RadioButton) view.findViewById(R.id.rbAlphabetical_master_list);
					if (rb != null) {
						rb.setChecked(true);
					}
					break;
				case ListPreferencesFragment.BY_GROUP:
					rb = (RadioButton) view.findViewById(R.id.rbByGroup_master_list);
					if (rb != null) {
						rb.setChecked(true);
					}
					break;
				case ListPreferencesFragment.SELECTED_AT_TOP:
					rb = (RadioButton) view.findViewById(R.id.rbSelectedItemsAtTop);
					if (rb != null) {
						rb.setChecked(true);
					}
					break;
				case ListPreferencesFragment.SELECTED_AT_BOTTOM:
					rb = (RadioButton) view.findViewById(R.id.rbSelectedItemsAtBottom);
					if (rb != null) {
						rb.setChecked(true);
					}
					break;
				case ListPreferencesFragment.LAST_USED:
					rb = (RadioButton) view.findViewById(R.id.rbLastUsed);
					if (rb != null) {
						rb.setChecked(true);
					}
					break;
				default:
					break;
				}

				radioGroup_master_list_sort_order.setOnCheckedChangeListener(new OnCheckedChangeListener()
				{
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.rbAlphabetical_master_list:
							mSortOrderResult = ListPreferencesFragment.ALPHABETICAL;
							break;
						case R.id.rbByGroup_master_list:
							mSortOrderResult = ListPreferencesFragment.BY_GROUP;
							break;
						case R.id.rbSelectedItemsAtTop:
							mSortOrderResult = ListPreferencesFragment.SELECTED_AT_TOP;
							break;
						case R.id.rbSelectedItemsAtBottom:
							mSortOrderResult = ListPreferencesFragment.SELECTED_AT_BOTTOM;
							break;
						case R.id.rbLastUsed:
							mSortOrderResult = ListPreferencesFragment.LAST_USED;
							break;
						default:
							mSortOrderResult = ListPreferencesFragment.ALPHABETICAL;
							break;
						}
					}
				});
			}

			txtEditListTitle = (EditText) view.findViewById(R.id.txtEditListTitle);
			if (txtEditListTitle != null) {
				switch (mDialogType) {
				case EDIT_LIST_TITLE:
					// We're displaying the Edit List Title dialog
					mListTitle = listSettings.getListTitle();
					txtEditListTitle.setText(mListTitle);
					break;

				case NEW_LIST:
					// We're creating a new List
					break;

				default:
					break;

				}

				if (txtEditListTitle.requestFocus()) {
					getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
				}
			}
		}

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		MyLog.i("SortOrderDialogFragment", "onActivityCreated");
		super.onActivityCreated(savedInstanceState);

		Bundle bundle = this.getArguments();
		if (bundle != null) {
			mActiveListID = bundle.getLong("listID", 0);
		}
	}

	public void onRadioButtonClicked(View view) {
		MyLog.i("SortOrderDialogFragment", "onRadioButtonClicked; view id = " + view.getId());
		switch (view.getId()) {
		case R.id.rbAlphabetical_list:
			mSortOrderResult = ListPreferencesFragment.ALPHABETICAL;
			break;
		case R.id.rbByGroup_list:
			mSortOrderResult = ListPreferencesFragment.BY_GROUP;
			break;
		case R.id.rbManual:
			mSortOrderResult = ListPreferencesFragment.MANUAL;
			break;
		case R.id.rbAlphabetical_master_list:
			mSortOrderResult = ListPreferencesFragment.ALPHABETICAL;
			break;
		case R.id.rbByGroup_master_list:
			mSortOrderResult = ListPreferencesFragment.BY_GROUP;
			break;
		case R.id.rbSelectedItemsAtTop:
			mSortOrderResult = ListPreferencesFragment.SELECTED_AT_TOP;
			break;
		case R.id.rbSelectedItemsAtBottom:
			mSortOrderResult = ListPreferencesFragment.SELECTED_AT_BOTTOM;
			break;
		case R.id.rbLastUsed:
			mSortOrderResult = ListPreferencesFragment.LAST_USED;
			break;
		default:
			break;
		}
		String toastMsg = "Selection = " + mSortOrderResult;
		Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyLog.i("SortOrderDialogFragment", "onCreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		MyLog.i("SortOrderDialogFragment", "onCreateDialog");
		return super.onCreateDialog(savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		MyLog.i("SortOrderDialogFragment", "onDestroyView");
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		MyLog.i("SortOrderDialogFragment", "onDetach");
		super.onDetach();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		MyLog.i("SortOrderDialogFragment", "onDismiss");
		super.onDismiss(dialog);
	}

	@Override
	public void onStart() {
		MyLog.i("SortOrderDialogFragment", "onStart");
		super.onStart();
	}

	@Override
	public void onStop() {
		MyLog.i("SortOrderDialogFragment", "onStop");
		super.onStop();
	}

	@Override
	public void onDestroy() {
		MyLog.i("SortOrderDialogFragment", "onDestroy");
		super.onDestroy();
	}

}
