package com.wahdahislamiyah.android.kitabuljami;

import com.wahdahislamiyah.android.kitabuljami.R;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;

public class IndexActivity extends ActionBarActivity implements
		ActionBar.OnNavigationListener {

	public static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);

		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		actionBar.setListNavigationCallbacks(
				new ArrayAdapter<String>(actionBar.getThemedContext(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, new String[] {
								getString(R.string.label_index),
								getString(R.string.label_bookmark), }), this);

		int state = getIntent().getIntExtra(STATE_SELECTED_NAVIGATION_ITEM, 0);
		actionBar.setSelectedNavigationItem(state);		
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getSupportActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getSupportActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		if (position == 0) {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, DaftarIsiFragment.newInstance())
					.commit();
		} else {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, BookmarkFragment.newInstance())
					.commit();
		}
		return true;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		DataLab.getInstance(this).writeBookmark();
	}
}
