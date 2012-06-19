package com.RemoteControl;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RemoteControlActivity extends Activity {
	private WakeLock wakeLock;
	private Button leftButton;
	private Button rightButton;
	private CommConnect commConnect;
	private MyApp myApp;
	private static char LEFT_SINGLE_CLICK = 'a';
	private static char RIGHT_CLICK = 'b';
	private static char MOVE_START = 'c';
	private static char MOVE = 'd';
	private static char KEYBOARD = 'e';
	private static char MOVE_STOP = 'f';

	private static final int MOUSE_ID = Menu.FIRST;
	private static final int KEYBD_ID = Menu.FIRST + 1;
	private static final int SETTING_ID = Menu.FIRST + 2;
	private Boolean mouseFlag = true;
	private ImageView imageViewDown;
	private ImageView imageViewMove;
	private RelativeLayout layout;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// 为自动弹出键盘设置焦点
		TextView tv = (TextView) findViewById(R.id.noSense);
		tv.setFocusableInTouchMode(true);
		tv.requestFocus();

		// 为自动弹出键盘设置计时器
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				InputMethodManager imm = (InputMethodManager) RemoteControlActivity.this
						.getSystemService(INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
			}
		}, 500);

		leftButton = (Button) findViewById(R.id.leftButton);
		rightButton = (Button) findViewById(R.id.rightButton);
		layout = (RelativeLayout) findViewById(R.id.main);

		layout.setClickable(true);
		myApp = (MyApp) getApplication();
		commConnect = myApp.getCommConnect();

		leftButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				final ControlData controlData = new ControlData();
				controlData.setHead(LEFT_SINGLE_CLICK);
				new Thread() {
					public void run() {
						SendData(controlData);
					}
				}.start();
			}
		});

		leftButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					leftButton.setBackgroundDrawable(leftButton.getResources()
							.getDrawable(R.drawable.button2_pressed));
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					leftButton.setBackgroundDrawable(leftButton.getResources()
							.getDrawable(R.drawable.button2));
				}
				return false;
			}
		});

		rightButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				final ControlData controlData = new ControlData();
				controlData.setHead(RIGHT_CLICK);
				new Thread() {
					public void run() {
						SendData(controlData);
					}
				}.start();
			}
		});

		rightButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					rightButton.setBackgroundDrawable(rightButton
							.getResources().getDrawable(
									R.drawable.button2_pressed));
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					rightButton.setBackgroundDrawable(rightButton
							.getResources().getDrawable(R.drawable.button2));
				}
				return false;
			}
		});

		// 触摸屏事件监听
		layout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					final ControlData controlData = new ControlData();
					controlData.setHead(MOVE);
					controlData.setDistanceX(event.getX());
					controlData.setDistanceY(event.getY());
					new Thread() {
						public void run() {
							SendData(controlData);
						}
					}.start();
				} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
					final ControlData controlData = new ControlData();
					controlData.setHead(MOVE_START);
					controlData.setDistanceX(event.getX());
					controlData.setDistanceY(event.getY());
					new Thread() {
						public void run() {
							SendData(controlData);
						}
					}.start();
					// Thread downThread = new Thread(sendThread);
					// downThread.start();
					// } else if (event.getAction() == MotionEvent.ACTION_UP) {
					// ControlData controlData = new ControlData();
					// controlData.setHead(MOVE_STOP);
					// new Thread() {
					// public void run() {
					// SendData(controlData);
					// }
					// }.start();
					// Thread upThread = new Thread(sendThread);
					// upThread.start();
				}

				return false;
			}
		});

		// 监听键盘按键
		tv.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_UP) {
					final ControlData controlData = new ControlData();
					controlData.setHead(KEYBOARD);
					controlData.setKeyCode(keyCode);
					new Thread() {
						public void run() {
							SendData(controlData);
						}
					}.start();
				}
				return false;
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		acquireWakeLock();
	}

	protected void onPause() {
		super.onPause();
		releaseWakeLock();

	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		menu.clear();
		menu.add(0, MOUSE_ID, 0, "鼠标").setIcon(R.drawable.mouse);
		menu.add(0, KEYBD_ID, 0, "键盘").setIcon(R.drawable.keybd);
		menu.add(0, SETTING_ID, 0, "工具").setIcon(R.drawable.setting);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MOUSE_ID: {
			if (mouseFlag == true) {
				findViewById(R.id.mouse).setVisibility(View.INVISIBLE);
				mouseFlag = false;
			} else {
				findViewById(R.id.mouse).setVisibility(View.VISIBLE);
				mouseFlag = true;
			}
			break;
		}
		case KEYBD_ID: {
			showKeyboard(findViewById(R.id.main));
			break;
		}
		case SETTING_ID: {
			showTools();
			break;
		}
		}
		return super.onOptionsItemSelected(item);
	}

	// 调用软键盘
	public void showKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager) RemoteControlActivity.this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
	}

	// 调用工具选择对话框
	public void showTools() {
		android.content.DialogInterface.OnClickListener toolSelect = new android.content.DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					Intent intent1 = new Intent(RemoteControlActivity.this,
							PowerPointActivity.class);
					startActivity(intent1);
					break;
				case 1:
					Intent intent2 = new Intent(RemoteControlActivity.this,
							MediaActivity.class);
					startActivity(intent2);
					break;
				}
			}
		};

		String[] choices = { "PPT播放", "音乐播放" };
		AlertDialog dialog = new AlertDialog.Builder(RemoteControlActivity.this)
				.setTitle("工具").setItems(choices, toolSelect).create();
		dialog.show();
	}

	// 保持屏幕常亮
	// 请求唤醒锁
	private void acquireWakeLock() {
		if (wakeLock == null) {
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, this
					.getClass().getCanonicalName());
			wakeLock.acquire();
		}
	}

	// 释放唤醒锁
	private void releaseWakeLock() {
		if (wakeLock != null && wakeLock.isHeld()) {
			wakeLock.release();
			wakeLock = null;
		}
	}

	private synchronized void SendData(ControlData controlData) {
		commConnect.send(controlData);
	}

}