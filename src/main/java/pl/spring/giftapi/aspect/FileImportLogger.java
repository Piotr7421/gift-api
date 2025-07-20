package pl.spring.giftapi.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Aspect
@Component
@Slf4j
public class FileImportLogger {

    @Pointcut("execution(* pl.spring.giftapi.service.KidsFileImportService.importKidsFromFile(..)) && args(csvPath)")
    public void fileImportPointcut(Path csvPath) {
    }

    @Around("fileImportPointcut(csvPath)")
    public Object logFileImport(ProceedingJoinPoint joinPoint, Path csvPath) throws Throwable {
        long startTime = System.currentTimeMillis();
        String fileName = csvPath.getFileName().toString();

        log.info("Starting file import: {}", fileName);

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            log.info("File import completed successfully: {} in {}ms", fileName, duration);
            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("File import failed: {} after {}ms", fileName, duration, e);
            throw e;
        }
    }
}