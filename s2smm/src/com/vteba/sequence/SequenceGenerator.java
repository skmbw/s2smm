package com.vteba.sequence;

import java.math.BigInteger;
import java.util.Random;

import org.springframework.stereotype.Component;

/**
 * sequence产生器。
 * @author yinlei 
 * @since 2012-7-9
 */
@Component
public class SequenceGenerator {
    public static final String SEQ_JR197 = "SEQ_JR197";
    
    private static final Random RANDOM = new Random();
    
//    @Autowired  
//    public OracleSequenceMaxValueIncrementer oracleSequenceMaxValueIncrementer;  
   
    /**
     * 根据业务类型获取sequence，并组装代码
     * @param bizProduct 业务
     * @param txnType 
     * @return
     */
//    public String nextString(String bizProduct , String txnType){  
//        String currval = oracleSequenceMaxValueIncrementer.nextStringValue();
//        return bizProduct + txnType  + currval; 
//    }
    
    /**
     * 根据sequence name获取sequence long值
     * @param seqName sequence名字
     * @return sequence long 值
     */
//    public long nextLong(String seqName){
//        oracleSequenceMaxValueIncrementer.setIncrementerName(seqName);
//        long currval = oracleSequenceMaxValueIncrementer.nextLongValue();
//        return currval; 
//    }
    
    /**
     * 如果没有建sequence，可以使用这个来生成主键。
     * @return sequence
     */
    public long nextSeqLong() {
        return System.nanoTime() + RANDOM.nextInt();// 当前时间精确到毫秒加上一个随机数
    }
    
    /**
     * 产生全局唯一的ID。
     * @author yinlei
     * date 2012-7-5 下午9:35:37
     */
    public String nextGUID(){
        return (new BigInteger(165, RANDOM)).toString(36).toUpperCase();
    }
}
