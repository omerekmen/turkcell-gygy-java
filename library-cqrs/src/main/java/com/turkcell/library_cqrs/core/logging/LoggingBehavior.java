package com.turkcell.library_cqrs.core.logging;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.core.mediator.pipeline.PipelineBehavior;
import com.turkcell.library_cqrs.core.mediator.pipeline.RequestHandlerDelegate;
import com.turkcell.library_cqrs.domain.entity.RequestLog;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.RequestLogRepository;

@Component
@Order(10)
public class LoggingBehavior implements PipelineBehavior {

    private final RequestLogRepository repo;

    public LoggingBehavior(RequestLogRepository repo) {
        this.repo = repo;
    }

    @Override
    public boolean supports(Object request) {
        return true;
    }

    @Override
    public <R> R handle(Object request, RequestHandlerDelegate<R> next) {
        long start = System.currentTimeMillis();
        System.out.println("[Library][LoggingBehavior] Request -> " + request.getClass().getSimpleName());
        R response = next.invoke();
        long duration = System.currentTimeMillis() - start;
        System.out.println("[Library][LoggingBehavior] Response <- " + request.getClass().getSimpleName() + " duration=" + duration + "ms");

        try {
            RequestLog log = new RequestLog();
            log.setRequestType(request.getClass().getSimpleName());
            log.setRequestPayload(request.toString());
            log.setResponsePayload(response == null ? null : response.toString());
            log.setDurationMs(duration);
            repo.save(log);
            System.out.println("[Library][LoggingBehavior] Request log persisted: " + log.getId());
            System.out.println(log.toJson());
        } catch (Exception ex) {
            System.err.println("[Library][LoggingBehavior] Failed to persist request log: " + ex.getMessage());
        }

        return response;
    }
}
