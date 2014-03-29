package com.lbconsulting.alist_03.classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.lbconsulting.alist_03.utilities.MyLog;

public class ReadWriteFile {

	public ReadWriteFile() {

	}

	public static boolean Write(String filename, String data) {
		boolean writeResult = false;
		File file = getFile(filename);

		if (isExternalStorageWritable()) {
			try {
				MyLog.d("ReadWriteFile", "Writing file:" + file.toString());
				FileWriter fileWriter = new FileWriter(file);
				fileWriter.write(data);
				fileWriter.flush();
				fileWriter.close();
				writeResult = true;

			} catch (IOException e) {
				MyLog.e("ReadWriteFile", "IOException in Write!");
				e.printStackTrace();
			}
		}
		return writeResult;
	}

	public static String Read(String filename) {
		String result = "";
		StringBuilder text = new StringBuilder();
		if (isExternalStorageReadable()) {
			File file = getFile(filename);
			if (file.exists()) {
				try {
					MyLog.d("ReadWriteFile", "Reading file:" + file.toString());
					BufferedReader br = new BufferedReader(new FileReader(file));
					String line;

					while ((line = br.readLine()) != null) {
						text.append(line).append(System.getProperty("line.separator"));
					}
					result = text.toString();
					if (result.endsWith(System.getProperty("line.separator"))) {
						result = result.substring(0, result.length() - 1);
					}
					br.close();

				} catch (IOException e) {
					MyLog.e("ReadWriteFile", "ERROR in Read:");
					e.printStackTrace();
				}
			} else {
				MyLog.e("ReadWriteFile", "ERROR in Read: file " + filename + " does not exist!");
			}
		}
		return result;
	}

	public static boolean Delete(String filename) {
		boolean result = false;
		if (isExternalStorageWritable()) {
			File file = getFile(filename);
			if (file.exists()) {
				file.delete();
				result = true;
				MyLog.d("ReadWriteFile", "file:" + filename + " deleted.");
			} else {
				result = true;
			}
		}
		return result;
	}

	private static File getFile(String filename) {
		/*File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);*/
		File file = new File(Environment.getExternalStorageDirectory(), filename);
		// dirs = Context.getExternalFilesDirs(filename);
		// file.mkdirs();
		boolean isDirectory = file.isDirectory();
		boolean isFile = file.isFile();
		return file;
	}

	/* Checks if external storage is available for read and write */
	public static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/* Checks if external storage is available to at least read */
	public static boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) ||
				Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}

	public static void sendEmail(Context context, String filename) {

		// File file = new File(Environment.getExternalStorageState()+"/folderName/" + fileName+ ".xml");
		File file = getFile(filename);
		long fileLength = file.length();

		Uri path = Uri.fromFile(file);
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("application/octet-stream");
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Store Data Submission");
		String to[] = { "LorenABaker@comcast.net" };
		emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
		emailIntent.putExtra(Intent.EXTRA_TEXT, "Here's the store data.");
		emailIntent.putExtra(Intent.EXTRA_STREAM, path);
		MyLog.d("ReadWriteFile", "file:" + filename + " being emailed. Lenght:" + fileLength);
		context.startActivity(Intent.createChooser(emailIntent, "Send your email using:"));
		// startActivityForResult(Intent.createChooser(emailIntent, "Send mail..."), 1222);
	}

}
