package com.example.mydb;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mydb.databinding.ActivityMainAdmenOptionBinding;

public class MainActivityAdmenOption extends AppCompatActivity {

    private ActivityMainAdmenOptionBinding binding;
    private DataBaseHelper db;
    private static final String INSERT = "Insert";
    private static final String UPDATE = "Update";
    private static final String DELETE = "Delete";
    private boolean state;
    private SharedPreferences.Editor editor;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainAdmenOptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences preferences = getSharedPreferences("AdmenData", Context.MODE_PRIVATE);
        editor = preferences.edit();
        count = preferences.getInt("count", count);
        if (count == 0){
            String name = getIntent().getStringExtra("name");
            String pass = getIntent().getStringExtra("pass");
            count ++;
            saveData(name, pass, count);
        }


        binding.textViewMode.setOnClickListener(Zaki -> {
            switch (binding.textViewMode.getText().toString()) {
                case INSERT: {
                    binding.textViewMode.setText(UPDATE);
                    binding.textViewMode.setTextColor(0xCC2196F3);
                    break;
                }
                case UPDATE: {
                    binding.textViewMode.setText(DELETE);
                    binding.textViewMode.setTextColor(0xCCE91E63);
                    break;
                }
                case DELETE: {
                    binding.textViewMode.setText(INSERT);
                    binding.textViewMode.setTextColor(0xCC4CAF50);
                    break;
                }
            }
        });

        try {
            db = new DataBaseHelper(this);
            binding.buttonAdmenActive.setOnClickListener(Zaki -> {
                switch (binding.textViewMode.getText().toString()) {
                    case INSERT: {
                        if (binding.editTextNumber2.getText().toString().equals("0")){
                            if (count == 3){
                               state = false;
                            }else {
                                String name = binding.editTextText2.getText().toString();
                                String pass = binding.editTextNumber.getText().toString();
                                count ++;
                                saveData(name, pass, count);
                                state = true;
                            }
                        }else
                        if (binding.editTextNumber.getText().toString().equals("1") && !binding.editTextText2.getText().toString().isEmpty() &&
                                !binding.editTextText3.getText().toString().isEmpty() && !binding.editTextNumber2.getText().toString().isEmpty()) {
                            state = db.addSubject(binding.editTextText2.getText().toString(), binding.editTextText3.getText().toString()
                                    , binding.editTextNumber2.getText().toString());
                        } else if (binding.editTextNumber2.getText().toString().equals("2")) {
                            if (!binding.editTextText2.getText().toString().isEmpty() && !binding.editTextNumber.getText().toString().isEmpty()) {
                                state = db.addStudent(binding.editTextText2.getText().toString(), binding.editTextNumber.getText().toString());
                            } else state = false;
                        } else if (binding.editTextNumber2.getText().toString().equals("3")) {
                            if (!binding.editTextText2.getText().toString().isEmpty() && !binding.editTextNumber.getText().toString().isEmpty()) {
                                state = db.addTeacher(binding.editTextText2.getText().toString(), binding.editTextNumber.getText().toString());
                            } else state = false;
                        } else state = false;
                        break;
                    }
                    case UPDATE: {
                        if (binding.editTextNumber2.getText().toString().equals("11")) {
                            if (!binding.editTextText2.getText().toString().isEmpty() &&
                                    !binding.editTextText3.getText().toString().isEmpty() &&
                                    !binding.editTextNumber.getText().toString().isEmpty()) {
                                state = db.updateSubjectName(binding.editTextNumber.getText().toString(),
                                        binding.editTextText3.getText().toString());
                            } else state = false;
                        } else if (binding.editTextNumber2.getText().toString().equals("12")) {
                            if (!binding.editTextText3.getText().toString().isEmpty() &&
                                    !binding.editTextNumber.getText().toString().isEmpty()) {
                                state = db.updateSubjectTime(binding.editTextNumber.getText().toString(),
                                        binding.editTextText3.getText().toString());
                            } else state = false;
                        } else if (binding.editTextNumber2.getText().toString().equals("13")) {
                            if (!binding.editTextNumber.getText().toString().isEmpty() &&
                                    !binding.editTextNumber2.getText().toString().isEmpty()) {
                                state = db.updateSubjectCredit(binding.editTextNumber.getText().toString(),
                                        binding.editTextNumber2.getText().toString());
                            } else state = false;
                        } else if (binding.editTextNumber2.getText().toString().equals("2")) {
                            if (!binding.editTextText2.getText().toString().isEmpty() &&
                                    !binding.editTextText3.getText().toString().isEmpty()) {
                                state = db.updateStudentName(binding.editTextNumber.getText().toString(),
                                        binding.editTextText3.getText().toString());
                            } else state = false;
                        } else if (binding.editTextNumber2.getText().toString().equals("3")) {
                            if (!binding.editTextText2.getText().toString().isEmpty() &&
                                    !binding.editTextText3.getText().toString().isEmpty()) {
                                state = db.updateTeacherName(binding.editTextNumber.getText().toString(),
                                        binding.editTextText3.getText().toString());
                            } else state = false;
                        }
                        break;
                    }
                    case DELETE: {
                        if (binding.editTextNumber2.getText().toString().equals("1")) {
                            if (!binding.editTextNumber.getText().toString().isEmpty()) {
                                state = db.deleteSubject(binding.editTextNumber.getText().toString());
                            } else state = false;
                        } else if (binding.editTextNumber2.getText().toString().equals("2")) {
                            if (!binding.editTextNumber.getText().toString().isEmpty()) {
                                state = db.deleteStudent(binding.editTextNumber.getText().toString());
                            } else state = false;
                        }else if (binding.editTextNumber2.getText().toString().equals("3")){
                            if (!binding.editTextNumber.getText().toString().isEmpty()) {
                                state = db.deleteTeacher(binding.editTextNumber.getText().toString());
                            } else state = false;
                        }else state = false;
                        break;
                    }
                    default:
                        break;
                }
                if (state){
                    Toast.makeText(this, "Active Successfully!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveData(String name, String pass, int count){
        editor.putString("admen"+count, name);
        editor.putString("pass"+count, pass);
        editor.putInt("count", count);
        editor.apply();
        Toast.makeText(this, "Active Successfully!", Toast.LENGTH_SHORT).show();
    }

}