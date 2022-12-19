///*
// * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
// */
//package com.baidu.ocr.demo;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.baidu.ocr.sdk.OCR;
//import com.baidu.ocr.sdk.OnResultListener;
//import com.baidu.ocr.sdk.exception.OCRError;
//import com.baidu.ocr.sdk.model.AccessToken;
//import com.baidu.ocr.ui.camera.CameraActivity;
//import com.baidu.ocr.ui.util.FileUtil;
//import com.sean.rao.baidu_ocr.R;
//import com.sean.rao.baidu_ocr.RecognizeService;
//
//public class MainActivity extends AppCompatActivity {
//
//    private static final int REQUEST_CODE_GENERAL = 105;
//    private static final int REQUEST_CODE_GENERAL_BASIC = 106;
//    private static final int REQUEST_CODE_ACCURATE_BASIC = 107;
//    private static final int REQUEST_CODE_ACCURATE = 108;
//    private static final int REQUEST_CODE_GENERAL_ENHANCED = 109;
//    private static final int REQUEST_CODE_GENERAL_WEBIMAGE = 110;
//    private static final int REQUEST_CODE_VEHICLE_LICENSE = 120;
//    private static final int REQUEST_CODE_DRIVING_LICENSE = 121;
//    private static final int REQUEST_CODE_LICENSE_PLATE = 122;
//    private static final int REQUEST_CODE_BUSINESS_LICENSE = 123;
//    private static final int REQUEST_CODE_RECEIPT = 124;
//
//    private static final int REQUEST_CODE_PASSPORT = 125;  // 护照识别
//    private static final int REQUEST_CODE_NUMBERS = 126;
//    private static final int REQUEST_CODE_QRCODE = 127; // 二维码识别
//    private static final int REQUEST_CODE_BUSINESSCARD = 128;
//    private static final int REQUEST_CODE_HANDWRITING = 129;
//    private static final int REQUEST_CODE_LOTTERY = 130;
//    private static final int REQUEST_CODE_VATINVOICE = 131;
//    private static final int REQUEST_CODE_CUSTOM = 132;
//    private static final int REQUEST_CODE_TAXIRECEIPT = 133;
//    private static final int REQUEST_CODE_VINCODE = 134;
//    private static final int REQUEST_CODE_TRAINTICKET = 135;
//    private static final int REQUEST_CODE_TRIP_TICKET = 136; // 行程单
//    private static final int REQUEST_CODE_CAR_SELL_INVOICE = 137; // 机动车销售发票
//    private static final int REQUEST_CODE_VIHICLE_SERTIFICATION = 138; // 车辆合格证
//    private static final int REQUEST_CODE_EXAMPLE_DOC_REG = 139; // 试卷分析和识别
//    private static final int REQUEST_CODE_WRITTEN_TEXT = 140; // 手写文字识别
//    private static final int REQUEST_CODE_HUKOU_PAGE = 141; // 户口本识别
//    private static final int REQUEST_CODE_NORMAL_MACHINE_INVOICE = 142; // 普通机打发票识别
//    private static final int REQUEST_CODE_WEIGHT_NOTE = 143;//磅单识别
//    private static final int REQUEST_CODE_MEDICAL_DETAIL = 144;//医疗费用明细识别
//    private static final int REQUEST_CODE_ONLINE_TAXI_ITINERARY = 145;//网约车行程单识别
//
//
//    private boolean hasGotToken = false;
//
//    private AlertDialog.Builder alertDialog;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        alertDialog = new AlertDialog.Builder(this);
//
//        // 通用文字识别
//        findViewById(R.id.general_basic_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                        CameraActivity.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_GENERAL_BASIC);
//            }
//        });
//
//        // 通用文字识别(高精度版)
//        findViewById(R.id.accurate_basic_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                        CameraActivity.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_ACCURATE_BASIC);
//            }
//        });
//
//        // 通用文字识别（含位置信息版）
//        findViewById(R.id.general_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                        CameraActivity.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_GENERAL);
//            }
//        });
//
//        // 通用文字识别（含位置信息高精度版）
//        findViewById(R.id.accurate_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                        CameraActivity.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_ACCURATE);
//            }
//        });
//
//        // 通用文字识别（含生僻字版）
//        findViewById(R.id.general_enhance_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                        CameraActivity.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_GENERAL_ENHANCED);
//            }
//        });
//
//        // 网络图片识别
//        findViewById(R.id.general_webimage_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                        CameraActivity.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_GENERAL_WEBIMAGE);
//            }
//        });
//
//        // 身份证识别
//        findViewById(R.id.idcard_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, IDCardActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        // 银行卡识别
//        findViewById(R.id.bankcard_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, BankCardActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        // 行驶证识别
//        findViewById(R.id.vehicle_license_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                        CameraActivity.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_VEHICLE_LICENSE);
//            }
//        });
//
//        // 驾驶证识别
//        findViewById(R.id.driving_license_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                        CameraActivity.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_DRIVING_LICENSE);
//            }
//        });
//
//        // 车牌识别
//        findViewById(R.id.license_plate_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                        CameraActivity.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_LICENSE_PLATE);
//            }
//        });
//
//        // 营业执照识别
//        findViewById(R.id.business_license_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                        CameraActivity.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_BUSINESS_LICENSE);
//            }
//        });
//
//        // 通用票据识别
//        findViewById(R.id.receipt_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                        CameraActivity.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_RECEIPT);
//            }
//        });
//
//        // 增值税发票识别
//        findViewById(R.id.value_added_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                        CameraActivity.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_VATINVOICE);
//            }
//        });
//
//        // 出租车票
//        findViewById(R.id.taxi_coupon_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                        CameraActivity.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_TAXIRECEIPT);
//            }
//        });
//
//        // VIN码
//        findViewById(R.id.vin_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                        CameraActivity.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_VINCODE);
//            }
//        });
//
//        // 火车票
//        findViewById(R.id.train_ticket_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                        CameraActivity.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_TRAINTICKET);
//            }
//        });
//
//        // 数字识别
//        findViewById(R.id.number_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                        CameraActivity.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_NUMBERS);
//            }
//        });
//        // 二维码识别
//        findViewById(R.id.qrcode_recognize_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                        CameraActivity.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_QRCODE);
//            }
//        });
//
//        // 行程单识别
//        findViewById(R.id.trip_ticket_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                        CameraActivity.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_TRIP_TICKET);
//            }
//        });
//
//        // 机动车销售发票识别
//        findViewById(R.id.car_sell_ticket_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                        CameraActivity.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_CAR_SELL_INVOICE);
//            }
//        });
//
//        // 车辆合格证
//        findViewById(R.id.vehicle_certificate_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                        CameraActivity.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_VIHICLE_SERTIFICATION);
//            }
//        });
//
//        // 试卷分析和识别
//        findViewById(R.id.example_recognize_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                        CameraActivity.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_EXAMPLE_DOC_REG);
//            }
//        });
//
//        // 手写文字识别
//        findViewById(R.id.written_text_recognize_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                        CameraActivity.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_WRITTEN_TEXT);
//            }
//        });
//
//        // 护照识别
//        findViewById(R.id.pass_port_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                        CameraActivity.CONTENT_TYPE_PASSPORT);
//                startActivityForResult(intent, REQUEST_CODE_PASSPORT);
//            }
//        });
//
//        // 户口本识别
//        findViewById(R.id.hukou_recognize_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                        CameraActivity.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_HUKOU_PAGE);
//            }
//        });
//
//        // 普通机打发票识别
//        findViewById(R.id.common_machine_invoice_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                        CameraActivity.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_NORMAL_MACHINE_INVOICE);
//            }
//        });
//
//        // 自定义模板
//        findViewById(R.id.custom_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                        CameraActivity.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_CUSTOM);
//            }
//        });
//
//        // 磅单识别
//        findViewById(R.id.weight_note_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                        CameraActivity.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_WEIGHT_NOTE);
//            }
//        });
//
//        // 医疗费用明细识别
//        findViewById(R.id.medical_detail_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                        CameraActivity.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_MEDICAL_DETAIL);
//            }
//        });
//
//        // 网约车行程单识别
//        findViewById(R.id.online_taxi_itinerary_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                        CameraActivity.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_ONLINE_TAXI_ITINERARY);
//            }
//        });
//
//        // 请选择您的初始化方式
//        initAccessToken();
//    }
//
//    private boolean checkTokenStatus() {
//        if (!hasGotToken) {
//            Toast.makeText(getApplicationContext(), "token还未成功获取", Toast.LENGTH_LONG).show();
//        }
//        return hasGotToken;
//    }
//
//    /**
//     * 以license文件方式初始化
//     */
//    private void initAccessToken() {
//        OCR.getInstance(getApplicationContext()).initAccessToken(new OnResultListener<AccessToken>() {
//            @Override
//            public void onResult(AccessToken accessToken) {
//                String token = accessToken.getAccessToken();
//                hasGotToken = true;
//            }
//
//            @Override
//            public void onError(OCRError error) {
//                error.printStackTrace();
//                alertText("licence方式获取token失败", error.getMessage());
//            }
//        }, getApplicationContext());
//    }
//
//    /**
//     * 自定义license的文件路径和文件名称，以license文件方式初始化
//     */
//    private void initAccessTokenLicenseFile() {
//        OCR.getInstance(getApplicationContext()).initAccessToken(new OnResultListener<AccessToken>() {
//            @Override
//            public void onResult(AccessToken accessToken) {
//                String token = accessToken.getAccessToken();
//                hasGotToken = true;
//            }
//
//            @Override
//            public void onError(OCRError error) {
//                error.printStackTrace();
//                alertText("自定义文件路径licence方式获取token失败", error.getMessage());
//            }
//        }, "aip.license", getApplicationContext());
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
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            initAccessToken();
//        } else {
//            Toast.makeText(getApplicationContext(), "需要android.permission.READ_PHONE_STATE", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // 识别成功回调，通用文字识别（含位置信息）
//        if (requestCode == REQUEST_CODE_GENERAL && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recGeneral(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，通用文字识别（含位置信息高精度版）
//        if (requestCode == REQUEST_CODE_ACCURATE && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recAccurate(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，通用文字识别
//        if (requestCode == REQUEST_CODE_GENERAL_BASIC && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recGeneralBasic(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，通用文字识别（高精度版）
//        if (requestCode == REQUEST_CODE_ACCURATE_BASIC && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recAccurateBasic(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，通用文字识别（含生僻字版）
//        if (requestCode == REQUEST_CODE_GENERAL_ENHANCED && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recGeneralEnhanced(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，网络图片文字识别
//        if (requestCode == REQUEST_CODE_GENERAL_WEBIMAGE && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recWebimage(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，行驶证识别
//        if (requestCode == REQUEST_CODE_VEHICLE_LICENSE && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recVehicleLicense(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，驾驶证识别
//        if (requestCode == REQUEST_CODE_DRIVING_LICENSE && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recDrivingLicense(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，车牌识别
//        if (requestCode == REQUEST_CODE_LICENSE_PLATE && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recLicensePlate(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，营业执照识别
//        if (requestCode == REQUEST_CODE_BUSINESS_LICENSE && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recBusinessLicense(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                new RecognizeService.ServiceListener() {
//                    @Override
//                    public void onResult(String result) {
//                        infoPopText(result);
//                    }
//                });
//        }
//
//        // 识别成功回调，通用票据识别
//        if (requestCode == REQUEST_CODE_RECEIPT && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recReceipt(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                new RecognizeService.ServiceListener() {
//                    @Override
//                    public void onResult(String result) {
//                        infoPopText(result);
//                    }
//                });
//        }
//
//        // 识别成功回调，出租车票
//        if (requestCode == REQUEST_CODE_TAXIRECEIPT && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recTaxireceipt(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，VIN码
//        if (requestCode == REQUEST_CODE_VINCODE && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recVincode(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，火车票
//        if (requestCode == REQUEST_CODE_TRAINTICKET && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recTrainticket(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，护照
//        if (requestCode == REQUEST_CODE_PASSPORT && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recPassport(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                new RecognizeService.ServiceListener() {
//                    @Override
//                    public void onResult(String result) {
//                        infoPopText(result);
//                    }
//                });
//        }
//
//        // 识别成功回调，二维码
//        if (requestCode == REQUEST_CODE_QRCODE && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recQrcode(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，彩票
//        if (requestCode == REQUEST_CODE_LOTTERY && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recLottery(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，增值税发票
//        if (requestCode == REQUEST_CODE_VATINVOICE && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recVatInvoice(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，数字
//        if (requestCode == REQUEST_CODE_NUMBERS && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recNumbers(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，手写
//        if (requestCode == REQUEST_CODE_HANDWRITING && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recHandwriting(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，名片
//        if (requestCode == REQUEST_CODE_BUSINESSCARD && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recBusinessCard(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，自定义模板
//        if (requestCode == REQUEST_CODE_CUSTOM && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recCustom(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，行程单识别
//        if (requestCode == REQUEST_CODE_TRIP_TICKET  && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recTripTicket(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，机动车销售发票
//        if (requestCode == REQUEST_CODE_CAR_SELL_INVOICE  && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recCarSellInvoice(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，车辆合格证
//        if (requestCode == REQUEST_CODE_VIHICLE_SERTIFICATION  && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recVihicleCertification(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，试卷分析和识别
//        if (requestCode == REQUEST_CODE_EXAMPLE_DOC_REG  && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recExampleDoc(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，手写文字识别
//        if (requestCode == REQUEST_CODE_WRITTEN_TEXT  && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recWrittenText(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，户口本识别
//        if (requestCode == REQUEST_CODE_HUKOU_PAGE  && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recHuKouPage(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，通用机打发票识别
//        if (requestCode == REQUEST_CODE_NORMAL_MACHINE_INVOICE  && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recNormalMachineInvoice(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，磅单识别
//        if (requestCode == REQUEST_CODE_WEIGHT_NOTE  && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recweightnote(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，医疗费用明细识别
//        if (requestCode == REQUEST_CODE_MEDICAL_DETAIL  && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recmedicaldetail(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，网约车行程单识别
//        if (requestCode == REQUEST_CODE_ONLINE_TAXI_ITINERARY  && resultCode == Activity.RESULT_OK) {
//            RecognizeService.reconlinetaxiitinerary(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        // 释放内存资源
//        OCR.getInstance(getApplicationContext()).release();
//        if (alertDialog != null){
//            alertDialog = null;
//        }
//    }
//}
