package com.microsoft.depthgauger.config;

import android.content.Context;

import com.microsoft.depthgauger.parameters.FloatConstantParameter;
import com.microsoft.depthgauger.parameters.FloatTensorConstantParameter;
import com.microsoft.depthgauger.parameters.IntegerTensorConstantParameter;
import com.microsoft.depthgauger.parameters.LongFlatTensorConstantParameter;
import com.microsoft.depthgauger.parameters.Parameter;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.microsoft.depthgauger.utils.IOUtils.readAssetToString;

public class Config {
    public Framework framework;
    public String method_name;
    public String model_filename;
    public List<ConfigParameter> input_parameters;
    public List<ConfigParameter> output_parameters;
    public List<SupplementalFile> supplemental_files;
    public Map<String, Object> properties;

    public static Config fromJson(String configAssetName, Context context) throws IOException {
        final String configJson = readAssetToString(configAssetName, context);
        final Moshi moshi = new Moshi.Builder()
                /*.add(PolymorphicJsonAdapterFactory.of(Parameter.class, "parameter_type")
                        .withSubtype(FloatConstantParameter.class, FloatConstantParameter.class.getSimpleName())
                        .withSubtype(FloatTensorConstantParameter.class, FloatTensorConstantParameter.class.getSimpleName())
                        .withSubtype(IntegerTensorConstantParameter.class, IntegerTensorConstantParameter.class.getSimpleName())
                        .withSubtype(LongFlatTensorConstantParameter.class, LongFlatTensorConstantParameter.class.getSimpleName()))*/
                .build();
        final JsonAdapter<Config> jsonAdapter = moshi.adapter(Config.class);
        return jsonAdapter.fromJson(configJson);
    }
}
