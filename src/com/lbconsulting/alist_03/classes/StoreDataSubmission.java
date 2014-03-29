package com.lbconsulting.alist_03.classes;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Calendar;

import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.provider.Settings.Secure;
import android.util.Xml;

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
	public final static String TAG_STORE_ID = "StoreID";
	public final static String TAG_STORE_ID_SUBMISSION = "StoreSubmission";
	public final static String TAG_STORE_LOCATION = "StoreLocation";
	public final static String TAG_STORE_NAME = "StoreName";
	public final static String TAG_STREET1 = "Street1";
	public final static String TAG_STREET2 = "Street2";
	public final static String TAG_SUBMISSION_DATE = "SubmissionDate";
	public final static String TAG_UNIQUE_ID = "UniqueID";
	public final static String TAG_WEBSITE_URL = "WebsiteURL";
	public final static String TAG_ZIP = "Zip";

	public String getXml() {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);

			serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

			serializer.startDocument("UTF-8", true);
			serializer.startTag("", TAG_STORE_ID_SUBMISSION);

			serializeSubmissionDate(serializer);
			serializeAuthorInformatoin(serializer);
			serializeStoreDescription(serializer);
			serializeGroupLocations(serializer);

			serializer.endTag("", TAG_STORE_ID_SUBMISSION);
			serializer.endDocument();
			return writer.toString();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// SUBMISSION DATE
	private void serializeSubmissionDate(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
		// Store date and time as an INTEGER as Unix Time, the number of milli seconds since 1970-01-01 00:00:00 UTC.
		serializer.startTag("", TAG_SUBMISSION_DATE);
		serializer.text(String.valueOf(Calendar.getInstance().getTimeInMillis()));
		serializer.endTag("", TAG_SUBMISSION_DATE);
	}

	// AUTHOR INFORMATION
	public void serializeAuthorInformatoin(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", TAG_AUTHOR_INFORMATION);

		serializeAuthorFirstName(serializer);
		serializeAuthorLastName(serializer);
		serializeDeviceID(serializer);
		serializeDeviceManufacturer(serializer);
		serializeDeviceModel(serializer);
		serializeDeviceApiVersion(serializer);

		serializer.endTag("", TAG_AUTHOR_INFORMATION);
	}

	private void serializeAuthorFirstName(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
		boolean haveAuthorFirstNameValue = hasValueToSerialize(mAuthorFirstName);
		if (haveAuthorFirstNameValue) {
			serializer.startTag("", TAG_FIRST_NAME);
			serializer.text(mAuthorFirstName);
			serializer.endTag("", TAG_FIRST_NAME);
		}
	}

	private void serializeAuthorLastName(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {

		boolean haveAuthorLastNameValue = hasValueToSerialize(mAuthorLastName);
		if (haveAuthorLastNameValue) {
			serializer.startTag("", TAG_LAST_NAME);
			serializer.text(mAuthorLastName);
			serializer.endTag("", TAG_LAST_NAME);
		}
	}

	private void serializeDeviceManufacturer(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
		String manufacturer = Build.MANUFACTURER;
		boolean haveManufacturerValue = hasValueToSerialize(manufacturer);
		if (haveManufacturerValue) {
			serializer.startTag("", TAG_DEVICE_MANUFACTURER);
			serializer.text(manufacturer);
			serializer.endTag("", TAG_DEVICE_MANUFACTURER);
		}
	}

	private void serializeDeviceModel(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
		String model = Build.MODEL;
		boolean haveModelValue = hasValueToSerialize(model);
		if (haveModelValue) {
			serializer.startTag("", TAG_DEVICE_MODEL);
			serializer.text(model);
			serializer.endTag("", TAG_DEVICE_MODEL);
		}
	}

	private void serializeDeviceApiVersion(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
		int currentApiVersion = android.os.Build.VERSION.SDK_INT;
		serializer.startTag("", TAG_DEVICE_API_VERSION);
		serializer.text(String.valueOf(currentApiVersion));
		serializer.endTag("", TAG_DEVICE_API_VERSION);
	}

	private void serializeDeviceID(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
		String deviceId = Secure.getString(mContext.getContentResolver(), Secure.ANDROID_ID);
		boolean haveDeviceIdValue = hasValueToSerialize(deviceId);
		if (haveDeviceIdValue) {
			serializer.startTag("", TAG_DEVICE_ID);
			serializer.text(deviceId);
			serializer.endTag("", TAG_DEVICE_ID);
		}
	}

	// STORE DESCRIPTION
	public void serializeStoreDescription(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", TAG_STORE_DESCRIPTION);

		serializeStoreID(serializer);
		serializeListDetails(serializer);
		serializeStoreAddress(serializer);
		serializeGPSCoordinates(serializer);
		serializeWebsiteURL(serializer);
		serializePhoneNuber(serializer);

		serializer.endTag("", TAG_STORE_DESCRIPTION);
	}

	private void serializeStoreID(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
		if (mStore != null) {

			mStore.moveToFirst();
			String storeName = mStore.getString(mStore.getColumnIndexOrThrow(StoresTable.COL_STORE_NAME));
			long localDeviceStoreID = mStore.getLong(mStore.getColumnIndexOrThrow(StoresTable.COL_STORE_ID));
			String uniqueID = "!!! StoreUniqueID !!!";

			serializer.startTag("", TAG_STORE_ID);

			serializer.startTag("", TAG_UNIQUE_ID);
			serializer.text(uniqueID);
			serializer.endTag("", TAG_UNIQUE_ID);

			serializer.startTag("", TAG_STORE_NAME);
			serializer.text(storeName);
			serializer.endTag("", TAG_STORE_NAME);

			serializer.startTag("", TAG_LOCAL_DEVICE_STORE_ID);
			serializer.text(String.valueOf(localDeviceStoreID));
			serializer.endTag("", TAG_LOCAL_DEVICE_STORE_ID);

			serializer.endTag("", TAG_STORE_ID);
		}
	}

	private void serializeListDetails(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
		if (mList != null) {

			mList.moveToFirst();
			String listTitle = mList.getString(mList.getColumnIndexOrThrow(ListsTable.COL_LIST_TITLE));
			long localDeviceListID = mList.getLong(mList.getColumnIndexOrThrow(ListsTable.COL_LIST_ID));
			String uniqueID = "!!! List Unique ID !!!";

			serializer.startTag("", TAG_LIST_DETAILS);

			serializer.startTag("", TAG_UNIQUE_ID);
			serializer.text(uniqueID);
			serializer.endTag("", TAG_UNIQUE_ID);

			serializer.startTag("", TAG_LIST_TITLE);
			serializer.text(listTitle);
			serializer.endTag("", TAG_LIST_TITLE);

			serializer.startTag("", TAG_LOCAL_DEVICE_LIST_ID);
			serializer.text(String.valueOf(localDeviceListID));
			serializer.endTag("", TAG_LOCAL_DEVICE_LIST_ID);

			serializer.endTag("", TAG_LIST_DETAILS);
		}
	}

	private void serializeStoreAddress(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
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
				serializer.startTag("", TAG_ADDRESS);

				if (haveStreet1Value) {
					serializer.startTag("", TAG_STREET1);
					serializer.text(street1);
					serializer.endTag("", TAG_STREET1);
				}
				if (haveStreet2Value) {
					serializer.startTag("", TAG_STREET2);
					serializer.text(street2);
					serializer.endTag("", TAG_STREET2);
				}
				if (haveCityValue) {
					serializer.startTag("", TAG_CITY);
					serializer.text(city);
					serializer.endTag("", TAG_CITY);
				}
				if (haveStateValue) {
					serializer.startTag("", TAG_STATE);
					serializer.text(state);
					serializer.endTag("", TAG_STATE);
				}
				if (haveZipValue) {
					serializer.startTag("", TAG_ZIP);
					serializer.text(zip);
					serializer.endTag("", TAG_ZIP);
				}

				serializer.endTag("", TAG_ADDRESS);
			}
		}
	}

	private void serializeGPSCoordinates(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
		if (mStore != null) {
			boolean haveStoreAddressValuesToSerialize = false;

			mStore.moveToFirst();
			String gpsLatitude = mStore.getString(mStore.getColumnIndexOrThrow(StoresTable.COL_GPS_LATITUDE));
			String gpsLongitude = mStore.getString(mStore.getColumnIndexOrThrow(StoresTable.COL_GPS_LONGITUDE));

			boolean haveGPSLatitudeValue = hasValueToSerialize(gpsLatitude);
			boolean haveGPSLongitudeValue = hasValueToSerialize(gpsLongitude);

			haveStoreAddressValuesToSerialize = haveGPSLatitudeValue || haveGPSLongitudeValue;
			if (haveStoreAddressValuesToSerialize) {
				serializer.startTag("", TAG_GPS_COORDINATES);

				if (haveGPSLatitudeValue) {
					serializer.startTag("", TAG_LATITUDE);
					serializer.text(gpsLatitude);
					serializer.endTag("", TAG_LATITUDE);
				}
				if (haveGPSLongitudeValue) {
					serializer.startTag("", TAG_LONGITUDE);
					serializer.text(gpsLongitude);
					serializer.endTag("", TAG_LONGITUDE);
				}

				serializer.endTag("", TAG_GPS_COORDINATES);
			}
		}
	}

	private void serializeWebsiteURL(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
		if (mStore != null) {
			mStore.moveToFirst();
			String webSiteURL = mStore.getString(mStore.getColumnIndexOrThrow(StoresTable.COL_WEBSITE_URL));

			boolean haveWebSiteURLValue = hasValueToSerialize(webSiteURL);

			if (haveWebSiteURLValue) {
				serializer.startTag("", TAG_WEBSITE_URL);
				serializer.text(webSiteURL);
				serializer.endTag("", TAG_WEBSITE_URL);
			}
		}
	}

	private void serializePhoneNuber(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
		if (mStore != null) {
			mStore.moveToFirst();
			String phoneNumber = mStore.getString(mStore.getColumnIndexOrThrow(StoresTable.COL_PHONE_NUMBER));

			boolean havePhoneNumberValue = hasValueToSerialize(phoneNumber);

			if (havePhoneNumberValue) {
				serializer.startTag("", TAG_PHONE_NUMBER);
				serializer.text(phoneNumber);
				serializer.endTag("", TAG_PHONE_NUMBER);
			}
		}
	}

	// GROUP LOCATIONS
	private void serializeGroupLocations(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
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
						serializer.startTag("", TAG_GROUP_LOCATION);

						serializer.startTag("", TAG_GROUP_NAME);
						serializer.text(groupName);
						serializer.endTag("", TAG_GROUP_NAME);

						serializer.startTag("", TAG_STORE_LOCATION);
						serializer.text(storeLocation);
						serializer.endTag("", TAG_STORE_LOCATION);

						serializer.endTag("", TAG_GROUP_LOCATION);
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
