/* 
 * @Title:  FragmentRecomm.java 
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved 
 * @Description:  TODO<请描述此文件是做什么的> 
 * @author:  xie.xin
 * @data:  2015-7-18 上午1:45:47 
 * @version:  V1.0 
 */
package com.chuannuo.tangguo;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chuannuo.tangguo.SyncImageLoader.ImageCallback;

/**
 * TODO<请描述这个类是干什么的>
 * 
 * @author xie.xin
 * @data: 2015-7-18 上午1:45:47
 * @version: V1.0
 */
public class FragmentDownLoad extends BaseFragment {

	private LinearLayout view;
	private ScrollView scrollView;
	private LinearLayout linearLayout1;
	private LinearLayout linearLayout2;
	private LinearLayout linearLayout3;
	public LinearLayout linearLayout4;
	private LinearLayout linearLayout5; // what do???
	private LinearLayout linearLayout6; // step 1
	private LinearLayout linearLayout7; // step 2
	private LinearLayout linearLayout8; // step 3
	public LinearLayout linearLayout9; // upload image
	public LinearLayout linearLayout10;

	private ImageView iv_logo;
	private TextView tv_app_name;
	private TextView tv_size;
	private TextView tv_line;
	private ImageView iv_how;
	private TextView tv_how;
	private TextView tv_screen;
	private ImageView ivStep1;
	private ImageView ivStep2;
	private ImageView ivStep3;
	private ImageView ivStep4;
	private TextView tv_tips1;
	private TextView tv_tips2;
	private TextView tv_tips3;
	private TextView tv_tips4;
	private RelativeLayout rl_upload; // 示例图
	private ImageView iv_example;
	public ImageView iv_upload;

	public TextView tv_desc;
	private TextView tv_downLoad;
	private TextView tv_total_score;
	private Drawable cachedImage;
	private Drawable cachedImage1;
	public AppInfo appInfo;
	public PopupWindow popupWindow;
	
	private ListView myListView;
	private ArrayList<String> sList;
	private ArrayAdapter<String> mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		initView();
		initData(); 
		return view;
	}

	private void initView() {
		appInfo = (AppInfo) getArguments().getSerializable("item");
		view = super.getRootLinearLayout();

		scrollView = new ScrollView(getActivity());
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		scrollView.setLayoutParams(lp);

		linearLayout1 = new LinearLayout(getActivity());
		linearLayout2 = new LinearLayout(getActivity());
		linearLayout1.setLayoutParams(lp);
		LinearLayout.LayoutParams lpx = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lpx.setMargins(10, 10, 10, 10);
		linearLayout2.setLayoutParams(lpx);
		linearLayout1.setOrientation(LinearLayout.VERTICAL);
		linearLayout2.setOrientation(LinearLayout.HORIZONTAL);

		iv_logo = new ImageView(getActivity());
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(120, 120);
		lp2.setMargins(10, 10, 10, 10);
		iv_logo.setLayoutParams(lp2);
		iv_logo.setId(Constant.IDValues.D_LOGO);
		iv_logo.setImageBitmap(ResourceUtil.getImageFromAssetsFile(
				getActivity(), "tangguo.png"));

		linearLayout3 = new LinearLayout(getActivity());
		LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		linearLayout3.setLayoutParams(lp3);
		linearLayout3.setOrientation(LinearLayout.VERTICAL);
		linearLayout3.setGravity(Gravity.CENTER_VERTICAL);

		tv_app_name = new TextView(getActivity());
		tv_app_name.setLayoutParams(lp3);
		tv_app_name.setId(Constant.IDValues.D_TITLE);
		tv_app_name.setText(Constant.StringValues.APP_NAME);
		tv_app_name.setTextColor(Color
				.parseColor(Constant.ColorValues.TITLE_COLOR));
		tv_app_name.setTextSize(17);

		tv_size = new TextView(getActivity());
		lp3.topMargin = 10;
		lp3.leftMargin = 10;
		tv_size.setLayoutParams(lp3);
		tv_size.setId(Constant.IDValues.D_SIZE);
		tv_size.setText("大小：6.8M");
		tv_size.setTextColor(Color.parseColor(Constant.ColorValues.SIZE_COLOR));

		linearLayout3.addView(tv_app_name);
		linearLayout3.addView(tv_size);
		linearLayout2.addView(iv_logo);
		linearLayout2.addView(linearLayout3);

		linearLayout4 = new LinearLayout(getActivity());
		linearLayout4.setLayoutParams(lp);
		linearLayout4.setId(Constant.IDValues.D_HOW_DO);
		linearLayout4.setBackgroundColor(Color
				.parseColor(Constant.ColorValues.YELLOW_BACK_COLOR));
		linearLayout4.setOrientation(LinearLayout.VERTICAL);

		tv_line = new TextView(getActivity());
		LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 1);
		tv_line.setLayoutParams(lp4);
		tv_line.setBackgroundColor(Color
				.parseColor(Constant.ColorValues.DIVIDER_COLOR));

		linearLayout5 = new LinearLayout(getActivity());
		LinearLayout.LayoutParams lp5 = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp5.setMargins(20, 20, 20, 20);
		linearLayout5.setLayoutParams(lp5);

		iv_how = new ImageView(getActivity());
		LinearLayout.LayoutParams lp6 = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		iv_how.setLayoutParams(lp6);
		iv_how.setImageBitmap(ResourceUtil.getImageFromAssetsFile(
				getActivity(), "icon_how.png"));

		tv_how = new TextView(getActivity());
		lp6.leftMargin = 10;
		tv_how.setLayoutParams(lp6);
		tv_how.setText("如何做");
		tv_how.setTextColor(Color.parseColor(Constant.ColorValues.HOW_TO_DO));
		tv_how.setTextSize(17);
		
		tv_total_score = new TextView(getActivity());
		tv_total_score.setLayoutParams(lp6);
		tv_total_score.setTextColor(Color
				.parseColor(Constant.ColorValues.GREEN_THEME));
		tv_total_score.setText("");
		tv_total_score.setId(Constant.IDValues.D_SCORE);
		
		linearLayout5.addView(iv_how);
		linearLayout5.addView(tv_how);
		linearLayout5.addView(tv_total_score);
		
		tv_screen = new TextView(getActivity());
		lp6.leftMargin = 50;
		tv_screen.setLayoutParams(lp6);
		PhoneInformation.initTelephonyManager(getActivity());
		String html ="<a href='"+Constant.URL.SCREEN_SHOT_RUL+PhoneInformation.getMachineType()+"手机怎么截图"+"'>不知道怎么截图？</a>";
		tv_screen.setText(Html.fromHtml(html));
		tv_screen.setTextColor(Color.parseColor(Constant.ColorValues.GREEN_THEME));
		tv_screen.setTextSize(15);
		tv_screen.setAutoLinkMask(Linkify.ALL); 
		tv_screen.setMovementMethod(LinkMovementMethod.getInstance());
		
		linearLayout5.addView(tv_screen);
		linearLayout4.addView(tv_line);
		linearLayout4.addView(linearLayout5);

		linearLayout6 = new LinearLayout(getActivity());
		linearLayout7 = new LinearLayout(getActivity());
		linearLayout8 = new LinearLayout(getActivity());
		linearLayout9 = new LinearLayout(getActivity());
		linearLayout10 = new LinearLayout(getActivity());

		ivStep1 = new ImageView(getActivity());
		ivStep2 = new ImageView(getActivity());
		ivStep3 = new ImageView(getActivity());
		ivStep4 = new ImageView(getActivity());

		tv_tips1 = new TextView(getActivity());
		tv_tips2 = new TextView(getActivity());
		tv_tips3 = new TextView(getActivity());
		tv_tips4 = new TextView(getActivity());

		LinearLayout.LayoutParams lp7 = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		linearLayout6.setLayoutParams(lp7);
		linearLayout7.setLayoutParams(lp7);
		linearLayout8.setLayoutParams(lp7);
		linearLayout10.setLayoutParams(lp7);
		/*
		 * 图片上传
		 */
		rl_upload = new RelativeLayout(getActivity());
		iv_example = new ImageView(getActivity());
		iv_upload = new ImageView(getActivity());
		TextView tv_example = new TextView(getActivity());

		LinearLayout.LayoutParams lpUpload = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams rllp = new LinearLayout.LayoutParams(0, 300,
				1);
		LinearLayout.LayoutParams ivlp = new LinearLayout.LayoutParams(0, 300,
				1);
		RelativeLayout.LayoutParams ivelp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 300);
		RelativeLayout.LayoutParams tvlp = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		rllp.setMargins(0, 0, 20, 0);
		tvlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		tvlp.addRule(RelativeLayout.CENTER_HORIZONTAL);

		rl_upload.setClickable(true);
		rl_upload.setBackgroundColor(Color
				.parseColor(Constant.ColorValues.UPLOAD_IMG_BCAK));
		rl_upload.setLayoutParams(rllp);

		ivlp.setMargins(20, 0, 0, 0);
		iv_example.setLayoutParams(ivelp);
		iv_upload.setLayoutParams(ivlp);

		tv_example.setLayoutParams(tvlp);
		tv_example.setText("示例图");
		tv_example.setTextColor(Color
				.parseColor(Constant.ColorValues.LIGHT_RED));
		tv_example.setTextSize(20);
		tv_example.setTypeface(Typeface.DEFAULT_BOLD);

		iv_upload.setBackgroundColor(Color
				.parseColor(Constant.ColorValues.UPLOAD_IMG_BCAK));
		iv_upload.setImageBitmap(ResourceUtil.getImageFromAssetsFile(
				getActivity(), "upload.png"));
		iv_upload.setClickable(true);

		rl_upload.addView(iv_example);
		rl_upload.addView(tv_example);

		// 查看大图
		rl_upload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewBigPic();
			}
		});

		// 图片上传
		iv_upload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				uploadImage();
			}
		});

		linearLayout9.setPadding(20, 20, 20, 20);
		linearLayout9.setLayoutParams(lpUpload);
		linearLayout9.addView(rl_upload);
		linearLayout9.addView(iv_upload);

		ivStep1.setLayoutParams(lp7);
		ivStep2.setLayoutParams(lp7);
		ivStep3.setLayoutParams(lp7);
		ivStep4.setLayoutParams(lp7);
		ivStep1.setImageBitmap(ResourceUtil.getImageFromAssetsFile(
				getActivity(), "task_step1.png"));
		ivStep2.setImageBitmap(ResourceUtil.getImageFromAssetsFile(
				getActivity(), "task_step2.png"));
		ivStep3.setImageBitmap(ResourceUtil.getImageFromAssetsFile(
				getActivity(), "task_step3.png"));
		ivStep4.setImageBitmap(ResourceUtil.getImageFromAssetsFile(
				getActivity(), "task_step4.png"));

		lp7.setMargins(10, 0, 10, 10);
		tv_tips1.setLayoutParams(lp7);
		tv_tips1.setId(Constant.IDValues.D_TIPS1);
		tv_tips1.setText("下载安装后，使用3分钟系统将会赠送积分！");
		tv_tips1.setTextSize(16);
		tv_tips1.setTextColor(Color.parseColor(Constant.ColorValues.LIGHT_RED));

		tv_tips2.setLayoutParams(lp7);
		tv_tips2.setId(Constant.IDValues.D_TIPS2);
		tv_tips2.setText("安装完成后，请到未完成任务列表中，继续签到，每次签到即可获得0.1元。");
		tv_tips2.setTextSize(16);
		tv_tips2.setTextColor(Color.parseColor(Constant.ColorValues.LIGHT_RED));

		tv_tips3.setLayoutParams(lp7);
		tv_tips3.setId(Constant.IDValues.D_TIPS3);
		tv_tips3.setText("每隔2天可签到一次，签到3次任务完成");
		tv_tips3.setTextSize(16);
		tv_tips3.setTextColor(Color.parseColor(Constant.ColorValues.LIGHT_RED));
		
		tv_tips4.setLayoutParams(lp7);
		tv_tips4.setText("");
		tv_tips4.setTextSize(16);
		tv_tips4.setTextColor(Color.parseColor(Constant.ColorValues.LIGHT_RED));

		linearLayout6.addView(ivStep1);
		linearLayout6.addView(tv_tips1);
		linearLayout7.addView(ivStep2);
		linearLayout7.addView(tv_tips2);
		linearLayout8.addView(ivStep3);
		linearLayout8.addView(tv_tips3);
		
		linearLayout4.addView(linearLayout6);
		linearLayout4.addView(linearLayout7);
		linearLayout4.addView(linearLayout8);
		if (appInfo.getClicktype() == 1 || (appInfo.getClicktype() ==8 && appInfo.getIs_photo_task() == 1)){
			if(appInfo.getPhoto_remarks()!=null && !appInfo.getPhoto_remarks().equals("")){
				tv_tips4.setText(appInfo.getPhoto_remarks());
				linearLayout10.addView(ivStep4);
				linearLayout10.addView(tv_tips4);
				linearLayout4.addView(linearLayout10);
			}
			
		}
		Log.w("FragmentDownLoad", appInfo.getClicktype()+"--"+appInfo.getIs_photo_task()+"--"+appInfo.getPhoto_remarks());
		linearLayout4.addView(linearLayout9);

		tv_desc = new TextView(getActivity());
		LinearLayout.LayoutParams lp8 = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp8.setMargins(20, 20, 20, 20);
		tv_desc.setLayoutParams(lp8);
		tv_desc.setText("");
		tv_desc.setTextColor(Color.parseColor(Constant.ColorValues.TITLE_COLOR));
		tv_desc.setId(Constant.IDValues.D_DESC);

		tv_downLoad = new TextView(getActivity());
		lp8.setMargins(50, 50, 50, 50);
		tv_downLoad.setLayoutParams(lp8);
		tv_downLoad.setText(Constant.StringValues.IMM_DOWN);
		tv_downLoad.setTextColor(Color.parseColor(Constant.ColorValues.WHITE));
		tv_downLoad.setBackgroundColor(Color
				.parseColor(Constant.ColorValues.BTN_NORMAL_COLOR));
		tv_downLoad.setPadding(20, 20, 20, 20);
		tv_downLoad.setTextSize(17);
		tv_downLoad.setGravity(Gravity.CENTER);
		tv_downLoad.setId(Constant.IDValues.D_DOWN);
		tv_downLoad.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (appInfo != null) {
					Intent intent = new Intent(getActivity(),
							DownloadService.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable(Constant.ITEM, appInfo);
					intent.putExtras(bundle);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					getActivity().startService(intent);
					tv_downLoad.setVisibility(View.GONE);
					editor.putInt(Constant.RESOURCE_ID,
							appInfo.getResource_id());
					editor.commit();
				}
			}
		});

		linearLayout1.addView(linearLayout2);
		linearLayout1.addView(linearLayout4);
		linearLayout1.addView(tv_desc);
		linearLayout1.addView(tv_downLoad);

		scrollView.addView(linearLayout1);
		view.addView(scrollView);

	}

	/**
	 * @Title: uploadImage
	 * @Description: TODO
	 * @author xie.xin
	 * @param
	 * @return void
	 * @throws
	 */
	protected void uploadImage() {
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, 9999);
	}

	/**
	 * @Title: viewBigPic
	 * @Description: TODO
	 * @author xie.xin
	 * @param
	 * @return void
	 * @throws
	 */
	protected void viewBigPic() {
		if (cachedImage1 != null) {
			ImageView view = new ImageView(getActivity());
			view.setImageDrawable(cachedImage1);
			popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			popupWindow.showAtLocation(this.view, Gravity.CENTER, 0, 0);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					popupWindow.dismiss();
				}
			});
		}
	}

	private void initData() {
		if (null != appInfo) {
			if (appInfo.getIsShow() == 1) {
				linearLayout4.setVisibility(View.GONE);
			} else {
				linearLayout4.setVisibility(View.VISIBLE);

				// tv_tips1.setText(appInfo.getAlert()+appInfo.getScore()+appInfo.getTextName());
				// tv_tips2.setText("安装完成后，请到未完成任务列表中，继续签到，每次签到即可获得"+10*appInfo.getVcPrice()+appInfo.getTextName());
				// tv_tips3.setText("每隔"+appInfo.getSign_rules()+"天可签到一次，签到"+appInfo.getNeedSign_times()+"次任务完成");
				//
				if (appInfo.getClicktype() == 1) {
					tv_tips1.setText(appInfo.getFile());
					tv_tips1.setAutoLinkMask(Linkify.ALL);
					tv_tips1.setMovementMethod(LinkMovementMethod.getInstance());
					tv_downLoad.setVisibility(View.GONE);
					tv_tips2.setText("打开上面连接，按提示完成操作。");
					tv_tips3.setText("按下面示例图截图上传即可获得 "
							+ appInfo.getPhoto_integral()
							+ appInfo.getTextName() + "，（注意只有一次上传机会，请严格按照要求上传）");
					rl_upload.setVisibility(View.VISIBLE);
					iv_upload.setVisibility(View.VISIBLE);
					tv_screen.setVisibility(View.VISIBLE);
					editor.putBoolean(Constant.IS_SIGN, false);
					editor.commit();
					loadImage();
				} else if (appInfo.getClicktype() == 8) {
					if(appInfo.isSign()){
						tv_downLoad.setVisibility(View.GONE);
						editor.putBoolean(Constant.IS_SIGN, true);
					}else{
						tv_downLoad.setVisibility(View.VISIBLE);
						editor.putBoolean(Constant.IS_SIGN, false);
					}
					editor.commit();
					String str = "";
					if (appInfo.getScore() > 0) {
						if(appInfo.getAlert()==null||appInfo.getAlert().equals("")){
							str = "试玩3分钟可获得" + appInfo.getScore()
									+ appInfo.getTextName()+"，";
						}else{
							str = appInfo.getAlert() + appInfo.getScore()
									+ appInfo.getTextName()+"，";
						}
						
					}
					if (appInfo.getIs_photo() == 1 || appInfo.isSign()) {
						tv_tips1.setText(str + "下载安装成功后，请到未完成任务列表中，按下面示例图截图上传即可获得 "
								+ appInfo.getPhoto_integral()
								+ appInfo.getTextName()
								+ "，（注意只有一次上传机会，请严格按照要求上传）");
						rl_upload.setVisibility(View.VISIBLE);
						if (appInfo.isSign()) {
							iv_upload.setVisibility(View.VISIBLE);
						} else {
							iv_upload.setVisibility(View.GONE);
						}
						tv_screen.setVisibility(View.VISIBLE);
						loadImage();
					} else {
						tv_tips1.setText(str);
						rl_upload.setVisibility(View.GONE);
						iv_upload.setVisibility(View.GONE);
						tv_screen.setVisibility(View.GONE);
					}

					tv_tips2.setText("安装完成后，请到未完成任务列表中，继续签到，每次签到即可获得" + 10
							* appInfo.getVcPrice() + appInfo.getTextName());
					tv_tips3.setText("每隔" + appInfo.getSign_rules()
							+ "天可签到一次，签到" + appInfo.getNeedSign_times()
							+ "次任务完成");
				}
			}

			cachedImage = SyncImageLoader.getInstance().loadDrawable(
					appInfo.getIcon(), new ImageCallback() {
						public void imageLoaded(Drawable imageDrawable,
								String imageUrl) {
							iv_logo.setImageDrawable(imageDrawable);
						}
					});
			if (cachedImage == null) {
				iv_logo.setImageBitmap(ResourceUtil.getImageFromAssetsFile(
						getActivity(), "tangguo.png"));
			} else {
				iv_logo.setImageDrawable(cachedImage);
			}

			tv_app_name.setText(appInfo.getTitle());
			tv_size.setText(appInfo.getResource_size() + "M");
			tv_desc.setText(appInfo.getDescription());

		} else {
			tv_downLoad.setVisibility(View.GONE);
		}
	}

	private void loadImage() {
		// TODO Auto-generated method stub
		cachedImage1 = SyncImageLoader.getInstance().loadDrawable(
				appInfo.getH5_big_url(), new ImageCallback() {
					public void imageLoaded(Drawable imageDrawable,
							String imageUrl) {
						iv_example.setImageDrawable(imageDrawable);
						cachedImage1 = imageDrawable;
					}
				});
		if (cachedImage1 == null) {
			iv_example.setImageBitmap(ResourceUtil.getImageFromAssetsFile(
					getActivity(), "tangguo.png"));
		} else {
			iv_example.setImageDrawable(cachedImage1);

		}
	}
}
