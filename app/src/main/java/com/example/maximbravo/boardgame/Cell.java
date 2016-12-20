package com.example.maximbravo.boardgame;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Maxim Bravo on 12/20/2016.
 */

public class Cell {
    private ArrayList<Integer> foregroundStateOptions = new ArrayList<>();

    private ArrayList<Integer> backgroundStateOptions = new ArrayList<>();
    private int cellId;
    private int cellForegroundId;
    private int cellBackgroundId;
    public Cell(){

    }
    public void addBackgroundStateOptions(ArrayList<Integer> stateOptions){
        backgroundStateOptions = stateOptions;
    }
    public void addForegroundStateOptions(ArrayList<Integer> stateOptions){
        foregroundStateOptions = stateOptions;
    }
    public void addId(int id){
        cellId = id;
    }
    public int getCellId(){
        return cellId;
    }
    public void addBackgroundResource(int stateOption){
        cellBackgroundId = stateOption;
    }
    public void addForegroundResource(int stateOption){
        cellForegroundId = stateOption;
    }
    ArrayList<Integer> combinationsInOrder = new ArrayList<>();

    //combines background and foreground views
    public int getResource(ArrayList<Integer> resIds){
        for(int i = 0; i < backgroundStateOptions.size(); i++){
            int background = backgroundStateOptions.get(i) * 10;
            for(int j = 0; j < foregroundStateOptions.size(); j++){
                int foreground = foregroundStateOptions.get(j);
                combinationsInOrder.add(background+foreground);
            }
        }

        int cellcombination = (cellBackgroundId*10) + cellForegroundId;
        for(int i = 0; i < combinationsInOrder.size(); i++){
            if(combinationsInOrder.get(i) == cellcombination){
                return resIds.get(i);
            }
        }
        return resIds.get(0);

    }
}
