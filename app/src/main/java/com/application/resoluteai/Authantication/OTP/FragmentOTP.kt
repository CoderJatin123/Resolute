package com.application.resoluteai.Authantication.OTP

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.application.resoluteai.Authantication.AuthActivity
import com.application.resoluteai.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class FragmentOTP : Fragment() {
    lateinit var otp: TextInputEditText
    lateinit var phone: TextInputEditText
    lateinit var verify_btn: Button
    lateinit var send_otp_btn: Button
    lateinit var opt_layout: TextInputLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_o_t_p, container, false)
        init(view)
        send_otp_btn.setOnClickListener {
            (activity as AuthActivity?)?.send_otp(phone.text.toString())

        }

        verify_btn.setOnClickListener{
            (activity as AuthActivity?)?.onVerify(otp.text.toString());
        }

        return view;
    }

    private fun init(view: View) {
        otp=view.findViewById(R.id.otp)
        verify_btn=view.findViewById(R.id.verify_otp_btn)
        send_otp_btn=view.findViewById(R.id.send_otp_btn)
        phone=view.findViewById(R.id.phone)
        opt_layout=view.findViewById(R.id.opt_layout)
    }

    fun enableVerifyBtn(){
        verify_btn.isEnabled=true
        opt_layout.isEnabled=true
    }

    companion object {
        @JvmStatic
        public fun newInstance() =
            FragmentOTP()

    }


}