package com.srijan.trex.activities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import viewpageradapter.PagerAdapter;

import com.srijan.trex.R;
import com.srijan.trex.R.drawable;
import com.srijan.trex.R.id;
import com.srijan.trex.R.layout;
import com.srijan.trex.adapters.DbAdapter;
import com.srijan.trex.fragments.CategoryListingFragment;
import com.srijan.trex.fragments.ExpenseCompleteFragment;
import com.srijan.trex.fragments.HomePageFragment;
import com.srijan.trex.fragments.SettingsFragment;
import com.srijan.trex.listingunreviewedtags.CustomArrayAdapter;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.AvoidXfermode;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.Toast;
/*
 * 
 * This is the welcome Activity of App.
 * This activity lists all tagged expenses
 * and provide 3 buttons to 
 *  - add new category
 *  - manage categories
 *  - settings
 * 
 * */
import android.widget.TabHost.TabSpec;


public class HomePageActivity extends FragmentActivity implements TabHost.OnTabChangeListener,ViewPager.OnPageChangeListener,
 					ExpenseCompleteFragment.MethodCallback{
	
	
	
	String TAG = "HomePageActivity" ;
	static Activity activity ;//= HomePageActivity.this ;
	private TabHost mTabHost;
    private ViewPager mViewPager;
    private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, HomePageActivity.TabInfo>();
    private PagerAdapter mPagerAdapter;
	
    static MethodCallBackHomePage mCallBackHomePage ;
    
    
public static interface MethodCallBackHomePage{
		
		public void updateTagList(int pos);
		
	}
	
    
    
    private class TabInfo {
        private String tag;
        private Class<?> clss;
        private Bundle args;
        private Fragment fragment;
        TabInfo(String tag, Class<?> clazz, Bundle args) {
            this.tag = tag;
            this.clss = clazz;
            this.args = args;
        }

   }
    
    /**
     * A simple factory that returns dummy views to the Tabhost
     * @author mwho
     */
    class TabFactory implements TabContentFactory {
 
        private final Context mContext;
 
        /**
         * @param context
         */
        public TabFactory(Context context) {
            mContext = context;
        }
 
        /** (non-Javadoc)
         * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
         */
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
 
    }
    
    List<Fragment> fragments = null;
    String FRAGMENT_LIST = "fragments_list";
    
	@SuppressWarnings("unchecked")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        
        
  /*
        if(savedInstanceState != null)
        {
        	ParcelableObject po = savedInstanceState.getParcelable(key)(FRAGMENT_LIST); 
        	mCallBackHomePage =  
        }
        
    */    
        activity = HomePageActivity.this ;
        
        
        createDatabase() ;
        //activity = (Activity) getApplicationContext() ;
        setContentView(R.layout.activity_home_page);
        
        //mCallBackHomePage = (MethodCallBackHomePage)this;
        Log.v(TAG,"In onCreate");
        mTabHost = (TabHost)findViewById(R.id.tabHost) ;
        mTabHost.setup() ;
        
        
        mViewPager = (ViewPager)findViewById(R.id.viewpager) ;
        
        initializeTabHost();
        initializeViewPager() ;
        
    }

	Fragment f  ;
	private void initializeViewPager() {
		// TODO Auto-generated method stub
		Log.v(TAG,"In initializeViewPager");
		

		if(fragments == null)
		{
			fragments = new Vector<Fragment>();
			/*fragments.add(Fragment.instantiate(activity, ExpenseCompleteFragment.class.getName()));
			fragments.add(Fragment.instantiate(activity, CategoryListingFragment.class.getName()));
			fragments.add(Fragment.instantiate(activity, ExpenseCompleteFragment.class.getName()));
			*/
				
			f = Fragment.instantiate(activity, HomePageFragment.class.getName()) ;
			mCallBackHomePage = (MethodCallBackHomePage)f ;
			fragments.add(f);
			
			fragments.add(Fragment.instantiate(activity, ExpenseCompleteFragment.class.getName()));
			fragments.add(Fragment.instantiate(activity, CategoryListingFragment.class.getName()));
			fragments.add(Fragment.instantiate(activity, SettingsFragment.class.getName()));

		}
			this.mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);
		
//		String t = fragments.get(3).getTag() ;
	//	Log.v(TAG, t);
		this.mViewPager.setAdapter(this.mPagerAdapter) ;
		Log.v(TAG,"After setting Adapater to ViewPager");
		this.mViewPager.setOnPageChangeListener(this) ;
		
		
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		
		// ParcelableObject po = new ParcelableObject(mCallBackHomePage) ;
		
		// outState.putSerializable(FRAGMENT_LIST, (Serializable)po);
		
		
		
		//outState.putParcelableArrayList(FRAGMENT_LIST, (ArrayList<? extends Parcelable>) fragments) ;
		super.onSaveInstanceState(outState);
	}


	private void initializeTabHost() {
		// TODO Auto-generated method stub
		
		Log.v(TAG,"In initializeTabHost");
	TabInfo tabInfo = null ;
	
	tabInfo = new TabInfo("HomePage", HomePageFragment.class, null);
	
	ImageView i1 = 	new ImageView(activity) ;
	i1.setBackgroundResource(R.drawable.footer_home_inactive) ;

	
	
	
	TabSpec ts = mTabHost.newTabSpec("Home")
				//.setIndicator("Home",getResources().getDrawable(R.drawable.footer_home_inactive))				
				.setIndicator("Home")
				.setContent(new TabFactory(activity)) ;
	mTabHost.addTab(ts) ;
	
	mapTabInfo.put(ts.getTag(), tabInfo) ;
	//i1 = null ;
	
	
	tabInfo = new TabInfo("Add Expense", ExpenseCompleteFragment.class, null);
		
	ImageView i2 = 	new ImageView(activity) ;
	i2.setBackgroundResource(R.drawable.footer_add_inactive) ;

	ts = mTabHost.newTabSpec("Add Expense")
				//.setIndicator("Add",getResources().getDrawable(R.drawable.footer_add_inactive))
				.setIndicator("Add")
				.setContent(new TabFactory(activity)) ;
	mTabHost.addTab(ts) ;
	
	mapTabInfo.put(ts.getTag(), tabInfo) ;
	//i2 = null ;
	
	
	tabInfo = new TabInfo("Manage Categories",CategoryListingFragment.class , null) ;
	
	ImageView i3 = 	new ImageView(activity) ;
	i3.setBackgroundResource(R.drawable.footer_categories_inactive) ;

	ts = mTabHost.newTabSpec("Manage Categories") 
				 //.setIndicator("Categories",getResources().getDrawable(R.drawable.footer_categories_inactive))
				 .setIndicator("Categories")
				 .setContent(new TabFactory(activity)) ;
	mTabHost.addTab(ts) ;
	
	mapTabInfo.put(ts.getTag(), tabInfo) ;
	//i3 = null ;
	
	
	tabInfo = new TabInfo("Settings", SettingsFragment.class, null);
	
	ImageView i4 = 	new ImageView(activity) ;
	i4.setBackgroundResource(R.drawable.footer_settings_inactive) ;

	ts = mTabHost.newTabSpec("Settings")
				 //.setIndicator("Setting",getResources().getDrawable(R.drawable.footer_settings_inactive))
				 .setIndicator("Setting")
				 .setContent(new TabFactory(activity)) ;
				 
	mTabHost.addTab(ts) ;
				 
	mapTabInfo.put(ts.getTag(), tabInfo) ;
	//i4 = null ;
	mTabHost.setOnTabChangedListener(this) ;
	tabPosition = 0 ;
	//this.mTabHost.getCurrentTabView().setBackgroundColor(Color.parseColor("#333945")) ;
	
	Log.v(TAG,"Finishing Initializing Tabhost");
	}

static int tabPosition ;
	@Override
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		//TabInfo newTab = this.mapTabInfo.get(tag);
		Log.v(TAG,"In onTabChanged");
        int pos = this.mTabHost.getCurrentTab();
        //this.mTabHost.getChildAt(tabPosition).setBackgroundColor(Color.parseColor("#AAD418")) ;
        //this.mTabHost.setBackgroundColor(Color.parseColor("#AAD418")) ;
        //this.mTabHost.getTabWidget().setBackgroundColor(Color.parseColor("#AAD418")) ;
        //this.mTabHost.getCurrentView().setBackgroundColor(Color.parseColor("#AAD418")) ;
        //this.mTabHost.getCurrentTabView().setBackgroundColor(Color.parseColor("#333945")) ;
        this.mViewPager.setCurrentItem(pos);
        if(pos == 1)
        {
        	getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE); // calling soft input keyboard
        }
        
        
        tabPosition = pos ;
      
	}


	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		Log.v(TAG,"On onPageSelected");
		
			this.mTabHost.setCurrentTab(position);
			//this.mTabHost.getChildAt(tabPosition).setBackgroundColor(Color.parseColor("#AAD418")) ;
		//	this.mTabHost.setBackgroundColor(Color.parseColor("#AAD418")) ;
			//this.mTabHost.getTabWidget().setBackgroundColor(Color.parseColor("#AAD418")) ;
		//this.mTabHost.getCurrentView().setBackgroundColor(Color.parseColor("#AAD418")) ;
//		this.mTabHost.getCurrentTabView().setBackgroundColor(Color.parseColor("#333945")) ;
			
			if(position == 1)
	        {
	        	getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE); // calling soft input keyboard
	        }
			tabPosition = position ;
	}


	@Override
	public void movePageNTab(int posPage) {
		// TODO Auto-generated method stub
		Log.v(TAG,"In movePageNTab");
		this.mViewPager.setCurrentItem(posPage);
		this.mTabHost.setCurrentTab(posPage);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if(KeyEvent.KEYCODE_HOME == keyCode || KeyEvent.KEYCODE_BACK == keyCode )
		{
			finish() ;
		}
		return super.onKeyDown(keyCode, event);
		//return true ;
	}
	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	
		super.onActivityResult(requestCode, resultCode, data);
		
		Log.v(TAG,"In onActivityResult");
		//Log.v(TAG,"request code is "+requestCode+" , result code is "+resultCode);
		if(requestCode == HomePageFragment.EXPENSE_COMPLETE_CODE)
		{
			if(resultCode == Activity.RESULT_OK)
			{
				Log.v(TAG,"In processing partt");
				
				int pos = data.getIntExtra(ExpenseCompleteFragment.POSITION_LIST, -1) ;
				//Log.v(TAG, "In onActivityResult: position in list is "+pos) ;
				if(pos != -1)
				{
					
					if(mCallBackHomePage != null)
					{
						mCallBackHomePage.updateTagList(pos);
						Toast.makeText(activity, "mCallBackHomePage is not null", Toast.LENGTH_LONG).show() ;
					}
					else
					{
						Toast.makeText(activity, "mCallBackHomePage is null", Toast.LENGTH_LONG).show() ;
					}
					//Log.v(TAG, "In onActivityResult: List content is - "+list.get(pos).getTag().toString()) ;
					//list.remove(pos) ;
					//cad.notifyDataSetChanged();
					
				}
				
			}
			else if(resultCode == Activity.RESULT_CANCELED)
			{
				
				
			}
			
		}
		
		
		
	
		
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		activity = null ;
		super.onDestroy();
	}



	/*
	 * If this activity is opened first time after installation of this app in the device then
	 * creation of DB schema for this app is done using this method 
	 * 
	 * */
		private void createDatabase() {
			
			//Log.v(TAG,"In createDatabase : Creating DB");
			DbAdapter dbAdapter = new DbAdapter(activity);
			dbAdapter.open();
			dbAdapter.close();
	    	
		}


	

	
	
	
	
}
