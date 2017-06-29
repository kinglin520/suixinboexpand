package com.sharewisesc.inbroadheart.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sharewisesc.inbroadheart.IConstants;
import com.sharewisesc.inbroadheart.InBroadcastHeartApplication;
import com.sharewisesc.inbroadheart.R;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {


    private static ProgressDialog progressDialog;

    private static ProgressBar progressBar;
    private static TextView progressSpeed_tv;

    public static Drawable getDrawableFromId(Context context, int id) {
        Bitmap bitmap = getBitmapFromId(id, context);
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    public static Bitmap getBitmapFromId(int id, Context context) {
        Bitmap bitmap = null;
        if (context == null)
            throw new RuntimeException("context can not be init !");
        InputStream openRawResource = context.getResources().openRawResource(id);
        Options option = new Options();
        try {
//	        option.inPreferredConfig =  Bitmap.Config.RGB_565;  
            option.inJustDecodeBounds = false;
            option.inPurgeable = true;
            bitmap = BitmapFactory.decodeStream(openRawResource, null, option);

        } catch (OutOfMemoryError e) {
            System.gc();
            option.inPreferredConfig = Bitmap.Config.RGB_565;
            option.inJustDecodeBounds = false;
            option.inPurgeable = true;
            option.inSampleSize = 2;
            bitmap = BitmapFactory.decodeStream(openRawResource, null, option);
        }
        return bitmap;
    }


    public static void showProgressDialog(Activity activity, boolean cancelable) {
        showProgressDialog(activity, "加载中...", cancelable);
    }

    public static void showProgressDialog(Activity activity, String msg, boolean cancelable) {
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(activity);
            }
            progressDialog.show();
            View view = View.inflate(activity, R.layout.progress_dialog, null);
            TextView progress_text = (TextView) view.findViewById(R.id.progress_text);
            progress_text.setText("" + msg);
            progressDialog.setContentView(view);
            progressDialog.setCancelable(cancelable);

            Window window = progressDialog.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = IConstants.width * 400 / 1080;
            window.setAttributes(attributes);
        } catch (Exception e) {
        }
    }

    public static ProgressBar showProgressSpeedDialog(Activity activity, boolean cancelable) {
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(activity);
            }
            progressDialog.show();
            View view = View.inflate(activity, R.layout.progressbar_speed_layout, null);
            progressSpeed_tv = (TextView) view.findViewById(R.id.progress_text);
            progressBar = (ProgressBar) view.findViewById(R.id.progress);
            progressDialog.setContentView(view);
            progressDialog.setCancelable(cancelable);

            Window window = progressDialog.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = IConstants.width * 400 / 1080;
            window.setAttributes(attributes);
            return progressBar;
        } catch (Exception e) {
        }
        return null;
    }

    public static void refreshProgresSpeedDialog(int max, int progress) {
        if (progressDialog != null) {
            if (progressBar != null) {
                progressBar.setMax(max);
                progressBar.setProgress(progress);
                progressSpeed_tv.setText((int) ((float) progress / max * 100) + "%");
            }
        }
    }

    public static void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


    public static boolean getProgressDialogStatus() {
        if (progressDialog != null) {
            return progressDialog.isShowing();
        } else {
            return false;
        }

    }

    //获取保留的两位数
    private static double getRealNum(double num) {
        BigDecimal bg = new BigDecimal(num);
        return bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static String getString(Context context, int id) {
        return context.getResources().getString(id);
    }

    public static float getDimension(Context context, int id) {
        return context.getResources().getDimension(id);
    }

    /**
     * 格式化时间
     *
     * @param time
     * @return
     */
    public static String dateFormat(int time) {
        StringBuilder sb = new StringBuilder();
        int h = time / 60;
        int m = time % 60;
        if (time == 0) {
            sb.append("0分钟");
        }
        if (h > 0) {
            sb.append(h + "小时");
        }
        if (m > 0) {
            sb.append(m + "分钟");
        }
        return sb.toString();
    }

    /**
     * 格式化距离
     *
     * @param distance
     * @return
     */
    public static String distanceFormat(int distance) {
        StringBuilder sb = new StringBuilder();
        int km = distance / 1000;
        int m = distance % 1000;
        if (distance == 0) {
            sb.append("0米");
        }
        if (km > 0) {
            if (m > 100) {
                sb.append(km + "." + m / 100 + "公里");
            } else {
                sb.append(km + "公里");
            }
            return sb.toString();
        }
        if (m > 0) {
            sb.append(m + "米");
        }
        return sb.toString();
    }


    public static String getMD5Key(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }


    /**
     * @param
     * @param bitmap 对象
     * @param w      要缩放的宽度
     * @param h      要缩放的高度
     * @return newBmp 新 Bitmap对象
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        return newBmp;
    }

    public static void callPhone(Activity activity, String phoneNum) {
        if (TextUtils.isEmpty(phoneNum))
            return;
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // 拨打电话
        Intent phoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
        activity.startActivity(phoneIntent);
    }


    /**
     * listview添加适配器后，根据item个数计算高度 <br/>
     * setListViewHeightBasedOnChildren:(). <br/>
     * date: 2015年8月28日 <br/>
     *
     * @param listView
     * @author wenlin
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem != null) {
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + 20;
        listView.setLayoutParams(params);
    }

    /**
     * GridView添加适配器后，根据item个数计算高度 <br/>
     * setListViewHeightBasedOnChildren:(). <br/>
     * date: 2015年8月28日 <br/>
     *
     * @param column
     * @param GridView
     * @author wenlin
     */
    public static void setGridViewHeightBasedOnChildren(GridView gridView, int column) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int count = 0;
        if (listAdapter.getCount() % column == 0) {
            count = listAdapter.getCount() / column;
        } else {
            double temp = listAdapter.getCount() / column;
            count = new Double(temp).intValue() + 1;
        }
        for (int i = 0; i < count; i++) {
            View listItem = listAdapter.getView(i, null, gridView);
            if (listItem != null) {
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight + (gridView.getVerticalSpacing() * (count - 1)) + 20;
        gridView.setLayoutParams(params);
    }

    /**
     * 处理adapter线
     * @param view
     * @param position
     * @param length
     * @param margin
     */
//    public static void handleItemLine(View view,int position,int length,int margin){
//    	if(position == length -1){
//    		MarginLayoutParams params =  (MarginLayoutParams) view.getLayoutParams();
//    		params.leftMargin = 0;
//    		params.rightMargin = 0;
//    		view.setLayoutParams(params);
//    		view.setBackgroundResource(R.color.image_line_color);
//    	}else{
//    		MarginLayoutParams params =  (MarginLayoutParams) view.getLayoutParams();
//    		params.leftMargin = margin;
//    		params.rightMargin = margin;
//    		view.setLayoutParams(params);
//    		view.setBackgroundResource(R.color.image_line_eeeeee);
//    	}
//    }

    /**
     * 处理adapter线 ，不改变线的颜色
     *
     * @param view
     * @param position
     * @param length
     * @param margin
     */
    public static void handleItemLine2(View view, int position, int length, int margin) {
        if (position == length - 1) {
            MarginLayoutParams params = (MarginLayoutParams) view.getLayoutParams();
            params.leftMargin = 0;
            params.rightMargin = 0;
            view.setLayoutParams(params);
        } else {
            MarginLayoutParams params = (MarginLayoutParams) view.getLayoutParams();
            params.leftMargin = margin;
            params.rightMargin = margin;
            view.setLayoutParams(params);
        }
    }

//    public static String getCacheSize(Context context){
//    	try {
//    		File cacheDirFile = getCacheDirFile(context);
//			double totalSizeOfFilesInDir = getTotalSizeOfFilesInDir(cacheDirFile);
//			DecimalFormat df = new DecimalFormat("#.00");
//			String chcheSize = df.format(totalSizeOfFilesInDir/1024./1024.)+"MB";
//
//			if(chcheSize.startsWith(".")){
//				chcheSize = "0"+chcheSize;
//			}
//			return chcheSize;
//		} catch (Exception e) {
//		}
//    	return "0.00MB";
//    }

    private static long getTotalSizeOfFilesInDir(final File file) {
        if (file.isFile())
            return file.length();
        final File[] children = file.listFiles();
        long total = 0;
        if (children != null)
            for (final File child : children)
                total += getTotalSizeOfFilesInDir(child);
        return total;
    }


    /**
     * 获取缓存文件路径文件对象
     * /storage/sdcard0/Android/data/com.superscene.landvendor/cache
     * @param context
     * @return
     */
//	public static File getCacheDirFile(Context context){
//		File individualCacheDirectory = StorageUtils.getIndividualCacheDirectory(context);
//		File directory = individualCacheDirectory.getParentFile();
//		return directory;
//	}


    /**
     * 设置对话框样式
     *
     * @param sexDialog
     */
    public static void setDialogStyle(AlertDialog sexDialog) {
        Window window = sexDialog.getWindow();
        window.setGravity(Gravity.CENTER);  //此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.lv_dialog_anim);  //添加动画
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = IConstants.width;
        window.setAttributes(params);
    }

    /**
     * 对数据内容进行编码
     *
     * @param target
     * @return
     */
    public static String encoder(String target) {
        try {
            return URLEncoder.encode(target, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return target;
    }

    /**
     * 对数据内容进行解码
     *
     * @param target
     * @return
     */
    public static String decode(String target) {
        try {
            return URLDecoder.decode(target, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return target;
    }

    /**
     * 释放View资源
     *
     * @param view
     */
    public static void recycleView(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View childAt = viewGroup.getChildAt(i);
                recycleView(childAt);
            }
        } else {
            try {
                if (view instanceof ImageView) {
                    ImageView imageview = (ImageView) view;
                    imageview.setImageBitmap(null);
                }
                Drawable background = view.getBackground();
                if (background != null) {
                    background.setCallback(null);
                    view.setBackground(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static long lastClickTime;

    /**
     * TODO 防止按钮连续点击 <br/>
     * isFastClick:(). <br/>
     * date: 2016年3月30日 <br/>
     *
     * @return
     * @author wenlin
     */
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static long mLastClick = 0L;
    private static final int THRESHOLD = 2000;

    public static boolean doubleClickCheck() {
        long now = System.currentTimeMillis();
        boolean b = now - mLastClick < THRESHOLD;
        mLastClick = now;
        return b;
    }


    /**
     * 补零方法 格式：yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String zeroFill(String date) {
        String[] temp = date.split("-");
        StringBuffer sb = new StringBuffer();
        sb.append(temp[0]);
        sb.append("-");
        if (temp[1].length() == 1) {
            sb.append("0");
        }
        sb.append(temp[1]);
        sb.append("-");
        if (temp[2].length() == 1) {
            sb.append("0");
        }
        sb.append(temp[2]);
        return sb.toString();
    }

    public static String fireZeroForDate(String mmdd) {
        String[] temp = mmdd.split("-");
        StringBuilder sb = new StringBuilder();
        sb.append(temp[0]).append("-");
        if (temp[1].length() == 2 && temp[1].startsWith("0")) {
            sb.append(temp[1].replace("0", "")).append("-");
        } else {
            sb.append(temp[1]).append("-");
        }
        if (temp[2].length() == 2 && temp[2].startsWith("0")) {
            sb.append(temp[2].replace("0", ""));
        } else {
            sb.append(temp[2]);
        }
        return sb.toString();
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static String getMainTime(int time) {
        if (time < 0)
            return "----";
        StringBuilder sb = new StringBuilder();
        int day = (int) Math.floor((time / 60f) / 24f);
        if (day > 0) {
            sb.append(day + "天");
        }
        int hour = (int) Math.floor((time / 60f) % 24f);
        if (hour > 0) {
            sb.append(hour + "时");
        }
        sb.append((time % 60) + "分");
        return sb.toString();
    }

    //版本名
    public static String getVersionName() {
        return getPackageInfo().versionName;
    }

    //版本号
    public static int getVersionCode() {
        return getPackageInfo().versionCode;
    }

    private static PackageInfo getPackageInfo() {
        PackageInfo pi = null;
        try {
            PackageManager pm = InBroadcastHeartApplication.getContext().getPackageManager();
            pi = pm.getPackageInfo(InBroadcastHeartApplication.getContext().getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }
}
