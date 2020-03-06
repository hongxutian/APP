package com.example.smartlock.query;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.smartlock.MyViewModel;
import com.example.smartlock.R;
import com.example.smartlock.adapter.UserAdapter;
import com.example.smartlock.util.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserInfoFragment extends Fragment implements Observer {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private MyViewModel myViewModel;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserInfoFragment newInstance(String param1, String param2) {
        UserInfoFragment fragment = new UserInfoFragment();
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
        myViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        myViewModel.getLiveData().observe(this, this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.user_recyclerview);
        List<Map<String, String>> user_info_list=new ArrayList<>();
        //指定RecyclerView的布局为线性布局
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        //创建UserAdapter的实例，将数据传入构造函数
        UserAdapter userAdapter = new UserAdapter(user_info_list);
        recyclerView.setAdapter(userAdapter);
        JSONObject jsonObject;

        JSONArray user_info_Array=HttpUtil.user_info_extra;

        for(int i=0;i<user_info_Array.size();i++){
            jsonObject=user_info_Array.getJSONObject(i);
            Map<String, String> map=new HashMap<>();

            String name=jsonObject.getString("user_name");
            String number=jsonObject.getString("user_number");
            map.put("name",name);
            map.put("number",number);
            System.out.println(map+"*****************");
            user_info_list.add(map);
        }
        System.out.println("user_info_list:"+user_info_list+"*****************");

        return view;
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_user_info, container, false);
    }

    @Override
    public void onChanged(Object o) {

    }
}
