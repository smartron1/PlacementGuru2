package com.yourstrulyssj.placementguru.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.yourstrulyssj.placementguru.R;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by SSJ_Recognized on 24-02-2018.
 */

public class GridViewAdapter extends BaseAdapter {
    //Imageloader to load images
    private ImageLoader imageLoader;
    private ImageView img;
    private TextView textView;

    //Context
    private Context context;

    //Array List that would contain the urls and the titles for the images
    private ArrayList<String> images;
    private ArrayList<String> names;

    public GridViewAdapter (Context context, ArrayList<String> images, ArrayList<String> names){
        //Getting all the values
        this.context = context;
        this.images = images;
        this.names = names;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Creating a linear layout
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.adapterlayout,parent,false);
        img = view.findViewById(R.id.imageView1);
        img.setClickable(true);
        //img.setTag(Integer.valueOf(position));

        new DownloadImageTask(img).execute(images.get(position));

        //Creating a textview to show the title
        textView = view.findViewById(R.id.textView2) ;
        textView.setText(names.get(position));

        //Scaling the imageview
        /*networkImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        networkImageView.setLayoutParams(new GridView.LayoutParams(200,200));

        //Adding views to the layout
        linearLayout.addView(textView);
        linearLayout.addView(networkImageView);*/

        //Returnint the layout
        return view;
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
