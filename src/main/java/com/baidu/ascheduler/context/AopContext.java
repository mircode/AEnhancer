package com.baidu.ascheduler.context;

import java.util.Random;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.context.ApplicationContext;

import com.baidu.ascheduler.entry.Sched;
import com.baidu.ascheduler.exception.IllegalParamException;
import com.baidu.ascheduler.ext.Cacheable;
import com.baidu.ascheduler.ext.Splitable;

/**
 * 处理上下文的AOP版本（从annotation中获取的信息）
 * 
 * hold variable to provide info of signature and annotation
 * 
 * @author xushuda
 *
 */
public class AopContext implements ProcessContext {

    private final long ctxId;
    private Sched annotation;
    private final Object[] clonedArgs; // 克隆的原参数
    private ProceedingJoinPoint jp; // join point
    // 子流程
    private Cacheable cacher = null;
    private Splitable spliter = null;

    public AopContext(Sched annotation, ProceedingJoinPoint jp, ApplicationContext context)
            throws InstantiationException, IllegalAccessException, IllegalParamException {
        ctxId = new Random().nextLong();

        this.annotation = annotation;
        this.jp = jp;
        clonedArgs = jp.getArgs().clone();

        if (annotation.cacher() != Sched.NULL.class) {
            cacher = annotation.cacher().newInstance();
            cacher.init(jp, context);
        }
        if (annotation.spliter() != Sched.NULL.class) {
            spliter = annotation.spliter().newInstance();
            spliter.init(jp, context);
        }
    }

    /**
     * 获取克隆的原始参数的引用，所以，直接修改这个数组的的内容不会对原对象产生影响<br>
     * 但是是不能修改数组中引用的对象
     * 
     * @return cloned的参数数组
     */
    @Override
    public Object[] getArgs() {
        return clonedArgs;
    }

    /**
     * 
     * @param args 参数
     * @return 返回按照给入参数调用原方法的结果
     * @throws Throwable 异常
     */
    @Override
    public Object invokeOrignialMethod(Object[] args) throws Throwable {
        return jp.proceed(args);
    }

    // /**
    // * 获取批量处理的限制
    // *
    // * @return
    // */
    // @Override
    // public int getBatchSize() {
    // return annotation.getBatchSize();
    // }

    /**
     * 获取忽略参数列表
     * 
     * @return
     */
    // @Override
    // public int[] getIgnoreList() {
    // return annotation.getIgnList();
    // }

    /**
     * 从参数集合中的一个对象获取key
     * 
     * @param paramElement
     * @return
     * @throws IllegalParamException
     */
    // @Override
    // public Object getKeyFromParam(Object paramElement) throws IllegalParamException {
    // return annotation.extParam(paramElement);
    // }

    /**
     * 从结果的一个对象取得Key
     * 
     * @param resultElement
     * @return
     * @throws IllegalParamException
     */
    // @Override
    // public Object getKeyFromResult(Object resultElement) throws IllegalParamException {
    // return annotation.extResult(resultElement);
    // }

    /**
     * 将原有参数中的聚合类参数替换为入参的对象
     * 
     * @param keys
     * @return
     */
    // @Override
    // public Object[] replaceArgsWithKeys(Object keys) {
    // clonedArgs[signature.getPosition()] = keys;
    // return clonedArgs;
    // }

    /**
     * 过期时间，秒
     * 
     * @return
     */
    // @Override
    // public int getExpiration() {
    // return annotation.getExpiration();
    // }

    /**
     * 对于AggrInvok中的集合对象参数
     * 
     * @return
     */
    // @Override
    // public Class<?> getAggParamType() {
    // return signature.getAggParamType();
    // }

    /**
     * 返回值类型
     * 
     * @return
     */
    // @Override
    // public Class<?> getRetType() {
    // return signature.getRetType();
    // }

    /**
     * 获取keySpace
     * 
     * @return
     */
    // @Override
    // public String getNameSpace() {
    // return annotation.getNameSpace().equals("") ? signature.getSignature() : annotation.getNameSpace();
    // }

    /**
     * 是否是聚合式调用
     * 
     * @return
     */
    // @Override
    // public boolean aggrInvok() {
    // return annotation.aggrInvok();
    // }

    @Override
    public int getRetry() {
        return annotation.retry();
    }

    /**
     * 
     * @return
     */
    // @Override
    // public boolean relyOnSeqResult() {
    // return annotation.isResultSequential();
    // }

    @Override
    public long getCtxId() {
        return ctxId;
    }

    /**
     * ( 获取集合类参数在原参数中的位置
     * 
     * @return
     */
    // @Override
    // public int getAggrPosition() {
    // return signature.getPosition();
    // }

    @Override
    public int getTimeout() {
        return annotation.timeout();
    }

    @Override
    public Cacheable getCacher() {
        return cacher;
    }

    @Override
    public Splitable getSpliter() {
        return spliter;
    }

    @Override
    public boolean split() {
        return spliter != null;
    }

    @Override
    public boolean cache() {
        return cacher != null;
    }

    @Override
    public boolean parallel() {
        return annotation.parallel();
    }

}
