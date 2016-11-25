package com.homework.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.homework.R;
import com.homework.bean.ExtraFile;
import com.wang.java_util.Pair;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * by wangrongjun on 2016/11/22.
 */
public class ExtraFileListAdapter extends BaseAdapter {

    private Context context;
    private List<Pair<ExtraFile, State>> extraFileStateList;

    public enum State {
        already_download,
        downloading,
        download_failed,
        not_exists
    }

    public ExtraFileListAdapter(Context context, List<Pair<ExtraFile, State>> extraFileStateList) {
        this.context = context;
        this.extraFileStateList = extraFileStateList;
    }

    public List<Pair<ExtraFile, State>> getExtraFileStateList() {
        return extraFileStateList;
    }

    @Override
    public int getCount() {
        return extraFileStateList.size();
    }

    @Override
    public Object getItem(int position) {
        return extraFileStateList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.lv_extra_file, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        updateView(holder, position);
        return convertView;
    }

    private void updateView(ViewHolder holder, int position) {
        ExtraFile extraFile = extraFileStateList.get(position).first;
        State state = extraFileStateList.get(position).second;
        holder.tvFileName.setText(extraFile.getFileName());
        String stateString = "";
        switch (state) {
            case not_exists:
                stateString = "下载";
                break;
            case downloading:
                stateString = "下载中";
                break;
            case already_download:
                stateString = "打开";
                break;
            case download_failed:
                stateString = "重新下载";
                break;
        }
        holder.tvState.setText(stateString);
    }

    static class ViewHolder {
        @Bind(R.id.tv_file_name)
        TextView tvFileName;
        @Bind(R.id.tv_state)
        TextView tvState;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
