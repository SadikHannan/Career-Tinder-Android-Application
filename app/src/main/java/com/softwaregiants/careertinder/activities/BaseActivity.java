package com.softwaregiants.careertinder.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.softwaregiants.careertinder.R;
import com.softwaregiants.careertinder.preferences.PreferenceManager;
import com.softwaregiants.careertinder.utilities.Constants;
import com.softwaregiants.careertinder.utilities.UtilityMethods;

import de.hdodenhof.circleimageview.CircleImageView;

abstract class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = BaseActivity.class.getSimpleName();
    Context mContext;
    CircleImageView circleImageView;
    NavigationView navigationView;
    DrawerLayout drawer;
    Toolbar toolbar;
    TextView TVNavEmail;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mContext = this;
    }

    public void addDrawer(String title, int selection) {
        bindViews();
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setTitle(title);
        } catch (NullPointerException e) {
            Log.e(TAG, "addDrawer: ", e);
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        if (selection != 0) {
            navigationView.setCheckedItem(selection);
        }
        TVNavEmail.setText(PreferenceManager.getInstance(getApplicationContext()).getString(Constants.PK_EMAIL, ""));
    }

    public void bindViews() {
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        TVNavEmail = hView.findViewById(R.id.TVNavEmail);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch ( item.getItemId() ) {
            case R.id.nav_job_search:
                startActivity(new Intent( mContext,CandidateDashboardActivity.class ));
                finish();
                break;
            case R.id.nav_edit_profile:
                startActivity(new Intent( mContext,EditCandidateProfile.class ));
                finish();
                break;
            case R.id.nav_job_list:
                startActivity(new Intent( mContext,JobOpeningsListActivity.class ));
                finish();
                break;
            case R.id.nav_view_matches:
                if (PreferenceManager.getInstance(getApplicationContext()).getString(Constants.PK_USER_TYPE,"").equals(Constants.USER_TYPE_JOB_SEEKER)) {
                    startActivity(new Intent(mContext, CandidateMatchViewerActivity.class));
                } else {
                    startActivity(new Intent(mContext, CompanyMatchViewerActivity.class));
                }
                finish();
                break;
            case R.id.nav_logout:
                logOut();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if ( drawer != null &&drawer.isDrawerOpen(GravityCompat.START) ) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void logOut(){
        PreferenceManager.getInstance(getApplicationContext()).clear();

        Toast.makeText(this, "You have been logged out", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public int getActionBarHeight() {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        actionBarHeight = UtilityMethods.dpToPx(actionBarHeight,mContext);
        return actionBarHeight;
    }
}
