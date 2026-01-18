package com.example.mydb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mydb.databinding.ActivityMainBinding;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Intent intent;
    private SharedPreferences preferences;
    private DataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        try {
            db = new DataBaseHelper(this);
            binding.button.setOnClickListener(Zaki->{
                String name = binding.editTextText.getText().toString();
                String pass = binding.editTextNumberPassword.getText().toString();
                if (name.isEmpty() || pass.isEmpty()){
                    Toast.makeText(this, "Field Cannot be Empty!", Toast.LENGTH_SHORT).show();
                }else{
                    String mode = "";
                    if (pass.charAt(0) == '9' && pass.charAt(1) == '9') {
                        Cursor cursor = db.getStudent(pass);
                        if (cursor.moveToFirst()){
                            mode = "Student";
                        }else mode = "empty";
                    }
                    else if (pass.charAt(0) == '9' && pass.charAt(1) == '8') {
                        Cursor cursor = db.getTeacher(pass);
                        if (cursor.moveToFirst()){
                            mode = "Teacher";
                        }else mode = "empty";
                    }else if (pass.charAt(0) == '0' && pass.charAt(1) == '0'){
                        preferences = getSharedPreferences("AdmenData", Context.MODE_PRIVATE);
                        int count = preferences.getInt("count", 0);
                        if (count == 0){
                            mode = "Admen";
                        }else {
                            if (binding.editTextText.getText().toString().equals(preferences.getString("admen1", "")) &&
                               binding.editTextNumberPassword.getText().toString().equals(preferences.getString("pass1", ""))){
                                mode = "Admen";
                            }else if (binding.editTextText.getText().toString().equals(preferences.getString("admen2", "")) &&
                                    binding.editTextNumberPassword.getText().toString().equals(preferences.getString("pass2", ""))){
                                mode = "Admen";
                            } else if (binding.editTextText.getText().toString().equals(preferences.getString("admen3", "")) &&
                                    binding.editTextNumberPassword.getText().toString().equals(preferences.getString("pass3", ""))) {
                                mode = "Admen";
                            }else mode = "empty";
                        }
                    }
                    if (mode.equals("Student") || mode.equals("Teacher")){
                        intent = new Intent(this, MainActivity_Users.class);
                        intent.putExtra("name", name);
                        intent.putExtra("pass", pass);
                        intent.putExtra("mode", mode);
                        startActivity(intent);
                    }else if (mode.equals("Admen")){
                        intent = new Intent(this, MainActivity_Admen.class);
                        intent.putExtra("name", name);
                        intent.putExtra("pass", pass);
                        startActivity(intent);
                    }else Toast.makeText(this, "Try Again!", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

}