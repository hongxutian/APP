package com.example.smartlock.device;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.smartlock.MyViewModel;
import com.example.smartlock.R;
import com.example.smartlock.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectDeviceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectDeviceFragment extends Fragment implements Observer {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private MyViewModel myViewModel;
    private String device_number;
    private List device_number_list=new ArrayList<>();
    private List number_list=new ArrayList<String>();
    private RadioGroup radioGroup;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SelectDeviceFragment() {

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectDeviceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectDeviceFragment newInstance(String param1, String param2) {
        SelectDeviceFragment fragment = new SelectDeviceFragment();
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

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_device, container, false);
        HttpUtil.shp=getContext().getSharedPreferences("device_number", Context.MODE_PRIVATE);


        radioGroup=view.findViewById(R.id.radioGroup);
       /* if (HttpUtil.shp.getString("device_number","")!=null){
            int size= HttpUtil.shp.getInt("size",0);
            for(int i=0;i<size;i++){
                String s=HttpUtil.shp.getString("device_number","");
                number_list.add(s);
            } System.out.println(number_list+"..................................");

        }*/
        int size= HttpUtil.shp.getInt("size",0);
        for(int i=0;i<size;i++){
            String s=HttpUtil.shp.getString("device_number"+i,"");
            System.out.println(s);
            number_list.add(s);
        }System.out.println(number_list+"..................................");




       /* JSONObject jsonObject;
        JSONArray device_Array= HttpUtil.device_info_extra;
        System.out.println(device_Array+"---------------");
        for (int i=0;i<device_Array.size();i++){
                jsonObject=device_Array.getJSONObject(i);
                Map<String, String> map=new HashMap<>();
                device_number=jsonObject.getString("device_number");
                map.put("device_number",device_number);
                device_number_list.add(i,map.get("device_number"));

            }
        System.out.println("list："+device_number_list+"/////////////////////////");
*/

        return  view;
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_select_device, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        radioGroup.removeAllViews();

        for (int j=0;j<number_list.size();j++){
            final RadioButton radioButton=new RadioButton(this.getContext());
            //LayoutParams相当于一个Layout的信息包，它封装了Layout的位置、高、宽等信息
            RadioGroup.LayoutParams lp=new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT,RadioGroup.LayoutParams.WRAP_CONTENT);
            //设置RadioButton边距
            lp.setMargins(15,0,0,0);
            //设置RadioButton的样式
            radioButton.setButtonDrawable(R.drawable.buttongroup);
            radioButton.setTextSize(24);
            radioButton.setText((CharSequence) number_list.get(j));
            radioGroup.addView(radioButton);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int index=checkedId%radioGroup.getChildCount();
                if(index==0){
                    index=radioGroup.getChildCount()-1;
                }else {
                    index=index-1;
                }
                RadioButton childAt = (RadioButton) radioGroup.getChildAt(index);
                CharSequence text = childAt.getText();
                Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();

                HttpUtil.shp=getContext().getSharedPreferences("current_device_number", Context.MODE_PRIVATE);
                HttpUtil.editor=HttpUtil.shp.edit();
                HttpUtil.editor.putString("current_number", (String) text);
                HttpUtil.editor.apply();
                /*Bundle bundle = new Bundle();
                bundle.putString("number",HttpUtil.shp.getString("current_number",""));
                System.out.println(bundle+"..................................");
*/
                //bundle.putString("number", (String) text);
                Navigation.findNavController(getView()).navigate(R.id.menuFragment);
            }
        });


    }

    @Override
    public void onChanged(Object o) {

    }
}
