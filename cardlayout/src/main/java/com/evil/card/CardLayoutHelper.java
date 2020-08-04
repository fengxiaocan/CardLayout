package com.evil.card;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import android.util.AttributeSet;
import android.view.View;

public final class CardLayoutHelper implements CardViewDelegate, ICardLayout {
    private static final int[] COLOR_BACKGROUND_ATTR = new int[]{16842801};
    private static final CardViewImpl IMPL;

    static {
        if (Build.VERSION.SDK_INT >= 21) {
            IMPL = new CardViewApi21Impl();
        } else if (Build.VERSION.SDK_INT >= 17) {
            IMPL = new CardViewApi17Impl();
        } else {
            IMPL = new CardViewBaseImpl();
        }

        IMPL.initStatic();
    }

    final Rect mContentPadding;
    final Rect mShadowBounds;
    final ICardView iCardLayoutView;

    int mUserSetMinWidth;
    int mUserSetMinHeight;
    boolean mCompatPadding;
    boolean mPreventCornerOverlap;
    boolean mCardIsCircle;
    boolean mCardIsRounded;
    Drawable mCardBackground;

    public CardLayoutHelper(ICardView layoutView) {
        mContentPadding = new Rect();
        mShadowBounds = new Rect();
        iCardLayoutView = layoutView;
    }

    public void initAttr(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CardLayout, defStyleAttr,
                R.style.CardLayout);
        ColorStateList backgroundColor = null;

        if (a.hasValue(R.styleable.CardLayout_cardBackgroundColor)) {
            try {
                backgroundColor = a.getColorStateList(R.styleable.CardLayout_cardBackgroundColor);
            } catch (Exception e) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    Drawable drawable = a.getDrawable(R.styleable.CardLayout_cardBackgroundColor);
                    getCardView().setBackground(drawable);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    backgroundColor = getCardView().getBackgroundTintList();
                }
            }
        } else {
            TypedArray aa = context.obtainStyledAttributes(COLOR_BACKGROUND_ATTR);
            int themeColorBackground = aa.getColor(0, 0);
            aa.recycle();
            float[] hsv = new float[3];
            Color.colorToHSV(themeColorBackground, hsv);
            backgroundColor = ColorStateList.valueOf(hsv[2] > 0.5F ?
                    context.getResources().getColor(R.color.cardview_light_background) :
                    context.getResources().getColor(R.color.cardview_dark_background));
        }

        float radius = a.getDimension(R.styleable.CardLayout_cardCornerRadius, 0);
        //是否是圆形
        mCardIsCircle = a.getBoolean(R.styleable.CardLayout_cardIsCircle, false);
        //是否是半圆的矩形
        mCardIsRounded = a.getBoolean(R.styleable.CardLayout_cardIsRounded, false);

        float elevation = a.getDimension(R.styleable.CardLayout_cardElevation, 0);
        float maxElevation = a.getDimension(R.styleable.CardLayout_cardMaxElevation, 0.0F);
        this.mCompatPadding = a.getBoolean(R.styleable.CardLayout_cardUseCompatPadding, false);
        this.mPreventCornerOverlap = a.getBoolean(R.styleable.CardLayout_cardPreventCornerOverlap,
                true);

        int defaultPadding = a.getDimensionPixelSize(R.styleable.CardLayout_contentPadding, 0);

        this.mContentPadding.left = a.getDimensionPixelSize(
                R.styleable.CardLayout_contentPaddingLeft,
                defaultPadding == 0 ? getCardView().getPaddingLeft() : defaultPadding);
        this.mContentPadding.top = a.getDimensionPixelSize(R.styleable.CardLayout_contentPaddingTop,
                defaultPadding == 0 ? getCardView().getPaddingTop() : defaultPadding);
        this.mContentPadding.right = a.getDimensionPixelSize(
                R.styleable.CardLayout_contentPaddingRight,
                defaultPadding == 0 ? getCardView().getPaddingRight() : defaultPadding);
        this.mContentPadding.bottom = a.getDimensionPixelSize(
                R.styleable.CardLayout_contentPaddingBottom,
                defaultPadding == 0 ? getCardView().getPaddingBottom() : defaultPadding);

        if (elevation > maxElevation) {
            maxElevation = elevation;
        }

        int minimumWidth = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            minimumWidth = getCardView().getMinimumWidth();
        }
        this.mUserSetMinWidth = a.getDimensionPixelSize(R.styleable.CardLayout_androidMinWidth,
                minimumWidth);
        int minimumHeight = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            minimumHeight = getCardView().getMinimumHeight();
        }
        this.mUserSetMinHeight = a.getDimensionPixelSize(R.styleable.CardLayout_androidMinHeight,
                minimumHeight);
        a.recycle();
        IMPL.initialize(this, context, backgroundColor, radius, elevation, maxElevation);
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mCardIsCircle) {
            int min = Math.min(widthMeasureSpec, heightMeasureSpec);
            heightMeasureSpec = widthMeasureSpec = min;
        }
        if (!(IMPL instanceof CardViewApi21Impl)) {
            int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
            int heightMode;
            switch (widthMode) {
                case -2147483648:
                case 1073741824:
                    heightMode = (int) Math.ceil((double) IMPL.getMinWidth(this));
                    widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                            Math.max(heightMode, View.MeasureSpec.getSize(widthMeasureSpec)),
                            widthMode);
                case 0:
                default:
                    heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
                    switch (heightMode) {
                        case -2147483648:
                        case 1073741824:
                            int minHeight = (int) Math.ceil((double) IMPL.getMinHeight(this));
                            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(Math.max(minHeight,
                                    View.MeasureSpec.getSize(heightMeasureSpec)), heightMode);
                        case 0:
                        default:
                            iCardLayoutView.superOnMeasure(widthMeasureSpec, heightMeasureSpec);
                    }
            }
        } else {
            iCardLayoutView.superOnMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (mCardIsCircle || mCardIsRounded) {
            //是否是圆形
            setRadius(Math.min((bottom - top) / 2, (right - left) / 2));
        }
    }

    public void setPadding(int left, int top, int right, int bottom) {
        //EMPTY
    }

    public void setPaddingRelative(int start, int top, int end, int bottom) {
        //EMPTY
    }

    @Override
    public boolean getUseCompatPadding() {
        return mCompatPadding;
    }

    @Override
    public boolean getPreventCornerOverlap() {
        return mPreventCornerOverlap;
    }

    @Override
    public void setShadowPadding(int left, int top, int right, int bottom) {
        mShadowBounds.set(left, top, right, bottom);
        getCardView().setPadding(left + mContentPadding.left, top + mContentPadding.top,
                right + mContentPadding.right, bottom + mContentPadding.bottom);
    }

    @Override
    public void setMinWidthHeightInternal(int width, int height) {
        if (width > mUserSetMinWidth) {
            getCardView().setMinimumWidth(width);
        }

        if (height > mUserSetMinHeight) {
            getCardView().setMinimumHeight(height);
        }
    }

    @Override
    public View getCardView() {
        return iCardLayoutView.getCurrentView();
    }

    @Override
    public Drawable getCardBackground() {
        return mCardBackground;
    }


    @Override
    public void setCardBackground(Drawable drawable) {
        mCardBackground = drawable;
        getCardView().setBackgroundDrawable(drawable);
    }

    @Override
    public void setUseCompatPadding(boolean useCompatPadding) {
        if (this.mCompatPadding != useCompatPadding) {
            this.mCompatPadding = useCompatPadding;
            IMPL.onCompatPaddingChanged(this);
        }
    }

    @Override
    public void setContentPadding(@Px int left, @Px int top, @Px int right, @Px int bottom) {
        this.mContentPadding.set(left, top, right, bottom);
        IMPL.updatePadding(this);
    }

    @Override
    public void setMinimumWidth(int minWidth) {
        this.mUserSetMinWidth = minWidth;
    }

    @Override
    public void setMinimumHeight(int minHeight) {
        this.mUserSetMinHeight = minHeight;
    }

    @Override
    public void setCardBackgroundColor(@ColorInt int color) {
        IMPL.setBackgroundColor(this, ColorStateList.valueOf(color));
    }

    @Override
    @NonNull
    public ColorStateList getCardBackgroundColor() {
        return IMPL.getBackgroundColor(this);
    }

    @Override
    public void setCardBackgroundColor(@Nullable ColorStateList color) {
        IMPL.setBackgroundColor(this, color);
    }

    @Override
    @Px
    public int getContentPaddingLeft() {
        return this.mContentPadding.left;
    }

    @Override
    @Px
    public int getContentPaddingRight() {
        return this.mContentPadding.right;
    }

    @Override
    @Px
    public int getContentPaddingTop() {
        return this.mContentPadding.top;
    }

    @Override
    @Px
    public int getContentPaddingBottom() {
        return this.mContentPadding.bottom;
    }

    @Override
    public float getRadius() {
        return IMPL.getRadius(this);
    }

    @Override
    public void setRadius(float radius) {
        IMPL.setRadius(this, radius);
    }

    @Override
    public float getCardElevation() {
        return IMPL.getElevation(this);
    }

    @Override
    public void setCardElevation(float elevation) {
        IMPL.setElevation(this, elevation);
    }

    @Override
    public float getMaxCardElevation() {
        return IMPL.getMaxElevation(this);
    }

    @Override
    public void setMaxCardElevation(float maxElevation) {
        IMPL.setMaxElevation(this, maxElevation);
    }

    @Override
    public void setPreventCornerOverlap(boolean preventCornerOverlap) {
        if (preventCornerOverlap != this.mPreventCornerOverlap) {
            this.mPreventCornerOverlap = preventCornerOverlap;
            IMPL.onPreventCornerOverlapChanged(this);
        }
    }

    @Override
    public void setIsCircle(boolean isCircle) {
        mCardIsCircle = isCircle;
        getCardView().invalidate();
    }

    @Override
    public void setIsRounded(boolean isRounded) {
        mCardIsRounded = isRounded;
        getCardView().invalidate();
    }

}
