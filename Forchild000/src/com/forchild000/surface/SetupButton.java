package com.forchild000.surface;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SetupButton extends LinearLayout {
	protected TextView content;
	protected View topFrame, bottomFrame;

	public final static int NO_FRAME = 0;
	public final static int ALL_FRAME = 1;
	public final static int TOP_FRAME = 2;
	public final static int BOTTOM_FRAME = 3;

	public SetupButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.forchild_setup_button, this, true);
		content = (TextView) findViewById(R.id.setup_button_content);
		topFrame = (View) findViewById(R.id.setup_button_top_frame);
		bottomFrame = (View) findViewById(R.id.setup_button_bottom_frame);
		this.setFrame(NO_FRAME);
	}

	public SetupButton(Context context) {
		super(context);

		LayoutInflater.from(context).inflate(R.layout.forchild_setup_button, this, true);
		content = (TextView) findViewById(R.id.setup_button_content);
		topFrame = (View) findViewById(R.id.setup_button_top_frame);
		bottomFrame = (View) findViewById(R.id.setup_button_bottom_frame);
		this.setFrame(NO_FRAME);
	}

	public void setText(CharSequence content) {
		this.content.setText(content);
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
			bottomFrame.setVisibility(View.INVISIBLE);
			break;
		case BOTTOM_FRAME:
			bottomFrame.setVisibility(View.VISIBLE);
			topFrame.setVisibility(View.INVISIBLE);
			break;
		default:
			break;
		}
	}

}
