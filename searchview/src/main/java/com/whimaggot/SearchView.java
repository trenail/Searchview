package com.whimaggot;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.whimaggot.searchview.searchview.R;

/**
 * Created by whiMaggot on 16/8/12.
 */

public class SearchView extends EditText{
    private static final String TAG = "SearchView";
    private float searchWidth ,searchHeight,deleteWidth,deleteHeight;
    private float hintTextSize = 0;
    private int hintTextColor = 0xFF000000;
    private Drawable mSearchDrawable;
    private Drawable mDeleteDrawable;
    private int searchDrawableId;
    private int deleteDrawableId;
    private String hintText;
    private Paint mPaint;
    private ValueAnimator searchIconAnimator = new ValueAnimator();
    private float iconMidPositionX;
    private float iconLeftPositionX;
    private float iconNowPositionX;
    private boolean playLeftFlag,playRightFlag;

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initResource(context, attrs);
        initPaint();

    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if(getText().length()==0) {
            playLeftFlag = focused;
            playRightFlag  = !playLeftFlag;
        }else{
            playLeftFlag = false;
            playRightFlag = playLeftFlag;
        }
    }

    private void initAnimation() {
        float textWidth = mPaint.measureText(hintText);
        iconMidPositionX = (getWidth() - searchWidth - textWidth - 8) / 2;
        if(playLeftFlag) {
            iconNowPositionX = iconMidPositionX;
            searchIconAnimator.setFloatValues(iconMidPositionX, iconLeftPositionX);
        }
        if(playRightFlag){
            iconNowPositionX = iconLeftPositionX;
            searchIconAnimator.setFloatValues(iconLeftPositionX, iconMidPositionX);
        }
        searchIconAnimator.setDuration(200);
        searchIconAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                iconNowPositionX = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        searchIconAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setCursorVisible(hasFocus());
                playLeftFlag = false;
                playRightFlag = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                setCursorVisible(hasFocus());
                playLeftFlag = false;
                playRightFlag = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void initResource(Context context, AttributeSet attrs) {
        setSingleLine(true);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.search_view);
        float density = context.getResources().getDisplayMetrics().density;
        searchWidth = mTypedArray.getDimension(R.styleable.search_view_search_icon_width, 18 * density + 0.5F);
        searchHeight = mTypedArray.getDimension(R.styleable.search_view_search_icon_height,18 * density + 0.5F);
        deleteWidth = mTypedArray.getDimension(R.styleable.search_view_delete_icon_width,18 * density + 0.5F);
        deleteHeight = mTypedArray.getDimension(R.styleable.search_view_delete_icon_height,18 * density + 0.5F);
        hintTextColor = mTypedArray.getColor(R.styleable.search_view_hint_text_color, 0xFF848484);
        hintTextSize = mTypedArray.getDimension(R.styleable.search_view_hint_text_size, 14 * density + 0.5F);
        searchDrawableId = mTypedArray.getResourceId(R.styleable.search_view_search_icon,R.drawable.icon_search_view_search);
        deleteDrawableId = mTypedArray.getResourceId(R.styleable.search_view_delete_icon,R.drawable.icon_search_view_delete);
        hintText = mTypedArray.getString(R.styleable.search_view_hint_text);
        if(hintText==null || "".equals(hintText)){
            hintText = TAG;
        }
        mTypedArray.recycle();
        iconLeftPositionX = getPaddingLeft();
        setPadding((int) searchWidth+getPaddingLeft(),0, (int) deleteWidth+getPaddingRight(),0);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if((event.getX()>getWidth()-getPaddingRight())&&(event.getX()<getWidth()-getPaddingRight()+deleteWidth)){
                    setText("");
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(hintTextColor);
        mPaint.setTextSize(hintTextSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(playLeftFlag || playRightFlag){
            if(!searchIconAnimator.isStarted()){
                initAnimation();
                searchIconAnimator.start();
            }
            onAnimationUpdate(canvas);
            return;
        }else{
            if(searchIconAnimator.isStarted()){
                searchIconAnimator.cancel();
            }
        }
        drawIcon(canvas);
    }

    private void drawIcon(Canvas canvas) {
        if (!hasFocus() && getText().toString().length()==0) {
            float textWidth = mPaint.measureText(hintText);
            float textHeight = getFontLeading(mPaint);

            float dx = (getWidth() - searchWidth - textWidth - 8) / 2;
            float dy = (getHeight() - searchHeight) / 2;

            canvas.save();
            canvas.translate(getScrollX() + dx, getScrollY() + dy);
            if (mSearchDrawable != null) {
                mSearchDrawable.draw(canvas);
            }
            canvas.drawText(hintText, getScrollX() + searchWidth + 8, getScrollY() + (getHeight() - (getHeight() - textHeight) / 2) - mPaint.getFontMetrics().bottom - dy, mPaint);
            canvas.restore();
        }else {
            float dx = iconLeftPositionX;
            float dy = (getHeight() - searchHeight) / 2;
            canvas.save();
            canvas.translate(getScrollX() + dx, getScrollY() + dy);
            if (mSearchDrawable != null) {
                mSearchDrawable.draw(canvas);
            }
            canvas.restore();
            if(getText().toString().length()>0) {
                dx = getWidth() -getPaddingRight();
                canvas.save();
                canvas.translate(getScrollX() + dx, getScrollY() + ((getHeight() - deleteHeight) / 2));
                if (mDeleteDrawable != null) {
                    mDeleteDrawable.draw(canvas);
                }
            }
        }
    }

    private void onAnimationUpdate(Canvas canvas) {
        float dy = (getHeight() - searchHeight) / 2;
        canvas.save();
        canvas.translate(getScrollX() + iconNowPositionX, getScrollY() + dy);
        if (mSearchDrawable != null) {
            mSearchDrawable.draw(canvas);
        }
        canvas.restore();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mSearchDrawable == null) {
            if(searchDrawableId>0){
                mSearchDrawable = getContext().getResources().getDrawable(searchDrawableId);
            }else {
                mSearchDrawable = getContext().getResources().getDrawable(R.drawable.icon_search_view_search);
            }
            mSearchDrawable.setBounds(0, 0, (int) searchWidth, (int) searchHeight);

        }
        if(mDeleteDrawable ==null){
            if(deleteDrawableId>0){
                mDeleteDrawable = getContext().getResources().getDrawable(deleteDrawableId);
            }else {
                mDeleteDrawable = getContext().getResources().getDrawable(R.drawable.icon_search_view_delete);
            }
            mDeleteDrawable.setBounds(0, 0, (int) deleteWidth, (int) deleteHeight);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mSearchDrawable != null) {
            mSearchDrawable.setCallback(null);
            mSearchDrawable = null;
        }
        if (mDeleteDrawable != null) {
            mDeleteDrawable.setCallback(null);
            mDeleteDrawable = null;
        }
        super.onDetachedFromWindow();
    }

    public float getFontLeading(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.bottom - fm.top;
    }
}
