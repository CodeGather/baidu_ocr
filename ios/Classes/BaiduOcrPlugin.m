#import "BaiduOcrPlugin.h"
#import <UIKit/UIKit.h>
#import <objc/runtime.h>
#import <AipOcrSdk/AipOcrSdk.h>

@interface BaiduOcrPlugin()<FlutterStreamHandler>
@end

@implementation BaiduOcrPlugin {
    // 默认的识别成功的回调
    void (^_successHandler)(id);
    // 默认的识别失败的回调
    void (^_failHandler)(NSError *);
    
    FlutterEventSink eventSink;
    UIViewController *flutterViewController;
    NSString *currentmethod;
    NSString *imagePath;
    FlutterResult eventResult;
}

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"baidu_ocr"
            binaryMessenger:[registrar messenger]];
    
  FlutterEventChannel* eventChannel = [FlutterEventChannel
      eventChannelWithName:@"baidu_ocr/event"
            binaryMessenger: [registrar messenger]];
    
    BaiduOcrPlugin* instance = [[BaiduOcrPlugin alloc] init];

  [eventChannel setStreamHandler: instance];
  [registrar addMethodCallDelegate:instance channel:channel];
    
  // 获取视图
  [instance getRootViewController];
  //处理回调方法
  [instance configCallback];
  //为了让手机安装demo弹出使用网络权限弹出框
  [instance httpAuthority];
}

#pragma mark - IOS 主动发送通知s让 flutter调用监听 eventChannel start
- (FlutterError*)onListenWithArguments:(id)arguments eventSink:(FlutterEventSink)eventSinks {
  eventSink = eventSinks;
  return nil;
}

- (FlutterError*)onCancelWithArguments:(id)arguments {
  [[NSNotificationCenter defaultCenter] removeObserver:self];
  eventSink = nil;
  return nil;
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
  currentmethod = call.method;
  eventResult = result;
  if ([@"getPlatformVersion" isEqualToString:call.method]) {
    result([@"iOS " stringByAppendingString:[[UIDevice currentDevice] systemVersion]]);
  }
    // SDK 初始化
  else if ([@"initSdk" isEqualToString:call.method]) {
      NSDictionary *dic = call.arguments;
      if ([dic isKindOfClass:[NSDictionary class]]) {
          NSString *key = dic[@"ak"];
          NSString *secret = dic[@"sk"];
          [[AipOcrService shardService] authWithAK:key andSK:secret];
          dispatch_async(dispatch_get_main_queue(), ^{
              NSLog(@"[OcrPlugin] success: %@", @"初始化成功");
              result(@"true");
          });
      }
  }
    // 通用文字识别
  else if([@"basicOcr" isEqualToString:call.method]){
      [self generalBasicOCR];
  }
    // 通用文字识别(高精度版)
  else if([@"accurateBasicOcr" isEqualToString:call.method]){
      [self generalAccurateBasicOCR];
  }
    // 通用文字识别(含位置信息版)
  else if([@"getBasicOcr" isEqualToString:call.method]){
      [self generalOCR];
  }
    // 通用文字识别(高精度含位置信息版)
  else if([@"accurateOcr" isEqualToString:call.method]){
      [self generalAccurateOCR];
  }
    // 通用文字识别(含生僻字版)
  else if([@"enchancedOcr" isEqualToString:call.method]){
      [self generalEnchancedOCR];
  }
    // 身份证正面
  else if([@"idcardFront" isEqualToString:call.method]){
      [self idcardOCROnlineFront];
  }
    // 身份证反面
  else if([@"idcardBack" isEqualToString:call.method]){
      [self idcardOCROnlineBack];
  }
    // 银行卡识别
  else if([@"bankCard" isEqualToString:call.method]){
      [self bankCardOCROnline];
  }
  else {
    result(FlutterMethodNotImplemented);
  }
}

#pragma mark - 通用文字识别
- (void)generalBasicOCR{
    UIViewController * vc = [AipGeneralVC ViewControllerWithHandler:^(UIImage *image) {
        NSDictionary *options = @{@"language_type": @"CHN_ENG", @"detect_direction": @"true"};
        [[AipOcrService shardService] detectTextBasicFromImage:image
                                                   withOptions:options
                                                successHandler:self->_successHandler
                                                   failHandler:self->_failHandler];
    }];
    [flutterViewController presentViewController: vc animated: false completion:nil];
}

#pragma mark - 通用文字识别(高精度版)
- (void)generalAccurateBasicOCR{
    UIViewController * vc = [AipGeneralVC ViewControllerWithHandler:^(UIImage *image) {
        NSDictionary *options = @{@"language_type": @"CHN_ENG", @"detect_direction": @"true"};
        [[AipOcrService shardService] detectTextAccurateBasicFromImage:image
                                                      withOptions:options
                                                        successHandler:self->_successHandler
                                                           failHandler:self->_failHandler];
    }];
    [flutterViewController presentViewController: vc animated: false completion:nil];
}

#pragma mark - 通用文字识别(含位置信息版)
- (void)generalOCR{
    UIViewController * vc = [AipGeneralVC ViewControllerWithHandler:^(UIImage *image) {
        // 在这个block里，image即为切好的图片，可自行选择如何处理
        NSDictionary *options = @{@"language_type": @"CHN_ENG", @"detect_direction": @"true"};
        [[AipOcrService shardService] detectTextFromImage:image
                                              withOptions:options
                                           successHandler:self->_successHandler
                                              failHandler:self->_failHandler];
    }];
    [flutterViewController presentViewController: vc animated: false completion:nil];
}

#pragma mark - 通用文字识别(高精度含位置版)
- (void)generalAccurateOCR{
    UIViewController * vc = [AipGeneralVC ViewControllerWithHandler:^(UIImage *image) {
        NSDictionary *options = @{@"language_type": @"CHN_ENG", @"detect_direction": @"true"};
        [[AipOcrService shardService] detectTextAccurateFromImage:image
                                                      withOptions:options
                                                   successHandler:self->_successHandler
                                                      failHandler:self->_failHandler];
    }];
    [flutterViewController presentViewController: vc animated: false completion:nil];
}

#pragma mark - 通用文字识别(含生僻字版)
- (void)generalEnchancedOCR{
    UIViewController * vc = [AipGeneralVC ViewControllerWithHandler:^(UIImage *image) {
        NSDictionary *options = @{@"language_type": @"CHN_ENG", @"detect_direction": @"true"};
        [[AipOcrService shardService] detectTextEnhancedFromImage:image
                                                      withOptions:options
                                                   successHandler:self->_successHandler
                                                      failHandler:self->_failHandler];
    }];
    [flutterViewController presentViewController: vc animated: false completion:nil];
}

#pragma mark - 身份证正面拍照识别
- (void)idcardOCROnlineFront{
    UIViewController * vc =
       [AipCaptureCardVC ViewControllerWithCardType:CardTypeIdCardFont andImageHandler:^(UIImage *image) {
           [self saveImage: image];
           [[AipOcrService shardService] detectIdCardFrontFromImage:image
                                                        withOptions:nil
                                                     successHandler:self->_successHandler
                                                        failHandler:self->_failHandler];
                                    }];
    [flutterViewController presentViewController: vc animated: false completion:nil];
}

#pragma mark - 身份证反面拍照识别
- (void)idcardOCROnlineBack{
    UIViewController * vc =
       [AipCaptureCardVC ViewControllerWithCardType:CardTypeIdCardBack andImageHandler:^(UIImage *image) {
           [self saveImage: image];
           [[AipOcrService shardService] detectIdCardBackFromImage:image
                                                       withOptions:nil
                                                    successHandler:self->_successHandler
                                                       failHandler:self->_failHandler];
            }];
   [flutterViewController presentViewController: vc animated: false completion:nil];
}

#pragma mark - 银行卡正面拍照识别
- (void)bankCardOCROnline{
    UIViewController * vc =
            [AipCaptureCardVC ViewControllerWithCardType:CardTypeBankCard andImageHandler:^(UIImage *image) {
                
                [[AipOcrService shardService] detectBankCardFromImage:image
                                                       successHandler:self->_successHandler
                                                          failHandler:self->_failHandler];
            }];
    [flutterViewController presentViewController:vc animated:YES completion:nil];
}

- (void)configCallback {
    __weak typeof(self) weakSelf = self;

    UIViewController *viewController = flutterViewController;
    
    // 这是默认的识别成功的回调
    _successHandler = ^(id result){
        NSMutableDictionary * muSuccessDic = [[NSMutableDictionary alloc] initWithCapacity:0];
//        NSLog(@"%@", result);
        NSMutableString *message = [NSMutableString string];
        [muSuccessDic setObject: @"1" forKey: @"resultCode"];
        if(result[@"words_result"]){
            if([result[@"words_result"] isKindOfClass:[NSDictionary class]]){
                [result[@"words_result"] enumerateKeysAndObjectsUsingBlock:^(id key, id obj, BOOL *stop) {
                    if([obj isKindOfClass:[NSDictionary class]] && [obj objectForKey:@"words"]){
                        [message appendFormat:@"%@: %@\n", key, obj[@"words"]];
                      if( [@"idcardFront idcardBack" containsString: currentmethod] ){
                        [muSuccessDic setObject: self->imagePath != nil ? self->imagePath : @"" forKey: @"imagePath"];
                            if( [key  isEqual: @"签发机关"] && obj[@"words"] ){
                                [muSuccessDic setObject: obj[@"words"] forKey: @"issueAuthority"];
                            }
                            if( [key  isEqual: @"失效日期"] && obj[@"words"] ){
                                [muSuccessDic setObject: obj[@"words"] forKey: @"expiryDate"];
                            }
                            if( [key  isEqual: @"签发日期"] && obj[@"words"] ){
                                [muSuccessDic setObject: obj[@"words"] forKey: @"signDate"];
                            }
                            if( [key  isEqual: @"姓名"] && obj[@"words"] ){
                                [muSuccessDic setObject: obj[@"words"] forKey: @"name"];
                            }
                            if( [key  isEqual: @"公民身份号码"] && obj[@"words"] ){
                                [muSuccessDic setObject: obj[@"words"] forKey: @"idNumber"];
                            }
                            if( [key  isEqual: @"出生"] && obj[@"words"] ){
                                [muSuccessDic setObject: obj[@"words"] forKey: @"birthday"];
                            }
                            if( [key  isEqual: @"性别"] && obj[@"words"] ){
                                [muSuccessDic setObject: obj[@"words"] forKey: @"gender"];
                            }
                            if( [key  isEqual: @"民族"] && obj[@"words"] ){
                                [muSuccessDic setObject: obj[@"words"] forKey: @"ethnic"];
                            }
                            if( [key  isEqual: @"住址"] && obj[@"words"] ){
                                [muSuccessDic setObject: obj[@"words"] forKey: @"address"];
                            }
                            
                        }
                    }else{
                        [message appendFormat:@"%@: %@\n", key, obj];
                    }
                }];
            }else if([result[@"words_result"] isKindOfClass:[NSArray class]]){
                for(NSDictionary *obj in result[@"words_result"]){
                    if([obj isKindOfClass:[NSDictionary class]] && [obj objectForKey:@"words"]){
                        [message appendFormat:@"%@\n", obj[@"words"]];
                    }else{
                        [message appendFormat:@"%@\n", obj];
                    }
                }
            }
            NSLog(@"[OcrPlugin] success: %@", muSuccessDic);
//            NSDictionary *resultData = @{
//                @"result": result[@"words_result"],
//                @"imagePath": imagePath != nil ? imagePath : @""
//            };
            self->eventSink(muSuccessDic);
        }else{
            [message appendFormat:@"%@", result];

            // {result: {签发机关: {words: 余江县公安局, location: {top: 181, width: 81, left: 176, height: 14}}, 签发日期: {words: 20140730, location: {top: 213, width: 71, left: 173, height: 13}}, 失效日期: {words: 20240730, location: {top: 214, width: 71, left: 254, height: 12}}}
          if ([@"bankCard" containsString: self->currentmethod]) {
                // {result: {bank_card_type: 1, bank_name: 中国银行, valid_date: 08/26, bank_card_number: 621788 0800004636579}, log_id: 6921486488948229332}
                if( result[@"result"] && result[@"result"][@"bank_name"] ){
                    [muSuccessDic setObject: result[@"result"][@"bank_name"] forKey: @"bankCardName"];
                }
                if( result[@"result"] && result[@"result"][@"bank_card_number"] ){
                    [muSuccessDic setObject: result[@"result"][@"bank_card_number"] forKey: @"bankCardNumber"];
                }
                if( result[@"result"] && result[@"result"][@"bank_card_type"] ){
                    [muSuccessDic setObject: result[@"result"][@"bank_card_type"] forKey: @"bankCardType"];
                }
              self->eventSink(muSuccessDic);
            } else {
              self->eventSink(message);
            }
        }
//        NSLog(@"[OcrPlugin] success: %@", message);
//        NSLog(@"[OcrPlugin] currentMethod--------: %@", currentmethod);
        
        dispatch_async(dispatch_get_main_queue(), ^{
            [viewController dismissViewControllerAnimated: YES completion:nil];
        });
    };
    // 识别失败的回调
    _failHandler = ^(NSError *error){
        NSMutableDictionary * muErrorDic = [[NSMutableDictionary alloc] initWithCapacity:0];
        NSLog(@"%@", error);
        [muErrorDic setObject: @"2" forKey: @"resultCode"];
        NSString *msg = [NSString stringWithFormat:@"%li:%@", (long)[error code], [error localizedDescription]];
        [[NSOperationQueue mainQueue] addOperationWithBlock:^{
            [[[UIAlertView alloc] initWithTitle:@"识别失败结果" message:msg delegate:weakSelf cancelButtonTitle:@"确定" otherButtonTitles:nil] show];
        }];
        eventSink(muErrorDic);
    };
}

#pragma mark - 保存图片文件
- (void)saveImage:(UIImage*)image {
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *fileName = [[self getNowTimeTimestamp] stringByAppendingString: @"-image.png"];
    NSString *filePath = [[paths objectAtIndex:0] stringByAppendingPathComponent: fileName];
    BOOL result = [UIImagePNGRepresentation(image) writeToFile:filePath atomically:YES]; // 保存成功会返回YES

    if(result ==YES) {
        NSLog(@"保存成功");
        imagePath = filePath;
    }
}

#pragma mark - 测试联网阿里授权必须
-(void)httpAuthority{
    NSURL *url = [NSURL URLWithString:@"https://www.jokui.com/"];//此处修改为自己公司的服务器地址
    NSURLRequest *request = [NSURLRequest requestWithURL:url];
    NSURLSession *session = [NSURLSession sharedSession];
    NSURLSessionDataTask *dataTask = [session dataTaskWithRequest:request completionHandler:^(NSData * _Nullable data, NSURLResponse * _Nullable response, NSError * _Nullable error) {
        if (error == nil) {
            NSDictionary *dict = [NSJSONSerialization JSONObjectWithData:data options:kNilOptions error:nil];
            NSLog(@"%@%@", @"测试联网成功---->", dict);
        }
    }];
    
    [dataTask resume];
}

#pragma mark - 获取到跟视图
- (UIViewController *)getRootViewController {
    UIWindow *window = [[[UIApplication sharedApplication] delegate] window];
    flutterViewController = window.rootViewController;
    return window.rootViewController;
}

#pragma mark - 获取当前时间戳有两种方法(以秒为单位)
-(NSString *)getNowTimeTimestamp{
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init] ;
    [formatter setDateStyle:NSDateFormatterMediumStyle];
    [formatter setTimeStyle:NSDateFormatterShortStyle];
    [formatter setDateFormat:@"YYYY-MM-dd HH:mm:ss"]; // ----------设置你想要的格式,hh与HH的区别:分别表示12小时制,24小时制
    //设置时区,这个对于时间的处理有时很重要
    NSTimeZone* timeZone = [NSTimeZone timeZoneWithName:@"Asia/Shanghai"];
    [formatter setTimeZone:timeZone];
    NSDate *datenow = [NSDate date];//现在时间,你可以输出来看下是什么格式
    NSString *timeSp = [NSString stringWithFormat:@"%ld", (long)[datenow timeIntervalSince1970]];
    return timeSp;
}

-(NSString *)getNowTimeTimestamp2{
    NSDate* dat = [NSDate dateWithTimeIntervalSinceNow:0];
    NSTimeInterval a=[dat timeIntervalSince1970];
    NSString*timeString = [NSString stringWithFormat:@"%0.f", a];//转为字符型
    ;
    return timeString;
}

#pragma mark - 获取当前时间戳  （以毫秒为单位）
-(NSString *)getNowTimeTimestamp3{
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init] ;
    [formatter setDateStyle:NSDateFormatterMediumStyle];
    [formatter setTimeStyle:NSDateFormatterShortStyle];
    [formatter setDateFormat:@"YYYY-MM-dd HH:mm:ss SSS"]; // ----------设置你想要的格式,hh与HH的区别:分别表示12小时制,24小时制
    //设置时区,这个对于时间的处理有时很重要
    NSTimeZone* timeZone = [NSTimeZone timeZoneWithName:@"Asia/Shanghai"];
    [formatter setTimeZone:timeZone];
    NSDate *datenow = [NSDate date];//现在时间,你可以输出来看下是什么格式
    NSString *timeSp = [NSString stringWithFormat:@"%ld", (long)[datenow timeIntervalSince1970]*1000];
    return timeSp;
}

@end
