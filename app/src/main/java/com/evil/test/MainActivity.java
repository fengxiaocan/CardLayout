package com.evil.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

import com.evil.cardlayout.CardLinearLayout;
import com.evil.cardlayout.CardRelativeLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
	
	private CardRelativeLayout mCardrelativelayout;
	private CardLinearLayout mCardlinearlayout;
	private CardView mCardview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}
	
	private void initView() {
		mCardrelativelayout = (CardRelativeLayout)findViewById(R.id.cardrelativelayout);
		mCardlinearlayout = (CardLinearLayout)findViewById(R.id.cardlinearlayout);
		mCardview = (CardView)findViewById(R.id.cardview);
		mCardrelativelayout.setOnClickListener(this);
		mCardlinearlayout.setOnClickListener(this);
		mCardview.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
	
	}
}
