package com.example.smartlock.query;

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
 * Use the {@link UserQueryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserQueryFragment extends Fragment implements Observer {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MyViewModel myViewModel;
    private String query_user_phone;

    public UserQueryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserQueryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserQueryFragment newInstance(String param1, String param2) {
        UserQueryFragment fragment = new UserQueryFragment();
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
        myViewModel.getLiveData().observe(this, this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_user_query,container,false);

        query_user_phone=HttpUtil.shp.getString("login_phone","");
        final EditText query_user_device=view.findViewById(R.id.query_user_device);

        //切换到Menu界面
        view.findViewById(R.id.user_canclce).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller= Navigation.findNavController(v);
               // controller.navigate(R.id.action_userQueryFragment_to_menuFragment);
            }
        });

        //用户查询
        view.findViewById(R.id.ueser_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(query_user_device.getText().toString().equals("")||query_user_device.getText().toString()==null){
                    Toast.makeText(getContext(),"请输入设备号",Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONObject object=new JSONObject();
                object.put("phone",query_user_phone);
                object.put("device", query_user_device.getText().toString());

                Map<String,String> map=new LinkedHashMap<>();
                map.put("content", object.toJSONString());

                try {
                    HttpUtil.webRequestWithToken(true,HttpUtil.QueryUserInfo_url,map,myViewModel,"QueryUserInfo");
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });
        return view;
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_user_query, container, false);
    }

    @Override
    public void onChanged(Object o) {
        Map<String,String> map =(Map<String, String>) o;
        System.out.println(map+"----------------------");
        JSONObject object;
        if(map.get("type").equals("QueryUserInfo")) {
            object= JSON.parseObject(map.get("content"));
            Toast.makeText(getContext(), object.getString("msg"),Toast.LENGTH_SHORT).show();

            HttpUtil.user_info_extra=object.getJSONArray("extra");
            System.out.println(HttpUtil.user_info_extra+"+++++++++++++++");

            if(object.getString("res").equals("0")){
               // Navigation.findNavController(getView()).navigate(R.id.action_userQueryFragment_to_userInfoFragment);
            }
        }

    }
}
