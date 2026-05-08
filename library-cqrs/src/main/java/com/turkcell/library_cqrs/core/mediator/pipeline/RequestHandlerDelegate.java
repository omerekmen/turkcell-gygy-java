package com.turkcell.library_cqrs.core.mediator.pipeline;

public interface RequestHandlerDelegate<R> {
    R invoke();
}
