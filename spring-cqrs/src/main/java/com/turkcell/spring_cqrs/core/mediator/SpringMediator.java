package com.turkcell.spring_cqrs.core.mediator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import com.turkcell.spring_cqrs.core.mediator.cqrs.Command;
import com.turkcell.spring_cqrs.core.mediator.cqrs.CommandHandler;
import com.turkcell.spring_cqrs.core.mediator.cqrs.Query;
import com.turkcell.spring_cqrs.core.mediator.cqrs.QueryHandler;

@Component
public class SpringMediator implements Mediator
{
    private final ApplicationContext context;
    private final Map<Class<?>, Object> handlerCache = new ConcurrentHashMap<>();
    
    public SpringMediator(ApplicationContext context) {
        this.context = context;
        initializeCache();
    }

    @Override
    public <R> R send(Command<R> command) {
        var handler = (CommandHandler<Command<R>, R>) resolveHandler(command.getClass());

        return handler.handle(command);
    }

    @Override
    public <R> R send(Query<R> query) {
        var handler = (QueryHandler<Query<R>, R>) resolveHandler(query.getClass());

        return handler.handle(query);
    }

    private Object resolveHandler(Class<?> requestType) {
        Object handler = handlerCache.get(requestType);

        if (handler == null) {
            throw new IllegalStateException("Handler not found for " + requestType.getSimpleName());
        }

        return handler;
    }

    private void initializeCache() {
        // Load all CommandHandlers
        Map<String, CommandHandler> commandHandlers = context.getBeansOfType(CommandHandler.class);
        registerHandlers(commandHandlers.values());

        // Load all QueryHandlers
        Map<String, QueryHandler> queryHandlers = context.getBeansOfType(QueryHandler.class);
        registerHandlers(queryHandlers.values());
    }

    private void registerHandlers(Iterable<?> handlers) {
        for (Object handler : handlers) {
            Class<?> handlerClass = handler.getClass();

            for (ResolvableType iface : ResolvableType.forClass(handlerClass).getInterfaces()) {

                Class<?> raw = iface.getRawClass();
                if (raw == null) continue;

                if (CommandHandler.class.isAssignableFrom(raw) ||
                    QueryHandler.class.isAssignableFrom(raw)) {

                    Class<?> requestType = iface.getGeneric(0).resolve();

                    if (requestType != null) {
                        handlerCache.put(requestType, handler);
                    }
                }
            }
        }
    }
}
