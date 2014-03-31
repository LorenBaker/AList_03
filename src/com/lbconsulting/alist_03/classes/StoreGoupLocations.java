package com.lbconsulting.alist_03.classes;

import java.util.ArrayList;
import java.util.Calendar;

public class StoreGoupLocations {
	private Calendar submissionDate;
	private AuthorInformation authorInformation;
	private StoreDescription storeDescription;
	private ArrayList<GroupLocation> groupLocations;

	// private String DEFAULT_VALUE = "!!!*** NO VALUE ***!!!";

	public StoreGoupLocations() {
		authorInformation = new AuthorInformation();
		storeDescription = new StoreDescription();
		groupLocations = new ArrayList<StoreGoupLocations.GroupLocation>();
	}

	public int getCount() {
		return groupLocations.size();
	}

	public long getLocalStoreID() {
		return storeDescription.store.getLocalStoreID();
	}

	public long getLocalListID() {
		return storeDescription.listDetails.getLocalListID();
	}

	public Calendar getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(String SubmissionDate) {
		long submissionDateMillis = Long.valueOf(SubmissionDate);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(submissionDateMillis);
		this.submissionDate = cal;
	}

	public AuthorInformation getAuthorInformation() {
		return authorInformation;
	}

	public void setAuthorInformation(AuthorInformation AuthorInformation) {
		this.authorInformation = AuthorInformation;
	}

	public StoreDescription getStoreDescription() {
		return storeDescription;
	}

	public void setStoreDescription(StoreDescription StoreDescription) {
		this.storeDescription = StoreDescription;
	}

	public ArrayList<GroupLocation> getGroupLocations() {
		return groupLocations;
	}

	public void setGroupLocations(ArrayList<GroupLocation> GroupLocations) {
		this.groupLocations = GroupLocations;
	}

	public class AuthorInformation {
		private String authorFirstName;
		private String authorLastName;
		private String deviceID;
		private String deviceManufacturer;
		private String deviceModel;
		private String deviceApiVersion;

		public AuthorInformation() {
			// constructor
		}

		public AuthorInformation(String AuthorFirstName, String AuthorLastName,
				String DeviceID, String DeviceManufacturer, String DeviceModel, String DeviceApiVersion) {
			this.authorFirstName = AuthorFirstName;
			this.authorLastName = AuthorLastName;
			this.deviceID = DeviceID;
			this.deviceManufacturer = DeviceManufacturer;
			this.deviceModel = DeviceModel;
			this.deviceApiVersion = DeviceApiVersion;
		}

		public String getAuthorFirstName() {
			return authorFirstName;
		}

		public void setAuthorFirstName(String AuthorFirstName) {
			this.authorFirstName = AuthorFirstName;
		}

		public String getAuthorLastName() {
			return authorLastName;
		}

		public void setAuthorLastName(String AuthorLastName) {
			this.authorLastName = AuthorLastName;
		}

		public String getDeviceID() {
			return deviceID;
		}

		public void setDeviceID(String DeviceID) {
			this.deviceID = DeviceID;
		}

		public String getDeviceManufacturer() {
			return deviceManufacturer;
		}

		public void setDeviceManufacturer(String DeviceManufacturer) {
			this.deviceManufacturer = DeviceManufacturer;
		}

		public String getDeviceModel() {
			return deviceModel;
		}

		public void setDeviceModel(String DeviceModel) {
			this.deviceModel = DeviceModel;
		}

		public String getDeviceApiVersion() {
			return deviceApiVersion;
		}

		public void setDeviceApiVersion(String DeviceApiVersion) {
			this.deviceApiVersion = DeviceApiVersion;
		}

	}

	public class StoreDescription {

		private Store store;
		private ListDetails listDetails;
		private Address storeAddress;
		private GPSCoordinates storeGPSCoordiantes;
		private String storeWebsiteURL;
		private String storePhoneNumber;

		public String getStoreWebsiteURL() {
			return storeWebsiteURL;
		}

		public void setStoreWebsiteURL(String StoreWebsiteURL) {
			this.storeWebsiteURL = StoreWebsiteURL;
		}

		public String getStorePhoneNumber() {
			return storePhoneNumber;
		}

		public void setStorePhoneNumber(String StorePhoneNumber) {
			this.storePhoneNumber = StorePhoneNumber;
		}

		public StoreDescription() {
			store = new Store();
			listDetails = new ListDetails();
			storeAddress = new Address();
			storeGPSCoordiantes = new GPSCoordinates();
		}

		public Store getStore() {
			return store;
		}

		public ListDetails getListDetails() {
			return listDetails;
		}

		public Address getStoreAddress() {
			return storeAddress;
		}

		public GPSCoordinates getStoreGPSCoordiantes() {
			return storeGPSCoordiantes;
		}

	}

	public class GroupLocation {

		private String groupName;
		private String storeLocation;

		public GroupLocation(String GroupName, String StoreLocation) {
			this.groupName = GroupName;
			this.storeLocation = StoreLocation;
		}

		public String getGroupName() {
			return groupName;
		}

		public void setGroupName(String GroupName) {
			this.groupName = GroupName;
		}

		public String getStoreLocation() {
			return storeLocation;
		}

		public void setStoreLocation(String StoreLocation) {
			this.storeLocation = StoreLocation;
		}

	}

	public class Store {
		private String uniqueID;
		private String storeName;
		private long localStoreID;

		public Store() {
			/*			setUniqueID(DEFAULT_VALUE);
						setStoreName(DEFAULT_VALUE);*/
			setLocalStoreID("-1");
		}

		public Store(String StoreName, String LocalStoreID) {
			/*setUniqueID(DEFAULT_VALUE);*/
			setStoreName(StoreName);
			setLocalStoreID(LocalStoreID);
		}

		public Store(String UniqueID, String StoreName, String LocalStoreID) {
			setUniqueID(UniqueID);
			setStoreName(StoreName);
			setLocalStoreID(LocalStoreID);
		}

		public String getUniqueID() {
			return uniqueID;
		}

		public void setUniqueID(String UniqueID) {
			this.uniqueID = UniqueID;
		}

		public String getStoreName() {
			return storeName;
		}

		public void setStoreName(String StoreName) {
			this.storeName = StoreName;
		}

		public long getLocalStoreID() {
			return localStoreID;
		}

		public void setLocalStoreID(String LocalStoreID) {
			long LocalStoreIDValue = Long.valueOf(LocalStoreID);
			this.localStoreID = LocalStoreIDValue;
		}

	}

	public class ListDetails {
		private String uniqueID;
		private String listTitle;
		private long localListID;

		public ListDetails() {
			setLocalLisstID("-1");
		}

		public ListDetails(String ListTitle, String LocalLisstID) {
			setListTitle(ListTitle);
			setLocalLisstID(LocalLisstID);
		}

		public ListDetails(String UniqueID, String ListTitle, String LocalLisstID) {
			setUniqueID(UniqueID);
			setListTitle(ListTitle);
			setLocalLisstID(LocalLisstID);
		}

		public String getUniqueID() {
			return uniqueID;
		}

		public void setUniqueID(String UniqueID) {
			this.uniqueID = UniqueID;
		}

		public String getListTitle() {
			return listTitle;
		}

		public void setListTitle(String ListTitle) {
			this.listTitle = ListTitle;
		}

		public long getLocalListID() {
			return localListID;
		}

		public void setLocalLisstID(String LocalListID) {
			long LocalListIDValue = Long.valueOf(LocalListID);
			this.localListID = LocalListIDValue;
		}

	}

	public class Address {
		private String street1;
		private String street2;
		private String city;
		private String state;
		private String zip;

		public Address() {
			/*			setStreet1(DEFAULT_VALUE);
						setStreet2(DEFAULT_VALUE);
						setCity(DEFAULT_VALUE);
						setState(DEFAULT_VALUE);
						setZip(DEFAULT_VALUE);*/
		}

		public String getStreet1() {
			return street1;
		}

		public void setStreet1(String Street1) {
			this.street1 = Street1;
		}

		public String getStreet2() {
			return street2;
		}

		public void setStreet2(String Street2) {
			this.street2 = Street2;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String City) {
			this.city = City;
		}

		public String getState() {
			return state;
		}

		public void setState(String State) {
			this.state = State;
		}

		public String getZip() {
			return zip;
		}

		public void setZip(String Zip) {
			this.zip = Zip;
		}
	}

	public class GPSCoordinates {
		private String gpsLatitude;
		private String gpsLongitude;

		public GPSCoordinates() {
			/*this.gpsLatitude = DEFAULT_VALUE;
			this.gpsLongitude = DEFAULT_VALUE;*/
		}

		public GPSCoordinates(String GPSLatitude, String GPSLongitude) {
			this.gpsLatitude = GPSLatitude;
			this.gpsLongitude = GPSLongitude;
		}

		public String getGpsLatitude() {
			return gpsLatitude;
		}

		public void setGpsLatitude(String GPSLatitude) {
			this.gpsLatitude = GPSLatitude;
		}

		public String getGpsLongitude() {
			return gpsLongitude;
		}

		public void setGpsLongitude(String GPSLongitude) {
			this.gpsLongitude = GPSLongitude;
		}
	}

}
