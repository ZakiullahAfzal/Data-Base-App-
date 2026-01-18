package com.example.mydb;

import android.content.Intent;
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

import com.example.mydb.databinding.ActivityMainSubjectListBinding;

public class MainActivity_Subject_List extends AppCompatActivity {

    private ActivityMainSubjectListBinding binding;
    private DataBaseHelper db;
    private String mode;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainSubjectListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        user_id = getIntent().getStringExtra("pass");
        assert user_id != null;
        if (user_id.charAt(0) == '9' && user_id.charAt(1) == '9'){
            mode = "Student";
             setRowName("Student");
        }else {
            mode = "Teacher";
            setRowName("Teacher");
        }
        try {
            db = new DataBaseHelper(this);
            binding.buttonSubject.setOnClickListener(Zaki -> {
                if (binding.editTextTextSubject.getText().toString().isEmpty()) {
                    Cursor cursor = db.getSubjects();
                    if (cursor.moveToFirst()) {
                        do {
                            String id = cursor.getString(0);
                            String name = cursor.getString(1);
                            String credit;
                            if (mode.equals("Student")){
                                credit = cursor.getString(3);
                            }else credit = cursor.getString(2);

                            setRowName(id, name, credit);
                        } while (cursor.moveToNext());
                    } else {
                        Toast.makeText(this, "No Data Found!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String name = binding.editTextTextSubject.getText().toString();
                    Cursor cursor = db.getSubjectByName(name);
                    if (cursor.moveToFirst()) {
                        String id = cursor.getString(0);
                        name = cursor.getString(1);
                        String credit = cursor.getString(2);
                        setRowName(id, name, credit);
                    }else {
                        Toast.makeText(this, "No Data Found!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            binding.textViewSubjectClose.setOnClickListener(Zaki->{
                finish();
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void setRowName(String mode){
        TextView textView = new TextView(this);
        textView.setBackgroundColor(0xCC009688);
        textView.setTextSize(25);
        textView.setTextColor(0xCCFFFFFF);
        textView.setLayoutParams(new TableRow.LayoutParams(1, TableLayout.LayoutParams.MATCH_PARENT, 1f));
        if (mode.equals("Student")){
            textView.setText("Credit");
        }else if (mode.equals("Teacher")){
            textView.setText("Time");
        }
        binding.subjectTableRow.addView(textView);
    }

    private void setRowName(String ID, String Name, String Credit) {
        TextView textView_Id = new TextView(this);
        TextView textView_Name = new TextView(this);
        TextView textView_Credit = new TextView(this);
        textView_Id.setTextSize(20);
        textView_Name.setTextSize(20);
        textView_Credit.setTextSize(20);
        textView_Id.setText(ID);
        textView_Name.setText(Name);
        textView_Credit.setText(Credit);

        textView_Name.setOnClickListener(Zaki->{
            Intent intent = new Intent(this, MainActivity_UserOption.class);
            intent.putExtra("subject_id", ID);
            intent.putExtra("User_id", user_id);
            intent.putExtra("subject_name", Name);
            intent.putExtra("mode", mode);
            startActivity(intent);
        });
        textView_Id.setLayoutParams(new TableRow.LayoutParams(1, TableLayout.LayoutParams.MATCH_PARENT, 1f));
        textView_Name.setLayoutParams(new TableRow.LayoutParams(1, TableLayout.LayoutParams.MATCH_PARENT, 1f));
        textView_Credit.setLayoutParams(new TableRow.LayoutParams(1, TableLayout.LayoutParams.MATCH_PARENT, 1f));
        TableRow tableRow = new TableRow(this);
        tableRow.addView(textView_Id);
        tableRow.addView(textView_Name);
        tableRow.addView(textView_Credit);
        binding.tableLayoutSubject.addView(tableRow);
    }
}