package com.microsoft.office.officemobile.ActionsTab;

import androidx.annotation.NonNull;
import androidx.annotation.StringDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef( {
               ActionType.TRANSFER_FILES,
               ActionType.SCAN_QR_CODE,
               ActionType.SIGN_PDF,
               ActionType.COPY_TEXT_FROM_PICTURE,
               ActionType.IMPORT_DATA_FROM_PICTURE,
               ActionType.SHARE_NEARBY,
               ActionType.SCAN_PDF,
               ActionType.PICTURE_TO_PDF,
               ActionType.DOCUMENT_TO_PDF,
               ActionType.REHEARSE_PPT,
               ActionType.CREATE_FORM,
               ActionType.PDF_TO_WORD,
               ActionType.REACT_ACTION,
               ActionType.RECORDING_AND_TRANSCRIBE,
} )

@Retention( RetentionPolicy.SOURCE )
public @interface ActionType
{
   String TRANSFER_FILES = "TRANSFER_FILES";
   String SCAN_QR_CODE = "SCAN_QR_CODE";
   String SIGN_PDF = "SIGN_PDF";
   String COPY_TEXT_FROM_PICTURE = "COPY_TEXT_FROM_PICTURE";
   String IMPORT_DATA_FROM_PICTURE = "IMPORT_DATA_FROM_PICTURE";
   String SHARE_NEARBY = "SHARE_NEARBY";
   String SCAN_PDF = "SCAN_TO_PDF";
   String PICTURE_TO_PDF = "PICTURE_TO_PDF";
   String DOCUMENT_TO_PDF = "DOCUMENT_TO_PDF";
   String REHEARSE_PPT = "REHEARSE_PPT";
   String CREATE_FORM = "CREATE_FORM";
   String PDF_TO_WORD = "PDF_TO_WORD";
   @NonNull String REACT_ACTION = "React Action";
   @NonNull String RECORDING_AND_TRANSCRIBE = "RECORDING_AND_TRANSCRIBE";
}