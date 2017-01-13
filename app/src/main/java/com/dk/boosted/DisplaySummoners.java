package com.dk.boosted;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import android.view.View;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.Arrays;


public class DisplaySummoners extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_summoners);
        Intent intent = getIntent();
        String[] champFulls = intent.getStringArrayExtra("champFulls");
        String[] summFulls = intent.getStringArrayExtra("summFulls");
        String[] ranks = intent.getStringArrayExtra("ranks");
        String patch = intent.getStringExtra("patch");
        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplaySummoners.this.finish();
            }
        });
//        Log.d("CHAMPFULLS", Arrays.toString(champFulls));
//        Log.d("SUMMFULLS", Arrays.toString(summFulls));
        for (int i = 1; i <= 10; i++) {
            String imageId = "summoner" + i;
            int resId = getResources().getIdentifier(imageId, "id", getPackageName());
            new DownloadImageTask((ImageView) findViewById(resId))
                    .execute("http://ddragon.leagueoflegends.com/cdn/" + patch + "/img/champion/" +
                        champFulls[i - 1]);
            String snId = "summName" + i;
            int resId2 = getResources().getIdentifier(snId, "id", getPackageName());
            TextView sn = (TextView) findViewById(resId2);
            sn.setText(summFulls[i - 1]);
            String rankId = "summRank" + i;
            int resId3 = getResources().getIdentifier(rankId, "id", getPackageName());
            TextView rank = (TextView) findViewById(resId3);
            rank.setText(ranks[i - 1]);
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
