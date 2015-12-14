package com.forchild.server;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.forchild.data.MessageFrame;
import com.forchild.data.SeniorInfoAutoMessage;
import com.forchild000.surface.ServiceCore;

public class DatabaseHelper {
	protected Context context;
	protected DBFrame dbFrame;
	protected Preferences preference;
	protected SQLiteDatabase db;

	protected static final String SENIORS_INFO_TABLE = "FORCHILD000_SENIORS_INFOMATION";
	protected static final String AUTO_MESSAGE_TABLE = "FORCHILD000_AUTO_MESSAGE";
	protected static final String MESSAGE_TABLE = "FORCHILD000_MESSAGE";
	protected static final String ACCIDENT_TABLE = "FORCHILD000_ACCIENDT_MESSAGE";
	protected static final String SOS_TABLE = "FORCHILD000_SOS_MESSAGE";
	protected static final String COMMON_MESSAGE_TABLE = "FORCHILD000_COMMON_MESSAGE";

	public DatabaseHelper(Context context) {
		this.context = context;
		dbFrame = new DBFrame(context);
	}

	/**
	 * 
	 * @param content
	 * @return id, 如果存在返回0，反之，返回其id
	 */
	public int insertCommonMessage(String content) {
		SQLiteDatabase db = dbFrame.getWritableDatabase();
		Cursor cursor = db.query(COMMON_MESSAGE_TABLE, null, "content = ?", new String[] { content }, null, null, "id");
		long insertResult = 0;
		int result = 0;
		if (cursor.getCount() == 0) {
			ContentValues values = new ContentValues();
			values.put("content", content);
			insertResult = db.insert(COMMON_MESSAGE_TABLE, null, values);
		}
		cursor.close();

		if (insertResult > 0) {
			Cursor idCursor = db.query(COMMON_MESSAGE_TABLE, new String[] { "id" }, "content = ?", new String[] { "content" }, null, null, "id");
			while (idCursor.moveToNext()) {
				result = idCursor.getInt(idCursor.getColumnIndex("id"));
			}
			idCursor.close();
		}
		db.close();
		return result;
	}

	public Cursor getCommonMessage() {
		db = dbFrame.getWritableDatabase();
		return db.query(COMMON_MESSAGE_TABLE, new String[] { "id", "content" }, null, null, null, null, null);
	}

	public int deleteCommonMessage(String selection, String[] condition) {
		SQLiteDatabase db = dbFrame.getWritableDatabase();
		int result = db.delete(COMMON_MESSAGE_TABLE, selection, condition);
		db.close();
		return result;
	}

	public long updateSeniorInfo(ContentValues cv, String selected, String[] condition) {
		SQLiteDatabase db = dbFrame.getWritableDatabase();
		Cursor cursor = db.query(SENIORS_INFO_TABLE, null, selected, condition, null, null, "id");
		long result = -1;
		if (cursor.getCount() > 0) {
			result = db.update(SENIORS_INFO_TABLE, cv, selected, condition);
		} else {
			result = db.insert(SENIORS_INFO_TABLE, null, cv);
		}
		db.close();
		return result;
	}

	public long insertSeniorInfo(ContentValues cv) {
		SQLiteDatabase db = dbFrame.getWritableDatabase();
		long result = db.insert(SENIORS_INFO_TABLE, null, cv);
		db.close();
		return result;
	}

	public void arrangeSeniors() {
		int id = 0;
		preference = new Preferences(context);
		SQLiteDatabase db = dbFrame.getReadableDatabase();
		Cursor cursor = db.query(SENIORS_INFO_TABLE, null, null, null, null, null, "id");
		db.delete(SENIORS_INFO_TABLE, null, null);
		while (cursor.moveToNext()) {
			String oid = cursor.getString(cursor.getColumnIndex("oid"));
			if (oid == null) {
				continue;
			}
			ContentValues cv = new ContentValues();
			cv.put("id", id);
			cv.put("oid", oid);
			cv.put("sex", cursor.getInt(cursor.getColumnIndex("sex")));
			cv.put("height", cursor.getInt(cursor.getColumnIndex("height")));
			cv.put("weight", cursor.getInt(cursor.getColumnIndex("weight")));
			cv.put("name", cursor.getString(cursor.getColumnIndex("name")));
			cv.put("nick", cursor.getString(cursor.getColumnIndex("nick")));
			cv.put("medical", cursor.getString(cursor.getColumnIndex("medical")));
			cv.put("blood", cursor.getString(cursor.getColumnIndex("blood")).toUpperCase());
			cv.put("phone", cursor.getString(cursor.getColumnIndex("phone")));
			cv.put("address", cursor.getString(cursor.getColumnIndex("address")));
			cv.put("code", cursor.getString(cursor.getColumnIndex("code")));
			cv.put("allergic", cursor.getString(cursor.getColumnIndex("allergic")));
			cv.put("birth", cursor.getLong(cursor.getColumnIndex("birth")));
			db.insert(SENIORS_INFO_TABLE, null, cv);
			++id;
		}
		preference.setLastSeniorId(id);
		db.close();
	}

	public int deleteSeniorInfo(String selected, String[] condition) {
		SQLiteDatabase db = dbFrame.getWritableDatabase();
		int result = db.delete(SENIORS_INFO_TABLE, selected, condition);
		Cursor cursor = db.query(SENIORS_INFO_TABLE, null, null, null, null, null, "id");
		db.delete(SENIORS_INFO_TABLE, null, null);
		int id = 0;
		preference = new Preferences(context);
		while (cursor.moveToNext()) {
			String oid = cursor.getString(cursor.getColumnIndex("oid"));
			if (oid == null) {
				continue;
			}
			ContentValues cv = new ContentValues();
			cv.put("id", id);
			cv.put("oid", oid);
			cv.put("sex", cursor.getInt(cursor.getColumnIndex("sex")));
			cv.put("height", cursor.getInt(cursor.getColumnIndex("height")));
			cv.put("weight", cursor.getInt(cursor.getColumnIndex("weight")));
			cv.put("name", cursor.getString(cursor.getColumnIndex("name")));
			cv.put("nick", cursor.getString(cursor.getColumnIndex("nick")));
			cv.put("medical", cursor.getString(cursor.getColumnIndex("medical")));
			cv.put("blood", cursor.getString(cursor.getColumnIndex("blood")).toUpperCase());
			cv.put("phone", cursor.getString(cursor.getColumnIndex("phone")));
			cv.put("address", cursor.getString(cursor.getColumnIndex("address")));
			cv.put("code", cursor.getString(cursor.getColumnIndex("code")));
			cv.put("allergic", cursor.getString(cursor.getColumnIndex("allergic")));
			cv.put("birth", cursor.getLong(cursor.getColumnIndex("birth")));
			db.insert(SENIORS_INFO_TABLE, null, cv);
			++id;
		}
		preference.setLastSeniorId(id);
		db.close();
		return result;
	}

	public int deleteSeniorInfo() {
		SQLiteDatabase db = dbFrame.getWritableDatabase();
		int result = db.delete(SENIORS_INFO_TABLE, null, null);
		db.close();
		return result;
	}

	public Cursor getSeniorInfo(String[] column, String selected, String[] condition) {
		db = dbFrame.getReadableDatabase();
		Cursor cursor = db.query(SENIORS_INFO_TABLE, column, selected, condition, null, null, "id");
		// db.close();
		return cursor;
	}

	public Cursor getSeniorInfo(String[] column, String selected, String[] condition, String orderBy) {
		db = dbFrame.getReadableDatabase();
		Cursor cursor = db.query(SENIORS_INFO_TABLE, column, selected, condition, null, null, orderBy);
		// db.close();
		return cursor;
	}

	public Cursor getAutoMessage() {
		db = dbFrame.getReadableDatabase();
		Cursor cursor = db.query(AUTO_MESSAGE_TABLE, null, null, null, null, null, "id");
		// db.close();
		return cursor;
	}

	public Cursor getAutoMessage(String[] column) {
		db = dbFrame.getReadableDatabase();
		Cursor cursor = db.query(AUTO_MESSAGE_TABLE, column, null, null, null, null, "id");
		// db.close();
		return cursor;
	}

	public Cursor getAutoMessage(String[] column, String selection, String[] condition) {
		db = dbFrame.getReadableDatabase();
		Cursor cursor = db.query(AUTO_MESSAGE_TABLE, column, selection, condition, null, null, "id");
		// db.close();
		return cursor;
	}

	public long addAutoMessage(ContentValues cv, int id) {
		SQLiteDatabase db = dbFrame.getWritableDatabase();
		Cursor cursor = db.query(AUTO_MESSAGE_TABLE, null, "id = ?", new String[] { id + "" }, null, null, "id");
		long result = -1;
		Log.e("DatabaseHelper.addAutoMessage", "add new Message, id:" + id + ", message:" + cv.toString());
		if (cursor.getCount() == 0) {
			result = db.insert(AUTO_MESSAGE_TABLE, null, cv);
		}
		db.close();
		return result;
	}

	public long addAutoMessage(SeniorInfoAutoMessage siam) {
		ContentValues values = new ContentValues();
		values.put("id", siam.getId());
		values.put("oid", siam.getOid());
		values.put("name", siam.getName());
		values.put("nick", siam.getNick());
		values.put("content", siam.getContent());
		values.put("year", siam.getYear());
		values.put("month", siam.getMonth());
		values.put("hour", siam.getHour());
		values.put("minute", siam.getMinute());
		values.put("day", siam.getDay());
		values.put("sex", siam.getSex());
		values.put("type", siam.getType());
		values.put("login_id", siam.getLoginId());
		return this.addAutoMessage(values, siam.getId());
	}

	public long updateAutoMessage(ContentValues cv, int id) {
		SQLiteDatabase db = dbFrame.getWritableDatabase();
		Cursor cursor = db.query(AUTO_MESSAGE_TABLE, null, "id = ?", new String[] { id + "" }, null, null, "id");
		long result = -1;
		if (cursor.getCount() > 0) {
			result = db.update(AUTO_MESSAGE_TABLE, cv, "id = ?", new String[] { id + "" });
		} else {
			result = db.insert(AUTO_MESSAGE_TABLE, null, cv);
		}
		db.close();
		return result;
	}

	public long updateAutoMessage(SeniorInfoAutoMessage siam) {
		ContentValues values = new ContentValues();
		values.put("id", siam.getId());
		values.put("oid", siam.getOid());
		values.put("name", siam.getName());
		values.put("nick", siam.getNick());
		values.put("content", siam.getContent());
		values.put("year", siam.getYear());
		values.put("month", siam.getMonth());
		values.put("hour", siam.getHour());
		values.put("minute", siam.getMinute());
		values.put("day", siam.getDay());
		values.put("sex", siam.getSex());
		values.put("type", siam.getType());
		return this.updateAutoMessage(values, siam.getId());
	}

	public int deleteAutoMessage(int id) {
		SQLiteDatabase db = dbFrame.getWritableDatabase();
		int result = db.delete(AUTO_MESSAGE_TABLE, "id = ?", new String[] { id + "" });
		db.close();
		return result;
	}

	public int deleteAutoMessage() {
		SQLiteDatabase db = dbFrame.getWritableDatabase();
		int result = db.delete(AUTO_MESSAGE_TABLE, null, null);
		db.close();
		return result;
	}

	public Cursor getMessage() {
		db = dbFrame.getReadableDatabase();
		Cursor cursor = db.query(MESSAGE_TABLE, null, null, null, null, null, "date", "100");
		// db.close();
		return cursor;
	}

	public Cursor getMessage(String[] column) {
		db = dbFrame.getReadableDatabase();
		Cursor cursor = db.query(MESSAGE_TABLE, column, null, null, null, null, "date", "100");
		// db.close();
		return cursor;
	}

	public Cursor getMessage(String[] column, String selection, String[] condition) {
		db = dbFrame.getReadableDatabase();
		Cursor cursor = db.query(MESSAGE_TABLE, column, selection, condition, null, null, "date", "100");
		// db.close();
		return cursor;
	}

	public Cursor getLastMessage(String loginId) {
		db = dbFrame.getReadableDatabase();
		Cursor cursor = db.query(MESSAGE_TABLE, new String[] { "oid", "content", "Max(date)" }, null, null, "oid", null, "oid");
		return cursor;
	}

	public int updateMessage(ContentValues values, String loginId, long date, int type) {
		SQLiteDatabase db = dbFrame.getWritableDatabase();
		int result = db.update(MESSAGE_TABLE, values, "login_id = ? AND date = ? AND type = ?",
				new String[] { loginId, String.valueOf(date), String.valueOf(type) });
		db.close();
		return result;
	}

	public long addMessage(ContentValues cv) {
		SQLiteDatabase db = dbFrame.getWritableDatabase();
		long result = -1;
		result = db.insert(MESSAGE_TABLE, null, cv);
		db.close();
		return result;
	}

	public long addMessage(MessageFrame msg) {
		SQLiteDatabase db = dbFrame.getWritableDatabase();
		long result = -1;
		ContentValues values = new ContentValues();
		values.put("login_id", msg.getLoginId());
		values.put("name", msg.getUname());
		values.put("date", msg.getDate());
		values.put("oid", msg.getOid());
		values.put("content", msg.getCon());
		values.put("send_state", msg.getState());
		values.put("type", msg.getUserMsgType());
		result = db.insert(MESSAGE_TABLE, null, values);
		db.close();
		return result;
	}

	public int deleteMessage(long date) {
		SQLiteDatabase db = dbFrame.getWritableDatabase();
		int result = db.delete(MESSAGE_TABLE, "date = ?", new String[] { date + "" });
		db.close();
		return result;
	}

	public int deleteMessage(String loginId, long date, int type) {
		SQLiteDatabase db = dbFrame.getWritableDatabase();
		int result = db.delete(MESSAGE_TABLE, "date = ? AND login_id = ? AND type = ?",
				new String[] { String.valueOf(date), loginId, String.valueOf(type) });
		db.close();
		return result;
	}

	public int deleteMessage() {
		SQLiteDatabase db = dbFrame.getWritableDatabase();
		int result = db.delete(MESSAGE_TABLE, null, null);
		db.close();
		return result;
	}

	public int deleteRedundantMessages(long date) {
		SQLiteDatabase db = dbFrame.getWritableDatabase();
		int result = db.delete(MESSAGE_TABLE, "date < ?", new String[] { date + "" });
		db.close();
		return result;
	}

	public Cursor getAccidentMessage(String[] column, String selection, String[] condition) {
		db = dbFrame.getReadableDatabase();
		Cursor cursor = db.query(ACCIDENT_TABLE, column, selection, condition, null, null, "date");
		// db.close();
		return cursor;
	}

	public Cursor getAccidentMessage() {
		return this.getAccidentMessage(null, null, null);
	}

	public long addAccidentMessage(ContentValues cv) {
		SQLiteDatabase db = dbFrame.getWritableDatabase();
		Cursor cursor = db.query(ACCIDENT_TABLE, null, "date = ?", new String[] { cv.getAsLong("date") + "" }, null, null, "date");
		long result = -1;
		if (cursor.getCount() > 0) {
			result = db.update(ACCIDENT_TABLE, cv, "date = ?", new String[] { cv.getAsLong("date") + "" });
		} else {
			result = db.insert(ACCIDENT_TABLE, null, cv);
		}
		db.close();
		return result;
	}

	public int deleteAccidentMessage() {
		SQLiteDatabase db = dbFrame.getWritableDatabase();
		int result = db.delete(SOS_TABLE, null, null);
		db.close();
		return result;
	}

	public int deleteAccidentMessage(String selection, String[] conditions) {
		SQLiteDatabase db = dbFrame.getWritableDatabase();
		int result = db.delete(ACCIDENT_TABLE, selection, conditions);
		db.close();
		return result;
	}

	public int deleteSOSMessage(String selection, String[] conditions) {
		SQLiteDatabase db = dbFrame.getWritableDatabase();
		int result = db.delete(SOS_TABLE, selection, conditions);
		db.close();
		return result;
	}

	public Cursor getSOSMessage(String[] column, String selection, String[] condition) {
		db = dbFrame.getReadableDatabase();
		Cursor cursor = db.query(SOS_TABLE, column, selection, condition, null, null, "date");
		// db.close();
		return cursor;
	}

	public Cursor getSOSMessage() {
		return this.getSOSMessage(null, null, null);
	}

	public long addSOSMessage(ContentValues cv) {
		SQLiteDatabase db = dbFrame.getWritableDatabase();
		Cursor cursor = db.query(SOS_TABLE, null, "date = ?", new String[] { cv.getAsLong("date") + "" }, null, null, "date");
		long result = -1;
		if (cursor.getCount() > 0) {
			result = db.update(SOS_TABLE, cv, "date = ?", new String[] { cv.getAsLong("date") + "" });
		} else {
			result = db.insert(SOS_TABLE, null, cv);
		}
		db.close();
		return result;
	}

	public int deleteSOSMessage() {
		SQLiteDatabase db = dbFrame.getWritableDatabase();
		int result = db.delete(SOS_TABLE, null, null);
		db.close();
		return result;
	}

	public void close() {
		if (this.db.isOpen()) {
			db.close();
		}
		db = null;
	}

	public void deleteAll() {
		this.deleteAutoMessage();
		this.deleteMessage();
		this.deleteSeniorInfo();
		this.deleteAccidentMessage();

	}

}
