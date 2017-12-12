package com.wahdahislamiyah.android.kitabuljami;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.wahdahislamiyah.android.kitabuljami.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private DataLab mDataLab;
	private static CustomTypefaceSpan customTypefaceSpan;

	public ExpandableListAdapter(Context context) {
		mContext = context.getApplicationContext();
		mDataLab = DataLab.getInstance(context);
	}

	@Override
	public int getGroupCount() {
		return mDataLab.getJumlahBab();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mDataLab.getHaditsCount(groupPosition);
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mDataLab.getJudulBab(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mDataLab.getHadits(groupPosition, childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return mDataLab.getHaditsId(groupPosition, childPosition);
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_group_view, null);

		}
		String judul = mDataLab.getJudulBab(groupPosition);
		TextView judulView = (TextView) convertView
				.findViewById(R.id.list_group_view_judul);
		judulView.setText("Bab " + (groupPosition + 1) + ": " + judul);
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_child_view, null);
		}

		Hadits hadits = mDataLab.getHadits(groupPosition, childPosition);
		String id = String.valueOf(hadits.getId() + 1);
		String judul = hadits.getJudul();

		if (customTypefaceSpan == null) {
			customTypefaceSpan = new CustomTypefaceSpan("",
					Typeface.createFromAsset(mContext.getAssets(),
							"arabesque_ii.ttf"));
		}
		SpannableStringBuilder ssJudul = new SpannableStringBuilder(judul);
		String[] honorifics = { " I ", " r ", " t ", " C ", " z " };
		int pos = 0;
		for (String honorific : honorifics) {
			pos = judul.indexOf(honorific, 0);
			while (pos != -1) {
				ssJudul.setSpan(customTypefaceSpan, pos, pos + 3,
						Spanned.SPAN_INCLUSIVE_INCLUSIVE);
				pos = judul.indexOf(honorific, pos + 3);
			}
		}

		TextView idView = (TextView) convertView
				.findViewById(R.id.list_child_view_id);
		TextView judulView = (TextView) convertView
				.findViewById(R.id.list_child_view_judul);

		idView.setText(id);
		judulView.setText(ssJudul);

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
