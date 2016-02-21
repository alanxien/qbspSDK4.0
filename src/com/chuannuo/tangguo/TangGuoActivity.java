package com.chuannuo.tangguo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.chuannuo.tangguo.listener.ResponseStateListener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * TODO<请描述这个类是干什么的>
 * 
 * @author xie.xin
 * @data: 2015-7-1 下午10:56:19
 * @version: V1.0
 */
public class TangGuoActivity extends FragmentActivity implements
		OnClickListener, BaseFragment.BtnClickListener {

	private LinearLayout rootLinearLayout;
	private FrameLayout fLinearLayout;
	private LinearLayout rLinearLayout;
	private LinearLayout dLinearLayout;
	private TextView tv1;
	private TextView tv2;
	private TextView tvRecomm;
	private TextView tvDepth;
	private TextView tvTitle;
	private ImageView ivBack;

	private LinearLayout containerLayout;

	private FragmentRecomm fragmentRecomm;
	private FragmentDepth fragmentDepth;
	private FragmentDownLoad fragmentDownLoad;
	private FragmentManager mFragmentManager;

	private String color = Constant.ColorValues.BTN_NORMAL_COLOR;
	public static Drawable icon;
	private AlertDialog aDialog;
	protected SharedPreferences pref;
	protected Editor editor;
	private ProgressBar mPgBar;
	//private TextView mTvProgress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(null == pref){
			pref = getSharedPreferences(Constant.PREF_QIANBAO_SDK, Context.MODE_PRIVATE);
		}
		if(null == editor){
			editor = pref.edit();
		}
		initView();
		setContentView(this.rootLinearLayout);
		try {
			icon = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).applicationInfo.loadIcon(getPackageManager());
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PhoneInformation.initTelephonyManager(this);
		mFragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = mFragmentManager
				.beginTransaction();
		fragmentRecomm = new FragmentRecomm();
		fragmentDepth = new FragmentDepth();
		
		if(PhoneInformation.isSimReady()){
			transaction.add(Constant.IDValues.CONTAINER, fragmentRecomm);
			transaction.commit();
		}else{
			Toast.makeText(this, "请插入SIM卡", Toast.LENGTH_SHORT).show();
		}
	}

	private void initView() {
		Intent intent = getIntent();
		String c = intent.getExtras().getString("color");
		if (c != null && !c.equals("")) {
			color = c;
		}

		rootLinearLayout = new LinearLayout(this);
		containerLayout = new LinearLayout(this);
		fLinearLayout = new FrameLayout(this);
		rLinearLayout = new LinearLayout(this);
		dLinearLayout = new LinearLayout(this);
		initRLinearLayout();
		initDLinearLayout();
		initHeadLinearLayout();
		initContainerLayout();
		initRootLinearLayout();

	}

	/**
	 * @Title: initRLinearLayout
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	private void initRLinearLayout() {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		rLinearLayout.setLayoutParams(lp);
		rLinearLayout.setPadding(0, 12, 0, 12);
		rLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		rLinearLayout.setBackgroundColor(Color
				.parseColor(Constant.ColorValues.BLUE));

		tvRecomm = new TextView(this);
		tvDepth = new TextView(this);

		LinearLayout.LayoutParams tlp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		tlp.weight = 1;
		tvRecomm.setLayoutParams(tlp);
		tvRecomm.setPadding(10, 0, 10, 0);
		tvRecomm.setGravity(Gravity.CENTER);
		tvRecomm.setTextSize(15);
		tvRecomm.setText(Constant.StringValues.RECOMM_TASK);
		tvRecomm.setTextColor(Color.parseColor(Constant.ColorValues.WHITE));
		tvRecomm.setId(Constant.IDValues.TV_RECOMM);
		tvRecomm.setOnClickListener(this);

		tvDepth.setLayoutParams(tlp);
		tvDepth.setPadding(10, 0, 10, 0);
		tvDepth.setGravity(Gravity.CENTER);
		tvDepth.setTextSize(15);
		tvDepth.setText(Constant.StringValues.UNFINISHED_TASK);
		tvDepth.setTextColor(Color.parseColor(Constant.ColorValues.WHITE));
		tvDepth.setId(Constant.IDValues.TV_DEPTH);
		tvDepth.setOnClickListener(this);

		rLinearLayout.addView(tvRecomm);
		rLinearLayout.addView(tvDepth);
	}

	/**
	 * @Title: initDLinearLayout
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	private void initDLinearLayout() {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		dLinearLayout.setLayoutParams(lp);
		dLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		dLinearLayout.setBackgroundColor(Color
				.parseColor(Constant.ColorValues.BLUE));

		tv1 = new TextView(this);
		tv2 = new TextView(this);

		LinearLayout.LayoutParams tlp = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, 6);
		tlp.weight = 1;
		tv1.setLayoutParams(tlp);
		tv2.setLayoutParams(tlp);
		tv1.setBackgroundColor(Color
				.parseColor(Constant.ColorValues.BTN_NORMAL_COLOR));
		tv2.setBackgroundColor(Color.parseColor(Constant.ColorValues.BLUE));

		dLinearLayout.addView(tv1);
		dLinearLayout.addView(tv2);

	}

	/**
	 * @Title: initHeadLinearLayout
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	private void initHeadLinearLayout() {
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		fLinearLayout.setLayoutParams(lp);
		fLinearLayout.setPadding(13, 13, 13, 13);
		fLinearLayout.setBackgroundColor(Color.parseColor(color));

		tvTitle = new TextView(this);
		ivBack = new ImageView(this);

		LinearLayout.LayoutParams ilp = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		ivBack.setLayoutParams(ilp);
		ivBack.setPadding(20, 0, 20, 0);
		ivBack.setId(Constant.IDValues.BACK);
		ivBack.setImageBitmap(ResourceUtil.getImageFromAssetsFile(this,
				"back.png"));
		ivBack.setOnClickListener(this);

		LinearLayout.LayoutParams tlp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		tvTitle.setLayoutParams(tlp);
		tvTitle.setTextSize(17);
		tvTitle.setPadding(10, 10, 10, 10);
		tvTitle.setGravity(Gravity.CENTER);
		tvTitle.setTextColor(Color.parseColor(Constant.ColorValues.WHITE));
		tvTitle.setText(Constant.StringValues.TITLE);

		fLinearLayout.addView(ivBack);
		fLinearLayout.addView(tvTitle);
	}

	/**
	 * @Title: initRootLinearLayout
	 * @Description: 初始化跟布局
	 * @param
	 * @return void
	 * @throws
	 */
	private void initRootLinearLayout() {
		LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		rootLinearLayout.setOrientation(LinearLayout.VERTICAL);
		rootLinearLayout.setLayoutParams(rlp);
		rootLinearLayout.setBackgroundColor(Color
				.parseColor(Constant.ColorValues.WHITE));
		rootLinearLayout.addView(fLinearLayout);
		rootLinearLayout.addView(rLinearLayout);
		rootLinearLayout.addView(dLinearLayout);
		rootLinearLayout.addView(containerLayout);
	}

	/**
	 * @Title: initContainerLayout
	 * @Description: fragment容器
	 * @param
	 * @return void
	 * @throws
	 */
	private void initContainerLayout() {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		containerLayout.setId(Constant.IDValues.CONTAINER);
		containerLayout.setOrientation(LinearLayout.VERTICAL);
		containerLayout.setLayoutParams(lp);
	}

	@Override
	public void onClick(View v) {
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		switch (v.getId()) {
		case Constant.IDValues.TV_RECOMM:
			tv1.setBackgroundColor(Color
					.parseColor(Constant.ColorValues.BTN_NORMAL_COLOR));
			tv2.setBackgroundColor(Color.parseColor(Constant.ColorValues.BLUE));
			if(PhoneInformation.isSimReady()){
				if (!fragmentRecomm.isAdded()) { // 先判断是否被add过
					transaction.hide(fragmentDepth)
							.add(Constant.IDValues.CONTAINER, fragmentRecomm)
							.commit();
				} else {
					transaction.hide(fragmentDepth).show(fragmentRecomm).commit();
				}
			}else{
				Toast.makeText(this, "请插入SIM卡", Toast.LENGTH_SHORT).show();
			}
			
			break;
		case Constant.IDValues.TV_DEPTH:
			tv1.setBackgroundColor(Color.parseColor(Constant.ColorValues.BLUE));
			tv2.setBackgroundColor(Color
					.parseColor(Constant.ColorValues.BTN_NORMAL_COLOR));

			if(PhoneInformation.isSimReady()){
				if (!fragmentDepth.isAdded()) { // 先判断是否被add过
					transaction.hide(fragmentRecomm)
							.add(Constant.IDValues.CONTAINER, fragmentDepth)
							.commit();
				} else {
					transaction.hide(fragmentRecomm).show(fragmentDepth).commit();
				}
			}else{
				Toast.makeText(this, "请插入SIM卡", Toast.LENGTH_SHORT).show();
			}
			
			break;
		case Constant.IDValues.BACK:
			if (rLinearLayout.getVisibility() == View.GONE) {
				mFragmentManager.popBackStack();
				rLinearLayout.setVisibility(View.VISIBLE);
				dLinearLayout.setVisibility(View.VISIBLE);
				tvTitle.setText(Constant.StringValues.TITLE);
			} else {
				this.finish();
			}

			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(fragmentDownLoad != null && fragmentDownLoad.popupWindow != null && fragmentDownLoad.popupWindow.isShowing()){
				fragmentDownLoad.popupWindow.dismiss();
				return true;
			}else if (rLinearLayout.getVisibility() == View.GONE) {
				rLinearLayout.setVisibility(View.VISIBLE);
				dLinearLayout.setVisibility(View.VISIBLE);
				tvTitle.setText(Constant.StringValues.TITLE);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBtnClickListener(int step, AppInfo appInfo) {
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		switch (step) {
		case Constant.STEP_1:
			fragmentDownLoad = new FragmentDownLoad();
			transaction.addToBackStack(null);
			Bundle bundle = new Bundle();
			bundle.putSerializable("item", appInfo);
			fragmentDownLoad.setArguments(bundle);
			transaction.replace(Constant.IDValues.CONTAINER, fragmentDownLoad);
			transaction.commit();

			rLinearLayout.setVisibility(View.GONE);
			dLinearLayout.setVisibility(View.GONE);
			tvTitle.setText(Constant.StringValues.APP_DETAIL);
			break;
		default:
			break;
		}
	}
	
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        // TODO Auto-generated method stub  
        super.onActivityResult(requestCode, resultCode, data);  
        if(resultCode == RESULT_OK && data != null){  
              
            Uri selectedImage = data.getData();  
            String[] filePathColumn = { MediaStore.Images.Media.DATA };  
   
            Cursor cursor = getContentResolver().query(selectedImage,  
                    filePathColumn, null, null, null);  
            cursor.moveToFirst();  
   
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);  
            final String picturePath = cursor.getString(columnIndex);  
            cursor.close();
            ImageView view = new ImageView(this);
            try {
				view.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            aDialog = new AlertDialog.Builder(this).setTitle("确认上传这张图片？") 
            .setPositiveButton("确定", new DialogInterface.OnClickListener() { 
         
                @Override 
                public void onClick(DialogInterface dialog, int which) { 
                	// 点击“确认”后的上传图片
                	uploadFile(picturePath);
                } 
            }) 
            .setNegativeButton("返回", new DialogInterface.OnClickListener() { 
         
                @Override 
                public void onClick(DialogInterface dialog, int which) { 
                	// 点击“返回”
                	aDialog.dismiss();
                } 
            }).setView(view).show(); 
        }  
    }
	
	/** 
	* @Title: uploadFile 
	* @Description: TODO
	* @author  xie.xin
	* @param 
	* @return void 
	* @throws 
	*/
	private void uploadFile(String file) {
		AppInfo appInfo = fragmentDownLoad.appInfo;
		mPgBar = new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);
		fragmentDownLoad.linearLayout4.addView(mPgBar,0);
		if(appInfo != null){
			
		}
	    if(appInfo.getClicktype() == 1){
//	    	setParams("app_id", pref.getString(Constant.APP_ID, "0"));
//	    	setParams("code", pref.getString(Constant.CODE, "0"));
//	    	setParams("ad_id",appInfo.getAdId()+"");
//	    	setParams("resource_id", appInfo.getResource_id()+"");
	    	String url = Constant.URL.UPLOADS_PHOTO_H5 + "&app_id="+pref.getString(Constant.APP_ID, "0")+
	    			"&code="+pref.getString(Constant.CODE, "0")+"&ad_id="+appInfo.getAdId()+"&resource_id="+
	    			appInfo.getResource_id();
	    	new uploadTask().execute(file,url);
	    }else{
//	    	RequestParams params = new RequestParams();  
//		    try {
//				params.put("photo", file);
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}  
//		    params.put("ad_install_id", appInfo.getInstall_id());
//			params.put("code", pref.getString(Constant.CODE, "0"));
//			
//	    	upLoading(Constant.UPLOADS_PHOTO,params);
//	    	setParams("app_id", pref.getString(Constant.APP_ID, "0"));
//	    	setParams("code", pref.getString(Constant.CODE, "0"));
//	    	setParams("ad_id",appInfo.getAdId()+"");
//	    	setParams("resource_id", appInfo.getResource_id()+"");
//	    	new uploadTask().execute(Constant.URL.UPLOADS_PHOTO);
	    }
	    
	}
	
	class uploadTask extends AsyncTask<String, Integer, String>{

		@Override
		protected void onPostExecute(String result) {
			//mTvProgress.setText(result);	
		}

		@Override
		protected void onPreExecute() {
			//mTvProgress.setText("loading...");
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			if(values[0]>99){
				fragmentDownLoad.linearLayout4.removeView(mPgBar);fdsfd
				Toast.makeText(TangGuoActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
			}else{
				mPgBar.setProgress(values[0]);
			}
			
			//mTvProgress.setText("loading..." + values[0] + "%");
		}

		@Override
		protected String doInBackground(String... params) {
			String filePath = params[0];
			String uploadUrl = params[1];
			String end = "\r\n";
			String twoHyphens = "--";
			String boundary = "******";
			try {
				URL url = new URL(uploadUrl);
				HttpURLConnection httpURLConnection = (HttpURLConnection) url
						.openConnection();
				httpURLConnection.setDoInput(true);
				httpURLConnection.setDoOutput(true);
				httpURLConnection.setUseCaches(false);
				httpURLConnection.setRequestMethod("POST");
				httpURLConnection.setConnectTimeout(6*1000);
				httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
				httpURLConnection.setRequestProperty("Charset", "UTF-8");
				httpURLConnection.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);

				DataOutputStream dos = new DataOutputStream(httpURLConnection
						.getOutputStream());
				dos.writeBytes(twoHyphens + boundary + end);
				dos
						.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
								+ filePath.substring(filePath.lastIndexOf("/") + 1)
								+ "\"" + end);
				dos.writeBytes(end);

				FileInputStream fis = new FileInputStream(filePath);
				long total = fis.available();
				String totalstr = String.valueOf(total);
				Log.d("文件大小", totalstr);
				byte[] buffer = new byte[8192]; // 8k
				int count = 0;
				int length = 0;
				while ((count = fis.read(buffer)) != -1) {
					dos.write(buffer, 0, count);
					length += count;
					publishProgress((int) ((length / (float) total) * 100));
					//为了演示进度,休眠500毫秒
					Thread.sleep(100);
				}			
				fis.close();
				dos.writeBytes(end);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
				dos.flush();

				InputStream is = httpURLConnection.getInputStream();
				InputStreamReader isr = new InputStreamReader(is, "utf-8");
				BufferedReader br = new BufferedReader(isr);
				@SuppressWarnings("unused")
				String result = br.readLine();
				dos.close();
				is.close();
				return "上传成功";
		}catch (Exception e) {
			e.printStackTrace();
			return "上传失败";
		}	
	}
}

}
