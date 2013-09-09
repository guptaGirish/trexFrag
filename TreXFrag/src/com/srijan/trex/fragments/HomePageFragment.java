package com.srijan.trex.fragments;

import java.util.ArrayList;

import com.srijan.trex.R;
import com.srijan.trex.R.id;
import com.srijan.trex.R.layout;
import com.srijan.trex.activities.HomePageActivity;
import com.srijan.trex.activities.HomePageActivity.MethodCallBackHomePage;
import com.srijan.trex.adapters.DbAdapter;
import com.srijan.trex.adapters.UnreviewedExpenseDbAdapter;
import com.srijan.trex.listingunreviewedtags.CustomArrayAdapter;
import com.srijan.trex.listingunreviewedtags.UnreviewedTagObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

public class HomePageFragment extends Fragment implements HomePageActivity.MethodCallBackHomePage{

	View viewer ;
	ListView tagList ;
	Button addNewExpenseTag ;
	private ArrayList<UnreviewedTagObject> list ;
	private CustomArrayAdapter cad;
	private String TAG = "HomePageFragment" ;
	public static int EXPENSE_COMPLETE_CODE = 4 ;
	Activity activity ;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity = activity ;
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		activity = null ;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		viewer = inflater.inflate(R.layout.fragment_home_page, container, false) ;
		 
		 //createDatabase();
		 
		initializeControls();
	      
		 populateTagList();    
	        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	 		return viewer ;
	}


	/*
	 * This method is to initialize views of associated xml layout 
	 * 
	 * */
		private void initializeControls() {
			
			//Log.v(TAG,"In initializeControls ");
	    	
	    	tagList = (ListView)viewer.findViewById(R.id.tag_list);  	
	    	addNewExpenseTag = (Button)viewer.findViewById(R.id.add_new_expense_tag);
	    //	addNewExpense.setOnClickListener(onClickListener);
	    	//viewCategorizedExpense = (Button)viewer.findViewById(R.id.view_categorized_expense);
	    	//viewCategorizedExpense.setOnClickListener(onClickListener);
	    	//settings = (Button)viewer.findViewById(R.id.settings);
	    	//settings.setOnClickListener(onClickListener);
			
		}

		
		
			/*
			 * This method is to populate listView with all uncomplete tagged expenses stored in DB
			 * */

			private void populateTagList() {
				
				//Log.v(TAG,"In populateTagList");
				
				developTagListArray() ;
				cad = new CustomArrayAdapter(activity, R.layout.layout_tagged_expense, list);
				tagList.setAdapter(cad);
				
				
				
			}
			/*
			 * This method is to develop ArrayList of objectst of tagged expenses for Listview 
			 * 
			 * */
			
			void developTagListArray()
			{
				UnreviewedExpenseDbAdapter udb = new UnreviewedExpenseDbAdapter(activity);
				udb.open();
				Cursor c = udb.fetchAllExpensesTags();
				list = new ArrayList<UnreviewedTagObject>();
				
				if(c != null)
				{
					if(c.getCount() > 0)
					{
							//int nEntries = c.getCount() ;
							c.moveToFirst() ;
							//c.moveToNext();
							do{
								int id = c.getInt(0);
								String tag = c.getString(1);
								long timestamp = c.getLong(2);
								UnreviewedTagObject uob = new UnreviewedTagObject(id, tag, timestamp);
								list.add(uob);
								//Log.v(TAG,"id is "+id+", tag is "+tag+", timestamp is "+timestamp);
								
							}while(c.moveToNext());
							
					}
					udb.close();
					c.close() ;
				}
				
				
			}
			
			/*
			 * This callback is to update Unreviewed expenses tags list, when one of them gets completed
			 *
			 * */
/*			
			@Override
			public void onActivityResult(int requestCode, int resultCode, Intent data) {
			
				super.onActivityResult(requestCode, resultCode, data);
				
				Log.v(TAG,"In onActivityResult");
				//Log.v(TAG,"request code is "+requestCode+" , result code is "+resultCode);
				if(requestCode == EXPENSE_COMPLETE_CODE)
				{
					if(resultCode == Activity.RESULT_OK)
					{
						Log.v(TAG,"In processing partt");
						
						int pos = data.getIntExtra(ExpenseCompleteFragment.POSITION_LIST, -1) ;
						//Log.v(TAG, "In onActivityResult: position in list is "+pos) ;
						if(pos != -1)
						{
							Log.v(TAG, "In onActivityResult: List content is - "+list.get(pos).getTag().toString()) ;
							list.remove(pos) ;
							cad.notifyDataSetChanged();
							
						}
						
					}
					
				}
				
				
				
			}


	*/		
			public void updateTagList(int pos) {
				// TODO Auto-generated method stub
				if(pos != -1)
				{
					//Log.v(TAG, "In onActivityResult: List content is - "+list.get(pos).getTag().toString()) ;
					list.remove(pos) ;
					cad.notifyDataSetChanged();
					
				}

				
			}


			

			
			/*
			 * Listener to start related Activities of button -
			 *  - Add New Expense
			 *  - View Categoried Expense
			 *  - Settings
			 *  
			 * */
			
			
			/*

			OnClickListener onClickListener = new OnClickListener() {
				
				Intent i = new Intent() ;
				
				@Override
				public void onClick(View v) {
					switch(v.getId())
					{
						case R.id.add_new_expense :
						{
							i.setClass(activity,com.srijan.trex.ExpenseCompleteFragment.class );
							i.putExtra(ExpenseCompleteFragment.ACTION, ExpenseCompleteFragment.ADD_NEW_ACTION);
							activity.startActivity(i);
							break ;
						}
						case R.id.view_categorized_expense :
						{
							i.setClass(activity,com.srijan.trex.CategoryListingActivity.class );
							activity.startActivity(i);
							break ;
						}
						case R.id.settings :
						{
							i.setClass(activity,com.srijan.trex.SettingsActivity.class );
							activity.startActivity(i);
							break ;
						}
					}
					
				
					
				}
				
			};
			
			*/

	
}
