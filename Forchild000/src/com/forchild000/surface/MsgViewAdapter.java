package com.forchild000.surface;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.forchild.data.MessageFrame;
import com.forchild.server.TimeFormat;

public class MsgViewAdapter extends BaseAdapter {

	protected OnFaultItemClickListener onFaultItemListener;
	protected int leftSex, rightSex;

	public static interface IMsgViewType {
		int IMVT_COM_MSG = 0;
		int IMVT_TO_MSG = 1;
	}

	private List<MessageFrame> coll;

	private Context ctx;

	private LayoutInflater mInflater;

	public MsgViewAdapter(Context context, List<MessageFrame> coll, OnFaultItemClickListener onFaultItemListener) {
		ctx = context;
		this.coll = coll;
		mInflater = LayoutInflater.from(context);
		this.onFaultItemListener = onFaultItemListener;
	}

	public void setLeftSex(int sex) {
		this.leftSex = sex;
	}

	public void setRightSex(int sex) {
		this.rightSex = sex;
	}

	@Override
	public int getCount() {
		return coll.size();
	}

	@Override
	public Object getItem(int position) {
		return coll.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		MessageFrame entity = coll.get(position);

		if (entity.getMsgType()) {
			return IMsgViewType.IMVT_COM_MSG;
		} else {
			return IMsgViewType.IMVT_TO_MSG;
		}

	}
	
	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final MessageFrame entity = coll.get(position);
		boolean isComMsg = entity.getMsgType();

		ViewHolder viewHolder = null;
		viewHolder = new ViewHolder();
		if (convertView == null) {
			if (isComMsg) {
				convertView = mInflater.inflate(R.layout.msg_adapter_item_left, null);
			} else {
				convertView = mInflater.inflate(R.layout.msg_adapter_item_right, null);
				viewHolder.sendStateView = (ImageView) convertView.findViewById(R.id.msgperson_send_state);
			}

			viewHolder.tvSendTime = (TextView) convertView.findViewById(R.id.msgperson_sendtime);
			viewHolder.tvContent = (TextView) convertView.findViewById(R.id.msgperson_chatcontent);
			viewHolder.figureView = (ImageView) convertView.findViewById(R.id.msgperson_userhead);
			viewHolder.isComMsg = isComMsg;

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (!isComMsg && viewHolder.sendStateView != null) {
			switch (entity.getState()) {
			case MessageFrame.SENDSTATE_FAULT:
				viewHolder.sendStateView.clearAnimation();
				viewHolder.sendStateView.setVisibility(View.VISIBLE);
				viewHolder.sendStateView.setImageResource(R.drawable.indicator_input_error);
				if (onFaultItemListener != null) {
					viewHolder.sendStateView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							onFaultItemListener.onFaultItemClick(v, entity);
						}
					});
				}
				break;
			case MessageFrame.SENDSTATE_FINISHED:
				viewHolder.sendStateView.clearAnimation();
				viewHolder.sendStateView.setVisibility(View.GONE);
				break;
			case MessageFrame.SENDSTATE_SENDING:
				viewHolder.sendStateView.setVisibility(View.VISIBLE);
				viewHolder.sendStateView.setImageResource(R.drawable.spinner_16_inner_holo);
				viewHolder.sendStateView.startAnimation(AnimationUtils.loadAnimation(ctx, R.anim.rotate_waiting));
				break;
			default:
				viewHolder.sendStateView.clearAnimation();
				viewHolder.sendStateView.setVisibility(View.GONE);
				break;
			}
		}

		Calendar now = Calendar.getInstance();
		Calendar sendTime = Calendar.getInstance();
		sendTime.setTimeInMillis(entity.getDate());
		StringBuffer timeDisplay = new StringBuffer();
		if (sendTime.getTimeInMillis() - entity.getLastTime() < 20 * 60 * 1000) {
			viewHolder.tvSendTime.setVisibility(View.GONE);
		} else if (now.get(Calendar.YEAR) == sendTime.get(Calendar.YEAR) && now.get(Calendar.MONTH) == sendTime.get(Calendar.MONTH)
				&& now.get(Calendar.DATE) == sendTime.get(Calendar.DATE)) {
			timeDisplay.append(TimeFormat.getDisplayTime(sendTime));
			viewHolder.tvSendTime.setVisibility(View.VISIBLE);
		} else {
			timeDisplay.append(TimeFormat.getDisplayDate(sendTime));
			viewHolder.tvSendTime.setVisibility(View.VISIBLE);
		}

		if (isComMsg) {
			if (leftSex == 2) {
				viewHolder.figureView.setImageResource(R.drawable.medical_card_figure_female);
			} else {
				viewHolder.figureView.setImageResource(R.drawable.medical_card_figure_male);
			}
		} else {
			if (rightSex == 2) {
				viewHolder.figureView.setImageResource(R.drawable.medical_card_figure_female);
			} else {
				viewHolder.figureView.setImageResource(R.drawable.medical_card_figure_male);
			}
		}

		viewHolder.tvSendTime.setText(timeDisplay.toString());
		viewHolder.tvContent.setText(entity.getCon());

		return convertView;
	}

	static class ViewHolder {
		public TextView tvSendTime;
		public TextView tvUserName;
		public TextView tvContent;
		public ImageView sendStateView;
		public ImageView figureView;
		public boolean isComMsg = true;
	}

	public interface OnFaultItemClickListener {
		public void onFaultItemClick(View view, MessageFrame msg);
	}

}
