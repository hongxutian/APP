package com.example.smartlock.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.smartlock.MyViewModel;
import com.example.smartlock.R;
import com.example.smartlock.RSATest.RSAUtils;
import com.example.smartlock.util.HttpUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginByVerificationCodeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginByVerificationCodeFragment extends Fragment implements Observer {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private MyViewModel myViewModel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginByVerificationCodeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginByVerificationCodeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginByVerificationCodeFragment newInstance(String param1, String param2) {
        LoginByVerificationCodeFragment fragment = new LoginByVerificationCodeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        myViewModel= ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        myViewModel.getLiveData().observe(this,this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_login_by_verification_code,container,false);

        final EditText EditText_phoneNumber =view.findViewById(R.id.EditText_phoneNumber);
        final EditText EditText_identifyingCode=view.findViewById(R.id.EditText_identifyingCode);
        Button back_to_code=view.findViewById(R.id.back_to_code);

        back_to_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller= Navigation.findNavController(v);
                controller.navigate(R.id.action_loginByVerificationCodeFragment_to_loginFragment);
            }
        });
        //获取验证码
        view.findViewById(R.id.btn_getIdentifyingCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EditText_phoneNumber.getText().toString().equals("")||EditText_phoneNumber.getText().toString()==null){
                    Toast.makeText(getContext(),"请输入手机号",Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String,String>map=new LinkedHashMap<>();
                map.put("phone",EditText_phoneNumber.getText().toString());
                HttpUtil.webRequest(HttpUtil.getVerificationCode_url,map,myViewModel,"login_GetVerificationCode");
            }
        });

        //通过短信验证码登录
        view.findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EditText_phoneNumber.getText().toString().equals("")||EditText_phoneNumber.getText().toString()==null){
                    Toast.makeText(getContext(),"请输入手机号",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(EditText_identifyingCode.getText().toString().equals("")||EditText_identifyingCode.getText().toString()==null){
                    Toast.makeText(getContext(),"请输入验证码",Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String,String> map=new HashMap<>();
                map.put("phone",EditText_phoneNumber.getText().toString());
                map.put("verificationCode",EditText_identifyingCode.getText().toString());
                Map key= RSAUtils.generateKeyPair();
                HttpUtil.phonePrivateKey=key.get("privateKey").toString();
                HttpUtil.id=EditText_phoneNumber.getText().toString();
                map.put("publicKey",key.get("publicKey").toString());
                HttpUtil.webRequest(HttpUtil.byVerificationCode_url,map,myViewModel,"byVerificationCode");
            }
        });
        return  view;


        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_login_by_verification_code, container, false);
    }

    @Override
    public void onChanged(Object o) {
        Map<String,String>map=(Map<String, String>)(o);
        JSONObject object;
        if(map.get("type").equals("login_GetVerificationCode")){
            Toast.makeText(getContext(),map.get("content"),Toast.LENGTH_SHORT).show();
        }
        if(map.get("type").equals("byVerificationCode")){
            Toast.makeText(getContext(),map.get("content"),Toast.LENGTH_SHORT).show();
            object= JSON.parseObject(map.get("content"));
            Toast.makeText(getContext(),object.getString("msg"),Toast.LENGTH_SHORT).show();
            if(object.getString("res").equals("0")){
                HttpUtil.serverPublicKey=object.getString("extra");
                Navigation.findNavController(getView()).navigate(R.id.action_loginByVerificationCodeFragment_to_menuFragment);
            }
        }

    }
}
