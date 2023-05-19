package pt.estg.es.security;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.logging.Logger;

@Auditavel
@Interceptor
public class AuditedInterceptor
{
    @Inject
    private Logger log;

    public static boolean calledBefore = false;
    public static boolean calledAfter = false;

    @AroundInvoke
    public Object auditMethod(InvocationContext ctx) throws Exception {

        log.info("Interceptando: " + ctx.getMethod().getName());
        AuditAnnotation annotation1 = ctx.getMethod().getAnnotation(AuditAnnotation.class);
        if(annotation1 != null)
            log.info("Interceptando conf: " + annotation1.conf());

        calledBefore = true;
        Object result = ctx.proceed();
        calledAfter = true;
        return result;
    }
}