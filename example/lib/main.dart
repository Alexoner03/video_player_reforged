import 'package:flutter/material.dart';
import 'package:video_player_reforged/views/video_view.dart';

void main() => runApp(const MaterialApp(home: VideoViewExample()));

class VideoViewExample extends StatefulWidget {
  const VideoViewExample({Key? key}) : super(key: key);

  @override
  State<VideoViewExample> createState() => _VideoViewExampleState();
}

class _VideoViewExampleState extends State<VideoViewExample> {
  late final TextEditingController _urlController;
  late final VideoViewController _videoViewController;

  @override
  void initState() {
    _urlController = TextEditingController(text: 'https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4');
    super.initState();
  }

  @override
  void dispose() {
    _urlController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Flutter VideView example')),
      body: Column(
        children: [
          TextFormField(
            controller: _urlController,
          ),
          ElevatedButton(
            onPressed: () =>
                _videoViewController.setUrl(url: _urlController.text, type: "MP4"),
            child: const Text('CHANGE TO MP4'),
          ),
          ElevatedButton(
            onPressed: () =>
                _videoViewController.play(),
            child: const Text('Play'),
          ),

          ElevatedButton(
            onPressed: () =>
                _videoViewController.pause(),
            child: const Text('Pause'),
          ),
          ElevatedButton(
            onPressed: () =>
                _videoViewController.setUrl(url: "https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.ism/.m3u8", type: "M3U8"),
            child: const Text('Change to M3U8'),
          ),
          ElevatedButton(
            onPressed: () =>
                _videoViewController.setUrl(url: "srt://virginia2.trapemn.tv:49004", type: "SRT"),
            child: const Text('Change to SRT'),
          ),
          ElevatedButton(
            onPressed: () =>
                _videoViewController.setUrl(url: "rtmp://virginia1.trapemn.tv:31935/live/test2", type: "RTMP"),
            child: const Text('Change to RTMP'),
          ),
          SizedBox(
            height: 300,
            child: VideoView(
              onVideoViewCreated: _onVideoViewCreated,
            ),
          ),
        ],
      ),
    );
  }

  // load default
  void _onVideoViewCreated(VideoViewController controller) {
    _videoViewController = controller;
    controller.setUrl(url: _urlController.text, type: "MP4");
  }
}