# seata-study
 Simple Implementation of Distributed Transactions

     参考seata，简单模拟实现分布式事务

1. 创建分布式事务注解-@GlobalTransactional
    - 以配置注解@GlobalTransactional的方法为切点，创建切面！获取所有需要分布式事务管理的方法
2. 以DataSource.getConnection(..)方法为切点，创建切面！获取数据库连接。从而实现干涉“commit”、“rollback”方法执行的目的
3. 设定全局事务、分支事务
    - 全局事务中所有的分支事务均执行“commit”操作，则判定全局事务执行“commit”，所有分支事务正常提交
    - 全局事务中任意一个分支事务执行“rollback”操作，则判定全局事务执行“rollback”，所有分支事务均执行回滚
