package com.evil.card;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

/**
 * @author noah
 * @email fengxiaocan@gmail.com
 * @create 31/10/18
 * @desc ...
 */
public class CardViews extends View implements ICardLayout {
    private final CardLayoutHelper HELPER;

    public CardViews(@NonNull Context context) {
        this(context, (AttributeSet) null);
    }

    public CardViews(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.cardViewStyle);
    }

    public CardViews(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        HELPER = new CardLayoutHelper(new ICardView() {
            @Override
            public View getCurrentView() {
                return CardViews.this;
            }

            @SuppressLint("WrongCall")
            @Override
            public void superOnMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                CardViews.super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }

        });
        HELPER.initAttr(getContext(), attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        HELPER.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        HELPER.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void setMinimumWidth(int minWidth) {
        HELPER.setMinimumWidth(minWidth);
        super.setMinimumWidth(minWidth);
    }

    @Override
    public void setMinimumHeight(int minHeight) {
        HELPER.setMinimumHeight(minHeight);
        super.setMinimumHeight(minHeight);
    }

    public void setPadding(int left, int top, int right, int bottom) {
        HELPER.setPadding(left, top, right, bottom);
    }

    public void setPaddingRelative(int start, int top, int end, int bottom) {
        HELPER.setPaddingRelative(start, top, end, bottom);
    }

    @Override
    public boolean getUseCompatPadding() {
        return HELPER.getUseCompatPadding();
    }

    @Override
    public void setUseCompatPadding(boolean useCompatPadding) {
        HELPER.setUseCompatPadding(useCompatPadding);
    }

    @Override
    public void setContentPadding(int left, int top, int right, int bottom) {
        HELPER.setContentPadding(left, top, right, bottom);
    }

    @Override
    public void setCardBackgroundColor(int color) {
        HELPER.setCardBackgroundColor(color);
    }

    @NonNull
    @Override
    public ColorStateList getCardBackgroundColor() {
        return HELPER.getCardBackgroundColor();
    }

    @Override
    public void setCardBackgroundColor(@Nullable ColorStateList color) {
        HELPER.setCardBackgroundColor(color);
    }

    @Override
    public int getContentPaddingLeft() {
        return HELPER.getContentPaddingLeft();
    }

    @Override
    public int getContentPaddingRight() {
        return HELPER.getContentPaddingRight();
    }

    @Override
    public int getContentPaddingTop() {
        return HELPER.getContentPaddingTop();
    }

    @Override
    public int getContentPaddingBottom() {
        return HELPER.getContentPaddingBottom();
    }

    @Override
    public float getRadius() {
        return HELPER.getRadius();
    }

    @Override
    public void setRadius(float radius) {
        HELPER.setRadius(radius);
    }

    @Override
    public float getCardElevation() {
        return HELPER.getCardElevation();
    }

    @Override
    public void setCardElevation(float elevation) {
        HELPER.setCardElevation(elevation);
    }

    @Override
    public float getMaxCardElevation() {
        return HELPER.getMaxCardElevation();
    }

    @Override
    public void setMaxCardElevation(float maxElevation) {
        HELPER.setMaxCardElevation(maxElevation);
    }

    @Override
    public boolean getPreventCornerOverlap() {
        return HELPER.getPreventCornerOverlap();
    }

    @Override
    public void setPreventCornerOverlap(boolean preventCornerOverlap) {
        HELPER.setPreventCornerOverlap(preventCornerOverlap);
    }

    @Override
    public void setBackgroundColor(int color) {
//        super.setBackgroundColor(color);
        HELPER.setCardBackgroundColor(color);
    }

    @Override
    public void setBackgroundTintList(ColorStateList tint) {
//        super.setBackgroundTintList(tint);
        HELPER.setCardBackgroundColor(tint);
    }

    @Override
    public void setIsCircle(boolean isCircle) {
        HELPER.setIsCircle(isCircle);
    }

    @Override
    public void setIsRounded(boolean isRounded) {
        HELPER.setIsCircle(isRounded);
    }
}

