package com.srijan.trex.widgetprovider;



import com.srijan.trex.R;
import com.srijan.trex.R.id;
import com.srijan.trex.R.layout;
import com.srijan.trex.activities.AddExpenseTagActivity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
/*
 * This class is used to develop Widgets of the app
 * 
 * */
public class TrexAppWidgetProvider extends AppWidgetProvider{
	
	String TAG = "TrexAppWidgetProvider" ;// A Tag to mention Classes's name in Log
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		
		//Log.v(TAG,"Line 1") ;
		final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            //Log.v(TAG,"Line 2") ;
            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, com.srijan.trex.activities.AddExpenseTagActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            //Log.v(TAG,"Line 3") ;
            
            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widgetlayout);
            views.setOnClickPendingIntent(R.id.tagButton, pendingIntent);
            //Log.v(TAG,"Line 4") ;
          
           

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
            //Log.v(TAG,"Line 5") ;
        }
    }

	

}
