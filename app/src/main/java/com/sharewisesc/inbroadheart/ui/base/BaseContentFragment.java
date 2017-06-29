package com.sharewisesc.inbroadheart.ui.base;

import android.support.v4.widget.SwipeRefreshLayout;

import com.sharewisesc.inbroadheart.R;
import com.sharewisesc.inbroadheart.utils.ThemeUtil;


/**
 * Created by wenlin on 2016/10/31.
 */

public abstract class BaseContentFragment extends BaseFragment {

    protected SwipeRefreshLayout refreshLayout;

    private void initRefreshLayout() {
        refreshLayout = findView(R.id.swipe_container);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initView();
            }
        });
        refreshLayout.setColorSchemeResources(ThemeUtil.getCurrentColorPrimary(getActivity()));
    }

    @Override
    protected void initView() {
        initRefreshLayout();
    }

    protected void showRefreshing(final boolean refresh) {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(refresh);
            }
        });
    }
}
