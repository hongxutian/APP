package com.example.smartlock.query;

import android.content.Context;
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
import com.example.smartlock.adapter.DeviceAdapter;
import com.example.smartlock.util.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeviceInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceInfoFragment extends Fragment implements Observer {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private MyViewModel myViewModel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DeviceInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeviceInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeviceInfoFragment newInstance(String param1, String param2) {
        DeviceInfoFragment fragment = new DeviceInfoFragment();
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
        View view = inflater.inflate(R.layout.fragment_device_info, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.device_recyclerview);
        List device_number_list=new ArrayList<String>();
        List<Map<String, String>> device_info_list=new ArrayList<>();

        HttpUtil.shp=getContext().getSharedPreferences("device_number", Context.MODE_PRIVATE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        DeviceAdapter deviceAdapter=new DeviceAdapter(device_info_list);
        recyclerView.setAdapter(deviceAdapter);

        JSONObject jsonObject;
        JSONArray device_info_Array= HttpUtil.device_info_extra;
        System.out.println(device_info_Array+"---------------");
        for (int i=0;i<device_info_Array.size();i++){
            jsonObject=device_info_Array.getJSONObject(i);
            Map<String, String> map=new HashMap<>();
            Map<String, String> map_number=new HashMap<>();

            String name=jsonObject.getString("user_name");
            String number=jsonObject.getString("device_number");

            map.put("user_name",name);
            map.put("device_number",number);
            device_info_list.add(map);

            map_number.put("device_number",number);
            device_number_list.add(i,map_number.get("device_number"));

        }

        System.out.println(device_number_list+"#######");
        List number_list=new ArrayList<>();

        HttpUtil.editor=HttpUtil.shp.edit();

        HttpUtil.editor.putInt("size",device_number_list.size());
        HttpUtil.editor.apply();

        for (int i=0;i<device_number_list.size();i++){
            HttpUtil.editor.putString("device_number"+i, (String) device_number_list.get(i));

            HttpUtil.editor.apply();
        }

        System.out.println(HttpUtil.shp.getString("device_number",null)+"*********");
/*

        int size= HttpUtil.shp.getInt("size",0);
        for(int i=0;i<size;i++){
            String s=HttpUtil.shp.getString("device_number"+i,"");
        System.out.println(s);
        number_list.add(s);
        }System.out.println(number_list+"..................................");
*/

        return view;
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_device_info, container, false);
        }

@Override
public void onChanged(Object o) {

        }
        }
