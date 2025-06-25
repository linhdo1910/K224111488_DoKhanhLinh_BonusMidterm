package com.example.k224111488_dokhanhlinh_practice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import models.Message;  // Import lớp Message từ package models

public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Kiểm tra nếu có tin nhắn đến
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }

                // Lấy số điện thoại người gửi và nội dung tin nhắn
                String sender = messages[0].getDisplayOriginatingAddress();
                String messageBody = messages[0].getMessageBody();

                // Gửi tin nhắn lên Firebase
                sendToFirebase(sender, messageBody);
            }
        }
    }

    private void sendToFirebase(String sender, String messageBody) {
        // Thực hiện gửi tin nhắn lên Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("messages");

        // Tạo đối tượng Message và đẩy lên Firebase
        Message message = new Message(sender, messageBody);
        myRef.push().setValue(message);
    }
}
