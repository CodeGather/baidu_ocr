package com.sean.rao.baidu_ocr.common;

public class CameraEnum {
    public static final String KEY_OUTPUT_FILE_PATH = "outputFilePath";
    public static final String KEY_CONTENT_TYPE = "contentType";
    public static final String KEY_NATIVE_TOKEN = "nativeToken";
    public static final String KEY_NATIVE_ENABLE = "nativeEnable";
    public static final String KEY_NATIVE_MANUAL = "nativeEnableManual";

    public static final String CONTENT_TYPE_GENERAL = "general";
    public static final String CONTENT_TYPE_ID_CARD_FRONT = "IDCardFront";
    public static final String CONTENT_TYPE_ID_CARD_BACK = "IDCardBack";
    public static final String CONTENT_TYPE_BANK_CARD = "bankCard";
    public static final String CONTENT_TYPE_PASSPORT = "passport";

    private static final int REQUEST_CODE_PICK_IMAGE = 100;
    private static final int PERMISSIONS_REQUEST_CAMERA = 800;
    private static final int PERMISSIONS_EXTERNAL_STORAGE = 801;

    public static final int REQUEST_CODE_ICARD = 104;
    public static final int REQUEST_CODE_GENERAL = 105; // 通用文字识别（含位置信息）
    public static final int REQUEST_CODE_GENERAL_BASIC = 106; // 通用文字识别
    public static final int REQUEST_CODE_ACCURATE_BASIC = 107; // 通用文字识别（高精度版）
    public static final int REQUEST_CODE_ACCURATE = 108; // 通用文字识别（含位置信息高精度版）
    public static final int REQUEST_CODE_GENERAL_ENHANCED = 109; // 通用文字识别（含生僻字版）
    public static final int REQUEST_CODE_GENERAL_WEBIMAGE = 110; // 网络图片文字识别
    public static final int REQUEST_CODE_BANKCARD = 111; // 银行卡
    public static final int REQUEST_CODE_VEHICLE_LICENSE = 120; // 行驶证识别
    public static final int REQUEST_CODE_DRIVING_LICENSE = 121; // 驾驶证识别
    public static final int REQUEST_CODE_LICENSE_PLATE = 122; // 车牌识别
    public static final int REQUEST_CODE_BUSINESS_LICENSE = 123; // 营业执照识别
    public static final int REQUEST_CODE_RECEIPT = 124; // 通用票据识别
    public static final int REQUEST_CODE_PASSPORT = 125;  // 护照识别
    public static final int REQUEST_CODE_NUMBERS = 126; // 数字
    public static final int REQUEST_CODE_QRCODE = 127; // 二维码识别
    public static final int REQUEST_CODE_BUSINESSCARD = 128; // 名片
    public static final int REQUEST_CODE_HANDWRITING = 129; // 手写
    public static final int REQUEST_CODE_LOTTERY = 130; // 彩票
    public static final int REQUEST_CODE_VATINVOICE = 131; // 增值税发票
    public static final int REQUEST_CODE_CUSTOM = 132; // 自定义模板
    public static final int REQUEST_CODE_TAXIRECEIPT = 133; // 出租车票
    public static final int REQUEST_CODE_VINCODE = 134; // VIN码
    public static final int REQUEST_CODE_TRAINTICKET = 135; // 火车票
    public static final int REQUEST_CODE_TRIP_TICKET = 136; // 行程单
    public static final int REQUEST_CODE_CAR_SELL_INVOICE = 137; // 机动车销售发票
    public static final int REQUEST_CODE_VIHICLE_SERTIFICATION = 138; // 车辆合格证
    public static final int REQUEST_CODE_EXAMPLE_DOC_REG = 139; // 试卷分析和识别
    public static final int REQUEST_CODE_WRITTEN_TEXT = 140; // 手写文字识别
    public static final int REQUEST_CODE_HUKOU_PAGE = 141; // 户口本识别
    public static final int REQUEST_CODE_NORMAL_MACHINE_INVOICE = 142; // 普通机打发票识别
    public static final int REQUEST_CODE_WEIGHT_NOTE = 143; //磅单识别
    public static final int REQUEST_CODE_MEDICAL_DETAIL = 144; //医疗费用明细识别
    public static final int REQUEST_CODE_ONLINE_TAXI_ITINERARY = 145; //网约车行程单识别


    public static final int REQUEST_CODE_PICK_IMAGE_FRONT = 201;
    public static final int REQUEST_CODE_PICK_IMAGE_BACK = 202;
    public static final int REQUEST_CODE_CAMERA = 102;

    public static final String REQUEST_CODE = "requestCode";
    public static final String FILE_PATH = "filePath";
}
