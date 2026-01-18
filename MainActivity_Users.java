package com.example.mydb;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mydb.databinding.ActivityMainUsersBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity_Users extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String SAVE = "Save";
    private static final String EDIT = "Edit";
    private static final String CREDIT = "Credit";
    private static final String TIME = "Time";
    private ActivityMainUsersBinding binding;
    private DataBaseHelper db;
    private String pass;
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.imageView.setOnClickListener(Zaki -> openImagePicker());


        String name = getIntent().getStringExtra("name");
        pass = getIntent().getStringExtra("pass");
        mode = getIntent().getStringExtra("mode");

        binding.textViewProfile.setText(String.format("Name: %s\nID: %s", name, pass));
        assert mode != null;
        setRowName(mode);
        binding.buttonEdit.setOnClickListener(Zaki -> {
            if (binding.buttonEdit.getText().toString().equals(SAVE)) {
                Toast.makeText(this, "Image Update", Toast.LENGTH_SHORT).show();
                binding.buttonEdit.setText(EDIT);
            } else {
                Intent intent = new Intent(this, MainActivity_Subject_List.class);
                intent.putExtra("pass", pass);
                startActivity(intent);
            }
        });

        try {
            db = new DataBaseHelper(this);
            if (mode.equals("Student")){
                byte[]image = db.getStudentImage(pass);
                if (image != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                    binding.imageView.setImageBitmap(bitmap);
                }

            }else{
                byte[]image = db.getTeacherImage(pass);
                if (image != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                    binding.imageView.setImageBitmap(bitmap);
                }
            }
            Cursor cursor;
            if (mode.equals("Student"))
                cursor = db.getSubjectForStudent(pass);
            else cursor = db.getSubjectForTeacher(pass);
            if (cursor.moveToFirst()) {
                do {
                    String id_ = cursor.getString(0);
                    String name_ = cursor.getString(1);
                    String credit = cursor.getString(2);
                    setRowName(id_, name_, credit);
                } while (cursor.moveToNext());
            } else {
                Toast.makeText(this, "No Data Found!", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    private void setRowName(String mode) {
        TextView textView = new TextView(this);
        textView.setBackgroundColor(0xCC009688);
        textView.setTextSize(25);
        textView.setTextColor(0xCCFFFFFF);
        textView.setLayoutParams(new TableRow.LayoutParams(1, TableLayout.LayoutParams.MATCH_PARENT, 1f));
        if (mode.equals("Student")) {
            textView.setText(CREDIT);
        } else if (mode.equals("Teacher")) {
            textView.setText(TIME);
        }
        binding.tableRow.addView(textView);
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                byte[] imageBytes = getBytes(bitmap);
                boolean state;
                if (mode.equals("Student"))
                    state = db.insertOrUpdateImageForStudent(pass, imageBytes);
                else state = db.insertOrUpdateImageForTeacher(pass, imageBytes);
                if (state) {
                    binding.imageView.setImageBitmap(bitmap);
                    Toast.makeText(this, "Image Change Successfully!", Toast.LENGTH_SHORT).show();
                } else Toast.makeText(this, "Image insert Failed!", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
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
        textView_Id.setLayoutParams(new TableRow.LayoutParams(1, TableLayout.LayoutParams.MATCH_PARENT, 1f));
        textView_Name.setLayoutParams(new TableRow.LayoutParams(1, TableLayout.LayoutParams.MATCH_PARENT, 1f));
        textView_Credit.setLayoutParams(new TableRow.LayoutParams(1, TableLayout.LayoutParams.MATCH_PARENT, 1f));
        TableRow tableRow = new TableRow(this);
        tableRow.addView(textView_Id);
        tableRow.addView(textView_Name);
        tableRow.addView(textView_Credit);
        binding.tableLayout.addView(tableRow);
    }

}