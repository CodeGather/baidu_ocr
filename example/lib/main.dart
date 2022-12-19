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
  OcrType? select;
  bool isScan = false;

  @override
  void initState() {
    super.initState();
    initPlatformState();

    select = OcrType.REQUEST_CODE_GENERAL;
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
            Row(
              children: [
                Checkbox(
                  value: isScan,
                  onChanged: (bool? status) {
                    setState(() {
                      isScan = status ?? false;
                      select = OcrType.REQUEST_CODE_ICARD;
                    });
                  },
                ),
                const Text("是否扫描")
              ],
            ),
            Expanded(
              child: ListView(
                shrinkWrap: true,
                children: OcrType.values.map((t) {
                  return Row(
                    children: [
                      Checkbox(
                        value: select == t,
                        onChanged:
                            isScan == true && t != OcrType.REQUEST_CODE_ICARD
                                ? null
                                : (bool? status) {
                                    setState(() {
                                      if (status == true) {
                                        select = t;
                                      }
                                    });
                                  },
                      ),
                      Text(t.name),
                    ],
                  );
                }).toList(),
              ),
            ),
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
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                ElevatedButton(
                  onPressed: () async {
                    BaiduOcr.initSdk("assets/${Platform.isAndroid ? 'aip-ocr' : 'aip-ocr-ios'}.license");
                  },
                  child: const Text('initSDK'),
                ),
                ElevatedButton(
                  onPressed: () async {
                    // REQUEST_CODE_ICARD;
                    // /// 通用文字识别（含位置信息）
                    // REQUEST_CODE_GENERAL;
                    // /// 通用文字识别
                    // REQUEST_CODE_GENERAL_BASIC;
                    // /// 通用文字识别（高精度版）
                    // REQUEST_CODE_ACCURATE_BASIC;
                    // /// 通用文字识别（含位置信息高精度版）
                    // REQUEST_CODE_ACCURATE;
                    // /// 通用文字识别（含生僻字版）
                    // REQUEST_CODE_GENERAL_ENHANCED;
                    // /// 网络图片文字识别
                    // REQUEST_CODE_GENERAL_WEBIMAGE;
                    // /// 银行卡
                    // REQUEST_CODE_BANKCARD;
                    // /// 行驶证识别
                    // REQUEST_CODE_VEHICLE_LICENSE;
                    // /// 驾驶证识别
                    // REQUEST_CODE_DRIVING_LICENSE;
                    // /// 车牌识别
                    // REQUEST_CODE_LICENSE_PLATE;
                    // /// 营业执照识别
                    // REQUEST_CODE_BUSINESS_LICENSE;
                    // /// 通用票据识别
                    // REQUEST_CODE_RECEIPT;
                    // /// 护照识别
                    // REQUEST_CODE_PASSPORT;
                    // /// 数字
                    // REQUEST_CODE_NUMBERS;
                    // /// 二维码识别
                    // REQUEST_CODE_QRCODE;
                    // /// 名片
                    // REQUEST_CODE_BUSINESSCARD;
                    // /// 手写
                    // REQUEST_CODE_HANDWRITING;
                    // /// 彩票
                    // REQUEST_CODE_LOTTERY;
                    // /// 增值税发票
                    // REQUEST_CODE_VATINVOICE;
                    // /// 自定义模板
                    // REQUEST_CODE_CUSTOM;
                    // /// 出租车票
                    // REQUEST_CODE_TAXIRECEIPT;
                    // /// VIN码
                    // REQUEST_CODE_VINCODE;
                    // /// 火车票
                    // REQUEST_CODE_TRAINTICKET;
                    // /// 行程单
                    // REQUEST_CODE_TRIP_TICKET;
                    // /// 机动车销售发票
                    // REQUEST_CODE_CAR_SELL_INVOICE;
                    // /// 车辆合格证
                    // REQUEST_CODE_VIHICLE_SERTIFICATION;
                    // /// 试卷分析和识别
                    // REQUEST_CODE_EXAMPLE_DOC_REG;
                    // /// 手写文字识别
                    // REQUEST_CODE_WRITTEN_TEXT;
                    // /// 户口本识别
                    // REQUEST_CODE_HUKOU_PAGE;
                    // /// 普通机打发票识别
                    // REQUEST_CODE_NORMAL_MACHINE_INVOICE;
                    // /// 磅单识别
                    // REQUEST_CODE_WEIGHT_NOTE;
                    // /// 医疗费用明细识别
                    // REQUEST_CODE_MEDICAL_DETAIL;
                    // /// 网约车行程单识别
                    // REQUEST_CODE_ONLINE_TAXI_ITINERARY;
                    final result =
                        await BaiduOcr.basicOcr(InvokeParams.fromJson({
                      "isScan": isScan,
                      "sideType": isScan
                          ? InvokeParams.idCardSideIDCardFront
                          : InvokeParams.contentTypeGeneral,
                      "type": select,
                    }));
                    if (kDebugMode) {
                      print(result);
                      setState(() {
                        _platformVersion = json.encode(result);
                      });
                    }
                  },
                  child: const Text('开始识别'),
                ),
              ],
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                ElevatedButton(
                  onPressed: () async {
                    IDCardResult? result = await BaiduOcr.idcard(
                        InvokeParams.fromJson({
                      "isScan": true,
                      "sideType": InvokeParams.idCardSideIDCardFront
                    }));
                    if (kDebugMode) {
                      print(result?.toJson());
                      setState(() {
                        _platformVersion = result!.toJson().toString();
                      });
                    }
                  },
                  child: const Text('身份证正面'),
                ),
                ElevatedButton(
                  onPressed: () async {
                    await BaiduOcr.bankCard;
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
