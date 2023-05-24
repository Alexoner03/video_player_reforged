import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'video_player_reforged_platform_interface.dart';

/// An implementation of [VideoPlayerReforgedPlatform] that uses method channels.
class MethodChannelVideoPlayerReforged extends VideoPlayerReforgedPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('video_player_reforged');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
