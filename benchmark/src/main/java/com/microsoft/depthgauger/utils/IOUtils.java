package com.microsoft.depthgauger.utils;

import android.content.Context;

import com.google.common.io.ByteStreams;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class IOUtils {
    public static String copyAssetAndGetPath(Context context, String assetName) throws IOException {
        final File file = new File(context.getFilesDir(), assetName);
        if (!file.exists() || (file.length() == 0)) {
            try (InputStream is = context.getAssets().open(assetName)) {
                try (OutputStream os = new FileOutputStream(file)) {
                    byte[] buffer = new byte[4 * 1024];
                    int read;
                    while ((read = is.read(buffer)) != -1) {
                        os.write(buffer, 0, read);
                    }
                    os.flush();
                }
            }
        }
        return file.getAbsolutePath();
    }

    public static String readAssetToString(String configAssetName, Context context)
            throws IOException {
        final StringBuilder out = new StringBuilder();
        try (InputStream is = context.getAssets().open(configAssetName);
             InputStreamReader isr = new InputStreamReader(is);
             BufferedReader br = new BufferedReader(isr)) {
            String line;
            while ((line = br.readLine()) != null) {
                out.append(line);
            }
        }
        return out.toString();
    }

    public static byte[] readFileToBytes(String filePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            return ByteStreams.toByteArray(fis);
        }
    }
}
