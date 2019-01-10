package com.yourstrulyssj.placementguru.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yourstrulyssj.placementguru.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class user_Profile extends AppCompatActivity {
    private ImageView img;
    private TextView txt;
    private TextView company;
    private TextView packageTxt;
    private TextView interviewTxt;
    private ImageView mail;
    private TextView mail1;
    private TextView mail2;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__profile);
        img = findViewById(R.id.imageView3);
        txt = findViewById(R.id.textView5);
        mail = (ImageView) findViewById(R.id.mail);
        mail1 = (TextView) findViewById(R.id.mail1);
        mail2 = (TextView) findViewById(R.id.mail2);
        back = (ImageView) findViewById(R.id.back) ;

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent i1 = getIntent();
        String url = i1.getStringExtra("url");
        String name = i1.getStringExtra("name");
        final String companytitle = i1.getStringExtra("company");
        String package1 = i1.getStringExtra("package");
        String interview = i1.getStringExtra("interview");
        final String companydp = i1.getStringExtra("companydp");
        final String email = i1.getStringExtra("email");


        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail(email);
            }
        });

        mail1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail(email);
            }
        });

        mail2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail(email);
            }
        });





        new DownloadImageTask(img).execute(url);
        txt.setText(name);
        company = (TextView) findViewById(R.id.companyname);
        packageTxt = (TextView) findViewById(R.id.package1);
        interviewTxt = (TextView) findViewById(R.id.interview);

        company.setText(companytitle);
        packageTxt.setText(package1);
        interviewTxt.setText(interview);

        company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(user_Profile.this, company_Profile.class);
                i.putExtra("name",companytitle);
                i.putExtra("url",companydp);
                startActivity(i);
            }
        });

    }

    private void sendMail(String mail){

        Intent mailClient = new Intent(Intent.ACTION_VIEW);
        mailClient.setData(Uri.parse(mail));
        mailClient.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmailExternal");
        startActivity(mailClient);


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
