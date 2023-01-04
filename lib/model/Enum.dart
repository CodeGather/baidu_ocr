// ignore_for_file: constant_identifier_names

enum OcrType {
  /// 身份证识别
  REQUEST_CODE_ICARD,

  /// 通用文字识别（含位置信息）
  REQUEST_CODE_GENERAL,

  /// 通用文字识别
  REQUEST_CODE_GENERAL_BASIC,

  /// 通用文字识别（高精度版）
  REQUEST_CODE_ACCURATE_BASIC,

  /// 通用文字识别（含位置信息高精度版）
  REQUEST_CODE_ACCURATE,

  /// 通用文字识别（含生僻字版）
  REQUEST_CODE_GENERAL_ENHANCED,

  /// 网络图片文字识别
  REQUEST_CODE_GENERAL_WEBIMAGE,

  /// 银行卡
  REQUEST_CODE_BANKCARD,

  /// 行驶证识别
  REQUEST_CODE_VEHICLE_LICENSE,

  /// 驾驶证识别
  REQUEST_CODE_DRIVING_LICENSE,

  /// 车牌识别
  REQUEST_CODE_LICENSE_PLATE,

  /// 营业执照识别
  REQUEST_CODE_BUSINESS_LICENSE,

  /// 通用票据识别
  REQUEST_CODE_RECEIPT,

  /// 护照识别
  REQUEST_CODE_PASSPORT,

  /// 数字
  REQUEST_CODE_NUMBERS,

  /// 二维码识别
  REQUEST_CODE_QRCODE,

  /// 名片
  REQUEST_CODE_BUSINESSCARD,

  /// 手写
  REQUEST_CODE_HANDWRITING,

  /// 彩票
  REQUEST_CODE_LOTTERY,

  /// 增值税发票
  REQUEST_CODE_VATINVOICE,

  /// 自定义模板
  REQUEST_CODE_CUSTOM,

  /// 出租车票
  REQUEST_CODE_TAXIRECEIPT,

  /// VIN码
  REQUEST_CODE_VINCODE,

  /// 火车票
  REQUEST_CODE_TRAINTICKET,

  /// 行程单
  REQUEST_CODE_TRIP_TICKET,

  /// 机动车销售发票
  REQUEST_CODE_CAR_SELL_INVOICE,

  /// 车辆合格证
  REQUEST_CODE_VIHICLE_SERTIFICATION,

  /// 试卷分析和识别
  REQUEST_CODE_EXAMPLE_DOC_REG,

  /// 手写文字识别
  REQUEST_CODE_WRITTEN_TEXT,

  /// 户口本识别
  REQUEST_CODE_HUKOU_PAGE,

  /// 普通机打发票识别
  REQUEST_CODE_NORMAL_MACHINE_INVOICE,

  /// 磅单识别
  REQUEST_CODE_WEIGHT_NOTE,

  /// 医疗费用明细识别
  REQUEST_CODE_MEDICAL_DETAIL,

  /// 网约车行程单识别
  REQUEST_CODE_ONLINE_TAXI_ITINERARY,
}

class EnumUtils {
  static int formatOcrTypeValue(OcrType? status) {
    switch (status) {
      case OcrType.REQUEST_CODE_ICARD:
        return 104;

      /// 通用文字识别（含位置信息）
      case OcrType.REQUEST_CODE_GENERAL:
        return 105;

      /// 通用文字识别
      case OcrType.REQUEST_CODE_GENERAL_BASIC:
        return 106;

      /// 通用文字识别（高精度版）
      case OcrType.REQUEST_CODE_ACCURATE_BASIC:
        return 107;

      /// 通用文字识别（含位置信息高精度版）
      case OcrType.REQUEST_CODE_ACCURATE:
        return 108;

      /// 通用文字识别（含生僻字版）
      case OcrType.REQUEST_CODE_GENERAL_ENHANCED:
        return 109;

      /// 网络图片文字识别
      case OcrType.REQUEST_CODE_GENERAL_WEBIMAGE:
        return 110;

      /// 银行卡
      case OcrType.REQUEST_CODE_BANKCARD:
        return 111;

      /// 行驶证识别
      case OcrType.REQUEST_CODE_VEHICLE_LICENSE:
        return 120;

      /// 驾驶证识别
      case OcrType.REQUEST_CODE_DRIVING_LICENSE:
        return 121;

      /// 车牌识别
      case OcrType.REQUEST_CODE_LICENSE_PLATE:
        return 122;

      /// 营业执照识别
      case OcrType.REQUEST_CODE_BUSINESS_LICENSE:
        return 123;

      /// 通用票据识别
      case OcrType.REQUEST_CODE_RECEIPT:
        return 124;

      /// 护照识别
      case OcrType.REQUEST_CODE_PASSPORT:
        return 125;

      /// 数字
      case OcrType.REQUEST_CODE_NUMBERS:
        return 126;

      /// 二维码识别
      case OcrType.REQUEST_CODE_QRCODE:
        return 127;

      /// 名片
      case OcrType.REQUEST_CODE_BUSINESSCARD:
        return 128;

      /// 手写
      case OcrType.REQUEST_CODE_HANDWRITING:
        return 129;

      /// 彩票
      case OcrType.REQUEST_CODE_LOTTERY:
        return 130;

      /// 增值税发票
      case OcrType.REQUEST_CODE_VATINVOICE:
        return 131;

      /// 自定义模板
      case OcrType.REQUEST_CODE_CUSTOM:
        return 132;

      /// 出租车票
      case OcrType.REQUEST_CODE_TAXIRECEIPT:
        return 133;

      /// VIN码
      case OcrType.REQUEST_CODE_VINCODE:
        return 134;

      /// 火车票
      case OcrType.REQUEST_CODE_TRAINTICKET:
        return 135;

      /// 行程单
      case OcrType.REQUEST_CODE_TRIP_TICKET:
        return 136;

      /// 机动车销售发票
      case OcrType.REQUEST_CODE_CAR_SELL_INVOICE:
        return 137;

      /// 车辆合格证
      case OcrType.REQUEST_CODE_VIHICLE_SERTIFICATION:
        return 138;

      /// 试卷分析和识别
      case OcrType.REQUEST_CODE_EXAMPLE_DOC_REG:
        return 139;

      /// 手写文字识别
      case OcrType.REQUEST_CODE_WRITTEN_TEXT:
        return 140;

      /// 户口本识别
      case OcrType.REQUEST_CODE_HUKOU_PAGE:
        return 141;

      /// 普通机打发票识别
      case OcrType.REQUEST_CODE_NORMAL_MACHINE_INVOICE:
        return 142;

      /// 磅单识别
      case OcrType.REQUEST_CODE_WEIGHT_NOTE:
        return 143;

      /// 医疗费用明细识别
      case OcrType.REQUEST_CODE_MEDICAL_DETAIL:
        return 144;

      /// 网约车行程单识别
      case OcrType.REQUEST_CODE_ONLINE_TAXI_ITINERARY:
        return 145;
      default:
        return 104;
    }
  }
}
