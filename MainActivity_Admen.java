package com.example.mydb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mydb.databinding.ActivityMainAdmenBinding;

public class MainActivity_Admen extends AppCompatActivity {

    private ActivityMainAdmenBinding binding;
    private DataBaseHelper db;
    private boolean state;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainAdmenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.textViewAdmenClose.setOnClickListener(Zaki -> finish());

        binding.buttonAdmenEdit.setOnClickListener(Zaki -> startActivity(new Intent(this, MainActivityAdmenOption.class)));
        preferences = getSharedPreferences("AdmenData", Context.MODE_PRIVATE);
        editor = preferences.edit();
        count = preferences.getInt("count", 0);
        if (count == 0){
            String name = getIntent().getStringExtra("name");
            String pass = getIntent().getStringExtra("pass");
            count ++;
            saveData(name, pass, count);
        }

        try {
            db = new DataBaseHelper(this);
            binding.buttonAdmenSearch.setOnClickListener(Zaki -> {
                String mode = binding.editTextTextAmen.getText().toString();
                switch (mode) {
                    case "1": {
                        Cursor cursor = db.getSubjects();
                        if (cursor.moveToFirst()) {
                            do {
                                String id = cursor.getString(0);
                                String name = cursor.getString(1);
                                setRows(id, name);
                            } while (cursor.moveToNext());
                            cursor.close();
                            state = true;
                        } else state = false;
                        break;
                    }
                    case "2": {
                        Cursor cursor = db.getStudents();
                        if (cursor.moveToFirst()) {
                            do {
                                String id = cursor.getString(0);
                                String name = cursor.getString(1);
                                setRows(id, name);
                            } while (cursor.moveToNext());
                            cursor.close();
                            state = true;
                        } else state = false;
                        break;
                    }
                    case "3": {
                        Cursor cursor = db.getTeachers();
                        if (cursor.moveToFirst()) {
                            do {
                                String id = cursor.getString(0);
                                String name = cursor.getString(1);
                                setRows(id, name);
                            } while (cursor.moveToNext());
                            cursor.close();
                            state = true;
                        } else state = false;
                        break;
                    }
                    default:{
                        if (!mode.isEmpty()){
                            if (mode.charAt(0) == '9' && mode.charAt(1) == '9'){
                                Cursor cursor = db.getStudent(mode);
                                if (cursor.moveToFirst()){
                                    String name = cursor.getString(1);
                                    setRows(mode, name);
                                }else state = false;
                            }else if (mode.charAt(0) == '9' && mode.charAt(1) == '8'){
                                Cursor cursor = db.getTeacher(mode);
                                if (cursor.moveToFirst()){
                                    String name = cursor.getString(1);
                                    setRows(mode, name);
                                }else state = false;
                            }else {
                                Cursor cursor = db.getSubject(mode);
                                if (cursor.moveToFirst()){
                                    String name = cursor.getString(1);
                                    setRows(mode, name);
                                }else state = false;
                            }
                        }else state = false;
                        break;
                    }

                }
                if (!state){
                    Toast.makeText(this, "No Data Found!", Toast.LENGTH_SHORT).show();
                }

            });
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void setRows(String id, String name) {
        TextView textView_Id = new TextView(this);
        TextView textView_Name = new TextView(this);
        textView_Id.setTextSize(20);
        textView_Name.setTextSize(20);
        textView_Id.setTextColor(0XCCFFFFFF);
        textView_Name.setTextColor(0XCCFFFFFF);
        textView_Id.setText(id);
        textView_Name.setText(name);
        textView_Id.setLayoutParams(new TableRow.LayoutParams(1, TableLayout.LayoutParams.MATCH_PARENT, 1f));
        textView_Name.setLayoutParams(new TableRow.LayoutParams(1, TableLayout.LayoutParams.MATCH_PARENT, 1f));
        TableRow tableRow = new TableRow(this);
        tableRow.addView(textView_Id);
        tableRow.addView(textView_Name);
        binding.tableLayoutAdmen.addView(tableRow);
    }

    private void saveData(String name, String pass, int count){
        editor.putString("admen"+count, name);
        editor.putString("pass"+count, pass);
        editor.putInt("count", count);
        editor.apply();
        Toast.makeText(this, "Active Successfully!", Toast.LENGTH_SHORT).show();
    }
}