package com.example.darren.new_design;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Fragment_signin extends Fragment {

    TextView in_signin;
    Button in_Login;
    FragmentManager fm;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View InputFragmentView = inflater.inflate(R.layout.signin, container, false);

        in_signin = (TextView) InputFragmentView.findViewById(R.id.in_signup);
        in_Login = (Button) InputFragmentView.findViewById(R.id.in_Login);


        fm = getFragmentManager();
        in_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.animator.enter_from_right, R.animator.exit_to_left)
                        .replace(R.id.content_login, new Fragment_signup())
                        .addToBackStack(null)
                        .commit();
            }
        });

        in_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent
                        (getActivity(), Activity_container.class);
                startActivity(intent);
            }
        });
        return InputFragmentView;
    }
}