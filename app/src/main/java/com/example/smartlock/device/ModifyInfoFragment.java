package com.example.smartlock.device;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.smartlock.util.HttpUtil;

import org.json.JSONException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModifyInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModifyInfoFragment extends Fragment implements Observer {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MyViewModel myViewModel;
    private String modify_phone;

    public ModifyInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ModifyInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ModifyInfoFragment newInstance(String param1, String param2) {
        ModifyInfoFragment fragment = new ModifyInfoFragment();
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
        View view=inflater.inflate(R.layout.fragment_modify_info,container,false);
        modify_phone=HttpUtil.shp.getString("login_phone","");
        //final EditText modify_phone=view.findViewById(R.id.modify_phone);
        final EditText modify_device_num=view.findViewById(R.id.modify_device_num);
        final EditText modify_note=view.findViewById(R.id.modify_note);

        //切换到设备管理界面
        view.findViewById(R.id.modify_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller= Navigation.findNavController(v);
                controller.navigate(R.id.action_modifyInfoFragment_to_deviceManageFragment);
            }
        });
        //修改设备信息
        view.findViewById(R.id.modify_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(modify_note.getText().toString().equals("")||modify_note.getText().toString()==null){
                    Toast.makeText(getContext(),"请输入备注信息",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(modify_device_num.getText().toString().equals("")||modify_device_num.getText().toString()==null){
                    Toast.makeText(getContext(),"请输入设备号",Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONObject object=new JSONObject();
                object.put("phone",modify_phone);
                object.put("note",modify_note.getText().toString());
                object.put("device", modify_device_num.getText().toString());

                Map<String,String> map=new LinkedHashMap<>();
                map.put("content", object.toJSONString());

                try {
                    HttpUtil.webRequestWithToken(true,HttpUtil.modifyInfo_url,map,myViewModel,"modifyInfo");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        return view;
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_modify_info, container, false);
    }

    @Override
    public void onChanged(Object o) {
        Map<String,String> map =(Map<String, String>) o;
        JSONObject object;;
        if(map.get("type").equals("modifyInfo")){
            Toast.makeText(getContext(),map.get("content"),Toast.LENGTH_SHORT).show();
            object= JSON.parseObject(map.get("content"));
            if(object.getString("res").equals("0")){
                Navigation.findNavController(getView()).navigate(R.id.action_modifyInfoFragment_to_deviceManageFragment);
            }
        }

    }
}
