package com.evil.cardlayout;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * @author noah
 * @email fengxiaocan@gmail.com
 * @create 31/10/18
 * @desc ...
 */
public class CardLinearLayout extends LinearLayout {
	private static final int[] COLOR_BACKGROUND_ATTR = new int[]{16842801};
	private static final CardViewImpl IMPL;
	static {
		if (Build.VERSION.SDK_INT >= 21) {
			IMPL = new CardViewApi21Impl();
		}
		else if (Build.VERSION.SDK_INT >= 17) {
			IMPL = new CardViewApi17Impl();
		}
		else {
			IMPL = new CardViewBaseImpl();
		}
		
		IMPL.initStatic();
	}
	
	final Rect mContentPadding;
	final Rect mShadowBounds;
	private final CardViewDelegate mCardViewDelegate;
	int mUserSetMinWidth;
	int mUserSetMinHeight;
	private boolean mCompatPadding;
	private boolean mPreventCornerOverlap;
	
	public CardLinearLayout(@NonNull Context context) {
		this(context,(AttributeSet)null);
	}
	
	public CardLinearLayout(@NonNull Context context,@Nullable AttributeSet attrs) {
		this(context,attrs,android.support.v7.cardview.R.attr.cardViewStyle);
	}
	
	public CardLinearLayout(@NonNull Context context,@Nullable AttributeSet attrs,int defStyleAttr) {
		super(context,attrs,defStyleAttr);
		this.mContentPadding = new Rect();
		this.mShadowBounds = new Rect();
		this.mCardViewDelegate = new CardViewDelegate() {
			private Drawable mCardBackground;
			
			public boolean getUseCompatPadding() {
				return CardLinearLayout.this.getUseCompatPadding();
			}
			
			public boolean getPreventCornerOverlap() {
				return CardLinearLayout.this.getPreventCornerOverlap();
			}
			
			public void setShadowPadding(int left,int top,int right,int bottom) {
				CardLinearLayout.this.mShadowBounds.set(left,top,right,bottom);
				CardLinearLayout.super.setPadding(left + CardLinearLayout.this.mContentPadding.left,
				                          top + CardLinearLayout.this.mContentPadding.top,
				                          right + CardLinearLayout.this.mContentPadding.right,
				                          bottom + CardLinearLayout.this.mContentPadding.bottom);
			}
			
			public void setMinWidthHeightInternal(int width,int height) {
				if (width > CardLinearLayout.this.mUserSetMinWidth) {
					CardLinearLayout.super.setMinimumWidth(width);
				}
				
				if (height > CardLinearLayout.this.mUserSetMinHeight) {
					CardLinearLayout.super.setMinimumHeight(height);
				}
				
			}
			
			public Drawable getCardBackground() {
				return this.mCardBackground;
			}
			
			public void setCardBackground(Drawable drawable) {
				this.mCardBackground = drawable;
				CardLinearLayout.this.setBackgroundDrawable(drawable);
			}
			
			public View getCardView() {
				return CardLinearLayout.this;
			}
		};
		TypedArray a = context
				.obtainStyledAttributes(attrs,R.styleable.CardLinearLayout,
				                        defStyleAttr,R.style.CardView);
		ColorStateList backgroundColor;
		if (a.hasValue(R.styleable.CardLinearLayout_cardBackgroundColor)) {
			backgroundColor = a.getColorStateList(
					R.styleable.CardLinearLayout_cardBackgroundColor);
		}
		else {
			TypedArray aa = this.getContext().obtainStyledAttributes(COLOR_BACKGROUND_ATTR);
			int themeColorBackground = aa.getColor(0,0);
			aa.recycle();
			float[] hsv = new float[3];
			Color.colorToHSV(themeColorBackground,hsv);
			backgroundColor = ColorStateList.valueOf(hsv[2] > 0.5F ? this.getResources().getColor(
					R.color.cardview_light_background)
					                                         : this.getResources().getColor(
							                                         R.color.cardview_dark_background));
		}
		
		float radius = a
				.getDimension(R.styleable.CardLinearLayout_cardCornerRadius,
				              0.0F);
		float elevation = a
				.getDimension(R.styleable.CardLinearLayout_cardElevation,0.0F);
		float maxElevation = a
				.getDimension(R.styleable.CardLinearLayout_cardMaxElevation,
				              0.0F);
		this.mCompatPadding = a
				.getBoolean(R.styleable.CardLinearLayout_cardUseCompatPadding,
				            false);
		this.mPreventCornerOverlap = a.getBoolean(
				R.styleable.CardLinearLayout_cardPreventCornerOverlap,true);
		int defaultPadding = a.getDimensionPixelSize(
				R.styleable.CardLinearLayout_contentPadding,0);
		this.mContentPadding.left = a.getDimensionPixelSize(
				R.styleable.CardLinearLayout_contentPaddingLeft,defaultPadding);
		this.mContentPadding.top = a.getDimensionPixelSize(
				R.styleable.CardLinearLayout_contentPaddingTop,defaultPadding);
		this.mContentPadding.right = a.getDimensionPixelSize(
				R.styleable.CardLinearLayout_contentPaddingRight,
				defaultPadding);
		this.mContentPadding.bottom = a.getDimensionPixelSize(
				R.styleable.CardLinearLayout_contentPaddingBottom,
				defaultPadding);
		if (elevation > maxElevation) {
			maxElevation = elevation;
		}
		
		this.mUserSetMinWidth = a.getDimensionPixelSize(
				R.styleable.CardLinearLayout_android_minWidth,0);
		this.mUserSetMinHeight = a.getDimensionPixelSize(
				R.styleable.CardLinearLayout_android_minHeight,0);
		a.recycle();
		IMPL.initialize(this.mCardViewDelegate,context,backgroundColor,radius,elevation,
		                maxElevation);
	}
	
	public void setPadding(int left,int top,int right,int bottom) {
	}
	
	public void setPaddingRelative(int start,int top,int end,int bottom) {
	}
	
	public boolean getUseCompatPadding() {
		return this.mCompatPadding;
	}
	
	public void setUseCompatPadding(boolean useCompatPadding) {
		if (this.mCompatPadding != useCompatPadding) {
			this.mCompatPadding = useCompatPadding;
			IMPL.onCompatPaddingChanged(this.mCardViewDelegate);
		}
		
	}
	
	public void setContentPadding(@Px int left,@Px int top,@Px int right,@Px int bottom) {
		this.mContentPadding.set(left,top,right,bottom);
		IMPL.updatePadding(this.mCardViewDelegate);
	}
	
	protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec) {
		if (!(IMPL instanceof CardViewApi21Impl)) {
			int widthMode = MeasureSpec.getMode(widthMeasureSpec);
			int heightMode;
			switch (widthMode) {
				case -2147483648:
				case 1073741824:
					heightMode = (int)Math.ceil((double)IMPL.getMinWidth(this.mCardViewDelegate));
					widthMeasureSpec = MeasureSpec.makeMeasureSpec(
							Math.max(heightMode,MeasureSpec.getSize(widthMeasureSpec)),widthMode);
				case 0:
				default:
					heightMode = MeasureSpec.getMode(heightMeasureSpec);
					switch (heightMode) {
						case -2147483648:
						case 1073741824:
							int minHeight = (int)Math
									.ceil((double)IMPL.getMinHeight(this.mCardViewDelegate));
							heightMeasureSpec = MeasureSpec.makeMeasureSpec(
									Math.max(minHeight,MeasureSpec.getSize(heightMeasureSpec)),
									heightMode);
						case 0:
						default:
							super.onMeasure(widthMeasureSpec,heightMeasureSpec);
					}
			}
		}
		else {
			super.onMeasure(widthMeasureSpec,heightMeasureSpec);
		}
		
	}
	
	public void setMinimumWidth(int minWidth) {
		this.mUserSetMinWidth = minWidth;
		super.setMinimumWidth(minWidth);
	}
	
	public void setMinimumHeight(int minHeight) {
		this.mUserSetMinHeight = minHeight;
		super.setMinimumHeight(minHeight);
	}
	
	public void setCardBackgroundColor(@ColorInt int color) {
		IMPL.setBackgroundColor(this.mCardViewDelegate,ColorStateList.valueOf(color));
	}
	
	@NonNull
	public ColorStateList getCardBackgroundColor() {
		return IMPL.getBackgroundColor(this.mCardViewDelegate);
	}
	
	public void setCardBackgroundColor(@Nullable ColorStateList color) {
		IMPL.setBackgroundColor(this.mCardViewDelegate,color);
	}
	
	@Px
	public int getContentPaddingLeft() {
		return this.mContentPadding.left;
	}
	
	@Px
	public int getContentPaddingRight() {
		return this.mContentPadding.right;
	}
	
	@Px
	public int getContentPaddingTop() {
		return this.mContentPadding.top;
	}
	
	@Px
	public int getContentPaddingBottom() {
		return this.mContentPadding.bottom;
	}
	
	public float getRadius() {
		return IMPL.getRadius(this.mCardViewDelegate);
	}
	
	public void setRadius(float radius) {
		IMPL.setRadius(this.mCardViewDelegate,radius);
	}
	
	public float getCardElevation() {
		return IMPL.getElevation(this.mCardViewDelegate);
	}
	
	public void setCardElevation(float elevation) {
		IMPL.setElevation(this.mCardViewDelegate,elevation);
	}
	
	public float getMaxCardElevation() {
		return IMPL.getMaxElevation(this.mCardViewDelegate);
	}
	
	public void setMaxCardElevation(float maxElevation) {
		IMPL.setMaxElevation(this.mCardViewDelegate,maxElevation);
	}
	
	public boolean getPreventCornerOverlap() {
		return this.mPreventCornerOverlap;
	}
	
	public void setPreventCornerOverlap(boolean preventCornerOverlap) {
		if (preventCornerOverlap != this.mPreventCornerOverlap) {
			this.mPreventCornerOverlap = preventCornerOverlap;
			IMPL.onPreventCornerOverlapChanged(this.mCardViewDelegate);
		}
		
	}
}

