package aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Aspect
public class TestAspect {

//    @Pointcut("execution(* *(..))")
//    private void pc() {
//
//    }

  @Around("execution(* *.*.set3DObjectForDraw(..))")
  public Object go(ProceedingJoinPoint thisJoinPoint) {
    Object result = null;
    long t0 = System.nanoTime();
    try {
      System.out.println("*************** " + thisJoinPoint.getSourceLocation().getFileName() + ":" + thisJoinPoint.getSignature());
      result = thisJoinPoint.proceed();
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    } finally {
      long dt = System.nanoTime() - t0;
      System.out.println("*************** " + thisJoinPoint.getSourceLocation().getFileName() + ":" + thisJoinPoint.getSignature() + "   deltaT=" + dt);
    }
    return result;
  }
}
