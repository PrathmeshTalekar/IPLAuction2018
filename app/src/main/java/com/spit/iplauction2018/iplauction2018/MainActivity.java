package com.spit.iplauction2018.iplauction2018;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    //    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static final int RC_SIGN_IN=1;
    Bundle frag_bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("lobby_path");
        frag_bundle = new Bundle();
        frag_bundle.putString("lobby", message);
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mFirebaseAuth=FirebaseAuth.getInstance();
//        final List<AuthUI.IdpConfig> providers = Arrays.asList(
//                new AuthUI.IdpConfig.EmailBuilder().build(),
//                new AuthUI.IdpConfig.GoogleBuilder().build());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        displaySelectedScreen(R.id.home);

//        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user=firebaseAuth.getCurrentUser();
//                if(user!=null){
//                    //user signed in
//                    Toast.makeText(MainActivity.this,"You're now signed in",Toast.LENGTH_SHORT).show();
//                }else{
//                    //user signed out
//                    startActivityForResult(
//                            AuthUI.getInstance()
//                                    .createSignInIntentBuilder()
//                                    .setIsSmartLockEnabled(false)
//                                    .setAvailableProviders(providers)
//                                    .build(),
//                            RC_SIGN_IN);
//                }
//            }
//        };
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RC_SIGN_IN) {
//            if (resultCode == RESULT_OK) {
//                Toast.makeText(this, "You're Signed In", Toast.LENGTH_SHORT).show();
//            } else if (resultCode == RESULT_CANCELED) {
//                Toast.makeText(this, "Signing Out", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        }
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.sign_out_menu:
//                AuthUI.getInstance().signOut(this);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
//        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game_main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }
    private void displaySelectedScreen(int id) {

        Fragment fragment = null;

        switch (id) {
            case R.id.home:
                fragment = new HomeFragment();
                fragment.setArguments(frag_bundle);
                break;
            case R.id.help:
                fragment = new HelpFragment();
                break;
            case R.id.sell:
                fragment = new SellFragment();
                break;
            case R.id.team:
                fragment = new TeamFragment();
                break;
            case R.id.rules:
                fragment = new RulesFragment();
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
