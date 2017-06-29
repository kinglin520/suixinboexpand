package com.sharewisesc.inbroadheart.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.sharewisesc.inbroadheart.R;
import com.sharewisesc.inbroadheart.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by wenlin on 2017/6/16.
 */

public class MainActivity extends SupportActivity implements NavigationView.OnNavigationItemSelectedListener , MainFragment.OnFragmentOpenDrawerListener{
    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_container, MainFragment.getInstance());
        }

        initView();
    }

    private void initView() {

    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置默认Fragment动画  默认竖向(和安卓5.0以上的动画相同)
//        return super.onCreateFragmentAnimator();
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
        // 设置自定义动画
//        return new FragmentAnimator(enter,exit,popEnter,popExit);
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);

        drawerLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                int id = item.getItemId();

                final SupportFragment topFragment = getTopFragment();

                if (id == R.id.nav_home) {
                } else if (id == R.id.nav_discover) {
                } else if (id == R.id.nav_msg) {
                } else if (id == R.id.nav_login) {
                } else if (id == R.id.nav_swipe_back) {
                } else if (id == R.id.nav_swipe_back_f) {
                }
            }
        }, 250);

        return true;
    }

    @Override
    public void onBackPressedSupport() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (getTopFragment() instanceof MainFragment ) {
                if (!Utils.doubleClickCheck()) {
                    Snackbar.make(MainActivity.this.getWindow().getDecorView().findViewById(android.R.id.content), "再按一次退出 App!", Snackbar.LENGTH_SHORT).show();
                } else {
                    if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                        pop();
                    }else {
                        finish();
                    }
                }
            } else {
                finish();
            }
        }
    }

    @Override
    public void onOpenDrawer() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }
}
