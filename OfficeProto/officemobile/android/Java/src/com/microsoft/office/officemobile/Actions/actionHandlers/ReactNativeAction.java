package com.microsoft.office.officemobile.Actions.actionHandlers;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.common.logging.LoggingDelegate;
import com.facebook.react.BuildConfig;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactInstanceManagerBuilder;
import com.facebook.react.ReactPackage;
import com.facebook.react.ReactRootView;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.devsupport.RedBoxHandler;
import com.facebook.react.shell.MainReactPackage;
import com.microsoft.codepush.react.CodePush;
import com.microsoft.office.apphost.OfficeActivityHolder;
import com.microsoft.office.apphost.OfficeApplication;
import com.microsoft.office.diagnosticsapi.Diagnostics;
import com.microsoft.office.docsui.common.CoauthGalleryController;
import com.microsoft.office.docsui.common.DrillInDialog;
import com.microsoft.office.identity.IdentityMetaData;
import com.microsoft.office.livepersona.container.IPeopleCardContainer;
import com.microsoft.office.livepersona.container.PeopleCardDrillInDialogContainer;
import com.microsoft.office.livepersona.control.PeopleCard;
import com.microsoft.office.loggingapi.Category;
import com.microsoft.office.officemobile.ActionsTab.IActionHandler;

import org.jetbrains.annotations.NotNull;
import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.List;
import com.microsoft.codepush.react.CodePush;
import com.microsoft.office.ui.controls.widgets.OfficeLinearLayout;

public class ReactNativeAction implements IActionHandler {

    @SuppressLint("staticfieldleak")
    private static DrillInDialog mHostDrillInDialog;


    private static class ReactInstanceManagerInstance {
        @SuppressLint("staticfieldleak")
        public static ReactInstanceManager instanceManager = null;
        @SuppressLint("staticfieldleak")
        private static CodePush codePush = null;




        private static List<ReactPackage> getPackages()
        {
            ArrayList<ReactPackage> packages = new ArrayList<>();
            packages.add(new MainReactPackage());
            //packages.add(new PolyesterReactPackage());
            //if (mReactPackageList != null && !mReactPackageList.isEmpty())
            //{
            //    packages.addAll(mReactPackageList);
            //}

            //ArrayList<ReactPackage> packages = new ArrayList<>();
            //packages.add(new MainReactPackage());
            //packages.add(codePush);

            return packages;
        }

        public static ReactInstanceManager GetManager() {
            if (instanceManager == null) {
                ReactInstanceManagerBuilder builder = ReactInstanceManager.builder()
                        .setApplication(OfficeApplication.Get())
                        .setJSMainModulePath("index")
                        .setUseDeveloperSupport(true)
                        .setRedBoxHandler(null)
                        .setInitialLifecycleState(LifecycleState.RESUMED)
                        .setCurrentActivity(OfficeActivityHolder.GetActivity());

                //CodePush.getJSBundleFile();
                codePush = new CodePush("s-jY9tQjk0NxyiawgasYpO_2hG1fRy-1nf2DS",
                        OfficeApplication.Get(),
                        BuildConfig.DEBUG);

                builder.setJSBundleFile(CodePush.getJSBundleFile("index.android.bundle"));


                com.facebook.common.logging.FLog.setLoggingDelegate(new LoggingDelegate() {
                    @Override
                    public void setMinimumLoggingLevel(int i) {

                    }

                    @Override
                    public int getMinimumLoggingLevel() {
                        return 0;
                    }

                    @Override
                    public boolean isLoggable(int i) {
                        return false;
                    }

                    @Override
                    public void v(String s, String s1) {
                        Log.e("ReactNative", s + "-" + s1);
                    }

                    @Override
                    public void v(String s, String s1, Throwable throwable) {
                        Log.e("ReactNative", s + "-" + s1 + "-" + throwable);
                    }

                    @Override
                    public void d(String s, String s1) {
                        Log.e("ReactNative", s + "-" + s1);
                    }

                    @Override
                    public void d(String s, String s1, Throwable throwable) {
                        Log.e("ReactNative", s + "-" + s1);
                    }

                    @Override
                    public void i(String s, String s1) {
                        Log.e("ReactNative", s + "-" + s1);
                    }

                    @Override
                    public void i(String s, String s1, Throwable throwable) {
                        Log.e("ReactNative", s + "-" + s1);
                    }

                    @Override
                    public void w(String s, String s1) {
                        Log.e("ReactNative", s + "-" + s1);
                    }

                    @Override
                    public void w(String s, String s1, Throwable throwable) {
                        Log.e("ReactNative", s + "-" + s1);
                    }

                    @Override
                    public void e(String s, String s1) {
                        Log.e("ReactNative", s + "-" + s1);
                    }

                    @Override
                    public void e(String s, String s1, Throwable throwable) {
                        Log.e("ReactNative", s + "-" + s1 + "-" + throwable);
                    }

                    @Override
                    public void wtf(String s, String s1) {
                        Log.e("ReactNative", s + "-" + s1);
                    }

                    @Override
                    public void wtf(String s, String s1, Throwable throwable) {
                        Log.e("ReactNative", s + "-" + s1 + "-" + throwable);
                    }

                    @Override
                    public void log(int i, String s, String s1) {
                        Log.e("ReactNative", s + "-" + s1);
                    }
                });

                for (ReactPackage reactPackage : getPackages())
                {
                    builder.addPackage(reactPackage);
                }
                builder.addPackage(codePush);

                instanceManager = builder.build();

            }

            return instanceManager;
        }
    }
    @Override
    public void launch(@NotNull Context context) {
        // React Native Launch
        Toast.makeText(context, "React Native Control", Toast.LENGTH_LONG).show();

        ReactRootView view = new ReactRootView(context);
        // view.setMinimumHeight(800);
        // view.setMinimumWidth(500);

        view.startReactApplication(ReactInstanceManagerInstance.GetManager(), "ReactAction2");
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//        alertDialogBuilder.setView(view);
//        alertDialogBuilder.create().show();

        DrillInDialog drillInDialog = DrillInDialog.Create(context);
        DrillInDialog.View drillInDialogView = drillInDialog.createView(view);
        drillInDialogView.setTitle("SDX View");
        drillInDialog.show(drillInDialogView);

        //mHostDrillInDialog = DrillInDialog.Create(context);
        //mHostDrillInDialog.addContentView(view, Office.);

       // OfficeLinearLayout.LayoutParams layoutParams = new OfficeLinearLayout.LayoutParams( WindowManager.LayoutParams.MATCH_PARENT,
        //        WindowManager.LayoutParams.WRAP_CONTENT );
        //mHostDrillInDialog.addContentView(view, layoutParams);

        //mHostDrillInDialog.showNext(mHostDrillInDialog.createView(view));
        //mHostDrillInDialog.setTitle("ReactNative in Action");
        //mHostDrillInDialog.show();


        /*IdentityMetaData docIdentityMetadata = CoauthGalleryController.GetInstance().getDocIdentityMetadata();

        if(docIdentityMetadata != null *//*&& docIdentityMetadata.EmailId != null && !docIdentityMetadata.EmailId.isEmpty()*//*)

        {


            IPeopleCardContainer peopleCardContainer = new PeopleCardDrillInDialogContainer(context);

            PeopleCard.GetInstance().show(OfficeActivityHolder.GetActivity()*//*context*//*, docIdentityMetadata.EmailId *//*emailId*//*,

                    peopleCardContainer *//*peopleCardContainer*//*, "kaghatak@microsoft.com" *//*lpcPersonEmailId*//*);

        }
*/
        //PeopleCard.GetInstance().show(contex, "kaghatak@microsoft.com", );


    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
