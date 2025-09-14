package com.example.demo.service;

import com.example.demo.service.config.ContextService;
import com.example.demo.utils.customexceptions.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.IllegalArgumentException;
import java.util.Objects;


public abstract class BaseService {
    @Autowired
    protected ContextService contextService;


    protected void processException(Exception e) throws Exception {
        e = parseExceptions(e);

        if (e instanceof UserAlreadyExistsException || e instanceof ResourceNotFoundException || e instanceof RequiredResourceNotFoundException || e instanceof UserNotFoundException || e instanceof RoleNotFoundException || e instanceof DoesNotHavePermissionException || e instanceof InvalidPasswordException || e instanceof IOException || e instanceof DisabledException || e instanceof LockedException || e instanceof BadCredentialsException || e instanceof PasswordResetTokenExpiredException || e instanceof ExternalAPIException || e instanceof ForgotPasswordException) {
            throw e;
        } else if (e instanceof SignatureException || e instanceof MalformedJwtException || e instanceof ExpiredJwtException || e instanceof UnsupportedJwtException || e instanceof RefreshTokenDoesNotExistException || e instanceof RefreshTokenNotValidException) {

            throw e;
        } else if (e instanceof IllegalStateException) {
            if (!Objects.equals(e.getMessage(),null) && e.getMessage()
                    .contains("No instances")) {

                throw new InstanceNotAvailableException(e.getMessage());
            }

        }else if (e instanceof IllegalArgumentException) {
            if (!Objects.equals(e.getMessage(),null) && e.getMessage()
                    .contains("Something went wrong")) {

                throw new InstanceNotAvailableException(e.getCause().getMessage());
            }
            if (!Objects.equals(e.getMessage(),null) && e.getMessage()
                    .contains("Couldn't access api")) {
                String expMsg=e.getMessage();

                int start=expMsg.indexOf("//")+2;
                String serviceNameFromMsg = expMsg.substring(start,expMsg.indexOf("/",start));
                String errMsg=String.format("Couldn't access the api of (%s)",serviceNameFromMsg);
                throw new InstanceNotAvailableException(errMsg);
            }
        } else if(!Objects.equals(e.getMessage(),null) && e.getMessage().contains("permission was denied on the object")){

            int index = e.getMessage().indexOf("denied");
            String message = e.getMessage().substring(0, index + "denied".length());
            throw new RequiredResourceNotFoundException(message);

        } else {
            throw e;
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

        return (Exception) cause;
    }


}
