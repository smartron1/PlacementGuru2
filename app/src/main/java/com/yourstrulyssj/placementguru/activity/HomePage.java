package com.yourstrulyssj.placementguru.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.yourstrulyssj.placementguru.R;
import com.yourstrulyssj.placementguru.helper.SQLiteHandler;
import com.yourstrulyssj.placementguru.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import static com.yourstrulyssj.placementguru.app.AppConfig.h1;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TextView txtName;
    private TextView txtEmail;
    ViewPager viewPager;
    private Button btnLogout;
    private ImageButton imgBtn1;
    private TextView txtAlumnae;
    private TextView txtCompany;
    private TextView txtQuiz;
    private ImageView alumane;
    private View actionBar;

    private TextView name;
    private TextView email;
    private SessionManager session;
    private ImageView company;
    private ImageView quiz;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        images = new ArrayList<>();
        descriptions = new ArrayList<>();
        actionBar = (View) findViewById(R.id.toolbar);
        company = (ImageView)findViewById(R.id.company);
        quiz = (ImageView)findViewById(R.id.experts);





        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        getData();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        TextView bug = (TextView) findViewById(R.id.bug);

        bug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomePage.this, webView.class);

                i.putExtra("url","https://docs.google.com/forms/d/18aYSKA_XY4OgJTSLTDSPgiufLiLgNLFrE6X39ciyPww/edit");

                startActivity(i);
            }
        });


        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.actionbar_title, null);

        TextView actionbar_title = (TextView)v.findViewById(R.id.actionbar);
        alumane = (ImageView)findViewById(R.id.alumnae);
        txtAlumnae = (TextView) findViewById(R.id.alumnaetxt);
        txtAlumnae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Alumnae1.class);
                startActivity(intent);
            }
        });
        alumane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Alumnae1.class);
                startActivity(intent);
            }
        });

        txtCompany = (TextView) findViewById(R.id.companytxt);
        txtCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomePage.this,company.class);
                startActivity(i);
            }
        });

        company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomePage.this,company.class);
                startActivity(i);
            }
        });


        txtQuiz = (TextView) findViewById(R.id.expertstxt);
        txtQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomePage.this, quiz.class);
                startActivity(i);
            }
        });
        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomePage.this, quiz.class);
                startActivity(i);
            }
        });


        actionBar.setCustomView(v);
        getSupportActionBar().setElevation(0);




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        HashMap<String, String> name1 = db.getUserDetails();


        name = (TextView) headerView.findViewById(R.id.username);
        email = (TextView) headerView.findViewById(R.id.usermail);


        name.setText(name1.get("name"));
        email.setText(name1.get("email"));





    }

    public static final String DATA_URL = h1;

    //Tag values to read from json
    public static final String TAG_IMAGE_URL = "url";
    public static final String TAG_DESCRIPTION = "description";
    private ArrayList<String> images;
    private ArrayList<String> descriptions;



    private void getData() {
        //Showing a progress dialog while our app fetches the data from url
        //final ProgressDialog loading = ProgressDialog.show(this, "Please wait...","Fetching data...",false,false);

        //Creating a json array request to get the json from our api
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(DATA_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Dismissing the progressdialog on response
                        //loading.dismiss();

                        //Displaying our grid
                        showGrid(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding our request to the queue
        requestQueue.add(jsonArrayRequest);
    }



    private void showGrid(JSONArray jsonArray){
        //Looping through all the elements of json array
        for(int i = 0; i<jsonArray.length(); i++){
            //Creating a json object of the current index
            JSONObject obj = null;
            try {
                //getting json object from current index
                obj = jsonArray.getJSONObject(i);

                //getting image url and title from json object
                images.add(obj.getString(TAG_IMAGE_URL));
                descriptions.add(obj.getString(TAG_DESCRIPTION));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        //Creating ViewPagerAdapter Object Using GridViewAdapter Logic

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager1);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this,images,descriptions);
        viewPager.setAdapter(viewPagerAdapter);

        //Prevents offscreen view from destroying
        viewPager.setOffscreenPageLimit(images.size());

        //Auto image slider using delayed threads
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new autoSlider(viewPager),6000,6000);


    }


    public class autoSlider extends TimerTask{

        //Passing viewPager Object to avoid nullObjectReference
        public ViewPager viewPager;

        public  autoSlider (ViewPager viewPager){
            this.viewPager = viewPager;
        }




        @Override
        public void run() {

            HomePage.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int a = images.size();
                    if (viewPager.getCurrentItem() == a-1){
                        viewPager.setCurrentItem(0);

                    }else {
                        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                    }
                }
            });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

        if (id == R.id.nav_camera) {
            logoutUser();
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(HomePage.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}
