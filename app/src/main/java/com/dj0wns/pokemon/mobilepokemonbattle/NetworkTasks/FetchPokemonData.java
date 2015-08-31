package com.dj0wns.pokemon.mobilepokemonbattle.NetworkTasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

/**
 * Created by dj0wns on 8/28/15.
 * Takes in a name and set the image view to that pokemon
 */
public class FetchPokemonData extends AsyncTask<String, Void, Bitmap> {

    private static String BASE_POKEMON_URL = "http://bulbapedia.bulbagarden" +
            ".net/wiki/";
    private static String END_POKEMON_URL = "_(Pokémon)";

    private ImageView toChange;
    private TextView name;
    private int level;


    public FetchPokemonData(ImageView view, int level ) {
        toChange = view;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String url = BASE_POKEMON_URL + params[0] + END_POKEMON_URL;
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
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
        result = Bitmap.createScaledBitmap(result, 800, 800, true);
        toChange.setImageBitmap(result);
    }

}
