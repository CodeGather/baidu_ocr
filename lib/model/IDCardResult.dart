import 'package:json_annotation/json_annotation.dart';

part 'IDCardResult.g.dart';

@JsonSerializable()

/// 身份证返回实体
class IDCardResult extends ResponseResult {
  int? direction;
  int? wordsResultNumber;

  /// 地址
  Word? address;

  /// 身份证号码
  Word? idNumber;

  /// 出生年月
  Word? birthday;

  /// 姓名
  Word? name;

  /// 性别
  Word? gender;

  /// 名族
  Word? ethnic;

  /// 身份证正反面 front back
  String? idCardSide;

  /// 风险类型
  String? riskType;

  /// 类型
  String? imageStatus;

  /// 签发时间
  Word? signDate;

  /// 过期时间
  Word? expiryDate;

  /// 签发机关
  Word? issueAuthority;

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
  List<WordsResult>? wordsResult;

  CodeResult();
  factory CodeResult.fromJson(Map<String, dynamic> json) =>
      _$CodeResultFromJson(json);
  Map<String, dynamic> toJson() => _$CodeResultToJson(this);
}

@JsonSerializable()

/// 通用识别
class WordsResult {
  List<VertexesLocation>? vertexesLocation;
  List<Chars>? chars;
  List<VertexesLocation>? finegrainedVertexesVocation;
  String? words;
  Location? location;
  List<VertexesLocation>? minFinegrainedVertexesVocation;

  WordsResult();
  factory WordsResult.fromJson(Map<String, dynamic> json) =>
      _$WordsResultFromJson(json);
  Map<String, dynamic> toJson() => _$WordsResultToJson(this);
}

@JsonSerializable()

/// 单个文字
class Chars {
  String? char;
  Location? location;

  Chars();
  factory Chars.fromJson(Map<String, dynamic> json) => _$CharsFromJson(json);
  Map<String, dynamic> toJson() => _$CharsToJson(this);
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
class ResponseResult {
  int? logId;
  String? jsonRes;

  ResponseResult();
  factory ResponseResult.fromJson(Map<String, dynamic> json) =>
      _$ResponseResultFromJson(json);
  Map<String, dynamic> toJson() => _$ResponseResultToJson(this);
}

@JsonSerializable()
class Word extends WordSimple {
  List<VertexesLocation>? vertexesLocation;
  List<Char>? characterResults;

  Word();
  factory Word.fromJson(Map<String, dynamic> json) => _$WordFromJson(json);
  Map<String, dynamic> toJson() => _$WordToJson(this);
}

@JsonSerializable()
class VertexesLocation {
  int? x;
  int? y;

  VertexesLocation();
  factory VertexesLocation.fromJson(Map<String, dynamic> json) =>
      _$VertexesLocationFromJson(json);
  Map<String, dynamic> toJson() => _$VertexesLocationToJson(this);
}

@JsonSerializable()
class Char {
  String? character;

  Char();
  factory Char.fromJson(Map<String, dynamic> json) => _$CharFromJson(json);
  Map<String, dynamic> toJson() => _$CharToJson(this);
}

@JsonSerializable()
class WordSimple {
  String? words;

  WordSimple();
  factory WordSimple.fromJson(Map<String, dynamic> json) =>
      _$WordSimpleFromJson(json);
  Map<String, dynamic> toJson() => _$WordSimpleToJson(this);
}
