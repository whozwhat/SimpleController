package sc.ustc.tool;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import sc.ustc.bean.ActionBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class CglibProxy {
    public static Object getProxy(Class<?> clazz) {
        CglibProxyX proxy = new CglibProxyX();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(proxy);
        return enhancer.create();
    }
}

class CglibProxyX implements MethodInterceptor {

    public Object intercept(Object object, Method method, Object[] args, MethodProxy proxy) throws Throwable {

        if (args.length == 3) {
            ActionBean actionBean = (ActionBean) args[2];
            String classN = actionBean.getActionInterceptors().get("0").get(1);
            String predo = actionBean.getActionInterceptors().get("0").get(2);
            String afterdo = actionBean.getActionInterceptors().get("0").get(3);
            Class<?> cl = Class.forName(classN);
            Method preMethod = cl.getMethod(predo, HttpServletRequest.class, HttpServletResponse.class, ActionBean.class);
            Method afterMethod = cl.getMethod(afterdo, HttpServletRequest.class, HttpServletResponse.class, ActionBean.class, String.class);

            preMethod.invoke(cl.newInstance(), (HttpServletRequest) args[0], (HttpServletResponse) args[1], actionBean);
            String result = (String) proxy.invokeSuper(object, args);
            afterMethod.invoke(cl.newInstance(), (HttpServletRequest) args[0], (HttpServletResponse) args[1], actionBean, result);
            return result;
        }else{
            Object result =  proxy.invokeSuper(object, args);
            return result;
        }
    }
}