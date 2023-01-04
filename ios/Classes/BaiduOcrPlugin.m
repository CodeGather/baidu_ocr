#import "BaiduOcrPlugin.h"
#import "NSDictionary+Utils.h"
#import <UIKit/UIKit.h>
#import <objc/runtime.h>
#import <AipOcrSdk/AipOcrSdk.h>

@interface BaiduOcrPlugin()<FlutterStreamHandler>
@end

typedef NS_ENUM(NSUInteger, OrcType) {
  REQUEST_CODE_ICARD = 104,
  REQUEST_CODE_GENERAL = 105,
  REQUEST_CODE_GENERAL_BASIC = 106,
  REQUEST_CODE_ACCURATE_BASIC = 107,
  REQUEST_CODE_ACCURATE = 108,
  REQUEST_CODE_GENERAL_ENHANCED = 109,
  REQUEST_CODE_GENERAL_WEBIMAGE = 110,
  REQUEST_CODE_BANKCARD = 111,
  REQUEST_CODE_VEHICLE_LICENSE = 120,
  REQUEST_CODE_DRIVING_LICENSE = 121,
  REQUEST_CODE_LICENSE_PLATE = 122,
  REQUEST_CODE_BUSINESS_LICENSE = 123,
  REQUEST_CODE_RECEIPT = 124,
  REQUEST_CODE_PASSPORT = 125,
  REQUEST_CODE_NUMBERS = 126,
  REQUEST_CODE_QRCODE = 127,
  REQUEST_CODE_BUSINESSCARD = 128,
  REQUEST_CODE_HANDWRITING = 129,
  REQUEST_CODE_LOTTERY = 130,
  REQUEST_CODE_VATINVOICE = 131,
  REQUEST_CODE_CUSTOM = 132,
  REQUEST_CODE_TAXIRECEIPT = 133,
  REQUEST_CODE_VINCODE = 134,
  REQUEST_CODE_TRAINTICKET = 135,
  REQUEST_CODE_TRIP_TICKET = 136,
  REQUEST_CODE_CAR_SELL_INVOICE = 137,
  REQUEST_CODE_VIHICLE_SERTIFICATION = 138,
  REQUEST_CODE_EXAMPLE_DOC_REG = 139,
  REQUEST_CODE_WRITTEN_TEXT = 140,
  REQUEST_CODE_HUKOU_PAGE = 141,
  REQUEST_CODE_NORMAL_MACHINE_INVOICE = 142,
  REQUEST_CODE_WEIGHT_NOTE = 143,
  REQUEST_CODE_MEDICAL_DETAIL = 144,
  REQUEST_CODE_ONLINE_TAXI_ITINERARY = 145,
  REQUEST_CODE_PICK_IMAGE_FRONT = 201,
  REQUEST_CODE_PICK_IMAGE_BACK = 202,
  REQUEST_CODE_CAMERA = 102,
};

@implementation BaiduOcrPlugin {
    // 默认的识别成功的回调
    void (^_successHandler)(id);
    // 默认的识别失败的回调
    void (^_failHandler)(NSError *);
    
    FlutterEventSink eventSink;
    UIViewController *flutterViewController;
    NSString *currentmethod;
    NSString *imagePath;
  NSInteger currentOcrModel;
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
  NSDictionary *dic = call.arguments;
  currentmethod = call.method;
  eventResult = result;
  if ([@"getPlatformVersion" isEqualToString:call.method]) {
    result([@"iOS " stringByAppendingString:[[UIDevice currentDevice] systemVersion]]);
  }
    // SDK 初始化
  else if ([@"initSdk" isEqualToString:call.method]) {
    if ([dic isKindOfClass:[NSDictionary class]] && [dic objectForKey: @"filePath"]) {
      NSString *filePath = [self changeUriToPath:[dic stringValueForKey:@"filePath"defaultValue:@""]];
      NSData *content = [NSData dataWithContentsOfFile:filePath];
      [[AipOcrService shardService] authWithLicenseFileData:content];
      dispatch_async(dispatch_get_main_queue(), ^{
        NSLog(@"[OcrPlugin] success: %@", @"初始化成功");
        [self resultData: @"初始化成功" data: @true];
      });
    }
  }
    // 通用文字识别
  else if([@"basicOcr" isEqualToString:call.method]){
    if ([dic isKindOfClass:[NSDictionary class]] && [dic objectForKey: @"type"]) {
      currentOcrModel = [dic integerValueForKey:@"type" defaultValue:REQUEST_CODE_ICARD];
      NSString *sideType = [dic stringValueForKey:@"sideType" defaultValue:@""];
      bool isScan = [dic boolValueForKey:@"isScan" defaultValue:false];
      switch (currentOcrModel) {
        case REQUEST_CODE_ICARD:
          if ([sideType  isEqual: @"IDCardFront"]) {
            if (isScan) {
              [self localIdcardOCROnlineFront];
            } else {
              [self idcardOCROnlineFront];
            }
          } else {
            if (isScan) {
              [self localIdcardOCROnlineBack];
            } else {
                [self idcardOCROnlineBack];
            }
          }
          break;
        case REQUEST_CODE_BANKCARD:
          [self bankCardOCROnline];
          break;
        default:
          [self generalBasicOCR];
          break;
      }
    }
  }
}

- (void)resultData:(NSString*)resultMsg data: (id __nullable)showResult {
  NSDictionary *dict = @{
        @"msg" : resultMsg,
        @"data" : showResult
    };
  self -> eventResult(dict);
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
  vc.modalPresentationStyle = UIModalPresentationFullScreen;
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
  vc.modalPresentationStyle = UIModalPresentationFullScreen;
  [flutterViewController presentViewController: vc animated: false completion:nil];
}

#pragma mark - 身份证反面拍照识别
- (void)idcardOCROnlineBack{
    UIViewController * vc =
     [AipCaptureCardVC ViewControllerWithCardType:CardTypeIdCardBack
                                  andImageHandler:^(UIImage *image) {
         [self saveImage: image];
         [[AipOcrService shardService] detectIdCardBackFromImage:image
                                                     withOptions:nil
                                                  successHandler:self->_successHandler
                                                     failHandler:self->_failHandler];
          }
  ];
  vc.modalPresentationStyle = UIModalPresentationFullScreen;
  [flutterViewController presentViewController: vc animated: false completion:nil];
}


- (void)localIdcardOCROnlineFront {
  UIViewController * vc = [
    AipCaptureCardVC ViewControllerWithCardType:CardTypeLocalIdCardFont
                                andImageHandler:^(UIImage *image) {
     [self saveImage: image];
      [[AipOcrService shardService] detectIdCardFrontFromImage:image
                                                   withOptions:nil
                                                successHandler:self->_successHandler
                                                   failHandler:self->_failHandler];
    }
  ];
  vc.modalPresentationStyle = UIModalPresentationFullScreen;
  [flutterViewController presentViewController: vc animated: false completion:nil];
}
- (void)localIdcardOCROnlineBack{
  UIViewController * vc = [
    AipCaptureCardVC ViewControllerWithCardType:CardTypeLocalIdCardBack
                                andImageHandler:^(UIImage *image) {
     [self saveImage: image];
      [[AipOcrService shardService] detectIdCardBackFromImage:image
                                                  withOptions:nil
                                               successHandler:self->_successHandler
                                                  failHandler:self->_failHandler];
    }
  ];
  vc.modalPresentationStyle = UIModalPresentationFullScreen;
  [flutterViewController presentViewController: vc animated: false completion:nil];
}

#pragma mark - 银行卡正面拍照识别
- (void)bankCardOCROnline{
  UIViewController * vc = [
    AipCaptureCardVC ViewControllerWithCardType:CardTypeBankCard
                                andImageHandler:^(UIImage *image) {
      [self saveImage: image];
      [[AipOcrService shardService] detectBankCardFromImage:image
                                             successHandler:self->_successHandler
                                                failHandler:self->_failHandler];
    }
  ];
  // [UIApplication sharedApplication].delegate.window.safeAreaInsets.bottom
  vc.modalPresentationStyle = UIModalPresentationFullScreen;
  [flutterViewController presentViewController:vc animated:YES completion:nil];
}

- (void)configCallback {
    typeof(self) weakSelf = self;

    UIViewController *viewController = flutterViewController;
    
    // 这是默认的识别成功的回调
    _successHandler = ^(id result){
      NSMutableDictionary *resultDict = [result mutableCopy];
      
      [resultDict setObject: weakSelf->imagePath != nil ? weakSelf->imagePath : @"" forKey: @"imagePath"];
      weakSelf->eventResult(resultDict);
      dispatch_async(dispatch_get_main_queue(), ^{
          [viewController dismissViewControllerAnimated: YES completion:nil];
      });
    };
    // 识别失败的回调
    _failHandler = ^(NSError *error){
      [weakSelf resultData: @"识别失败" data: @false];
      dispatch_async(dispatch_get_main_queue(), ^{
          [viewController dismissViewControllerAnimated: YES completion:nil];
      });
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

#pragma mark  assets -> 转换成真实路径
- (NSString *) changeUriToPath:(NSString *) key{
  NSString* keyPath = [[self flutterVC] lookupKeyForAsset: key];
  NSString* path = [[NSBundle mainBundle] pathForResource: keyPath ofType:nil];
  return path;
}

#pragma mark  ======获取flutterVc========
- (FlutterViewController *)flutterVC{
  UIViewController * viewController = [UIApplication sharedApplication].keyWindow.rootViewController;
  if ([viewController isKindOfClass: [FlutterViewController class]]) {
    return (FlutterViewController *)viewController;
  } else {
    return (FlutterViewController *)[self findCurrentViewController];
  }
}
#pragma mark  ======在view上添加UIViewController========
- (UIViewController *)findCurrentViewController{
    UIWindow *window = [[UIApplication sharedApplication].delegate window];
    UIViewController *topViewController = [window rootViewController];
    while (true) {
        if (topViewController.presentedViewController) {
            topViewController = topViewController.presentedViewController;
        } else if ([topViewController isKindOfClass:[UINavigationController class]] && [(UINavigationController*)topViewController topViewController]) {
            topViewController = [(UINavigationController *)topViewController topViewController];
        } else if ([topViewController isKindOfClass:[UITabBarController class]]) {
            UITabBarController *tab = (UITabBarController *)topViewController;
            topViewController = tab.selectedViewController;
        } else {
            break;
        }
    }
    return topViewController;
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
