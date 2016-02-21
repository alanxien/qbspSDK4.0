/* 
 * @Title:  FragmentRecomm.java 
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved 
 * @Description:  TODO<请描述此文件是做什么的> 
 * @author:  xie.xin
 * @data:  2015-7-18 上午1:45:47 
 * @version:  V1.0 
 */
package com.chuannuo.tangguo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chuannuo.tangguo.listener.ResponseStateListener;
import com.chuannuo.tangguo.task.SpendPointTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * TODO<请描述这个类是干什么的>
 * 
 * @author xie.xin
 * @data: 2015-7-18 上午1:45:47
 * @version: V1.0
 */
public class FragmentDepth extends BaseFragment {

	private LinearLayout view;
	private ListView myListView;
	private ArrayList<AppInfo> depthList;
	private DepthTaskAdapter adapter;
	private TextView tvTips;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		initView();
		if (PhoneInformation.isSimReady()) {
			initData();
		}
		return view;
	}

	@Override
	public void onResume() {
		if (depthList != null && depthList.size() > 0) {
			AppInfo app = new AppInfo();
			for (int i = depthList.size() - 1; i >= 0; i--) {
				app = depthList.get(i);
				if (app.getResource_id() == pref
						.getInt(Constant.S_RESOURCE_ID, 0)) {
					depthList.remove(i);
					break;
				}
			}
			if (adapter != null) {
				adapter.notifyDataSetChanged();
			}

		}
		super.onResume();
	}

	/**
	 * @Title: initView
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	private void initView() {
		view = super.getRootLinearLayout();

		myListView = new ListView(getActivity());
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		myListView.setDivider(new ColorDrawable(Color
				.parseColor(Constant.ColorValues.DIVIDER_COLOR)));
		myListView.setDividerHeight(1);
		myListView.setId(Constant.IDValues.LV_RECOMM);
		myListView.setLayoutParams(lp);
		myListView.setCacheColorHint(0);

		tvTips = new TextView(getActivity());
		tvTips.setLayoutParams(lp);
		tvTips.setText(Constant.StringValues.DEPTH_TIPS);
		tvTips.setTextSize(17);
		tvTips.setTextColor(Color.parseColor(Constant.ColorValues.SIZE_COLOR));
		tvTips.setPadding(10, 30, 10, 0);
		tvTips.setVisibility(View.GONE);

		view.addView(tvTips);
		view.addView(myListView);
	}

	/**
	 * @Title: initData
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	private void initData() {
		if (null == depthList) {
			depthList = new ArrayList<AppInfo>();
		}
		if(depthList.size() <= 0){
			initProgressDialog(Constant.StringValues.LOADING);
			progressDialog.show();

			HttpUtil.setParams("app_id", pref.getString(Constant.APP_ID, "0"));
			HttpUtil.post(Constant.URL.UN_FINISHED_TASK,
					new ResponseStateListener() {

						@Override
						public void onSuccess(Object result) {
							if (null != result
									&& !result.equals(Constant.NET_ERROR)) {
								JSONObject jsonObject;
								try {
									jsonObject = new JSONObject(result.toString());
									String code = jsonObject.getString("code");
									if (code.equals("1")) {
										JSONArray jArray = jsonObject
												.getJSONArray("data");
										if (null != jArray && jArray.length() > 0) {
											for (int i = 0; i < jArray.length(); i++) {
												JSONObject obj = jArray
														.getJSONObject(i);
												String s = obj
														.getString("resourceArr");
												if (!s.equals("[]")) {
													JSONObject childObj = obj
															.getJSONObject("resourceArr");
													// if(checkPackage(obj.getString("package_name"))){
													// //判断用户是否已经安装该软件
													AppInfo appInfo = new AppInfo();
													appInfo.setInstall_id(obj
															.getInt("ad_install_id"));
													if (null != childObj) {
														appInfo.setTitle(childObj
																.getString("title"));
														appInfo.setResource_id(childObj
																.getInt("id"));
														appInfo.setAdId(childObj
																.getInt("ad_id"));
														appInfo.setResource_size(childObj
																.getString("resource_size"));
														appInfo.setB_type(childObj
																.getInt("btype"));

														String fileUrl = childObj
																.getString("file");
														String iconUrl = childObj
																.getString("icon");
														if (!fileUrl
																.contains("http")) {
															fileUrl = Constant.URL.ROOT_URL
																	+ fileUrl;
														}
														if (!iconUrl
																.contains("http")) {
															iconUrl = Constant.URL.ROOT_URL
																	+ iconUrl;
														}

														appInfo.setFile(fileUrl);
														appInfo.setIcon(iconUrl);
													}
													appInfo.setSign_times(obj
															.getInt("sign_count"));
													appInfo.setNeedSign_times(obj
															.getInt("sign_number"));
													appInfo.setSign_rules(obj
															.getInt("reportsigntime"));
													appInfo.setPackage_name(obj
															.getString("package_name"));
													appInfo.setIsAddIntegral(obj
															.getInt("is_add_integral"));
													appInfo.setScore(obj
															.getInt("integral"));

													if (appInfo.getIsAddIntegral() == 0
															|| isSignTime(obj)) {
														depthList.add(appInfo);
													}

												}
											}
										}

										Message msg = mHandler.obtainMessage();
										msg.what = 1;
										mHandler.sendMessage(msg);
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} finally {
									progressDialog.dismiss();
								}

							} else {
								Toast.makeText(getActivity(), "获取数据失败",
										Toast.LENGTH_SHORT).show();
								progressDialog.dismiss();
							}
						}

						@SuppressLint("SimpleDateFormat")
						private boolean isSignTime(JSONObject obj) {
							SimpleDateFormat df = new SimpleDateFormat(
									"yyyy-MM-dd hh:mm:ss");
							String date;
							long time;
							try {
								date = (null == obj.getString("update_date") || obj
										.getString("update_date").equals("null")) ? obj
										.getString("create_date") : obj
										.getString("update_date");

								time = df.parse(date).getTime()
										+ obj.getLong("reportsigntime") * 24 * 60
										* 60 * 1000;
								if (time < System.currentTimeMillis()) {
									return true;
								}
							} catch (ParseException | JSONException e) {
								// TODO Auto-generated catch
								// block
								e.printStackTrace();
							}

							return false;
						}

					});
		}
		
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (depthList.size() > 0) {
					tvTips.setVisibility(View.GONE);
					myListView.setVisibility(View.VISIBLE);
					if (null == adapter) {
						adapter = new DepthTaskAdapter(getActivity(),
								depthList, myListView);
					} else {
						adapter.notifyDataSetChanged();
					}
					myListView.setAdapter(adapter);
				} else {
					tvTips.setVisibility(View.VISIBLE);
					myListView.setVisibility(View.GONE);
				}

				break;
			default:
				break;
			}
		}
	};
}
