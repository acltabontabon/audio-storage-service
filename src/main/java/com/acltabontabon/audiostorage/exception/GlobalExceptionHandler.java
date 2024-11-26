package com.acltabontabon.audiostorage.exception;

import com.acltabontabon.audiostorage.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FileConversionException.class)
    public ResponseEntity<ApiResponse> handleInvalidAudioFile(FileConversionException ex) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
            .body(ApiResponse.builder()
                .message("Failed to convert audio file")
                .build());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
            .body(ApiResponse.builder()
                    .message(ex.getMessage())
                    .build());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.builder()
            .message(ex.getMessage())
            .build());
    }

    @ExceptionHandler(AudioNotFoundException.class)
    public ResponseEntity<ApiResponse> handleAudioNotFoundException(AudioNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.builder()
            .message(ex.getMessage())
            .build());
    }

    @ExceptionHandler(PhraseNotFoundException.class)
    public ResponseEntity<ApiResponse> handlePhraseNotFoundException(PhraseNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.builder()
            .message(ex.getMessage())
            .build());
    }

    @ExceptionHandler(AudioAlreadyExistException.class)
    public ResponseEntity<ApiResponse> handlePhraseNotFoundException(AudioAlreadyExistException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.builder()
            .message(ex.getMessage())
            .build());
    }


    @ExceptionHandler(UnsupportedFileExtensionException.class)
    public ResponseEntity<ApiResponse> handleInvalidFileExtensionException(UnsupportedFileExtensionException ex) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(ApiResponse.builder()
            .message(ex.getMessage())
            .build());
    }
}
