package com.yourstrulyssj.placementguru.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yourstrulyssj.placementguru.R;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by SSJ_Recognized on 24-03-2018.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater LayoutInflater;


    private ArrayList<String> images;
    private ArrayList<String> description;

    public ViewPagerAdapter (Context context, ArrayList<String> images, ArrayList<String> description){
        //Getting all the values
        this.context = context;
        this.images = images;
        this.description = description;
    }


    @Override
    public int getCount() {
        return images.size();
    }


    public Object getItem(int position) {
        return images.get(position);
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
       LayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       View view = LayoutInflater.inflate(R.layout.pagerlayout,null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView2);
        TextView txtview = (TextView) view.findViewById(R.id.textView4);

        new DownloadImageTask(imageView).execute(images.get(position));
        txtview.setText(description.get(position));

        container.addView(view,0);
        return view;



    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

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
