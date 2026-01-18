package com.example.mydb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(@Nullable Context context) {
        super(context, "University", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String TABLE_SUBJECT = "CREATE TABLE subject(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "subject_name TEXT, subject_time TEXT, subject_credit INTEGER)";
        db.execSQL(TABLE_SUBJECT);
        String TABLE_STUDENT = "CREATE TABLE student(ID INTEGER PRIMARY KEY UNIQUE, name TEXT, image BLOB);";
        db.execSQL(TABLE_STUDENT);
        String TABLE_TEACHER = "CREATE TABLE teacher(ID INTEGER PRIMARY KEY UNIQUE, name TEXT, image BLOB);";
        db.execSQL(TABLE_TEACHER);
        String TABLE_STUDENT_SUBJECT = "CREATE TABLE student_subject(ID INTEGER PRIMARY KEY, " +
                "subject_name TEXT, subject_credit INTEGER, student_id INTEGER," +
                "FOREIGN KEY(student_id) REFERENCES student(ID))";
        db.execSQL(TABLE_STUDENT_SUBJECT);
        String TABLE_TEACHER_SUBJECT = "CREATE TABLE teacher_subject(ID INTEGER PRIMARY KEY, " +
                "subject_name TEXT, subject_time TEXT, " +
                "teacher_id INTEGER," +
                "FOREIGN KEY(teacher_id) REFERENCES teacher(ID))";
        db.execSQL(TABLE_TEACHER_SUBJECT);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public Cursor getSubjects() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT ID, subject_name , subject_time, subject_credit FROM " + "subject";
        return db.rawQuery(query, null);
    }
    public Cursor getStudents() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("student",
                new String[]{"ID", "name"},
                null,
                null,
                null, null, null);
    }
    public Cursor getTeachers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("teacher",
                new String[]{"ID", "name"},
                null,
                null,
                null, null, null);
    }
    public Cursor getSubjectByName(String subjectName) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {"ID", "subject_name", "subject_time", "subject_credit"};
        String selection = "subject_name = ?";
        String[] selectionArgs = {subjectName};
        return db.query(
                "subject",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }
    public boolean addSubject(String name, String time, String credit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("subject_name", name);
        values.put("subject_time", time);
        values.put("subject_credit", Integer.parseInt(credit));
        return db.insert("subject", null, values) > 0;
    }
    public boolean addStudent(String name, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("id", id);
        return db.insert("student", null, values) > 0;
    }
    public boolean addTeacher(String name, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("id", id);
        return db.insert("teacher", null, values) > 0;
    }
    public boolean addSubjectForTeacher(String id, String subject_name, String subject_time, String subject_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ID", subject_id);
        values.put("subject_name", subject_name);
        values.put("subject_time", subject_time);
        values.put("teacher_id", id);
        return db.insert("teacher_subject", null, values) > 0;
    }
    public boolean addSubjectForStudent(String id, String subject_name, String subject_credit, String subject_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ID", subject_id);
        values.put("subject_name", subject_name);
        values.put("subject_credit", subject_credit);
        values.put("student_id", id);
        return db.insert("student_subject", null, values) > 0;
    }
    public boolean addSubjectForSpecificStudent(String id, String subject_id) {
        Cursor cursor = getSubjectValuesForStudent(subject_id);
        boolean state = false;
        if (cursor.moveToFirst()) {
            String ID = cursor.getString(0);
            String name = cursor.getString(1);
            String credit = cursor.getString(2);
            state = addSubjectForStudent(id, name, credit, ID);
        }
        cursor.close();
        return state;
    }
    public boolean addSubjectForSpecificTeacher(String id, String subject_id) {
        Cursor cursor = getSubjectValuesForTeacher(subject_id);
        boolean state = false;
        if (cursor.moveToFirst()) {
            String ID = cursor.getString(0);
            String name = cursor.getString(1);
            String credit = cursor.getString(2);
            state = addSubjectForTeacher(id, name, credit, ID);
        }
        cursor.close();
        return state;
    }
    private Cursor getSubjectValuesForStudent(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("subject",
                new String[]{"ID", "subject_name", "subject_credit"},
                "ID = ?",
                new String[]{id},
                null, null, null);
    }
    private Cursor getSubjectValuesForTeacher(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("subject",
                new String[]{"ID", "subject_name", "subject_time"},
                "ID = ?",
                new String[]{id},
                null, null, null);
    }
    public Cursor getSubjectForStudent(String studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("student_subject",
                new String[]{"ID", "subject_name", "subject_credit"},
                "student_id = ?",
                new String[]{studentId},
                null, null, null);
    }
    public boolean getSpecificSubjectIdForStudent(String subject_id, String studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {"ID"};
        String selection = "ID = ? AND student_id = ?";
        String[] selectionArgs = {subject_id, studentId};
        Cursor cursor = db.query(
                "student_subject",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        int subjectId = -1;
        if (cursor != null && cursor.moveToFirst()) {
            subjectId = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
            cursor.close();
        }
        db.close();
        return subjectId > 0;
    }
    public boolean getSpecificSubjectIdForTeacher(String subject_id, String teacher_Id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {"ID"};
        String selection = "ID = ? AND teacher_id = ?";
        String[] selectionArgs = {subject_id, teacher_Id};
        Cursor cursor = db.query(
                "teacher_subject",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        int subjectId = -1;
        if (cursor != null && cursor.moveToFirst()) {
            subjectId = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
            cursor.close();
        }
        db.close();
        return subjectId > 0;
    }
    public Cursor getStudent(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("student",
                new String[]{"ID", "name"},
                "ID = ?",
                new String[]{id},
                null, null, null);
    }
    public Cursor getTeacher(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("teacher",
                new String[]{"ID", "name"},
                "ID = ?",
                new String[]{id},
                null, null, null);
    }
    public Cursor getSubject(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("subject",
                new String[]{"ID", "subject_name"},
                "ID = ?",
                new String[]{id},
                null, null, null);
    }
    public Cursor getSubjectForTeacher(String teacherId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("teacher_subject",
                new String[]{"ID", "subject_name", "subject_time"},
                "teacher_id = ?",
                new String[]{teacherId},
                null, null, null);
    }

    public boolean insertOrUpdateImageForStudent(String studentId, byte[] imageData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("image", imageData);
        String whereClause = "ID = ?";
        String[] whereArgs = {studentId};
        int rowsAffected = db.update("student", values, whereClause, whereArgs);
        if (rowsAffected == 0) {
            values.put("ID", studentId);
            long newRowId = db.insert("student", null, values);
            return newRowId != -1;
        }
        return true;
    }
    public boolean insertOrUpdateImageForTeacher(String studentId, byte[] imageData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("image", imageData);
        String whereClause = "ID = ?";
        String[] whereArgs = {studentId};
        int rowsAffected = db.update("teacher", values, whereClause, whereArgs);
        if (rowsAffected == 0) {
            values.put("ID", studentId);
            long newRowId = db.insert("teacher", null, values);
            return newRowId != -1;
        }
        return true;
    }
    public byte[] getStudentImage(String studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("student",
                new String[]{"image"},
                "ID" + " = ?",
                new String[]{studentId},
                null, null, null);
        byte[] imageBytes = null;
        if (cursor.moveToFirst()) {
            imageBytes = cursor.getBlob(0);
        }
        cursor.close();
        return imageBytes;
    }

    public byte[] getTeacherImage(String teacher_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("teacher",
                new String[]{"image"},
                "ID" + " = ?",
                new String[]{teacher_id},
                null, null, null);
        byte[] imageBytes = null;
        if (cursor.moveToFirst()) {
            imageBytes = cursor.getBlob(0);
        }
        cursor.close();
        return imageBytes;
    }
    public boolean updateSubjectName(String subjectId, String newSubjectName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("subject_name", newSubjectName);
        return db.update("subject", values, "ID" + " = ?", new String[]{subjectId}) > 0;
    }
    public boolean updateStudentName(String id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        return db.update("student", values, id + "=?", new String[]{id}) > 0;
    }

    public boolean updateTeacherName(String id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        return db.update("teacher", values, id + "=?", new String[]{id}) > 0;
    }
    public boolean updateSubjectTime(String subjectId, String newSubjectTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("subject_time", newSubjectTime);
        return db.update("subject", values, "ID" + " = ?", new String[]{subjectId}) > 0;
    }
    public boolean updateSubjectCredit(String subjectId, String newSubjectCredit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("subject_credit", newSubjectCredit);
        return db.update("subject", values, "ID" + " = ?", new String[]{subjectId}) > 0;
    }
    public boolean deleteSubjectForStudent(String subject_id, String student_id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("student_subject", "ID" + " = ? AND student_id =?", new String[]{subject_id, student_id}) > 0;
    }
    public boolean deleteSubjectForTeacher(String subject_id, String student_id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("teacher_subject", "ID" + " = ? AND teacher_id =?", new String[]{subject_id, student_id}) > 0;
    }
    public boolean deleteSubject(String subjectId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("subject", "ID" + " = ?", new String[]{subjectId}) > 0;
    }
    public boolean deleteStudent(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("student", "ID" + " = ?", new String[]{id}) > 0;
    }
    public boolean deleteTeacher(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("teacher", "ID" + " = ?", new String[]{id}) > 0;
    }
}
