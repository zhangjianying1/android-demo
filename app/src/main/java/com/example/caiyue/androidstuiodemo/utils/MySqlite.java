package com.example.caiyue.androidstuiodemo.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class MySqlite extends SQLiteOpenHelper{
    public static final String DBNAME = "base";
    public static final String NAME = "name";
    public static final String DATA = "data";
    public static final String TIME = "time";
    public static final String TABLENAME = "security";
    private static MySqlite  mySqlite;
    public static final String CREATE_BOOK ="create table " + TABLENAME + " ("
            +"id integer primary key autoincrement, "
            + NAME + " text, "
            + DATA + " text, "
            + TIME + " text)";
    private Context context;
    public MySqlite(Context context) {
        super(context, DBNAME, null, 1);
        this.context = context;
    }
    public static  MySqlite getInstance(Context context){

        if (mySqlite == null) {
            synchronized(MySqlite.class){
                return mySqlite = new MySqlite(context);
            }

        }
        return mySqlite;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("sql", "ok");
        db.execSQL(CREATE_BOOK);
        Toast.makeText(context, "数据库和数据库表创建成功",Toast.LENGTH_SHORT).show() ;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    /**
     * 插入缓存，没有就插入，有就替换
     *
     * @param name  地址
     * @param data json数据
     */
    public synchronized void insertData(String name, String data) {
        SQLiteDatabase db = mySqlite.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(NAME, name);
            values.put(DATA, data);
            values.put(TIME, System.currentTimeMillis());
            db.replace(TABLENAME, null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    /**
     * 根据url获取缓存数据
     *
     * @param name 地址
     * @return 数据
     */
    public synchronized String getData(String name) {
        String result = "";
        SQLiteDatabase db = mySqlite.getReadableDatabase();
        db.beginTransaction();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLENAME + " WHERE " +  NAME + " = ?", new String[]{name});
            while (cursor.moveToNext()) {
                result = cursor.getString(cursor.getColumnIndex(MySqlite.DATA));
            }
            cursor.close();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        return result;
    }
    public void delete(String name) {
        SQLiteDatabase db = mySqlite.getWritableDatabase();
        db.beginTransaction();
        try {
            String sql = "DELETE FROM " + TABLENAME + " WHERE " + NAME + " = '" + name +"'";
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }
//    private static final String TAG = "LouSQLite";
//    private static MySqlite INSTANCE;
//    private final ICallBack callBack;
//
//
//    private MySqlite(Context context, ICallBack callBack) {
//        super(context, callBack.getDatabaseName(), null, callBack.getVersion());
//        this.callBack = callBack;
//    }
//
//    public static void init(@NonNull Context context, @NonNull ICallBack callBack) {
//        INSTANCE = new MySqlite(context, callBack);
//    }
//
//
//    public static <T> void insert(String tableName, T entity) {
//        SQLiteDatabase db = INSTANCE.getWritableDatabase();
//        db.beginTransaction();
//        try {
//            ContentValues values = new ContentValues();
//            INSTANCE.callBack.assignValuesByEntity(tableName, entity, values);
//            db.insert(tableName, null, values);
//            values.clear();
//            db.setTransactionSuccessful();
//        } finally {
//            db.endTransaction();
//            db.close();
//        }
//    }
//
//
//    public static <T> void insert(String tableName, List<T> entities) {
//        SQLiteDatabase db = INSTANCE.getWritableDatabase();
//        db.beginTransaction();
//        try {
//            ContentValues values = new ContentValues();
//            for (T entity : entities) {
//                INSTANCE.callBack.assignValuesByEntity(tableName, entity, values);
//                db.insert(tableName, null, values);
//                values.clear();
//            }
//            db.setTransactionSuccessful();
//        } finally {
//            db.endTransaction();
//            db.close();
//        }
//    }
//
//    public static <T> void update(String tableName, T entity, String whereClause, String[] whereArgs) {
//        SQLiteDatabase db = INSTANCE.getWritableDatabase();
//        db.beginTransaction();
//        try {
//            ContentValues values = new ContentValues();
//            INSTANCE.callBack.assignValuesByEntity(tableName, entity, values);
//            db.update(tableName, values, whereClause, whereArgs);
//            values.clear();
//            db.setTransactionSuccessful();
//        } finally {
//            db.endTransaction();
//            db.close();
//        }
//    }
//
//
//    public static <T> List<T> query(String tableName, @NonNull String queryStr, @Nullable String[] whereArgs) {
//        SQLiteDatabase db = INSTANCE.getReadableDatabase();
//        Cursor cursor = db.rawQuery(queryStr, whereArgs);
//        try {
//            List<T> lists = new ArrayList<>(cursor.getCount());
//            if (cursor.moveToFirst()) {
//                do {
//                    T entity = INSTANCE.callBack.newEntityByCursor(tableName, cursor);
//                    if (entity != null) {
//                        lists.add(entity);
//                    }
//                } while (cursor.moveToNext());
//            }
//            return lists;
//        } finally {
//            cursor.close();
//            db.close();
//        }
//
//    }
//
//    public static void deleteFrom(String tableName) {
//
//        SQLiteDatabase db = INSTANCE.getWritableDatabase();
//        db.beginTransaction();
//        try {
//            String sql = "DELETE FROM " + tableName;
//            db.execSQL(sql);
//            db.setTransactionSuccessful();
//        } finally {
//            db.endTransaction();
//            db.close();
//        }
//    }
//
//    // delete的适用场合是涉及到删除的对象数量较少时。
//    // 当删除多条数据时（例如：500条），通过循环的方式来一个一个的删除需要12s，而使用execSQL语句结合(delete from table id in("1", "2", "3"))的方式只需要50ms
//    public static void delete(String tableName, String whereClause, String[] whereArgs) {
//        SQLiteDatabase db = INSTANCE.getWritableDatabase();
//        db.beginTransaction();
//        try {
//            db.delete(tableName, whereClause, whereArgs);
//            db.setTransactionSuccessful();
//        } finally {
//            db.endTransaction();
//            db.close();
//        }
//    }
//
//    /*
//     * 当操作数据较多时，直接使用sql语句或许效率更高
//     *
//     * 执行sql语句（例如: String sql = "delete from tableName where mac in ('24:71:89:0A:DD:82', '24:71:89:0A:DD:83','24:71:89:0A:DD:84')"）
//     * 注意：db.execSQL文档中有说明"the SQL statement to be executed. Multiple statements separated by semicolons are not supported."，
//     * 也就是说通过分号分割的多个statement操作是不支持的。
//     *
//     */
//    public static void execSQL(String sql) {
//        SQLiteDatabase db = INSTANCE.getWritableDatabase();
//        db.beginTransaction();
//        try {
//            db.execSQL(sql);
//            db.setTransactionSuccessful();
//        } finally {
//            db.endTransaction();
//            db.close();
//        }
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        List<String> tablesSQL = callBack.createTablesSQL();
//        for (String create_table : tablesSQL) {
//            db.execSQL(create_table);
//            Log.d(TAG, "create table " + "[ \n" + create_table + "\n ]" + " successful! ");
//        }
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
//        callBack.onUpgrade(sqLiteDatabase, oldVersion, newVersion);
//    }
//
//    public interface ICallBack {
//        String getDatabaseName();
//
//        int getVersion();
//
//        void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
//
//        List<String> createTablesSQL();
//
//        <T> void assignValuesByEntity(String tableName, T entity, ContentValues values);
//
//        <T> T newEntityByCursor(String tableName, Cursor cursor);
//    }
}