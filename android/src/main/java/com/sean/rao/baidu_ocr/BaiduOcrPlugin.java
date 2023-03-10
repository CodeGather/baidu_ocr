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
      case "initSdk": // ?????????SDK
        initAccessTokenWithAkSk( call, result);
        break;
      case "basicOcr": // ??????????????????
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
   * flutter ????????????
   * @param fluPath
   * @return
   */
  public static String flutterToPath(@Nullable Object fluPath){
    FlutterLoader flutterLoader = FlutterInjector.instance().flutterLoader();
    return flutterLoader.getLookupKeyForAsset(String.valueOf(fluPath));
  }

  private boolean checkTokenStatus() {
    if (!hasGotToken) {
      Toast.makeText(activity, "token??????????????????", Toast.LENGTH_LONG).show();
    }
    return hasGotToken;
  }

  /**
   * ?????????ak???sk?????????
   */
  private void initAccessTokenWithAkSk(final MethodCall call, final Result methodResult ) {
    String filePath = flutterToPath(call.argument("filePath"));
    OCR.getInstance(activity).initAccessToken(new OnResultListener<AccessToken>() {
      @Override
      public void onResult(AccessToken result) {
        String token = result.getAccessToken();
        hasGotToken = true;
        JSONObject resultData = new JSONObject();
        resultData.put("msg", "???????????????");
        resultData.put("msg", true);
        eventResult.success(resultData);
        Log.d("BdOcrPlugin-initSDK ??????", token);
      }
      @Override
      public void onError(OCRError error) {
        error.printStackTrace();
        Log.d("BdOcrPlugin-AK,SK??????????????????", error.getMessage());
      }
    }, filePath, activity);
  }

  // ????????????????????????
  private void localIdcardOCROnline(String type, int reCode) {
    Intent intent = new Intent(activity, CameraActivity.class);
    intent.putExtra(CameraEnum.KEY_OUTPUT_FILE_PATH, FileUtil.getSaveFile(activity).getAbsolutePath());
    intent.putExtra(CameraEnum.KEY_NATIVE_ENABLE,true);
    // KEY_NATIVE_MANUAL???????????????CameraActivity???????????????????????????????????????
    // ???????????????CameraNativeHelper????????????????????????
    // ????????????????????????????????????activity?????????????????????????????????
    intent.putExtra(CameraEnum.KEY_NATIVE_MANUAL,true);
    intent.putExtra(CameraEnum.KEY_CONTENT_TYPE, type);
    intent.putExtra(CameraEnum.REQUEST_CODE, reCode);
    activity.startActivityForResult(intent, reCode);
  }

  /**
   * ????????????????????????
   * @param type ??????
   * @param reCode ????????????
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
    // ????????????????????????
    param.setIdCardSide(idCardSide);
    // ??????????????????
    param.setDetectDirection(true);
    // ??????????????????????????????0-100, ??????????????????????????????????????????????????? ????????????????????????20
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
        JSONObject resultError = new JSONObject();
        resultError.put("msg", "????????????");
        eventResult.success(resultError);
      }
    });
  }

  // ????????????
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
//            if (permissions.length != 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {//??????
//              Toast.makeText(activity, "????????????????????????", Toast.LENGTH_SHORT).show();
//            } else {//??????
//              //????????????VIN?????????????????????
//              Intent intent = new Intent(activity, ScanVinActivity.class);
//              activity.startActivityForResult(intent, VIN_RECOG_CODE);
//            }
//            break;
//          case IMPORT_PERMISSION_CODE:
//            if (permissions.length != 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {//??????
//              Toast.makeText(activity, "????????????????????????", Toast.LENGTH_SHORT).show();
//            } else {//??????
//              //????????????VIN?????????????????????
//              Intent intent = new Intent(activity, VinRecogActivity.class);
//              activity.startActivityForResult(intent, VIN_RECOG_CODE);
//            }
//            break;
//        }
        return false;
      }
    });
    // ??????ActivityResult??????
    binding.addActivityResultListener(new PluginRegistry.ActivityResultListener() {
      @Override
      public boolean onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
          assert data != null;
          String filePath = FileUtil.getSaveFile(activity.getApplicationContext()).getAbsolutePath();
          String filePathFront = getRealPathFromURI(data.getStringExtra(CameraEnum.FILE_PATH));
          switch (requestCode) {
            case CameraEnum.REQUEST_CODE_GENERAL_BASIC: // ???????????????????????????????????????
              RecognizeService.recGeneralBasic(activity, filePath, new RecognizeService.ServiceListener() {
                  @Override
                  public void onResult(String result) {
                    eventResult.success(result);
                  }
              });
              break;
            case CameraEnum.REQUEST_CODE_ACCURATE: // ???????????????????????????????????????
              RecognizeService.recAccurate(activity, filePath, new RecognizeService.ServiceListener() {
                  @Override
                  public void onResult(String result) {
                    eventResult.success(result);
                  }
              });
              break;
            case CameraEnum.REQUEST_CODE_GENERAL: // ????????????????????????????????????????????????????????????
              RecognizeService.recGeneral(activity, filePath, new RecognizeService.ServiceListener() {
                  @Override
                  public void onResult(String result) {
                    eventResult.success(result);
                  }
              });
              break;
            case CameraEnum.REQUEST_CODE_ACCURATE_BASIC: // ?????????????????????????????????????????????????????????
              RecognizeService.recAccurateBasic(activity, filePath, new RecognizeService.ServiceListener() {
                @Override
                public void onResult(String result) {
                  eventResult.success(result);
                }
              });
              break;
            case CameraEnum.REQUEST_CODE_GENERAL_ENHANCED: // ????????????????????????????????????????????????????????????
              RecognizeService.recGeneralEnhanced(activity, filePath, new RecognizeService.ServiceListener() {
                @Override
                public void onResult(String result) {
                  eventResult.success(result);
                }
              });
              break;
            case CameraEnum.REQUEST_CODE_GENERAL_WEBIMAGE: // ?????????????????????????????????????????????
              RecognizeService.recWebimage(activity, filePath, new RecognizeService.ServiceListener() {
                @Override
                public void onResult(String result) {
                  eventResult.success(result);
                }
              });
              break;
            case CameraEnum.REQUEST_CODE_VEHICLE_LICENSE: // ????????????????????????????????????
              RecognizeService.recVehicleLicense(activity, filePath, new RecognizeService.ServiceListener() {
                @Override
                public void onResult(String result) {
                  eventResult.success(result);
                }
              });
              break;
            case CameraEnum.REQUEST_CODE_DRIVING_LICENSE: // ????????????????????????????????????
              RecognizeService.recDrivingLicense(activity, filePath, new RecognizeService.ServiceListener() {
                @Override
                public void onResult(String result) {
                  eventResult.success(result);
                }
              });
              break;
            case CameraEnum.REQUEST_CODE_LICENSE_PLATE: // ?????????????????????????????????
              RecognizeService.recLicensePlate(activity, filePath, new RecognizeService.ServiceListener() {
                @Override
                public void onResult(String result) {
                  eventResult.success(result);
                }
              });
              break;
            case CameraEnum.REQUEST_CODE_BUSINESS_LICENSE: // ???????????????????????????????????????
              RecognizeService.recBusinessLicense(activity, filePath, new RecognizeService.ServiceListener() {
                @Override
                public void onResult(String result) {
                  eventResult.success(result);
                }
              });
              break;
            case CameraEnum.REQUEST_CODE_RECEIPT: // ???????????????????????????????????????
              RecognizeService.recReceipt(activity, filePath, new RecognizeService.ServiceListener() {
                @Override
                public void onResult(String result) {
                  eventResult.success(result);
                }
              });
              break;
            case CameraEnum.REQUEST_CODE_TAXIRECEIPT: // ?????????????????????????????????
              RecognizeService.recTaxireceipt(activity, filePath, new RecognizeService.ServiceListener() {
                @Override
                public void onResult(String result) {
                  eventResult.success(result);
                }
              });
              break;
            case CameraEnum.REQUEST_CODE_VINCODE: // ?????????????????????VIN???
              RecognizeService.recVincode(activity, filePath, new RecognizeService.ServiceListener() {
                @Override
                public void onResult(String result) {
                  eventResult.success(result);
                }
              });
              break;
            case CameraEnum.REQUEST_CODE_TRAINTICKET: // ??????????????????????????????
              RecognizeService.recTrainticket(activity, filePath, new RecognizeService.ServiceListener() {
                @Override
                public void onResult(String result) {
                  eventResult.success(result);
                }
              });
              break;
            case CameraEnum.REQUEST_CODE_PASSPORT: // ???????????????????????????
              RecognizeService.recPassport(activity, filePath, new RecognizeService.ServiceListener() {
                @Override
                public void onResult(String result) {
                  eventResult.success(result);
                }
              });
              break;
            case CameraEnum.REQUEST_CODE_QRCODE: // ??????????????????????????????
              RecognizeService.recQrcode(activity, filePath, new RecognizeService.ServiceListener() {
                @Override
                public void onResult(String result) {
                  eventResult.success(result);
                }
              });
              break;
            case CameraEnum.REQUEST_CODE_LOTTERY: // ???????????????????????????
              RecognizeService.recLottery(activity, filePath, new RecognizeService.ServiceListener() {
                @Override
                public void onResult(String result) {
                  eventResult.success(result);
                }
              });
              break;
            case CameraEnum.REQUEST_CODE_VATINVOICE: // ????????????????????????????????????
              RecognizeService.recVatInvoice(activity, filePath, new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                          eventResult.success(result);
                        }
                      });
              break;
            case CameraEnum.REQUEST_CODE_NUMBERS: // ???????????????????????????
              RecognizeService.recNumbers(activity, filePath, new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                          eventResult.success(result);
                        }
                      });
              break;
            case CameraEnum.REQUEST_CODE_HANDWRITING: // ???????????????????????????
              RecognizeService.recHandwriting(activity, filePath, new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                          eventResult.success(result);
                        }
                      });
              break;
            case CameraEnum.REQUEST_CODE_BUSINESSCARD: // ???????????????????????????
              RecognizeService.recBusinessCard(activity, filePath, new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                          eventResult.success(result);
                        }
                      });
              break;
            case CameraEnum.REQUEST_CODE_CUSTOM: // ????????????????????????????????????
              RecognizeService.recCustom(activity, filePath, new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                          eventResult.success(result);
                        }
                      });
              break;
            case CameraEnum.REQUEST_CODE_TRIP_TICKET: // ????????????????????????????????????
              RecognizeService.recTripTicket(activity, filePath, new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                          eventResult.success(result);
                        }
                      });
              break;
            case CameraEnum.REQUEST_CODE_CAR_SELL_INVOICE: // ??????????????????????????????????????????
              RecognizeService.recCarSellInvoice(activity, filePath, new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                          eventResult.success(result);
                        }
                      });
              break;
            case CameraEnum.REQUEST_CODE_VIHICLE_SERTIFICATION: // ????????????????????????????????????
              RecognizeService.recVihicleCertification(activity, filePath, new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                          eventResult.success(result);
                        }
                      });
              break;
            case CameraEnum.REQUEST_CODE_EXAMPLE_DOC_REG: // ??????????????????????????????????????????
              RecognizeService.recExampleDoc(activity, filePath, new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                          eventResult.success(result);
                        }
                      });
              break;
            case CameraEnum.REQUEST_CODE_WRITTEN_TEXT: // ???????????????????????????????????????
              RecognizeService.recWrittenText(activity, filePath, new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                          eventResult.success(result);
                        }
                      });
              break;
            case CameraEnum.REQUEST_CODE_HUKOU_PAGE: // ????????????????????????????????????
              RecognizeService.recHuKouPage(activity, filePath, new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                          eventResult.success(result);
                        }
                      });
              break;
            case CameraEnum.REQUEST_CODE_NORMAL_MACHINE_INVOICE: // ?????????????????????????????????????????????
              RecognizeService.recNormalMachineInvoice(activity, filePath, new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                          eventResult.success(result);
                        }
                      });
              break;
            case CameraEnum.REQUEST_CODE_WEIGHT_NOTE: // ?????????????????????????????????
              RecognizeService.recweightnote(activity, filePath, new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                          eventResult.success(result);
                        }
                      });
              break;
            case CameraEnum.REQUEST_CODE_MEDICAL_DETAIL: // ?????????????????????????????????????????????
              RecognizeService.recmedicaldetail(activity, filePath, new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                          eventResult.success(result);
                        }
                      });
              break;
            case CameraEnum.REQUEST_CODE_ONLINE_TAXI_ITINERARY: // ?????????????????????????????????????????????
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
                  JSONObject resultError = new JSONObject();
                  resultError.put("msg", "????????????");
                  eventResult.success(resultError);
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
