package com.yourstrulyssj.placementguru.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yourstrulyssj.placementguru.R;
import com.yourstrulyssj.placementguru.helper.SQLiteHandler;
import com.yourstrulyssj.placementguru.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.yourstrulyssj.placementguru.app.AppConfig.q1;

public class questions extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private TextView questionTitle;
    private int counter;
    private int score;
    private TextView no;

    private TextView marks;
    private Button previous;
    private Button ok;
    private Button next;


    private GridView gridView;

    //ArrayList for Storing image urls and titles
    private ArrayList<String> question;
    private ArrayList<String> answer;
    private ArrayList<String> option;
    private SQLiteHandler db;
    private SessionManager session;
    private String name;

    public static final String DATA_URL = q1;


    //Tag values to read from json
    public static final String TAG_IMAGE_URL = "dp";
    public static final String TAG_NAME = "company";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());



        setContentView(R.layout.activity_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        questionTitle = (TextView) findViewById(R.id.question);
        marks = (TextView)findViewById(R.id.marks);
        no = (TextView) findViewById(R.id.questionnumber);


        previous = (Button) findViewById(R.id.previous) ;

        ok = (Button) findViewById(R.id.submit) ;

        Intent i1 = getIntent();
        name = i1.getStringExtra("name");


        getSupportActionBar().setElevation(0);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);


        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.actionbar_title, null);

        actionBar.setCustomView(v);
        getSupportActionBar().setElevation(0);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        question = new ArrayList<>();
        option = new ArrayList<>();
        answer = new ArrayList<>();
        getData();
    }






    private void getData(){

        //Showing a progress dialog while our app fetches the data from url
        StringRequest strReq = new StringRequest(Request.Method.POST,
                DATA_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Tag", "Register Response: " + response.toString());



                try {
                    JSONArray jObj = new JSONArray(response);
                    showGrid(jObj);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding our request to the queue
        requestQueue.add(strReq);
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
                question.add(obj.getString("question"));
                option.add(obj.getString("option"));
                answer.add(obj.getString("answer"));



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        //Creating GridViewAdapter Object



        counter = 0;
        score = 0;

        setQuestion(counter,question,option,answer);

        Thread thread = new Thread(){
            @Override
            public void run() {

                previous.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (counter==0){

                        }else {
                            counter -= 1;
                            setQuestion(counter, question, option, answer);
                        }
                    }
                });


            }
        };

        thread.start();








    }

    public void setQuestion(final int counter, final ArrayList question, final ArrayList option, final ArrayList answer){
        no.setText(String.valueOf(counter+1));

        if (counter == question.size()){
            Intent i = new Intent(questions.this,result.class);
            i.putExtra("score",String.valueOf(score));
            startActivity(i);
            Log.d("Score", String.valueOf(score));
            finish();
        }else if (counter == 0){
            questionTitle.setText(question.get(counter).toString());


            final RadioGroup options = (RadioGroup) findViewById(R.id.options);



            options.removeAllViews();
            String [] opt = option.get(counter).toString().split(",");
            for(int i = 0; i < 4; i++) {
                RadioButton button = new RadioButton(this);
                button.setText(opt[i]);
                options.addView(button);
            }

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {




                    if(options.getCheckedRadioButtonId() == Integer.parseInt(answer.get(counter).toString())){
                        score += 10;


                        int x = counter;
                        x+=1;
                        setQuestion(x, question, option, answer);
                    }else {
                        int x = counter;
                        x+=1;
                        setQuestion(x, question, option, answer);
                    }
                }
            });
        }else {
            questionTitle.setText(question.get(counter).toString());


            final RadioGroup options = (RadioGroup) findViewById(R.id.options);



            options.removeAllViews();
            String [] opt = option.get(counter).toString().split(",");
            for(int i = 0; i < 4; i++) {
                RadioButton button = new RadioButton(this);
                button.setText(opt[i]);
                options.addView(button);
            }

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {




                    if(options.getCheckedRadioButtonId() == Integer.parseInt(answer.get(counter).toString())+ counter*4){
                        score += 10;

                        int x = counter;
                        x+=1;
                        setQuestion(x, question, option, answer);
                    }else {
                        int x = counter;
                        x+=1;
                        setQuestion(x, question, option, answer);

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
        getMenuInflater().inflate(R.menu.alumnae1, menu);
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
        Intent intent = new Intent(questions.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
