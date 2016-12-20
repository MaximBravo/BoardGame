package com.example.maximbravo.boardgame;

import android.app.Activity;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Maxim Bravo on 12/19/2016.
 */

public class Board {
    private Activity parentActivity;
    private int mNumberOfRows;
    private int mNumberOfColumns;
    private int mScreenWidth;
    private int mIdOfLastTouchedView;
    private ViewGroup parentViewGroup;
    private TextView moutput;
    private Cell[][] board;
    public Board(Activity activity, ViewGroup viewGroup, int numberOfRows, int numberOfColumns, int screenWidth, TextView output){
        parentActivity = activity;
        mNumberOfColumns = numberOfColumns;
        mNumberOfRows = numberOfRows;
        mScreenWidth = screenWidth;
        mIdOfLastTouchedView = 0;
        parentViewGroup = viewGroup;
        moutput = output;
        board = new Cell[numberOfRows][numberOfColumns];

    }

    private int[][] initialScheme;
    public void makeInitialScheme(int[][] scheme){
        initialScheme = scheme;
    }


    public void makeBoard(){
        LinearLayout boardLinear = new LinearLayout(parentActivity.getApplicationContext());
        boardLinear.setOrientation(LinearLayout.VERTICAL);
        int count = 1;
        int cellSide = mScreenWidth/(mNumberOfColumns+3);
        int margin = 0;
        for(int rows = 0; rows < mNumberOfRows; rows++) {
            LinearLayout l = new LinearLayout(parentActivity.getApplicationContext());
            l.setOrientation(LinearLayout.HORIZONTAL);
            for(int columns = 0; columns < mNumberOfColumns; columns++) {
                Cell currentCell = new Cell();
                TextView square = new TextView(parentActivity.getApplicationContext());
                square.setHeight(cellSide);
                square.setWidth(cellSide);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(cellSide, cellSide);
                params.setMargins(margin, margin, margin, margin);
                square.setLayoutParams(params);
                square.setId(count);
                currentCell.addId(count);
                //square.setBackground(parentActivity.getResources().getDrawable(initialScheme[rows][columns]));

                square.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if(motionEvent.getAction() != MotionEvent.ACTION_UP) {
                            mIdOfLastTouchedView = view.getId();
                            moutput.setText("" + mIdOfLastTouchedView);
                        } else {

                            moutput.setText("You are not hovering");
                        }
                        return false;
                    }
                });
                l.addView(square);
                board[columns][rows] = currentCell;
                count++;
            }

            //l.setLayoutParams(params);
            boardLinear.addView(l);

        }

        int rowWidth = (cellSide*mNumberOfColumns) + 2*(margin*mNumberOfColumns);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(rowWidth, rowWidth);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        boardLinear.setLayoutParams(params);
        boardLinear.setId(count);
        boardLinear.setBackgroundColor(Color.WHITE);
        parentViewGroup.addView(boardLinear);
    }

    private ArrayList<Integer> totalCombinations = new ArrayList<>();
    public void addCombinations(ArrayList<Integer> combinations){
        totalCombinations = combinations;
    }
    public void addForeGroundOptions(ArrayList<Integer> foreground){
        for(int rows = 0; rows < board.length; rows++){
            for(int columns = 0; columns < board[0].length; columns++){
                Cell currentCell = board[rows][columns];
                currentCell.addForegroundStateOptions(foreground);
                currentCell.addForegroundResource(foreground.get(foreground.size()-1));

                board[rows][columns] = currentCell;
            }
        }
    }
    public void addBackGroundOptions(ArrayList<Integer> background){
        for(int rows = 0; rows < board.length; rows++){
            for(int columns = 0; columns < board[0].length; columns++){
                Cell currentCell = board[rows][columns];
                currentCell.addBackgroundStateOptions(background);
                board[rows][columns] = currentCell;
            }
        }
    }
    public void addCheckerBoardTheme(){
        boolean firstColor = true;
        for(int rows = 0; rows < board.length; rows++){
            for(int columns = 0; columns < board[0].length; columns++){
                Cell currentCell = board[rows][columns];
                if(firstColor){
                    currentCell.addBackgroundResource(1);
                    if(columns != board[0].length-1){
                        firstColor = false;
                    }
                } else {
                    currentCell.addBackgroundResource(2);
                    if(columns != board[0].length-1){
                        firstColor = true;
                    }
                }
                TextView cellText = (TextView) parentActivity.findViewById(currentCell.getCellId());
                cellText.setBackgroundResource(currentCell.getResource(totalCombinations));
                board[rows][columns] = currentCell;
            }
        }

    }
    public int getIdOfLastTouchedView(){
        return mIdOfLastTouchedView;
    }


}
