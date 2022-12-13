import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:http/http.dart' as http;
import 'package:web_socket_channel/web_socket_channel.dart';

class HomePage extends StatefulWidget {
  const HomePage({Key? key}) : super(key: key);

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  final form = GlobalKey<FormState>();
  late TextEditingController controller;
  final url = Uri.http('localhost', '/api/votes');
  Votes votes = Votes();
  bool fetchedBefore = false;
  final channel =
      WebSocketChannel.connect(Uri.parse('ws://localhost/events/votes'));

  void fetchVotes() async {
    Map<String, int> data =
        Map<String, int>.from(jsonDecode((await http.get(url)).body));
    setState(() {
      votes = Votes.fromJson(data);
    });
  }

  @override
  void initState() {
    super.initState();
    controller = TextEditingController();
    fetchVotes();
    channel.stream.listen((event) {
      fetchVotes();
    });
  }

  @override
  void dispose() {
    controller.dispose();
    channel.sink.close();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Center(child: Text('Yest/No Vote')),
      ),
      body: SafeArea(
        child: ListView.builder(
          itemCount: 1,
          itemBuilder: (_, int index) {
            return Column(
              children: [
                const SizedBox(height: 15),
                Form(
                  key: form,
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      OutlinedButton(
                        style: OutlinedButton.styleFrom(
                            padding: const EdgeInsets.all(25)),
                        onPressed: () {
                          if (form.currentState!.validate()) {
                            http.post(
                              url,
                              headers: <String, String>{
                                'Content-Type':
                                'application/json; charset=UTF-8',
                              },
                              body: jsonEncode(['yes']),
                            );
                          }
                        },
                        child: const Text("Yes"),
                      ),
                      const SizedBox(width: 15),
                      OutlinedButton(
                        style: OutlinedButton.styleFrom(
                            padding: const EdgeInsets.all(25)),
                        onPressed: () {
                          if (form.currentState!.validate()) {
                            http.post(
                              url,
                              headers: <String, String>{
                                'Content-Type':
                                'application/json; charset=UTF-8',
                              },
                              body: jsonEncode(['no']),
                            );
                          }
                        },
                        child: const Text("No"),
                      ),
                    ],
                  ),
                ),
                const SizedBox(height: 15),
                const Text(
                  "Yes votes:",
                  style: TextStyle(fontWeight: FontWeight.bold),
                ),
                Text(votes.yes.toString()),
                const SizedBox(height: 15),
                const Text(
                  "No votes:",
                  style: TextStyle(fontWeight: FontWeight.bold),
                ),
                const SizedBox(height: 5),
                Text(votes.no.toString()),
              ],
            );
          },
        ),
      ),
    );
  }
}

class Votes {
  final int yes;
  final int no;

  Votes({this.yes = 0, this.no = 0});

  Votes.fromJson(Map<String, int> json)
      : yes = json['yes'] ?? 0,
        no = json['no'] ?? 0;

  Map<String, int> toJson() => {
        'yes': yes,
        'no': no,
      };
}
