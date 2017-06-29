package com.sharewisesc.inbroadheart.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sharewisesc.inbroadheart.R;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

/**
 * Created by wenlin on 2016/10/17.
 */
public abstract class BaseFragment extends SwipeBackFragment {
    private static final String TAG = "Fragmentation";
    private boolean isViewPrepared; // 标识fragment视图已经初始化完毕
    private boolean hasFetchData; // 标识已经触发过懒加载数据
    protected Activity activity;
    protected View mRootView;

    protected abstract
    @LayoutRes
    int getLayoutId();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        isViewPrepared = true;
        lazyFetchDataIfPrepared();
    }

    private void lazyFetchDataIfPrepared() {
        if (getUserVisibleHint() && !hasFetchData && isViewPrepared) {
            hasFetchData = true;
            initData();
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            lazyFetchDataIfPrepared();
        }
    }


    protected abstract void initView();

    protected abstract void initData();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hasFetchData = false;
        isViewPrepared = false;
    }


    public void openActivity(Class<? extends Activity> clazz, Bundle bundle) {
        Intent intent = new Intent(activity, clazz);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
//        activity.overridePendingTransition(R.anim.h_fragment_enter, R.anim.activity_anim_out);
    }

    public void openActivityResult(Class<? extends Activity> clazz, Bundle bundle, int request) {
        Intent intent = new Intent(activity, clazz);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, request);
//        activity.overridePendingTransition(R.anim.h_fragment_enter, R.anim.h_fragment_pop_exit);
    }

    public <V extends View> V findViewById(View view, int id) {
        return (V) view.findViewById(id);
    }

    protected <T extends View> T findView(@IdRes int id) {
        return (T) mRootView.findViewById(id);
    }

    public void initEmpty(ListView listview, String text, int drawableId) {
        try {
            if (listview.getEmptyView() == null) {
                View emptyVIew = View.inflate(activity, R.layout.listview_empty_layout, null);
                TextView emptyText = findViewById(emptyVIew, R.id.empty_text);
                ImageView emptyImage = findViewById(emptyVIew, R.id.empty_image);
                emptyText.setText(text);
                if (drawableId != 0) {
                    emptyImage.setBackgroundResource(drawableId);
                } else {
//                    emptyImage.setBackgroundResource(R.drawable.dummy_status);
                }
                ((ViewGroup) listview.getParent()).addView(emptyVIew);
                listview.setEmptyView(emptyVIew);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


