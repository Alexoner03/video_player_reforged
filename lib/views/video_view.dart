import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

typedef FlutterWebViewCreatedCallback = void Function(
    VideoViewController controller);

class VideoView extends StatelessWidget {
  final FlutterWebViewCreatedCallback onVideoViewCreated;
  const VideoView({Key? key, required this.onVideoViewCreated}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    switch (defaultTargetPlatform) {
      case TargetPlatform.android:
        return AndroidView(
          viewType: 'video_player_view/flutter_video_view',
          onPlatformViewCreated: _onPlatformViewCreated,
        );
      case TargetPlatform.iOS:
        return UiKitView(
          viewType: 'video_player_view/flutter_video_view',
          onPlatformViewCreated: _onPlatformViewCreated,
        );
      default:
        return Text(
            '$defaultTargetPlatform is not yet supported by the web_view plugin');
    }
  }

  // Callback method when platform view is created
  void _onPlatformViewCreated(int id) =>
      onVideoViewCreated(VideoViewController._(id));
}

// WebView Controller class to set url etc
class VideoViewController {
  VideoViewController._(int id)
      : _channel =
  MethodChannel('video_player_view/flutter_video_view_$id');

  final MethodChannel _channel;

  Future<void> setUrl({required String url, required String type}) async {
    return _channel.invokeMethod('setUrl', "${url}|||$type");
  }

  Future<void> play() async {
    return _channel.invokeMethod('play');
  }

  Future<void> pause() async {
    return _channel.invokeMethod('pause');
  }
}