package com.homework.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.homework.R;
import com.wang.java_util.Pair;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * by wangrongjun on 2016/11/9.
 */
public class DefaultListAdapter extends BaseAdapter {

    private Context context;
    private List<Pair<String, String>> showList;

    public DefaultListAdapter(Context context, List<Pair<String, String>> showList) {
        this.context = context;
        this.showList = showList;
    }

    @Override
    public int getCount() {
        return showList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.lv_default, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        updateView(holder, position);
        return convertView;
    }

    private void updateView(ViewHolder holder, int position) {
        Pair<String, String> data = showList.get(position);
        holder.tvLeft.setText(data.first);
        holder.tvRight.setText(data.second);
    }

    static class ViewHolder {
        @Bind(R.id.tv_left)
        TextView tvLeft;
        @Bind(R.id.tv_right)
        TextView tvRight;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
