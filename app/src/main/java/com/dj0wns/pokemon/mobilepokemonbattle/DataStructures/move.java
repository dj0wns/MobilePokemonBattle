package com.dj0wns.pokemon.mobilepokemonbattle.DataStructures;

/**
 * Created by dj0wns on 9/2/15.
 * Contains complete data on a move
 */
public class move {
    private String name, type, category, effect;
    private int power, accuracy, powerPoints, probability;

    public move(String name, String type, String category, String effect,
                int power, int accuracy, int powerPoints, int probability) {
        this.name = name;
        this.type = type;
        this.category = category;
        this.effect = effect;
        this.power = power;
        this.powerPoints = powerPoints;
        this.accuracy = accuracy;
        this.probability = probability;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public int getPower() {
        return power;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public String getEffect() {
        return effect;
    }

    public int getProbability() {
        return probability;
    }

    public int getPowerPoints() {
        return powerPoints;
    }
}
