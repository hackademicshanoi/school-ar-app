package com.example.daomy.uar2.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.daomy.uar2.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopFragment extends Fragment {

    @BindView(R.id.imgRegisTop)
    ImageView imgRegisTop;
    @BindView(R.id.imgLoginTop)
    ImageView imgLoginTop;
    @BindView(R.id.imgRecoveryTop)
    ImageView imgRecoveryTop;

    public TopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.top, container, false);
        ButterKnife.bind(this, v);
        addControls();
        addEvents();
        return v;
    }

    private void addControls() {

    }

    private void addEvents() {
        imgRegisTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.nav_contentframe,  new RegisterFragment());
                ft.addToBackStack(null);
                ft.commit();
//                getFragmentManager().beginTransaction().replace(R.id.nav_contentframe, new LoginFragment()).addToBackStack(null).commit();
            }
        });

        imgLoginTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.nav_contentframe,  new LoginFragment());
                ft.addToBackStack(null);
                ft.commit();
//                getFragmentManager().beginTransaction().replace(R.id.nav_contentframe, new LoginFragment()).addToBackStack(null).commit();
            }
        });

        imgRecoveryTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.nav_contentframe,  new ReminderFragment());
                ft.addToBackStack(null);
                ft.commit();
//                getFragmentManager().beginTransaction().replace(R.id.nav_contentframe, new LoginFragment()).addToBackStack(null).commit();
            }
        });
    }

}
