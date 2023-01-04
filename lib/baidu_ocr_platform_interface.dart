import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'baidu_ocr_method_channel.dart';
import 'model/IDCardResult.dart';
import 'model/InvokeParams.dart';

abstract class BaiduOcrPlatform extends PlatformInterface {
  /// Constructs a BaiduOcrPlatform.
  BaiduOcrPlatform() : super(token: _token);

  static final Object _token = Object();

  static BaiduOcrPlatform _instance = MethodChannelBaiduOcr();

  /// The default instance of [BaiduOcrPlatform] to use.
  ///
  /// Defaults to [MethodChannelBdOcrPlugin].
  static BaiduOcrPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [BaiduOcrPlatform] when
  /// they register themselves.
  static set instance(BaiduOcrPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future initSdk(Map<String, String> params) {
    throw UnimplementedError('initSdk() has not been implemented.');
  }

  Future get startIdCardOcr {
    throw UnimplementedError('startIdCardOcr() has not been implemented.');
  }

  Future startImgOcr(Map<String, String> params) {
    throw UnimplementedError('startImgOcr() has not been implemented.');
  }

  Future basicOcr(InvokeParams params) {
    throw UnimplementedError('get basicOcr has not been implemented.');
  }

  Future<IDCardResult?> idcard(InvokeParams params) {
    throw UnimplementedError('get idcardFront has not been implemented.');
  }

  Future<BankResult?> bankCard(InvokeParams params) {
    throw UnimplementedError('get bankCard has not been implemented.');
  }
}
