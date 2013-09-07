package com.srijan.trex;

import java.util.ArrayList;

import com.srijan.trex.adapters.ExpenseDbAdapter;
import com.srijan.trex.listingexpense.ExpenseArrayAdapter;
import com.srijan.trex.listingexpense.ExpenseObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class CategoryExpenseFragment extends Fragment implements CategoryExpenseActivity.UpdatesInCategoryExpenseElements{
	
	
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		activity = null ;
	}


	public static int UPDATE_EXPENSE = 2 ;
	public static String CATEGORY_ID = "cid" ;
	public static String CATEGORY_NAME = "cname" ;
	private ArrayList<ExpenseObject> UsExpenseArrayList, SExpenseArrayList ;
	private ListView UsExpenseList, SExpenseList ;
	private TextView categoryName ;
	private Button sendMail ;
	private ExpenseArrayAdapter UsEap,SEap ;
	static int cId ;
	static String cName ;
	private String TAG = "CategoryExpenseFragment";
	
	
	
	View viewer ;
	Activity activity ;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity = activity ;
	}
	
	
	/*
	 * This callback implementation is to assign xml layout to activity and 
	 * initiating population of ListView
	 * 
	 * */
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		viewer = inflater.inflate(R.layout.fragment_category_expense, container, false) ;
		//Log.v(TAG,"");
		initializeControls() ;  // initialize view elements
		
		Intent i = activity.getIntent() ;
		cId = Integer.parseInt(i.getStringExtra(CATEGORY_ID)) ; // Extract category Id from intent 
		cName = i.getStringExtra(CATEGORY_NAME) ; // Extract category Name from intent 
		
		initializeControls() ;  // initialize view elements
		populateExpenseList(cId) ;  // populate expenses list of the asked category 
		return viewer ;
		
	}
	
	
	

	
	/*
	 * This method is to populate the lists with all expenses of asked category.
	 * 
	 * 2 separate Lists are for - 
	 *  - Settled Expenses
	 *  - Unsettled Expenses
	 * 
	 * */
	
	private void populateExpenseList(int cId) {
		
		
		fillArrayLists(cId);	// Fill the arraylists with expense details
		
		
		
		//  Associate the created arraylists of expenses with their relative adapters 
		 
		UsEap = new ExpenseArrayAdapter(activity, R.layout.layout_expense, UsExpenseArrayList);
		SEap = new ExpenseArrayAdapter(activity, R.layout.layout_expense, SExpenseArrayList);
		
		
		 //  Set adapters to relative Listviews 
		
		UsExpenseList.setAdapter(UsEap) ;
		SExpenseList.setAdapter(SEap);
		
	}

	
	
	/*
	 * This method is to fill the ArrayLists with objects of expenses stored in the DB 
	 * which are associated with cId.
	 * 
	 * In this first all expenses of 'cId' are fetch from DB then
	 * iterating through all fetched row, settle flag of every row is checked
	 * to know whether it is settled or unsettled expense and based on that flag
	 * that expense row's content in the form of object is added to related ArrayList 
	 * 
	 * 
	 * */
	
	private void fillArrayLists(int cId)
	{
		
		ExpenseDbAdapter edb = new ExpenseDbAdapter(activity) ;
		edb.open() ;
		Cursor c = edb.fetchAllExpenses(cId);
		SExpenseArrayList = new ArrayList<ExpenseObject>();
		UsExpenseArrayList = new ArrayList<ExpenseObject>();
		if(c!=null)
		{
			if(c.getCount() > 0)
			{
				c.moveToFirst() ;
				do{
					int id = c.getInt(0);
					String name = c.getString(1);
					float amount = c.getFloat(2);
					int cid = c.getInt(3);
					long timeStamp = c.getLong(4);
		//			Log.v(TAG, "id is "+id+", name is "+name+", Amt is "+amount+", cid is "+cid+", timeStamp is "+timeStamp);
					
					int settleFlag = c.getInt(5) ;
					
					ExpenseObject eo =  new ExpenseObject(id, name, cid, amount, timeStamp,settleFlag);
					
					// If settleFlag is 0 it means expense is not settled
					
					if(settleFlag == 0)
					    UsExpenseArrayList.add(eo) ;  
					else
						SExpenseArrayList.add(eo);
					
				}while(c.moveToNext()) ;
				
				
			}
			
			
		}
		
		c.close() ;
		edb.close() ;
		
	}
	
	
	/*
	 * This method is to initialize all views available in associated xml of 
	 * current activity
	 * 
	 * */
	
	private void initializeControls() {
		// TODO Auto-generated method stub
		
		UsExpenseList = (ListView)viewer.findViewById(R.id.us_expense_list) ;
		SExpenseList  = (ListView)viewer.findViewById(R.id.s_expense_list) ;
 		categoryName = (TextView)viewer.findViewById(R.id.category_name) ;
		sendMail = (Button)viewer.findViewById(R.id.send_mail) ;
		
		
		categoryName.setText(cName);
		
		
		 //Implementing the functionality associated with sendMail Button
		
		sendMail.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
		
				
			}
		});
		
	}

	/*
	 *If one expense is settled from unsettled expenses lists then this function is
	 * called to update the settled expenses lists by adding that expense to settled 
	 * expenses list which has been removed from unsettled expenses list
	 *
	 * This method is called from 'ExpenseArrayAdapter' using instance of 
	 * 'ListUpdateCallBack' interface
	 * 
	 * 
	 * */
	

	@Override
	public void onUpdateList(int id,String name, int cid, float amount, long timeStamp,int flag ) {
		
		//Log.v(TAG, "On Updation of both lists");
		ExpenseObject eo = new ExpenseObject(id, name, cid, amount, timeStamp, flag) ;
		//Log.v(TAG, "before adding object to list");
		SExpenseArrayList.add(eo) ;
		//Log.v(TAG, "before notifying adapter");
		SEap.notifyDataSetChanged() ;
		
		
	}

	/*
	 * This function is used to provide cId to ExpenseArrayAdapter using instance of 
	 * 'ListUpdateCallBack' interface 
	 * 
	 * */
		
	@Override
	public int getCategoryId() {
		// TODO Auto-generated method stub
		return cId;
	}

	
	
	/*
	 * This callback method is to update the expense list with updated expenses.
	 * 
	 * */

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == UPDATE_EXPENSE)
		{
			if(resultCode == Activity.RESULT_OK)
			{
				
				populateExpenseList(cId) ;
			}
			
			
		}
		
	}


	@Override
	public void onModifyExpenseInList() {
		// TODO Auto-generated method stub
		populateExpenseList(cId) ;
		
	}

	
	

}
