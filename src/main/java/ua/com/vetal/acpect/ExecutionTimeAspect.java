package ua.com.vetal.acpect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ua.com.vetal.controller.CatalogController;

@Aspect
@Component
public class ExecutionTimeAspect {
	static final Logger logger = LoggerFactory.getLogger(CatalogController.class);

	@Around("@annotation(LogExecutionTime)")
	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();

		Object proceed = joinPoint.proceed();

		long executionTime = System.currentTimeMillis() - start;

		//System.out.println(joinPoint.getSignature() + " executed in " + executionTime + "ms");
		logger.info(joinPoint.getSignature() + " executed in " + executionTime + "ms");
		return proceed;
	}
}
