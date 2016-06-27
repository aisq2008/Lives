package com.allyn.lives.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.allyn.lives.R;
import com.allyn.lives.activity.base.BaseActivity;
import com.allyn.lives.app.MainApp;
import com.allyn.lives.fragment.SettingsFragment;
import com.allyn.lives.fragment.TranslationFragment;
import com.allyn.lives.fragment.books.BooksMainFragment;
import com.allyn.lives.fragment.books.RecommendBooksFragment;
import com.allyn.lives.fragment.music.local.MusicLocalFragment;
import com.allyn.lives.fragment.video.TVFragment;
import com.allyn.lives.view.bottontab.BottomBarTab;
import com.allyn.lives.view.bottontab.BottomNavigationBar;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationBar bottomLayout;
    private boolean isreome = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Instant run");

        isreome = getSharedPreferences("config", MODE_PRIVATE).getBoolean("isUserDarkMode", false);

        if (isreome) {
            setTheme(R.style.AppTheme);
        } else {
            setTheme(R.style.Mytheme);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, BooksMainFragment.newInstance()).commitAllowingStateLoss();

        setUpBottomNavigationBar();
    }

    public void setUpBottomNavigationBar() {
        bottomLayout = (BottomNavigationBar) findViewById(R.id.bottomLayout);
        bottomLayout.addTab(R.mipmap.ic_favorite_white, "分类图书", MainApp.getContexts().getResources().getColor(R.color.colorPrimary));
        bottomLayout.addTab(R.mipmap.ic_book_selected, "推荐图书", MainApp.getContexts().getResources().getColor(R.color.colorAccent));
        bottomLayout.setOnTabListener(new BottomNavigationBar.TabListener() {
            @Override
            public void onSelected(BottomBarTab tab, int position) {
                Fragment fragment = null;
                switch (position) {
                    case 0:
                        fragment = BooksMainFragment.newInstance();
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case 1:
                        fragment = RecommendBooksFragment.newInstance();
                        break;
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment)
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .commitAllowingStateLoss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        int id = item.getItemId();
        if (id == R.id.nav_gallery) {
            fragment = BooksMainFragment.newInstance();
        } else if (id == R.id.nav_camera) {
            fragment = MusicLocalFragment.newInstance();
            bottomLayout.setVisibility(View.GONE);
        } else if (id == R.id.nav_manage) {
            fragment = TranslationFragment.newInstance();
            bottomLayout.setVisibility(View.GONE);
        } else if (id == R.id.nav_slideshow) {
            fragment = TVFragment.newInstance();
            bottomLayout.setVisibility(View.GONE);
        } else if (id == R.id.nav_share) {
            setDarkTheme(isreome);
            this.recreate();
            return true;
        } else if (id == R.id.nav_send) {
            fragment = SettingsFragment.newInstance();
            bottomLayout.setVisibility(View.GONE);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .commitAllowingStateLoss();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setDarkTheme(boolean is) {
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isUserDarkMode", !is);
        editor.commit();
    }
}