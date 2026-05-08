package com.turkcell.spring_cqrs.core.transaction;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import com.turkcell.spring_cqrs.core.mediator.pipeline.PipelineBehavior;
import com.turkcell.spring_cqrs.core.mediator.pipeline.RequestHandlerDelegate;
import com.turkcell.spring_cqrs.core.mediator.cqrs.Command;

@Component
@Order(15)
public class TransactionBehavior implements PipelineBehavior {

    private final PlatformTransactionManager txManager;

    public TransactionBehavior(PlatformTransactionManager txManager) {
        this.txManager = txManager;
    }

    @Override
    public boolean supports(Object request) {
        return request instanceof Command; // only wrap commands in transactions
    }

    @Override
    public <R> R handle(Object request, RequestHandlerDelegate<R> next) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        TransactionStatus status = txManager.getTransaction(def);
        try {
            R result = next.invoke();
            txManager.commit(status);
            return result;
        } catch (RuntimeException ex) {
            txManager.rollback(status);
            throw ex;
        } catch (Error err) {
            txManager.rollback(status);
            throw err;
        }
    }
}
