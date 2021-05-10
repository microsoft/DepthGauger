package com.microsoft.depthgauger;

import android.content.Context;

import com.google.common.collect.ImmutableList;

import com.microsoft.depthgauger.benchmark.R;
import com.microsoft.depthgauger.config.Config;
import com.microsoft.depthgauger.config.ConfigParameter;
import com.microsoft.depthgauger.parameters.Parameter;

import java.io.IOException;
import java.util.List;

public abstract class BaseConfig {
    private final String methodName;
    private final String modelAssetName;
    protected final List<Parameter> inputParameters;
    protected final List<Parameter> outputParameters;

    public BaseConfig(Context context) throws IOException {
        final Config config = Config.fromJson(context.getResources().getString(R.string.configFileName), context);
        this.methodName = config.method_name;
        this.modelAssetName = config.model_filename;
        final ImmutableList.Builder<Parameter> inputParametersBuilder = ImmutableList.builder();
        for (ConfigParameter configParameter : config.input_parameters) {
            inputParametersBuilder.add(configParameter.extractParameter());
        }
        this.inputParameters = inputParametersBuilder.build();
        final ImmutableList.Builder<Parameter> outputParametersBuilder = ImmutableList.builder();
        for (ConfigParameter configParameter : config.output_parameters) {
            outputParametersBuilder.add(configParameter.extractParameter());
        }
        this.outputParameters = outputParametersBuilder.build();
    }

    public String getMethodName() {
        return methodName;
    }

    public String getModelAssetName() {
        return modelAssetName;
    }

    public List<Parameter> getInputParameters() {
        return inputParameters;
    }
}
