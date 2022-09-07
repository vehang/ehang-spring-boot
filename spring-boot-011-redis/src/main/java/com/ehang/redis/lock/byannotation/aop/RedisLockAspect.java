package com.ehang.redis.lock.byannotation.aop;

import com.ehang.redis.lock.byannotation.anno.RedisLockAnnotation;
import com.ehang.redis.lock.byannotation.enums.RedisLockTypeEnum;
import com.ehang.redis.lock.byannotation.holder.RedisLockDefinitionHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author LENOVO
 * @title: RedisLockAspect
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/9/7 10:12
 */
@Aspect
@Component
@Slf4j
public class RedisLockAspect {
    /**
     * 线程池，维护keyAliveTime
     */
    private static final ScheduledExecutorService SCHEDULER = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("redisLock-schedule-pool").daemon(true).build());
    // 扫描的任务队列
    private static ConcurrentLinkedQueue<RedisLockDefinitionHolder> holderList = new ConcurrentLinkedQueue();
    @Autowired
    RedisTemplate redisTemplate;

    {
        // 两秒执行一次「续时」操作
        SCHEDULER.scheduleAtFixedRate(() -> {
            // 这里记得加 try-catch，否者报错后定时任务将不会再执行=-=
            Iterator<RedisLockDefinitionHolder> iterator = holderList.iterator();
            while (iterator.hasNext()) {
                RedisLockDefinitionHolder holder = iterator.next();
                // 判空
                if (holder == null) {
                    iterator.remove();
                    continue;
                }
                // 判断 key 是否还有效，无效的话进行移除
                if (redisTemplate.opsForValue().get(holder.getBusinessKey()) == null) {
                    iterator.remove();
                    continue;
                }
                // 超时重试次数，超过时给线程设定中断
                if (holder.getCurrentCount() > holder.getTryCount()) {
                    holder.getCurrentTread().interrupt();
                    iterator.remove();
                    continue;
                }
                // 判断是否进入最后三分之一时间
                long curTime = System.currentTimeMillis();
                boolean shouldExtend = (holder.getLastModifyTime() + holder.getModifyPeriod()) <= curTime;
                if (shouldExtend) {
                    holder.setLastModifyTime(curTime);
                    redisTemplate.expire(holder.getBusinessKey(), holder.getLockTime(), TimeUnit.SECONDS);
                    log.info("Thread:{} businessKey : [{}], try count : {}", holder.getCurrentTread().getName(), holder.getBusinessKey(), holder.getCurrentCount());
                    holder.setCurrentCount(holder.getCurrentCount() + 1);
                }
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

    /**
     * @annotation 中的路径表示拦截特定注解
     */
    @Pointcut("@annotation(com.ehang.redis.lock.byannotation.anno.RedisLockAnnotation)")
    public void redisLockPC() {
    }

    @Around(value = "redisLockPC()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        // 解析参数
        Method method = resolveMethod(pjp);
        RedisLockAnnotation annotation = method.getAnnotation(RedisLockAnnotation.class);
        RedisLockTypeEnum typeEnum = annotation.typeEnum();
        Object[] params = pjp.getArgs();
        String ukString = params[annotation.lockFiled()].toString();
        // 省略很多参数校验和判空
        String businessKey = typeEnum.getUniqueKey(ukString);
        String uniqueValue = UUID.randomUUID().toString();
        // 加锁
        Object result = null;
        try {
            Thread currentThread = Thread.currentThread();
            int tryLockCount = 1;
            while (true) {
                boolean isSuccess = redisTemplate.opsForValue().setIfAbsent(businessKey, uniqueValue);
                if (isSuccess) {
                    log.info("Thread：{} 取到锁！try num：{}", currentThread.getName(), tryLockCount);
                    break;
                } else {
                    // 注解中拿到加锁重试次数
                    if (tryLockCount > annotation.tryLockCount()) {
                        log.error("You can't do it，because another has get the lock =-=; try num:{}", tryLockCount);
                        throw new Exception("You can't do it，because another has get the lock =-=");
                    } else {
                        log.warn("Thread：{} 等待重试！try num：{}", currentThread.getName(), tryLockCount);
                        Thread.sleep(2000);
                    }
                }
                tryLockCount++;
            }

            redisTemplate.expire(businessKey, annotation.lockTime(), TimeUnit.SECONDS);

            // 将本次 Task 信息加入「延时」队列中
            holderList.add(new RedisLockDefinitionHolder(businessKey, annotation.lockTime(), System.currentTimeMillis(),
                    currentThread, annotation.tryCount()));
            // 执行业务操作
            result = pjp.proceed();
            // 线程被中断，抛出异常，中断此次请求
            if (currentThread.isInterrupted()) {
                throw new InterruptedException("You had been interrupted =-=");
            }
        } catch (InterruptedException e) {
            log.error("Interrupt exception, rollback transaction", e);
            throw new Exception("Interrupt exception, please send request again");
        } catch (Exception e) {
            log.error("has some error, please check again", e);
        } finally {
            // 请求结束后，强制删掉 key，释放锁
            redisTemplate.delete(businessKey);
            log.info("release the lock, businessKey is [{}]", businessKey);
        }
        return result;
    }

    private Method resolveMethod(ProceedingJoinPoint pjp) {
        try {
            Signature sig = pjp.getSignature();
            MethodSignature msig = null;
            if (!(sig instanceof MethodSignature)) {
                throw new IllegalArgumentException("该注解只能用于方法");
            }
            msig = (MethodSignature) sig;
            Object target = pjp.getTarget();
            Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
            return currentMethod;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }
}
