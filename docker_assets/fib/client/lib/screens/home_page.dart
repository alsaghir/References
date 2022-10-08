import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:http/http.dart' as http;

class HomePage extends StatefulWidget {
  const HomePage({Key? key}) : super(key: key);

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  final form = GlobalKey<FormState>();
  late TextEditingController controller;
  final url = Uri.http('localhost', '/api/values');
  final List<Widget> calculatedValues = List.empty(growable: true);
  String seenIndexes = "";
  Map<String, String>? values;

  void fetchValues() async {
    var fetchUrl = url.resolve('/api/values/current');
    Map<String, String> data =
        Map<String, String>.from(jsonDecode((await http.get(fetchUrl)).body));
    setState(() {
      values = data;
      values!.forEach((key, value) {
        calculatedValues.add(Text("For index $key I calculated $value"));
      });
    });
  }

  void fetchIndexes() async {
    var fetchUrl = url.resolve('/api/values/all');
    String data = List<String>.from(jsonDecode((await http.get(fetchUrl)).body))
        .join(", ");
    setState(() {
      seenIndexes = data;
    });
  }

  @override
  void initState() {
    super.initState();
    controller = TextEditingController();
    fetchValues();
    fetchIndexes();
  }

  @override
  void dispose() {
    controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Center(child: Text('Fib Calculator')),
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
                      SizedBox(
                        width: 250,
                        child: TextFormField(
                          controller: controller,
                          inputFormatters: <TextInputFormatter>[
                            FilteringTextInputFormatter.digitsOnly
                          ],
                          keyboardType: TextInputType.number,
                          validator: (text) => text != null && text.isEmpty
                              ? "Value Can't Be Empty"
                              : null,
                          decoration: const InputDecoration(
                            contentPadding: EdgeInsets.all(20),
                            isDense: true,
                            icon: Icon(Icons.numbers),
                            border: OutlineInputBorder(),
                            labelText: 'Enter your index',
                          ),
                        ),
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
                              body: jsonEncode(<String, String>{
                                'index': controller.text,
                              }),
                            );
                          }
                        },
                        child: const Text("Submit"),
                      ),
                    ],
                  ),
                ),
                const SizedBox(height: 15),
                const Text(
                  "Indices I have seen:",
                  style: TextStyle(fontWeight: FontWeight.bold),
                ),
                Text(seenIndexes),
                const SizedBox(height: 15),
                const Text(
                  "Calculated Values:",
                  style: TextStyle(fontWeight: FontWeight.bold),
                ),
                const SizedBox(height: 5),
                ...calculatedValues,
              ],
            );
          },
        ),
      ),
    );
  }
}
