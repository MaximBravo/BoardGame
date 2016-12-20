package com.example.maximbravo.boardgame;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

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
    public Board(Activity activity, ViewGroup viewGroup, int numberOfRows, int numberOfColumns, int screenWidth, TextView output){
        parentActivity = activity;
        mNumberOfColumns = numberOfColumns;
        mNumberOfRows = numberOfRows;
        mScreenWidth = screenWidth;
        mIdOfLastTouchedView = 0;
        parentViewGroup = viewGroup;
        moutput = output;
        makeBoard();
    }

    private final int STATE_RED = R.drawable.red;
    private final int STATE_BLACK = R.drawable.black;
    private final int STATE_BLACK_ON_BLACK = R.drawable.blackonblack;
    private final int STATE_RED_ON_RED = R.drawable.redonred;
    private final int STATE_BLACK_ON_RED = R.drawable.blackonred;
    private final int STATE_RED_ON_BLACK = R.drawable.redonblack;
    int[][] initialScheme = {
            {STATE_BLACK_ON_RED, STATE_BLACK_ON_BLACK, STATE_BLACK_ON_RED, STATE_BLACK_ON_BLACK, STATE_RED, STATE_BLACK, STATE_RED, STATE_BLACK},
            {STATE_BLACK_ON_BLACK, STATE_BLACK_ON_RED, STATE_BLACK_ON_BLACK, STATE_BLACK_ON_RED, STATE_BLACK, STATE_RED, STATE_BLACK, STATE_RED},
            {STATE_BLACK_ON_RED, STATE_BLACK_ON_BLACK, STATE_BLACK_ON_RED, STATE_BLACK_ON_BLACK, STATE_RED, STATE_BLACK, STATE_RED, STATE_BLACK},
            {STATE_BLACK_ON_BLACK, STATE_BLACK_ON_RED, STATE_BLACK_ON_BLACK, STATE_BLACK_ON_RED, STATE_BLACK, STATE_RED, STATE_BLACK, STATE_RED},
            {STATE_RED, STATE_BLACK, STATE_RED, STATE_BLACK, STATE_RED_ON_RED, STATE_RED_ON_BLACK, STATE_RED_ON_RED, STATE_RED_ON_BLACK},
            {STATE_BLACK, STATE_RED, STATE_BLACK, STATE_RED, STATE_RED_ON_BLACK, STATE_RED_ON_RED, STATE_RED_ON_BLACK, STATE_RED_ON_RED},
            {STATE_RED, STATE_BLACK, STATE_RED, STATE_BLACK, STATE_RED_ON_RED, STATE_RED_ON_BLACK, STATE_RED_ON_RED, STATE_RED_ON_BLACK},
            {STATE_BLACK, STATE_RED, STATE_BLACK, STATE_RED, STATE_RED_ON_BLACK, STATE_RED_ON_RED, STATE_RED_ON_BLACK, STATE_RED_ON_RED}
    };
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
                TextView right = new TextView(parentActivity.getApplicationContext());
                right.setHeight(cellSide);
                right.setWidth(cellSide);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(cellSide, cellSide);
                params.setMargins(margin, margin, margin, margin);
                right.setLayoutParams(params);
                right.setId(count);
                right.setBackground(parentActivity.getResources().getDrawable(initialScheme[rows][columns]));

                right.setOnTouchListener(new View.OnTouchListener() {
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
                l.addView(right);

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

    public int getIdOfLastTouchedView(){
        return mIdOfLastTouchedView;
    }
}
