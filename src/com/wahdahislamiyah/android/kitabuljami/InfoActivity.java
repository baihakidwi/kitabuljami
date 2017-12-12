package com.wahdahislamiyah.android.kitabuljami;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.widget.TextView;

import com.wahdahislamiyah.android.kitabuljami.R;

public class InfoActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Typeface amiri = Typeface.createFromAsset(getAssets(),
				"amiri-regular.ttf");
		Typeface arabesque = Typeface.createFromAsset(getAssets(),
				"arabesque_ii.ttf");

		TextView bismillahView = (TextView) findViewById(R.id.activity_info_bismillah);
		bismillahView.setTypeface(amiri);

		String muqaddimah = getString(R.string.muqaddimah);
		SpannableStringBuilder ssMuqaddimah = new SpannableStringBuilder(
				muqaddimah);

		String[] honorifics = { " I ", " r ", " t ", " C ", " z ", " V " };

		int pos = 0;
		for (String honorific : honorifics) {
			pos = muqaddimah.indexOf(honorific, 0);
			while (pos != -1) {
				ssMuqaddimah.setSpan(new CustomTypefaceSpan("", arabesque),
						pos, pos + 3, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
				ssMuqaddimah.setSpan(new RelativeSizeSpan(1.6f), pos + 1,
						pos + 2, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
				pos = muqaddimah.indexOf(honorific, pos + 3);
			}
		}

		TextView muqaddimahView = (TextView) findViewById(R.id.activity_info_muqaddimah);
		muqaddimahView.setText(ssMuqaddimah);

	}
	//
	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.info, menu);
	// return true;
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// // Handle action bar item clicks here. The action bar will
	// // automatically handle clicks on the Home/Up button, so long
	// // as you specify a parent activity in AndroidManifest.xml.
	// int id = item.getItemId();
	// if (id == R.id.action_settings) {
	// return true;
	// }
	// return super.onOptionsItemSelected(item);
	// }
}
