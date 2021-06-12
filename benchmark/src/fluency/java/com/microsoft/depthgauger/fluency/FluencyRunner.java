package com.microsoft.depthgauger.fluency;

import android.content.Context;

import com.microsoft.depthgauger.BaseRunner;
import com.microsoft.depthgauger.memory.MemoryStats;
import com.microsoft.depthgauger.utils.Stats;

import com.touchtype_fluency.ModelSetDescription;
import com.touchtype_fluency.ResultsFilter;
import com.touchtype_fluency.Session;
import com.touchtype_fluency.Sequence;
import com.touchtype_fluency.SwiftKeySDK;
import com.touchtype_fluency.TouchHistory;

import java.util.Map;

public class FluencyRunner extends BaseRunner<FluencyConfig> {
    private static final String LICENSE_KEY = "";

    private Session session;
    private ModelSetDescription msd;

    public FluencyRunner(Context context, FluencyConfig config) throws Exception {
        super(context, config);
        session = SwiftKeySDK.createSession(LICENSE_KEY);
        msd = ModelSetDescription.dynamicTemporary(4, new String[] {});
    }

    @Override
    public void unloadModel() throws Exception {
        session.unload(msd);
    }

    @Override
    public void loadModel(Stats stats, MemoryStats baselineMemoryStats) throws Exception {
        unloadModel();
        final long startLoadingTimeMs = System.currentTimeMillis();
        session.load(msd);
        final long loadTimeMs = System.currentTimeMillis() - startLoadingTimeMs;
        stats.appendTimeMs(loadTimeMs);
        stats.appendMemoryStats(baselineMemoryStats);
    }

    @Override
    public void call(Stats stats, MemoryStats baselineMemoryStats) throws Exception {
        final long startCallTimeMs = System.currentTimeMillis();
        session.getPredictor().getPredictions(
                new Sequence(),
                new TouchHistory(),
                new ResultsFilter(1));
        final long callTimeMs = System.currentTimeMillis() - startCallTimeMs;
        stats.appendTimeMs(callTimeMs);
        stats.appendMemoryStats(baselineMemoryStats);
    }
}
