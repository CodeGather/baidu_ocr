#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html.
# Run `pod lib lint baidu_ocr.podspec` to validate before publishing.
#
Pod::Spec.new do |s|
  s.name             = 'baidu_ocr'
  s.version          = '1.0.0'
  s.summary          = '这个一个百度文字识别的flutter插件'
  s.description      = <<-DESC
这个一个百度文字识别的flutter插件
                       DESC
  s.homepage         = 'http://example.com'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'Your Company' => 'email@example.com' }
  s.source           = { :path => '.' }
  s.source_files = 'Classes/**/*'
  s.public_header_files = 'Classes/**/*.h'
  s.dependency 'Flutter'
  s.platform = :ios, '11.0'

  s.vendored_frameworks = 'libs/AipBase.framework', 'libs/AipOcrSdk.framework', 'libs/IdcardQuality.framework'

  # Flutter.framework does not contain a i386 slice.
  s.pod_target_xcconfig = { 'DEFINES_MODULE' => 'YES', 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'i386' }
end
