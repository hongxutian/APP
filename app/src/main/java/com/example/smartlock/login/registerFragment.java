package com.example.smartlock.login;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.smartlock.MyViewModel;
import com.example.smartlock.R;
import com.example.smartlock.RSATest.Base64Utils;
import com.example.smartlock.RSATest.MD5;
import com.example.smartlock.util.HttpUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
    public class registerFragment extends Fragment implements Observer {
    private MyViewModel myViewModel;
    public registerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myViewModel=ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        myViewModel.getLiveData().observe(this,this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_register,container,false);
        final EditText edt_name=view.findViewById(R.id.edt_name);
        final EditText edt_phone=view.findViewById(R.id.edt_phone);
        final EditText edt_openingcode=view.findViewById(R.id.edt_openingcode);
        final EditText edt_identifyingCode=view.findViewById(R.id.edt_identifyingCode);

        //获取验证码
        view.findViewById(R.id.getIdentifyingCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_phone.getText().toString().equals("")||edt_phone.getText().toString()==null){
                    Toast.makeText(getContext(),"请输入手机号",Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String,String> map=new HashMap<>();
                map.put("phone",edt_phone.getText().toString());
                HttpUtil.webRequest(HttpUtil.getVerificationCode_url,map,myViewModel,"register_GetVerificationCode");
            }
        });

        //注册
        view.findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_name.getText().toString().equals("")||edt_name.getText().toString()==null){
                    Toast.makeText(getContext(),"请输入用户名",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(edt_phone.getText().toString().equals("")||edt_phone.getText().toString()==null){
                    Toast.makeText(getContext(),"请输入手机号",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(edt_openingcode.getText().toString().equals("")||edt_openingcode.getText().toString()==null){
                    Toast.makeText(getContext(),"请设置登录密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(edt_identifyingCode.getText().toString().equals("")||edt_identifyingCode.getText().toString()==null){
                    Toast.makeText(getContext(),"请输入验证码",Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONObject object=new JSONObject();
                object.put("phone",edt_phone.getText().toString());
                object.put("user_name",edt_name.getText().toString());
                object.put("password", MD5.getMD5(edt_openingcode.getText().toString()));

                Map<String,String>map=new LinkedHashMap<>();
                map.put("phone",edt_phone.getText().toString());
                map.put("data", Base64Utils.encodeURL(object.toJSONString()));
                map.put("verificationCode",edt_identifyingCode.getText().toString());
                for(String key:map.keySet()){
                    System.out.println(key+"------------"+map.get(key));
                }
                HttpUtil.webRequest(HttpUtil.register_url,map,myViewModel,"register");

            }
        });

        return  view;
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_register, container, false);
    }
    @Override
    //处理订阅信息
    public void onChanged(Object o) {
        Map<String,String> map=(Map)o;
        JSONObject object;
        if(map.get("type").equals("register_GetVerificationCode")){
            object= JSON.parseObject(map.get("content"));
            Toast.makeText(getContext(),object.getString("msg"),Toast.LENGTH_SHORT).show();
        }
        if(map.get("type").equals("register")){
            object=JSON.parseObject(map.get("content"));
            Toast.makeText(getContext(),object.getString("msg"),Toast.LENGTH_SHORT).show();
            if(object.getString("res").equals("0")){
                Navigation.findNavController(getView()).navigate(R.id.action_registerFragment_to_loginFragment);
            }
        }

    }
}
