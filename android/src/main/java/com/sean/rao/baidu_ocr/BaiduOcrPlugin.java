package com.sean.rao.baidu_ocr;

import static com.sean.rao.baidu_ocr.common.CameraEnum.CONTENT_TYPE_BANK_CARD;
import static com.sean.rao.baidu_ocr.common.CameraEnum.REQUEST_CODE_BANKCARD;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.BankCardResult;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.baidu.ocr.ui.util.FileUtil;
import com.sean.rao.baidu_ocr.common.CameraEnum;

import java.io.File;
import java.util.Objects;

import io.flutter.FlutterInjector;
import io.flutter.embedding.engine.loader.FlutterLoader;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;

/** BaiduOcrPlugin */
public class BaiduOcrPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware, EventChannel.StreamHandler {
  private static final String METHOD_CHANNEL = "baidu_ocr";
  private static final String EVENT_CHANNEL = "baidu_ocr/event";

  private Activity activity;
  private boolean hasGotToken = false;
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private EventChannel eventChannel;
  private Result eventResult;

  public static EventChannel.EventSink _events;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), METHOD_CHANNEL);
    eventChannel = new EventChannel(flutterPluginBinding.getBinaryMessenger(), EVENT_CHANNEL);

    eventChannel.setStreamHandler(this);
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    String method = call.method;
    eventResult = result;
    switch (method) {
      case "getPlatformVersion":
        result.success("Android " + android.os.Build.VERSION.RELEASE);
        break;
      case "initSdk": // 初始化SDK
        initAccessTokenWithAkSk( call, result);
        break;
      case "basicOcr": // 通用文字识别
        // CONTENT_TYPE_ID_CARD_FRONT = "IDCardFront";
        // CONTENT_TYPE_ID_CARD_BACK = "IDCardBack";
        String sideType = call.argument("sideType");
        Object argument = call.argument("isScan");
        boolean isScan = argument != null && (boolean) argument;
        Object ocrType = call.argument("type");
        int type = ocrType != null ? Integer.parseInt(String.valueOf(ocrType)) : CameraEnum.REQUEST_CODE_GENERAL;
        if (isScan) {
          localIdcardOCROnline(sideType, type);
        } else {
//          startActivity(sideType, CameraEnum.REQUEST_CODE_ICARD);
          if (type == REQUEST_CODE_BANKCARD) {
            startActivity(CameraActivity.CONTENT_TYPE_BANK_CARD, CameraEnum.REQUEST_CODE_BANKCARD);
          } else {
            startActivity(Objects.requireNonNull(sideType).isEmpty() ? CameraActivity.CONTENT_TYPE_GENERAL : sideType, type);
          }
        }
        break;
      default:
        result.notImplemented();
        break;
    }
  }

  /**
   * flutter 路径转换
   * @param fluPath
   * @return
   */
  public static String flutterToPath(@Nullable Object fluPath){
    FlutterLoader flutterLoader = FlutterInjector.instance().flutterLoader();
    return flutterLoader.getLookupKeyForAsset(String.valueOf(fluPath));
  }

  private boolean checkTokenStatus() {
    if (!hasGotToken) {
      Toast.makeText(activity, "token还未成功获取", Toast.LENGTH_LONG).show();
    }
    return hasGotToken;
  }

  /**
   * 用明文ak，sk初始化
   */
  private void initAccessTokenWithAkSk(final MethodCall call, final Result methodResult ) {
    String filePath = flutterToPath(call.argument("filePath"));
    OCR.getInstance(activity).initAccessToken(new OnResultListener<AccessToken>() {
      @Override
      public void onResult(AccessToken result) {
        String token = result.getAccessToken();
        hasGotToken = true;
        JSONObject resultData = new JSONObject();
        resultData.put("msg", "初始化成功");
        resultData.put("msg", true);
        eventResult.success(resultData);
        Log.d("BdOcrPlugin-initSDK 成功", token);
      }
      @Override
      public void onError(OCRError error) {
        error.printStackTrace();
        Log.d("BdOcrPlugin-AK,SK方式获取失败", error.getMessage());
      }
    }, filePath, activity);
  }

  // 身份证正反面扫描
  private void localIdcardOCROnline(String type, int reCode) {
    Intent intent = new Intent(activity, CameraActivity.class);
    intent.putExtra(CameraEnum.KEY_OUTPUT_FILE_PATH, FileUtil.getSaveFile(activity).getAbsolutePath());
    intent.putExtra(CameraEnum.KEY_NATIVE_ENABLE,true);
    // KEY_NATIVE_MANUAL设置了之后CameraActivity中不再自动初始化和释放模型
    // 请手动使用CameraNativeHelper初始化和释放模型
    // 推荐这样做，可以避免一些activity切换导致的不必要的异常
    intent.putExtra(CameraEnum.KEY_NATIVE_MANUAL,true);
    intent.putExtra(CameraEnum.KEY_CONTENT_TYPE, type);
    intent.putExtra(CameraEnum.REQUEST_CODE, reCode);
    activity.startActivityForResult(intent, reCode);
  }

  /**
   * 开始打开相机页面
   * @param type 类型
   * @param reCode 返回类型
   */
  private void startActivity(String type, int reCode) {
      if (!checkTokenStatus()) {
        return;
      }
      Intent intent = new Intent(activity, CameraActivity.class);
      intent.putExtra(CameraEnum.KEY_OUTPUT_FILE_PATH, FileUtil.getSaveFile(activity).getAbsolutePath());
      intent.putExtra(CameraEnum.KEY_CONTENT_TYPE, type);
      intent.putExtra(CameraEnum.REQUEST_CODE, reCode);
      activity.startActivityForResult(intent, reCode);
  }

  private String getRealPathFromURI(String url) {
    Uri contentURI = Uri.parse(url);
    String result;
    Cursor cursor = activity.getApplicationContext().getContentResolver().query(contentURI, null, null, null, null);
    if (cursor == null) { // Source is Dropbox or other similar local file path
      result = contentURI.getPath();
    } else {
      cursor.moveToFirst();
      int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
      result = cursor.getString(idx);
      cursor.close();
    }
    return result;
  }

  private void recIDCard(String idCardSide, String filePath) {
    IDCardParams param = new IDCardParams();
    param.setImageFile(new File(filePath));
    // 设置身份证正反面
    param.setIdCardSide(idCardSide);
    // 设置方向检测
    param.setDetectDirection(true);
    // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
    param.setImageQuality(20);

    param.setDetectRisk(true);

    OCR.getInstance(activity).recognizeIDCard(param, new OnResultListener<IDCardResult>() {
      @Override
      public void onResult(IDCardResult result) {
        if (result != null) {
          JSONObject resultData = JSON.parseObject(result.getJsonRes());
          resultData.put("imagePath", filePath);
          eventResult.success(resultData);
        }
      }

      @Override
      public void onError(OCRError error) {
        eventResult.success(error);
      }
    });
  }

  // 检查权限
  private boolean checkGalleryPermission() {
    int ret = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
    if (ret != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
      return false;
    }
    return true;
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    Log.e("onAttachedToActivity", "onAttachedToActivity" + binding);
    activity = binding.getActivity();
    binding.addRequestPermissionsResultListener(new PluginRegistry.RequestPermissionsResultListener() {
      @Override
      public boolean onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//          case SCAN_PERMISSION_CODE:
//            if (permissions.length != 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {//失败
//              Toast.makeText(activity, "请允许权限在识别", Toast.LENGTH_SHORT).show();
//            } else {//成功
//              //启动启动VIN码扫描识别页面
//              Intent intent = new Intent(activity, ScanVinActivity.class);
//              activity.startActivityForResult(intent, VIN_RECOG_CODE);
//            }
//            break;
//          case IMPORT_PERMISSION_CODE:
//            if (permissions.length != 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {//失败
//              Toast.makeText(activity, "请允许权限在识别", Toast.LENGTH_SHORT).show();
//            } else {//成功
//              //启动启动VIN码导入识别页面
//              Intent intent = new Intent(activity, VinRecogActivity.class);
//              activity.startActivityForResult(intent, VIN_RECOG_CODE);
//            }
//            break;
//        }
        return false;
      }
    });
    // 注册ActivityResult回调
    binding.addActivityResultListener(new PluginRegistry.ActivityResultListener() {
      @Override
      public boolean onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
          assert data != null;
          String filePath = FileUtil.getSaveFile(activity.getApplicationContext()).getAbsolutePath();
          String filePathFront = getRealPathFromURI(data.getStringExtra(CameraEnum.FILE_PATH));
          switch (requestCode) {
            case CameraEnum.REQUEST_CODE_GENERAL_BASIC: // 识别成功回调，通用文字识别
              RecognizeService.recGeneralBasic(activity, filePath, new RecognizeService.ServiceListener() {
                  @Override
                  public void onResult(String result) {
                    eventResult.success(result);
                  }
              });
              break;
            case CameraEnum.REQUEST_CODE_ACCURATE: // 识别成功回调，通用文字识别
              RecognizeService.recAccurate(activity, filePath, new RecognizeService.ServiceListener() {
                  @Override
                  public void onResult(String result) {
                    eventResult.success(result);
                  }
              });
              break;
            case CameraEnum.REQUEST_CODE_GENERAL: // 识别成功回调，通用文字识别（含位置信息）
              RecognizeService.recGeneral(activity, filePath, new RecognizeService.ServiceListener() {
                  @Override
                  public void onResult(String result) {
                    eventResult.success(result);
                  }
              });
              break;
            case CameraEnum.REQUEST_CODE_ACCURATE_BASIC: // 识别成功回调，通用文字识别（高精度版）
              RecognizeService.recAccurateBasic(activity, filePath, new RecognizeService.ServiceListener() {
                @Override
                public void onResult(String result) {
                  eventResult.success(result);
                }
              });
              break;
            case CameraEnum.REQUEST_CODE_GENERAL_ENHANCED: // 识别成功回调，通用文字识别（含生僻字版）
              RecognizeService.recGeneralEnhanced(activity, filePath, new RecognizeService.ServiceListener() {
                @Override
                public void onResult(String result) {
                  eventResult.success(result);
                }
              });
              break;
            case CameraEnum.REQUEST_CODE_GENERAL_WEBIMAGE: // 识别成功回调，网络图片文字识别
              RecognizeService.recWebimage(activity, filePath, new RecognizeService.ServiceListener() {
                @Override
                public void onResult(String result) {
                  eventResult.success(result);
                }
              });
              break;
            case CameraEnum.REQUEST_CODE_VEHICLE_LICENSE: // 识别成功回调，行驶证识别
              RecognizeService.recVehicleLicense(activity, filePath, new RecognizeService.ServiceListener() {
                @Override
                public void onResult(String result) {
                  eventResult.success(result);
                }
              });
              break;
            case CameraEnum.REQUEST_CODE_DRIVING_LICENSE: // 识别成功回调，驾驶证识别
              RecognizeService.recDrivingLicense(activity, filePath, new RecognizeService.ServiceListener() {
                @Override
                public void onResult(String result) {
                  eventResult.success(result);
                }
              });
              break;
            case CameraEnum.REQUEST_CODE_LICENSE_PLATE: // 识别成功回调，车牌识别
              RecognizeService.recLicensePlate(activity, filePath, new RecognizeService.ServiceListener() {
                @Override
                public void onResult(String result) {
                  eventResult.success(result);
                }
              });
              break;
            case CameraEnum.REQUEST_CODE_BUSINESS_LICENSE: // 识别成功回调，营业执照识别
              RecognizeService.recBusinessLicense(activity, filePath, new RecognizeService.ServiceListener() {
                @Override
                public void onResult(String result) {
                  eventResult.success(result);
                }
              });
              break;
            case CameraEnum.REQUEST_CODE_RECEIPT: // 识别成功回调，通用票据识别
              RecognizeService.recReceipt(activity, filePath, new RecognizeService.ServiceListener() {
                @Override
                public void onResult(String result) {
                  eventResult.success(result);
                }
              });
              break;
            case CameraEnum.REQUEST_CODE_TAXIRECEIPT: // 识别成功回调，出租车票
              RecognizeService.recTaxireceipt(activity, filePath, new RecognizeService.ServiceListener() {
                @Override
                public void onResult(String result) {
                  eventResult.success(result);
                }
              });
              break;
            case CameraEnum.REQUEST_CODE_VINCODE: // 识别成功回调，VIN码
              RecognizeService.recVincode(activity, filePath, new RecognizeService.ServiceListener() {
                @Override
                public void onResult(String result) {
                  eventResult.success(result);
                }
              });
              break;
            case CameraEnum.REQUEST_CODE_TRAINTICKET: // 识别成功回调，火车票
              RecognizeService.recTrainticket(activity, filePath, new RecognizeService.ServiceListener() {
                @Override
                public void onResult(String result) {
                  eventResult.success(result);
                }
              });
              break;
            case CameraEnum.REQUEST_CODE_PASSPORT: // 识别成功回调，护照
              RecognizeService.recPassport(activity, filePath, new RecognizeService.ServiceListener() {
                @Override
                public void onResult(String result) {
                  eventResult.success(result);
                }
              });
              break;
            case CameraEnum.REQUEST_CODE_QRCODE: // 识别成功回调，二维码
              RecognizeService.recQrcode(activity, filePath, new RecognizeService.ServiceListener() {
                @Override
                public void onResult(String result) {
                  eventResult.success(result);
                }
              });
              break;
            case CameraEnum.REQUEST_CODE_LOTTERY: // 识别成功回调，彩票
              RecognizeService.recLottery(activity, filePath, new RecognizeService.ServiceListener() {
                @Override
                public void onResult(String result) {
                  eventResult.success(result);
                }
              });
              break;
            case CameraEnum.REQUEST_CODE_VATINVOICE: // 识别成功回调，增值税发票
              RecognizeService.recVatInvoice(activity, filePath, new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                          eventResult.success(result);
                        }
                      });
              break;
            case CameraEnum.REQUEST_CODE_NUMBERS: // 识别成功回调，数字
              RecognizeService.recNumbers(activity, filePath, new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                          eventResult.success(result);
                        }
                      });
              break;
            case CameraEnum.REQUEST_CODE_HANDWRITING: // 识别成功回调，手写
              RecognizeService.recHandwriting(activity, filePath, new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                          eventResult.success(result);
                        }
                      });
              break;
            case CameraEnum.REQUEST_CODE_BUSINESSCARD: // 识别成功回调，名片
              RecognizeService.recBusinessCard(activity, filePath, new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                          eventResult.success(result);
                        }
                      });
              break;
            case CameraEnum.REQUEST_CODE_CUSTOM: // 识别成功回调，自定义模板
              RecognizeService.recCustom(activity, filePath, new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                          eventResult.success(result);
                        }
                      });
              break;
            case CameraEnum.REQUEST_CODE_TRIP_TICKET: // 识别成功回调，行程单识别
              RecognizeService.recTripTicket(activity, filePath, new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                          eventResult.success(result);
                        }
                      });
              break;
            case CameraEnum.REQUEST_CODE_CAR_SELL_INVOICE: // 识别成功回调，机动车销售发票
              RecognizeService.recCarSellInvoice(activity, filePath, new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                          eventResult.success(result);
                        }
                      });
              break;
            case CameraEnum.REQUEST_CODE_VIHICLE_SERTIFICATION: // 识别成功回调，车辆合格证
              RecognizeService.recVihicleCertification(activity, filePath, new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                          eventResult.success(result);
                        }
                      });
              break;
            case CameraEnum.REQUEST_CODE_EXAMPLE_DOC_REG: // 识别成功回调，试卷分析和识别
              RecognizeService.recExampleDoc(activity, filePath, new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                          eventResult.success(result);
                        }
                      });
              break;
            case CameraEnum.REQUEST_CODE_WRITTEN_TEXT: // 识别成功回调，手写文字识别
              RecognizeService.recWrittenText(activity, filePath, new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                          eventResult.success(result);
                        }
                      });
              break;
            case CameraEnum.REQUEST_CODE_HUKOU_PAGE: // 识别成功回调，户口本识别
              RecognizeService.recHuKouPage(activity, filePath, new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                          eventResult.success(result);
                        }
                      });
              break;
            case CameraEnum.REQUEST_CODE_NORMAL_MACHINE_INVOICE: // 识别成功回调，通用机打发票识别
              RecognizeService.recNormalMachineInvoice(activity, filePath, new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                          eventResult.success(result);
                        }
                      });
              break;
            case CameraEnum.REQUEST_CODE_WEIGHT_NOTE: // 识别成功回调，磅单识别
              RecognizeService.recweightnote(activity, filePath, new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                          eventResult.success(result);
                        }
                      });
              break;
            case CameraEnum.REQUEST_CODE_MEDICAL_DETAIL: // 识别成功回调，医疗费用明细识别
              RecognizeService.recmedicaldetail(activity, filePath, new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                          eventResult.success(result);
                        }
                      });
              break;
            case CameraEnum.REQUEST_CODE_ONLINE_TAXI_ITINERARY: // 识别成功回调，网约车行程单识别
              RecognizeService.reconlinetaxiitinerary(activity, filePath, new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                          eventResult.success(result);
                        }
                      });
              break;
            case CameraEnum.REQUEST_CODE_ICARD:
              recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePathFront);
              break;
            case CameraEnum.REQUEST_CODE_BANKCARD:
              RecognizeService.recBankCard(activity, filePath, new OnResultListener<BankCardResult>()  {
                @Override
                public void onResult(BankCardResult bankCardResult) {
                  eventResult.success(JSON.parseObject(bankCardResult.getJsonRes()));
                }
                @Override
                public void onError(OCRError ocrError) {
                  eventResult.success(ocrError);
                }
              });
              break;
            case CameraEnum.REQUEST_CODE_CAMERA:
              String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
              if (!TextUtils.isEmpty(contentType)) {
                if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                  recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath);
                } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK.equals(contentType)) {
                  recIDCard(IDCardParams.ID_CARD_SIDE_BACK, filePath);
                }
              }
              break;
            default:
              break;
          }
          return true;
        }
        return false;
      }
    });
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {

  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {

  }

  @Override
  public void onDetachedFromActivity() {
    if( activity != null){
      activity = null;
    }
  }

  @Override
  public void onListen(Object arguments, EventChannel.EventSink events) {
    Log.d("TAG", "onListen: "+events);
    if( events != null){
      _events = events;
    }
  }

  @Override
  public void onCancel(Object arguments) {
    if( _events != null){
      _events = null;
    }
  }
}
