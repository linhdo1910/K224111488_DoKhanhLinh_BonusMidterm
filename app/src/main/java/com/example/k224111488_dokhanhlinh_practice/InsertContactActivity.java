package com.example.k224111488_dokhanhlinh_practice;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.k224111488_dokhanhlinh_practice.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InsertContactActivity extends AppCompatActivity {

    // Khai báo các EditText
    EditText edtContactId, edtName, edtPhone, edtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_contact);

        // Gọi hàm addViews() để ánh xạ các đối tượng View
        addViews();

        // Cài đặt sự kiện cho nút Insert
        Button btnInsert = findViewById(R.id.btnInsert);
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processInsertContact(v);
            }
        });
    }

    private void addViews() {
        edtContactId = findViewById(R.id.edtContactId);
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
    }

    // Hàm xử lý việc thêm liên hệ vào Firebase Realtime Database
    public void processInsertContact(View view) {
        try {
            // Lấy giá trị từ các EditText
            String contactId = edtContactId.getText().toString();
            String name = edtName.getText().toString();
            String phone = edtPhone.getText().toString();
            String email = edtEmail.getText().toString();

            // Kiểm tra nếu các trường không rỗng
            if (contactId.isEmpty() || name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Khởi tạo Firebase Realtime Database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("contacts");

            // Thêm dữ liệu vào Firebase
            myRef.child(contactId).child("phone").setValue(phone);
            myRef.child(contactId).child("email").setValue(email);
            myRef.child(contactId).child("name").setValue(name);

            // Hiển thị thông báo thành công
            Toast.makeText(this, "Contact inserted successfully!", Toast.LENGTH_LONG).show();

            // Kết thúc Activity sau khi thêm dữ liệu
            finish();
        } catch (Exception ex) {
            // Xử lý ngoại lệ nếu có lỗi
            Toast.makeText(this, "Error: " + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
