package com.lbconsulting.alist_03.classes;

import java.util.Calendar;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.provider.Settings.Secure;

import com.lbconsulting.alist_03.database.GroupsTable;
import com.lbconsulting.alist_03.database.ListsTable;
import com.lbconsulting.alist_03.database.LocationsTable;
import com.lbconsulting.alist_03.database.StoresTable;

public class StoreDataSubmission {
	private String mAuthorFirstName;
	private String mAuthorLastName;
	private Cursor mStore;
	private Cursor mGroupLocations;
	private Cursor mList;
	private Context mContext;

	public StoreDataSubmission() {

	}

	public StoreDataSubmission(Context context, String authorFirstName, String authorLastName, Cursor list, Cursor store, Cursor groupLocations) {
		mAuthorFirstName = authorFirstName;
		mAuthorLastName = authorLastName;
		mList = list;
		mStore = store;
		mGroupLocations = groupLocations;
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
	public final static String TAG_EMAIL_ADDRESS = "EmailAddress";
	public final static String TAG_FIRST_NAME = "FirstName";
	public final static String TAG_GPS_COORDINATES = "GPSCoordinates";
	public final static String TAG_GROUP_LOCATION = "GroupLocation";
	public final static String TAG_GROUP_NAME = "GroupName";
	public final static String TAG_LAST_NAME = "LastName";
	public final static String TAG_LATITUDE = "Latitude";
	public final static String TAG_LIST_DETAILS = "ListDetails";
	public final static String TAG_LIST_TITLE = "ListTitle";
	public final static String TAG_LOCAL_DEVICE_LIST_ID = "LocalDeviceListID";
	public final static String TAG_LOCAL_DEVICE_STORE_ID = "LocalDeviceStoreID";
	public final static String TAG_LONGITUDE = "Longitude";
	public final static String TAG_PHONE_NUMBER = "PhoneNumber";
	public final static String TAG_STATE = "State";
	public final static String TAG_STORE_DESCRIPTION = "StoreDescription";
	public final static String TAG_STORE = "Store";
	public final static String TAG_STORE_SUBMISSION = "StoreSubmission";
	public final static String TAG_STORE_LOCATION = "StoreLocation";
	public final static String TAG_STORE_NAME = "StoreName";
	public final static String TAG_STREET1 = "Street1";
	public final static String TAG_STREET2 = "Street2";
	public final static String TAG_SUBMISSION_DATE = "SubmissionDate";
	public final static String TAG_UNIQUE_ID = "UniqueID";
	public final static String TAG_WEBSITE_URL = "WebsiteURL";
	public final static String TAG_ZIP = "Zip";

	public String getXml() {
		AListXmlSerializer serializer = new AListXmlSerializer();

		try {
			serializer.startDocument("UTF-8", true);
			serializer.startTag(TAG_STORE_SUBMISSION);

			serializeSubmissionDate(serializer);
			serializeAuthorInformatoin(serializer);
			serializeStoreDescription(serializer);
			serializeGroupLocations(serializer);

			serializer.endTag(TAG_STORE_SUBMISSION);
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

	// STORE DESCRIPTION
	public void serializeStoreDescription(AListXmlSerializer serializer) {
		serializer.startTag(TAG_STORE_DESCRIPTION);

		serializeStoreID(serializer);
		serializeListDetails(serializer);
		serializeStoreAddress(serializer);
		serializeGPSCoordinates(serializer);
		serializeWebsiteURL(serializer);
		serializePhoneNuber(serializer);

		serializer.endTag(TAG_STORE_DESCRIPTION);
	}

	private void serializeStoreID(AListXmlSerializer serializer) {
		if (mStore != null) {

			mStore.moveToFirst();
			String storeName = mStore.getString(mStore.getColumnIndexOrThrow(StoresTable.COL_STORE_NAME));
			long localDeviceStoreID = mStore.getLong(mStore.getColumnIndexOrThrow(StoresTable.COL_STORE_ID));
			String uniqueID = "!!! StoreUniqueID !!!";

			serializer.startTag(TAG_STORE);

			serializer.text(TAG_UNIQUE_ID, uniqueID);
			serializer.text(TAG_STORE_NAME, storeName);
			serializer.text(TAG_LOCAL_DEVICE_STORE_ID, String.valueOf(localDeviceStoreID));

			serializer.endTag(TAG_STORE);
		}
	}

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

	private void serializeStoreAddress(AListXmlSerializer serializer) {
		if (mStore != null) {
			boolean haveStoreAddressValuesToSerialize = false;

			mStore.moveToFirst();
			String street1 = mStore.getString(mStore.getColumnIndexOrThrow(StoresTable.COL_STREET1));
			String street2 = mStore.getString(mStore.getColumnIndexOrThrow(StoresTable.COL_STREET2));
			String city = mStore.getString(mStore.getColumnIndexOrThrow(StoresTable.COL_CITY));
			String state = mStore.getString(mStore.getColumnIndexOrThrow(StoresTable.COL_STATE));
			String zip = mStore.getString(mStore.getColumnIndexOrThrow(StoresTable.COL_ZIP));

			boolean haveStreet1Value = hasValueToSerialize(street1);
			boolean haveStreet2Value = hasValueToSerialize(street2);
			boolean haveCityValue = hasValueToSerialize(city);
			boolean haveStateValue = hasValueToSerialize(state);
			boolean haveZipValue = hasValueToSerialize(zip);

			haveStoreAddressValuesToSerialize = haveStreet1Value || haveStreet2Value || haveCityValue || haveStateValue || haveZipValue;
			if (haveStoreAddressValuesToSerialize) {
				serializer.startTag(TAG_ADDRESS);

				if (haveStreet1Value) {
					serializer.text(TAG_STREET1, street1);
				}
				if (haveStreet2Value) {
					serializer.text(TAG_STREET2, street2);
				}
				if (haveCityValue) {
					serializer.text(TAG_CITY, city);
				}
				if (haveStateValue) {
					serializer.text(TAG_STATE, state);
				}
				if (haveZipValue) {
					serializer.text(TAG_ZIP, zip);
				}

				serializer.endTag(TAG_ADDRESS);
			}
		}
	}

	private void serializeGPSCoordinates(AListXmlSerializer serializer) {
		if (mStore != null) {
			boolean haveStoreAddressValuesToSerialize = false;

			mStore.moveToFirst();
			String gpsLatitude = mStore.getString(mStore.getColumnIndexOrThrow(StoresTable.COL_GPS_LATITUDE));
			String gpsLongitude = mStore.getString(mStore.getColumnIndexOrThrow(StoresTable.COL_GPS_LONGITUDE));

			boolean haveGPSLatitudeValue = hasValueToSerialize(gpsLatitude);
			boolean haveGPSLongitudeValue = hasValueToSerialize(gpsLongitude);

			haveStoreAddressValuesToSerialize = haveGPSLatitudeValue || haveGPSLongitudeValue;
			if (haveStoreAddressValuesToSerialize) {
				serializer.startTag(TAG_GPS_COORDINATES);

				if (haveGPSLatitudeValue) {
					serializer.text(TAG_LATITUDE, gpsLatitude);

				}
				if (haveGPSLongitudeValue) {
					serializer.text(TAG_LONGITUDE, gpsLongitude);
				}

				serializer.endTag(TAG_GPS_COORDINATES);
			}
		}
	}

	private void serializeWebsiteURL(AListXmlSerializer serializer) {
		if (mStore != null) {
			mStore.moveToFirst();
			String webSiteURL = mStore.getString(mStore.getColumnIndexOrThrow(StoresTable.COL_WEBSITE_URL));

			boolean haveWebSiteURLValue = hasValueToSerialize(webSiteURL);

			if (haveWebSiteURLValue) {
				serializer.text(TAG_WEBSITE_URL, webSiteURL);
			}
		}
	}

	private void serializePhoneNuber(AListXmlSerializer serializer) {
		if (mStore != null) {
			mStore.moveToFirst();
			String phoneNumber = mStore.getString(mStore.getColumnIndexOrThrow(StoresTable.COL_PHONE_NUMBER));

			boolean havePhoneNumberValue = hasValueToSerialize(phoneNumber);

			if (havePhoneNumberValue) {
				serializer.text(TAG_PHONE_NUMBER, phoneNumber);
			}
		}
	}

	// GROUP LOCATIONS
	private void serializeGroupLocations(AListXmlSerializer serializer) {
		if (mGroupLocations != null && mGroupLocations.getCount() > 0) {

			mGroupLocations.moveToPosition(-1);
			String groupName = "";
			String storeLocation = "";
			boolean haveGroupNameValue = false;
			boolean haveStoreLocationValue = false;
			while (mGroupLocations.moveToNext()) {

				groupName = mGroupLocations.getString(mGroupLocations.getColumnIndexOrThrow(GroupsTable.COL_GROUP_NAME));
				storeLocation = mGroupLocations.getString(mGroupLocations.getColumnIndexOrThrow(LocationsTable.COL_LOCATION_NAME));

				haveGroupNameValue = hasValueToSerialize(groupName);
				haveStoreLocationValue = hasValueToSerialize(storeLocation);

				if (haveGroupNameValue && haveStoreLocationValue) {
					if (!storeLocation.equals(LocationsTable.DEFAULT_LOCATION)) {
						serializer.startTag(TAG_GROUP_LOCATION);
						serializer.text(TAG_GROUP_NAME, groupName);
						serializer.text(TAG_STORE_LOCATION, storeLocation);

						serializer.endTag(TAG_GROUP_LOCATION);
					}
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
