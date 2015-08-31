package com.dj0wns.pokemon.mobilepokemonbattle.Fragments;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


import com.dj0wns.pokemon.mobilepokemonbattle.NetworkTasks.FetchPokemonData;
import com.dj0wns.pokemon.mobilepokemonbattle.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

public class BattleFragment extends AppCompatActivity {

    private static String url = "http://bulbapedia.bulbagarden" +
            ".net/wiki/List_of_Pok%C3%A9mon_by_National_Pok%C3%A9dex_number";
    private static int NATNL_DEX_CELL_NUMBER = 1;
    private static int POKEMON_NAME_CELL_NUMBER = 3;
    private static int OPPONENT_POKEMON_NUMBER = 295;
    private static int USER_POKEMON_NUMBER = 125;
    private static String POKEMON_LEVEL = "Lv50";


    private HashMap<Integer, String> pokemap;
    private PopulateNationalDex population;

    private ImageView opponentImage;
    private TextView opponentName;
    private TextView opponentLevel;
    private ImageView userImage;
    private TextView userName;
    private TextView userLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        opponentImage = (ImageView) findViewById(R.id.opponent_pokemon_image_view);
        opponentName = (TextView) findViewById(R.id.opponent_pokemon_name);
        opponentLevel = (TextView) findViewById(R.id.opponent_pokemon_level);
        userImage = (ImageView) findViewById(R.id.user_pokemon_image_view);
        userName = (TextView) findViewById(R.id.user_pokemon_name);
        userLevel = (TextView) findViewById(R.id.user_pokemon_level);
        setFonts();
        population = new PopulateNationalDex();
        population.execute();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pokemon, menu);
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

    private void setFonts(){
        Typeface font = Typeface.createFromAsset(getAssets(), "Pokemon_DPPt.ttf");
        opponentName.setTypeface(font);
        opponentLevel.setTypeface(font);
        userName.setTypeface(font);
        userLevel.setTypeface(font);
    }

    /**
     * Created by dj0wns on 8/28/15.
     * Intended to populate the national dex to allow for easy translation from
     * pokemon number to name
     */
    public class PopulateNationalDex extends AsyncTask<Void, Void, Void> {



        @Override
        protected Void doInBackground(Void... params) {
            Document doc = null;
            pokemap = new HashMap<>();
            int key;

            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (Element table : doc.select("table")) {
                for (Element row : table.select("tr")) {
                    Elements tds = row.select("td");
                    if (tds.size() > POKEMON_NAME_CELL_NUMBER) {
                        try {
                            key = Integer.parseInt(tds.get(NATNL_DEX_CELL_NUMBER)
                                    .text()
                                    .substring(1)); //ignore the '#' char
                            pokemap.put(key, tds.get(POKEMON_NAME_CELL_NUMBER)
                                    .text());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            FetchPokemonData fetch = new FetchPokemonData(opponentImage, 50);
            FetchPokemonData fetch2 = new FetchPokemonData(userImage, 50);
            String toFetch = pokemap.get((int) (Math.random() * pokemap.size()));
            String toFetch2 = pokemap.get((int) (Math.random() * pokemap.size()));

            //set names
            opponentName.setText(toFetch);
            userName.setText(toFetch2);
            opponentLevel.setText(POKEMON_LEVEL);
            userLevel.setText(POKEMON_LEVEL);

            Log.d("pokemon", toFetch);
            fetch.execute(toFetch);
            fetch2.execute(toFetch2);
        }
    }

}
