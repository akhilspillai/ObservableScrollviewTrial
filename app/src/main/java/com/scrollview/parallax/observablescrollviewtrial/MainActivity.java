package com.scrollview.parallax.observablescrollviewtrial;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {

    @Bind(R.id.title)
    TextView mTitleView;
    @Bind(R.id.image)
    ImageView mImageView;
    @Bind(R.id.overlay)
    View mOverlayView;
    @Bind(R.id.list_background)
    View mListBg;
    @Bind(R.id.recycler)
    ObservableRecyclerView mScrollView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;
    private static final int NUM_OF_ITEMS = 100;

    private int mFlexibleSpaceImageHeight, mActionBarSize;
    private PlaylistAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mTitleView.setText(getTitle());
        setTitle(null);

        setSupportActionBar(mToolbar);

        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mActionBarSize = getActionBarSize();

        List<String> playlists = new ArrayList<>();
        playlists.add("Now Playing");
        playlists.add("Playlist 1");
        playlists.add("Playlist 2");
        playlists.add("Playlist 3");
        playlists.add("Playlist 4");
        playlists.add("Playlist 5");
        playlists.add("Playlist 6");


        mScrollView.setLayoutManager(new LinearLayoutManager(this));

        final View headerView = LayoutInflater.from(this).inflate(R.layout.recycler_header, null);

        mScrollView.setScrollViewCallbacks(this);

        setDummyDataWithHeader(mScrollView, headerView);

        headerView.post(new Runnable() {
            @Override
            public void run() {
                headerView.getLayoutParams().height = mFlexibleSpaceImageHeight;
            }
        });

    }

    protected void setDummyDataWithHeader(RecyclerView recyclerView, View headerView) {
        recyclerView.setAdapter(new SimpleHeaderRecyclerAdapter(this, getDummyData(), headerView));
    }

    public static ArrayList<String> getDummyData() {
        return getDummyData(NUM_OF_ITEMS);
    }

    public static ArrayList<String> getDummyData(int num) {
        ArrayList<String> items = new ArrayList<>();
        for (int i = 1; i <= num; i++) {
            items.add("Item " + i);
        }
        return items;
    }

    private int getActionBarSize() {
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            return TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        return 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
        Log.d("Akhil", "scroll is "+scrollY);
        Log.d("Akhil", "minOverlayTransitionY is " + minOverlayTransitionY);
        mOverlayView.setTranslationY(ScrollUtils.getFloat(-scrollY,minOverlayTransitionY, 0));
//        mOverlayView.setAlpha(ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));
        int color = adjustAlpha(Color.BLUE, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));
        mOverlayView.setBackgroundColor(color);

        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
        mTitleView.setPivotX(0);
        mTitleView.setPivotY(0);
        mTitleView.setScaleX(scale);
        mTitleView.setScaleY(scale);

        int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - mTitleView.getMeasuredHeight() * scale);
        float titleTranslationY = ScrollUtils.getFloat(maxTitleTranslationY - scrollY ,
                0, maxTitleTranslationY);
        mTitleView.setTranslationY(titleTranslationY);

//        mListBg.setTranslationY(Math.max(0, -scrollY + mFlexibleSpaceImageHeight));
        mListBg.setTranslationY(ScrollUtils.getFloat(mFlexibleSpaceImageHeight - scrollY ,
                mActionBarSize, mFlexibleSpaceImageHeight));

        Log.d("Akhil", "list bg scroll is "+(mFlexibleSpaceImageHeight-scrollY));
    }

    public int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}
