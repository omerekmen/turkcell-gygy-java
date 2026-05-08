package com.turkcell.spring_cqrs.core.logging;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.turkcell.spring_cqrs.core.mediator.pipeline.PipelineBehavior;
import com.turkcell.spring_cqrs.core.mediator.pipeline.RequestHandlerDelegate;
import com.turkcell.spring_cqrs.infrastructure.persistance.postgresql.RequestLogRepository;
import com.turkcell.spring_cqrs.domain.entity.RequestLog;

@Component
@Order(10)
public class LoggingBehavior implements PipelineBehavior {

    private final RequestLogRepository repo;

    public LoggingBehavior(RequestLogRepository repo) {
        this.repo = repo;
    }

    @Override
    public boolean supports(Object request) {
       return !(request instanceof NotLoggableRequest);
    }

    @Override
    public <R> R handle(Object request, RequestHandlerDelegate<R> next) {
        long start = System.currentTimeMillis();
        System.out.println("[LoggingBehavior] Request -> " + request.getClass().getSimpleName() + " | payload=" + request.toString());
        R response = next.invoke();
        long duration = System.currentTimeMillis() - start;
        System.out.println("[LoggingBehavior] Response <- " + request.getClass().getSimpleName() + " | result=" + (response == null ? "null" : response.toString()) + " | duration=" + duration + "ms");

        try {
            RequestLog log = new RequestLog();
            log.setRequestType(request.getClass().getSimpleName());
            log.setRequestPayload(request.toString());
            log.setResponsePayload(response == null ? null : response.toString());
            log.setDurationMs(duration);
            repo.save(log);
        } catch (Exception ex) {
            System.err.println("[LoggingBehavior] failed to persist request log: " + ex.getMessage());
        }

        return response;
    }

}
