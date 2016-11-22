package com.homework.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.homework.R;
import com.homework.bean.ExtraFile;
import com.homework.constant.C;
import com.homework.util.P;
import com.wang.android_lib.util.IntentOpenFileUtil;
import com.wang.java_util.CharsetUtil;
import com.wang.java_util.DebugUtil;
import com.wang.java_util.Downloader;
import com.wang.java_util.TextUtil;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * by wangrongjun on 2016/11/22.
 */
public class ExtraFileAdapter extends BaseAdapter {

    private Context context;
    private List<ExtraFile> extraFileList;

    public ExtraFileAdapter(Context context, List<ExtraFile> extraFileList) {
        this.context = context;
        this.extraFileList = extraFileList;
    }

    @Override
    public int getCount() {
        return extraFileList.size();
    }

    @Override
    public Object getItem(int position) {
        return extraFileList.get(position);
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
        return null;
    }

    private void updateView(final ViewHolder holder, int position) {
        final ExtraFile extraFile = extraFileList.get(position);

        holder.tvFileName.setText(extraFile.getFileName());

        //url编码后就是文件名
        final String filePath = C.dir + CharsetUtil.encode(extraFile.getFileUrl());
        DebugUtil.println(filePath);

        final boolean exists = new File(filePath).exists();
        holder.btnDownloadOrOpen.setText(exists ? "打开" : "下载");
        holder.btnDownloadOrOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.btnDownloadOrOpen.getText().equals("打开")) {
                    context.startActivity(
                            IntentOpenFileUtil.getRandomFileIntent(filePath));
                } else {
                    startDownload(extraFile.getFileUrl(), filePath, holder.btnDownloadOrOpen);
                }
            }
        });

    }

    private void startDownload(String fileUrl, String filePath, final TextView btnDownloadOrOpen) {

        Downloader downloader = new Downloader(fileUrl, filePath);
        downloader.setCookie(P.getCookie()).setListener(new Downloader.OnDownloadListener() {
            @Override
            public boolean onDownloadProgressUpdate(int status, String fileUrl, String savePath,
                                                    double currentBytes, double totalBytes, double speed) {
                switch (status) {
                    case Downloader.DOWNLOADING:
                        String s = TextUtil.formatDouble(currentBytes / totalBytes * 100, 1);
                        btnDownloadOrOpen.setText(s);
                        break;
                    case Downloader.DOWNLOAD_FINISH:
                        btnDownloadOrOpen.setText("打开");
                        break;
                }
                return false;
            }
        });

    }

    static class ViewHolder {
        @Bind(R.id.tv_file_name)
        TextView tvFileName;
        @Bind(R.id.btn_download_or_open)
        TextView btnDownloadOrOpen;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
