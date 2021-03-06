package com.example.avescera.remindme;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//TODO : Finaliser la mise en forme (bonnes images, valider les couleurs, valider le texte, ...)

public class WelcomeActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private PrefManager prefManager;
    private static LinearLayout dotsLayout;
    private static TextView[] dots;
    private static Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Checking for first time launch - before calling setContentView()
        prefManager = new PrefManager(this);

        if (!prefManager.isFirstTimeLaunch()) {
            goToHomePage();
            finish();
        }

        if (Build.VERSION.SDK_INT >= 19) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        setContentView(R.layout.activity_welcome);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnStart = (Button) findViewById(R.id.btn_start);
        addBottomDots(0);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                if(position == 3) {
                    btnStart.setText(R.string.start);
                } else {
                    btnStart.setText(R.string.skip);
                }
                addBottomDots(position);
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });

        //TODO : ajouter ici la fonction de chargement des catégories de base de la table catégorie.

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHomePage(v);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Context context = getContext();

            View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
            TextView textViewTitle = (TextView) rootView.findViewById(R.id.txtViewWelcomeTitle);
            TextView textViewDesc = (TextView) rootView.findViewById(R.id.txtViewWelcomeDesc);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageViewWelcome);

            int fragment = getArguments().getInt(ARG_SECTION_NUMBER);

            switch (fragment)
            {
                case 1:

                    imageView.setImageResource(R.drawable.ic_menu_camera);
                    textViewTitle.setText(R.string.fragment_1_title);
                    textViewDesc.setText(R.string.fragment_1_desc);

                    rootView.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_screen1));

                    return rootView;

                case 2:

                    imageView.setImageResource(R.drawable.ic_menu_gallery);
                    textViewTitle.setText(R.string.fragment_2_title);
                    textViewDesc.setText(R.string.fragment_2_desc);

                    rootView.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_screen2));

                    return rootView;

                case 3:

                    imageView.setImageResource(R.drawable.ic_menu_manage);
                    textViewTitle.setText(R.string.fragment_3_title);
                    textViewDesc.setText(R.string.fragment_3_desc);

                    rootView.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_screen3));

                    return rootView;

                case 4:

                    imageView.setImageResource(R.drawable.ic_menu_send);
                    textViewTitle.setText(R.string.fragment_4_title);
                    textViewDesc.setText(R.string.fragment_4_desc);

                    rootView.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_screen4));

                    return rootView;

                default:

                    return rootView;

            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
                case 3:
                    return "SECTION 4";
            }
            return null;
        }
    }

    public void goToHomePage(View view) {
        prefManager.setFirstTimeLaunch(false);
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
    }

    public void goToHomePage() {
        prefManager.setFirstTimeLaunch(false);
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[mSectionsPagerAdapter.getCount()];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N){
                dots[i].setText(Html.fromHtml("&#8226;",Html.FROM_HTML_MODE_LEGACY));
            } else {
                dots[i].setText(Html.fromHtml("&#8226;"));
            }
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

}
