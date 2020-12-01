package com.microsoft.codepush.react;

import android.annotation.SuppressLint;

@SuppressLint({"HardwareIds", "ObsoleteSdkInt", "StaticFieldLeak", "LogConditional",
        "KotlinPropertyAccess", "LambdaLast", "UnknownNullness"})

interface DownloadProgressCallback {
    void call(DownloadProgress downloadProgress);
}
