package com.dk.boosted;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;


public class IGN extends AppCompatActivity {
    ProgressDialog pd;
    public enum States {
        findingSummoner, findingPatch, findingPlayers, findingFull,
        findingRank
    }
    States state;
    String patch;
    String[] championIDs = new String[10];
    String[] champFulls = new String[10];
    String[] summNames = new String[10];
    String[] summFulls = new String[10];
    String[] summIDs = new String[10];
    String[] ranks = new String[10];
    String summonerNameForUrl;
    JSONArray participantsInfo;
    String id;
    int champCounter = 0;
    Intent intent;
    int rankIndex;

//    SQLiteDatabase db = openOrCreateDatabase("Summoner Information", MODE_PRIVATE, null);



    //Finds JSON data from URL
    //Credit: http://stackoverflow.com/questions/33229869/get-json-data-from-url-using-android
    private class JsonTask extends AsyncTask<String, String, String> {

//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            pd = new ProgressDialog(IGN.this);
//            pd.setMessage("Please wait");
//            pd.setCancelable(false);
//            pd.show();
//        }

        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                }
                //might not need
//                if (state == States.findingFull) {
//                    JSONObject response= new JSONObject(buffer.toString());
//                    JSONObject image = response.getJSONObject("image");
//                    String full  = image.getString("full");
//                    Log.d("FULL", full);
////                    champCounter++;
//                }
                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            catch (JSONException e) {
//                e.printStackTrace();
//            }
            finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            if (pd.isShowing()){
//                pd.dismiss();
//            }
            try {
                //find the current patch
                if (state == States.findingPatch) {
                    intent = new Intent(IGN.this, DisplaySummoners.class);
                    JSONArray versions = new JSONArray(result);
                    patch = versions.get(0).toString();
                    intent.putExtra("patch", patch);
                    Log.d("PATCH", patch);
                //find the data for the summoner entered
                } else if (state == States.findingSummoner) {
                    JSONObject response = new JSONObject(result);
                    Log.d("LOL", response.toString());
                    EditText summonerSearchEdit = (EditText) findViewById(R.id.summonerSearchEdit);
                    summonerNameForUrl = summonerNameForUrl.replace("%20", "");
                    //get summoner id to perform next JSON task
                    id = response.getJSONObject(summonerNameForUrl).getString("id");
                    state = States.findingPlayers;
                    //find summoner current game data
                    new JsonTask().execute("https://na.api.pvp.net/observer-mode/rest/consumer/getSpectatorGameInfo/NA1/" +
                            id + "?api_key=RGAPI-4BA2AC26-F249-4BDC-AE5E-7BF6042EB508");
                //finds current game data to get each players data
                } else if (state == States.findingPlayers) {
                    JSONObject response = new JSONObject(result);
                    Log.d("FINDINGPLAYERS", response.toString());
                    participantsInfo = response.getJSONArray("participants");
                    for (int i = 0; i < participantsInfo.length(); i++) {
                        JSONObject participantInfo = participantsInfo.getJSONObject(i);
                        Log.d("PARTICIPANTS INFO", participantInfo.toString());
                        championIDs[i] = participantInfo.getString("championId");
                        summNames[i] = participantInfo.getString("summonerName");
                        summIDs[i] = participantInfo.getString("summonerId");
                        Log.d("CHAMPION ID", championIDs[i]);
                        Log.d("SUMMONER NAME", summNames[i]);
                    }
                    state = States.findingFull;
//                    find first champion image
                    new JsonTask().execute("https://global.api.pvp.net/api/lol/static-data/na/v1.2/champion/" +
                            championIDs[champCounter] + "?champData=image&api_key=RGAPI-4BA2AC26-F249-4BDC-AE5E-7BF6042EB508");
//                    champCounter++;
                } else if (state == States.findingFull) {
                    JSONObject response= new JSONObject(result);
                    JSONObject image = response.getJSONObject("image");
                    String full  = image.getString("full");
                    Log.d("FULL1", full + ": " + champCounter);

                    champFulls[champCounter] = full;
                    summFulls[champCounter] = summNames[champCounter];
                    champCounter++;
                    if (champCounter < 10) {
                        //find images for rest of the champions
                        new JsonTask().execute("https://global.api.pvp.net/api/lol/static-data/na/v1.2/champion/" +
                                championIDs[champCounter] + "?champData=image&api_key=RGAPI-4BA2AC26-F249-4BDC-AE5E-7BF6042EB508");
                        //champCounter++;
                    } else {
                        champCounter = 0;
                        //store the champions and summoners
                        intent.putExtra("champFulls", champFulls);
                        intent.putExtra("summFulls", summFulls);
                        //find the summoners' ranks
                        state = States.findingRank;
                        Log.d("TEST FOR sumIDS", Arrays.toString(summIDs));
                        new JsonTask().execute("https://global.api.pvp.net/api/lol/na/v2.5/league/by-summoner/" +
                                summIDs[0] + "," + summIDs[1] + "," + summIDs[2] + "," + summIDs[3] + "," + summIDs[4] + "," +
                                summIDs[5] + "," + summIDs[6] + "," + summIDs[7] + "," + summIDs[8] + "," + summIDs[9]
                                + "/entry?api_key=RGAPI-4BA2AC26-F249-4BDC-AE5E-7BF6042EB508");
                    }
                } else if (state == States.findingRank) {
                    JSONObject response = new JSONObject(result);
                    Log.d("Finding Rank", response.toString());
                    Log.d("SummonerIDs", Arrays.toString(summIDs));
                    for (int i = 0; i < 10; i++) {
                        //TODO PLACE RANK IN TEXTBOX

                        rankIndex = i;
                        if (response.has(summIDs[i])) {
                            ranks[i] = response.getJSONArray(summIDs[i]).getJSONObject(0).getString("tier") + " "
                                    + response.getJSONArray(summIDs[i]).getJSONObject(0).getJSONArray("entries").getJSONObject(0).getString("division");
                        } else {
                            ranks[i] = "UNRANKED";
                        }
                        Log.d("ranks", ranks[i]);
                    }
                    intent.putExtra("ranks", ranks);
                    pd.dismiss();
                    startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                new AlertDialog.Builder(IGN.this)
                        .setTitle("There was an error.")
                        .setMessage("Please try again.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Do nothing
                                startActivity(new Intent(IGN.this, IGN.class));
                            }
                        })
                        .show();
            } catch (NullPointerException e) {
                e.printStackTrace();
                //Alert Dialog for when invalid summoner name is entered
                if (state == States.findingSummoner) {
                    pd.dismiss();
                    new AlertDialog.Builder(IGN.this)
                            .setTitle("Summoner Name Not Found")
                            .setMessage("Please enter a valid summoner who is currently in a game.")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Do nothing
                                }
                            })
                            .show();
                //Alert Dialog for when summoner is not in a game
                } else if (state == States.findingPlayers) {
                    pd.dismiss();
                    state = States.findingSummoner;
                    new AlertDialog.Builder(IGN.this)
                            .setTitle("Summoner Game Not Found")
                            .setMessage("Please enter a valid summoner who is currently in a game.")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Do nothing
                                }
                            })
                            .show();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ign);
        state = States.findingPatch;
        //search box for finding a summoner
        final EditText summonerSearchEdit = (EditText) findViewById(R.id.summonerSearchEdit);
//        db.execSQL("CREATE TABLE IF NOT EXISTS Summoner_Name_Count(SummonerName VARCHAR, Count INTEGER);");

        //find current patch
        new JsonTask().execute("https://global.api.pvp.net/api/lol/static-data/na/v1.2/versions?api_key=RGAPI-4BA2AC26-F249-4BDC-AE5E-7BF6042EB508");
        //Action for when summoner name is entered
        summonerSearchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    state = States.findingSummoner;
//                    Cursor cursor = db.rawQuery("SELECT SummonerName, Count FROM Summoner_Name_Count " +
//                            "WHERE SummonerName = ?", new String[] {"summonerSearchEdit.getText().toString()"});
//                    if (cursor.getCount() == 0) {
//                        Log.d("NONE", "NONE");
//                    } else {
//                        Log.d("RESULTS", cursor.getString(0) + cursor.getString(1));
//
//                    }
                    pd = new ProgressDialog(IGN.this);
                    pd.setMessage("Please wait");
                    pd.setCancelable(false);
                    pd.show();
                    summonerNameForUrl = summonerSearchEdit.getText().toString().toLowerCase().replace(" ", "%20");
                    new JsonTask().execute("https://na.api.pvp.net/api/lol/na/v1.4/summoner/by-name/" +
                        summonerNameForUrl + "?api_key=RGAPI-4BA2AC26-F249-4BDC-AE5E-7BF6042EB508");
//                    intent.putExtra("SummonerName", summonerSearchEdit.getText());
                    return true;
                }
                return false;
            }
        });
    }

}
