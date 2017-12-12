package com.wahdahislamiyah.android.kitabuljami;

import com.wahdahislamiyah.android.kitabuljami.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class BookmarkFragment extends Fragment {

	ListAdapter adapter;
	TextView mNote;

	public static BookmarkFragment newInstance() {
		BookmarkFragment fragment = new BookmarkFragment();
		return fragment;
	}

	public BookmarkFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView;
		rootView = inflater.inflate(R.layout.fragment_list, container, false);
		mNote = (TextView) rootView.findViewById(R.id.fragment_list_no_data);
		ListView list = (ListView) rootView
				.findViewById(R.id.fragment_list_list);
		adapter = new ListAdapter(getActivity(), R.layout.fragment_list,
				DataLab.getInstance(getActivity()).getBookmarkList());
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int haditsId = DataLab.getInstance(getActivity())
						.getBookmarkedHaditsId(position);
				Intent intent = new Intent(getActivity(),
						HaditsViewActivity.class);
				intent.putExtra(HaditsViewActivity.ARG_SECTION_NUMBER, haditsId);
				startActivity(intent);
			}
		});

		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (adapter != null) {
			adapter.updateData(DataLab.getInstance(getActivity())
					.getBookmarkList());
			if (adapter.isEmpty()) {
				mNote.setVisibility(View.VISIBLE);
			} else {
				mNote.setVisibility(View.INVISIBLE);
			}
		}

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.index, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int actionId = item.getItemId();
		switch (actionId) {
		case R.id.action_search:
			Intent i = new Intent(getActivity(), SearchActivity.class);
			startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}
}
