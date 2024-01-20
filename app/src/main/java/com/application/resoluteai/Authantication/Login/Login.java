package com.application.resoluteai.Authantication.Login;

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

import java.util.HashMap;
import java.util.Map;

public class Login extends Fragment {

    private TextView signup_btn;
    private Button login;
    private TextInputEditText email,password;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root=inflater.inflate(R.layout.fragment_login, container, false);
        init(root);

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AuthActivity)getActivity()).loadSignup();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AuthActivity)getActivity()).onLogin();
            }
        });

        return root;
    }
    private void init(View root){

        login=root.findViewById(R.id.login_btn);
        signup_btn=root.findViewById(R.id.signup);
        email=root.findViewById(R.id.email_edittext);
        password=root.findViewById(R.id.password_edittext);

    }

    public Map<String,String> getCredential(){
        Map<String,String> x =new HashMap<>();
        x.put("email",email.getText().toString());
        x.put("pass",password.getText().toString());
        return x;
    }

    public void EmailError(){
        email.setError("Incorrect Email");
    }
    public void PasswordError(){
        password.setError("Incorrect Password");
    }

}