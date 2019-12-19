package com.evil.card;

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * @author noah
 * @email fengxiaocan@gmail.com
 * @create 31/10/18
 * @desc ...
 */
interface CardViewDelegate {
	void setCardBackground(Drawable var1);
	
	Drawable getCardBackground();
	
	boolean getUseCompatPadding();
	
	boolean getPreventCornerOverlap();
	
	void setShadowPadding(int var1,int var2,int var3,int var4);
	
	void setMinWidthHeightInternal(int var1,int var2);
	
	View getCardView();
}
