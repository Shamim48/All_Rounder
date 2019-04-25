package com.example.abdullahjubayer.shareinfo2.User;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.example.abdullahjubayer.shareinfo2.R;

public class Home_User extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewPagerId);
        FloatingActionButton fab = findViewById(R.id.fab);


        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_homecolorful));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_employeesicon));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_users));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_notification));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_services));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_photogallery));
        //tabLayout.setTabTextColors(Color.WHITE,Color.GREEN);
        // tabLayout.setSelectedTabIndicator(R.color.White);
        tabLayout.setBackgroundResource(R.color.white);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));





        UserFragmentAdapter adapter=new UserFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()){
                    case 0:
                        tab.setIcon(getResources().getDrawable(R.drawable.ic_homecolorful));
                        break;
                    case 1:
                        tab.setIcon(getResources().getDrawable(R.drawable.ic_employeecolorful));
                        break;
                    case 2:
                        tab.setIcon(getResources().getDrawable(R.drawable.ic_userscolorful));
                        break;
                        case 3:
                        tab.setIcon(getResources().getDrawable(R.drawable.ic_notificationcolorful));
                        break;

                    case 4:
                        tab.setIcon(getResources().getDrawable(R.drawable.ic_servicescolorful));
                        break;

                    case 5:
                        tab.setIcon(getResources().getDrawable(R.drawable.ic_photogallerycolorful));
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {
                    case 0:
                        tab.setIcon(getResources().getDrawable(R.drawable.ic_home));
                        break;
                    case 1:
                        tab.setIcon(getResources().getDrawable(R.drawable.ic_employee));
                        break;
                    case 2:
                        tab.setIcon(getResources().getDrawable(R.drawable.ic_users));
                        break;

                        case 3:
                        tab.setIcon(getResources().getDrawable(R.drawable.ic_notification));
                        break;

                    case 4:
                        tab.setIcon(getResources().getDrawable(R.drawable.ic_services));
                        break;

                    case 5:
                        tab.setIcon(getResources().getDrawable(R.drawable.ic_photogallery));
                        break;
                }
                }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home__user, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class UserFragmentAdapter extends FragmentPagerAdapter {
        public UserFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

            switch (i){
                case 0:
                    return new Home_User_FMT();
                case 1:
                    return new Employee_User_FMT();
                case 2:
                    return new Users_user_FMT();
                case 3:
                    return new Notice_User_FMT();
                case 4:
                    return new SerVices_User_FMT();
                case 5:
                    return new PhotosUser_FMT();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 6;
        }
    }
}
