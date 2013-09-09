package com.srijan.trex.activities;

import com.srijan.trex.R;
import com.srijan.trex.R.id;
import com.srijan.trex.R.layout;
import com.srijan.trex.fragments.UpdateExpenseFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

public class UpdateExpenseActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_update_expense) ;
		//Window w = (Window) getSystemService(WINDOW_SERVICE);
		
		UpdateExpenseFragment uef = (UpdateExpenseFragment)getSupportFragmentManager().findFragmentById(R.id.update_expense);
	}

	
	
}
