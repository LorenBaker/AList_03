package com.lbconsulting.alist_03;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.ColorPicker.OnColorChangedListener;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.lbconsulting.alist_03.adapters.ColorsPreviewPagerAdapter;
import com.lbconsulting.alist_03.classes.ListSettings;
import com.lbconsulting.alist_03.database.ListsTable;
import com.lbconsulting.alist_03.fragments.ColorsPreviewFragment;
import com.lbconsulting.alist_03.utilities.MyLog;

public class ColorsActivity extends FragmentActivity implements View.OnClickListener, OnColorChangedListener {
	private ColorsPreviewPagerAdapter mColorsPreviewPagerAdapter;
	private ViewPager mPager;
	private Cursor mAllListsCursor;

	private long mActiveListID = -1;
	private int mActiveListPosition = -1;
	private int mSelectedNavigationIndex = 0;
	private ListSettings mListSettings;

	private ScrollView mPresetsScrollView;
	private ScrollView mPickerScrollView;

	private Button btnPreset0;
	private Button btnPreset1;
	private Button btnPreset2;
	private Button btnPreset3;
	private Button btnPreset4;
	private Button btnPreset5;
	private Button btnApply;

	private Button btnSetTitleBackground;
	private Button btnSetTitleText;
	private Button btnSetListBackground;
	private Button btnSetListNormalText;
	private Button btnSetListStrikeOutText;
	private Button btnSetSeparatorBackground;
	private Button btnSetSeparatorText;

	private ColorPicker picker;
	private SVBar svBar;
	private OpacityBar opacityBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyLog.i("Colors_ACTIVITY", "onCreate");
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_colors);

		mPresetsScrollView = (ScrollView) findViewById(R.id.presetsScrollView);
		mPickerScrollView = (ScrollView) findViewById(R.id.pickerScrollView);

		SharedPreferences storedStates = getSharedPreferences("AList", MODE_PRIVATE);
		mActiveListID = storedStates.getLong("ActiveListID", -1);
		mActiveListPosition = storedStates.getInt("ActiveListPosition", -1);

		picker = (ColorPicker) findViewById(R.id.picker);
		svBar = (SVBar) findViewById(R.id.svbar);
		opacityBar = (OpacityBar) findViewById(R.id.opacitybar);

		picker.addSVBar(svBar);
		picker.addOpacityBar(opacityBar);
		picker.setOnColorChangedListener(this);

		final ActionBar actionBar = getActionBar();
		actionBar.setTitle(R.string.action_bar_title_select_list_colors);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// add a tabs to the action bar.
		actionBar.addTab(actionBar.newTab()
				.setText(R.string.actionBar_tab_color_presets)
				.setTabListener(new TabListener() {

					@Override
					public void onTabUnselected(Tab tab, FragmentTransaction ft) {
						// Do nothing
					}

					@Override
					public void onTabSelected(Tab tab, FragmentTransaction ft) {
						mPresetsScrollView.setVisibility(View.VISIBLE);
						mPickerScrollView.setVisibility(View.GONE);
					}

					@Override
					public void onTabReselected(Tab tab, FragmentTransaction ft) {
						// Do nothing
					}
				})
				);
		actionBar.addTab(actionBar.newTab()
				.setText(R.string.actionBar_tab_color_picker)
				.setTabListener(new TabListener() {

					@Override
					public void onTabUnselected(Tab tab, FragmentTransaction ft) {
						// Do nothing
					}

					@Override
					public void onTabSelected(Tab tab, FragmentTransaction ft) {
						mPresetsScrollView.setVisibility(View.GONE);
						mPickerScrollView.setVisibility(View.VISIBLE);
					}

					@Override
					public void onTabReselected(Tab tab, FragmentTransaction ft) {
						// Do nothing
					}
				})
				);

		mAllListsCursor = ListsTable.getAllLists(this);
		mListSettings = new ListSettings(this, mActiveListID);

		mColorsPreviewPagerAdapter = new ColorsPreviewPagerAdapter(getSupportFragmentManager(), this);
		mPager = (ViewPager) findViewById(R.id.colorsPreviewFragmentPager);
		mPager.setAdapter(mColorsPreviewPagerAdapter);
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

				// send broadcast to revert colors in fragment to those saved in the database
				String setListSettingsColorsKey = String.valueOf(mActiveListID)
						+ ColorsPreviewFragment.SET_LIST_SETTINGS_COLORS_BROADCAST_KEY;
				Intent setListSettingsColorsIntent = new Intent(setListSettingsColorsKey);
				LocalBroadcastManager.getInstance(ColorsActivity.this).sendBroadcast(setListSettingsColorsIntent);

				// set the ActiveID
				SetActiveListID(position);
				MyLog.d("CheckItems_ACTIVITY", "onPageSelected() - position = " + position + " ; listID = "
						+ mActiveListID);
			}
		});

		btnPreset0 = (Button) findViewById(R.id.btnPreset0);
		btnPreset1 = (Button) findViewById(R.id.btnPreset1);
		btnPreset2 = (Button) findViewById(R.id.btnPreset2);
		btnPreset3 = (Button) findViewById(R.id.btnPreset3);
		btnPreset4 = (Button) findViewById(R.id.btnPreset4);
		btnPreset5 = (Button) findViewById(R.id.btnPreset5);
		btnApply = (Button) findViewById(R.id.btnApply);

		btnSetTitleBackground = (Button) findViewById(R.id.btnSetTitleBackground);
		btnSetTitleText = (Button) findViewById(R.id.btnSetTitleText);
		btnSetListBackground = (Button) findViewById(R.id.btnSetListBackground);
		btnSetListNormalText = (Button) findViewById(R.id.btnSetListNormalText);
		btnSetListStrikeOutText = (Button) findViewById(R.id.btnSetListStrikeOutText);
		btnSetSeparatorBackground = (Button) findViewById(R.id.btnSetSeparatorBackground);
		btnSetSeparatorText = (Button) findViewById(R.id.btnSetSeparatorText);

		btnPreset0.setOnClickListener(this);
		btnPreset1.setOnClickListener(this);
		btnPreset2.setOnClickListener(this);
		btnPreset3.setOnClickListener(this);
		btnPreset4.setOnClickListener(this);
		btnPreset5.setOnClickListener(this);
		btnApply.setOnClickListener(this);

		btnSetTitleBackground.setOnClickListener(this);
		btnSetTitleText.setOnClickListener(this);
		btnSetListBackground.setOnClickListener(this);
		btnSetListNormalText.setOnClickListener(this);
		btnSetListStrikeOutText.setOnClickListener(this);
		btnSetSeparatorBackground.setOnClickListener(this);
		btnSetSeparatorText.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		String setPresetColorsKey = String.valueOf(mActiveListID)
				+ ColorsPreviewFragment.SET_PRESET_COLORS_BROADCAST_KEY;
		Intent setPresetColorsIntent = new Intent(setPresetColorsKey);

		String setViewIdKey = String.valueOf(mActiveListID)
				+ ColorsPreviewFragment.SET_VIEW_BROADCAST_KEY;
		Intent setViewIntent = new Intent(setViewIdKey);

		switch (v.getId()) {

		case R.id.btnPreset0:
			setPresetColorsIntent.putExtra("setPresetColors", ColorsPreviewFragment.SET_PRESET_0_COLORS);
			LocalBroadcastManager.getInstance(this).sendBroadcast(setPresetColorsIntent);
			break;

		case R.id.btnPreset1:
			setPresetColorsIntent.putExtra("setPresetColors", ColorsPreviewFragment.SET_PRESET_1_COLORS);
			LocalBroadcastManager.getInstance(this).sendBroadcast(setPresetColorsIntent);
			break;

		case R.id.btnPreset2:
			setPresetColorsIntent.putExtra("setPresetColors", ColorsPreviewFragment.SET_PRESET_2_COLORS);
			LocalBroadcastManager.getInstance(this).sendBroadcast(setPresetColorsIntent);
			break;

		case R.id.btnPreset3:
			setPresetColorsIntent.putExtra("setPresetColors", ColorsPreviewFragment.SET_PRESET_3_COLORS);
			LocalBroadcastManager.getInstance(this).sendBroadcast(setPresetColorsIntent);
			break;

		case R.id.btnPreset4:
			setPresetColorsIntent.putExtra("setPresetColors", ColorsPreviewFragment.SET_PRESET_4_COLORS);
			LocalBroadcastManager.getInstance(this).sendBroadcast(setPresetColorsIntent);
			break;

		case R.id.btnPreset5:
			setPresetColorsIntent.putExtra("setPresetColors", ColorsPreviewFragment.SET_PRESET_5_COLORS);
			LocalBroadcastManager.getInstance(this).sendBroadcast(setPresetColorsIntent);
			break;

		case R.id.btnApply:
			String applyPresetColorsKey = String.valueOf(mActiveListID)
					+ ColorsPreviewFragment.APPLY_PRESET_COLORS_BROADCAST_KEY;
			Intent applyPresetColorsIntent = new Intent(applyPresetColorsKey);
			LocalBroadcastManager.getInstance(this).sendBroadcast(applyPresetColorsIntent);
			break;

		case R.id.btnSetTitleBackground:
			setViewIntent.putExtra("setViewID", ColorsPreviewFragment.TITLE_BACKGROUND_COLOR);
			LocalBroadcastManager.getInstance(this).sendBroadcast(setViewIntent);
			break;

		case R.id.btnSetTitleText:
			setViewIntent.putExtra("setViewID", ColorsPreviewFragment.TITLE_TEXT_COLOR);
			LocalBroadcastManager.getInstance(this).sendBroadcast(setViewIntent);
			break;

		case R.id.btnSetListBackground:
			setViewIntent.putExtra("setViewID", ColorsPreviewFragment.LIST_BACKGROUND_COLOR);
			LocalBroadcastManager.getInstance(this).sendBroadcast(setViewIntent);
			break;

		case R.id.btnSetListNormalText:
			setViewIntent.putExtra("setViewID", ColorsPreviewFragment.LIST_NORMAL_TEXT_COLOR);
			LocalBroadcastManager.getInstance(this).sendBroadcast(setViewIntent);
			break;

		case R.id.btnSetListStrikeOutText:
			setViewIntent.putExtra("setViewID", ColorsPreviewFragment.LIST_STRIKEOUT_TEXT_COLOR);
			LocalBroadcastManager.getInstance(this).sendBroadcast(setViewIntent);
			break;

		case R.id.btnSetSeparatorBackground:
			setViewIntent.putExtra("setViewID", ColorsPreviewFragment.SEPARATOR_BACKGROUND_COLOR);
			LocalBroadcastManager.getInstance(this).sendBroadcast(setViewIntent);
			break;

		case R.id.btnSetSeparatorText:
			setViewIntent.putExtra("setViewID", ColorsPreviewFragment.SEPARATOR_TEXT_COLOR);
			LocalBroadcastManager.getInstance(this).sendBroadcast(setViewIntent);
			break;

		default:
			break;
		}
	}

	private void SetActiveListID(int position) {
		if (mAllListsCursor != null) {
			try {
				mAllListsCursor.moveToPosition(position);
				mActiveListID = mAllListsCursor.getLong(mAllListsCursor.getColumnIndexOrThrow(ListsTable.COL_LIST_ID));
				mListSettings = new ListSettings(this, mActiveListID);
				mActiveListPosition = position;
			} catch (Exception e) {
				MyLog.d("CheckItems_ACTIVITY", "Exception in getlistID: " + e);
			}
		}
	}

	@Override
	protected void onStart() {
		MyLog.i("Colors_ACTIVITY", "onStart");
		super.onStart();
	}

	@Override
	protected void onRestart() {
		MyLog.i("Colors_ACTIVITY", "onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		MyLog.i("Colors_ACTIVITY", "onResume");
		SharedPreferences storedStates = getSharedPreferences("AList", MODE_PRIVATE);
		mActiveListID = storedStates.getLong("ActiveListID", -1);
		mActiveListPosition = storedStates.getInt("ActiveListPosition", -1);
		mSelectedNavigationIndex = storedStates.getInt("SelectedNavigationIndex", 0);

		if (mActiveListPosition > -1) {
			mPager.setCurrentItem(mActiveListPosition);
		}
		getActionBar().setSelectedNavigationItem(mSelectedNavigationIndex);
		super.onResume();
	}

	@Override
	protected void onPause() {
		MyLog.i("Colors_ACTIVITY", "onPause");
		SharedPreferences preferences = getSharedPreferences("AList", MODE_PRIVATE);
		SharedPreferences.Editor applicationStates = preferences.edit();
		applicationStates.putLong("ActiveListID", mActiveListID);
		applicationStates.putInt("ActiveListPosition", mActiveListPosition);
		applicationStates.putInt("SelectedNavigationIndex", getActionBar().getSelectedNavigationIndex());
		applicationStates.commit();
		super.onPause();
	}

	@Override
	protected void onStop() {
		MyLog.i("Colors_ACTIVITY", "onStop");
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MyLog.i("Colors_ACTIVITY", "onCreateOptionsMenu");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onDestroy() {
		MyLog.i("Colors_ACTIVITY", "onDestroy");
		if (mAllListsCursor != null) {
			mAllListsCursor.close();
		}
		super.onDestroy();
	}

	@Override
	public void onColorChanged(int color) {

		String setByColorPickerKey = String.valueOf(mActiveListID)
				+ ColorsPreviewFragment.SET_BY_COLOR_PICKER_BROADCAST_KEY;
		Intent setByColorPickerIntent = new Intent(setByColorPickerKey);
		setByColorPickerIntent.putExtra("colorPickerColor", color);
		LocalBroadcastManager.getInstance(this).sendBroadcast(setByColorPickerIntent);

	}

}
