package com.srijan.trex.activities;

import com.srijan.trex.R;
import com.srijan.trex.R.id;
import com.srijan.trex.R.layout;
import com.srijan.trex.fragments.ExpenseCompleteFragment;

import android.app.Activity;
import android.content.Intent;
import android.inputmethodservice.Keyboard.Key;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;

public class ExpenseCompleteActivity extends FragmentActivity implements ExpenseCompleteFragment.MethodCallback{

	String TAG = "ExpenseCompleteActivity" ;
	ExpenseCompleteFragment ecf ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.v(TAG, "Line 1") ;
		setContentView(R.layout.activity_expense_complete);
		Log.v(TAG, "Line 2") ;
		ecf = (ExpenseCompleteFragment)getSupportFragmentManager().findFragmentById(R.id.expense_complete);
		//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE); // calling soft input keyboard
		
	}
	/*
	@Override
	public void movePageNTab(int posPage) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void finishActivity(Intent i) {
		// TODO Auto-generated method stub
		setResult(Activity.RESULT_OK,i);        // so that that expense can be removed from list
		// after returning result
//Log.v(TAG, "Result set has been done. List position "+pos);

		finish() ;
	}
	
	*/


	@Override
	public void movePageNTab(int posPage) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if(KeyEvent.KEYCODE_HOME == keyCode || KeyEvent.KEYCODE_BACK == keyCode )
		{
			getSupportFragmentManager().beginTransaction().remove(ecf).commit() ;
			finish() ;
		}
		return super.onKeyDown(keyCode, event);
		//return true ;
	}

	
	
}
