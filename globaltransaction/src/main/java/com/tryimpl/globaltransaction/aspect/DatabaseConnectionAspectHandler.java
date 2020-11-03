package com.tryimpl.globaltransaction.aspect;

import com.tryimpl.globaltransaction.connection.DefaultDatabaseConnection;
import com.tryimpl.globaltransaction.transaction.GlobalTransantionManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.sql.Connection;

/**
 * 通过对“javax.sql.DataSource.getConnection(..)”创建切面
 * 获取Spring中获取数据库Connection的对象
 */
@Aspect
@Component
public class DatabaseConnectionAspectHandler {

    @Pointcut(value = "execution(* javax.sql.DataSource.getConnection(..))")
    public void handleDatabaseConnectionPointcut(){}

    @Around(value = "handleDatabaseConnectionPointcut()")
    public Connection handleDatabaseConnectionAround(ProceedingJoinPoint point) throws Throwable {
        if(GlobalTransantionManager.getCurrentDefaultTransaction() != null) {
            Connection connection = (Connection) point.proceed();
            return new DefaultDatabaseConnection(connection, GlobalTransantionManager.getCurrentDefaultTransaction());
        } else {
            return (Connection) point.proceed();
        }

    }
}
