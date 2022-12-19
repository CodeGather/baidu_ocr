// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'InvokeParams.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

InvokeParams _$InvokeParamsFromJson(Map<String, dynamic> json) => InvokeParams()
  ..isScan = json['isScan'] as bool?
  ..type = json['type']
  ..sideType = json['sideType'] as String?;

Map<String, dynamic> _$InvokeParamsToJson(InvokeParams instance) =>
    <String, dynamic>{
      'isScan': instance.isScan,
      'type': EnumUtils.formatOcrTypeValue(
          instance.type ?? OcrType.REQUEST_CODE_GENERAL),
      'sideType': instance.sideType,
    };
