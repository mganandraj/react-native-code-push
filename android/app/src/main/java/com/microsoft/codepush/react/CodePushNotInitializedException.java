package com.microsoft.codepush.react;

import android.annotation.SuppressLint;

@SuppressLint({"HardwareIds", "ObsoleteSdkInt", "StaticFieldLeak", "LogConditional",
        "KotlinPropertyAccess", "LambdaLast", "UnknownNullness"})

public final class CodePushNotInitializedException extends RuntimeException {

    public CodePushNotInitializedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CodePushNotInitializedException(String message) {
        super(message);
    }
}