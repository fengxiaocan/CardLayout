package com.evil.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

import com.evil.card.CardFrameLayout;
import com.evil.card.CardLinearLayout;
import com.evil.card.CardRelativeLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
	
	private CardRelativeLayout mCardrelativelayout;
	private CardLinearLayout mCardlinearlayout;
	private CardFrameLayout cardFrameLayout;
	private CardView mCardview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}
	
	private void initView() {
		mCardrelativelayout = findViewById(R.id.cardrelativelayout);
		mCardlinearlayout = findViewById(R.id.cardlinearlayout);
		cardFrameLayout =  findViewById(R.id.cardframelayout);
		mCardview = findViewById(R.id.cardview);

		mCardrelativelayout.setOnClickListener(this);
		mCardlinearlayout.setOnClickListener(this);
		cardFrameLayout.setOnClickListener(this);
		mCardview.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
	
	}
}
