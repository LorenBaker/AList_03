package com.lbconsulting.alist_03.classes;

import java.util.Calendar;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.provider.Settings.Secure;

import com.lbconsulting.alist_03.database.GroupsTable;
import com.lbconsulting.alist_03.database.ItemsTable;
import com.lbconsulting.alist_03.database.ListsTable;

public class ItemsSubmission {
	private String mAuthorFirstName;
	private String mAuthorLastName;
	private Cursor mList;
	private Cursor mItems;
	private Context mContext;

	public ItemsSubmission(Context context, String authorFirstName, String authorLastName, Cursor list, Cursor items) {
		mAuthorFirstName = authorFirstName;
		mAuthorLastName = authorLastName;
		mList = list;
		mItems = items;
		mContext = context;
	}

	public final static String TAG_ADDRESS = "Address";
	public final static String TAG_AUTHOR = "Author";
	public final static String TAG_AUTHOR_INFORMATION = "AuthorInformation";
	public final static String TAG_CITY = "City";
	public final static String TAG_DEVICE_API_VERSION = "DeviceApiVersion";
	public final static String TAG_DEVICE_ID = "DeviceID";
	public final static String TAG_DEVICE_MANUFACTURER = "DeviceManufacturer";
	public final static String TAG_DEVICE_MODEL = "DeviceModel";
	// public final static String TAG_EMAIL_ADDRESS = "EmailAddress";
	public final static String TAG_FIRST_NAME = "FirstName";
	// public final static String TAG_GPS_COORDINATES = "GPSCoordinates";
	// public final static String TAG_GROUP_LOCATION = "GroupLocation";
	public final static String TAG_GROUP_NAME = "GroupName";
	public final static String TAG_LAST_NAME = "LastName";
	// public final static String TAG_LATITUDE = "Latitude";
	public final static String TAG_LIST_DETAILS = "ListDetails";
	public final static String TAG_LIST_TITLE = "ListTitle";
	public final static String TAG_LOCAL_DEVICE_LIST_ID = "LocalDeviceListID";
	public final static String TAG_LOCAL_DEVICE_STORE_ID = "LocalDeviceStoreID";
	// public final static String TAG_LONGITUDE = "Longitude";
	// public final static String TAG_PHONE_NUMBER = "PhoneNumber";
	public final static String TAG_STATE = "State";
	// public final static String TAG_STORE_DESCRIPTION = "StoreDescription";
	// public final static String TAG_STORE = "Store";
	public final static String TAG_ITEMS_SUBMISSION = "ItemsSubmission";
	public final static String TAG_ITEM = "Item";
	public final static String TAG_ITEM_NAME = "ItemName";

	public final static String TAG_LOCAL_DEVICE_ITEM_ID = "LocalDeviceItemID";
	public final static String TAG_ITEM_UNIQUE_ID = "ItemUniqueID";

	// public final static String TAG_STORE_LOCATION = "StoreLocation";
	// public final static String TAG_STORE_NAME = "StoreName";
	public final static String TAG_STREET1 = "Street1";
	public final static String TAG_STREET2 = "Street2";
	public final static String TAG_SUBMISSION_DATE = "SubmissionDate";
	public final static String TAG_UNIQUE_ID = "UniqueID";
	// public final static String TAG_WEBSITE_URL = "WebsiteURL";
	public final static String TAG_ZIP = "Zip";

	public String getXml() {
		AListXmlSerializer serializer = new AListXmlSerializer();

		try {
			serializer.startDocument("UTF-8", true);
			serializer.startTag(TAG_ITEMS_SUBMISSION);

			serializeSubmissionDate(serializer);
			serializeAuthorInformatoin(serializer);
			serializeListDetails(serializer);
			serializeItems(serializer);

			serializer.endTag(TAG_ITEMS_SUBMISSION);
			serializer.endDocument();

			return serializer.getXml();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// SUBMISSION DATE
	private void serializeSubmissionDate(AListXmlSerializer serializer) {
		serializer.text(TAG_SUBMISSION_DATE, String.valueOf(Calendar.getInstance().getTimeInMillis()));
	}

	// AUTHOR INFORMATION
	public void serializeAuthorInformatoin(AListXmlSerializer serializer) {
		serializer.startTag(TAG_AUTHOR_INFORMATION);

		serializeAuthorFirstName(serializer);
		serializeAuthorLastName(serializer);
		serializeDeviceID(serializer);
		serializeDeviceManufacturer(serializer);
		serializeDeviceModel(serializer);
		serializeDeviceApiVersion(serializer);

		serializer.endTag(TAG_AUTHOR_INFORMATION);
	}

	private void serializeAuthorFirstName(AListXmlSerializer serializer) {
		boolean haveAuthorFirstNameValue = hasValueToSerialize(mAuthorFirstName);
		if (haveAuthorFirstNameValue) {
			serializer.text(TAG_FIRST_NAME, mAuthorFirstName);
		}
	}

	private void serializeAuthorLastName(AListXmlSerializer serializer) {

		boolean haveAuthorLastNameValue = hasValueToSerialize(mAuthorLastName);
		if (haveAuthorLastNameValue) {
			serializer.text(TAG_LAST_NAME, mAuthorLastName);
		}
	}

	private void serializeDeviceManufacturer(AListXmlSerializer serializer) {
		String manufacturer = Build.MANUFACTURER;
		boolean haveManufacturerValue = hasValueToSerialize(manufacturer);
		if (haveManufacturerValue) {
			serializer.text(TAG_DEVICE_MANUFACTURER, manufacturer);
		}
	}

	private void serializeDeviceModel(AListXmlSerializer serializer) {
		String model = Build.MODEL;
		boolean haveModelValue = hasValueToSerialize(model);
		if (haveModelValue) {
			serializer.text(TAG_DEVICE_MODEL, model);
		}
	}

	private void serializeDeviceApiVersion(AListXmlSerializer serializer) {
		int currentApiVersion = android.os.Build.VERSION.SDK_INT;
		serializer.text(TAG_DEVICE_API_VERSION, String.valueOf(currentApiVersion));
	}

	private void serializeDeviceID(AListXmlSerializer serializer) {
		String deviceId = Secure.getString(mContext.getContentResolver(), Secure.ANDROID_ID);
		boolean haveDeviceIdValue = hasValueToSerialize(deviceId);
		if (haveDeviceIdValue) {
			serializer.text(TAG_DEVICE_ID, deviceId);
		}
	}

	// LIST DETAILS
	private void serializeListDetails(AListXmlSerializer serializer) {
		if (mList != null) {

			mList.moveToFirst();
			String listTitle = mList.getString(mList.getColumnIndexOrThrow(ListsTable.COL_LIST_TITLE));
			long localDeviceListID = mList.getLong(mList.getColumnIndexOrThrow(ListsTable.COL_LIST_ID));
			String uniqueID = "!!! List Unique ID !!!";

			serializer.startTag(TAG_LIST_DETAILS);

			serializer.text(TAG_UNIQUE_ID, uniqueID);
			serializer.text(TAG_LIST_TITLE, listTitle);
			serializer.text(TAG_LOCAL_DEVICE_LIST_ID, String.valueOf(localDeviceListID));

			serializer.endTag(TAG_LIST_DETAILS);
		}
	}

	// ITEMS
	private void serializeItems(AListXmlSerializer serializer) {
		if (mItems != null && mItems.getCount() > 0) {
			mItems.moveToPosition(-1);

			String itemName = "";
			long itemID = -1;
			String itemLocalID = "";
			String itemUniqueID = "!*!*!* Item Unique ID !*!*!*";
			String groupName = "";
			boolean haveItemNameValue = false;
			boolean haveGroupNameValue = false;

			while (mItems.moveToNext()) {
				itemName = mItems.getString(mItems.getColumnIndexOrThrow(ItemsTable.COL_ITEM_NAME));
				itemID = mItems.getLong(mItems.getColumnIndexOrThrow(ItemsTable.COL_ITEM_ID));
				itemLocalID = String.valueOf(itemID);
				groupName = mItems.getString(mItems.getColumnIndexOrThrow(GroupsTable.COL_GROUP_NAME));

				haveItemNameValue = hasValueToSerialize(itemName);
				if (haveItemNameValue) {
					serializer.startTag(TAG_ITEM);

					serializer.text(TAG_ITEM_NAME, itemName);
					serializer.text(TAG_ITEM_LOCAL_ID, itemLocalID);
					serializer.text(TAG_ITEM_UNIQUE_ID, itemUniqueID);

					haveGroupNameValue = hasValueToSerialize(groupName);
					if (haveGroupNameValue) {
						if (!groupName.equals(GroupsTable.DEFAULT_GROUP_VALUE)) {
							serializer.text(TAG_GROUP_NAME, groupName);
						}
					}
					serializer.endTag(TAG_ITEM);
				}
			}
		}
	}

	// HELPER METHODS
	private boolean hasValueToSerialize(String value) {
		if (value != null && !value.isEmpty()) {
			return true;
		}
		return false;
	}
}
