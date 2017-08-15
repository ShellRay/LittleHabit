package com.example.frescogif.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;

/**
 * <p>Copyright: Copyright (c) 2016</p>
 *
 * <p>Company: 浙江齐聚科技有限公司<a href="www.guagua.cn">www.guagua.cn</a></p>
 *
 * @description 列表Adapter基类
 *
 * @author 薛文超
 * @modify
 * @version 1.0.0
 */
public abstract class ArrayListAdapter<T> extends BaseAdapter{
	public ArrayList<T> mList;
	public Context mContext;
	public AdapterView<ListAdapter> mListView;
	
	protected String  mKey;
	
	public ArrayListAdapter(Context context){
		this.mContext = context;
	}

	@Override
	public int getCount() {
		if(mList != null)
			return mList.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		return mList == null ? null : mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	abstract public View getView(int position, View convertView, ViewGroup parent);
	

	public void setList(ArrayList<T> list){
		this.mList = list;
		notifyDataSetChanged();
	}
	
	public ArrayList<T> getList(){
		return mList;
	}
	
	public void setList(T[] list){
		ArrayList<T> arrayList = new ArrayList<T>(list.length);  
		for (T t : list) {  
			arrayList.add(t);  
		}  
		setList(arrayList);
	}
	
	public void addList(ArrayList<T> list){
		if(this.mList!=null &&this.mList.size()>0){
			for (T t : list) {  
				mList.add(t);  
			}  
		}else{
			this.mList = list;	
		}
		notifyDataSetChanged();
	}

	public AdapterView<ListAdapter> getAdapterView() {
		return mListView;
	}
	
	public void setAdapterView(AdapterView<ListAdapter> listView){
		mListView = listView;
	}

	public String getmKey() {
		return mKey;
	}

	public void setmKey(String mKey) {
		this.mKey = mKey;
	}
}
