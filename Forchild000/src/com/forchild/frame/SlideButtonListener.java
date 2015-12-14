package com.forchild.frame;

import com.forchild000.surface.SlideButton;
import com.forchild000.surface.SlideListView;

public interface SlideButtonListener {
	
	public void onClick(SlideButton v);
	
	public void onScroll(SlideButton v);
	
	public void onButtonClick(SlideButton v);
	
	public void onBottomButtonClick(SlideListView v);
	
}
