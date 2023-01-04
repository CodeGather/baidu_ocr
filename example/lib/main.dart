import 'dart:async';
import 'dart:convert';
import 'dart:io';
import 'package:baidu_ocr/baidu_ocr.dart';
import 'package:baidu_ocr/model/Enum.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() => runApp(const MyApp());

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    initPlatformState();
    // BaiduOcr.imgOcrListen( type: false, onEvent: _onEvent, onError: _onError);
  }

  void _onEvent(event) {
    // {result: {bank_card_type: 1, bank_name: 中国银行, valid_date: 08/26, bank_card_number: 621788 0800004636579}, log_id: 1597677334438540723}
    if (kDebugMode) {
      print(
        "------------------------------------------------------------------------------------------$event");
    }
  }

  void _onError(error) {
    if (kDebugMode) {
      print(
        "==========================================================================================$error");
    }
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String? platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await BaiduOcr.getPlatformVersion();
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion ?? '';
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          children: <Widget>[
            Container(
              padding: const EdgeInsets.all(10),
              color: Colors.grey.withOpacity(0.5),
              constraints: const BoxConstraints(maxHeight: 200),
              child: ListView(
                children: [
                  Text(_platformVersion),
                ],
              ),
            ),
            Wrap(
              children: [
                ElevatedButton(
                  onPressed: () async {
                    final result = await BaiduOcr.initSdk("assets/${Platform.isAndroid ? 'aip-ocr' : 'aip-ocr-ios'}.license");
                    if (kDebugMode) {
                      print(result);
                    }
                  },
                  child: const Text('initSDK'),
                ),
                ElevatedButton(
                  onPressed: () async {
                    CodeResult? result =
                    await BaiduOcr.basicOcr(InvokeParams.fromJson({
                      "isScan": true,
                      "sideType": InvokeParams.idCardSideFront,
                      "type": OcrType.REQUEST_CODE_ICARD,
                    }));
                    if (kDebugMode) {
                      print(result);
                      setState(() {
                        _platformVersion = json.encode(result);
                      });
                    }
                  },
                  child: const Text('扫描身份证正面'),
                ),
                ElevatedButton(
                  onPressed: () async {
                    final result =
                    await BaiduOcr.basicOcr(InvokeParams.fromJson({
                      "isScan": true,
                      "sideType": InvokeParams.idCardSideBack,
                      "type": OcrType.REQUEST_CODE_ICARD,
                    }));
                    if (kDebugMode) {
                      print(result);
                      setState(() {
                        _platformVersion = json.encode(result);
                      });
                    }
                  },
                  child: const Text('扫描身份证反面'),
                ),
                ElevatedButton(
                  onPressed: () async {
                    final result =
                    await BaiduOcr.basicOcr(InvokeParams.fromJson({
                      "isScan": false,
                      "sideType": InvokeParams.idCardSideBack,
                      "type": OcrType.REQUEST_CODE_ICARD,
                    }));
                    if (kDebugMode) {
                      print(result);
                      setState(() {
                        _platformVersion = json.encode(result);
                      });
                    }
                  },
                  child: const Text('身份证反面识别'),
                ),
                ElevatedButton(
                  onPressed: () async {
                    final result =
                    await BaiduOcr.basicOcr(InvokeParams.fromJson({
                      "isScan": false,
                      "sideType": InvokeParams.idCardSideFront,
                      "type": OcrType.REQUEST_CODE_ICARD,
                    }));
                    if (kDebugMode) {
                      print(result);
                      setState(() {
                        _platformVersion = json.encode(result);
                      });
                    }
                  },
                  child: const Text('身份证正面识别'),
                ),
                ElevatedButton(
                  onPressed: () async {
                    final result =
                    await BaiduOcr.bankCard(InvokeParams.fromJson({
                      "isScan": false,
                      "sideType": InvokeParams.contentTypeBankCard,
                      "type": OcrType.REQUEST_CODE_BANKCARD,
                    }));
                    if (kDebugMode) {
                      print(result);
                      setState(() {
                        _platformVersion = json.encode(result);
                      });
                    }
                  },
                  child: const Text('银行卡识别'),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
