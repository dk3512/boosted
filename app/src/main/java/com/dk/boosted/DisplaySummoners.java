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
        new DownloadImageTask((ImageView) findViewById(R.id.summoner1))
                .execute("http://ddragon.leagueoflegends.com/cdn/" + patch + "/img/champion/" +
                    champFulls[0]);
        new DownloadImageTask((ImageView) findViewById(R.id.summoner2))
                .execute("http://ddragon.leagueoflegends.com/cdn/" + patch + "/img/champion/" +
                    champFulls[1]);
        new DownloadImageTask((ImageView) findViewById(R.id.summoner3))
                .execute("http://ddragon.leagueoflegends.com/cdn/" + patch + "/img/champion/" +
                        champFulls[2]);
        new DownloadImageTask((ImageView) findViewById(R.id.summoner4))
                .execute("http://ddragon.leagueoflegends.com/cdn/" + patch + "/img/champion/" +
                        champFulls[3]);
        new DownloadImageTask((ImageView) findViewById(R.id.summoner5))
                .execute("http://ddragon.leagueoflegends.com/cdn/" + patch + "/img/champion/" +
                        champFulls[4]);
        new DownloadImageTask((ImageView) findViewById(R.id.summoner6))
                .execute("http://ddragon.leagueoflegends.com/cdn/" + patch + "/img/champion/" +
                        champFulls[5]);
        new DownloadImageTask((ImageView) findViewById(R.id.summoner7))
                .execute("http://ddragon.leagueoflegends.com/cdn/" + patch + "/img/champion/" +
                        champFulls[6]);
        new DownloadImageTask((ImageView) findViewById(R.id.summoner8))
                .execute("http://ddragon.leagueoflegends.com/cdn/" + patch + "/img/champion/" +
                        champFulls[7]);
        new DownloadImageTask((ImageView) findViewById(R.id.summoner9))
                .execute("http://ddragon.leagueoflegends.com/cdn/" + patch + "/img/champion/" +
                        champFulls[8]);
        new DownloadImageTask((ImageView) findViewById(R.id.summoner10))
                .execute("http://ddragon.leagueoflegends.com/cdn/" + patch + "/img/champion/" +
                        champFulls[9]);
        //--- text view---
        TextView sn1 = (TextView) findViewById(R.id.summName1);
        sn1.setText(summFulls[0]);
        TextView sn2 = (TextView) findViewById(R.id.summName2);
        sn2.setText(summFulls[1]);
        TextView sn3 = (TextView) findViewById(R.id.summName3);
        sn3.setText(summFulls[2]);
        TextView sn4 = (TextView) findViewById(R.id.summName4);
        sn4.setText(summFulls[3]);
        TextView sn5 = (TextView) findViewById(R.id.summName5);
        sn5.setText(summFulls[4]);
        TextView sn6 = (TextView) findViewById(R.id.summName6);
        sn6.setText(summFulls[5]);
        TextView sn7 = (TextView) findViewById(R.id.summName7);
        sn7.setText(summFulls[6]);
        TextView sn8 = (TextView) findViewById(R.id.summName8);
        sn8.setText(summFulls[7]);
        TextView sn9 = (TextView) findViewById(R.id.summName9);
        sn9.setText(summFulls[8]);
        TextView sn10 = (TextView) findViewById(R.id.summName10);
        sn10.setText(summFulls[9]);
        TextView rank1 = (TextView) findViewById(R.id.summRank1);
        rank1.setText(ranks[0]);
        TextView rank2 = (TextView) findViewById(R.id.summRank2);
        rank2.setText(ranks[1]);
        TextView rank3 = (TextView) findViewById(R.id.summRank3);
        rank3.setText(ranks[2]);
        TextView rank4 = (TextView) findViewById(R.id.summRank4);
        rank4.setText(ranks[3]);
        TextView rank5 = (TextView) findViewById(R.id.summRank5);
        rank5.setText(ranks[4]);
        TextView rank6 = (TextView) findViewById(R.id.summRank6);
        rank6.setText(ranks[5]);
        TextView rank7 = (TextView) findViewById(R.id.summRank7);
        rank7.setText(ranks[6]);
        TextView rank8 = (TextView) findViewById(R.id.summRank8);
        rank8.setText(ranks[7]);
        TextView rank9 = (TextView) findViewById(R.id.summRank9);
        rank9.setText(ranks[8]);
        TextView rank10 = (TextView) findViewById(R.id.summRank10);
        rank10.setText(ranks[9]);
    }
// show The Image in a ImageView



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
