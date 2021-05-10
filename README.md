# DepthGauger

DepthGauger is a benchmarking library for deep learning models which runs on real Android devices,
either locally or attached to a cloud test platform (like 
[Microsoft App Center](https://appcenter.ms/)).

## Build

### Prerequisites

The generated dummy app APK needs to be signed to work with MS App Center. A guide for generating a
signing key can be found [here](https://developer.android.com/studio/publish/app-signing).

Once you have a signing key, you should define `depthgauger_keystore_path`,
`depthgauger_keystore_password`, `depthgauger_keystore_key_alias`, and
`depthgauger_keystore_key_password` in your `~/.gradle/gradle.properties` file:

```
depthgauger_keystore_path=...
depthgauger_keystore_password=...
depthgauger_keystore_key_alias=...
depthgauger_keystore_key_password=...
```

If you're using Windows, remember to escape your slashes in `depthgauger_keystore_path`.

Add a compiled ONNX Runtime AAR file called `onnxruntime-debug.aar` to the `onnxruntime-debug`
directory.

### Choose a framework to test

The deep learning framework you'd like to test is referred to as follows:

* `<LOWERCASE_FRAMEWORK>` is `onnx`, `pytorch`, or `tensorflow`, corresponding to ONNX Runtime,
  PyTorch Mobile and TensorFlow Lite
* `<INTIAL_UPPERCASE_FRAMEWORK>` is `Onnx`, `Pytorch`, or `Tensorflow`
* `<CAMELCASE_FRAMEWORK>` is `Onnx`, `PyTorch`, or `TensorFlow`
* `<UPPERCASE_FRAMEWORK>` is `ONNX`, `PYTORCH`, or `TENSORFLOW`

### Consider using a slim framework dependency

The dependencies for each framework contain a lot of functionality that aren't needed by all models.
Consider using a custom build for each framework in order to produce a much slimmer library:

* [ONNX Runtime](https://github.com/microsoft/onnxruntime/blob/master/docs/ONNX_Runtime_for_Mobile_Platforms.md)
* [PyTorch Mobile](https://pytorch.org/mobile/android/#custom-build)
* [TensorFlow Lite](https://www.tensorflow.org/lite/guide/reduce_binary_size)

### Create a config file for your model

An example config file is shown below:

```
{
    "framework": "PYTORCH",
    "method_name": "train_step",
    "model_filename": "swiftkeyrnn.pt",
    "input_parameters": [
        {
            "name": "data",
            "parameter_type": "LongFlatTensorConstantParameter",
            "shape": [2, 3],
            "values": [1, 2, 3, 4, 5, 6]
        },
        {
            "name": "learning_rate",
            "parameter_type": "FloatConstantParameter",
            "shape": [1],
            "values": [0.01]
        }
    ],
    "output_parameters": [],
    "supplemental_files": [],
    "properties": {}
}
```

* `framework` is one of the values in `<UPPERCASE_FRAMEWORK>`.
* `method_name` is the name of the method to call in the model. If unknown, please use `forward`.
* `model_filename` is the filename of the model for which this config file applies.
* `input_parameters` are passed when calling the method specified in `method_name`. Possible
  `parameter_type` values and the schemas around them can be found in the 
  `com.microsoft.depthgauger.parameters` package. The values for each input parameter are flat
  arrays. They can be rearranged into multiple dimensions via the `shape` property.
* `output_parameters` are similar to `input_parameters`, for the outputs produced when calling the
  method specified in `method_name`.
* `supplemental_files` is currently unused and should be an empty array.
* `properties` is currently unused and should be an empty map.

### Gather your model and config files

The path to your model file is referred to as `<MODEL_FILE_PATH>`. The filename should have a
`.ort`, `.pt`, or `.tflite` extension.

The part to the config file for your model is referred to as `<CONFIG_FILE_PATH>`.

### Assemble dummy app APK

A dummy signed release app APK can be built for uploading to cloud test platforms. To generate a
dummy app APK, run:

`gradlew dummyapp:assembleRelease`

### Assemble benchmark APK

The benchmark release APK contains only the necessary files and dependencies in order to keep the
file size as small as possible. To generate the benchmark release APK, run:

`gradlew clean benchmark:assemble<INTIAL_UPPERCASE_FRAMEWORK>ReleaseAndroidTest -Pmodel_file=<MODEL_FILE_PATH> -Pconfig_file=<CONFIG_FILE_PATH>`

It is strongly recommended to `clean` before each build, especially if changing framework.

## Run

### On local devices

Install the benchmark APK:

`adb install -r benchmark/build/outputs/apk/androidTest/<LOWERCASE_FRAMEWORK>/release/benchmark-<LOWERCASE_FRAMEWORK>-release-androidTest.apk`

Then start the benchmark instrumentation test:

`adb shell am instrument -w -e androidx.benchmark.profiling.mode none -e debug false -e class 'com.microsoft.depthgauger.benchmark.<CAMELCASE_FRAMEWORK>Benchmark' com.microsoft.depthgauger.benchmark.test/androidx.benchmark.junit4.AndroidBenchmarkRunner`

### On Microsoft App Center

Create an Espresso test, follow the
[setup instructions](https://docs.microsoft.com/en-us/appcenter/cli/) for the CLI, ensure that
you're logged in, and then run:

`appcenter test run espresso --app "<APP CENTER PROJECT NAME>" --devices <DEVICES> --app-path <FULL PATH TO DUMMY APP APK FILE> --test-series "master" --locale "en_US" --build-dir <FULL PATH TO BENCHMARK APK DIRECTORY>`

The paths to the dummy app APK file and the benchmark APK directory ***must be absolute***. The
dummy app APK file is at `dummyapp/build/outputs/apk/release/dummyapp-release.apk`, and the
benchmark APK directory is at `benchmark/build/outputs/apk/androidTest/<LOWERCASE_FRAMEWORK>/release`.

## Extracting results

Results are output to the device logs, via both our own test time recorders and the
[Benchmark library](https://developer.android.com/studio/profile/benchmark) too.

## Contributing

This project welcomes contributions and suggestions.  Most contributions require you to agree to a
Contributor License Agreement (CLA) declaring that you have the right to, and actually do, grant us
the rights to use your contribution. For details, visit https://cla.opensource.microsoft.com.

When you submit a pull request, a CLA bot will automatically determine whether you need to provide
a CLA and decorate the PR appropriately (e.g., status check, comment). Simply follow the instructions
provided by the bot. You will only need to do this once across all repos using our CLA.

This project has adopted the [Microsoft Open Source Code of Conduct](https://opensource.microsoft.com/codeofconduct/).
For more information see the [Code of Conduct FAQ](https://opensource.microsoft.com/codeofconduct/faq/) or
contact [opencode@microsoft.com](mailto:opencode@microsoft.com) with any additional questions or comments.

## Trademarks

This project may contain trademarks or logos for projects, products, or services. Authorized use of Microsoft 
trademarks or logos is subject to and must follow 
[Microsoft's Trademark & Brand Guidelines](https://www.microsoft.com/en-us/legal/intellectualproperty/trademarks/usage/general).
Use of Microsoft trademarks or logos in modified versions of this project must not cause confusion or imply Microsoft sponsorship.
Any use of third-party trademarks or logos are subject to those third-party's policies.
