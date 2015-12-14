package com.forchild000.surface;

import java.util.ArrayList;
import java.util.List;

import com.forchild.data.SeniorInfoAutoMessage;
import com.forchild.frame.SlideButtonListener;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class SlideListView extends RelativeLayout {
	public Context context;
	public SlideButton nowDisplayedButton;
	private List<SlideButton> list = new ArrayList<SlideButton>();
	private int viewId = 0;
	private int startViewId = 1;
	private SlideButtonListener slideButtonListener;
	private SetupButton bottomButton;
	private RelativeLayout.LayoutParams buttonLayoutParams;

	public SlideListView(Context context) {
		super(context);

		this.init(context);
	}

	public SlideListView(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.init(context);
	}

	public SlideListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		this.init(context);
	}

	public SlideListView clearAllSlideButton() {
		this.removeAllViews();
		list.clear();
		buttonLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		this.addView(bottomButton, buttonLayoutParams);
		return this;
	}
	
	private SlideListView init(Context context) {
		this.context = context;
		bottomButton = new SetupButton(context);
		bottomButton.setText("添加");
		buttonLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		this.addView(bottomButton, buttonLayoutParams);
		bottomButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (slideButtonListener != null)
					slideButtonListener.onBottomButtonClick(SlideListView.this);
			}

		});
		return this;
	}

	public SlideListView setBottomButtonText(CharSequence buttonText) {
		bottomButton.setText(buttonText);
		return this;
	}

	public SlideListView setPublicListener(SlideButtonListener slideButtonListener) {
		this.slideButtonListener = slideButtonListener;
		return this;
	}

	public SlideListView setStartId(int startViewId) {
		this.startViewId = startViewId;
		return this;
	}
	
	public SlideListView setInvisibleRightButton() {
		this.nowDisplayedButton.setInvisibleButton();
		return this;
	}
	
	public SlideListView setVisibleRightButton() {
		this.nowDisplayedButton.setVisibleButton();
		return this;
	}
	
	public SlideListView setInvisibleBottomButton() {
		this.bottomButton.setVisibility(View.GONE);
		return this;
	}
	
	public SlideListView setVisibleBottomButton() {
		this.bottomButton.setVisibility(View.VISIBLE);
		return this;
	}

	public SlideListView addSlideButton() {
		SlideButton slideButton = new SlideButton(context);
		slideButton.setParentView(this);
		slideButton.setId(viewId + startViewId);
		slideButton.setViewId(viewId);
		++viewId;
		RelativeLayout.LayoutParams slideButtonLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		slideButton.setListener(slideButtonListener);
		if (list.size() == 0) {
			list.add(slideButton);
			slideButtonLP.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			this.addView(slideButton, slideButtonLP);
			buttonLayoutParams.addRule(RelativeLayout.BELOW, slideButton.getId());
		} else {
			slideButtonLP.addRule(RelativeLayout.BELOW, list.get(list.size() - 1).getId());
			list.add(slideButton);
			this.addView(slideButton, slideButtonLP);
			buttonLayoutParams.addRule(RelativeLayout.BELOW, slideButton.getId());
		}
		return this;
	}

	public SlideListView addSlideButton(CharSequence title, CharSequence content, CharSequence remarkTitle, CharSequence remark) {
		SlideButton slideButton = new SlideButton(context);
		slideButton.setParentView(this);
		slideButton.setId(viewId + startViewId);
		slideButton.setTitle(title);
		slideButton.setContent(content);
		slideButton.setRemark(remark);
		slideButton.setRemarkTitle(remarkTitle);
		slideButton.setViewId(viewId);
		++viewId;
		RelativeLayout.LayoutParams slideButtonLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		slideButton.setListener(slideButtonListener);
		if (list.size() == 0) {
			list.add(slideButton);
			slideButtonLP.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			this.addView(slideButton, slideButtonLP);
			buttonLayoutParams.addRule(RelativeLayout.BELOW, slideButton.getId());
		} else {
			slideButtonLP.addRule(RelativeLayout.BELOW, list.get(list.size() - 1).getId());
			list.add(slideButton);
			this.addView(slideButton, slideButtonLP);
			buttonLayoutParams.addRule(RelativeLayout.BELOW, slideButton.getId());
		}
		return this;
	}

	public SlideListView addSlideButton(CharSequence title, CharSequence content, CharSequence remarkTitle, CharSequence remark,
			CharSequence buttonText) {
		SlideButton slideButton = new SlideButton(context);
		slideButton.setParentView(this);
		slideButton.setId(viewId + startViewId);
		slideButton.setTitle(title);
		slideButton.setContent(content);
		slideButton.setRemark(remark);
		slideButton.setRemarkTitle(remarkTitle);
		slideButton.setButtonText(buttonText);
		slideButton.setViewId(viewId);
		++viewId;
		RelativeLayout.LayoutParams slideButtonLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		slideButton.setListener(slideButtonListener);
		if (list.size() == 0) {
			list.add(slideButton);
			slideButtonLP.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			this.addView(slideButton, slideButtonLP);
			buttonLayoutParams.addRule(RelativeLayout.BELOW, slideButton.getId());
		} else {
			slideButtonLP.addRule(RelativeLayout.BELOW, list.get(list.size() - 1).getId());
			list.add(slideButton);
			this.addView(slideButton, slideButtonLP);
			buttonLayoutParams.addRule(RelativeLayout.BELOW, slideButton.getId());
		}
		return this;
	}

	public SlideListView addSlideButton(CharSequence title, CharSequence content, CharSequence remarkTitle, CharSequence remark,
			SlideButtonListener listener) {
		SlideButton slideButton = new SlideButton(context);
		slideButton.setParentView(this);
		slideButton.setId(viewId + startViewId);
		slideButton.setTitle(title);
		slideButton.setContent(content);
		slideButton.setRemark(remark);
		slideButton.setRemarkTitle(remarkTitle);
		slideButton.setListener(listener);
		slideButton.setViewId(viewId);
		++viewId;
		RelativeLayout.LayoutParams slideButtonLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
//		slideButton.setListener(slideButtonListener);
		if (list.size() == 0) {
			list.add(slideButton);
			slideButtonLP.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			this.addView(slideButton, slideButtonLP);
			buttonLayoutParams.addRule(RelativeLayout.BELOW, slideButton.getId());
		} else {
			slideButtonLP.addRule(RelativeLayout.BELOW, list.get(list.size() - 1).getId());
			list.add(slideButton);
			this.addView(slideButton, slideButtonLP);
			buttonLayoutParams.addRule(RelativeLayout.BELOW, slideButton.getId());
		}
		return this;
	}

	public SlideListView addSlideButton(CharSequence title, CharSequence content, CharSequence remarkTitle, CharSequence remark,
			CharSequence buttonText, SlideButtonListener listener) {
		SlideButton slideButton = new SlideButton(context);
		slideButton.setParentView(this);
		slideButton.setId(viewId + startViewId);
		slideButton.setTitle(title);
		slideButton.setContent(content);
		slideButton.setRemark(remark);
		slideButton.setRemarkTitle(remarkTitle);
		slideButton.setButtonText(buttonText);
		slideButton.setListener(listener);
		slideButton.setViewId(viewId);
		++viewId;
		RelativeLayout.LayoutParams slideButtonLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
//		slideButton.setListener(slideButtonListener);
		if (list.size() == 0) {
			list.add(slideButton);
			slideButtonLP.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			this.addView(slideButton, slideButtonLP);
			buttonLayoutParams.addRule(RelativeLayout.BELOW, slideButton.getId());
		} else {
			slideButtonLP.addRule(RelativeLayout.BELOW, list.get(list.size() - 1).getId());
			list.add(slideButton);
			this.addView(slideButton, slideButtonLP);
			buttonLayoutParams.addRule(RelativeLayout.BELOW, slideButton.getId());
		}
		return this;
	}

	/**
	 * 用于设置某一个滑动按键的内容
	 * 
	 * @param index
	 *            滑动按键在SlideListView中的位置，自上向下，从0开始
	 * @param title
	 * @param content
	 * @param remarkTitle
	 * @param remark
	 * @return 如果index超过了现有滑动按键的数量，返回false;否则，返回true。
	 */
	public boolean setSlideButton(int index, CharSequence title, CharSequence content, CharSequence remarkTitle, CharSequence remark) {
		if (index >= list.size()) {
			return false;
		}

		SlideButton slideButton = list.get(index);
		slideButton.setTitle(title);
		slideButton.setContent(content);
		slideButton.setRemark(remark);
		slideButton.setRemarkTitle(remarkTitle);
		return true;
	}

	/**
	 * 用于设置某一个滑动按键的监听器内容
	 * 
	 * @param index
	 *            滑动按键在SlideListView中的位置，自上向下，从0开始
	 * @param title
	 * @param content
	 * @param remarkTitle
	 * @param remark
	 * @return 如果index超过了现有滑动按键的数量，返回false;否则，返回true。
	 */
	public boolean setSlideButtonListener(int index, SlideButtonListener listener) {
		if (index >= list.size()) {
			return false;
		}

		list.get(index).setListener(listener);
		return true;
	}
}
