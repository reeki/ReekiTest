package com.forchild000.surface;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContentButton extends LinearLayout {
	protected TextView title, content;
	protected View topFrame, bottomFrame;

	public final static int NO_FRAME = 0;
	public final static int ALL_FRAME = 1;
	public final static int TOP_FRAME = 2;
	public final static int BOTTOM_FRAME = 3;

	public ContentButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.forchild_content_button, this, true);
		content = (TextView) findViewById(R.id.content_button_content);
		title = (TextView) findViewById(R.id.content_button_title);
		topFrame = (View) findViewById(R.id.content_button_top_frame);
		bottomFrame = (View) findViewById(R.id.content_button_bottom_frame);
	}

	public void setTitle(CharSequence title) {
		if (title != null) {
			this.title.setText(title);
		} else {
			this.content.setText("");
		}
	}

	public void setTitleSize(float size) {
		this.title.setTextSize(size);
	}

	public void setTitleColor(ColorStateList colors) {
		this.title.setTextColor(colors);
	}

	public void setTitleColor(int colors) {
		this.title.setTextColor(colors);
	}

	public void setText(CharSequence content) {
		if (content != null) {
			this.content.setText(content);
		} else {
			this.content.setText("");
		}
	}

	public void setTextSize(float size) {
		this.content.setTextSize(size);
	}

	public void setTextColor(ColorStateList colors) {
		this.content.setTextColor(colors);
	}

	public void setTextColor(int colors) {
		this.content.setTextColor(colors);
	}

	public void setFrame(int frameType) {
		switch (frameType) {
		case NO_FRAME:
			topFrame.setVisibility(View.GONE);
			bottomFrame.setVisibility(View.GONE);
			break;
		case ALL_FRAME:
			topFrame.setVisibility(View.VISIBLE);
			bottomFrame.setVisibility(View.VISIBLE);
			break;
		case TOP_FRAME:
			topFrame.setVisibility(View.VISIBLE);
			break;
		case BOTTOM_FRAME:
			bottomFrame.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}
}
