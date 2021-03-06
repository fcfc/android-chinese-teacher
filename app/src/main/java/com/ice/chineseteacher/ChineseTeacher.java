package com.ice.chineseteacher;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class ChineseTeacher extends Activity implements View.OnClickListener {
    private static final int MENU_PAUSE = 1;
    private static final int MENU_RESUME = 2;
    private static final int MENU_FLASHCARD = 3;
    private static final int MENU_SOUNDOFF = 4;
    private static final int MENU_SOUNDON = 5;
    private static final int MENU_START = 6;
    private static final int MENU_RANDOM = 7;

    /** A handle to the View */
    private ChineseView mChineseView;
    private TextView mTextView;
    private TextView mTextView2;


    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
    private Handler mHandler = new Handler();
    private long mStartTime;
    private int framenum = 1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(0, MENU_START, 0, R.string.menu_start);
//        menu.add(0, MENU_RANDOM, 0, R.string.menu_random);
        menu.add(0, MENU_FLASHCARD, 0, R.string.menu_flashcard);
//        menu.add(0, MENU_SOUNDOFF, 0, R.string.menu_soundoff);
//        menu.add(0, MENU_SOUNDON, 0, R.string.menu_soundon);
        menu.add(0, MENU_PAUSE, 0, R.string.menu_easy);
        menu.add(0, MENU_RESUME, 0, R.string.menu_hard);

        return true;
    }

    /*** Invoked when the user selects an item from the Menu.     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case MENU_START:
        	mTextView.setText(R.string.on);
            startActivity(new Intent(this, Preferences.class));
            return true;
        case MENU_RANDOM:
            mChineseView.setFlashcard(false);
        	mTextView.setText(R.string.randommode);
            return true;
        case MENU_FLASHCARD:
            mChineseView.setFlashcard(true);
        	mTextView.setText(R.string.flashcardmode);
            return true;
        case MENU_SOUNDOFF:
            mChineseView.setSound("off");
            return true;
        case MENU_SOUNDON:
          mChineseView.setSound("on");
           return true;

        }

        return false;
    }


    public TextView getTextView()
    {
    	return mTextView;
    }

    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        mChineseView =  (ChineseView)findViewById(R.id.chineseview);        // get handles to the ChineseView from XML,

        mChineseView.setOnClickListener(ChineseTeacher.this); 
        mChineseView.setOnTouchListener(gestureListener);
        mTextView = (TextView)findViewById(R.id.frame2);
        mTextView2 = (TextView)findViewById(R.id.frame3);

        Button mainLeft = (Button) findViewById(R.id.left);
        mainLeft.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	mChineseView.setCharacter(ChineseView.PREVIOUS);
            	framenum = mChineseView.getFramenum();
            	mTextView.setText(String.valueOf(framenum));
                mTextView2.setText(mChineseView.getDefinition());


            }
        });
        Button mainRight = (Button) findViewById(R.id.right);
        mainRight.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	mChineseView.setCharacter(ChineseView.NEXT);
                framenum = mChineseView.getFramenum();
            	mTextView.setText(String.valueOf(framenum));
                mTextView2.setText(mChineseView.getDefinition());


            }
        });

        // Gesture detection
        gestureDetector = new GestureDetector(this, new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    return true;
                }
                return false;
            }
        };

        mChineseView.setOnTouchListener(gestureListener);

    }
    
    class MyGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        	mTextView.setText("GESTURE");
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                	mChineseView.setCharacter(ChineseView.PREVIOUS);
                	mTextView.setText("GESTURE R TO L");

                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                	mChineseView.setCharacter(ChineseView.NEXT);
                	mTextView.setText("GESTURE L TO R");

                }
                // up down swipe
                if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                	mChineseView.setCharacter(ChineseView.PREVIOUS);
                	mTextView.setText("GESTURE D TO U");

                }  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                	mChineseView.setCharacter(ChineseView.NEXT);
                	mTextView.setText("GESTURE U TO D");

                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }
    }

    public boolean onDown(MotionEvent e)
    {
    	mTextView.setText(R.string.down);
        return true;
    }

    /** * Invoked when the Activity loses user focus. */
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
   
    /* Button Handler */
    public void onClick(View v) {
    	mChineseView.setCharacter(ChineseView.NEXT);
    }

    // Timer setup
    OnClickListener mStartListener = new OnClickListener() {
    	   public void onClick(View v) {
    	            mStartTime = System.currentTimeMillis();
    	 //           mHandler.removeCallbacks(mUpdateTimeTask);
    	            mHandler.postDelayed(mUpdateTimeTask, 100);
                	mTextView.setText("LISTENER START");

    	   }
    	};
    private Runnable mUpdateTimeTask = new Runnable() {
    	   public void run() {
    	       final long start = mStartTime;
    	       long millis = SystemClock.uptimeMillis() - start;
    	       int seconds = (int) (millis / 1000);
    	       int minutes = seconds / 60;
    	       seconds     = seconds % 60;
           	mTextView.setText("UPDATE TIME " + millis);

    	       
    	       mHandler.postAtTime(this,
    	               start + (((minutes * 60) + seconds + 1) * 1000));
    	       if (seconds % 5 == 0)  {
//    	           mChineseThread.setCharacter(ChineseThread.NEXT);

    	       }
    	   }
   };
}



