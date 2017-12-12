package com.wahdahislamiyah.android.kitabuljami;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.wahdahislamiyah.android.kitabuljami.R;

public class ListAdapter extends ArrayAdapter<Integer> {

	private ArrayList<Integer> mHaditsIdList;
	private ArrayList<Integer> mFilteredList;
	private Context mContext;
	private DataLab mDataLab;
	private HaditsFilter filter;
	private static CustomTypefaceSpan customTypefaceSpan;

	public ListAdapter(Context context, int resource, List<Integer> objects) {
		super(context, resource, objects);
		mHaditsIdList = new ArrayList<Integer>();
		mHaditsIdList.addAll(objects);
		mFilteredList = new ArrayList<Integer>();
		mFilteredList.addAll(objects);
		mContext = context.getApplicationContext();
		mDataLab = DataLab.getInstance(mContext);
		getFilter();
	}

	public void updateData(List<Integer> list) {
		mHaditsIdList.clear();
		mHaditsIdList.addAll(list);
		mFilteredList.clear();
		mFilteredList.addAll(list);
		notifyDataSetChanged();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// convertView = inflater.inflate(R.layout.list_child_view, parent);
			convertView = inflater.inflate(R.layout.list_child_view, null);
		}
		Hadits hadits = mDataLab.getHadits(mFilteredList.get(position));
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

		// convertView.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// }
		// });

		return convertView;
	}

	@Override
	public int getCount() {
		return mFilteredList.size();
	}

	@Override
	public Integer getItem(int position) {
		return mFilteredList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mFilteredList.get(position);
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		if (filter == null) {
			filter = new HaditsFilter();
		}
		return filter;
	}

	private class HaditsFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			if (constraint != null && constraint.length() > 0) {
				constraint = constraint.toString().toLowerCase().trim()
						.replaceAll("\\s+", " ");
				String[] queries = constraint.toString().split(" ");
				ArrayList<Integer> filteredList = new ArrayList<Integer>();
				boolean containConstraint = true;
				for (int haditsId : mHaditsIdList) {
					containConstraint = true;
					String judul = mDataLab.getHadits(haditsId).getJudul()
							.toLowerCase();
					for (String query : queries) {
						if (!judul.contains(query)) {
							containConstraint = false;
							break;
						}
					}
					if (containConstraint) {
						filteredList.add(haditsId);
					}
				}
				results.values = filteredList;
				results.count = filteredList.size();
			} else {
				synchronized (this) {
					results.values = mHaditsIdList;
					results.count = mHaditsIdList.size();
				}
			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			mFilteredList = (ArrayList<Integer>) results.values;
			notifyDataSetChanged();
			clear();
			for (int haditsId : mFilteredList) {
				add(haditsId);
			}
			notifyDataSetInvalidated();
		}

	}
}
