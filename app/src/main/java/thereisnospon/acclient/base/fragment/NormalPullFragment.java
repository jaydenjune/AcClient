package thereisnospon.acclient.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import thereisnospon.acclient.R;

/**
 * Created by yzr on 16/8/20.
 * 实现了基本功能的 下拉刷新，上拉加载的 Fragment 基类
 */

public abstract class NormalPullFragment<T>extends BasePullFragment<T> {


    public View normalView(LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.normal_list_layout,container,false);
        initRefreshViews(view,R.id.normal_list_swipe,R.id.normal_list_recycle);
        return view;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

}
