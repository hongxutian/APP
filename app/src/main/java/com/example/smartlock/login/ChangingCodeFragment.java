package com.example.smartlock.login;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.smartlock.MyViewModel;
import com.example.smartlock.R;
import com.example.smartlock.RSATest.Base64Utils;
import com.example.smartlock.RSATest.MD5;
import com.example.smartlock.util.HttpUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChangingCodeFragment extends Fragment implements Observer {
    private MyViewModel myViewModel;

    public ChangingCodeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myViewModel= ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        myViewModel.getLiveData().observe(this,this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            View view=inflater.inflate(R.layout.fragment_changing_code,container,false);

            Button cancle_modify=view.findViewById(R.id.cancle_modify);
            final EditText edit_phone=view.findViewById(R.id.edit_phone);
            final EditText et_new_password=view.findViewById(R.id.et_new_password);
            final EditText edit_identifyingCode=view.findViewById(R.id.edit_identifyingCode);

        //切换到登录界面
        cancle_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller= Navigation.findNavController(v);
                controller.navigate(R.id.action_changingCodeFragment_to_loginFragment);
            }
        });
        //获取验证码
        view.findViewById(R.id.bt_getIdentifyingCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_phone.getText().toString().equals("")||edit_phone.getText().toString()==null){
                    Toast.makeText(getContext(),"请输入手机号",Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String,String>map=new HashMap<>();
                map.put("phone",edit_phone.getText().toString());
                HttpUtil.webRequest(HttpUtil.getVerificationCode_url,map,myViewModel,"modify_GetVerificationCode");

            }
        });
        //修改密码
        view.findViewById(R.id.ok_modify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_phone.getText().toString().equals("")||edit_phone.getText().toString()==null){
                    Toast.makeText(getContext(),"请输入手机号",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(et_new_password.getText().toString().equals("")||et_new_password.getText().toString()==null){
                    Toast.makeText(getContext(),"请输入新密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(edit_identifyingCode.getText().toString().equals("")||edit_identifyingCode.getText().toString()==null){
                    Toast.makeText(getContext(),"请输入验证码",Toast.LENGTH_SHORT).show();
                    return;
                }
                String new_password=MD5.getMD5(et_new_password.getText().toString());
                Map<String,String>map=new HashMap<>();
                map.put("phone",edit_phone.getText().toString());
                map.put("password", Base64Utils.encodeURL(new_password));
                map.put("verificationCode",edit_identifyingCode.getText().toString());
                HttpUtil.webRequest(HttpUtil.modifyPassword_url,map,myViewModel,"modifyPassword");

            }
        });



            return view;
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_changing_code, container, false);
    }

    //处理订阅信息
    @Override
    public void onChanged(Object o) {
        Map<String,String>map=(Map<String, String>)(o);
        JSONObject object;
        if(map.get("type").equals("modify_GetVerificationCode")){
            Toast.makeText(getContext(),map.get("content"),Toast.LENGTH_SHORT).show();
        }
        if(map.get("type").equals("modifyPassword")){
            object= JSON.parseObject(map.get("content"));
            Toast.makeText(getContext(),object.getString("msg"),Toast.LENGTH_SHORT).show();
            if(object.getString("res").equals("0")){
                Navigation.findNavController(getView()).navigate(R.id.action_changingCodeFragment_to_loginFragment);
            }
        }

    }
}
