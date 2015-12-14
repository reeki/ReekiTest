package com.forchild000.surface;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CheckButton extends LinearLayout {
	protected TextView content;
	protected ImageView image;
	protected boolean option = false;
	protected View topFrame, bottomFrame;

	public final static int NO_FRAME = 0;
	public final static int ALL_FRAME = 1;
	public final static int TOP_FRAME = 2;
	public final static int BOTTOM_FRAME = 3;

	public CheckButton(Context context) {
		super(context);
	}

	public CheckButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.forchild_check_button, this, true);
		content = (TextView) findViewById(R.id.check_button_content);
		image = (ImageView) findViewById(R.id.check_button_image);

		topFrame = (View) findViewById(R.id.check_button_top_frame);
		bottomFrame = (View) findViewById(R.id.check_button_bottom_frame);
	}

	public void setText(CharSequence content) {
		this.content.setText(content);
	}

	public void setOption(boolean option) {
		this.option = option;
		if (option) {
			image.setImageResource(R.drawable.btn_check_buttonless_on);
		} else {
			image.setImageResource(R.drawable.btn_check_buttonless_off);
		}
	}

	public void changeOption() {
		this.setOption(!option);
	}

	public boolean getOption() {
		return this.option;
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
