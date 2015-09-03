package com.dj0wns.pokemon.mobilepokemonbattle.NetworkTasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

/**
 * Created by dj0wns on 8/28/15.
 * Takes in a name and set the image view to that Pokemon
 */
public class FetchPokemonData extends AsyncTask<String, Void, Bitmap> {

    private static String BASE_POKEMON_URL = "http://bulbapedia.bulbagarden" +
            ".net/wiki/";
    private static String END_POKEMON_URL = "_(Pokémon)";

    private ImageView toChange;
    private TextView name;
    private Button slots[];
    private int statTotal;
    private Element moveList;


    public FetchPokemonData(ImageView view, int statTotal) {
        toChange = view;
        this.statTotal = statTotal;
    }

    public FetchPokemonData(ImageView view, Button slot1, Button slot2,
                            Button slot3, Button slot4, int statTotal) {
        toChange = view;
        slots = new Button[4];
        slots[0] = slot1;
        slots[1] = slot2;
        slots[2] = slot3;
        slots[3] = slot4;
        this.statTotal = statTotal;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String url = BASE_POKEMON_URL + params[0] + END_POKEMON_URL;
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
            //Get Moveset
            Elements moves = doc.select("table.sortable");
            outerloop:
            for(Element move: moves){
                Elements titles = move.select("th span");
                for(Element title : titles){

                    if(title.text().equals("Move")){
                        this.moveList = move;
                        break outerloop;
                    }
                }
            }

            //Get Icon
            Elements icon = doc.select("[alt=" + params[0] + "]");
            URL imageLocation = new URL(icon.attr("src"));
            Log.d("src", imageLocation.toString());
            return BitmapFactory.decodeStream(imageLocation.openConnection()
                    .getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        String moveName;
        if(slots != null ){
            if(moveList != null){
                Elements moves = moveList.select("td a[title*=\"(Move)\"] span");
                for(int i = 0; i < slots.length && i < moves.size(); i++){
                    moveName = moves.get(i).text().toLowerCase();
                    Log.d("moveName", moveName);
                    moveName = Character.toUpperCase(moveName.charAt(0)) + moveName
                            .substring(1);
                    Log.d("moveName", moveName);
                    slots[i].setText(moveName);
                }
            }
        }
        result = Bitmap.createScaledBitmap(result, 800, 800, true);
        toChange.setImageBitmap(result);
    }

}
