package com.homework.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.homework.R;
import com.homework.activity.StudentInfoActivity;
import com.homework.adapter.StudentListAdapter;
import com.homework.constant.StateCode;
import com.homework.model.CallBack;
import com.homework.model.DataModel;
import com.homework.model.api.Msg;
import com.homework.bean.Student;
import com.homework.constant.C;
import com.homework.util.P;
import com.homework.util.Util;
import com.wang.android_lib.adapter.LoadingAdapter;
import com.wang.android_lib.adapter.NullAdapter;
import com.wang.android_lib.helper.AndroidHttpHelper;
import com.wang.android_lib.util.AndroidHttpUtil;
import com.wang.java_util.HttpUtil;
import com.wang.java_util.Pair;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * by 王荣俊 on 2016/10/4.
 */
public class StudentClassFragment extends Fragment {

    @Bind(R.id.tv_class_name)
    TextView tvClassName;
    @Bind(R.id.lv_student)
    ListView lvStudent;

    private DataModel dataModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_class, container, false);
        ButterKnife.bind(this, view);
        initData();
        initView();
        getClassmateListAndShow();
        return view;
    }

    private void initData() {
        dataModel = new DataModel();
        try {
            String className = P.getStudent().getStudentInformation().getStudentClass().getClassName();
            tvClassName.setText(className);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        lvStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id == NullAdapter.NULL_ADAPTER_ID && position == 0) {//如果是空适配器，即加载失败
//                    startGetClassInfo();
                    getClassmateListAndShow();
                } else if (id != LoadingAdapter.LOADING_ADAPTER_ID) {//若当前不是查询中，即已加载完成
                    Student student = (Student) lvStudent.getAdapter().getItem(position);
                    StudentInfoActivity.start(getActivity(), student);
                }
            }
        });
    }

    private void getClassmateListAndShow() {
        dataModel.studentGetClassmateList(new CallBack<List<Student>>() {
            @Override
            public void onSucceed(List<Student> data) {
                lvStudent.setAdapter(new StudentListAdapter(getActivity(), data));
            }

            @Override
            public void onFailure(StateCode stateCode, String errorMsg) {
                lvStudent.setAdapter(new NullAdapter(getActivity(), "重新获取"));
            }
        });
    }

    //TODO delete
    private void startGetClassInfo() {
        lvStudent.setAdapter(new LoadingAdapter(getActivity()));

        AndroidHttpHelper helper = new AndroidHttpHelper(getActivity());
        helper.setShowToastHint(true).addRequestProperty("Cookie", P.getCookie());
        helper.setOnSucceedListener(new AndroidHttpUtil.OnSucceedListener() {
            @Override
            public void onSucceed(HttpUtil.Result r) {
                Type type = new TypeToken<Msg<List<Student>>>() {
                }.getType();
                Pair<Boolean, Object> pair = Util.handleMsg(getActivity(), r.result, type);
                if (pair.first) {
                    List<Student> studentList = (List<Student>) pair.second;
                    lvStudent.setAdapter(new StudentListAdapter(getActivity(), studentList));
                } else {
                    lvStudent.setAdapter(new NullAdapter(getActivity(), "重新获取"));
                }
            }
        });
        helper.setOnFailedListener(new AndroidHttpUtil.OnFailedListener() {
            @Override
            public void onFailed(HttpUtil.Result r) {
                lvStudent.setAdapter(new NullAdapter(getActivity(), "重新获取"));
            }
        });
        helper.request(C.getClassmateInfoUrl(P.getStudent().getStudentId()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
