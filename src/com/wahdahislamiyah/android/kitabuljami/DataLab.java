package com.wahdahislamiyah.android.kitabuljami;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.util.Xml;

public class DataLab {

	private static final String[] JUDUL_BAB = { "Adab",
			"Silaturahim dan Kebajikan", "Zuhud dan WaraÅf",
			"Akhlaq Mulia dan Akhlaq Tercela", "Dzikir Dan DoÅea" };
	private static final int JUMLAH_BAB = 5;

	private Context mAppContext;
	private static DataLab sDataLab;

	private static int sHaditsId = 0;
	private ArrayList<Integer> mBookmark;
	private ArrayList<Hadits> mHaditsList;
	private int[] mBabHaditsCount;

	private boolean mUpdate = false;

	public static DataLab getInstance(Context context) {
		if (sDataLab == null) {
			sDataLab = new DataLab(context.getApplicationContext());
		}
		return sDataLab;
	}

	private DataLab(Context context) {
		mAppContext = context;
		mHaditsList = new ArrayList<Hadits>();
		mBabHaditsCount = new int[JUMLAH_BAB];
		sHaditsId = 0;
		for (int i = 0; i < JUMLAH_BAB; i++) {
			mBabHaditsCount[i] = sHaditsId;
			readDBase(i);
			mBabHaditsCount[i] = sHaditsId - mBabHaditsCount[i];
		}
		mBookmark = new ArrayList<Integer>();
		readBookmark();
	}

	private void readDBase(int dBaseId) {
		try {
			XmlPullParser parser = Xml.newPullParser();
			InputStream inputStream = mAppContext.getAssets().open(
					"kitabul_jami_bab_" + (dBaseId + 1) + ".xml");
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(inputStream, null);
			int eventType = parser.getEventType();
			String text = "";
			Hadits hadits = new Hadits();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if (parser.getName().equals("hadits")) {
						hadits = new Hadits();
						hadits.setId(sHaditsId);
						sHaditsId++;
					}
					break;
				case XmlPullParser.TEXT:
					text = parser.getText();
					break;
				case XmlPullParser.END_TAG:
					String name = parser.getName();
					if (name.equals("hadits")) {
						mHaditsList.add(hadits);
					} else if (name.equals("judul")) {
						hadits.setJudul(text);
					} else if (name.equals("arab")) {
						hadits.setArab(text);
					} else if (name.equals("terjemah")) {
						hadits.setTerjemah(text);
					} else if (name.equals("note")) {
						hadits.setNote(text);
					}
					break;
				}
				eventType = parser.next();
			}
		} catch (IOException ex) {
		} catch (XmlPullParserException ex) {
		}
	}

	private void readBookmark() {
		mBookmark.clear();
		try {
			FileInputStream fileInputStream = mAppContext
					.openFileInput("bookmark.xml");
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(fileInputStream, "UTF-8");
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.TEXT) {
					mBookmark.add(Integer.parseInt(parser.getText()));
				}
				eventType = parser.next();
			}
		} catch (Exception ex) {
		}
	}

	public void writeBookmark() {
		if (!mUpdate) {
			return;
		}
		try {
			FileOutputStream fileOutputStream = mAppContext.openFileOutput(
					"bookmark.xml", Context.MODE_PRIVATE);
			StringWriter stringWriter = new StringWriter();
			XmlSerializer xmlSerializer = Xml.newSerializer();
			xmlSerializer.setOutput(stringWriter);
			xmlSerializer.startDocument("UTF-8", true);
			xmlSerializer.startTag("", "data");
			for (int bookmark : mBookmark) {
				xmlSerializer.startTag("", "id");
				xmlSerializer.text(String.valueOf(bookmark));
				xmlSerializer.endTag("", "id");
			}
			xmlSerializer.endTag("", "data");
			xmlSerializer.endDocument();

			fileOutputStream.write(stringWriter.toString().getBytes());
			fileOutputStream.close();
		} catch (Exception e) {
		}
		mUpdate = false;
	}

	public boolean toggleBookmark(int haditsId) {
		mUpdate = true;
		if (haditsId < 0 || haditsId > mHaditsList.size()) {
			return false;
		}
		if (isBookmarked(haditsId)) {
			int index = 0;
			for (int bookmark : mBookmark) {
				if (haditsId == bookmark) {
					mBookmark.remove(index);
					break;
				}
				index++;
			}
			return false;
		} else {
			int index = 0;
			for (int bookmark : mBookmark) {
				if (haditsId < bookmark) {
					mBookmark.add(index, haditsId);
					return true;
				}
				index++;
			}
			mBookmark.add(haditsId);
			return true;
		}
	}

	public int getBabId(int haditsId) {
		if (haditsId < 0 || haditsId >= mHaditsList.size()) {
			return -1;
		}
		for (int i = 0; i < JUMLAH_BAB; i++) {
			if (haditsId > mBabHaditsCount[i]) {
				haditsId -= mBabHaditsCount[i];
			} else {
				return i;
			}
		}
		return -1;
	}

	public int getBookmarkCount() {
		return mBookmark.size();
	}

	public int getBookmarkedHaditsId(int index) {
		if (index < 0 || index >= mBookmark.size()) {
			return -1;
		}
		return mBookmark.get(index);
	}

	public ArrayList<Integer> getBookmarkList() {
		return mBookmark;
	}

	public Hadits getHadits(int haditsId) {
		if (haditsId < 0 || haditsId >= mHaditsList.size()) {
			return null;
		}
		return mHaditsList.get(haditsId);
	}

	public Hadits getHadits(int babId, int id) {
		if (babId < 0 || babId >= JUMLAH_BAB) {
			return null;
		}
		if (id < 0 || id >= mBabHaditsCount[babId]) {
			return null;
		}
		for (int i = 0; i < babId; i++) {
			id += mBabHaditsCount[i];
		}
		return mHaditsList.get(id);
	}

	public int getHaditsCount() {
		return mHaditsList.size();
	}

	public int getHaditsCount(int babId) {
		if (babId < 0 || babId >= JUMLAH_BAB) {
			return -1;
		}
		return mBabHaditsCount[babId];
	}

	public int getHaditsId(int babId, int id) {
		if (babId < 0 || babId >= JUMLAH_BAB) {
			return -1;
		}
		if (id < 0 || id >= mBabHaditsCount[babId]) {
			return -1;
		}
		for (int i = 0; i < babId; i++) {
			id += mBabHaditsCount[i];
		}
		return id;
	}

	public ArrayList<Hadits> getHaditsList() {
		return mHaditsList;
	}

	public String getJudulBab(int babId) {
		if (babId < 0 || babId >= JUMLAH_BAB) {
			return null;
		}
		return JUDUL_BAB[babId];
	}

	public int getJumlahBab() {
		return JUMLAH_BAB;
	}

	public boolean isBookmarked(int haditsId) {
		for (int bookmark : mBookmark) {
			if (bookmark == haditsId) {
				return true;
			}
		}
		return false;
	}

}
