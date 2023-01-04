import 'package:json_annotation/json_annotation.dart';

import 'Enum.dart';
part 'InvokeParams.g.dart';

/// 设置参数
@JsonSerializable()
class InvokeParams {
  /// 通用识别
  static const String contentTypeGeneral = "general";

  /// 正面
  static const String idCardSideFront = "IDCardFront";

  /// 反面
  static const String idCardSideBack = "IDCardBack";

  /// 银行卡
  static const String contentTypeBankCard = "bankCard";
  static const String contentTypePassport = "passport";
  bool? isScan;
  OcrType? type;
  String? sideType;
  InvokeParams();
  factory InvokeParams.fromJson(Map<String, dynamic> json) =>
      _$InvokeParamsFromJson(json);
  Map<String, dynamic> toJson() => _$InvokeParamsToJson(this);
}
