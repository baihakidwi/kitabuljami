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
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

public class DaftarIsiFragment extends Fragment {

	public static DaftarIsiFragment newInstance() {
		DaftarIsiFragment fragment = new DaftarIsiFragment();
		return fragment;
	}

	public DaftarIsiFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView;

		rootView = inflater.inflate(R.layout.fragment_expandable_list,
				container, false);
		ExpandableListView expListView = (ExpandableListView) rootView
				.findViewById(R.id.fragment_exp_list_list);
		expListView.setAdapter(new ExpandableListAdapter(getActivity()));
		expListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				int haditsId = DataLab.getInstance(getActivity()).getHaditsId(
						groupPosition, childPosition);
				Intent intent = new Intent(getActivity(),
						HaditsViewActivity.class);
				intent.putExtra(HaditsViewActivity.ARG_SECTION_NUMBER, haditsId);
				startActivity(intent);

				return false;
			}
		});

		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onResume() {
		super.onResume();
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
