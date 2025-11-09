package com.example.stage1db;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * שלב 1: יצירת Database והכנסת רשומה
 * ====================================
 *
 * Activity פשוט עם:
 * 1. שדות קלט לשם וציון
 * 2. כפתור להוספת סטודנט
 * 3. הצגת הודעה על הצלחה
 *
 * לצפייה בנתונים: השתמש ב-App Inspection ב-Android Studio!
 */
public class MainActivity extends AppCompatActivity {

    // UI Components
    private EditText etName, etGrade;
    private Button btnAddStudent;

    // Database Helper
    private StudentDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // אינציאליזציה של מסד הנתונים
        dbHelper = new StudentDatabaseHelper(this);

        // קישור ל-UI Components
        etName = findViewById(R.id.etName);
        etGrade = findViewById(R.id.etGrade);
        btnAddStudent = findViewById(R.id.btnAddStudent);

        // הגדרת האזנה ללחיצה על הכפתור
        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudent();
            }
        });
    }

    /**
     * פונקציה להוספת סטודנט
     * קוראת את הערכים מהשדות ומכניסה למסד הנתונים
     */
    private void addStudent() {
        // קריאת הערכים מהשדות
        String name = etName.getText().toString().trim();
        String gradeStr = etGrade.getText().toString().trim();

        // בדיקת תקינות קלט
        if (name.isEmpty()) {
            Toast.makeText(this, "נא להזין שם", Toast.LENGTH_SHORT).show();
            return;
        }

        if (gradeStr.isEmpty()) {
            Toast.makeText(this, "נא להזין ציון", Toast.LENGTH_SHORT).show();
            return;
        }

        int grade;
        try {
            grade = Integer.parseInt(gradeStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "ציון לא תקין", Toast.LENGTH_SHORT).show();
            return;
        }

        // בדיקת טווח ציון (0-100)
        if (grade < 0 || grade > 100) {
            Toast.makeText(this, "הציון חייב להיות בין 0 ל-100", Toast.LENGTH_SHORT).show();
            return;
        }

        // הוספת הסטודנט למסד הנתונים
        long newRowId = dbHelper.addStudent(name, grade);

        // בדיקה אם ההוספה הצליחה
        if (newRowId != -1) {
            Toast.makeText(this,
                    "סטודנט נוסף בהצלחה! ID: " + newRowId,
                    Toast.LENGTH_LONG).show();

            // ניקוי השדות
            etName.setText("");
            etGrade.setText("");
            etName.requestFocus();

            // הצגת הודעה - כיצד לצפות בנתונים
            Toast.makeText(this,
                    "לצפייה בנתונים: App Inspection ← View ← Android Studio",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "שגיאה בהוספת סטודנט", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * סגירת מסד הנתונים כאשר ה-Activity נהרס
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}