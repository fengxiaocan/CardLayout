package com.evil.card;

import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;

public interface ICardLayout {

    boolean getUseCompatPadding();

    void setUseCompatPadding(boolean useCompatPadding);

    void setContentPadding(@Px int left, @Px int top, @Px int right, @Px int bottom);

    void setMinimumWidth(int minWidth);

    void setMinimumHeight(int minHeight);

    void setCardBackgroundColor(@ColorInt int color);

    @NonNull
    ColorStateList getCardBackgroundColor();

    void setCardBackgroundColor(@Nullable ColorStateList color);

    @Px
    int getContentPaddingLeft();

    @Px
    int getContentPaddingRight();

    @Px
    int getContentPaddingTop();

    @Px
    int getContentPaddingBottom();

    float getRadius();

    void setRadius(float radius);

    float getCardElevation();

    void setCardElevation(float elevation);

    float getMaxCardElevation();

    void setMaxCardElevation(float maxElevation);

    boolean getPreventCornerOverlap();

    void setPreventCornerOverlap(boolean preventCornerOverlap);

    void setIsCircle(boolean isCircle);

    void setIsRounded(boolean isRounded);
}
