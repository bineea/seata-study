package com.tryimpl.globaltransaction.aspect;

import com.tryimpl.globaltransaction.annotation.GlobalTransactional;
import com.tryimpl.globaltransaction.transaction.DefaultTransaction;
import com.tryimpl.globaltransaction.transaction.GlobalTransantionManager;
import com.tryimpl.globaltransaction.transaction.TransactionType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class GlobalTransactionalAspectHandler implements Ordered {

    @Pointcut(value = "@annotation(com.tryimpl.globaltransaction.annotation.GlobalTransactional)")
    public void handleGlobalTransactionalPointcut() {}

    @Around(value = "handleGlobalTransactionalPointcut()")
    public void handleGlobalTransactionalAound(ProceedingJoinPoint point) {

        //MethodSignature signature = (MethodSignature) point.getSignature();
        //Method method = signature.getMethod();
        //GlobalTransactional globalTransactional = method.getAnnotation(GlobalTransactional.class);

        //创建全局事务
        String groupId = GlobalTransantionManager.getOrCreateGroupId();

        //创建分支事务
        DefaultTransaction defaultTransaction = GlobalTransantionManager.getOrCreateTransaction(groupId);

        TransactionType transactionType = null;

        //根据程序是否异常，确定分支事务的类型
        try {
            //‘point.proceed();’方法就是添加了‘GlobalTransactional’注解的方法，方法肯定存在数据库操作
            //但是因为定义DefaultDatabaseConnection，实现干涉‘commit’、‘rollback’方法的目的，
            //导致操作数据库的同时就会触发‘commit’、‘rollback’方法中的阻塞状态，进而导致‘point.proceed();’阻塞
            //所以调整DefaultDatabaseConnection关于干涉‘commit’、‘rollback’方法的具体实现
            point.proceed();
            transactionType = TransactionType.commit;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            transactionType = TransactionType.rollback;
        }

        //注册分支事务
        GlobalTransantionManager.registerTransaction(defaultTransaction, transactionType);
    }

    @Override
    public int getOrder() {
        return 1000;
    }
}
