package com.chuannuo.tangguo;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chuannuo.tangguo.SyncImageLoader.ImageCallback;
import com.chuannuo.tangguo.listener.ResponseStateListener;
import com.chuannuo.tangguo.task.SpendPointTask;

public class DepthTaskAdapter extends BaseAdapter {

	public static String TAG = "DepthTaskAdapter";

	ArrayList<AppInfo> infoList;
	Context context;
	LayoutInflater la;
	String appName;
	Timer timer;
	SharedPreferences pref;
	Editor editor;
	AppInfo appInfo;
	int position;
	private ListView mListView;

	private RelativeLayout relativeLayout;
	private ImageView imageView;
	private LinearLayout linearLayout;
	private TextView app_name;
	private TextView app_sign_rule; // 签到规则(隔几天才能签到，签到几次完成任务)
	private TextView app_sign; // 签到按钮
	private TextView app_sign_times; // 签到次数
	private View view;

	public DepthTaskAdapter(final Context context, ArrayList<AppInfo> list,
			ListView listView) {
		this.context = context;
		this.infoList = list;
		if (null == pref) {
			pref = context.getSharedPreferences(Constant.PREF_QIANBAO_SDK,
					Context.MODE_PRIVATE);
		}
		if (null == editor) {
			editor = pref.edit();
		}
		mListView = listView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.infoList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return infoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = getConverView();

			holder = new ViewHolder();
			holder.app_name = app_name;
			holder.app_icon = imageView;
			holder.app_sign_rule = app_sign_rule;
			holder.app_sign = app_sign;
			holder.app_sign_times = app_sign_times;
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		view = convertView;
		holder.app_name.setText(this.infoList.get(position).getTitle());
		holder.app_sign_rule.setText("隔"
				+ this.infoList.get(position).getSign_rules() + "天签一次到，签到"
				+ this.infoList.get(position).getNeedSign_times() + "次完成任务");
		holder.app_sign_times.setText("已签到："
				+ this.infoList.get(position).getSign_times() + "");
		String url = this.infoList.get(position).getIcon();
		holder.app_icon.setTag(url);
		Drawable cachedImage = SyncImageLoader.getInstance().loadDrawable(url,
				new ImageCallback() {
					public void imageLoaded(Drawable imageDrawable,
							String imageUrl) {
						ImageView imageViewByTag = (ImageView) mListView
								.findViewWithTag(imageUrl);
						if (imageViewByTag != null) {
							imageViewByTag.setImageDrawable(imageDrawable);
						}
					}
				});
		if (cachedImage == null) {
			holder.app_icon.setImageDrawable(TangGuoActivity.icon);
		} else {
			holder.app_icon.setImageDrawable(cachedImage);
		}

		holder.app_sign.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 应用被卸载，重新安装
				if (checkPackage(infoList.get(position).getPackage_name())) {// 如果
																				// 应用已经安装
					doStartApplicationWithPackageName(infoList.get(position)
							.getPackage_name());
					// editor.putLong(Constant.APP_RUNNING_TIME,
					// System.currentTimeMillis());
					// editor.commit();

					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							if (isTopActivity(infoList.get(position)
									.getPackage_name())) {
								Message msg = mHandler.obtainMessage(1,
										infoList.get(position));
								msg.arg1 = infoList.get(position)
										.getSign_rules();
								msg.arg2 = infoList.get(position)
										.getIsAddIntegral();
								mHandler.sendMessage(msg);
							} else {
								Message msg = mHandler.obtainMessage();
								msg.what = 2;
								mHandler.sendMessage(msg);
							}
						}

					}, 2*60 * 1000);
					//
					// timer = new Timer();
					//
					// timer.schedule(new TimerTask() {
					//
					// @Override
					// public void run() {
					// if (isTopActivity(infoList.get(position)
					// .getPackage_name())) {
					// timer.cancel();
					// Message msg = mHandler.obtainMessage(1,
					// infoList.get(position));
					// msg.arg1 = infoList.get(position)
					// .getSign_rules();
					// msg.arg2 = infoList.get(position)
					// .getIsAddIntegral();
					// mHandler.sendMessage(msg);
					// } else {
					// Message msg = mHandler.obtainMessage();
					// msg.what = 2;
					// mHandler.sendMessage(msg);
					// timer.cancel();
					// }
					// if (isTopActivity(infoList.get(position)
					// .getPackage_name())) {
					// if (moreThanTimes(System.currentTimeMillis(), 2)) {
					// Log.i(TAG, "---运行超过2分钟，开始签到。。。---");
					// timer.cancel();
					// Message msg = mHandler.obtainMessage(1,
					// infoList.get(position));
					// msg.arg1 = infoList.get(position)
					// .getSign_rules();
					// msg.arg2 = infoList.get(position)
					// .getIsAddIntegral();
					// mHandler.sendMessage(msg);
					// } else {
					// Log.i(TAG, "---正在签到。。。---"+System.currentTimeMillis());
					// }
					// } else {
					// Log.i(TAG, "---退出签到---");
					// Message msg = mHandler.obtainMessage();
					// msg.what = 2;
					// mHandler.sendMessage(msg);
					// timer.cancel();
					// }
					// editor.commit();
					// }
					// }, 3000, 2*60*1000);
				} else {
					Intent intent = new Intent(context, DownloadService.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable(Constant.ITEM,
							infoList.get(position));
					bundle.putBoolean("isRepeatDown", true);
					intent.putExtras(bundle);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startService(intent);
				}
			}
		});

		return convertView;
	}

	private View getConverView() {
		relativeLayout = new RelativeLayout(context);
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		relativeLayout.setLayoutParams(lp);
		relativeLayout.setPadding(0, 10, 0, 10);

		imageView = new ImageView(context);
		RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(120,
				120);
		lp1.setMargins(10, 0, 10, 10);
		imageView.setLayoutParams(lp1);
		imageView.setImageBitmap(ResourceUtil.getImageFromAssetsFile(context,
				"tangguo.png"));
		imageView.setId(Constant.IDValues.IV_LOGO);

		linearLayout = new LinearLayout(context);
		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp2.leftMargin = 140;
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setLayoutParams(lp2);
		linearLayout.setId(Constant.IDValues.LL1);

		app_name = new TextView(context);
		LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(400,
				LayoutParams.WRAP_CONTENT);
		app_name.setLayoutParams(lp3);
		app_name.setTextSize(15);
		app_name.setTextColor(Color
				.parseColor(Constant.ColorValues.TITLE_COLOR));
		app_name.setText(Constant.StringValues.APP_NAME);
		app_name.setEllipsize(TruncateAt.END);
		app_name.setSingleLine(true);
		app_name.setId(Constant.IDValues.TV_APP_NAME);

		app_sign_times = new TextView(context);
		LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		app_sign_times.setLayoutParams(lp4);
		app_sign_times.setTextSize(15);
		app_sign_times.setTextColor(Color
				.parseColor(Constant.ColorValues.SIZE_COLOR));
		app_sign_times.setText("大小：6.8M");
		app_sign_times.setEllipsize(TruncateAt.END);
		app_sign_times.setSingleLine(true);
		app_sign_times.setId(Constant.IDValues.SIGN_TIMES);

		app_sign_rule = new TextView(context);
		app_sign_rule.setLayoutParams(lp4);
		app_sign_rule.setTextSize(15);
		app_sign_rule.setTextColor(Color
				.parseColor(Constant.ColorValues.SIZE_COLOR));
		app_sign_rule.setText("描述");
		app_sign_rule.setEllipsize(TruncateAt.END);
		app_sign_rule.setSingleLine(true);
		app_sign_rule.setId(Constant.IDValues.SIGN_RELUS);

		linearLayout.addView(app_name);
		linearLayout.addView(app_sign_times);
		linearLayout.addView(app_sign_rule);

		app_sign = new TextView(context);
		RelativeLayout.LayoutParams lp5 = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp5.rightMargin = 10;
		lp5.addRule(RelativeLayout.ALIGN_TOP, Constant.IDValues.LL1);
		lp5.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		app_sign.setLayoutParams(lp5);
		app_sign.setTextSize(17);
		app_sign.setPadding(20, 10, 20, 10);
		app_sign.setTextColor(Color.parseColor(Constant.ColorValues.WHITE));
		app_sign.setText("签到");
		app_sign.setEllipsize(TruncateAt.END);
		app_sign.setSingleLine(true);
		app_sign.setId(Constant.IDValues.SIGN);
		app_sign.setBackgroundColor(Color
				.parseColor(Constant.ColorValues.BTN_NORMAL_COLOR));

		relativeLayout.addView(imageView);
		relativeLayout.addView(linearLayout);
		relativeLayout.addView(app_sign);
		return relativeLayout;
	}

	class ViewHolder {
		public ImageView app_icon;
		public TextView app_name;
		public TextView app_sign_rule; // 签到规则(隔几天才能签到，签到几次完成任务)
		public TextView app_sign; // 签到按钮
		public TextView app_sign_times; // 签到次数
		public TextView tv_is_add_ntegral;// 提示是否已经获取下载积分
	}

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		public void handleMessage(final Message msg) {
			final AppInfo res = (AppInfo) msg.obj;
			switch (msg.what) {
			case 1:
				HttpUtil.setParams("app_id",
						pref.getString(Constant.APP_ID, "0"));
				HttpUtil.setParams("ad_install_id", res.getInstall_id() + "");
				HttpUtil.setParams("key", PhoneInformation.getMetaData(context,
						Constant.TANGGUO_APPKEY));
				HttpUtil.setParams("channel_id", PhoneInformation.getMetaData(
						context, Constant.TANGGUO_APPID));
				PhoneInformation.initTelephonyManager(context);
				HttpUtil.setParams("imei", PhoneInformation.getImei());
				HttpUtil.setParams("imsi", PhoneInformation.getImsi());
				HttpUtil.setParams("machineType",
						PhoneInformation.getMachineType());
				HttpUtil.setParams("net_type", PhoneInformation.getNetType()
						+ "");
				HttpUtil.setParams("macaddress",
						PhoneInformation.getMacAddress());
				HttpUtil.setParams("androidid", Secure.getString(
						context.getContentResolver(), Secure.ANDROID_ID));
				/*
				 * 增加积分（防止用户第一次下载应用没有体验足够长时间，而没有加积分）
				 */
				if (res.getIsAddIntegral() == 0) {
					String alter = res.getAlert();
					if(alter != null){
						Toast.makeText(context, alter, Toast.LENGTH_LONG)
						.show();
					}
					
					HttpUtil.post(Constant.URL.CONFIRM_INSTALL_INTEGRAL,
							new ResponseStateListener() {

								@Override
								public void onSuccess(Object result) {
									try {
										if (null != result
												&& !result
														.equals(Constant.NET_ERROR)) {
											JSONObject jsonObject;
											jsonObject = new JSONObject(result
													.toString());
											String code = jsonObject
													.getString("code");

											if (code.equals("1")) {
												infoList.remove(position);
												JSONObject data = jsonObject
														.getJSONObject("data");
												TangGuoWall.tangGuoWallListener
														.onSign(Constant.ACCESS_SUCCESS,
																data.getString("ad_name"),
																data.getInt("integral"));
												editor.putInt(
														Constant.S_RESOURCE_ID,
														res.getResource_id());
												editor.commit();
											} else {
												TangGuoWall.tangGuoWallListener
														.onSign(Constant.ACCESS_FAILURE,
																"", 0);
											}
										}
									} catch (Exception e) {
										// TODO: handle exception
									}

								};
							});
				} else {

					Toast.makeText(context, "试玩两分钟即完成签到", Toast.LENGTH_LONG)
							.show();
					/*
					 * 签到
					 */
					HttpUtil.post(Constant.URL.REPEAT_SIGN_URL,
							new ResponseStateListener() {

								@Override
								public void onSuccess(Object result) {
									try {
										if (null != result
												&& !result
														.equals(Constant.NET_ERROR)) {
											JSONObject jsonObject;
											jsonObject = new JSONObject(result
													.toString());
											String code = jsonObject
													.getString("code");
											if (code.equals("1")) {
												JSONObject data = jsonObject
														.getJSONObject("data");
												TangGuoWall.tangGuoWallListener
														.onSign(Constant.ACCESS_SUCCESS,
																data.getString("ad_name"),
																data.getInt("integral"));
												editor.putInt(
														Constant.S_RESOURCE_ID,
														res.getResource_id());
												editor.commit();
											} else {
												TangGuoWall.tangGuoWallListener
														.onSign(Constant.ACCESS_FAILURE,
																"", 0);
											}
											editor.commit();
										}
									} catch (Exception e) {
										// TODO: handle exception
									}

								};
							});
				}
				break;
			case 2:
				TangGuoWall.tangGuoWallListener.onSign(Constant.ACCESS_FAILURE,
						"", 0);
				break;
			default:
				break;
			}
		}
	};

	/**
	 * @author alan.xie
	 * @date 2014-12-18 下午3:27:00
	 * @Description: 通过包名启动应用程序
	 * @param @param packagename
	 * @return void
	 */
	private void doStartApplicationWithPackageName(String packagename) {

		// 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
		PackageInfo packageinfo = null;
		try {
			packageinfo = context.getPackageManager().getPackageInfo(
					packagename, 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (packageinfo == null) {
			return;
		}

		// 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(packageinfo.packageName);

		// 通过getPackageManager()的queryIntentActivities方法遍历
		List<ResolveInfo> resolveinfoList = context.getPackageManager()
				.queryIntentActivities(resolveIntent, 0);

		ResolveInfo resolveinfo = resolveinfoList.iterator().next();
		if (resolveinfo != null) {
			// packagename = 参数packname
			String packageName = resolveinfo.activityInfo.packageName;
			// 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
			String className = resolveinfo.activityInfo.name;
			// LAUNCHER Intent
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);

			// 设置ComponentName参数1:packagename参数2:MainActivity路径
			ComponentName cn = new ComponentName(packageName, className);

			intent.setComponent(cn);
			context.startActivity(intent);
		}
	}

	/**
	 * @author alan.xie
	 * @date 2014-12-18 下午5:42:49
	 * @Description: 检测应用程序是否在前台运行
	 * @param @param packageName
	 * @param @return
	 * @return boolean
	 */
	private boolean isTopActivity(String packageName) {

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
		if (tasksInfo.size() > 0) {
			Log.i(TAG, "---------------包名-----------" + packageName);
			// 应用程序位于堆栈的顶层
			if (packageName.equals(tasksInfo.get(0).topActivity
					.getPackageName())) {
				Log.i(TAG, packageName);
				return true;
			}
		}
		return false;
	}

	/**
	 * @author alan.xie
	 * @date 2014-10-29 上午9:40:47
	 * @Description: 计算时间间隔是否大于days
	 * @param @return
	 * @return Boolean
	 */
	public Boolean moreThanTimes(long currTimes, int minute) {
		long times = pref.getLong(Constant.APP_RUNNING_TIME, 0);
		if (currTimes - times > minute * 60 * 1000) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @author alan.xie
	 * @date 2014-12-17 下午2:45:48
	 * @Description: 检测该包名所对应的应用是否存在
	 * @param @param packageName
	 * @param @return
	 * @return boolean
	 */
	public boolean checkPackage(String packageName) {
		if (packageName == null || "".equals(packageName)) {
			return false;
		}
		try {
			context.getPackageManager().getApplicationInfo(packageName,
					PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

}
