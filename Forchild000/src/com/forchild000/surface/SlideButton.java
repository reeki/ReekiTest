package com.forchild000.surface;

import com.forchild.frame.SlideButtonListener;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SlideButton extends LinearLayout {
	protected Context context;
	private int viewId = -1;
	public int arg0 = -1;
	public int arg1 = -1;
	private TextView titleText, contentText, remarkText, remarkLableText;
	private Button btn;
	private int screenPoint;
	private float x = 0;
	private SlideButtonListener slideButtonListener;
	private SlideListView parentView;

	/***
	 * 建议构造后第一时间绑定公用监听器，使用setPublicListener()
	 * @param context
	 */
	public SlideButton(Context context) {
		super(context);

		this.init(context);
	}

	/***
	 * 建议构造后第一时间绑定公用监听器，使用setPublicListener()
	 * @param context
	 */
	public SlideButton(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.init(context);
	}

	/***
	 * 建议构造后第一时间绑定公用监听器，使用setPublicListener()
	 * @param context
	 */
	public SlideButton setViewId(int viewId) {
		this.viewId = viewId;
		return this;
	}

	public int getViewId() {
		return this.viewId;
	}

	protected SlideButton setInvisibleButton() {
		btn.setVisibility(View.GONE);
		return this;
	}

	public SlideButton setTitle(CharSequence title) {
		titleText.setText(title);
		return this;
	}

	public SlideButton setContent(CharSequence content) {
		contentText.setText(content);
		return this;
	}

	public SlideButton setRemarkTitle(CharSequence remarkTitle) {
		remarkLableText.setText(remarkTitle);
		return this;
	}

	public SlideButton setRemark(CharSequence remark) {
		remarkText.setText(remark);
		return this;
	}

	public SlideButton setButtonText(CharSequence buttonText) {
		btn.setText(buttonText);
		return this;
	}

	private OnClickListener ButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.deleteable_text_delete_btn) {

				if (slideButtonListener != null) {
					slideButtonListener.onButtonClick(SlideButton.this);
				}

				SlideButton.this.setVisibility(View.GONE);
			}
		}
	};

	public void setParentView(SlideListView parentView) {
		this.parentView = parentView;
	}

	public void init(Context context) {
		LayoutInflater.from(context).inflate(R.layout.deletable_text, this, true);

		this.context = context;

		titleText = (TextView) findViewById(R.id.deleteable_text_title);
		contentText = (TextView) findViewById(R.id.deleteable_text_content);
		remarkText = (TextView) findViewById(R.id.deleteable_text_remark0_content);
		remarkLableText = (TextView) findViewById(R.id.deleteable_text_remark0_title);
		btn = (Button) findViewById(R.id.deleteable_text_delete_btn);
		btn.setOnClickListener(ButtonListener);
		btn.setVisibility(View.GONE);

		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		screenPoint = wm.getDefaultDisplay().getWidth();

		this.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}

		});

		this.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					x = event.getX();
				}

				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (parentView == null) {
						return false;
					}

					if (Math.abs(event.getX() - x) > screenPoint / 4 && parentView.nowDisplayedButton == null) {
						if (slideButtonListener != null) {
							slideButtonListener.onScroll(SlideButton.this);
						}
						parentView.nowDisplayedButton = SlideButton.this;
						btn.setVisibility(View.VISIBLE);
					} else {
						if (parentView.nowDisplayedButton != null) {
							parentView.nowDisplayedButton.setInvisibleButton();
							parentView.nowDisplayedButton = null;
						} else {
							if (Math.abs(event.getX() - x) < screenPoint / 10) {
								// Clicked

								if (slideButtonListener != null) {
									slideButtonListener.onClick(SlideButton.this);
								}
							}
						}
					}
				}
				return false;
			}
		});
	}

	public SlideButton setListener(SlideButtonListener slideButtonListener) {
		this.slideButtonListener = slideButtonListener;
		return this;
	}

	public SlideButton setVisibleButton() {
		btn.setVisibility(View.VISIBLE);
		return this;
	}
}
