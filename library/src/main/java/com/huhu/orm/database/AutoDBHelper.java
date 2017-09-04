package com.huhu.orm.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by steward on 2017/8/24.
 */

public class AutoDBHelper extends SQLiteOpenHelper {
    private static volatile AutoDBHelper INSTANCE = null;
    private static final String TAG = "AutoDatabaseHelper";
    private static String ID = "id";// 主键
    private static final String DB_NAME = "ab_db";//数据库名字
    private static final int DB_VERSION = 3;   // 数据库版本

    private AutoDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static AutoDBHelper getINSTANCE(Context context) {
        if (null == INSTANCE) {
            synchronized (AutoDBHelper.class) {
                if (null == INSTANCE) {
                    INSTANCE = new AutoDBHelper(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    public <T> List<T> get(Class<T> clazz) {
        List<T> results = new ArrayList<>();
        Field[] fs = clazz.getDeclaredFields();
        List<Field> usableFields = new ArrayList<>();
        for (Field f : fs) {
            if (!f.getName().equals("$change") && !f.getName().equals("serialVersionUID")) {
                usableFields.add(f);
            }
        }
        results = queryData(formatName(clazz.getName()), usableFields, clazz);
        return results;
    }

    private <T> List<T> queryData(String tableName, List<Field> fields, Class<T> tClass) {
        List<T> results = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        try {
            cursor = db.rawQuery("select * from " + tableName, null);
        }catch (Exception e) {
            return results;
        }
        T object = null;
        try {
            object = tClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        while (cursor.moveToNext()) {
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    field.set(object, cursor.getString(cursor.getColumnIndex(field.getName())));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            results.add(object);

        }
        cursor.close();
        db.close();
        return results;
    }

    public void save(Object object) {
        Class clazz = object.getClass();
        Field[] fs = clazz.getDeclaredFields();
        List<String> usableParams = new ArrayList<>();
        List<Field> usableFields = new ArrayList<>();
        for (Field f : fs) {
            if (!f.getName().equals("$change") && !f.getName().equals("serialVersionUID")) {
                usableParams.add(f.getName());
                usableFields.add(f);
            }
        }
        createTable(formatName(clazz.getName()), usableParams);
        insertData(formatName(clazz.getName()), object, usableFields);
    }

    private void createTable(String tableName, List<String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE if not exists ").append(tableName).append("(").append(ID).append(" integer primary key autoincrement");
        for (String param : params) {
            sb.append(", ").append(param);
        }
        sb.append(")");
        try {
            getWritableDatabase().execSQL(sb.toString());
        } catch (SQLException e) {
            Log.e(TAG, "onCreate " + tableName + "Error" + e.toString());
        }

    }

    private void insertData(String tableName, Object object, List<Field> fields) {
        ContentValues values = new ContentValues();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                values.put(field.getName(), field.get(object).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        getWritableDatabase().insert(tableName, null, values);
    }

    //表的名字为去除掉"."的路径+类名
    private String formatName(String oriName) {
        return oriName.replace(".", "");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
