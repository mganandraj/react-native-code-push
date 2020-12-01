package com.microsoft.codepush.react;

import android.annotation.SuppressLint;

@SuppressLint({"HardwareIds", "ObsoleteSdkInt", "StaticFieldLeak", "LogConditional",
        "KotlinPropertyAccess", "LambdaLast", "UnknownNullness"})

class CodePushInvalidPublicKeyException extends RuntimeException {

    public CodePushInvalidPublicKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public CodePushInvalidPublicKeyException(String message) {
        super(message);
    }
}