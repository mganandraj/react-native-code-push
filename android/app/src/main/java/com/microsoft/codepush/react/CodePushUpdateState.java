package com.microsoft.codepush.react;

import android.annotation.SuppressLint;

@SuppressLint({"HardwareIds", "ObsoleteSdkInt", "StaticFieldLeak", "LogConditional",
        "KotlinPropertyAccess", "LambdaLast", "UnknownNullness"})

public enum CodePushUpdateState {
    RUNNING(0),
    PENDING(1),
    LATEST(2);

    private final int value;
    CodePushUpdateState(int value) {
        this.value = value;
    }
    public int getValue() {
        return this.value;
    }
}