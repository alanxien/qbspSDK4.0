package com.chuannuo.tangguo;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
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
			if (rLinearLayout.getVisibility() == View.GONE) {
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

}
