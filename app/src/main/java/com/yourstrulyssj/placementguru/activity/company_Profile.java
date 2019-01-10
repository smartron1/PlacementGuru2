package com.yourstrulyssj.placementguru.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yourstrulyssj.placementguru.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class company_Profile extends AppCompatActivity {
    private ImageView img;
    private TextView txt;
    private TextView desc1;
    private TextView domain;
    private TextView placement;
    private TextView requirements;
    private TextView focus;
    private TextView moreinfo;
    private String url;
    private TextView link;
    private ImageView link1;
    private TextView link2;
    private String DATA_URL = "http://192.168.43.72/android_login_api/getDesc.php";

    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company__profile);
        img = findViewById(R.id.imageView3);
        txt = findViewById(R.id.textView5);
        desc1 = (TextView)findViewById(R.id.desc);
        domain = (TextView) findViewById(R.id.domain);
        placement = (TextView) findViewById(R.id.placement);
        requirements = (TextView) findViewById(R.id.requirements);
        focus = (TextView) findViewById(R.id.focus);
        moreinfo = (TextView) findViewById(R.id.moreinfo);
        link = (TextView) findViewById(R.id.link);
        link1 = (ImageView) findViewById(R.id.link1);
        link2 = (TextView) findViewById(R.id.link2);
        back = (ImageView) findViewById(R.id.back);


        link1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(company_Profile.this, webView.class);

                    i.putExtra("url",url);

                startActivity(i);
            }
        });

        link2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(company_Profile.this, webView.class);

                i.putExtra("url",url);

                startActivity(i);
            }
        });




        back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        finish();
                                    }
                                }
        );

        Intent i1 = getIntent();
        String url = i1.getStringExtra("url");
        final String name = i1.getStringExtra("name");
        String desc = i1.getStringExtra("desc");

        getDesc(name,desc1);




        new DownloadImageTask(img).execute(url);
        txt.setText(name);


    }



    private void getDesc(final String name, final TextView desc1) {

        //Showing a progress dialog while our app fetches the data from url
        StringRequest strReq = new StringRequest(Request.Method.POST,
                DATA_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Tag", "Register Response: " + response.toString());


                try {
                    JSONArray jObj = new JSONArray(response);
                    Log.d("JSONArray",jObj.toString());
                    setDesc(jObj, desc1);


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

    private void setDesc(JSONArray jobj, TextView desc1){

        try {
            final JSONObject object = jobj.getJSONObject(0);
            desc1.setText(object.getString("desc"));
            url = object.getString("link");
            domain.setText(object.getString("domain"));
            placement.setText(object.getString("placement"));
            focus.setText(object.getString("focus"));
            moreinfo.setText(object.getString("cwiki"));
            requirements.setText(object.getString("requirements"));


            moreinfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(company_Profile.this, webView.class);
                    try {
                        i.putExtra("url",object.getString("cwiki"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    startActivity(i);
                }
            });

            link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(company_Profile.this, webView.class);
                    try {
                        i.putExtra("url",object.getString("link"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(i);

                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
