// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'IDCardResult.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

IDCardResult _$IDCardResultFromJson(Map<String, dynamic> json) => IDCardResult()
  ..address = json['住址'] == null
      ? null
      : Words.fromJson(json['住址'] as Map<String, dynamic>)
  ..idNumber = json['公民身份号码'] == null
      ? null
      : Words.fromJson(json['公民身份号码'] as Map<String, dynamic>)
  ..birthday = json['出生'] == null
      ? null
      : Words.fromJson(json['出生'] as Map<String, dynamic>)
  ..name = json['姓名'] == null
      ? null
      : Words.fromJson(json['姓名'] as Map<String, dynamic>)
  ..gender = json['性别'] == null
      ? null
      : Words.fromJson(json['性别'] as Map<String, dynamic>)
  ..ethnic = json['民族'] == null
      ? null
      : Words.fromJson(json['民族'] as Map<String, dynamic>)
  ..signDate = json['签发日期'] == null
      ? null : Words.fromJson(json['签发日期'] as Map<String, dynamic>)
  ..expiryDate = json['失效日期'] == null
      ? null : Words.fromJson(json['失效日期'] as Map<String, dynamic>)
  ..issueAuthority = json['签发机关'] == null
      ? null : Words.fromJson(json['签发机关'] as Map<String, dynamic>);

Map<String, dynamic> _$IDCardResultToJson(IDCardResult instance) =>
    <String, dynamic>{
      'address': instance.address?.toJson() ?? {},
      'idNumber': instance.idNumber?.toJson() ?? {},
      'birthday': instance.birthday?.toJson() ?? {},
      'name': instance.name?.toJson() ?? {},
      'gender': instance.gender?.toJson() ?? {},
      'ethnic': instance.ethnic?.toJson() ?? {},
      'signDate': instance.signDate?.toJson() ?? {},
      'expiryDate': instance.expiryDate?.toJson() ?? {},
      'issueAuthority': instance.issueAuthority?.toJson() ?? {},
    };

CodeResult _$CodeResultFromJson(Map<String, dynamic> json) => CodeResult()
  ..logId = json['log_id'] as int?
  ..jsonRes = json['jsonRes'] as String?
  ..direction = json['direction'] as int?
  ..wordsResultNumber = json['words_result_number'] as int?
  ..wordsResult = IDCardResult.fromJson(json['words_result'] as Map<String, dynamic>)
  ..imagePath = json['imagePath'] as String?;

Map<String, dynamic> _$CodeResultToJson(CodeResult instance) =>
    <String, dynamic>{
      'logId': instance.logId,
      'jsonRes': instance.jsonRes,
      'direction': instance.direction,
      'imagePath': instance.imagePath,
      'wordsResultNumber': instance.wordsResultNumber,
      'wordsResult': instance.wordsResult?.toJson() ?? {},
    };

Words _$WordsFromJson(Map<String, dynamic> json) => Words()
  ..words = json['words'] as String?
  ..location = json['location'] == null
      ? null
      : Location.fromJson(json['location'] as Map<String, dynamic>);

Map<String, dynamic> _$WordsToJson(Words instance) =>
    <String, dynamic>{
      'words': instance.words,
      'location': instance.location,
    };

Location _$LocationFromJson(Map<String, dynamic> json) => Location()
  ..top = json['top'] as int?
  ..left = json['left'] as int?
  ..width = json['width'] as int?
  ..height = json['height'] as int?;

Map<String, dynamic> _$LocationToJson(Location instance) => <String, dynamic>{
      'top': instance.top,
      'left': instance.left,
      'width': instance.width,
      'height': instance.height,
    };


BankResult _$BankResultFromJson(Map<String, dynamic> json) => BankResult()
  ..direction = json['direction'] as int?
  ..result = json['result'] == null
      ? null
      : Result.fromJson(json['result'] as Map<String, dynamic>);

Map<String, dynamic> _$BankResultToJson(BankResult instance) => <String, dynamic>{
  'direction': instance.direction,
  'result': instance.result?.toJson() ?? {},
};

Result _$ResultFromJson(Map<String, dynamic> json) =>
    Result()
      ..bankCardType = json['bank_card_type'] as int?
      ..bankName = json['bank_name'] as String?
      ..validDate = json['valid_date'] as String?
      ..holderName = json['holder_name'] as String?
      ..bankCardNumber = json['bank_card_number'] as String?;

Map<String, dynamic> _$ResultToJson(Result instance) =>
    <String, dynamic>{
      'bankCardType': instance.bankCardType,
      'bankName': instance.bankName,
      'validDate': instance.validDate,
      'holderName': instance.holderName,
      'bankCardNumber': instance.bankCardNumber,
    };

ResponseResult _$ResponseResultFromJson(Map<String, dynamic> json) =>
    ResponseResult()
      ..logId = json['logId'] as int?
      ..jsonRes = json['jsonRes'] as String?;

Map<String, dynamic> _$ResponseResultToJson(ResponseResult instance) =>
    <String, dynamic>{
      'logId': instance.logId,
      'jsonRes': instance.jsonRes,
    };
