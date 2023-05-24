import 'package:flutter_test/flutter_test.dart';
import 'package:video_player_reforged/video_player_reforged.dart';
import 'package:video_player_reforged/video_player_reforged_platform_interface.dart';
import 'package:video_player_reforged/video_player_reforged_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockVideoPlayerReforgedPlatform
    with MockPlatformInterfaceMixin
    implements VideoPlayerReforgedPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final VideoPlayerReforgedPlatform initialPlatform = VideoPlayerReforgedPlatform.instance;

  test('$MethodChannelVideoPlayerReforged is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelVideoPlayerReforged>());
  });

  test('getPlatformVersion', () async {
    VideoPlayerReforged videoPlayerReforgedPlugin = VideoPlayerReforged();
    MockVideoPlayerReforgedPlatform fakePlatform = MockVideoPlayerReforgedPlatform();
    VideoPlayerReforgedPlatform.instance = fakePlatform;

    expect(await videoPlayerReforgedPlugin.getPlatformVersion(), '42');
  });
}
