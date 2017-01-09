package com.example.maximbravo.boardgame;

import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

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
        combinations.add(STATE_BLACK_ON_RED);

        combinations.add(STATE_RED_ON_RED);
        combinations.add(STATE_RED);
        combinations.add(STATE_BLACK_ON_BLACK);
        combinations.add(STATE_RED_ON_BLACK);
        combinations.add(STATE_BLACK);


        backgroundOptions.add(1);
        backgroundOptions.add(2);

        foregroundOptions.add(1);
        foregroundOptions.add(2);
        foregroundOptions.add(3);


    }
    private boolean lock = false;
    private  FloatingActionButton fab;
    private Toolbar toolbar;
    private ArrayList<Integer> foregroundOptions = new ArrayList<>();
    private ArrayList<Integer> backgroundOptions = new ArrayList<>();
    private RelativeLayout mainLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                if(history.size() > 1) {
                    if (whiteTurn) {
                        whiteTurn = false;
                        output.setText("Blacks Turn");
                        output.setTextColor(Color.BLACK);
                        fab.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));

                        mainLayout.setBackgroundColor(Color.WHITE);
                        Toast.makeText(MainActivity.this, "Blacks Turn", Toast.LENGTH_LONG);
                        history.clear();
                        previousMove = 0;
                        originalId = 0;
                        lock = false;
                    } else {
                        whiteTurn = true;
                        output.setText("Whites Turn");
                        output.setTextColor(Color.WHITE);
                        fab.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                        Toast.makeText(MainActivity.this, "Reds Turn", Toast.LENGTH_LONG);

                        mainLayout.setBackgroundColor(Color.BLACK);
                        history.clear();
                        previousMove = 0;
                        originalId = 0;
                        lock = false;
                    }
                } else {
                    Toast.makeText(MainActivity.this, "You havent made a move yet.", Toast.LENGTH_SHORT).show();

                }
            }
        });
        mainContent = (ViewGroup) findViewById(R.id.content_main);
        mainLayout = (RelativeLayout) findViewById(R.id.content_main);
        output = (TextView) findViewById(R.id.output);
//        flipCoin();
//        if(whiteTurn){
//            fab.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
//            mainContent.setBackgroundResource(R.drawable.black);
//        } else {
//            fab.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
//            mainContent.setBackgroundResource(R.drawable.red);
//        }


        Display display = getWindowManager().getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        int smallestDimmension = Math.min(screenHeight, screenWidth);
        int divider = 3;
        if(smallestDimmension == screenWidth){
            divider = 1;
        }
        board = new Board(this, mainContent, BOARD_SIDE_LENGTH, BOARD_SIDE_LENGTH, smallestDimmension, output, divider);
        initializeCombinations();
        board.makeBoard();
        startGame();

    }

    public void startGame(){
        flipCoin();

        board.addCombinations(combinations);
        board.addBackGroundOptions(backgroundOptions);
        board.addForeGroundOptions(foregroundOptions);
        board.addCheckerBoardTheme();

        history.clear();
        if(whiteTurn){
            toolbar.setBackgroundColor(Color.WHITE);
            toolbar.setTitleTextColor(Color.BLACK);
        } else {
            toolbar.setBackgroundColor(Color.BLACK);
            toolbar.setTitleTextColor(Color.WHITE);
        }
    }
    public void flipCoin(){
        Random randomNum = new Random();
        int result = 0;
        //result = randomNum.nextInt(2);
        if(result == 1){
            whiteTurn = true;
            output.setText("White Turn");
            output.setTextColor(Color.WHITE);
            fab.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));

            mainLayout.setBackgroundColor(Color.BLACK);
        } else {
            whiteTurn = false;
            output.setText("Blacks Turn");
            output.setTextColor(Color.BLACK);
            fab.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));

            mainLayout.setBackgroundColor(Color.WHITE);
        }

    }
    private boolean hoverMode = false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(!hoverMode) {
            playCheckersGame();
        }
        return super.onTouchEvent(event);
    }


    private boolean whiteTurn = true;
    private int originalId = 1;
    private int lastId = 0;
    private ArrayList<Integer> history = new ArrayList<Integer>();

    private int previousMove = 0;
    public void playCheckersGame(){
        int touchedId = board.getIdOfLastTouchedView();
        //something on the board has been touched
        if(touchedId != 0){
            Cell touchedCell = board.getCellAt(touchedId);
            //if it is right side than see if first move in turn
            if(history.size() == 0){
                //check if piece picked up is the right color
                if(((whiteTurn && touchedCell.getCellForegroundId() == 2) || (!whiteTurn && touchedCell.getCellForegroundId() == 1)) && !isRepeat(touchedId)) {
                    if(board.getIdOfLastTouchedView() != originalId) {
                        originalId = touchedId;
                        history.add(originalId);
                    }
                }
            } else {
                boolean verified = verify(touchedId);

                //check for place back
                if(!isRepeat(touchedId) && touchedId == originalId){
                    move(getLastIdInHistory(), touchedId);
//                    originalId = 0;
                    previousMove = 0;
                    history.clear();
                }
                //verify if can move
                else if(verified){
                    if(previousMove != 1){
                        int possibleMove = getMove(getLastIdInHistory(), touchedId);
                        if(possibleMove == 2) {
                            move(getLastIdInHistory(), touchedId);
                            history.add(touchedId);
                            previousMove = 2;
                        }
                        if (possibleMove == 1){
                            if(previousMove == 0) {
                                move(getLastIdInHistory(), touchedId);
                                history.add(touchedId);
                                previousMove = 1;
                            }
                        }
                    }
                }
            }
        }
    }
    public int getMove(int prevId, int currentId){
        String xypos1 = board.getCellPositionAt(prevId);
        String[] pos1 = xypos1.split("-");
        int x1 = Integer.parseInt(pos1[0]);
        int y1 = Integer.parseInt(pos1[1]);
        String xypos2 = board.getCellPositionAt(currentId);
        String[] pos2 = xypos2.split("-");
        int x2 = Integer.parseInt(pos2[0]);
        int y2 = Integer.parseInt(pos2[1]);

        if(x1 == x2){
            int difference = y1 - y2;
            if(Math.abs(difference) == 2){
                int newy = Math.min(y1, y2) + 1;
                if(board.getCellAt(x1, newy).getCellForegroundId() != 3) {
                    return 2;
                }
            } else if (Math.abs(difference) == 1){
                return 1;
            }
        } else if(y1 == y2){
            int difference = x1 - x2;
            if(Math.abs(difference) == 2){
                int newx = Math.min(x1, x2) + 1;
                if(board.getCellAt(newx, y1).getCellForegroundId() != 3) {
                    return 2;
                }
            } else if(Math.abs(difference) == 1){
                return 1;
            }
        } else {
            return 0;
        }
        return 0;

    }
    public void move(int firstid, int secondId){
        Cell firstCell = board.getCellAt(firstid);
        int firstCellforegroundId = firstCell.getCellForegroundId();
        Cell secondCell = board.getCellAt(secondId);
        int secondCellForegroundId = secondCell.getCellForegroundId();

        firstCell.addForegroundResource(secondCellForegroundId);
        secondCell.addForegroundResource(firstCellforegroundId);

        board.setCellAtBlankTo(firstid, firstCell);
        board.setCellAtBlankTo(secondId, secondCell);
    }
    public boolean isRepeat(int id){
        if(id == getLastIdInHistory()){
            return true;
        }
        return false;
    }
    public boolean verify(int touchedId){
        if(isRepeat(touchedId)){
            return false;
        }
        //we know there is a different click
        Cell touchedCell = board.getCellAt(touchedId);
        if(touchedCell.getCellForegroundId() != 3){
            if(((whiteTurn && touchedCell.getCellForegroundId() == 2) || (!whiteTurn && touchedCell.getCellForegroundId() == 1))) {
                if(board.getIdOfLastTouchedView() != originalId && history.size() == 1) {
                    originalId = touchedId;
                    history.clear();
                    history.add(originalId);
                }
            }
            return false;
        }
        //we know the touch is blank
        return true;

    }

    public int getLastIdInHistory(){
        if(history.size() == 0){
            return 0;
        } else {
            return history.get(history.size()-1);
        }
    }
    public void updateMode(){
        ActionMenuItemView hoverButton = (ActionMenuItemView) findViewById(R.id.hover_button);
        if(hoverMode){
            hoverMode = false;
            hoverButton.setTitle("Play Mode");
        } else {
            hoverMode = true;
            hoverButton.setTitle("Hover Mode");
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
        if (id == R.id.action_restart) {
            startGame();
            return true;
        }
        if(id == R.id.hover_button){
            updateMode();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
