package com.example.maximbravo.boardgame;

import android.app.ActionBar;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
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


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                if(history.size() > 1) {
                    if (redTurn) {
                        redTurn = false;
                        output.setText("Blacks Turn");
                        output.setTextColor(Color.BLACK);
                        fab.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
                        toolbar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                        mainLayout.setBackground(getResources().getDrawable(R.drawable.red));
                        Toast.makeText(MainActivity.this, "Blacks Turn", Toast.LENGTH_LONG);
                        history.clear();
                        lock = false;
                    } else {
                        redTurn = true;
                        output.setText("Reds Turn");
                        output.setTextColor(Color.RED);
                        fab.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        Toast.makeText(MainActivity.this, "Reds Turn", Toast.LENGTH_LONG);
                        toolbar.setBackgroundDrawable(new ColorDrawable(Color.RED));
                        mainLayout.setBackground(getResources().getDrawable(R.drawable.black));
                        history.clear();
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
//        if(redTurn){
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
        board = new Board(this, mainContent, BOARD_SIDE_LENGTH, BOARD_SIDE_LENGTH, smallestDimmension, output);
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
        lastMove = 0;
        history.clear();

    }
    public void flipCoin(){
        Random randomNum = new Random();
        int result = 0;
        result = randomNum.nextInt(2);
        if(result == 1){
            redTurn = true;
            output.setText("Reds Turn");
            output.setTextColor(Color.RED);
            fab.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            toolbar.setBackgroundDrawable(new ColorDrawable(Color.RED));
            mainLayout.setBackground(getResources().getDrawable(R.drawable.black));
        } else {
            redTurn = false;
            output.setText("Blacks Turn");
            output.setTextColor(Color.BLACK);
            fab.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
            toolbar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            mainLayout.setBackground(getResources().getDrawable(R.drawable.red));
        }

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        newComputeMove();

        return super.onTouchEvent(event);
    }

    private boolean redTurn = true;
    private int originalId = 1;
    private int lastId = 0;
    private ArrayList<Integer> history = new ArrayList<Integer>();

    public void newComputeMove(){
        int touchedId = board.getIdOfLastTouchedView();
        //something on the board has been touched
        if(touchedId != 0){
            Cell touchedCell = board.getCellAt(touchedId);
            //if it is right side than see if first move in turn
            if(history.size() == 0){
                //check if piece picked up is the right color
                if(((redTurn && touchedCell.getCellForegroundId() == 2) || (!redTurn && touchedCell.getCellForegroundId() == 1)) && !isRepeat(touchedId)) {
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
                    history.clear();
                }
                //verify if can move
                else if(verified){
                    move(getLastIdInHistory(), touchedId);
                    history.add(touchedId);
                }
            }
        }
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
    public void computeMove(){
        int id = board.getIdOfLastTouchedView();
        if(id != 0) {
            //output.setText("Last View touched: " + id);

            Cell currentCell = board.getCellAt(id);
            //output.setText("The Cell has a background resource of: " + currentCell.getBackgroundResource() + "\nAnd Foreground: " + currentCell.getCellForegroundId());


            if (history.size() >= 1) {

                //second move in series
                if (history.size() % 2 == 1) {
                    //second move in series is blank
                    if (currentCell.getCellForegroundId() == 3 && canSwap(history.get(history.size() - 1), id) != 0) {
                        history.add(id);
                        int currentForeground = currentCell.getCellForegroundId();
                        Cell previous = board.getCellAt(history.get(history.size() - 2));
                        int previousForeground = previous.getCellForegroundId();
                        currentCell.addForegroundResource(previousForeground);
                        previous.addForegroundResource(currentForeground);

                        board.updateCellAt(id);
                        board.updateCellAt(history.get(history.size() - 2));
                        if (id == originalId) {

                            lastId = id;
                            history.clear();
                        }
                    } else {
                        Toast.makeText(this, "Sorry you cannot move their.", Toast.LENGTH_SHORT);
                    }
                } else {
                    if (redTurn) {
                        if (currentCell.getCellForegroundId() == 2) {
                            if (lastId != id) {
                                history.add(id);
                                lastId = 0;
                            }
                        }
                    } else {
                        if (currentCell.getCellForegroundId() == 1) {
                            if (lastId != id) {
                                history.add(id);
                                lastId = 0;
                            }
                        }
                    }

                }

            } else {
                if (redTurn) {
                    if (currentCell.getCellForegroundId() == 2) {
                        if (lastId != id) {
                            history.add(id);
                            lastId = 0;
                            originalId = id;
                        }
                    }
                } else {
                    if (currentCell.getCellForegroundId() == 1) {
                        if (lastId != id) {
                            history.add(id);
                            lastId = 0;
                            originalId = id;
                        }
                    }
                }
            }
        }

    }

    private int lastMove = 0;
    public int canSwap(int prevId, int currentId){
        String xypos1 = board.getCellPositionAt(prevId);
        String[] pos1 = xypos1.split("-");
        int xpos1 = Integer.parseInt(pos1[0]);
        int ypos1 = Integer.parseInt(pos1[1]);
        String xypos2 = board.getCellPositionAt(currentId);
        String[] pos2 = xypos2.split("-");
        int xpos2 = Integer.parseInt(pos2[0]);
        int ypos2 = Integer.parseInt(pos2[1]);
        int betweenCell = getCellBetween(xpos1, ypos1, xpos2, ypos2);

        if(lastMove == 1){
            return 0;
        }
        lastMove = betweenCell;
        return betweenCell;
    }
    public int getCellBetween(int x1, int y1, int x2, int y2){
        if(x1 == x2){
            int difference = y1 - y2;
            if(Math.abs(difference) == 2){
                int newy = y1 + difference/2;
                if(board.getCellAt(x1, newy).getCellForegroundId() != 3) {
                    return 2;
                }
            } else if (Math.abs(difference) == 1){
                int newy = y1 + difference;
                if(board.getCellAt(x1, newy).getCellForegroundId() == 3) {
                    return 1;
                }
            }
        } else if(y1 == y2){
            int difference = x1 - x2;
            if(Math.abs(difference) == 2){
                int newx = x1 + difference/2;
                if(board.getCellAt(newx, y1).getCellForegroundId() != 3) {
                    return 2;
                }
            } else if(Math.abs(difference) == 1){
                int newx = x1 + difference;
                if(board.getCellAt(newx, y1).getCellForegroundId() == 3) {
                    return 1;
                }
            }
        } else {
            return 0;
        }
        return 0;
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

        return super.onOptionsItemSelected(item);
    }
}
