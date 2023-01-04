import 'package:json_annotation/json_annotation.dart';

part 'IDCardResult.g.dart';

@JsonSerializable()

/// 身份证返回实体
class IDCardResult extends ResponseResult {
  /// 地址
  Words? address;

  /// 身份证号码
  Words? idNumber;

  /// 出生年月
  Words? birthday;

  /// 姓名
  Words? name;

  /// 性别
  Words? gender;

  /// 名族
  Words? ethnic;

  /// 签发时间
  Words? signDate;

  /// 过期时间
  Words? expiryDate;

  /// 签发机关
  Words? issueAuthority;

  IDCardResult();
  factory IDCardResult.fromJson(Map<String, dynamic> json) =>
      _$IDCardResultFromJson(json);
  Map<String, dynamic> toJson() => _$IDCardResultToJson(this);
}

@JsonSerializable()

/// 通用识别
class CodeResult extends ResponseResult {
  int? direction;
  int? wordsResultNumber;
  IDCardResult? wordsResult;
  String? imagePath;

  CodeResult();
  factory CodeResult.fromJson(Map<String, dynamic> json) =>
      _$CodeResultFromJson(json);
  Map<String, dynamic> toJson() => _$CodeResultToJson(this);
}

@JsonSerializable()

/// 通用识别
class Words {
  String? words;
  Location? location;

  Words();
  factory Words.fromJson(Map<String, dynamic> json) => _$WordsFromJson(json);
  Map<String, dynamic> toJson() => _$WordsToJson(this);
}

@JsonSerializable()

/// 定位
class Location {
  int? top;
  int? left;
  int? width;
  int? height;

  Location();
  factory Location.fromJson(Map<String, dynamic> json) =>
      _$LocationFromJson(json);
  Map<String, dynamic> toJson() => _$LocationToJson(this);
}

@JsonSerializable()
class BankResult extends ResponseResult {
  int? direction;
  Result? result;
  BankResult();
  factory BankResult.fromJson(Map<String, dynamic> json) =>
      _$BankResultFromJson(json);
  Map<String, dynamic> toJson() => _$BankResultToJson(this);
}

@JsonSerializable()
class Result {
  int? bankCardType;
  String? bankName;
  String? validDate;
  String? holderName;
  String? bankCardNumber;

  Result();
  factory Result.fromJson(Map<String, dynamic> json) => _$ResultFromJson(json);
  Map<String, dynamic> toJson() => _$ResultToJson(this);
}

@JsonSerializable()
class ResponseResult {
  int? logId;
  String? jsonRes;

  ResponseResult();
  factory ResponseResult.fromJson(Map<String, dynamic> json) =>
      _$ResponseResultFromJson(json);
  Map<String, dynamic> toJson() => _$ResponseResultToJson(this);
}
