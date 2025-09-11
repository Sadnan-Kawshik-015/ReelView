package com.example.demo.controller;

import com.example.demo.dtos.output.ResponseModelDTO;
import com.example.demo.utils.customexceptions.*;
import com.example.demo.utils.enums.Status;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

import static com.example.demo.utils.constants.CommonMessage.UNHANDLED_EXCEPTION;

@SecurityRequirement(name = "bearerAuth")
@Component
public abstract class BaseController {
    private HttpServletRequest request;
    @Value("${is-debug-mode}")
    private boolean isDebugMode;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    protected ResponseEntity<ResponseModelDTO> doExceptionTask(Exception e) {
        e = parseExceptions(e);
        if(Objects.nonNull(e.getMessage()) && e.getMessage().contains("permission was denied on the object")){

            int index = e.getMessage().indexOf("denied");
            String message = e.getMessage().substring(0, index + "denied".length());
            return ResponseEntity.badRequest()
                    .body(
                            ResponseModelDTO.builder()
                                    .status(Status.ERROR.getValue())
                                    .message(message)
                                    .correlation_id(getCorrelationId())
                                    .build()
                    );

        }

        if (
                e instanceof UserAlreadyExistsException |
                        e instanceof RefreshTokenNotValidException |
                        e instanceof RefreshTokenDoesNotExistException |
                        e instanceof IOException |
                        e instanceof DisabledException |
                        e instanceof LockedException |
                        e instanceof BadCredentialsException |
                        e instanceof PasswordResetTokenExpiredException |
                        e instanceof InvalidPasswordException |
                        e instanceof DoesNotHavePermissionException |
                        e instanceof ForgotPasswordException |
                        e instanceof UserNotFoundException |
                        e instanceof RequiredResourceNotFoundException |
                        e instanceof ExternalAPIException |
                        e instanceof JDBCConnectionException
        ) {
            return ResponseEntity.badRequest()
                    .body(
                            ResponseModelDTO.builder()
                                    .status(Status.ERROR.getValue())
                                    .message(e.getMessage())
                                    .correlation_id(null)
                                    .build()
                    );
        } else if (e instanceof ResourceNotFoundException
        ) {
            return ResponseEntity.ok(
                    ResponseModelDTO.builder()
                            .status(Status.SUCCESS.getValue())
                            .message(e.getMessage())
                            .build()
            );
        } else {
            return new ResponseEntity<>(
                    ResponseModelDTO.builder()
                            .status(Status.ERROR.getValue())
                            .message(isDebugMode ? e.getMessage() : UNHANDLED_EXCEPTION)
                            .correlation_id(null)
                            .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
    public <T extends Throwable> Exception parseExceptions(T t) {
        if (t == null) {
            return null;
        }

        Throwable cause = t;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        var e = (Exception) cause;
        return e;
    }

    public String getCorrelationId() {
        return this.request.getHeader("x-correlation-id");
    }

}
