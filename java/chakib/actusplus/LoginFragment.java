package chakib.actusplus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by Chakib on 28/09/2017.
 */

public class LoginFragment extends DialogFragment {


    EditText mEmailText, mEmailSign, mPasswordText, mPasswordSign, mVerifyPass, mName, mFamilyName;
    TextView mSignupLink, mLoginLink ;
    LinearLayout mLinearLayout, mLlSign ;
    ScrollView mScrollView, mSvSign ;
    Button mButtonLogin, mButtonSignin ;
    CheckBox mCbShowPwd, mCbShowPwdSign, mCbShowPwdVerify, mCbMale, mCbFemale, mCbRememberMe ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        getDialog().setTitle("Login");
        mEmailText = (EditText) view.findViewById(R.id.ev_input_email);
        mPasswordText = (EditText) view.findViewById(R.id.ev_input_password);
        mEmailSign = (EditText) view.findViewById(R.id.ev_input_email_sign);
        mPasswordSign = (EditText) view.findViewById(R.id.ev_input_password_sign);
        mSignupLink = (TextView) view.findViewById(R.id.tv_link_signup);
        mLoginLink = (TextView) view.findViewById(R.id.tv_link_login);
        mLinearLayout = (LinearLayout) view.findViewById(R.id.ll_log_in);
        mLlSign = (LinearLayout) view.findViewById(R.id.ll_sign_in);
        mScrollView = (ScrollView) view.findViewById(R.id.sv_log_in);
        mSvSign = (ScrollView) view.findViewById(R.id.sv_sign_in);
        mButtonLogin = (Button) view.findViewById(R.id.btn_login);
        mButtonSignin = (Button) view.findViewById(R.id.btn_login);
        mName = (EditText) view.findViewById(R.id.ev_name);
        mFamilyName = (EditText) view.findViewById(R.id.ev_family_name);
        mCbMale = (CheckBox) view.findViewById(R.id.cb_gender_m);
        mCbFemale = (CheckBox) view.findViewById(R.id.cb_gender_f);
        mVerifyPass = (EditText) view.findViewById(R.id.ev_verify_password);
        mCbShowPwd = (CheckBox) view.findViewById(R.id.cbShowPwd);
        mCbShowPwdVerify = (CheckBox) view.findViewById(R.id.cbShowPwdverify);
        mCbShowPwdSign = (CheckBox) view.findViewById(R.id.cbShowPwd_sign);
        mCbRememberMe = (CheckBox) view.findViewById(R.id.cb_remember_me);
        return view;
    }

}
