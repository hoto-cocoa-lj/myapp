package com.slq.myapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.slq.myapp.R;

import butterknife.BindView;
import butterknife.OnClick;
import skin.support.SkinCompatManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFragment extends Fragment {
    Button b;

    public MyFragment() {
        // Required empty public constructor
    }
    
    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        b= view.findViewById(R.id.changskin);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeskin();
            }
        });
        return view;
    }

    void changeskin(){
        SharedPreferences slq = getActivity().getSharedPreferences("slq", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = slq.edit();
        String skin = slq.getString("skin", "default");
        if (!skin.equals("night")) {
            SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
            edit.putString("skin","night");
        } else {
            SkinCompatManager.getInstance().restoreDefaultTheme();
            edit.putString("skin","default");
        }
        edit.commit();

    }
}