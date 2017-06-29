package com.sharewisesc.inbroadheart.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sharewisesc.inbroadheart.R;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;

/**
 * Created by wenlin on 2016/10/17.
 */
public abstract class BaseActivity extends SwipeBackActivity {
    protected Toolbar toolbar;
    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";

    protected abstract
    @LayoutRes
    int getLayoutId();

    protected abstract void initViews(Bundle savedInstanceState);

    protected abstract void loadData();

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initToolBar();
        initViews(savedInstanceState);
        loadData();
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        if (name.equals(LAYOUT_FRAMELAYOUT)) {
            view = new AutoFrameLayout(context, attrs);
        }

        if (name.equals(LAYOUT_LINEARLAYOUT)) {
            view = new AutoLinearLayout(context, attrs);
        }

        if (name.equals(LAYOUT_RELATIVELAYOUT)) {
            view = new AutoRelativeLayout(context, attrs);
        }

        if (view != null) return view;

        return super.onCreateView(name, context, attrs);
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(R.anim.h_fragment_pop_enter, R.anim.activity_anim_out);
    }

    public void openActivity(Class<? extends Activity> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
//        overridePendingTransition(R.anim.h_fragment_enter, R.anim.h_fragment_pop_exit);
    }

    public void openActivityForResult(Class<? extends Activity> clazz, Bundle bundle, int request) {
        Intent intent = new Intent(this, clazz);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, request);
//        this.overridePendingTransition(R.anim.h_fragment_enter, R.anim.h_fragment_pop_exit);
    }

    public <V extends View> V findViewById(View view, int id) {
        return (V) view.findViewById(id);
    }

    public void initEmpty(ListView listview, String text, int drawableId) {
        try {
            if (listview.getEmptyView() == null) {
                View emptyVIew = View.inflate(this, R.layout.listview_empty_layout, null);
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
