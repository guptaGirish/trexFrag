package com.srijan.trex;

import com.srijan.trex.listingexpense.ExpenseArrayAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.ExpandableListAdapter;

public class CategoryExpenseActivity extends FragmentActivity implements ExpenseArrayAdapter.ListUpdateCallBack
{

String TAG = "CategoryExpenseActivity" ;
	
UpdatesInCategoryExpenseElements updateCallback = null ;
public static interface UpdatesInCategoryExpenseElements{
	
	public int getCategoryId() ;
	public void onModifyExpenseInList() ;
	public void onUpdateList(int id, String name, int cid, float amount,
			long timeStamp, int flag);
	
	
}	

CategoryExpenseFragment ecf ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.v(TAG, "Line 1") ;
		setContentView(R.layout.activity_category_expense);
		Log.v(TAG, "Line 2") ;
		ecf = (CategoryExpenseFragment)getSupportFragmentManager().findFragmentById(R.id.category_expenses);
		
		updateCallback = (UpdatesInCategoryExpenseElements)ecf ;
		//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE); // calling soft input keyboard
	}

	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == CategoryExpenseFragment.UPDATE_EXPENSE)
		{
			if(resultCode == Activity.RESULT_OK)
			{
				updateCallback.onModifyExpenseInList() ;
				//populateExpenseList(cId) ;
			}
			
			
		}
		
	}
	
	

	@Override
	public void onUpdateList(int id, String name, int cid, float amount,
			long timeStamp, int flag) {
		// TODO Auto-generated method stub
		updateCallback.onUpdateList(id, name, cid, amount, timeStamp, flag );
	}


	@Override
	public int getCategoryId() {
		// TODO Auto-generated method stub
		return updateCallback.getCategoryId() ;
		
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if(KeyEvent.KEYCODE_HOME == keyCode || KeyEvent.KEYCODE_BACK == keyCode )
		{
			getSupportFragmentManager().beginTransaction().remove(ecf).commit();
			finish() ;
		}
		return super.onKeyDown(keyCode, event);
		//return true ;
	}

	
}
