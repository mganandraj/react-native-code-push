package com.microsoft.codepush.react;

import android.annotation.SuppressLint;

@SuppressLint({"HardwareIds", "ObsoleteSdkInt", "StaticFieldLeak", "LogConditional",
        "KotlinPropertyAccess", "LambdaLast", "UnknownNullness"})

public class CodePushInvalidUpdateException extends RuntimeException {
    public CodePushInvalidUpdateException(String message) {
        super(message);
    }
}
