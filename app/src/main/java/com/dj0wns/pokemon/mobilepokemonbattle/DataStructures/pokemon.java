package com.dj0wns.pokemon.mobilepokemonbattle.DataStructures;

/**
 * Created by dj0wns on 9/2/15.
 */
public class pokemon {
    private String name, type1, type2;
    private int total, hp, attack, defense, specialAttack, specialDefense, speed;

    public pokemon(String name, String type1, String type2, int total, int hp,
                   int attack, int defense, int specialAttack, int specialDefense, int speed) {
        this.name = name;
        this.type1 = type1;
        this.type2 = type2;
        this.total = total;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.specialAttack = specialAttack;
        this.specialDefense = specialDefense;
        this.speed = speed;
    }

    public String getName() {
        return name;
    }

    public String getType1() {
        return type1;
    }

    public String getType2() {
        return type2;
    }

    public int getTotal() {
        return total;
    }

    public int getHp() {
        return hp;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpecialAttack() {
        return specialAttack;
    }

    public int getSpecialDefense() {
        return specialDefense;
    }

    public int getSpeed() {
        return speed;
    }
}
