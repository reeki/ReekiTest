package com.forchild.frame.servicenet.imple;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.forchild.data.AccidentInfo;
import com.forchild.data.BaseProtocolFrame;
import com.forchild.data.MessageAccident;
import com.forchild.data.MessageAttention;
import com.forchild.data.MessageFrame;
import com.forchild.data.MessageHelp;
import com.forchild.data.MessageSystem;
import com.forchild.data.RequestChildInformation;
import com.forchild.data.RequestUpdateLocation;
import com.forchild.data.SOSInfo;
import com.forchild.server.DatabaseHelper;
import com.forchild.server.Preferences;
import com.forchild000.surface.AccidentDisplayActivity;
import com.forchild000.surface.LoginActivity;
import com.forchild000.surface.R;
import com.forchild000.surface.ServiceCore;
import com.forchild000.surface.WarningActivity;

public class ServiceUpdateLocationProcess implements ServiceNetworkResultHandler {

	protected Context context;
	protected Preferences preference;

	public ServiceUpdateLocationProcess() {

	}

	public ServiceUpdateLocationProcess(Context context, Preferences preference) {
		this.context = context;
		this.preference = preference;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public void setPreferences(Preferences preferences) {
		this.preference = preferences;
	}

	@Override
	public int getType() {
		return BaseProtocolFrame.UPDATE_LOCATION;
	}

	@Override
	public int process(BaseProtocolFrame source) {
		if (source != null && source instanceof RequestUpdateLocation) {
			ServiceCore sc = (ServiceCore) context;
			switch (source.getReq()) {
			case BaseProtocolFrame.RESPONSE_TYPE_OKAY:
				RequestUpdateLocation rul = (RequestUpdateLocation) source;
				DatabaseHelper dbHelper = new DatabaseHelper(context);
				for (MessageFrame mf : rul.getMessage()) {
					Intent msIntent = new Intent(context, WarningActivity.class);
					switch (mf.getType()) {
					case MessageFrame.SYSTEM_MESSAGE:
						if (mf instanceof MessageSystem) {
							MessageSystem ms = (MessageSystem) mf;
							msIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
							msIntent.putExtra("type", WarningActivity.WARNING_TYPE_SYSTEM_MESSAGE);
							msIntent.putExtra("date", ms.getDate());
							msIntent.putExtra("content", ms.getContent());
							context.startActivity(msIntent);
						}
						break;
					case MessageFrame.SENIORS_ACCIDENT_MESSAGE:
						if (mf instanceof MessageAccident) {
							MessageAccident ma = (MessageAccident) mf;
							// msIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
							// Cursor cursor = dbHelper.getSeniorInfo(new
							// String[] { "nick", "name" }, "oid = ?", new
							// String[] { ma.getOldid() + "" });
							// if (cursor.moveToNext()) {
							// msIntent.putExtra("type",
							// WarningActivity.WARNING_TYPE_ACCIDENT_MESSAGE);
							// msIntent.putExtra("msg", ma);
							// String nick =
							// cursor.getString(cursor.getColumnIndex("nick"));
							// String name =
							// cursor.getString(cursor.getColumnIndex("name"));

							// if (nick != null && nick.length() > 0) {
							// msIntent.putExtra("name", nick);
							// } else if (name != null) {
							// msIntent.putExtra("name", name);
							// } else {
							// msIntent.putExtra("name",
							// context.getText(R.string.unknown).toString());
							// }

							// 发向AccidentHistoryActivity
							Intent accidentBroadcastIntent = new Intent();
							accidentBroadcastIntent.setAction("com.forchild.messages.acc.display");
							accidentBroadcastIntent.putExtra("bean", ma);
							context.sendBroadcast(accidentBroadcastIntent);
							Cursor accMsgCursor = dbHelper.getAccidentMessage();
							int id = accMsgCursor.getCount();

							Cursor seniorCursor = dbHelper.getSeniorInfo(new String[] { "nick", "name", "oid" }, "oid = ?",
									new String[] { ma.getOid() + "" });
							String name = new String();
							if (seniorCursor.moveToNext()) {
								String nickBuff = seniorCursor.getString(seniorCursor.getColumnIndex("nick"));
								String nameBuff = seniorCursor.getString(seniorCursor.getColumnIndex("name"));
								if (nickBuff != null && nickBuff.length() > 0) {
									name = nickBuff;
								} else if (name != null) {
									name = nameBuff;
								}
							}
							ma.setUname(name);

							ContentValues cv = new ContentValues();
							cv.put("id", ++id);
							cv.put("acc", ma.getAcc());
							cv.put("lo", ma.getLo());
							cv.put("la", ma.getLa());
							cv.put("date", ma.getDate());
							cv.put("uname", ma.getUname());
							cv.put("address", ma.getAddress());
							cv.put("sos_id", ma.getSosId());
							cv.put("belongs", ServiceCore.getLoginId());
							cv.put("oid", ma.getOid());
							dbHelper.addAccidentMessage(cv);
							accMsgCursor.close();
							dbHelper.close();
							// context.startActivity(msIntent);

							// } else {
							// Log.e("ServiceUpdateLocationProcess.process",
							// "无对应关注人" + ma.getOldid());
							// }
							AccidentInfo accInfo = new AccidentInfo(id, ma.getLo(), ma.getLa(), ma.getDate(), ma.getUname(), ma.getOid(),
									ma.getSosId());
							accInfo.setAddress(ma.getAddress());
							Intent accNotifiIntent = new Intent(sc, AccidentDisplayActivity.class);
							accNotifiIntent.putExtra("type", AccidentDisplayActivity.ACCIDENT_TYPE_ACCIDENT);
							accNotifiIntent.putExtra("accident_info", accInfo);
							PendingIntent accPIntent = PendingIntent.getActivity(sc, ma.getSosId(), accNotifiIntent, PendingIntent.FLAG_UPDATE_CURRENT);
							NotificationManager mNotifiManager = (NotificationManager) sc.getSystemService(Context.NOTIFICATION_SERVICE);
							NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(sc);
							mBuilder.setContentTitle(sc.getText(R.string.servicecore_accmsg_title))
									.setContentText(
											sc.getText(R.string.warning_accident_message_content_front) + accInfo.getName()
													+ sc.getText(R.string.warning_accident_message_content_mid) + accInfo.getAddress()
													+ sc.getText(R.string.warning_accident_message_content_behind))
									.setContentIntent(accPIntent)
									.setTicker(sc.getText(R.string.servicecore_accmsg_title) + ":" + accInfo.getAddress())
									.setWhen(System.currentTimeMillis()).setPriority(NotificationCompat.PRIORITY_DEFAULT)
									.setAutoCancel(true)
									.setOngoing(false)
									.setDefaults(Notification.DEFAULT_ALL)
									.setSmallIcon(R.drawable.ic_launcher);
							mNotifiManager.notify(id, mBuilder.build());
						}
						break;
					case MessageFrame.ACCIDENT_HELP_MESSAGE:
						if (mf instanceof MessageHelp) {
							MessageHelp mh = (MessageHelp) mf;
							Intent sosBroadcastIntent = new Intent();
							sosBroadcastIntent.setAction("com.forchild.messages.sos.display");
							sosBroadcastIntent.putExtra("bean", mh);
							sc.sendBroadcast(sosBroadcastIntent);
							Cursor sosMsgCursor = dbHelper.getSOSMessage();
							int sosId = sosMsgCursor.getCount();
							sosMsgCursor.close();
							ContentValues sosValues = new ContentValues();
							sosValues.put("id", ++sosId);
							sosValues.put("acc", mh.getAcc());
							sosValues.put("lo", mh.getLo());
							sosValues.put("la", mh.getLa());
							sosValues.put("date", mh.getDate());
//							sosValues.put("uname", mh.getUname());
							sosValues.put("uname", "跌倒求救");
							sosValues.put("sos_id", mh.getSosId());
							sosValues.put("belongs", ServiceCore.getLoginId());
							dbHelper.close();
							dbHelper.addSOSMessage(sosValues);

							SOSInfo sosInfo = new SOSInfo(sosId, mh.getLo(), mh.getLa(), mh.getDate(), mh.getUname(), mh.getCon(), mh.getSosId());

							// msIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
							// msIntent.putExtra("type",
							// WarningActivity.WARNING_TYPE_SOS_MESSAGE);
							// msIntent.putExtra("msg", mh);
							// context.startActivity(msIntent);

							Intent SOSNotifiIntent = new Intent(sc, AccidentDisplayActivity.class);
							SOSNotifiIntent.putExtra("type", AccidentDisplayActivity.ACCIDENT_TYPE_SOS);
							SOSNotifiIntent.putExtra("accident_info", sosInfo);
							PendingIntent pIntent = PendingIntent.getActivity(sc, mh.getSosId(), SOSNotifiIntent, PendingIntent.FLAG_UPDATE_CURRENT);
							NotificationManager mNotificationManager = (NotificationManager) sc.getSystemService(Context.NOTIFICATION_SERVICE);
							NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(sc);
							mBuilder.setContentTitle(sc.getText(R.string.servicecore_sosmsg_title)).setContentText(sosInfo.getContent())
									.setContentIntent(pIntent)
									.setTicker(sc.getText(R.string.servicecore_sosmsg_title) + ":" + sosInfo.getContent())
									.setWhen(System.currentTimeMillis()).setPriority(NotificationCompat.PRIORITY_DEFAULT).setAutoCancel(true)
									.setOngoing(false).setDefaults(Notification.DEFAULT_ALL).setSmallIcon(R.drawable.ic_launcher);
							mNotificationManager.notify(sosId, mBuilder.build());

						}
						break;
					case MessageFrame.USER_INFO_CHANGED_MESSAGE:
						Preferences preference = new Preferences(context);
						ServiceCore.addNetTask(new RequestChildInformation(preference.getSid()));
						break;
					case MessageFrame.REQUEST_ATTENTION:
						if (mf instanceof MessageAttention) {
							MessageAttention mat = (MessageAttention) mf;
							msIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							Cursor cursor = dbHelper.getSeniorInfo(new String[] { "nick", "name" }, "oid = ?", new String[] { mat.getOid() + "" });
							Log.e("ServiceUpdateLocation", mat.toString() + "cursor count:" + cursor.getCount());
							if (cursor.moveToNext()) {
								msIntent.putExtra("type", WarningActivity.WARNING_TYPE_REQUEST_ATTENTION);
								msIntent.putExtra("msg", mat);
								String nick = cursor.getString(cursor.getColumnIndex("nick"));
								String name = cursor.getString(cursor.getColumnIndex("name"));

								if (nick != null && nick.length() > 0) {
									msIntent.putExtra("name", nick);
								} else if (name != null) {
									msIntent.putExtra("name", name);
								} else {
									msIntent.putExtra("name", context.getText(R.string.unknown).toString());
								}

//								context.startActivity(msIntent);
								
								StringBuffer contentText = new StringBuffer();
								if (mat.getNick() != null && mat.getNick().length() > 0) {
									contentText.append(mat.getNick());
									contentText.append(",");

								}

								if (mat.getPhone() != null && mat.getPhone().length() > 0) {
									contentText.append("电话号码:");
									contentText.append(mat.getPhone());
									contentText.append(",");
								}

								contentText.append(sc.getText(R.string.warning_attention_message_content));
								contentText.append(msIntent.getStringExtra("name"));
								contentText.append(sc.getText(R.string.warning_attention_message_content_agree));
								
								PendingIntent attPIntent = PendingIntent.getActivity(sc, 0, msIntent, PendingIntent.FLAG_UPDATE_CURRENT);
								NotificationManager mNotifiManager = (NotificationManager) sc.getSystemService(Context.NOTIFICATION_SERVICE);
								NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(sc);
								mBuilder.setContentTitle(sc.getText(R.string.warning_attention_request_title))
										.setContentText(contentText)
										.setContentIntent(attPIntent)
										.setTicker(contentText)
										.setWhen(System.currentTimeMillis())
										.setPriority(NotificationCompat.PRIORITY_DEFAULT)
										.setAutoCancel(true)
										.setOngoing(false)
										.setDefaults(Notification.DEFAULT_ALL)
										.setSmallIcon(R.drawable.ic_launcher);
								mNotifiManager.notify(0, mBuilder.build());
							} else {
								Log.e("ServiceUpdateLocationProcess.process", "无对应关注人" + mat.getOid());
							}

							cursor.close();
							dbHelper.close();
						}
						break;
					default:
						break;
					}
				}
				break;
			case BaseProtocolFrame.RESPONSE_TYPE_NO_LOGIN:
				Toast.makeText(context, context.getText(R.string.response_error_no_login), Toast.LENGTH_SHORT).show();
				if (preference == null) {
					preference = new Preferences(context);
				}
				preference.setLoginState(false);
				Intent logoutIntent = new Intent();
				logoutIntent.setAction("com.forolder.logout.activity");
				context.sendBroadcast(logoutIntent);
				Intent intent = new Intent(context, LoginActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
				break;
			case BaseProtocolFrame.RESPONSE_TYPE_OTHER_LOGIN:
				Toast.makeText(context, context.getText(R.string.response_error_login_other_phone), Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
		return 0;
	}

}
