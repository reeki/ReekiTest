package com.forchild.server;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBFrame extends SQLiteOpenHelper {

	public DBFrame(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public DBFrame(Context context) {
		this(context, "forchild000", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE `FORCHILD000_MESSAGE` (`login_id` varchar(11), `name` varchar(32), `date` long, `oid` int, `content` varchar(512), `send_state` int, "
				+ "`type` int default 4)"); // type 10以下是用户发出的消息，10以上是用户收到的消息,
											// 其中4为用户手动发送的消息

		db.execSQL("CREATE TABLE `FORCHILD000_SENIORS_INFOMATION` (`id` int, `belongs` varchar(11), `oid` int, `sex` int, `name` varchar(16), "
				+ "`nick` varchar(32), `medical` varchar(256), `height` int, `weight` int, `blood` varchar(2), "
				+ "`phone` varchar(11), `address` varchar(256), `code` varchar(6), `birth` long, `allergic` varchar(128))");

		db.execSQL("CREATE TABLE `FORCHILD000_AUTO_MESSAGE` (`id` int, `oid` int, `name` varchar(16), `nick` varchar(32), "
				+ "`content` varchar(512), `year` int, `month` int, `day` int, `hour` int, `minute` int, `sex` int default 1, `type` int default 0, "
				+ "`login_id` varchar(11))");

		db.execSQL("CREATE TABLE `FORCHILD000_ACCIENDT_MESSAGE` (`id` int, `belongs` varchar(11), `acc` int, `lo` double, `la` double, `date` long, `uname` varchar(32), `address` varchar(256), `sos_id` int, `oid` int)");

		db.execSQL("CREATE TABLE `FORCHILD000_SOS_MESSAGE` (`id` int, `belongs` varchar(11), `acc` int, `lo` double, `la` double, `date` long, `uname` varchar(32), 'content' varchar(512), `sos_id` int)");

		db.execSQL("CREATE TABLE `FORCHILD000_COMMON_MESSAGE` (`id` INTEGER PRIMARY KEY autoincrement, `content` varchar(512))");

		db.execSQL("CREATE TABLE `FORCHILD000_LOG` (`id` INTEGER PRIMARY KEY autoincrement, `content` varchar(512))");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
