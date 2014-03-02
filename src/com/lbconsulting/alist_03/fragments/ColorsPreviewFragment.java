package com.lbconsulting.alist_03.fragments;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lbconsulting.alist_03.R;
import com.lbconsulting.alist_03.classes.ListSettings;
import com.lbconsulting.alist_03.utilities.MyLog;

public class ColorsPreviewFragment extends Fragment {

	private long mActiveListID = -9999;

	private ListSettings listSettings;

	private LinearLayout colorsPreviewFragmentLinearLayout;
	private TextView tvListTitle;
	private TextView tvListNormalText;
	private TextView tvListStrikeOutText;
	private TextView tvListItemSeparator;
	private TextView tvMasterListNotSelectedText;
	private TextView tvMasterListSelectedText;

	public ColorsPreviewFragment() {
		// Empty constructor
	}

	/**
	 * Create a new instance of ColorsPreviewFragment
	 * 
	 * @param itemID
	 * @return ColorsPreviewFragment
	 */
	public static ColorsPreviewFragment newInstance(long newListID) {

		if (newListID < 2) {
			MyLog.e("ColorsPreviewFragment: newInstance; listID = " + newListID, " is less than 2!!!!");
			return null;
		} else {

			ColorsPreviewFragment f = new ColorsPreviewFragment();

			// Supply listID input as an argument.
			Bundle args = new Bundle();
			args.putLong("listID", newListID);
			f.setArguments(args);

			return f;
		}
	}

	@Override
	public void onAttach(Activity activity) {
		MyLog.i("ColorsPreviewFragment", "onAttach");
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyLog.i("ColorsPreviewFragment", "onCreate");
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
		MyLog.i("ColorsPreviewFragment", "onCreateView");

		if (savedInstanceState != null && savedInstanceState.containsKey("listID")) {
			mActiveListID = savedInstanceState.getLong("listID", 0);
		} else {
			Bundle bundle = getArguments();
			if (bundle != null)
				mActiveListID = bundle.getLong("listID", 0);
		}

		View view = inflater.inflate(R.layout.frag_colors_preview, container, false);

		listSettings = new ListSettings(getActivity(), mActiveListID);
		if (listSettings != null) {

			colorsPreviewFragmentLinearLayout = (LinearLayout) view
					.findViewById(R.id.colorsPreviewFragmentLinearLayout);
			if (colorsPreviewFragmentLinearLayout != null) {
				colorsPreviewFragmentLinearLayout.setBackgroundColor(listSettings.getListBackgroundColor());
			}

			tvListTitle = (TextView) view.findViewById(R.id.tvListTitle);
			if (tvListTitle != null) {
				tvListTitle.setText(listSettings.getListTitle());
				tvListTitle.setBackgroundColor(this.listSettings.getTitleBackgroundColor());
				tvListTitle.setTextColor(this.listSettings.getTitleTextColor());
			}

			tvListNormalText = (TextView) view.findViewById(R.id.tvListNormalText);
			if (tvListNormalText != null) {
				tvListNormalText.setBackgroundColor(this.listSettings.getListBackgroundColor());
				tvListNormalText.setTextColor(this.listSettings.getItemNormalTextColor());
			}

			tvListStrikeOutText = (TextView) view.findViewById(R.id.tvListStrikeOutText);
			if (tvListStrikeOutText != null) {
				tvListStrikeOutText.setBackgroundColor(this.listSettings.getListBackgroundColor());
				tvListStrikeOutText.setTextColor(this.listSettings.getItemStrikeoutTextColor());
				tvListStrikeOutText.setPaintFlags(tvListStrikeOutText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			}

			tvListItemSeparator = (TextView) view.findViewById(R.id.tvListItemSeparator);
			if (tvListItemSeparator != null) {
				tvListItemSeparator.setBackgroundColor(this.listSettings.getSeparatorBackgroundColor());
				tvListItemSeparator.setTextColor(this.listSettings.getSeparatorTextColor());
			}

			tvMasterListNotSelectedText = (TextView) view.findViewById(R.id.tvMasterListNotSelectedText);
			if (tvMasterListNotSelectedText != null) {
				tvMasterListNotSelectedText.setBackgroundColor(this.listSettings.getMasterListBackgroundColor());
				tvMasterListNotSelectedText.setTextColor(this.listSettings.getMasterListItemNormalTextColor());
			}

			tvMasterListSelectedText = (TextView) view.findViewById(R.id.tvMasterListSelectedText);
			if (tvMasterListSelectedText != null) {
				tvMasterListSelectedText.setBackgroundColor(this.listSettings.getMasterListBackgroundColor());
				tvMasterListSelectedText.setTextColor(this.listSettings.getMasterListItemSelectedTextColor());
			}

		}
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		MyLog.i("ColorsPreviewFragment", "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		MyLog.i("ColorsPreviewFragment", "onStart");
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
		MyLog.i("ColorsPreviewFragment", "onResume");
		Bundle bundle = this.getArguments();
		if (bundle != null) {
			mActiveListID = bundle.getLong("listID", 0);
		}

		listSettings = new ListSettings(getActivity(), mActiveListID);
	}

	@Override
	public void onPause() {
		MyLog.i("ColorsPreviewFragment", "onPause");
		super.onPause();
	}

	@Override
	public void onStop() {
		MyLog.i("ColorsPreviewFragment", "onStop");
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		MyLog.i("ColorsPreviewFragment", "onDestroyView");
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		MyLog.i("ColorsPreviewFragment", "onDestroy");
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		MyLog.i("ColorsPreviewFragment", "onDetach");
		super.onDetach();
	}

	@Override
	public View getView() {
		MyLog.i("ColorsPreviewFragment", "getView");
		return super.getView();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		MyLog.i("ColorsPreviewFragment", "onViewCreated");
		super.onViewCreated(view, savedInstanceState);
	}

}
