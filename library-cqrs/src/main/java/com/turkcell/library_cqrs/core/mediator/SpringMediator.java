package com.turkcell.library_cqrs.core.mediator;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.core.mediator.cqrs.Command;
import com.turkcell.library_cqrs.core.mediator.cqrs.CommandHandler;
import com.turkcell.library_cqrs.core.mediator.cqrs.Query;
import com.turkcell.library_cqrs.core.mediator.cqrs.QueryHandler;
import com.turkcell.library_cqrs.core.mediator.pipeline.PipelineBehavior;
import com.turkcell.library_cqrs.core.mediator.pipeline.RequestHandlerDelegate;

@Component
public class SpringMediator implements Mediator
{
    private final ApplicationContext context;
    private final Map<Class<?>, Object> handlerCache = new ConcurrentHashMap<>();
    private final List<PipelineBehavior> behaviors;
    
    public SpringMediator(ApplicationContext context, List<PipelineBehavior> behaviors) {
        this.context = context;
        this.behaviors = behaviors == null ? List.of() : behaviors.stream().sorted(AnnotationAwareOrderComparator.INSTANCE).toList();
        initializeCache();
    }

    @Override
    public <R> R send(Command<R> command) {
        var handler = (CommandHandler<Command<R>, R>) resolveHandler(command.getClass(), CommandHandler.class);

        return invokePipeline(command, () -> handler.handle(command));
    }

    @Override
    public <R> R send(Query<R> query) {
        var handler = (QueryHandler<Query<R>, R>) resolveHandler(query.getClass(), QueryHandler.class);

        return invokePipeline(query, () -> handler.handle(query));
    }
    
    private Object resolveHandler(Class<?> requestType, Class<?> handlerInterface) {
        Object handler = handlerCache.get(requestType);

        if (handler == null) {
            // try to find and cache
            String[] beanNames = context.getBeanNamesForType(handlerInterface);

            for(String beanName: beanNames)
            {
                Class<?> beanClass = context.getType(beanName);
                if(beanClass == null) continue;

                ResolvableType[] interfaces = ResolvableType.forClass(beanClass).getInterfaces();

                for(ResolvableType iface: interfaces)
                {
                    if(iface.getRawClass() != null && handlerInterface.isAssignableFrom(iface.getRawClass()))
                    {
                        Class<?> firstGeneric = iface.getGeneric(0).resolve();

                        if(firstGeneric != null && firstGeneric.equals(requestType)) {
                            handler = context.getBean(beanName);
                            handlerCache.put(requestType, handler);
                            return handler;
                        }
                    }
                }
            }
            throw new IllegalStateException("Handler not found for " + requestType.getSimpleName());
        }

        return handler;
    }

    private void initializeCache() {
        Map<String, CommandHandler> commandHandlers = context.getBeansOfType(CommandHandler.class);
        registerHandlers(commandHandlers.values());

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

    private <R> R invokePipeline(Object request, RequestHandlerDelegate<R> handlerInvocation)
    {
        RequestHandlerDelegate<R> next = handlerInvocation;

        for(int i = behaviors.size() - 1; i >= 0; i--) {
            PipelineBehavior behavior = behaviors.get(i);
            if(!behavior.supports(request)) continue;

            RequestHandlerDelegate<R> current = next;
            next = () -> behavior.handle(request, current);
        }

        return next.invoke();
    }
}
