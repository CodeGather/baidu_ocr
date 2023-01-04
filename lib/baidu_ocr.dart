import 'package:baidu_ocr/model/IDCardResult.dart';

export 'model/IDCardResult.dart';
export 'model/InvokeParams.dart';
export 'model/Enum.dart';

import 'baidu_ocr_platform_interface.dart';
import 'model/InvokeParams.dart';

class BaiduOcr {
  static Future<String?> getPlatformVersion() {
    return BaiduOcrPlatform.instance.getPlatformVersion();
  }

  // 初始化ak，sk
  static Future initSdk(String filePath) async {
    Map<String, String> params = {'filePath': filePath};
    return BaiduOcrPlatform.instance.initSdk(params);
  }

  // 开始拍照识别身份证
  static Future get startIdCardOcr async {
    return BaiduOcrPlatform.instance.startIdCardOcr;
  }

  // 开始拍照识别身份证
  static Future startImgOcr(String filepath, String languageType) async {
    print("OCRplugin filepath=$filepath,languageType = $languageType");
    Map<String, String> params = {
      'filePath': filepath,
      'languageType': languageType
    };
    return BaiduOcrPlatform.instance.startImgOcr(params);
  }

  // 通用文字识别
  static Future<CodeResult?> basicOcr(InvokeParams params) async {
    return await BaiduOcrPlatform.instance.basicOcr(params);
  }

  // 身份证正面
  // {address: 江西省鹰潭市余江县春涛乡东门村老东门组003号, resultCode: 1, ethnic: 汉, name: 饶鸿, gender: 男, idNumber: 36062219900710531X, birthday: 19900710, imagePath: /data/data/com.jokui.ocr.bd_ocr_plugin_example/files/pic.jpg}
  static Future<IDCardResult?> idcard(InvokeParams params) async {
    return await BaiduOcrPlatform.instance.idcard(params);
  }

  // 银行卡识别
  // {bankCardName: 中国银行, bankCardType: Debit, bankCardNumber: 621788 0800004636579, resultCode: 1}
  static Future<BankResult?> bankCard(InvokeParams params) async {
    return BaiduOcrPlatform.instance.bankCard(params);
  }
}
