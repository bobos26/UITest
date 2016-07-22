package com.skplanet.prototype.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.skplanet.prototype.BuildConfig;
import com.skplanet.prototype.R;
import com.skplanet.prototype.activeAndroid.ActiveAndroidActivity;
import com.skplanet.prototype.adaptors.GlideImageListAdapter;
import com.skplanet.prototype.adaptors.VolleyImageListAdapter;
import com.skplanet.prototype.serverAPIs.DataModel.ImageData;
import com.skplanet.prototype.serverAPIs.Glide.GlideNetUtils;
import com.skplanet.prototype.serverAPIs.Volley.VolleyNetUtils;
import com.skplanet.prototype.tmap.TmapActivity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String KEI_IMAGE_URL = "http://mimgnews2.naver.net/image/001/2015/12/28/PYH2" +
            "015122816990001300_P2_99_20151228202707.jpg?type=w540";
    public static final String SUL_IMAGE_URL = "https://tv.pstatic.net/thm?size=120x150&quality=9" +
            "&q=http://sstatic.naver.net/people/portrait/201506/20150618145705548.jpg";

    private ListView listView;
    private static VolleyImageListAdapter volleyImageListAdapter;
    private static GlideImageListAdapter glideImageListAdapter;
    public static List<ImageData> imageDataList;
    private static ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "This is FloatingActionButton for prototyping app", Snackbar.LENGTH_SHORT)
                        .setAction("TEST", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        })
                        .show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initStetho();
    }

    private void callVolleyImageList() {
        showPDialog();
        imageDataList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list);
        volleyImageListAdapter = new VolleyImageListAdapter(MainActivity.this, imageDataList);
        listView.setAdapter(volleyImageListAdapter);

        VolleyNetUtils volleyNetUtils = VolleyNetUtils.getInstance(getApplicationContext());
        volleyNetUtils.initialize();
    }

    public static void notifyVolleyImageListAdaptor() {
        volleyImageListAdapter.notifyDataSetChanged();
        hidePDialog();
    }

    private void callGlideImageList() {
        showPDialog();
        imageDataList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list);
        glideImageListAdapter = new GlideImageListAdapter(MainActivity.this, imageDataList);
        listView.setAdapter(glideImageListAdapter);

        GlideNetUtils glideNetUtils = GlideNetUtils.getInstance(getApplicationContext());
        glideNetUtils.initialize();
    }

    public static void notifyGlideImageListAdaptor() {
        glideImageListAdapter.notifyDataSetChanged();
        hidePDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void showPDialog() {
        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
    }


    private static void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_volley_list) {
            callVolleyImageList();
            setActionBarTitle("Volley");
        } else if (id == R.id.nav_glide_list) {
            callGlideImageList();
            setActionBarTitle("Glide");
        } else if (id == R.id.nav_send_sms) {
            startActivity(new Intent(MainActivity.this, SendSmsActivity.class));
        } else if (id == R.id.nav_register_gcm) {
            startActivity(new Intent(MainActivity.this, GCMActivity.class));
        } else if (id == R.id.nav_show_chart) {
            startActivity(new Intent(MainActivity.this, ChartActivity.class));
        } else if (id == R.id.nav_show_customer) {
            startActivity(new Intent(MainActivity.this, CustomerActivity.class));
        } else if (id == R.id.nav_tmap) {
            startActivity(new Intent(MainActivity.this, TmapActivity.class));
        } else if (id == R.id.nav_active_android) {
            startActivity(new Intent(MainActivity.this, ActiveAndroidActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setActionBarTitle (String titleStr) {
        getSupportActionBar().setTitle(titleStr);
    }

    private void initStetho() {
        // Debug모드에서만 Stetho를 initialize한다.
        if (BuildConfig.DEBUG) {
            try {
                Class clazz = Class.forName("com.facebook.stetho.Stetho");
                Class[] paramType = {Context.class};
                Method method = clazz.getDeclaredMethod("initializeWithDefaults", paramType);
                method.invoke(null, new Object[]{this});
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
