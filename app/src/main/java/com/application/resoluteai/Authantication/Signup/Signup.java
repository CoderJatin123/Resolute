package com.application.resoluteai.Authantication.Signup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.application.resoluteai.Authantication.AuthActivity;
import com.application.resoluteai.R;
import com.google.android.material.textfield.TextInputEditText;


public class Signup extends Fragment {

   private TextInputEditText name,email,password;
   private Button signup_btn;

   private TextView login;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_signup, container, false);
        init(root);
        signup_btn.setOnClickListener(view -> ((AuthActivity)getActivity()).createUser(name.getText().toString().trim(),email.getText().toString().trim(),password.getText().toString().trim()));


        login.setOnClickListener(view -> ((AuthActivity)getActivity()).loadLogin());

        return root;
    }
    private void init(View root){

        name=root.findViewById(R.id.signup_name_edittext);
        email=root.findViewById(R.id.signup_email_edittext);
        password=root.findViewById(R.id.signup_password_edittext);
        signup_btn=root.findViewById(R.id.signup_btn);
        login=root.findViewById(R.id.login);

    }

}