package com.ice.chineseteacher;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class ChineseView extends View {

    private TextView mTextView;

    public static final int NEXT = 1;
    public static final int PREVIOUS = 2;
	public static final int NUMCHARS = 170;

	public  int TOTALCHARS = 100;
    public static final boolean TIMER_OFF = false;
    public static final boolean TIMER_ON = true;
    public static boolean soundEnabled = true;

    private Handler mHandler;
    private Drawable mChineseChar;
    private long mLastTime;

    private Paint mLinePaint;
    private Paint mTextPaint;
    private Paint mTextSmPaint;
	
    private TextView mframe;

    private double mX;
    private double mY;
    private int charindex;
		
    private Timer mTimer = null;
    private boolean mTimerOn = true;
    private TimerTask mTimerTask = null;
    private int mTaskIntervalInMillis = 5000;           // 5 seconds
    private long mLastBeatTime;
    private long mPassedTime;

	List<Character> chinesechars = new ArrayList();
	List<Integer> chineseIds= new ArrayList();

    public ChineseView(Context context, AttributeSet attrs) {
	super(context, attrs);
	
	Resources res = this.getResources();
	mTimer = new Timer();
	// Initialize paints
	mLinePaint = new Paint();
	mLinePaint.setAntiAlias(true);
	mLinePaint.setARGB(255, 0, 255, 0);
			
	mTextPaint = new Paint();
	mTextPaint.setColor(Color.WHITE);
	mTextPaint.setTextAlign(Paint.Align.CENTER);;
	mTextPaint.setAntiAlias(true);
	mTextPaint.setTextSize(28);

	mTextSmPaint = new Paint();
	mTextSmPaint.setColor(Color.BLACK);
	mTextSmPaint.setAntiAlias(true);
	mTextSmPaint.setTextAlign(Paint.Align.CENTER);;

	        
	mframe = (TextView) findViewById(R.id.frame);
	TypedArray icons = res.obtainTypedArray(R.array.chargraphics);
//	  TypedArray charname = res.obtainTypedArray(R.array.charname);
	TypedArray unicode = res.obtainTypedArray(R.array.unicode);
//	  TypedArray tone = res.obtainTypedArray(R.array.tones);
//	  TypedArray pron = res.obtainTypedArray(R.array.pronunciation);

	for (int i=0; i<NUMCHARS; i++)    {
 	    int charId = icons.getResourceId(i, 0);

 	    chinesechars.add(new Character(unicode.getString(i), charId, null, null, null));
        chineseIds.add(charId);
	}
     TOTALCHARS = chinesechars.size();
	 charindex = 0;
	 invalidate();
     }
	    	
    public void setSound(String string) {
         soundEnabled = false;
         if (string.equals("on"))  {
             soundEnabled = true;
         } 
      }
		
    public void setCharacter(int direction) {
	    if (direction == NEXT)  {
	      charindex += 1;
	    }
        if (direction == PREVIOUS)  {
           charindex -= 1;
        }

	    charindex %= TOTALCHARS;
        if (charindex < 0) {
           charindex = TOTALCHARS-1;
        }
        invalidate();
		}

    public void setFlashcard(boolean b) {
	mTimerOn = b;
    }

    public void setState(int mode) {
	setState(mode, null);
		}

    public void setState(int mode, CharSequence message) {

				CharSequence str = "";

				Message msg = mHandler.obtainMessage();
				Bundle b = new Bundle();
				b.putString("text", str.toString());
				b.putInt("viz", View.VISIBLE);
				msg.setData(b);
				mHandler.sendMessage(msg);
		}

	public int getFramenum() {
		return charindex;
	}


	boolean doKeyDown(int keyCode, KeyEvent msg) {         
				boolean okStart = false;
				if (keyCode == KeyEvent.KEYCODE_DPAD_UP
					|| keyCode == KeyEvent.KEYCODE_DPAD_DOWN
					|| keyCode == KeyEvent.KEYCODE_S)
					okStart = true;

				boolean center = (keyCode == KeyEvent.KEYCODE_DPAD_UP);


				return false;
		}

    boolean doKeyUp(int keyCode, KeyEvent msg) {
	boolean handled = false;

	if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER|| keyCode == KeyEvent.KEYCODE_SPACE) {
	handled = true;
	} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_Q || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_W) {
	handled = true;
			    }
	return handled;
	}


    @Override
    public void onDraw(Canvas canvas) {
        final Character thc;

	Paint p=new Paint();
	p.setFilterBitmap(true);
	thc = chinesechars.get(charindex);
//	thc.charImage.draw(canvas);

    
        Bitmap b=BitmapFactory.decodeResource(getResources(), chineseIds.get(charindex));
        p.setColor(Color.RED);
        canvas.scale(0.6f, 0.75f);
        canvas.drawBitmap(b, 150, 60, p);
//      ChineseTeacher.getTextView().setText("set");
	        
//	    ImageView i = new ImageView(this);
//	    i.setImageResource(R.drawable.smiley);
	        
    	mTextPaint.setTextSize(50);

//	    canvas.drawText(thc.charName, 300, 800, mTextPaint);
//		canvas.drawText(thc.unicode, 10, 70, mTextSmPaint);
//		canvas.drawText(thc.tone, 150, 370, mTextSmPaint);
//		canvas.drawText(thc.pronunciation, 300, 880, mTextPaint);

	canvas.restore();
	b.recycle();
	b = null;
    }
	
}
