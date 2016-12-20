package com.example.maximbravo.boardgame;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ViewGroup mainContent;
    private TextView output;
    private Board board;
    private final int BOARD_SIDE_LENGTH = 8;

    private final int STATE_RED_ON_BLACK = R.drawable.redonblack;
    private final int STATE_RED_ON_RED = R.drawable.redonred;
    private final int STATE_RED = R.drawable.red;
    private final int STATE_BLACK_ON_BLACK = R.drawable.blackonblack;
    private final int STATE_BLACK_ON_RED = R.drawable.blackonred;
    private final int STATE_BLACK = R.drawable.black;

    private ArrayList<Integer> combinations = new ArrayList<Integer>();

    public void initializeCombinations(){
        combinations.add(STATE_RED_ON_BLACK);
        combinations.add(STATE_RED);
        combinations.add(STATE_RED_ON_RED);
        combinations.add(STATE_BLACK);
        combinations.add(STATE_BLACK_ON_RED);
        combinations.add(STATE_BLACK_ON_BLACK);

        foregroundOptions.add(1);
        foregroundOptions.add(2);

        backgroundOptions.add(1);
        backgroundOptions.add(2);
        backgroundOptions.add(3);


    }

    private ArrayList<Integer> foregroundOptions = new ArrayList<>();
    private ArrayList<Integer> backgroundOptions = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mainContent = (ViewGroup) findViewById(R.id.content_main);
        output = (TextView) findViewById(R.id.output);
        Display display = getWindowManager().getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        int smallestDimmension = Math.min(screenHeight, screenWidth);
        board = new Board(this, mainContent, BOARD_SIDE_LENGTH, BOARD_SIDE_LENGTH, smallestDimmension, output);
        initializeCombinations();
        board.makeBoard();
        board.addCombinations(combinations);
        board.addBackGroundOptions(backgroundOptions);
        board.addForeGroundOptions(foregroundOptions);
        board.addCheckerBoardTheme();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        computeMove();

        return super.onTouchEvent(event);
    }


    private ArrayList<Integer> history = new ArrayList<Integer>();
    public void computeMove(){
        int id = board.getIdOfLastTouchedView();
        output.setText("Last View touched: " + id);
        history.add(id);
        TextView current;
        if(history.size() > 1) {
            if (id != 0) {
                current = (TextView) findViewById(id);
                Drawable currentBackground = current.getBackground();
                TextView previous = (TextView) findViewById(history.get(history.size()-2));

                current.setBackground(previous.getBackground());
                previous.setBackground(currentBackground);
            }
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
