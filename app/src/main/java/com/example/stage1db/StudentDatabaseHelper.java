package com.example.stage1db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * שלב 1: יצירת Database והכנסת רשומה
 * ====================================
 *
 * מחלקה זו מנהלת את מסד הנתונים המקומי
 * בשלב זה: רק יצירה והכנסה - עדיין אין קריאה!
 */
public class StudentDatabaseHelper extends SQLiteOpenHelper {

    // קבועים למסד הנתונים
    private static final String DATABASE_NAME = "students.db";
    private static final int DATABASE_VERSION = 1;

    // שם הטבלה והעמודות
    private static final String TABLE_STUDENTS = "students";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_GRADE = "grade";

    /**
     * Constructor - מקבל Context מה-Activity
     */
    public StudentDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * onCreate - נקראת פעם אחת ביצירה הראשונה של מסד הנתונים
     * כאן ניצור את הטבלה
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // יצירת טבלת students עם 3 עמודות
        String createTableQuery = "CREATE TABLE " + TABLE_STUDENTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_GRADE + " INTEGER)";

        // הרצת השאילתה
        db.execSQL(createTableQuery);
    }

    /**
     * onUpgrade - נקראת כאשר משדרגים את גרסת מסד הנתונים
     * בשלב זה: פשוט נמחק ונייצר מחדש
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // מחיקת הטבלה הישנה
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        // יצירה מחדש
        onCreate(db);
    }

    /**
     * הוספת סטודנט חדש
     *
     * @param name שם הסטודנט
     * @param grade הציון
     * @return המזהה של הרשומה החדשה, או -1 במקרה של שגיאה
     */
    public long addStudent(String name, int grade) {
        // פתיחת מסד הנתונים במצב כתיבה
        SQLiteDatabase db = this.getWritableDatabase();

        // יצירת ContentValues - מחזיק את הערכים להכנסה
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_GRADE, grade);
        // שים לב: לא צריך לציין את COLUMN_ID - הוא אוטומטי!

        // הכנסת הנתונים לטבלה
        long newRowId = db.insert(TABLE_STUDENTS, null, values);

        // סגירת מסד הנתונים
        db.close();

        // החזרת המזהה של הרשומה החדשה
        return newRowId;
    }

    // בשלבים הבאים נוסיף כאן פונקציות לקריאה, עדכון ומחיקה!
}
