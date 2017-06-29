package com.sharewisesc.inbroadheart.ui.main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sharewisesc.inbroadheart.R;
import com.sharewisesc.inbroadheart.ui.base.BaseFragment;
import com.tencent.qcloud.suixinbo.views.LoginActivity;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by wenlin on 2017/6/16.
 */

public class MainFragment extends BaseFragment {

    @BindView(R.id.toolbar_desc)
    TextView toolbarDesc;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.id_tool_bar)
    Toolbar idToolBar;
    @BindView(R.id.to_suixinbo_bt)
    Button toSuixinboBt;

    protected OnFragmentOpenDrawerListener mOpenDraweListener;
    public static MainFragment getInstance() {
        MainFragment mainFragment = new MainFragment();
        return mainFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentOpenDrawerListener) {
            mOpenDraweListener = (OnFragmentOpenDrawerListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentOpenDrawerListener");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView() {
        initToolbarNav(idToolBar, true);
        toolbarTitle.setText("应泛心娱乐");
    }

    @Override
    protected void initData() {

    }

    protected void initToolbarNav(Toolbar toolbar, boolean isHome) {
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOpenDraweListener != null) {
                    mOpenDraweListener.onOpenDrawer();
                }
            }
        });
    }
    @OnClick(R.id.to_suixinbo_bt)
    public void goTosuixinbo(){
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置默认Fragment动画  默认竖向(和安卓5.0以上的动画相同)
//        return super.onCreateFragmentAnimator();
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
        // 设置自定义动画
//        return new FragmentAnimator(enter,exit,popEnter,popExit);
    }


    public interface OnFragmentOpenDrawerListener {
        void onOpenDrawer();
    }
}
