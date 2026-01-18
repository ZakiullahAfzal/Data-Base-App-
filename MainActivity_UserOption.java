package com.example.mydb;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mydb.databinding.ActivityMainUserOptionBinding;

public class MainActivity_UserOption extends AppCompatActivity {

    private ActivityMainUserOptionBinding binding;
    private DataBaseHelper db;
    private static final String INSERT = "Insert";
    private static final String UPDATE = "Update";
    private static final String DELETE = "Delete";
    private boolean state;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainUserOptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String subject_id = getIntent().getStringExtra("subject_id");
        String subject_name = getIntent().getStringExtra("subject_name");
        String User_id = getIntent().getStringExtra("User_id");
        String mode = getIntent().getStringExtra("mode");
        Toast.makeText(this, User_id+" "+mode, Toast.LENGTH_LONG).show();
        binding.editTextTextUserOption1.setText(subject_name);
        binding.editTextNumberUserOption1.setText(subject_id);

        binding.textViewModeUserOption.setOnClickListener(Zaki -> {
            switch (binding.textViewModeUserOption.getText().toString()) {
                case INSERT: {
                    binding.textViewModeUserOption.setText(UPDATE);
                    binding.textViewModeUserOption.setTextColor(0xCC2196F3);
                    break;
                }
                case UPDATE: {
                    binding.textViewModeUserOption.setText(DELETE);
                    binding.textViewModeUserOption.setTextColor(0xCCE91E63);
                    break;
                }
                case DELETE: {
                    binding.textViewModeUserOption.setText(INSERT);
                    binding.textViewModeUserOption.setTextColor(0xCC4CAF50);
                    break;
                }
            }
        });


       try {
           db = new DataBaseHelper(this);
           binding.buttonUserOptionActive.setOnClickListener(Zaki -> {
               switch (binding.textViewModeUserOption.getText().toString()){
                   case INSERT:{
                       assert mode != null;
                       if (mode.equals("Student")) {
                           if (db.getSpecificSubjectIdForStudent(subject_id, User_id)) {
                               state = false;
                           } else {
                               state = db.addSubjectForSpecificStudent(User_id, subject_id);
                           }
                       }else {
                           if (db.getSpecificSubjectIdForTeacher(subject_id, User_id)){
                               state = false;
                           }else{
                               state = db.addSubjectForSpecificTeacher(User_id, subject_id);
                           }
                       }
                       break;
                   }
                   case UPDATE:{
                       assert mode != null;
                       if (mode.equals("Student")){
                           if (db.getSpecificSubjectIdForStudent(binding.editTextNumberUserOption2.getText().toString(), User_id)){
                               state = false;
                           }else{
                               state = db.deleteSubjectForStudent(binding.editTextNumberUserOption1.getText().toString(), User_id);
                               if (state){
                                   state = db.addSubjectForSpecificStudent(User_id, binding.editTextNumberUserOption2.getText().toString());
                               }
                           }
                       }else{
                           if (db.getSpecificSubjectIdForTeacher(binding.editTextTextUserOption2.getText().toString(), User_id)){
                               state = false;
                           }else {
                               state = db.deleteSubjectForTeacher(binding.editTextNumberUserOption1.getText().toString(), User_id);
                               if (state){
                                  state = db.addSubjectForSpecificTeacher(User_id, binding.editTextNumberUserOption2.getText().toString());
                               }
                           }
                       }
                       break;
                   }

                   case DELETE:{
                       assert mode != null;
                       if (mode.equals("Student")){
                           state = db.deleteSubjectForStudent(binding.editTextNumberUserOption1.getText().toString(), User_id);
                       }else {
                           state = db.deleteSubjectForTeacher(binding.editTextNumberUserOption1.getText().toString(), User_id);
                       }
                   }

               }
               if (state){
                   Toast.makeText(this, "Active Successfully!", Toast.LENGTH_SHORT).show();
               }else {
                   Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
               }
           });
       }catch (Exception e){
           Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
       }
    }
}