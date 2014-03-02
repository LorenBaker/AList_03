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

	private ListSettings mListSettings;

	private LinearLayout colorsPreviewFragmentLinearLayout;
	private TextView tvListTitle;
	private TextView tvListNormalText;
	private TextView tvListStrikeOutText;
	private TextView tvListItemSeparator;
	private TextView tvMasterListNotSelectedText;
	private TextView tvMasterListSelectedText;

	private int mTitle_background_color;
	private int mTitle_text_color;
	private int mList_background_color;
	private int mList_normal_text_color;
	private int mList_strikeout_text_color;
	private int mSeparator_background_color;
	private int mSeparator_text_color;

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

		mListSettings = new ListSettings(getActivity(), mActiveListID);
		if (mListSettings != null) {

			getColorsFromListSettings();

			colorsPreviewFragmentLinearLayout = (LinearLayout) view
					.findViewById(R.id.colorsPreviewFragmentLinearLayout);

			tvListTitle = (TextView) view.findViewById(R.id.tvListTitle);
			if (tvListTitle != null) {
				tvListTitle.setText(mListSettings.getListTitle());
			}

			tvListNormalText = (TextView) view.findViewById(R.id.tvListNormalText);

			tvListStrikeOutText = (TextView) view.findViewById(R.id.tvListStrikeOutText);
			if (tvListStrikeOutText != null) {
				tvListStrikeOutText.setPaintFlags(tvListStrikeOutText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			}

			tvListItemSeparator = (TextView) view.findViewById(R.id.tvListItemSeparator);

			setAllColors();

			/*tvMasterListNotSelectedText = (TextView) view.findViewById(R.id.tvMasterListNotSelectedText);
			if (tvMasterListNotSelectedText != null) {
				tvMasterListNotSelectedText.setBackgroundColor(this.listSettings.getMasterListBackgroundColor());
				tvMasterListNotSelectedText.setTextColor(this.listSettings.getMasterListItemNormalTextColor());
			}

			tvMasterListSelectedText = (TextView) view.findViewById(R.id.tvMasterListSelectedText);
			if (tvMasterListSelectedText != null) {
				tvMasterListSelectedText.setBackgroundColor(this.listSettings.getMasterListBackgroundColor());
				tvMasterListSelectedText.setTextColor(this.listSettings.getMasterListItemSelectedTextColor());
			}*/

		}
		return view;
	}

	private void setAllColors() {
		setPreviewFragmentLinearLayoutBackgroundColor();
		setTitleBackgroundColor();
		setTitleTextColor();
		setListBackgroundColor();
		setItemNormalTextColor();
		setItemStrikeoutTextColor();
		setSeparatorBackgroundColor();
		setSeparatorTextColor();
	}

	private void setPreviewFragmentLinearLayoutBackgroundColor() {
		if (colorsPreviewFragmentLinearLayout != null) {
			colorsPreviewFragmentLinearLayout.setBackgroundColor(mList_background_color);
		}
	}

	private void setTitleBackgroundColor() {
		if (tvListTitle != null) {
			tvListTitle.setBackgroundColor(mTitle_background_color);
		}
	}

	private void setTitleTextColor() {
		if (tvListTitle != null) {
			tvListTitle.setTextColor(mTitle_text_color);
		}
	}

	private void setListBackgroundColor() {
		if (tvListNormalText != null && tvListStrikeOutText != null) {
			tvListNormalText.setBackgroundColor(mList_background_color);
			tvListStrikeOutText.setBackgroundColor(mList_background_color);
		}
	}

	private void setItemNormalTextColor() {
		if (tvListNormalText != null) {
			tvListNormalText.setTextColor(mList_normal_text_color);
		}
	}

	private void setItemStrikeoutTextColor() {
		if (tvListStrikeOutText != null) {
			tvListStrikeOutText.setTextColor(mList_strikeout_text_color);
		}
	}

	private void setSeparatorBackgroundColor() {
		if (tvListItemSeparator != null) {
			tvListItemSeparator.setBackgroundColor(mSeparator_background_color);
		}
	}

	private void setSeparatorTextColor() {
		if (tvListItemSeparator != null) {
			tvListItemSeparator.setTextColor(mSeparator_text_color);
		}
	}

	private void getColorsFromListSettings() {
		mTitle_background_color = mListSettings.getTitleBackgroundColor();
		mTitle_text_color = mListSettings.getTitleTextColor();
		mList_background_color = mListSettings.getListBackgroundColor();
		mList_normal_text_color = mListSettings.getItemNormalTextColor();
		mList_strikeout_text_color = mListSettings.getItemStrikeoutTextColor();
		mSeparator_background_color = mListSettings.getSeparatorBackgroundColor();
		mSeparator_text_color = mListSettings.getSeparatorTextColor();
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

		mListSettings = new ListSettings(getActivity(), mActiveListID);
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
