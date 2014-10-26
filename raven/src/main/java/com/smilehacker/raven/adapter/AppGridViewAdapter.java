package com.smilehacker.raven.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smilehacker.raven.R;
import com.smilehacker.raven.activity.AppDetailActivity;
import com.smilehacker.raven.activity.MainActivity;
import com.smilehacker.raven.model.db.AppInfo;
import com.smilehacker.raven.util.image.AsyncIconLoader;

import java.util.List;

/**
 * Created by kleist on 14-5-19.
 */
public class AppGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<AppInfo> mAppInfos;
    private LayoutInflater mLayoutInflater;
    private AsyncIconLoader mAsyncIconLoader;

    public AppGridViewAdapter(Context context, List<AppInfo> appInfos) {
        mContext = context;
        mAppInfos = appInfos;
        mLayoutInflater = LayoutInflater.from(context);
        mAsyncIconLoader = new AsyncIconLoader(context);
    }

    @Override
    public int getCount() {
        return mAppInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return mAppInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.item_gv_app, viewGroup, false);
            holder = new ViewHolder();
            holder.ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
            holder.tvName = (TextView) view.findViewById(R.id.tv_name);
            holder.cbEnable = (CheckBox) view.findViewById(R.id.cb_enable);
            holder.rlCard = (RelativeLayout) view.findViewById(R.id.rl_card);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final AppInfo appInfo = mAppInfos.get(i);
        holder.tvName.setText(appInfo.appName);
        holder.cbEnable.setChecked(appInfo.enable);
        mAsyncIconLoader.loadBitmap(appInfo.packageName, holder.ivIcon);

        holder.cbEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appInfo.enable = !appInfo.enable;
                holder.cbEnable.setChecked(appInfo.enable);
                appInfo.save();
            }
        });

        holder.rlCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        return view;
    }

    public void flush(List<AppInfo> appInfos) {
        mAppInfos.clear();
        mAppInfos.addAll(appInfos);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        public ImageView ivIcon;
        public TextView tvName;
        public CheckBox cbEnable;
        public RelativeLayout rlCard;
    }


}
