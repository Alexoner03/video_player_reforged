import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'video_player_reforged_method_channel.dart';

abstract class VideoPlayerReforgedPlatform extends PlatformInterface {
  /// Constructs a VideoPlayerReforgedPlatform.
  VideoPlayerReforgedPlatform() : super(token: _token);

  static final Object _token = Object();

  static VideoPlayerReforgedPlatform _instance = MethodChannelVideoPlayerReforged();

  /// The default instance of [VideoPlayerReforgedPlatform] to use.
  ///
  /// Defaults to [MethodChannelVideoPlayerReforged].
  static VideoPlayerReforgedPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [VideoPlayerReforgedPlatform] when
  /// they register themselves.
  static set instance(VideoPlayerReforgedPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
