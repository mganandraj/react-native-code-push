package com.microsoft.office.officemobile.Actions.actionHandlers

import com.microsoft.office.officemobile.ActionsTab.ActionType
import com.microsoft.office.officemobile.ActionsTab.IActionHandler

/**
 * A factory class to generate the ActionHandler based on the action type
 */
object ActionHandlerFactory {
  /**
   * Return the ActionHandler responsible for handling the corresponding [ActionType]
   *
   * @param actionType [ActionType] for which the ActionHandler implementation is required
   */
  fun getActionHandler(@ActionType actionType: String): IActionHandler? =
    when (actionType) {
      ActionType.SCAN_QR_CODE -> ScanQRCodeActionHandler()
      ActionType.PDF_TO_WORD -> ConversionToDocActionHandler()
      ActionType.SIGN_PDF -> SignPdfActionHandler()
      ActionType.IMPORT_DATA_FROM_PICTURE, ActionType.COPY_TEXT_FROM_PICTURE -> DataFromPictureActionHandler(actionType)
      ActionType.TRANSFER_FILES -> TransferFilesActionHandler()
      ActionType.SHARE_NEARBY -> ShareNearbyActionHandler()
      ActionType.SCAN_PDF -> ScanPdfActionHandler()
      ActionType.PICTURE_TO_PDF -> PictureToPdfActionHandler()
      ActionType.DOCUMENT_TO_PDF -> DocumentToPdfActionHandler()
      ActionType.REHEARSE_PPT -> RehearsePptActionHandler()
      ActionType.CREATE_FORM -> FormsActionHandler()
      ActionType.RECORDING_AND_TRANSCRIBE -> TranscriptionActionHandler()
      ActionType.REACT_ACTION -> ReactNativeAction()
      else -> null
    }

}