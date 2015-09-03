package com.dj0wns.pokemon.mobilepokemonbattle.Fragments;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.dj0wns.pokemon.mobilepokemonbattle.DataStructures.Move;
import com.dj0wns.pokemon.mobilepokemonbattle.DataStructures.Pokemon;
import com.dj0wns.pokemon.mobilepokemonbattle.NetworkTasks.FetchPokemonData;
import com.dj0wns.pokemon.mobilepokemonbattle.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

public class BattleFragment extends Fragment {

    private static String POKEDEX_URL = "http://pokemondb.net/pokedex/all";
    private static String MOVEDEX_URL = "http://pokemondb.net/move/all";
    private static int NATNL_DEX_CELL_NUMBER = 1;
    private static int POKEMON_NAME_CELL_NUMBER = 9;
    private static int NORMALIZED_STAT_TOTAL = 450;
    private static String POKEMON_LEVEL = "Lv50";

    private Vector<Move> movedex;


    private HashMap<Integer, Pokemon> pokemap;
    private PopulateNationalDex population;

    private ImageView opponentImage;
    private TextView opponentName, opponentLevel;
    private ImageView userImage;
    private TextView userName, userLevel;
    private Button moveSlot1, moveSlot2, moveSlot3, moveSlot4;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.battle_fragment, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        opponentImage = (ImageView) getView().findViewById(R.id
                .opponent_pokemon_image_view);
        opponentName = (TextView) getView().findViewById(R.id
                .opponent_pokemon_name);
        opponentLevel = (TextView) getView().findViewById(R.id
                .opponent_pokemon_level);
        userImage = (ImageView) getView().findViewById(R.id
                .user_pokemon_image_view);
        userName = (TextView) getView().findViewById(R.id.user_pokemon_name);
        userLevel = (TextView) getView().findViewById(R.id.user_pokemon_level);
        moveSlot1 = (Button) getView().findViewById(R.id.move_slot_1);
        moveSlot2 = (Button) getView().findViewById(R.id.move_slot_2);
        moveSlot3 = (Button) getView().findViewById(R.id.move_slot_3);
        moveSlot4 = (Button) getView().findViewById(R.id.move_slot_4);
        setFonts();
        population = new PopulateNationalDex();
        population.execute();
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

    private void setFonts() {
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(),
                "Pokemon_DPPt.ttf");
        opponentName.setTypeface(font);
        opponentLevel.setTypeface(font);
        userName.setTypeface(font);
        userLevel.setTypeface(font);
    }

    /**
     * Created by dj0wns on 8/28/15.
     * Intended to populate the national dex to allow for easy translation from
     * Pokemon number to name
     */
    public class PopulateNationalDex extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            Document doc = null;
            pokemap = new HashMap<>();
            int key, totalStat, attack, defense, speed, hp, specialDefense,
                    specialAttack;
            String name, type1, type2;
            Pokemon pokemon;

            try {
                doc = Jsoup.connect(POKEDEX_URL).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (Element table : doc.select("table#pokedex")) {
                for (Element row : table.select("tr")) {
                    Elements tds = row.select("td");
                    if (tds.size() > POKEMON_NAME_CELL_NUMBER) {
                        try {
                            key = Integer.parseInt(tds.get(0)
                                    .text());
                            Log.d("key", key + "");
                            name = tds.get(1).text();
                            Elements a = tds.get(2).select("a");
                            type1 = a.get(0).text();
                            if(a.size() > 1){
                                type2 = a.get(1).text();
                            } else {
                                type2 = null;
                            }
                            totalStat = Integer.parseInt(tds.get(3)
                                    .text());
                            hp = Integer.parseInt(tds.get(4)
                                    .text());
                            attack= Integer.parseInt(tds.get(5)
                                    .text());
                            defense = Integer.parseInt(tds.get(6)
                                    .text());
                            specialAttack = Integer.parseInt(tds.get(7)
                                    .text());
                            specialDefense = Integer.parseInt(tds.get(8)
                                    .text());
                            speed = Integer.parseInt(tds.get(9)
                                    .text());
                            pokemon = new Pokemon(name, type1, type2, totalStat,
                                    hp, attack, defense, specialAttack,
                                    specialDefense, speed);

                            pokemap.put(key, pokemon);
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
            FetchPokemonData fetch = new FetchPokemonData(opponentImage,
                    NORMALIZED_STAT_TOTAL);
            FetchPokemonData fetch2 = new FetchPokemonData(userImage, moveSlot1,
                    moveSlot2, moveSlot3, moveSlot4, NORMALIZED_STAT_TOTAL);
            String toFetch = pokemap.get((int) (Math.random() * pokemap.size())).getName();
            String toFetch2 = pokemap.get((int) (Math.random() * pokemap.size()))
                    .getName();

            //set names
            opponentName.setText(toFetch);
            userName.setText(toFetch2);
            opponentLevel.setText(POKEMON_LEVEL);
            userLevel.setText(POKEMON_LEVEL);

            Log.d("Pokemon", toFetch);
            fetch.execute(toFetch);
            fetch2.execute(toFetch2);
        }
    }

    /**
     * Created by dj0wns on 8/28/15.
     * Intended to populate the national dex to allow for easy translation from
     * Pokemon number to name
     */
    public class DownloadMoveData extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
//            Document doc = null;
//            pokemap = new HashMap<>();
//            int key;
//
//            try {
//                doc = Jsoup.connect(url).get();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            for (Element table : doc.select("table")) {
//                for (Element row : table.select("tr")) {
//                    Elements tds = row.select("td");
//                    if (tds.size() > POKEMON_NAME_CELL_NUMBER) {
//                        try {
//                            key = Integer.parseInt(tds.get(NATNL_DEX_CELL_NUMBER)
//                                    .text()
//                                    .substring(1)); //ignore the '#' char
//                            pokemap.put(key, tds.get(POKEMON_NAME_CELL_NUMBER)
//                                    .text());
//                        } catch (NumberFormatException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }

}
