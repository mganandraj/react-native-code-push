package com.microsoft.codepush.react;

import android.annotation.SuppressLint;
import android.content.Context;

@SuppressLint({"HardwareIds", "ObsoleteSdkInt", "StaticFieldLeak", "LogConditional",
        "KotlinPropertyAccess", "LambdaLast", "UnknownNullness"})
public class CodePushBuilder {
    private String mDeploymentKey;
    private Context mContext;

    private boolean mIsDebugMode;
    private String mServerUrl;
    private Integer mPublicKeyResourceDescriptor;

    public CodePushBuilder(String deploymentKey, Context context) {
        this.mDeploymentKey = deploymentKey;
        this.mContext = context;
        this.mServerUrl = CodePush.getServiceUrl();
    }

    public CodePushBuilder setIsDebugMode(boolean isDebugMode) {
        this.mIsDebugMode = isDebugMode;
        return this;
    }

    public CodePushBuilder setServerUrl(String serverUrl) {
        this.mServerUrl = serverUrl;
        return this;
    }

    public CodePushBuilder setPublicKeyResourceDescriptor(int publicKeyResourceDescriptor) {
        this.mPublicKeyResourceDescriptor = publicKeyResourceDescriptor;
        return this;
    }

    public CodePush build() {
        return new CodePush(this.mDeploymentKey, this.mContext, this.mIsDebugMode, this.mServerUrl, this.mPublicKeyResourceDescriptor);
    }
}
