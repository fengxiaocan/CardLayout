package com.evil.card;

import android.view.View;

interface ICardView {
    View getCurrentView();

    void superOnMeasure(int widthMeasureSpec, int heightMeasureSpec);
}
