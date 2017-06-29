package com.sharewisesc.inbroadheart.crash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class CrashHandler implements UncaughtExceptionHandler {

	public static final String TAG = "CrashHandler";

	private UncaughtExceptionHandler mDefaultHandler;
	private static CrashHandler INSTANCE = new CrashHandler();
	private Context mContext;
//	private String postCrashUrl = "http://api.superscene.cn/superscene/bjqb/?postAddAppCrash";
	private boolean isDeleteCrashFile = true;
	private String path = Environment.getExternalStorageDirectory().getPath() +"/inbroadheart/log/";
	private String fileName = "crash.log";
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");

	private CrashHandler() {
	}

	public static CrashHandler getInstance() {
		return INSTANCE;
	}

	public void init(Context context) {
		mContext = context;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
		} else {
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				Log.e(TAG, "error : ", e);
//			}
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
	}


	private boolean handleException(Throwable ex) {
		if (ex == null) {
//		    Log.e("info", "sssssssssssssssssssssssss");
			return false;
		}
		ex.printStackTrace();
//		new Thread() {
//			public void run() {
//				Looper.prepare();
//				String text = getText();
//				if(TextUtils.isEmpty(text))
//				    return ;
//				Toast.makeText(mContext, text, Toast.LENGTH_LONG)
//						.show();
//				Looper.loop();
//
//			};
//		}.start();

		// collectDeviceInfo(mContext);
		saveCrashInfo2File(ex, mContext);
		return true;
	}

	protected String getText(){
	    return "hahahha";
	}

	public static Map<String, String> collectDeviceInfo(Context ctx) {
		Map<String, String> infos = new HashMap<>();
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				Log.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.e(TAG, "an error occured when collect crash info", e);
			}
		}
		return infos;
	}

	protected String getPath(){
	    return path;
	}
	public String saveCrashInfo2File(Throwable ex, Context mContext) {

		StringBuffer sb = new StringBuffer();

		String path = getPath();

		try {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				Date nowtime = new Date();

				String time = "";
				do {
					time = formatter.format(nowtime);
				} while (time.length() > 19 || "".equals(time));
				File file = new File(path + fileName);
				if (!file.exists()) {
					sb.append("\n--------------设备信息------------\n");
					Map<String, String> infos = collectDeviceInfo(mContext);
					for (Map.Entry<String, String> entry : infos.entrySet()) {
						String key = entry.getKey();
						String value = entry.getValue();
						sb.append(key + "=" + value + "\n");
					}
				}

				sb.append("\n\n--------------" + time + "---------------\n");
				Writer writer = new StringWriter();
				PrintWriter printWriter = new PrintWriter(writer);
				ex.printStackTrace(printWriter);
				Throwable cause = ex.getCause();
				while (cause != null) {
					cause.printStackTrace(printWriter);
					cause = cause.getCause();
				}
				printWriter.close();
				String result = writer.toString();
				sb.append(result);

				FileOutputStream fos = new FileOutputStream(file, true);
				fos.write(sb.toString().getBytes());
				fos.close();
			}
			return fileName;
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing file...", e);
		}
		return null;
	}

	public void scaneCrashFile(){
		File file = new File(path);
		File[] files = file.listFiles();
		if(files != null){
			for(File f:files){
				if(f != null &&!f.isDirectory() && fileName.equals(f.getName())){
					writeCrashToNet(f);
				}
			}
		}


	}

	private void writeCrashToNet(File f) {
//		{“appid”:””,”deviceid”:””,”deviceinfo”:””,”error_info”:””}
		Crash crash = fillBean(f);
		if(crash != null){
			writeToNet(crash,f);
		}

	}


	private void writeToNet(final Crash crash,final  File f) {
//		RetrofitUtils.getApiService().sendErrMsg(crash.toString())
//				.observeOn(AndroidSchedulers.mainThread())
//				.subscribeOn(Schedulers.io())
//				.subscribe(d->{
//					if(f != null){
//						f.delete();
//					}
//				},e->e.printStackTrace());


//		new AsyncTask<Crash, Void, Boolean>(){
//
//			@Override
//			protected Boolean doInBackground(Crash... params) {
//				try {
//
//					DefaultHttpClient client = new DefaultHttpClient();
////					HttpPost post = new HttpPost("http://192.168.6.172/bjqb/?postAddAppCrash");
////					HttpPost post = new HttpPost("http://10.0.2.2:8080/test/test/a");
//					String data = crash.toJsonString();
//					if(data == null)
//						return false;
//
//					HttpPost post = new HttpPost(postCrashUrl);
//					post.setHeader("Authorization", "a89fb86c5ca5941640079c0eebb8391d");
//					StringEntity entity = new StringEntity(data, "UTF-8");
//					post.setEntity(entity);
//
//
//					HttpResponse execute = client.execute(post);
//					int code = execute.getStatusLine().getStatusCode();
//					switch (code/100) {
//					case 2:
//						return true;
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				return false;
//			}
//
//			protected void onPostExecute(Boolean result) {
//				if(result ){
//					f.delete();
//				}
//			};
//		}.execute(crash);
	}

	private Crash fillBean(File f) {
		BufferedReader reader =null;
		Crash crash = new Crash();
		try {
			crash.setAppid(mContext.getApplicationInfo().packageName);
			StringBuilder sb = new StringBuilder();
			FileInputStream in = new FileInputStream(f);
			String deviceId = "";
			boolean isDeviceinfo = true;
			reader = new BufferedReader(new InputStreamReader(in));
			for(String line = null;(line = reader.readLine())!= null;){
				if(line.contains("-------") && isDeviceinfo){
					crash.setDeviceinfo(sb.toString());
					sb.delete(0, sb.length());
					isDeviceinfo = false;
				}
				if(line.contains("MANUFACTURER")||line.contains("MODEL")){
					deviceId += line.split("=")[1];
				}
				sb.append(line+"\n");
			}
			crash.setError_info(sb.toString());
			crash.setDeviceid(deviceId);
			sb.delete(0, sb.length());
//			System.out.println(crash);
			return crash;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return null;
	}


	private static class Crash{
		private String appid;
		private String deviceid;
		private String deviceinfo;
		private String error_info;
		public String getAppid() {
			return appid;
		}
		public void setAppid(String appid) {
			this.appid = appid;
		}
		public String getDeviceid() {
			return deviceid;
		}
		public void setDeviceid(String deviceid) {
			this.deviceid = deviceid;
		}
		public String getDeviceinfo() {
			return deviceinfo;
		}
		public void setDeviceinfo(String deviceinfo) {
			this.deviceinfo = deviceinfo;
		}
		public String getError_info() {
			return error_info;
		}
		public void setError_info(String error_info) {
			this.error_info = error_info;
		}
		@Override
		public String toString() {
			return "Crash [appid=" + appid + ", deviceid=" + deviceid
					+ ", deviceinfo=" + deviceinfo + ", error_info="
					+ error_info + "]";
		}

		public String toJsonString() {
			try {
				JSONObject object = new JSONObject();
				object.put("appid", getAppid());
				object.put("deviceid", getDeviceid());
				object.put("deviceinfo", getDeviceinfo());
				object.put("errorinfo", getError_info());
				return object.toString();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

	}

}