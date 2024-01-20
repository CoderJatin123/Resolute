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


class FragmentOTP : Fragment() {
    lateinit var otp: TextInputEditText
    lateinit var phone: TextInputEditText
    lateinit var verify_btn: Button
    lateinit var send_otp_btn: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_o_t_p, container, false)
        init(view)

        send_otp_btn.setOnClickListener {
            (activity as AuthActivity?)?.send_otp(phone.text.toString())
            verify_btn.isEnabled=true
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
    }

    companion object {

        fun newInstance() =
            FragmentOTP()
    }


}