//
//  AipOCRBankVC.m
//  AipOcrDemo
//
//  Created by v_guijiang on 2021/11/29.
//  Copyright © 2021 Baidu. All rights reserved.
//

#import "AipOCRBankVC.h"
#import "AipOCRIDCardVC.h"
#import <objc/runtime.h>
#import <AipOcrSdk/AipOcrSdk.h>
#import "UIViewController+bd_present.h"

@interface AipOCRBankVC ()
@property (strong, nonatomic) IBOutlet UITableView *tableView;
@property (nonatomic, strong) NSMutableArray<NSArray<NSString *> *> *actionList;
@end

@implementation AipOCRBankVC{
    // 默认的识别成功的回调
    void (^_successHandler)(id);
    // 默认的识别失败的回调
    void (^_failHandler)(NSError *);
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self configureView];
    [self configureData];
    [self configCallback];
}
- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [self updateTableView];
}
- (void)configureView {
    
    self.title = @"银行卡识别";
}
- (void)configureData {
    
    self.actionList = [NSMutableArray array];
    [self.actionList addObject:@[@"银行卡正面拍照识别", @"bankCardOCROnline"]];
}

- (void)configCallback {
    __weak typeof(self) weakSelf = self;
    
    // 这是默认的识别成功的回调
    _successHandler = ^(id result){
        NSLog(@"%@", result);
        NSString *title = @"识别结果";
        NSMutableString *message = [NSMutableString string];
        
        
        if (result[@"words_result"]){
            if ([result[@"words_result"] isKindOfClass:[NSDictionary class]]){
                [result[@"words_result"] enumerateKeysAndObjectsUsingBlock:^(id key, id obj, BOOL *stop) {
                    if ([obj isKindOfClass:[NSDictionary class]] && [obj objectForKey:@"words"]){
                        [message appendFormat:@"%@: %@\n", key, obj[@"words"]];
                    } else {
                        [message appendFormat:@"%@: %@\n", key, obj];
                    }
                }];
            } else if ([result[@"words_result"] isKindOfClass:[NSArray class]]){
                for (NSDictionary *obj in result[@"words_result"]){
                    if ([obj isKindOfClass:[NSDictionary class]] && [obj objectForKey:@"words"]){
                        [message appendFormat:@"%@\n", obj[@"words"]];
                    } else {
                        [message appendFormat:@"%@\n", obj];
                    }
                }
            }
        } else if (result[@"codes_result"]){
            if ([result[@"codes_result"] isKindOfClass:[NSArray class]]){
                for (id dict in result[@"codes_result"]) {
                    if ([dict isKindOfClass:[NSDictionary class]]) {
                        NSArray *array = dict[@"text"];
                        for (int i=0; i<array.count; i++) {
                            [message appendFormat:@"%@\n", array[i]];
                        }
                    }
                }
            }
        } else {
            
            NSError*parseError =nil;
            NSData *jsonData = [NSJSONSerialization dataWithJSONObject:result options:NSJSONWritingPrettyPrinted error:&parseError];
            
            NSMutableString *jsStr = [[NSMutableString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
            [jsStr replaceOccurrencesOfString:@"\\" withString:@"" options:1 range:NSMakeRange(0, jsStr.length)];
            [message appendFormat:@"%@", jsStr];
           
        }
        
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{

            UIAlertController * alertCon = [UIAlertController alertControllerWithTitle:@"识别结果" message:message preferredStyle:UIAlertControllerStyleAlert];
               
                UIAlertAction * ok =[UIAlertAction actionWithTitle:@"确定" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
 
                }];
                [alertCon addAction:ok];

                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    [weakSelf dismissViewControllerAnimated:YES completion:^{
                        [weakSelf bd_presentViewControllerWithFullScreen:alertCon animated:YES completion:nil];
                    }];

                });
            

            });
    };
    
    _failHandler = ^(NSError *error){
        NSLog(@"%@", error);
        NSString *msg = [NSString stringWithFormat:@"%li:%@", (long)[error code], [error localizedDescription]];
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{

            UIAlertController * alertCon = [UIAlertController alertControllerWithTitle:@"识别失败" message:msg preferredStyle:UIAlertControllerStyleAlert];
               
                UIAlertAction * ok =[UIAlertAction actionWithTitle:@"确定" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
 
                }];
                [alertCon addAction:ok];

                dispatch_async(dispatch_get_main_queue(), ^{

                    [weakSelf dismissViewControllerAnimated:YES completion:^{
                        [weakSelf bd_presentViewControllerWithFullScreen:alertCon animated:YES completion:nil];
                    }];

                });

            });
    };
}

- (void)updateTableView {
    
    [self.tableView reloadData];
}
- (void)bankCardOCROnline{
    
    UIViewController * vc =
    [AipCaptureCardVC ViewControllerWithCardType:CardTypeBankCard
                                 andImageHandler:^(UIImage *image) {
        
        [[AipOcrService shardService] detectBankCardFromImage:image
                                               successHandler:_successHandler
                                                  failHandler:_failHandler];
        
    }];
    [self bd_presentViewControllerWithFullScreen:vc animated:YES completion:nil];
    
}
- (void)mockBundlerIdForTest {
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wundeclared-selector"
    [self mockClass:[NSBundle class] originalFunction:@selector(bundleIdentifier) swizzledFunction:@selector(sapicamera_bundleIdentifier)];
#pragma clang diagnostic pop
}

- (void)mockClass:(Class)class originalFunction:(SEL)originalSelector swizzledFunction:(SEL)swizzledSelector {
    
    Method originalMethod = class_getInstanceMethod(class, originalSelector);
    Method swizzledMethod = class_getInstanceMethod(class, swizzledSelector);
    
    BOOL didAddMethod = class_addMethod(class, originalSelector, method_getImplementation(swizzledMethod), method_getTypeEncoding(swizzledMethod));
    
    if (didAddMethod) {
        class_replaceMethod(class, swizzledSelector, method_getImplementation(originalMethod), method_getTypeEncoding(originalMethod));
    } else {
        method_exchangeImplementations(originalMethod, swizzledMethod);
    }
    
}

#pragma mark - UITableViewDelegate & UITableViewDataSource
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return self.actionList.count;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    
    return 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    UITableViewCell *cell = nil;
    
    NSArray *actions = self.actionList[indexPath.row];
    cell = [tableView dequeueReusableCellWithIdentifier:@"BankCell" forIndexPath:indexPath];
    cell.textLabel.text = actions[0];
    
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    if (indexPath.section == 0) {
        return 55;
    } else {
        return 44;
    }
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
    SEL funSel = NSSelectorFromString(self.actionList[indexPath.row][1]);
    if (funSel) {
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Warc-performSelector-leaks"
        [self performSelector:funSel];
#pragma clang diagnostic pop
    }
}

-(BOOL)shouldAutorotate{
    return NO;
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations{
    return UIInterfaceOrientationMaskPortrait;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}
#pragma mark UIAlertViewDelegate
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    
    [self dismissViewControllerAnimated:YES completion:nil];
}

@end
