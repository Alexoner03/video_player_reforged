
import 'video_player_reforged_platform_interface.dart';

class VideoPlayerReforged {
  Future<String?> getPlatformVersion() {
    return VideoPlayerReforgedPlatform.instance.getPlatformVersion();
  }
}
