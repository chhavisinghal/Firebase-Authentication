package com.example.dell.splash;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.dell.splash.Fragment.All;
import com.example.dell.splash.Fragment.Fitness;
import com.example.dell.splash.Fragment.Gym;
import com.example.dell.splash.Fragment.Sportswear;
import com.example.dell.splash.Fragment.game_product;

public class tabpagerAdapter extends FragmentStatePagerAdapter {
    String[] tabarray=new String[]{"All","Game Products","Sportswear","Fitness","Gym"};
    Integer tabNumber=5;

    public tabpagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: All all=new All();
                    return all;
            case 1: game_product gp=new game_product();
                    return gp;
            case 2: Sportswear sportswear=new Sportswear();
                    return sportswear;
            case 3: Fitness fitness=new Fitness();
                    return fitness;
            case 4: Gym gym=new Gym();
                    return gym;
        }
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabarray[position];
    }

    @Override
    public int getCount() {
        return tabNumber;
    }
}
