package com.federicocolantoni.projects.interventix.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class InterventoPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragments;

	public InterventoPagerAdapter(FragmentManager fm, List<Fragment> listFrag) {

		super(fm);

		fragments = listFrag;
	}

	public Fragment getItemByClassType() {

		return null;
	}

	@Override
	public Fragment getItem(int position) {

		return fragments.get(position);
	}

	@Override
	public int getCount() {

		return fragments.size();
	}
}
