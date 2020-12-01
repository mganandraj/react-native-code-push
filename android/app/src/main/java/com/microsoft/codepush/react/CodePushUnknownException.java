package com.microsoft.codepush.react;

import android.annotation.SuppressLint;

@SuppressLint({"HardwareIds", "ObsoleteSdkInt", "StaticFieldLeak", "LogConditional",
        "KotlinPropertyAccess", "LambdaLast", "UnknownNullness"})

class CodePushUnknownException extends RuntimeException {

    public CodePushUnknownException(String message, Throwable cause) {
        super(message, cause);
    }

    public CodePushUnknownException(String message) {
        super(message);
    }
}