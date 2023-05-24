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
    _urlController = TextEditingController(text: 'https://flutter.dev/');
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
      appBar: AppBar(title: const Text('Flutter WebView example')),
      body: Column(
        children: [
          TextFormField(
            controller: _urlController,
          ),
          ElevatedButton(
            onPressed: () =>
                _videoViewController.setUrl(url: _urlController.text),
            child: const Text('Load Url'),
          ),
          Expanded(
            child: VideoView(
              onMapViewCreated: _onMapViewCreated,
            ),
          ),
        ],
      ),
    );
  }

  // load default
  void _onMapViewCreated(VideoViewController controller) {
    _videoViewController = controller;
    controller.setUrl(url: _urlController.text);
  }
}