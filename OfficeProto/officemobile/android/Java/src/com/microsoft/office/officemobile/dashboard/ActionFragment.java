package com.microsoft.office.officemobile.dashboard;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.microsoft.office.docsui.eventproxy.InitDependentActionHandler;
import com.microsoft.office.identity.IdentityLiblet;
import com.microsoft.office.identity.IdentityMetaData;
import com.microsoft.office.licensing.ILicensingChangedListener;
import com.microsoft.office.licensing.LicensingManager;
import com.microsoft.office.licensing.LicensingState;
import com.microsoft.office.officehub.util.OHubUtil;
import com.microsoft.office.officemobile.ActionsTab.ActionData;
import com.microsoft.office.officemobile.ActionsTab.ActionGroup;
import com.microsoft.office.officemobile.ActionsTab.ActionType;
import com.microsoft.office.officemobile.ActionsTab.ActionListAdapter;
import com.microsoft.office.officemobile.LifeCycleAwareRunner;
import com.microsoft.office.officemobile.fragmentmanagerinfra.BaseFragment;
import com.microsoft.office.officemobile.fragmentmanagerinfra.FragmentNavigationManager;
import com.microsoft.office.officemobile.fragmentmanagerinfra.OptionMenuItem;
import com.microsoft.office.officemobile.ICollapsingToolbarListener;
import com.microsoft.office.officemobile.OfficeMobileViewModel;
import com.microsoft.office.officemobile.helpers.FeatureGateUtils;
import com.microsoft.office.officemobilelib.R;
import com.microsoft.office.permission.PermissionProvider;
import com.microsoft.office.plat.DeviceUtils;
import com.microsoft.office.ui.utils.OfficeStringLocator;


import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for managing action list on office mobile dashboard.
 * Created by hibansal on 10/5/2018.
 */
public class ActionFragment extends BaseFragment implements ILicensingChangedListener
{
   private ActionContentView mContentView;
   private OfficeMobileViewModel mViewModel;

   @Nullable
   @Override
   public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState )
   {
      super.onCreateView( inflater, container, savedInstanceState );
      // Registering the callback. Wrapping to make sure SO's are loaded.
      InitDependentActionHandler.ExecuteWhenInitIsComplete( () ->
              LicensingManager.getInstance().RegisterLicensingChangedListener( this ) );
      return inflater.inflate( R.layout.fragment_tab_actions, container, false );
   }

   @Override
   public View getScrollableView()
   {
      return mContentView.getRecyclerView();
   }

   /**
    * This method is used to create action fragment. Fragment creation waits for SO load based on whether savedInstanceState is null or not in the super method call
    *
    * @param view
    * @param savedInstanceState
    */
   @Override
   public void handleOnMAMViewCreated( View view, Bundle savedInstanceState )
   {
      super.handleOnMAMViewCreated( view, savedInstanceState );
      ViewGroup container = view.findViewById( R.id.frame_content );

      initializeActionsListTab( getContext(), container );
      mViewModel = ViewModelProviders.of( this ).get( OfficeMobileViewModel.class );

      // Register for Identity change (sign in or sign out) to update the actions tab.
      IdentityLiblet.GetInstance().registerIdentityManagerListener( new IdentityLiblet.IIdentityManagerListener()
      {
         @Override
         public void OnIdentitySignIn( IdentityMetaData identityMetaData,
                                       IdentityLiblet.SignInContext signInContext )
         {
            HandleIdentityChange();
         }

         @Override
         public void OnIdentityPropertyChanged( IdentityMetaData identityMetaData )
         {
         }

         @Override
         public void OnIdentitySignOut( IdentityMetaData identityMetaData )
         {
            HandleIdentityChange();
         }

         @Override
         public void OnIdentityProfilePhotoChanged( IdentityMetaData identityMetaData )
         {
         }
      } );
   }

   @Override
   protected String getTitle()
   {
      return getResources().getString( R.string.nav_actions );
   }

   @Override
   public void onHiddenChanged( boolean hidden )
   {
      super.onHiddenChanged( hidden );
      mViewModel = ViewModelProviders.of( this ).get( OfficeMobileViewModel.class );
      mViewModel.cleanup();
      mViewModel.cleanUpTasks();
   }

   @Override
   protected List<OptionMenuItem> getOptionMenuItems()
   {
      return DashboardInitializationHelper.getInstance().getOptionMenuItems( FragmentNavigationManager.FragmentTag.ACTIONS );
   }

   /**
    * updates collapsing behaviour based on the current state of the scrollable view
    */
   @Override
   public void updateToolBarCollapsingBehaviour()
   {
      if( getActivity() instanceof ICollapsingToolbarListener && mContentView != null && mContentView.getRecyclerView() != null )
      {
         // Enabling collapsing behaviour if total scrolling range(i.e.total height including the item that are visible through scroll) is greater than the view height of recycler view.
         // This measure will return us true if we have any vertical scrolling possible in given recycler view
         boolean enableCollapsingToolbar = mContentView.getRecyclerView().computeVerticalScrollRange() > mContentView.getRecyclerView().getHeight();
         ( ( ICollapsingToolbarListener ) getActivity() ).updateCollapsingToolbarBehaviour( enableCollapsingToolbar );
      }
   }

   /**
    * Returns the list of views where the focus should be going to inside action fragment's frame layout.
    */
   public List<View> getFocusableList()
   {
      List<View> focusableList = new ArrayList<View>();
       if( mContentView != null )
       {
          return mContentView.getFocusableList();
       }
       return focusableList;
   }

   //Currently any activity or fragment apart form OfficeActivity making call to get permission [PermissionProvider.RequestPermission] needs to implement
   //this function. Permission result call back will come here. Calling will be made to it from LensFlow from IDialogBasedPermissionResultCallback.
   @Override
   public void onRequestPermissionsResult( int requestCode, String[] permissions,
                                           int[] grantResults )
   {
      PermissionProvider.HandlePermissionResult( getActivity(), Manifest.permission.CAMERA, requestCode, permissions, grantResults );
   }

   /**
    * Initializes the actions tab by creating a linearLayout in a recycler view
    * This recycler view is added in the passed in container.
    *
    * @param context      - Activity's context.
    * @param container    - The container inside which Actions tab will be inflated.
    */
   public void initializeActionsListTab( final Context context, ViewGroup container )
   {
      mContentView = ActionContentView.Companion.create( context, getActionGroupDataList() );
      container.addView( mContentView );
   }

   /**
    * It returns a list of ActionGroup consisting of 4 groups that need to be shown on Action screen
    */
   private List<ActionGroup> getActionGroupDataList()
   {
      List<ActionData> actionFiles = new ArrayList<>();
      if( FeatureGateUtils.isFileTransferEnabled() )
      {
         String titleText = FeatureGateUtils.isFileTransferReceiveModeEnabled() ? OfficeStringLocator.getOfficeStringFromKey( "officemobile.idsActionTransferFiles" ) :
                            OfficeStringLocator.getOfficeStringFromKey( "officemobile.idsActionSendFiles" );
         String subtext = FeatureGateUtils.isFileTransferReceiveModeEnabled() ? OfficeStringLocator.getOfficeStringFromKey( "officemobile.idsActionSubtextTransferFiles" ) :
                          OfficeStringLocator.getOfficeStringFromKey( "officemobile.idsActionSubtextSendFiles" );


         actionFiles.add( new ActionData( ActionType.TRANSFER_FILES, titleText, subtext, R.drawable.ic_action_list_receive_files_overlay, R.color.receive_files_icon_background ) );
      }
      if( FeatureGateUtils.isShareNearbyEnabled() )
      {
         actionFiles.add( new ActionData( ActionType.SHARE_NEARBY, getResources().getString( R.string.idsActionShareNearby ), getResources().getString( R.string.idsActionSubtextShareNearby ), R.drawable.ic_action_list_share_nearby_overlay, R.color.share_nearby_icon_background ) );
      }

      List<ActionData> actionImages = new ArrayList<>();
      if( FeatureGateUtils.isCopyTextFromPictureEnabled() )
      {
         actionImages.add( new ActionData( ActionType.COPY_TEXT_FROM_PICTURE, getResources().getString( R.string.idsActionRecognizeTextFromPicture ), getResources().getString( R.string.idsActionSubtextRecognizeTextFromPicture ), R.drawable.ic_action_list_copy_text_from_picture_overlay, R.color.copy_text_from_picture_icon_background ) );
      }
      if( FeatureGateUtils.isImportDataFromPictureEnabled() )
      {
         actionImages.add( new ActionData( ActionType.IMPORT_DATA_FROM_PICTURE, getResources().getString( R.string.idsActionRecognizeDataFromPicture ), getResources().getString( R.string.idsActionSubtextRecognizeDataFromPicture ), R.drawable.ic_action_list_import_data_from_picture_overlay, R.color.import_data_from_picture_icon_background ) );
      }

      List<ActionData> actionVoice = new ArrayList<>();
      if( FeatureGateUtils.isTranscriptionEnabled() )
      {
         // We show the premium badge only if the licensing state of the app is not premium
          if ( !isLicensingStatePremium() )
          {
              actionVoice.add( new ActionData( ActionType.RECORDING_AND_TRANSCRIBE, OfficeStringLocator.getOfficeStringFromKey( "officemobile.idsActionRecordingAndTranscribe" ), OfficeStringLocator.getOfficeStringFromKey( "officemobile.idsActionSubtextRecordingAndTranscribe" ), R.drawable.ic_actions_recording_and_transcribe, R.color.recording_and_transcribe_background, true /* isPremiumFeature */ ) );
          }
          else
          {
              actionVoice.add( new ActionData( ActionType.RECORDING_AND_TRANSCRIBE, OfficeStringLocator.getOfficeStringFromKey( "officemobile.idsActionRecordingAndTranscribe" ), OfficeStringLocator.getOfficeStringFromKey( "officemobile.idsActionSubtextRecordingAndTranscribe" ), R.drawable.ic_actions_recording_and_transcribe, R.color.recording_and_transcribe_background ) );
          }
      }

      List<ActionData> actionPdf = new ArrayList<>();
      actionPdf.add( new ActionData( ActionType.SIGN_PDF, getResources().getString( R.string.idsActionSignAPdf ), getResources().getString( R.string.idsActionSubtextSignAPdf ), R.drawable.ic_action_list_sign_a_pdf_overlay, R.color.sign_a_pdf_icon_background ) );

      if( DeviceUtils.isCameraSupported() )
      {
         actionPdf.add( new ActionData( ActionType.SCAN_PDF, getResources().getString( R.string.idsScanToPdf ), getResources().getString( R.string.idsActionSubtextScanToPdf ), R.drawable.ic_actions_pdf_scan_main, R.color.scan_to_pdf_icon_background ) );
      }
      if( DeviceUtils.isNativeImageGallerySupported() )
      {
         actionPdf.add( new ActionData( ActionType.PICTURE_TO_PDF, getResources().getString( R.string.idsPicturesToPdf ), getResources().getString( R.string.idsActionSubtextPicturesToPdf ), R.drawable.ic_actions_pdf_choose_pictures_main, R.color.picture_to_pdf_icon_background ) );
      }
      if( FeatureGateUtils.isDocumentsToPdfEnabled() )
      {
         actionPdf.add( new ActionData( ActionType.DOCUMENT_TO_PDF, getResources().getString( R.string.idsDocumentToPdf ), getResources().getString( R.string.idsActionSubtextDocumentToPdf ), R.drawable.ic_actions_pdf_convert_doc_main, R.color.document_to_pdf_icon_background ) );
      }

      if( FeatureGateUtils.isPdfToWordEnabled() )
      {
         actionPdf.add( new ActionData( ActionType.PDF_TO_WORD, OfficeStringLocator.getOfficeStringFromKey( "officemobile.idsPdfToWord" ), OfficeStringLocator.getOfficeStringFromKey( "officemobile.idsActionSubtextPdfToWord" ), R.drawable.ic_actions_pdf_convert_to_word, R.color.pdf_to_word_icon_background ) );
      }

      List<ActionData> actionOthers = new ArrayList<>();
      if( FeatureGateUtils.isScanQRCodeEnabled() )
      {
         actionOthers.add( new ActionData( ActionType.SCAN_QR_CODE, getResources().getString( R.string.idsActionScanQrCode ), getResources().getString( R.string.idsActionSubtextScanQrCode ), R.drawable.ic_action_list_scan_qr_code_overlay, R.color.scan_qr_code_icon_background ) );
      }
      if( FeatureGateUtils.isRehearsePPTEnabled() )
      {
         actionOthers.add( new ActionData( ActionType.REHEARSE_PPT, getResources().getString( R.string.idsActionRehearsePresentation ), getResources().getString( R.string.idsActionSubtextRehearsePresentation ), R.drawable.ic_action_list_rehearse_ppt, R.color.rehearse_ppt_icon_background ) );
      }
      if( FeatureGateUtils.isFormsEnabled() )
      {
         //TODO: Get Forms icon from UX. Tracked by: TASK 4052815
         actionOthers.add( new ActionData( ActionType.CREATE_FORM, OfficeStringLocator.getOfficeStringFromKey( "officemobile.idsActionCreateForms" ), OfficeStringLocator.getOfficeStringFromKey( "officemobile.idsActionSubtextCreateForms" ), R.drawable.ic_action_list_create_forms_overlay, R.color.create_forms_icon_background ) );
      }

      actionOthers.add(new ActionData(ActionType.REACT_ACTION, "ReactAction1", OfficeStringLocator.getOfficeStringFromKey( "officemobile.idsActionSubtextCreateForms" ), R.drawable.ic_action_list_create_forms_overlay, R.color.create_forms_icon_background) );

      List<ActionGroup> actionGroupList = new ArrayList<>();

      if( !actionFiles.isEmpty() )
      {
         actionGroupList.add( new ActionGroup( getResources().getString( R.string.idsActionGroupFiles ), actionFiles ) );
      }
      if( !actionVoice.isEmpty() )
      {
         actionGroupList.add( new ActionGroup( OfficeStringLocator.getOfficeStringFromKey( "officemobile.idsActionGroupVoice" ), actionVoice ) );
      }
      if( !actionImages.isEmpty() )
      {
         actionGroupList.add( new ActionGroup( getResources().getString( R.string.idsActionGroupImages ), actionImages ) );
      }
      if( !actionPdf.isEmpty() )
      {
         actionGroupList.add( new ActionGroup( getResources().getString( R.string.idsActionGroupPDF ), actionPdf ) );
      }
      if( !actionOthers.isEmpty() )
      {
         actionGroupList.add( new ActionGroup( getResources().getString( R.string.idsActionGroupOthers ), actionOthers ) );
      }

      return actionGroupList;
   }

   /**
    * Handles sign-in or sign-out of accounts for changes in actions tab.
    */
   private void HandleIdentityChange()
   {
      Lifecycle lifecycle = this.getLifecycle();
      new Handler( Looper.getMainLooper() ).post( () ->
      {
         new LifeCycleAwareRunner( lifecycle, () ->
         {
            if( mContentView.getRecyclerView() != null )
            {
               ActionListAdapter actionListAdapter = ( ActionListAdapter ) mContentView.getRecyclerView().getAdapter();
               actionListAdapter.UpdateActionGroupList( getActionGroupDataList() );
            }
         } ).run();
      } );
   }

   // Returns true if the licensing state of the app is premium
   private boolean isLicensingStatePremium()
   {
      return OHubUtil.GetLicensingState() == LicensingState.ConsumerPremium || OHubUtil.GetLicensingState() == LicensingState.EnterpriseView ||
              OHubUtil.GetLicensingState() == LicensingState.EnterprisePremium;
   }

   @Override
   public void onLicensingChanged( @NonNull LicensingState licensingState )
   {
      HandleIdentityChange();
   }

   @Override
   public void onDetach()
   {
      LicensingManager.getInstance().UnregisterLicensingChangedListener( this );
      super.onDetach();
   }
}