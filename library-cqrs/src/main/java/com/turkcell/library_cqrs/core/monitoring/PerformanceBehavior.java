package com.turkcell.library_cqrs.core.monitoring;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.core.mediator.pipeline.PipelineBehavior;
import com.turkcell.library_cqrs.core.mediator.pipeline.RequestHandlerDelegate;

@Component
@Order(10)
public class PerformanceBehavior implements PipelineBehavior {
    private final long thresholdMs = 3000L;

    @Override
    public boolean supports(Object request) {
        return true;
    }

    @Override
    public <R> R handle(Object request, RequestHandlerDelegate<R> next) {
        long start = System.currentTimeMillis();
        R response = next.invoke();
        long duration = System.currentTimeMillis() - start;

        if (duration >= thresholdMs) {
            System.out.println(String.format("[Library][Performance][ALERT] %s took %d ms (threshold=%d ms)", request.getClass().getSimpleName(), duration, thresholdMs));
        }

        return response;
    }
}
