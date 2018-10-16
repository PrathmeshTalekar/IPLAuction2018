package com.spit.iplauction2018.iplauction2018;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Menu menu;
    Bundle frag_bundle;
    public String message;
    private Boolean exit = false;
    Fragment fragment = null;
    HomeFragment homeFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        message = bundle.getString("lobby_path");
        frag_bundle = new Bundle();
        frag_bundle.putString("lobby", message);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (exit) {
                finish();
            } else {
                Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3 * 1000);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        displaySelectedScreen(R.id.home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game_main, menu);
        this.menu = menu;
        return true;
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }

    private void displaySelectedScreen(int id) {
        switch (id) {
            case R.id.home:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    if (menu == null) {
                        Runnable r = new Runnable() {
                            @Override
                            public void run() {
                                ((HomeFragment) fragment).setGlobalMenu(menu);
                            }
                        };
                        Handler h = new Handler();
                        h.postDelayed(r, 1000);
                    } else {
                        ((HomeFragment) fragment).setGlobalMenu(menu);
                    }
                }
                fragment = homeFragment;
                fragment.setArguments(frag_bundle);
                break;
            case R.id.help:
                fragment = new HelpFragment();
                break;
            case R.id.team:
                fragment = new TeamFragment();
                fragment.setArguments(frag_bundle);
                break;
            case R.id.rules:
                fragment = new RulesFragment();
                break;
            case R.id.credits:
                fragment = new CreditsFragment();
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
