package com.homework.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.homework.R;
import com.homework.bean.CommitSchoolWork;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * by wangrongjun on 2016/12/2.
 */
public class TeacherCommitSchoolWorkListAdapter extends BaseAdapter {

    private Context context;
    private List<CommitSchoolWork> commitSchoolWorkList;

    public TeacherCommitSchoolWorkListAdapter(Context context, List<CommitSchoolWork> commitSchoolWorkList) {
        this.context = context;
        this.commitSchoolWorkList = commitSchoolWorkList;
    }

    public List<CommitSchoolWork> getCommitSchoolWorkList() {
        return commitSchoolWorkList;
    }

    @Override
    public int getCount() {
        return commitSchoolWorkList.size();
    }

    @Override
    public Object getItem(int position) {
        return commitSchoolWorkList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.lv_commit_school_work, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        updateView(holder, position);
        return convertView;
    }

    private void updateView(ViewHolder holder, int position) {
        CommitSchoolWork commitSchoolWork = commitSchoolWorkList.get(position);
        String realName = commitSchoolWork.getStudent().getStudentInformation().getRealName();
        holder.tvStudentName.setText(realName);
        //TODO 实现批阅功能，显示真实的批阅状态
        holder.tvCheckState.setText("未批阅");
    }

    static class ViewHolder {
        @Bind(R.id.tv_student_name)
        TextView tvStudentName;
        @Bind(R.id.tv_check_state)
        TextView tvCheckState;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
