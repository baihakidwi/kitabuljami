package com.wahdahislamiyah.android.kitabuljami;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wahdahislamiyah.android.kitabuljami.R;

public class HaditsViewActivity extends ActionBarActivity {

	public static Typeface sFontAmiri;
	public static Typeface sFontArabesque;
	public static final String ARG_SECTION_NUMBER = "section_number";

	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hadits_view);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager(), this);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		int section = getIntent().getIntExtra(ARG_SECTION_NUMBER, 0);
		mViewPager.setCurrentItem(section);

	}

	public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

		private DataLab mDataLab;

		public SectionsPagerAdapter(FragmentManager fm, Context context) {
			super(fm);
			mDataLab = DataLab.getInstance(context);
		}

		@Override
		public Fragment getItem(int position) {
			return PlaceholderFragment.newInstance(position);
		}

		@Override
		public int getCount() {
			return mDataLab.getHaditsCount();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "Hadits ke-" + String.valueOf(position);
		}
	}

	public void setActionBarTitle(String title) {
		getSupportActionBar().setTitle(title);
	}

	public void setActionBarSubtitle(String subtitle) {
		getSupportActionBar().setSubtitle(subtitle);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		DataLab.getInstance(this).writeBookmark();
	}

	public static class PlaceholderFragment extends Fragment {

		private Menu mMenu;
		private TextView mJudul;
		private TextView mArab;
		private TextView mTerjemah;
		private TextView mNote;

		private static final String ARG_SECTION_NUMBER = "section_number";

		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_hadits_view,
					container, false);

			if (sFontAmiri == null) {
				sFontAmiri = Typeface.createFromAsset(
						getActivity().getAssets(), "amiri-regular.ttf");
			}

			if (sFontArabesque == null) {
				sFontArabesque = Typeface.createFromAsset(getActivity()
						.getAssets(), "arabesque_ii.ttf");
			}

			DataLab dataLab = DataLab.getInstance(getActivity());
			int haditsId = getArguments().getInt(ARG_SECTION_NUMBER, 0);
			int bab = dataLab.getBabId(haditsId);
			((HaditsViewActivity) getActivity()).setActionBarTitle("Bab "
					+ (bab + 1));
			((HaditsViewActivity) getActivity()).setActionBarSubtitle("Hadits "
					+ haditsId);
			Hadits hadits = dataLab.getHadits(haditsId);

			TextView judulView = (TextView) rootView
					.findViewById(R.id.fragment_hadits_view_judul);
			mJudul = judulView;

			TextView arabView = (TextView) rootView
					.findViewById(R.id.fragment_hadits_view_arab);
			mArab = arabView;

			TextView terjemahView = (TextView) rootView
					.findViewById(R.id.fragment_hadits_view_terjemah);
			mTerjemah = terjemahView;

			TextView noteView = (TextView) rootView
					.findViewById(R.id.fragment_hadits_view_note);
			mNote = noteView;

			SpannableStringBuilder ssJudul = getIndoTeks(hadits.getJudul());
			SpannableStringBuilder ssArab = getArabTeks(hadits.getArab());
			SpannableStringBuilder ssTerjemah = getIndoTeks(hadits
					.getTerjemah());
			SpannableStringBuilder ssNote = getIndoTeks(hadits.getNote());

			judulView.setText(ssJudul);
			arabView.setText(ssArab);
			terjemahView.setText(ssTerjemah);
			noteView.setText(ssNote);

			rePaint();
			return rootView;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setHasOptionsMenu(true);
		}

		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			super.onCreateOptionsMenu(menu, inflater);
			inflater.inflate(R.menu.hadits_view, menu);
			int id = getArguments().getInt(ARG_SECTION_NUMBER, -1);
			if (DataLab.getInstance(getActivity()).isBookmarked(id)) {
				MenuItem bookmark = menu.findItem(R.id.action_bookmark);
				bookmark.setIcon(R.drawable.bookmark2);
			}
			mMenu = menu;
		}

		@Override
		@SuppressWarnings("deprecation")
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.action_bookmark:
				int haditsId = getArguments().getInt(ARG_SECTION_NUMBER);
				MenuItem bookmarkButton = mMenu.findItem(R.id.action_bookmark);
				if (DataLab.getInstance(getActivity()).toggleBookmark(haditsId)) {
					bookmarkButton.setIcon(R.drawable.bookmark2);
				} else {
					bookmarkButton.setIcon(R.drawable.bookmark1);
				}
				break;

			case R.id.action_copy:
				int id = getArguments().getInt(ARG_SECTION_NUMBER, 0);
				Hadits hadits = DataLab.getInstance(getActivity())
						.getHadits(id);
				String arabcopy = hadits.getArab();
				arabcopy = replaceTeksArab(arabcopy);
				String terjemahcopy = hadits.getTerjemah();
				terjemahcopy = replaceTeksIndo(terjemahcopy);

				String text = arabcopy + "\n\n" + terjemahcopy;
				copyToClipboard(text);
				break;

			case R.id.action_copy_hadits:
				id = getArguments().getInt(ARG_SECTION_NUMBER, 0);
				hadits = DataLab.getInstance(getActivity()).getHadits(id);
				text = hadits.getArab();
				text = replaceTeksArab(text);
				copyToClipboard(text);
				break;
			case R.id.action_copy_terjemah:
				id = getArguments().getInt(ARG_SECTION_NUMBER, 0);
				hadits = DataLab.getInstance(getActivity()).getHadits(id);
				text = hadits.getTerjemah();
				text = replaceTeksIndo(text);
				copyToClipboard(text);
				break;
			case R.id.action_enlarge_text:
				Context context = getActivity();
				String settingArabTextSize = context
						.getString(R.string.setting_text_size_arab);
				String settingTextSize = context
						.getString(R.string.setting_text_size);

				SharedPreferences sharedPref = getActivity().getPreferences(
						MODE_PRIVATE);
				int textArabSize = sharedPref.getInt(settingArabTextSize, 22);
				int textSize = sharedPref.getInt(settingTextSize, 16);
				if (textArabSize <= 36) {
					textArabSize += 2;
					textSize += 2;
				}

				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putInt(settingArabTextSize, textArabSize);
				editor.putInt(settingTextSize, textSize);
				editor.commit();
				rePaint();
				break;
			case R.id.action_shrink_text:
				context = getActivity();
				settingArabTextSize = context
						.getString(R.string.setting_text_size_arab);
				settingTextSize = context.getString(R.string.setting_text_size);

				sharedPref = getActivity().getPreferences(MODE_PRIVATE);
				textArabSize = sharedPref.getInt(settingArabTextSize, 22);
				textSize = sharedPref.getInt(settingTextSize, 16);
				if (textArabSize > 16) {
					textArabSize -= 2;
					textSize -= 2;
				}

				editor = sharedPref.edit();
				editor.putInt(settingArabTextSize, textArabSize);
				editor.putInt(settingTextSize, textSize);
				editor.commit();
				rePaint();
				break;
			default:
				return super.onOptionsItemSelected(item);
			}
			return true;
		}

		private void rePaint() {
			Context context = getActivity();
			String arab = context.getString(R.string.setting_text_size_arab);
			String teks = context.getString(R.string.setting_text_size);

			SharedPreferences sharedPref = getActivity().getPreferences(
					MODE_PRIVATE);
			int textArabSize = sharedPref.getInt(arab, 20);
			int textSize = sharedPref.getInt(teks, 15);

			mJudul.setTextSize(textSize);
			mArab.setTextSize(textArabSize);
			mTerjemah.setTextSize(textSize);
			mNote.setTextSize(textSize);
		}

		private SpannableStringBuilder getArabTeks(String text) {
			SpannableStringBuilder ssText = new SpannableStringBuilder(text);
			ssText.setSpan(new CustomTypefaceSpan("", sFontAmiri), 0,
					text.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			CustomTypefaceSpan customTypefaceSpan = new CustomTypefaceSpan("",
					sFontArabesque);

			String[] honorifics = { " I ", " r ", " t ", " C ", " z " };
			int pos = 0;
			for (String honorific : honorifics) {
				pos = text.indexOf(honorific, 0);
				while (pos != -1) {
					ssText.setSpan(new CustomTypefaceSpan("", sFontArabesque),
							pos, pos + 3, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
					ssText.setSpan(new RelativeSizeSpan(1.6f), pos + 1,
							pos + 2, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
					pos = text.indexOf(honorific, pos + 3);
				}
			}

			return ssText;
		}

		private SpannableStringBuilder getIndoTeks(String text) {
			SpannableStringBuilder ssText = new SpannableStringBuilder(text);
			CustomTypefaceSpan customTypefaceSpan = new CustomTypefaceSpan("",
					sFontArabesque);
			String[] honorifics = { " I ", " r ", " t ", " C ", " z " };
			int pos = 0;
			for (String honorific : honorifics) {
				pos = text.indexOf(honorific, 0);
				while (pos != -1) {
					ssText.setSpan(new CustomTypefaceSpan("", sFontArabesque),
							pos, pos + 3, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
					ssText.setSpan(new RelativeSizeSpan(1.6f), pos + 1,
							pos + 2, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
					pos = text.indexOf(honorific, pos + 3);
				}
			}

			return ssText;
		}

		private String replaceTeksArab(String text) {
			text = text.replaceAll(" I ", " " + getString(R.string.swt_arab)
					+ " ");
			text = text.replaceAll(" r ", " " + getString(R.string.saw_arab)
					+ " ");
			text = text.replaceAll(" t ", " " + getString(R.string.ra_arab)
					+ " ");
			text = text.replaceAll(" C ", " " + getString(R.string.rma_arab)
					+ " ");
			text = text.replaceAll(" z ", " " + getString(R.string.rha_arab)
					+ " ");
			return text;
		}

		private String replaceTeksIndo(String text) {
			text = text.replaceAll(" I ", " "
					+ getString(R.string.swt_indonesia) + " ");
			text = text.replaceAll(" r ", " "
					+ getString(R.string.saw_indonesia) + " ");
			text = text.replaceAll(" t ", " "
					+ getString(R.string.ra_indonesia) + " ");
			text = text.replaceAll(" C ", " "
					+ getString(R.string.rma_indonesia) + " ");
			text = text.replaceAll(" z ", " "
					+ getString(R.string.rha_indonesia) + " ");
			return text;
		}

		private void copyToClipboard(String text) {
			int sdk = android.os.Build.VERSION.SDK_INT;
			if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
				android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getActivity()
						.getSystemService(Context.CLIPBOARD_SERVICE);
				clipboard.setText(text);
			} else {
				android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity()
						.getSystemService(Context.CLIPBOARD_SERVICE);
				android.content.ClipData clip = android.content.ClipData
						.newPlainText("text label", text);
				clipboard.setPrimaryClip(clip);
			}
			Toast.makeText(getActivity(), "hadits disalin ke clipboard", 1000)
					.show();
		}
	}
}
