package com.microsoft.codepush.react;

import android.annotation.SuppressLint;

import com.facebook.react.ReactInstanceManager;

/**
 * Provides access to a {@link ReactInstanceManager}.
 *
 * ReactNativeHost already implements this interface, if you make use of that react-native
 * component (just add `implements ReactInstanceHolder`).
 *
 */
@SuppressLint({"HardwareIds", "ObsoleteSdkInt", "StaticFieldLeak", "LogConditional",
        "KotlinPropertyAccess", "LambdaLast", "UnknownNullness"})

public interface ReactInstanceHolder {

  /**
   * Get the current {@link ReactInstanceManager} instance. May return null.
   */
  ReactInstanceManager getReactInstanceManager();
}
