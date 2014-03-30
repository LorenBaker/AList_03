package com.lbconsulting.alist_03.classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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

	/*	public ReadWriteFile() {

		}*/

	public static boolean Write(String filename, String data) {
		boolean writeResult = false;
		File file = getFile(filename);

		if (isExternalStorageWritable()) {
			try {
				MyLog.d("ReadWriteFile", "Writing file:" + file.toString());
				BufferedWriter bw = new BufferedWriter(new FileWriter(file));
				bw.write(data);
				bw.flush();
				bw.close();
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
						// text.append(line).append(System.getProperty("line.separator"));
						text.append(line).append("\r\n");

					}
					result = text.toString();
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
		String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/alist/";
		File file = new File(directory);
		file.mkdirs();

		/*		boolean madeDirectory = file.mkdirs();*/
		/*		if (madeDirectory) {
					MyLog.i("ReadWriteFile", "Created directory:" + file.toString());
				} else if (file.isDirectory()) {
					MyLog.i("ReadWriteFile", "Directory exists:" + file.toString());
				} else {
					MyLog.i("ReadWriteFile", "Only God knows what was made:" + file.toString());
				}*/

		file = new File(directory, filename);
		/*		boolean isFile = file.isFile();
				if (isFile) {
					MyLog.i("ReadWriteFile", "isFile:" + file.toString());
				} else {
					MyLog.i("ReadWriteFile", "IS NOT a File:" + file.toString());
				}*/
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
	}

}
