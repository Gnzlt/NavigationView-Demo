package com.gnzlt.navigationview;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.gnzlt.navigationview.fragments.FavoritesFragment;
import com.gnzlt.navigationview.fragments.HomeFragment;
import com.gnzlt.navigationview.fragments.SettingsFragment;


public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Toolbar toolbar = null;
    ActionBarDrawerToggle drawerToggle;

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        setupDrawerLayout();
        setupInit();
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);
    }

    private void setupInit() {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new HomeFragment()).commit();

        drawerLayout.openDrawer(GravityCompat.START);
    }

    private void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass;
        Float elevation = getResources().getDimension(R.dimen.elevation_toolbar);

        switch (menuItem.getItemId()) {
            case R.id.drawer_home:
                fragmentClass = HomeFragment.class;
                break;
            case R.id.drawer_favorites:
                fragmentClass = FavoritesFragment.class;
                elevation = 0.0f;
                break;
            case R.id.drawer_settings:
                fragmentClass = SettingsFragment.class;
                break;

            default:
                fragmentClass = HomeFragment.class;
                break;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack("FRAGMENT");
        fragmentTransaction.commit();

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            toolbar.setElevation(elevation);

        drawerLayout.closeDrawers();
    }

    public void showSnackbarMessage(View v) {
        EditText et_snackbar = (EditText) findViewById(R.id.et_snackbar);
        TextInputLayout textInputLayout = (TextInputLayout) findViewById(R.id.textInputLayout);
        View view = findViewById(R.id.coordinator_layout);
        if (et_snackbar.getText().toString().isEmpty()) {
            textInputLayout.setError(getString(R.string.alert_text));
        } else {
            textInputLayout.setErrorEnabled(false);
            et_snackbar.onEditorAction(EditorInfo.IME_ACTION_DONE);
            Snackbar.make(view, et_snackbar.getText().toString(), Snackbar.LENGTH_LONG)
                    .setAction(getResources().getString(android.R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Do nothing
                        }
                    })
                    .show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
}
