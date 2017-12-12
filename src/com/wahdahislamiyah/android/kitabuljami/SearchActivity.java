package com.wahdahislamiyah.android.kitabuljami;

import java.util.ArrayList;

import com.wahdahislamiyah.android.kitabuljami.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class SearchActivity extends Activity {

	private DataLab mDataLab;
	private ListAdapter mAdapter;

	EditText mEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
				
		mDataLab = DataLab.getInstance(this);
		ArrayList<Integer> data = new ArrayList<Integer>();
		int n = mDataLab.getHaditsCount();
		for (int i = 0; i < n; i++) {
			data.add(i);
		}

		mAdapter = new ListAdapter(this, R.layout.list_child_view, data);

		ListView list = (ListView) findViewById(R.id.activity_search_list);
		list.setAdapter(mAdapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView tv = (TextView) view
						.findViewById(R.id.list_child_view_id);
				int haditsId = Integer.parseInt(tv.getText().toString()) - 1;
				Intent i = new Intent(getApplicationContext(),
						HaditsViewActivity.class);
				i.putExtra(HaditsViewActivity.ARG_SECTION_NUMBER, haditsId);
				startActivity(i);
			}
		});

		mEditText = (EditText) findViewById(R.id.activity_search_edit_text);
		mEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mAdapter.getFilter().filter(s);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		ImageButton clear = (ImageButton) findViewById(R.id.activity_search_clear_button);
		clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mEditText.setText("");
			}
		});
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (mDataLab == null){
			mDataLab = DataLab.getInstance(this);
		}
		mDataLab.writeBookmark();
	}
}
