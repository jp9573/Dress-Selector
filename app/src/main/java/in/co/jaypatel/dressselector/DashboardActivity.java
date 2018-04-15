package in.co.jaypatel.dressselector;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import in.co.jaypatel.dressselector.database.DressItemDatabase;

public class DashboardActivity extends AppCompatActivity implements SlideFragment.ChangeData{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public static int numberOfSlides = 1;
    public static HashMap<Integer,Dress> dressMap = new HashMap<>();
    int currentSlideNumber = 0;
    public static Context context;

    ImageButton leftSlide, rightSlide;
    FloatingActionButton fabFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        context = this;

        leftSlide = findViewById(R.id.iBLeft);
        rightSlide = findViewById(R.id.iBRight);
        fabFavourite = findViewById(R.id.fabFavourite);
        mViewPager = findViewById(R.id.container);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        if(savedInstanceState == null) {
            loadAllFavourites();
        }else {
            dressMap = (HashMap<Integer, Dress>) savedInstanceState.getSerializable("dressMap");
            savedInstanceState.remove("dressMap");
            currentSlideNumber = savedInstanceState.getInt("currentSlideNumber");
            numberOfSlides = savedInstanceState.getInt("numberOfSlides");

            if(savedInstanceState.getString("leftArrow") != null) {
                leftSlide.setVisibility(View.VISIBLE);
            }
            if(savedInstanceState.getString("rightArrow") != null) {
                rightSlide.setVisibility(View.VISIBLE);
            }

            mSectionsPagerAdapter.notifyDataSetChanged();
            mViewPager.setCurrentItem(currentSlideNumber+1);
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentSlideNumber = position;
                if(position == 0 && numberOfSlides == 1) {
                    rightSlide.setVisibility(View.GONE);
                    leftSlide.setVisibility(View.GONE);
                }else if (position == 0 && numberOfSlides != 1) {
                    rightSlide.setVisibility(View.VISIBLE);
                    leftSlide.setVisibility(View.GONE);
                }else if(position == numberOfSlides - 1) {
                    rightSlide.setVisibility(View.GONE);
                    leftSlide.setVisibility(View.VISIBLE);
                }else {
                    rightSlide.setVisibility(View.VISIBLE);
                    leftSlide.setVisibility(View.VISIBLE);
                }

                Dress dress = dressMap.get(position+1);
                if(dress == null) {
                    updateFavourite(false);
                }else {
                    if(dress.isFavourite())
                        updateFavourite(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("dressMap",dressMap);
        outState.putInt("currentSlideNumber",currentSlideNumber);
        outState.putInt("numberOfSlides",numberOfSlides);
        if(leftSlide.getVisibility() == View.VISIBLE)
            outState.putString("leftArrow", "visible");
        if(rightSlide.getVisibility() == View.VISIBLE)
            outState.putString("rightArrow", "visible");
        super.onSaveInstanceState(outState);
    }

    public void addNewSlide(View view) {
        numberOfSlides++;
        mSectionsPagerAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(numberOfSlides-1);
    }

    public void slideRight(View view) {
        mViewPager.setCurrentItem(currentSlideNumber + 1);
    }

    public void slideLeft(View view) {
        mViewPager.setCurrentItem(currentSlideNumber - 1);
    }

    public void addToFavourite(View view) {
        Dress dress = dressMap.get(currentSlideNumber+1);
        if(dress == null) {
            Toast.makeText(this, "First add proper images and then press favourite", Toast.LENGTH_LONG).show();
        }else {
            if(dress.isFavourite()) {
                dress.setFavourite(false);
                updateFavourite(false);
                DeleteItem deleteItem = new DeleteItem();
                deleteItem.execute(dress);
            }else {
                dress.setFavourite(true);
                updateFavourite(true);

                dressMap.remove(currentSlideNumber+1);
                dressMap.put(currentSlideNumber+1, dress);

                InsertItem insertItem = new InsertItem();
                insertItem.execute(dress);
            }
        }
    }

    public void loadAllFavourites() {
        FetchAllItems fetchAllItems = new FetchAllItems();
        fetchAllItems.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DressItemDatabase.destroyInstance();
    }

    @Override
    public void updateFavourite(boolean isFavourite) {
        if(isFavourite) {
            fabFavourite.setImageResource(R.drawable.ic_delete);
            fabFavourite.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }else {
            fabFavourite.setImageResource(R.drawable.ic_favorite_border);
            fabFavourite.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.pink)));
        }
    }

    void updateAdapter(int size) {
        numberOfSlides = size;

        if(size > 1) {
            rightSlide.setVisibility(View.VISIBLE);
        }

        mSectionsPagerAdapter.notifyDataSetChanged();

        mSectionsPagerAdapter.updateFragments();
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        private Observable mObservers = new FragmentObserver();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object){
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            mObservers.deleteObservers();

            SlideFragment slideFragment = SlideFragment.newInstance(position + 1);

            if(slideFragment instanceof Observer)
                mObservers.addObserver(slideFragment);

            return slideFragment;
        }

        @Override
        public int getCount() {
            return numberOfSlides;
        }

        void updateFragments() {
            mObservers.notifyObservers();
        }
    }

    public class FragmentObserver extends Observable {
        @Override
        public void notifyObservers() {
            setChanged(); // Set the changed flag to true, otherwise observers won't be notified.
            super.notifyObservers();
        }
    }

    class FetchAllItems extends AsyncTask<Void, Void, List<Dress>> {

        @Override
        protected List<Dress> doInBackground(Void... voids) {
            List<Dress> dresses =  DressItemDatabase.getDatabase(context).dressItemDao().getDressItems();
            return dresses;
        }

        @Override
        protected void onPostExecute(List<Dress> dressItems) {
            super.onPostExecute(dressItems);
            if(dressItems.size() > 0) {
                if(dressMap == null) {
                    dressMap = new HashMap<>();
                }
                for (int i = 0; i < dressItems.size(); i++) {
                    DashboardActivity.dressMap.put(i+1, dressItems.get(i));
                }
                updateAdapter(dressItems.size());
            }
        }
    }

    static class InsertItem extends AsyncTask<Dress, Void, Long> {

        @Override
        protected Long doInBackground(Dress... params) {
            Dress dress = params[0];
            long id = DressItemDatabase.getDatabase(context).dressItemDao().insertItem(dress);
            return id;
        }

        @Override
        protected void onPostExecute(Long id) {
            super.onPostExecute(id);
            if(id > 0) {
                Toast.makeText(context, "Added to favourites!", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context, "Error in saving data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    static class DeleteItem extends AsyncTask<Dress, Void, Integer> {

        @Override
        protected Integer doInBackground(Dress... params) {
            Dress item = params[0];
            int res;
            if(item != null) {
                res = DressItemDatabase.getDatabase(context).dressItemDao().deleteItem(item);
                if(res < 1) {
                    List<Dress> dresses =  DressItemDatabase.getDatabase(context).dressItemDao().getDressItems();
                    for (Dress dress: dresses) {
                        if(String.valueOf(dress.getTopCloth()).equals(String.valueOf(item.getTopCloth()))) {
                            if (String.valueOf(dress.getBottomCloth()).equals(String.valueOf(item.getBottomCloth()))) {
                                item = dress;
                                break;
                            }
                        }
                    }
                    res = DressItemDatabase.getDatabase(context).dressItemDao().deleteItem(item);
                }
            }else {
                res = -1;
            }
            return res;
        }

        @Override
        protected void onPostExecute(Integer res) {
            super.onPostExecute(res);
            if(res > 0) {
                Toast.makeText(context, "Removed from favourites!", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context, "No data found to be delete", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
