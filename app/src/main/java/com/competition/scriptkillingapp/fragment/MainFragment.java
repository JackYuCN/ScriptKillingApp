package com.competition.scriptkillingapp.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.competition.scriptkillingapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainFragment extends Fragment {

    public BottomNavigationView navigationView;
    private FragmentTransaction childTransaction;
    private FragmentManager childManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frament_main_navigation, container, false);
        navigationView = view.findViewById(R.id.bottom_navigation);
        navigationView.setItemIconTintList(null);
        init();
        return view;
    }

    @SuppressLint("NonConstantResourceId")
    private void init(){
        childManager = getChildFragmentManager();
        childTransaction = childManager.beginTransaction();
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.main_content_fragment, new HomeFragment(), "home")
                .commit();
        childTransaction.commit();
        navigationView.setOnItemSelectedListener(item -> {
            resetIcon();
            switch(item.getItemId()){
                case R.id.navi_home: {
                    if(navigationView.getSelectedItemId() != R.id.navi_home){
                        item.setIcon(R.drawable.navi_home);
                        childTransaction = getChildFragmentManager().beginTransaction();
                        hideFragment(navigationView.getSelectedItemId());
                        childTransaction.show(Objects.requireNonNull(childManager.findFragmentByTag("home")));
                        childTransaction.commit();
                    }
                    break;
                }
                case R.id.navi_scripts:{
                    if(navigationView.getSelectedItemId() != R.id.navi_scripts){
                        item.setIcon(R.drawable.navi_scripts);
                        childTransaction = getChildFragmentManager().beginTransaction();
                        hideFragment(navigationView.getSelectedItemId());
                        if(getChildFragmentManager().findFragmentByTag("scripts") == null){
                            ScriptsFragment fragment = new ScriptsFragment();
                            childTransaction.add(R.id.main_content_fragment, fragment, "scripts");
                            childTransaction.show(fragment);
                        }else{
                            childTransaction.show(Objects.requireNonNull(childManager.findFragmentByTag("scripts")));
                        }
                        childTransaction.commit();
                    }
                    break;
                }
                case R.id.navi_message:{
                    if(navigationView.getSelectedItemId() != R.id.navi_message){
                        item.setIcon(R.drawable.navi_message);
                        childTransaction = getChildFragmentManager().beginTransaction();
                        hideFragment(navigationView.getSelectedItemId());
                        if(getChildFragmentManager().findFragmentByTag("message") == null){
                            MessageFragment fragment = new MessageFragment();
                            childTransaction.add(R.id.main_content_fragment, fragment, "message");
                            childTransaction.show(fragment);
                        }else{
                            childTransaction.show(Objects.requireNonNull(childManager.findFragmentByTag("message")));
                        }
                        childTransaction.commit();
                    }
                    break;
                }
                case R.id.navi_my:{
                    if(navigationView.getSelectedItemId() != R.id.navi_my){
                        item.setIcon(R.drawable.navi_my);
                        childTransaction = getChildFragmentManager().beginTransaction();
                        hideFragment(navigationView.getSelectedItemId());
                        if(getChildFragmentManager().findFragmentByTag("my") == null){
                            MyFragment fragment = new MyFragment();
                            childTransaction.add(R.id.main_content_fragment, fragment, "my");
                            childTransaction.show(fragment);
                        }else{
                            childTransaction.show(Objects.requireNonNull(childManager.findFragmentByTag("my")));
                        }
                        childTransaction.commit();
                    }
                    break;
                }
                default:
                    break;
            }
            return true;
        });

    }

    @SuppressLint("NonConstantResourceId")
    private void hideFragment(int selectedID){
        switch (selectedID){
            case R.id.navi_home:{
                if(childManager.findFragmentByTag("home") != null)
                    childTransaction.hide(Objects.requireNonNull(childManager.findFragmentByTag("home")));
                break;
            }
            case R.id.navi_scripts:{
                if(childManager.findFragmentByTag("scripts") != null){
                    childTransaction.hide(Objects.requireNonNull(childManager.findFragmentByTag("scripts")));
                }
                break;
            }
            case R.id.navi_message:{
                if(childManager.findFragmentByTag("message") != null){
                    childTransaction.hide(Objects.requireNonNull(childManager.findFragmentByTag("message")));
                }
                break;
            }
            case R.id.navi_my:{
                if(childManager.findFragmentByTag("my") != null){
                    childTransaction.hide(Objects.requireNonNull(childManager.findFragmentByTag("my")));
                }
                break;
            }
            default:
                break;
        }
    }

    private void resetIcon(){
        MenuItem home = navigationView.getMenu().findItem(R.id.navi_home);
        home.setIcon(R.drawable.navi_home);
        MenuItem scripts = navigationView.getMenu().findItem(R.id.navi_scripts);
        scripts.setIcon(R.drawable.navi_scripts);
        MenuItem message = navigationView.getMenu().findItem(R.id.navi_message);
        message.setIcon(R.drawable.navi_message);
        MenuItem my = navigationView.getMenu().findItem(R.id.navi_my);
        my.setIcon(R.drawable.navi_my);
    }
}
