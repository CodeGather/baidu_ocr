import 'package:flutter_test/flutter_test.dart';
import 'package:baidu_ocr/baidu_ocr.dart';
import 'package:baidu_ocr/baidu_ocr_platform_interface.dart';
import 'package:baidu_ocr/baidu_ocr_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockBaiduOcrPlatform
    with MockPlatformInterfaceMixin
    implements BaiduOcrPlatform {
  @override
  Future<String?> getPlatformVersion() => Future.value('42');

  @override
  // TODO: implement accurateBasicOcr
  Future get accurateBasicOcr => throw UnimplementedError();

  @override
  // TODO: implement accurateOcr
  Future get accurateOcr => throw UnimplementedError();

  @override
  // TODO: implement bankCard
  Future get bankCard => throw UnimplementedError();

  @override
  // TODO: implement basicOcr
  Future basicOcr(InvokeParams params) => throw UnimplementedError();

  @override
  // TODO: implement idcardFront
  Future<IDCardResult?> idcard(InvokeParams params) =>
      throw UnimplementedError();

  @override
  Future initSdk(Map<String, String> params) {
    // TODO: implement initSdk
    throw UnimplementedError();
  }

  @override
  // TODO: implement startIdCardOcr
  Future get startIdCardOcr => throw UnimplementedError();

  @override
  Future startImgOcr(Map<String, String> params) {
    // TODO: implement startImgOcr
    throw UnimplementedError();
  }
}

void main() {
  final BaiduOcrPlatform initialPlatform = BaiduOcrPlatform.instance;

  test('$MethodChannelBaiduOcr is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelBaiduOcr>());
  });

  test('getPlatformVersion', () async {
    BaiduOcr baiduOcrPlugin = BaiduOcr();
    MockBaiduOcrPlatform fakePlatform = MockBaiduOcrPlatform();
    BaiduOcrPlatform.instance = fakePlatform;

    expect(await fakePlatform.getPlatformVersion(), '42');
  });
}
