package com.srijan.trex;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

class ParcelableObject implements Parcelable
{

	HomePageActivity.MethodCallBackHomePage mCallbackObject ;
	
	public ParcelableObject(HomePageActivity.MethodCallBackHomePage ob ) {
		// TODO Auto-generated constructor stub
		this.mCallbackObject = ob ;
	}
	
	public HomePageActivity.MethodCallBackHomePage getInterfaceObject()
	{
		
		return mCallbackObject ;
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
	
	
}
