package examples.effective_java;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;

public class UsePrimitive {
    public static void main(String[] args) {
        var dec = new BigDecimal("2.54684564321321654984313565456465464546565465454684");
        System.out.println(dec.round(MathContext.DECIMAL32));
        System.out.println(dec.round(MathContext.DECIMAL64));
        System.out.println(dec.round(MathContext.DECIMAL128));
        System.out.println(dec.round(MathContext.UNLIMITED));
        System.out.println(new BigDecimal("1.0").equals(new BigDecimal("1.00")));  // false
        System.out.println(new BigDecimal("1.0").compareTo(new BigDecimal("1.00")));  // 0
        System.out.println(new Double("1.0").equals(new Double("1.00")));  // true
        System.out.println(new Double("1.0").compareTo(new Double("1.00")));  // 0

        Date start1 = new Date();
        Long sum1 = 0L;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            sum1 += i;
        }
        Date end1 = new Date();
        System.out.println(sum1);
        System.out.println(end1.getTime() - start1.getTime() + " ms");
        Date start2 = new Date();
        long sum2 = 0L;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            sum2 += i;
        }
        Date end2 = new Date();
        System.out.println(sum2);
        System.out.println(end2.getTime() - start2.getTime() + " ms");  // Работает в 11 раз быстрее
    }
}
