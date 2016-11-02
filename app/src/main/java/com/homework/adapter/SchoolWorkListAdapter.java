package com.homework.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.homework.R;
import com.homework.bean.SchoolWork;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * by wangrongjun on 2016/11/1.
 */
public class SchoolWorkListAdapter extends BaseAdapter {

    private Context context;
    private List<SchoolWork> schoolWorkList;

    public SchoolWorkListAdapter(Context context, List<SchoolWork> schoolWorkList) {
        this.context = context;
        this.schoolWorkList = schoolWorkList;
    }

    public SchoolWorkListAdapter(Context context) {
        this.context = context;
        schoolWorkList = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            SchoolWork work = new SchoolWork();
            work.setSchoolWorkId(i);
            work.setName("homework_name_" + i);
            work.setContent("content_" + i);
            work.setCreateDate(new Date(2016, 5, i + 1));
            work.setFinalDate(new Date(2016, 5, i + 10));
            schoolWorkList.add(work);
        }
    }

    @Override
    public int getCount() {
        return schoolWorkList.size();
    }

    @Override
    public Object getItem(int position) {
        return schoolWorkList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.lv_school_work, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        updateView(holder, position);
        return convertView;
    }

    private void updateView(ViewHolder holder, int position) {
        SchoolWork schoolWork = schoolWorkList.get(position);
        holder.tvSchoolWorkName.setText(schoolWork.getName());
        holder.tvSchoolWorkContent.setText(schoolWork.getContent());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String createDate = sdf.format(schoolWork.getCreateDate());
        String finalDate = sdf.format(schoolWork.getFinalDate());
        holder.tvDate.setText(createDate + "\n" + finalDate);
    }

    static class ViewHolder {
        @Bind(R.id.tv_school_work_name)
        TextView tvSchoolWorkName;
        @Bind(R.id.tv_date)
        TextView tvDate;
        @Bind(R.id.tv_school_work_content)
        TextView tvSchoolWorkContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
