package com.srijan.trex;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import com.srijan.trex.adapters.CategoryDbAdapter;
import com.srijan.trex.adapters.ExpenseDbAdapter;
import com.srijan.trex.adapters.UnreviewedExpenseDbAdapter;
import com.srijan.trex.listingcategory.CategoryArrayAdapter;
import com.srijan.trex.listingcategory.CategoryObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.AvoidXfermode;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ExpenseCompleteFragment extends Fragment{

	private String TAG = "ExpenseCompleteFragment" ;

	

    static int GET_CATEGORY_CODE = 2 ;

    private String PAGE_HEADER = "page header" ;
	private String EDIT_HEADER = "Complete Your Expense" ;
	private String ADD_NEW_HEADER = "Add New Expnese" ;
    private String CATEGORY_LIST = "category_list" ;
    private String CATEGORY_OBJECT_LIST = "category_object_list" ;
    
    
    public static String ACTION = "operation" ;
    public static String EDIT_ACTION = "complete_expense" ;
    public static String ADD_NEW_ACTION = "add_new_expense" ;
    public static String EXPENSE_TAG = "expense_tag" ;
    public static String EXPENSE_AMOUNT = "expense_amount" ;
    private static String POSITION_SPINNER = "position_spinner" ;
    public static String TIME_STAMP = "timestamp" ;
    public static String EXPENSE_ID = "id" ;
    public static String POSITION_LIST = "position" ; // position of passed Unreviewed expense
     											      //in Unreviewed Expense List
    private ArrayList<CategoryObject> colist ;
    private ArrayList<String> clist ;
    private CategoryArrayAdapter cap ;
    private long timeStamp ;
    private int tagId = -1 ;
	private int pos = -1 ;
	private int posSpinner = -1 ; // to maintain the position when orientation change occurs
	private int mYear, mMonth, mDay, mHour, mMinute, mAM_PM ;
	
	View viewer ;
	FragmentActivity activity ;
	MethodCallback mCallBack ;
	
	public static interface MethodCallback{
		public void movePageNTab(int posPage) ;
	//	public void updateTagList(int position) ;
		//public void updateViewPagerAdd() ;
		//public void finishActivity(Intent i);
		
	}
	
	
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity = (FragmentActivity)activity ;
		mCallBack = (MethodCallback)activity ;
		if(activity == null)
		{
			Log.v(TAG,"Acvtivity reference not received.") ;
		}
		
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		activity = null ;
		mCallBack = null ;
	}

	Button addNewCategory,commitExpense, expenseDate, expenseTime  ;
	EditText expenseContent, amountExpense ;
	TextView pageHeader ;
    Spinner choosedCategory ;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		//setRetainInstance(true) ;
		
		Log.v(TAG, "Line 1") ;
		viewer = inflater.inflate(R.layout.fragment_expense_complete, container, false) ;
		initializeControls();
		
		String etag ;
		float eamt ;
		if(savedInstanceState != null) //  Extracting values when orientation of devices changes
		{  
			
			pageHeader.setText(savedInstanceState.getString(PAGE_HEADER)) ;	
			timeStamp = Long.parseLong(savedInstanceState.getString(TIME_STAMP));
			//expenseContent.setText(savedInstanceState.getString(EXPENSE_TAG)) ;
			//amountExpense.setText(savedInstanceState.getString(EXPENSE_AMOUNT)) ;
			etag =savedInstanceState.getString(EXPENSE_TAG) ;
			//eamt = savedInstanceState.getString(EXPENSE_AMOUNT) ;
			eamt = Float.parseFloat(savedInstanceState.getString(EXPENSE_AMOUNT)) ;
			tagId = Integer.parseInt(savedInstanceState.getString(EXPENSE_ID)) ;
			
			posSpinner = Integer.parseInt(savedInstanceState.getString(POSITION_SPINNER));
			
			clist = (ArrayList<String>)savedInstanceState.getStringArrayList(CATEGORY_LIST) ;
			colist = savedInstanceState.getParcelableArrayList(CATEGORY_OBJECT_LIST);
			pos = Integer.parseInt(savedInstanceState.getString(POSITION_LIST)) ;
			
			//ValuesAssignmentToControls(etag, Float.parseFloat(eamt)) ;
		}
		else // Extracting values of tagged_expense and filling under related views to complete it
		{
			
		
			
			Intent i = activity.getIntent();
			String action  ;
					
					action = i.getStringExtra(ACTION);
			
					if(action == null)
					{
						action = "" ;
						Log.v(TAG, "action is null") ;
						
					}
			  	//Bundle b = getArguments();
				
			  
			if(action.equals(EDIT_ACTION))
				{
					
					pageHeader.setText(EDIT_HEADER);
					
					etag = i.getStringExtra(EXPENSE_TAG);
					eamt = Float.parseFloat(i.getStringExtra(EXPENSE_AMOUNT)) ;
					//eamt = i.getStringExtra(EXPENSE_AMOUNT) ;
					timeStamp = Long.parseLong(i.getStringExtra(TIME_STAMP));
					//expenseContent.setText(etag) ;
					//amountExpense.setText(amt) ;
					//ValuesAssignmentToControls(etag, Float.parseFloat(amt)) ;
					tagId = Integer.parseInt(i.getStringExtra(EXPENSE_ID));
					pos = Integer.parseInt(i.getStringExtra(POSITION_LIST));
					
				}
				else //If new expense is needed to be added
				{
					etag = "" ;
					eamt =  0f ;
					pageHeader.setText(ADD_NEW_HEADER) ;
					
					Calendar c = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault()) ;
					timeStamp = c.getTimeInMillis();
						
				}
		
				//Log.v(TAG,"");
				
		
		}
		
		ValuesAssignmentToControls(etag, eamt ) ;
		//populateCategorySpinner(this.posSpinner) ; // populate the spinner with all available categories 
		
		//updateDateValuesWithTimeStamp() ; // Update date String and values - mYear, mMonth, mDay 
		//updateTimeValuesWithTimeStamp() ; // Update time String and values - mHour, mMinute, mAM_PM
		
		
		
		
		return viewer ;
	}
	
	private void ValuesAssignmentToControls(String eTag, float eAmt) {
		// TODO Auto-generated method stub
		//pageHeader.setText("Update Expense") ;
		expenseContent.setText(eTag) ;
		
		if(eAmt > 0)
		{
			amountExpense.setText(new StringBuilder().append(eAmt)) ;
		}
		//updateExpense.setText("Update");
		populateCategorySpinner(posSpinner);
		
		updateDateValuesWithTimeStamp() ; // Update date String and values - mYear, mMonth, mDay 
		updateTimeValuesWithTimeStamp() ; // Update time String and values - mHour, mMinute, mAM_PM
		
	}
	

	/*
	 * This method is to update values related to Date 
	 * 
	 * */
	
	
	private void updateDateValuesWithTimeStamp()
	{
		Calendar c = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault()) ;
		c.setTimeInMillis(timeStamp) ;
		
		mYear = c.get(Calendar.YEAR) ;
		mMonth = c.get(Calendar.MONTH) ;
		mDay = c.get(Calendar.DAY_OF_MONTH) ;
		expenseDate.setText(new StringBuilder().append(mDay).append("/").append(mMonth+1).append("/").append(mYear)) ;
		
		
	}
	
	/*
	 * This method is to update values related to Date
	 * (called from DatePicker Listener's onDateSet() 
	 * 
	 * */
	
	
	private void updateDateValuesWithTimeStamp(int year, int month, int day)
	{
		
		Calendar c = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault()) ;
		mYear = year ;
		mMonth = month ;
		mDay = day ;
		
		c.set(Calendar.YEAR, mYear) ;
		c.set(Calendar.MONTH, mMonth) ;
		c.set(Calendar.DAY_OF_MONTH, mDay) ;
		c.set(Calendar.HOUR, mHour) ;
		c.set(Calendar.MINUTE, mMinute) ;
		c.set(Calendar.AM_PM, mAM_PM) ;
		
		timeStamp = c.getTimeInMillis() ;
		
		expenseDate.setText(new StringBuilder().append(mDay).append("/").append(mMonth+1).append("/").append(mYear)) ;
			
	}

	/*
	 * This method is to update values related to Date 
	 * 
	 * */	
	private void updateTimeValuesWithTimeStamp()
	{
		Calendar c = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault()) ;
		c.setTimeInMillis(timeStamp) ;
		
		mHour = c.get(Calendar.HOUR) ;
		mMinute = c.get(Calendar.MINUTE) ;
		mAM_PM = c.get(Calendar.AM_PM) ;
		
		
		
		
		if(mAM_PM == 0)
			expenseTime.setText(new StringBuilder().append(mHour).append(":").append(mMinute).append(" AM")) ;
		else 
			expenseTime.setText(new StringBuilder().append(mHour).append(":").append(mMinute).append(" PM")) ;
					
	}
	/*
	 * This method is to update values related to Date
	 * (called from DatePicker Listener's onDateSet() 
	 * 
	 * */
	
	private void updateTimeValuesWithTimeStamp(int hourofday, int minute)
	{
		Calendar c = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault()) ;
		c.set(Calendar.HOUR_OF_DAY,hourofday) ;
		
		mHour = c.get(Calendar.HOUR) ;
		mMinute = minute ;
		mAM_PM = c.get(Calendar.AM_PM) ;
		
		c.set(Calendar.YEAR, mYear) ;
		c.set(Calendar.MONTH, mMonth) ;
		c.set(Calendar.DAY_OF_MONTH, mDay) ;
		c.set(Calendar.HOUR, mHour) ;
		c.set(Calendar.MINUTE, mMinute) ;
		c.set(Calendar.AM_PM, mAM_PM) ;
		
		timeStamp = c.getTimeInMillis() ;
		
		if(mAM_PM == 0)
			expenseTime.setText(new StringBuilder().append(mHour).append(":").append(mMinute).append(" AM")) ;
		else 
			expenseTime.setText(new StringBuilder().append(mHour).append(":").append(mMinute).append(" PM")) ;
		
	}
	
	/*
	 * Implementation of onSavedInstanceState() callback to deal with orientation change
	 * so that values in views fields can be maintained while completing expense form 
	 * */
	
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putString(PAGE_HEADER, pageHeader.getText().toString());
		outState.putString(EXPENSE_ID, new StringBuilder().append(tagId).toString()) ;
		outState.putString(EXPENSE_TAG, expenseContent.getText().toString()) ;
		outState.putString(EXPENSE_AMOUNT, amountExpense.getText().toString()) ;
		outState.putString(POSITION_SPINNER, new StringBuilder().append(choosedCategory.getSelectedItemPosition()).toString());
		outState.putString(TIME_STAMP, new StringBuilder().append(timeStamp).toString()) ;
		
		outState.putStringArrayList(CATEGORY_LIST, clist) ;
		outState.putParcelableArrayList(CATEGORY_OBJECT_LIST, colist) ;
		outState.putString(POSITION_LIST, new StringBuilder().append(pos).toString()) ;
		super.onSaveInstanceState(outState);
		
		
	}

/*
 * This method is to populate spinner with all available categories
 * 
 * */

	private void populateCategorySpinner(int posSpinner) {
		// TODO Auto-generated method stub
		if(posSpinner != -1)
		{
			
			//when orientation change occurs then this is used to populate spinner
			 
			cap = new CategoryArrayAdapter(activity, android.R.layout.simple_spinner_item, clist);
			cap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			choosedCategory.setAdapter(cap);
			choosedCategory.setSelection(posSpinner) ;
		}
		else
		{
			
			 // When Activity is started first time
			 
			developCategoryList();
			cap = new CategoryArrayAdapter(activity, android.R.layout.simple_spinner_item, clist);
			cap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			choosedCategory.setAdapter(cap);
		}
		
		
	}
	
	
	/*
	 * This method is to initialize all views available in related xml layout of current activity
	 * */
	
	private void initializeControls() {
		// TODO Auto-generated method stub
		
		 pageHeader = (TextView)viewer.findViewById(R.id.page_header) ;
		 choosedCategory = (Spinner)viewer.findViewById(R.id.choosed_Category);	 
		 addNewCategory = (Button)viewer.findViewById(R.id.add_new_category);
		 expenseContent = (EditText)viewer.findViewById(R.id.expense_content) ;
		 amountExpense = (EditText)viewer.findViewById(R.id.amount_expense);
		 commitExpense = (Button)viewer.findViewById(R.id.commit_expense) ;
		 expenseDate = (Button)viewer.findViewById(R.id.expense_date) ;
		 expenseTime = (Button)viewer.findViewById(R.id.expense_time) ;
		 
		 expenseContent.requestFocus() ;
		 activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE); // calling soft input keyboard
		  //To change date, implementing DatePicker
		 
			 expenseDate.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
						
					DatePickerDialog dpd = new DatePickerDialog(activity, mDListener, mYear, mMonth, mDay) ;
					dpd.show() ;
					
				}
			});

			  // To change Time, implementing TimePicker		 
			 
			 expenseTime.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
					TimePickerDialog tpd = new TimePickerDialog(activity, mTListener, mHour, mMinute, false) ;
					tpd.show() ;
				}
			});
			  		 
			 //To add new category 
			 
			 addNewCategory.setOnClickListener(new  View.OnClickListener() {
				@Override
				public void onClick(View v) {
				
					Intent i = new Intent();
					i.setClass(activity, AddNewCategoryActivity.class);
					startActivityForResult(i, GET_CATEGORY_CODE); // Start activity to get new category		
				}
			});

			 // To commit new expense in the database
			
			commitExpense.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					
						posSpinner = choosedCategory.getSelectedItemPosition() ;
						int cId = colist.get(posSpinner).getCategoryId() ; // Extracting category Id of expense
						String cName = colist.get(posSpinner).getCategoryName() ;
						//Log.v(TAG,"On commit, Category id is - "+cId+" , Category Name is - "+cName) ;			
						String expenseTag = expenseContent.getText().toString().trim() ;
					//	Log.v(TAG,"On commit, Expense amount is " +amountExpense.getText().toString()) ;	
						String eAmt  = amountExpense.getText().toString().trim() ; // Trimming of fetched String value expense amount 
						float expenseAmt = 0.0f ;
						
						if(eAmt.length() > 0)   
							expenseAmt = Float.parseFloat(eAmt) ;
						
					
					if(expenseTag.length()>0 && eAmt.length() > 0) // checked to see whether all elements of form are filled 
					{
						
						ExpenseDbAdapter edb = new ExpenseDbAdapter(activity) ;
						edb.open();
						
						long x = edb.insertExpense(expenseTag, expenseAmt, cId,timeStamp );
						edb.close() ;
							if(x!= -1) // check to see if expense inserted successfully
							{
								
								//Log.v(TAG,"Expense inserted successfully");
								if(tagId != -1) //check to see if inserted expense was tagged expense of not
								{
									 // If inserted expense was tagged expense then it is needed to be remove from
									 // unreviewed expenses DB table
									
										UnreviewedExpenseDbAdapter udb = new UnreviewedExpenseDbAdapter(activity);
										udb.open() ;
										boolean r = udb.deleteExpenseTag(tagId);
										udb.close();
										if(r)
										{
											
											//activity.getSupportFragmentManager().beginTransaction().
											//.r(arg0)(arg0)("ExpenseComplete") ;
											//mCallBack.updateViewPagerAdd() ;
											//mCallBack.updateTagList(pos);
											//Log.v(TAG, "Deleted : Tagged Expense with id "+tagId);
											Intent i = new Intent() ;
											
											i.putExtra(POSITION_LIST, pos) ; // Setting position of tagged expense in unreviewed tag list  
											
											//mCallBack.finishActivity(i) ;
											activity.setResult(Activity.RESULT_OK,i);        // so that that expense can be removed from list
																			// after returning result
											//Log.v(TAG, "Result set has been done. List position "+pos);
											
											activity.finish() ;
										}
										else
										{
											
											activity.setResult(Activity.RESULT_CANCELED);
											activity.finish() ;
										}
										
										
								}
								else
								{
									expenseContent.setText("") ;
									amountExpense.setText("") ;
									
									
									mCallBack.movePageNTab(0);
									//activity.setResult(Activity.RESULT_OK);
									//activity.finish() ;
								}
							}
							else
							{
								activity.setResult(Activity.RESULT_CANCELED);
								activity.finish();
							}
						
						
					}	
					
					
				else
				{
					Toast.makeText(activity, "Complete Expense Details", Toast.LENGTH_SHORT).show() ;
					
				}
					
				
				}
			}) ;
		
		 
		 
	}
	
	
	// Creation of Listener for DatePicker so that Date values can be updated	
		private DatePickerDialog.OnDateSetListener  mDListener = new DatePickerDialog.OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				updateDateValuesWithTimeStamp(year, monthOfYear, dayOfMonth) ;
				
			}
		};
		
		
		// Creation of Listener for TimePicker so that Time values can be updated
		private TimePickerDialog.OnTimeSetListener mTListener = new TimePickerDialog.OnTimeSetListener() {
			
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				updateTimeValuesWithTimeStamp(hourOfDay, minute) ;
			}
		};
		
		
		
		/*
		 * This callback is implemented to get new category and inserting new category into
		 * category list DB table
		 * */ 
		
		
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			if(requestCode == GET_CATEGORY_CODE)
			{
				if(resultCode == Activity.RESULT_OK)
				{
					
					String cname = data.getStringExtra(AddNewCategoryActivity.CATEGORY_NAME);			
					if(cname.length()>0)
					{
						CategoryDbAdapter cdb = new CategoryDbAdapter(activity);
						cdb.open() ;
						long x = cdb.insertCategory(cname);
						if(x!= -1){
							//Log.v(TAG, "In onActivityResult, category inserted") ;
							ModifyCategoryList(cname);
						}
					}
				}
				
				
			}
				
			
			
		}

	/*
	 * This method is used to modify the category listing in spinner when new category is inserted
	 * 
	 * */
		private void ModifyCategoryList(String cname) {

			if(colist.size() >=1 ) // If there is already some categories are available
			{	 
				CategoryObject cob = colist.get(colist.size()-1) ;
				int id = cob.getCategoryId() + 1 ;
				clist.add(cname) ;
				
				colist.add(new CategoryObject(id, cname));
				cap.notifyDataSetChanged();
				int s = colist.size();
				//choosedCategory.setSelection(choosedCategory.getLastVisiblePosition()) ;
				choosedCategory.setSelection(s-1) ;
			}
			else if(colist.size() == 0 ) // If inserted category is first category
			{
				
				  CategoryDbAdapter cdb = new CategoryDbAdapter(activity) ;
				  cdb.open() ;
				  Cursor c = cdb.fetchAllCategories() ;
				  c.moveToFirst() ;
				  int cid = c.getInt(0);
				  cdb.close() ;
				  
				  colist.add(new CategoryObject(cid, cname));
				  clist.add(cname) ;
				  cap.notifyDataSetChanged() ;
				  
				  
			}
			
		}

		
		/*
		 * To fill array list with categories available in database
		 * 
		 * */

		private void developCategoryList() {
			
			colist = new ArrayList<CategoryObject>();
			clist = new ArrayList<String>();
			CategoryDbAdapter cdb = new CategoryDbAdapter(activity) ;
			cdb.open();
			Cursor c = cdb.fetchAllCategories() ;
			if(c != null)
			{
				if(c.getCount()>0)
				{
					c.moveToFirst();
					do{
						int cid = c.getInt(0); 
						String cname = c.getString(1) ;
						CategoryObject co = new CategoryObject(cid, cname);
						colist.add(co);
						clist.add(cname);
						
					}while(c.moveToNext());
					
				}
				
			}
			
			cdb.close();
		}
		
	
	

}
