package com.yourstrulyssj.placementguru.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import static com.yourstrulyssj.placementguru.app.AppConfig.alumnaeEdit;

public class AlumnaeEdit extends AppCompatActivity {
    private ImageView img;
    private TextView txt;
    private TextView company1;
    private TextView packageTxt;
    private TextView interviewTxt;
    private ImageView mail;
    private TextView mail1;
    private TextView mail2;
    private ImageView back;
    private Button edit;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumnae_edit);
        img = findViewById(R.id.imageView3);
        txt = findViewById(R.id.textView5);
        packageTxt = (TextView) findViewById(R.id.package1);
        company1 = (TextView) findViewById(R.id.companyname);
        interviewTxt = (TextView) findViewById(R.id.interview);
        mail = (ImageView) findViewById(R.id.mail);
        mail1 = (TextView) findViewById(R.id.mail1);
        mail2 = (TextView) findViewById(R.id.mail2);
        back = (ImageView) findViewById(R.id.back) ;
        edit = (Button) findViewById(R.id.edit);
        back.setVisibility(View.GONE);

        Intent i1 = getIntent();

        name = i1.getStringExtra("name");

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AlumnaeEdit.this, AlumnaeSuggestion.class);
                i.putExtra("name", name);
                startActivity(i);
            }
        });



        txt.setText(name);
        getData();








    }

    private void sendMail(String mail){

        Intent mailClient = new Intent(Intent.ACTION_VIEW);
        mailClient.setData(Uri.parse(mail));
        mailClient.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmailExternal");
        startActivity(mailClient);


    }

    private void getData(){

        //Showing a progress dialog while our app fetches the data from url
        StringRequest strReq = new StringRequest(Request.Method.POST,
                alumnaeEdit, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Tag", "Register Response: " + response.toString());
                Log.d("TYPE RESPONSE", response.getClass().getName());
                if (response == ""){
                    Intent i = new Intent(AlumnaeEdit.this,alumnaeFeedback.class);
                    startActivity(i);
                }



                try {
                    JSONArray jObj = new JSONArray(response);

                        try {
                            jObj.get(0);
                            showGrid(jObj);
                        }catch (JSONException e){
                            Intent i = new Intent(AlumnaeEdit.this,alumnaeFeedback.class);
                            startActivity(i);
                            finish();
                        }





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

    private void showGrid(JSONArray jsonArray) {
        //Looping through all the elements of json array
        for (int i = 0; i < jsonArray.length(); i++) {
            //Creating a json object of the current index
            JSONObject obj = null;
            try {
                //getting json object from current index
                obj = jsonArray.getJSONObject(i);
                if(obj.getString("company") == null){
                    Intent i2 = new Intent(AlumnaeEdit.this,alumnaeFeedback.class);
                    startActivity(i2);
                }else {

                    //getting image url and title from json object
                    String dp = (obj.getString("dp"));
                    String company = (obj.getString("company"));
                    String package1 = (obj.getString("package"));
                    String interview = (obj.getString("interview"));
                    String email = (obj.getString("email"));

                    new DownloadImageTask(img).execute(dp);
                    packageTxt.setText(package1);
                    company1.setText(company);
                    interviewTxt.setText(interview);
                    mail2.setText(email);
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }


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
                InputStream in = new java.net.URL(urldisplay).openStream();
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


