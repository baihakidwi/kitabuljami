package com.wahdahislamiyah.android.kitabuljami;

import com.wahdahislamiyah.android.kitabuljami.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		Button indexButton = (Button) findViewById(R.id.activity_home_button_index);
		Button bookmarkButton = (Button) findViewById(R.id.activity_home_button_bookmark);
		Button searchButton = (Button) findViewById(R.id.activity_home_button_search);
		Button infoButton = (Button) findViewById(R.id.activity_home_button_info);

		indexButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						IndexActivity.class);
				intent.putExtra(IndexActivity.STATE_SELECTED_NAVIGATION_ITEM, 0);
				startActivity(intent);
			}
		});

		searchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
				startActivity(intent);			
			}
		});
		
		bookmarkButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						IndexActivity.class);
				intent.putExtra(IndexActivity.STATE_SELECTED_NAVIGATION_ITEM, 1);
				startActivity(intent);
			}
		});

		infoButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
				startActivity(intent);
			}
		});
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		DataLab.getInstance(this).writeBookmark();
	}
}
