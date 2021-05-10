package com.microsoft.depthgauger;

import android.content.Context;

import com.microsoft.depthgauger.utils.TimeStats;

import java.io.IOException;

import static com.microsoft.depthgauger.utils.IOUtils.copyAssetAndGetPath;

public abstract class BaseRunner<T extends BaseConfig> {
    protected final String modelPath;
    protected final T config;

    public BaseRunner(Context context, T config) throws IOException {
        this.modelPath = copyAssetAndGetPath(context, config.getModelAssetName());
        this.config = config;
    }

    public abstract void unloadModel() throws Exception;

    public abstract void loadModel(TimeStats timeStats) throws Exception;

    public abstract void call(TimeStats timeStats) throws Exception;
}
