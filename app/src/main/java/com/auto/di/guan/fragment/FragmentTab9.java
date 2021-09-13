package com.auto.di.guan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.auto.di.guan.R;
import com.auto.di.guan.adapter.Fragment9Adapter;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.entity.OptionEntiy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *   视频监控
 */
public class FragmentTab9 extends BaseFragment {
	private View view;

	private RecyclerView recyclerView;
	private Fragment9Adapter adapter;
	private List<OptionEntiy>entiys = new ArrayList<>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_9, null);
		recyclerView = view.findViewById(R.id.list);
		recyclerView.setLayoutManager(new LinearLayoutManager(activity));

		HashMap<Integer, Boolean> hashMap = Entiy.getStatu();
		Iterator<HashMap.Entry<Integer, Boolean>> entries = hashMap.entrySet().iterator();
		while (entries.hasNext()) {
			HashMap.Entry<Integer, Boolean> entry = entries.next();
			entiys.add(new OptionEntiy(entry.getKey(),entry.getValue()));
		}
		Collections.sort(entiys);

		adapter = new Fragment9Adapter(entiys);
		recyclerView.setAdapter(adapter);
		return view;
	}

	@Override
	public void refreshData() {

	}


}
