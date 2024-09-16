package com.demo.fluid.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.jakewharton.rxbinding4.view.RxView;
import io.reactivex.rxjava3.disposables.Disposable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.functions.Consumer;
import kotlin.Unit;
import com.demo.fluid.R;
import com.demo.fluid.activity.home.HomeModel;

public class AdapterHome extends RecyclerView.Adapter<AdapterHome.ViewHolder> {
    private final ArrayList<HomeModel> _List = new ArrayList<>();
    private final Context context;
    private final OnClickListener onClickListener;

    public interface OnClickListener {
        void onClickListener(String str, String str2);
    }

    public AdapterHome(Context context2, OnClickListener onClickListener2) {
        this.context = context2;
        this.onClickListener = onClickListener2;
    }

    public void setData(List<HomeModel> list) {
        this._List.clear();
        this._List.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_home, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        HomeModel homeModel = this._List.get(i);
        ((RequestBuilder) ((RequestBuilder) Glide.with(this.context).load(getPathThumbnail(homeModel.getTitle())).error(R.drawable.img_thumbnail_default)).placeholder(R.drawable.img_thumbnail_default)).into(viewHolder.image);
        viewHolder.disposable = RxView.clicks(viewHolder.rlView).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(new Consumer<Unit>() {
            @Override
            public void accept(Unit unit) throws Throwable {
                onClickListener.onClickListener(homeModel.getTitle(), homeModel.getAds());
            }
        });
    }

    @Override
    public int getItemCount() {
        return this._List.size();
    }

    private Uri getPathThumbnail(String str) {
        return Uri.parse("file:///android_asset/image/" + str + ".webp");
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public Disposable disposable;
        public ImageView image;
        public LinearLayout rlView;

        public ViewHolder(View view) {
            super(view);
            this.rlView = (LinearLayout) view.findViewById(R.id.rlView);
            this.image = (ImageView) view.findViewById(R.id.image);
        }
    }
}
