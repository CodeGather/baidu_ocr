///*
// * Copyright (C) 2021 Baidu, Inc. All Rights Reserved.
// */
//package com.baidu.ocr.demo;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.baidu.ocr.ui.camera.CameraActivity;
//import com.baidu.ocr.ui.util.FileUtil;
//import com.sean.rao.baidu_ocr.R;
//import com.sean.rao.baidu_ocr.RecognizeService;
//
//public class BankCardActivity extends AppCompatActivity {
//
//    private static final int REQUEST_CODE_BANKCARD = 111;
//    private AlertDialog.Builder alertDialog;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_bank_card);
//        alertDialog = new AlertDialog.Builder(this);
//
//        // 银行卡识别
//        findViewById(R.id.bank_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(BankCardActivity.this, CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                        CameraActivity.CONTENT_TYPE_BANK_CARD);
//                startActivityForResult(intent, REQUEST_CODE_BANKCARD);
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        // 识别成功回调，银行卡识别
//        if (requestCode == REQUEST_CODE_BANKCARD && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recBankCard(this, FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//    }
//
//    private void alertText(final String title, final String message) {
//        this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                alertDialog.setTitle(title)
//                        .setMessage(message)
//                        .setPositiveButton("确定", null)
//                        .show();
//            }
//        });
//    }
//
//    private void infoPopText(final String result) {
//        alertText("", result);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (alertDialog != null){
//            alertDialog = null;
//        }
//    }
//}