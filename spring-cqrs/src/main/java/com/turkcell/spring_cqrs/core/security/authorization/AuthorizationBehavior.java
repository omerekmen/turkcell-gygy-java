package com.turkcell.spring_cqrs.core.security.authorization;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.turkcell.spring_cqrs.core.mediator.pipeline.PipelineBehavior;
import com.turkcell.spring_cqrs.core.mediator.pipeline.RequestHandlerDelegate;
import com.turkcell.spring_cqrs.core.security.context.UserContext;
import com.turkcell.spring_cqrs.core.exception.UnauthenticatedException;
import com.turkcell.spring_cqrs.core.exception.UnauthorizedException;
import com.turkcell.spring_cqrs.core.security.authorization.AuthorizableRequest;

@Component
@Order(10)
public class AuthorizationBehavior implements PipelineBehavior {
    private final UserContext userContext;

    public AuthorizationBehavior(UserContext userContext) {
        this.userContext = userContext;
    }

    @Override
    public boolean supports(Object request) {
        return request instanceof AuthorizableRequest;
    }

    // ilgili handler'ın öncesi ve sonrası çalıştırabilen kodlar.
    @Override
    public <R> R handle(Object request, RequestHandlerDelegate<R> next) {
        if(!userContext.isAuthenticated())
            throw new UnauthenticatedException("User is not authenticated"); // 401
        
        // If the request declares required roles, check them
        if (request instanceof AuthorizableRequest ar) {
            var required = ar.requiredRoles();
            if (required != null && !required.isEmpty()) {
                var userRoles = userContext.getRoles();
                boolean anyMatch = userRoles.stream().anyMatch(required::contains);
                if (!anyMatch) {
                    throw new UnauthorizedException("User does not have required role(s)"); // 403
                }
            }
        }
        // Özel bir exception türü belirle.
        // Handlerda bu exceptionı eğer giriş yapılmamışsa 401, (UnauthenticatedException)
        // yapılmış ancak rol yetersiz ise 403 döndürecek şekilde (UnauthorizedException)
        // düzenle..
        
        return next.invoke(); // zincirdeki sonraki halkayı çağır..
    }

}
