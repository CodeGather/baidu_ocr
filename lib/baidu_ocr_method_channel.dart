import 'dart:convert';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'baidu_ocr_platform_interface.dart';
import 'model/IDCardResult.dart';
import 'model/InvokeParams.dart';

/// An implementation of [BaiduOcrPlatform] that uses method channels.
class MethodChannelBaiduOcr extends BaiduOcrPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('baidu_ocr');

  @override
  Future<String?> getPlatformVersion() async {
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  // 初始化ak，sk
  @override
  Future initSdk(Map<String, String> params) async {
    return await methodChannel.invokeMethod('initSdk', params);
  }

  // 开始拍照识别身份证
  @override
  Future get startIdCardOcr async {
    return await methodChannel.invokeMethod('startIdCardOcr');
  }

  // 开始拍照识别身份证
  @override
  Future startImgOcr(Map<String, String> params) async {
    return await methodChannel.invokeMethod('startImgOcr', params);
  }

  // 通用文字识别
  @override
  Future<CodeResult?> basicOcr(InvokeParams params) async {
    String result =
        await methodChannel.invokeMethod('basicOcr', params.toJson());
    return CodeResult.fromJson(json.decode(result));
  }

  // 身份证正面
  // {address: 江西省鹰潭市余江县春涛乡东门村老东门组003号, resultCode: 1, ethnic: 汉, name: 饶鸿, gender: 男, idNumber: 36062219900710531X, birthday: 19900710, imagePath: /data/data/com.jokui.ocr.bd_ocr_plugin_example/files/pic.jpg}
  @override
  Future<IDCardResult?> idcard(InvokeParams params) async {
    String result =
        await methodChannel.invokeMethod('idCard', params.toJson()) ?? "{}";
    return IDCardResult.fromJson(json.decode(result));
  }

  // 银行卡识别
  // {bankCardName: 中国银行, bankCardType: Debit, bankCardNumber: 621788 0800004636579, resultCode: 1}
  @override
  Future get bankCard async {
    return await methodChannel.invokeMethod('bankCard');
  }
}
