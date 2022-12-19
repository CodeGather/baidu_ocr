// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'IDCardResult.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

IDCardResult _$IDCardResultFromJson(Map<String, dynamic> json) => IDCardResult()
  ..logId = json['logId'] as int?
  ..jsonRes = json['jsonRes'] as String?
  ..direction = json['direction'] as int?
  ..wordsResultNumber = json['wordsResultNumber'] as int?
  ..address = json['address'] == null
      ? null
      : Word.fromJson(json['address'] as Map<String, dynamic>)
  ..idNumber = json['idNumber'] == null
      ? null
      : Word.fromJson(json['idNumber'] as Map<String, dynamic>)
  ..birthday = json['birthday'] == null
      ? null
      : Word.fromJson(json['birthday'] as Map<String, dynamic>)
  ..name = json['name'] == null
      ? null
      : Word.fromJson(json['name'] as Map<String, dynamic>)
  ..gender = json['gender'] == null
      ? null
      : Word.fromJson(json['gender'] as Map<String, dynamic>)
  ..ethnic = json['ethnic'] == null
      ? null
      : Word.fromJson(json['ethnic'] as Map<String, dynamic>)
  ..idCardSide = json['idCardSide'] as String?
  ..riskType = json['riskType'] as String?
  ..imageStatus = json['imageStatus'] as String?
  ..signDate = json['signDate'] == null
      ? null
      : Word.fromJson(json['signDate'] as Map<String, dynamic>)
  ..expiryDate = json['expiryDate'] == null
      ? null
      : Word.fromJson(json['expiryDate'] as Map<String, dynamic>)
  ..issueAuthority = json['issueAuthority'] == null
      ? null
      : Word.fromJson(json['issueAuthority'] as Map<String, dynamic>);

Map<String, dynamic> _$IDCardResultToJson(IDCardResult instance) =>
    <String, dynamic>{
      'logId': instance.logId,
      'jsonRes': instance.jsonRes,
      'direction': instance.direction,
      'wordsResultNumber': instance.wordsResultNumber,
      'address': instance.address,
      'idNumber': instance.idNumber,
      'birthday': instance.birthday,
      'name': instance.name,
      'gender': instance.gender,
      'ethnic': instance.ethnic,
      'idCardSide': instance.idCardSide,
      'riskType': instance.riskType,
      'imageStatus': instance.imageStatus,
      'signDate': instance.signDate,
      'expiryDate': instance.expiryDate,
      'issueAuthority': instance.issueAuthority,
    };

CodeResult _$CodeResultFromJson(Map<String, dynamic> json) => CodeResult()
  ..logId = json['log_id'] as int?
  ..jsonRes = json['jsonRes'] as String?
  ..direction = json['direction'] as int?
  ..wordsResultNumber = json['words_result_number'] as int?
  ..wordsResult = (json['words_result'] as List<dynamic>?)
      ?.map((e) => WordsResult.fromJson(e as Map<String, dynamic>))
      .toList();

Map<String, dynamic> _$CodeResultToJson(CodeResult instance) =>
    <String, dynamic>{
      'logId': instance.logId,
      'jsonRes': instance.jsonRes,
      'direction': instance.direction,
      'wordsResultNumber': instance.wordsResultNumber,
      'wordsResult': instance.wordsResult,
    };

WordsResult _$WordsResultFromJson(Map<String, dynamic> json) => WordsResult()
  ..vertexesLocation = (json['vertexes_location'] as List<dynamic>?)
      ?.map((e) => VertexesLocation.fromJson(e as Map<String, dynamic>))
      .toList()
  ..chars = (json['chars'] as List<dynamic>?)
      ?.map((e) => Chars.fromJson(e as Map<String, dynamic>))
      .toList()
  ..finegrainedVertexesVocation =
      (json['finegrained_vertexes_vocation'] as List<dynamic>?)
          ?.map((e) => VertexesLocation.fromJson(e as Map<String, dynamic>))
          .toList()
  ..words = json['words'] as String?
  ..location = json['location'] == null
      ? null
      : Location.fromJson(json['location'] as Map<String, dynamic>)
  ..minFinegrainedVertexesVocation =
      (json['min_finegrained_vertexes_vocation'] as List<dynamic>?)
          ?.map((e) => VertexesLocation.fromJson(e as Map<String, dynamic>))
          .toList();

Map<String, dynamic> _$WordsResultToJson(WordsResult instance) =>
    <String, dynamic>{
      'vertexesLocation': instance.vertexesLocation,
      'chars': instance.chars,
      'finegrainedVertexesVocation': instance.finegrainedVertexesVocation,
      'words': instance.words,
      'location': instance.location,
      'minFinegrainedVertexesVocation': instance.minFinegrainedVertexesVocation,
    };

Chars _$CharsFromJson(Map<String, dynamic> json) => Chars()
  ..char = json['char'] as String?
  ..location = json['location'] == null
      ? null
      : Location.fromJson(json['location'] as Map<String, dynamic>);

Map<String, dynamic> _$CharsToJson(Chars instance) => <String, dynamic>{
      'char': instance.char,
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

ResponseResult _$ResponseResultFromJson(Map<String, dynamic> json) =>
    ResponseResult()
      ..logId = json['logId'] as int?
      ..jsonRes = json['jsonRes'] as String?;

Map<String, dynamic> _$ResponseResultToJson(ResponseResult instance) =>
    <String, dynamic>{
      'logId': instance.logId,
      'jsonRes': instance.jsonRes,
    };

Word _$WordFromJson(Map<String, dynamic> json) => Word()
  ..words = json['words'] as String?
  ..vertexesLocation = (json['vertexes_location'] as List<dynamic>?)
      ?.map((e) => VertexesLocation.fromJson(e as Map<String, dynamic>))
      .toList()
  ..characterResults = (json['character_results'] as List<dynamic>?)
      ?.map((e) => Char.fromJson(e as Map<String, dynamic>))
      .toList();

Map<String, dynamic> _$WordToJson(Word instance) => <String, dynamic>{
      'words': instance.words,
      'vertexesLocation': instance.vertexesLocation,
      'characterResults': instance.characterResults,
    };

VertexesLocation _$VertexesLocationFromJson(Map<String, dynamic> json) =>
    VertexesLocation()
      ..x = json['x'] as int?
      ..y = json['y'] as int?;

Map<String, dynamic> _$VertexesLocationToJson(VertexesLocation instance) =>
    <String, dynamic>{
      'x': instance.x,
      'y': instance.y,
    };

Char _$CharFromJson(Map<String, dynamic> json) =>
    Char()..character = json['character'] as String?;

Map<String, dynamic> _$CharToJson(Char instance) => <String, dynamic>{
      'character': instance.character,
    };

WordSimple _$WordSimpleFromJson(Map<String, dynamic> json) =>
    WordSimple()..words = json['words'] as String?;

Map<String, dynamic> _$WordSimpleToJson(WordSimple instance) =>
    <String, dynamic>{
      'words': instance.words,
    };
