package com.lbconsulting.alist_03.classes;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.lbconsulting.alist_03.classes.StoreGoupLocations.AuthorInformation;
import com.lbconsulting.alist_03.classes.StoreGoupLocations.GPSCoordinates;
import com.lbconsulting.alist_03.classes.StoreGoupLocations.GroupLocation;
import com.lbconsulting.alist_03.classes.StoreGoupLocations.ListDetails;
import com.lbconsulting.alist_03.classes.StoreGoupLocations.Store;
import com.lbconsulting.alist_03.classes.StoreGoupLocations.StoreDescription;

public class StoreDataParser {
	// We don't use namespaces
	private static final String ns = null;

	public static StoreGoupLocations parse(InputStream in) throws XmlPullParserException, IOException {

		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			return readFeed(parser);
		} finally {
			in.close();
		}
	}

	private static StoreGoupLocations readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
		StoreGoupLocations parseResult = new StoreGoupLocations();

		parser.require(XmlPullParser.START_TAG, ns, StoreDataSubmission.TAG_STORE_SUBMISSION);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();

			if (name.equals(StoreDataSubmission.TAG_SUBMISSION_DATE)) {
				readSubmissionDate(parseResult, parser);

			} else if (name.equals(StoreDataSubmission.TAG_AUTHOR_INFORMATION)) {
				readAuthorInformation(parseResult, parser);

			} else if (name.equals(StoreDataSubmission.TAG_STORE_DESCRIPTION)) {
				readStoreDescripton(parseResult, parser);

			} else if (name.equals(StoreDataSubmission.TAG_GROUP_LOCATION)) {
				readGroupLocation(parseResult, parser);

			} else {
				skip(parser);
			}
		}
		return parseResult;
	}

	private static void readSubmissionDate(StoreGoupLocations parseResult, XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, StoreDataSubmission.TAG_SUBMISSION_DATE);
		String submissionDate = readText(parser);
		parseResult.setSubmissionDate(submissionDate);
	}

	private static void readAuthorInformation(StoreGoupLocations parseResult, XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, StoreDataSubmission.TAG_AUTHOR_INFORMATION);
		AuthorInformation authorInformation = parseResult.getAuthorInformation();
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();

			if (name.equals(StoreDataSubmission.TAG_FIRST_NAME)) {
				String firstName = readText(parser);
				authorInformation.setAuthorFirstName(firstName);

			} else if (name.equals(StoreDataSubmission.TAG_LAST_NAME)) {
				String lastName = readText(parser);
				authorInformation.setAuthorLastName(lastName);

			} else if (name.equals(StoreDataSubmission.TAG_DEVICE_ID)) {
				String deviceID = readText(parser);
				authorInformation.setDeviceID(deviceID);

			} else if (name.equals(StoreDataSubmission.TAG_DEVICE_MANUFACTURER)) {
				String deviceManufacturer = readText(parser);
				authorInformation.setDeviceManufacturer(deviceManufacturer);

			} else if (name.equals(StoreDataSubmission.TAG_DEVICE_MODEL)) {
				String deviceModel = readText(parser);
				authorInformation.setDeviceModel(deviceModel);

			} else if (name.equals(StoreDataSubmission.TAG_DEVICE_API_VERSION)) {
				String deviceAPIversion = readText(parser);
				authorInformation.setDeviceApiVersion(deviceAPIversion);

			} else {
				skip(parser);
			}
		}

	}

	private static void readStoreDescripton(StoreGoupLocations parseResult, XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, StoreDataSubmission.TAG_STORE_DESCRIPTION);
		StoreDescription storeDescription = parseResult.getStoreDescription();
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();

			if (name.equals(StoreDataSubmission.TAG_STORE)) {
				readStore(storeDescription, parser);

			} else if (name.equals(StoreDataSubmission.TAG_LIST_DETAILS)) {
				readListDetails(storeDescription, parser);

			} else if (name.equals(StoreDataSubmission.TAG_GPS_COORDINATES)) {
				readGPSCoordinates(storeDescription, parser);

			} else if (name.equals(StoreDataSubmission.TAG_WEBSITE_URL)) {
				String StoreWebsiteURL = readText(parser);
				storeDescription.setStoreWebsiteURL(StoreWebsiteURL);

			} else if (name.equals(StoreDataSubmission.TAG_PHONE_NUMBER)) {
				String phoneNumber = readText(parser);
				storeDescription.setStorePhoneNumber(phoneNumber);

			} else {
				skip(parser);
			}
		}
	}

	private static void readStore(StoreDescription storeDescription, XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, StoreDataSubmission.TAG_STORE);
		Store store = storeDescription.getStore();
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();

			if (name.equals(StoreDataSubmission.TAG_UNIQUE_ID)) {
				String UniqueID = readText(parser);
				store.setUniqueID(UniqueID);

			} else if (name.equals(StoreDataSubmission.TAG_STORE_NAME)) {
				String StoreName = readText(parser);
				store.setStoreName(StoreName);

			} else if (name.equals(StoreDataSubmission.TAG_LOCAL_DEVICE_STORE_ID)) {
				String LocalStoreID = readText(parser);
				store.setLocalStoreID(LocalStoreID);

			} else {
				skip(parser);
			}
		}
	}

	private static void readListDetails(StoreDescription storeDescription, XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, StoreDataSubmission.TAG_LIST_DETAILS);
		ListDetails listDetails = storeDescription.getListDetails();
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();

			if (name.equals(StoreDataSubmission.TAG_UNIQUE_ID)) {
				String UniqueID = readText(parser);
				listDetails.setUniqueID(UniqueID);

			} else if (name.equals(StoreDataSubmission.TAG_LIST_TITLE)) {
				String ListTitle = readText(parser);
				listDetails.setListTitle(ListTitle);

			} else if (name.equals(StoreDataSubmission.TAG_LOCAL_DEVICE_LIST_ID)) {
				String LocalListID = readText(parser);
				listDetails.setLocalLisstID(LocalListID);

			} else {
				skip(parser);
			}
		}
	}

	private static void readGPSCoordinates(StoreDescription storeDescription, XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, StoreDataSubmission.TAG_STORE);
		GPSCoordinates gpsCoordinates = storeDescription.getStoreGPSCoordiantes();
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();

			if (name.equals(StoreDataSubmission.TAG_LATITUDE)) {
				String GPSLatitude = readText(parser);
				gpsCoordinates.setGpsLatitude(GPSLatitude);

			} else if (name.equals(StoreDataSubmission.TAG_LONGITUDE)) {
				String GPSLatitude = readText(parser);
				gpsCoordinates.setGpsLatitude(GPSLatitude);

			} else {
				skip(parser);
			}
		}
	}

	private static void readGroupLocation(StoreGoupLocations parseResult, XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, StoreDataSubmission.TAG_GROUP_LOCATION);
		ArrayList<GroupLocation> groupLocations = parseResult.getGroupLocations();

		String groupName = "";
		String storeLocation = "";

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();

			if (name.equals(StoreDataSubmission.TAG_GROUP_NAME)) {
				groupName = readText(parser);

			} else if (name.equals(StoreDataSubmission.TAG_STORE_LOCATION)) {
				storeLocation = readText(parser);

			} else {
				skip(parser);
			}
		}
		if (!groupName.isEmpty() && !storeLocation.isEmpty()) {
			GroupLocation groupLocation = parseResult.new GroupLocation(groupName, storeLocation);
			groupLocations.add(groupLocation);
		}
	}

	private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			default:
				break;
			}
		}
	}

	// For the tags title and summary, extracts their text values.
	private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

}
