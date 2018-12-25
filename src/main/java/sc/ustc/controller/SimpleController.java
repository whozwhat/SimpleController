package sc.ustc.controller;

import sc.ustc.bean.ActionBean;
import sc.ustc.bean.DIBean;
import sc.ustc.tool.CglibProxy;
import sc.ustc.tool.DITools;
import sc.ustc.tool.MyTools;
import sc.ustc.tool.Xml2Html;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SimpleController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html,charset=utf-8");
        response.setCharacterEncoding("utf-8");
        request.setCharacterEncoding("utf-8");

        String actionName = request.getServletPath().toString();
        String[] actionUrl = actionName.split("/");
        actionName = actionUrl[actionUrl.length - 1];
        String realPath = this.getServletContext().getRealPath("");
        String path = realPath+"WEB-INF/classes/controller.xml";
        System.out.println("controller_path:"+path);
        MyTools mt = new MyTools();
        ActionBean actionBean = mt.readXml_dom(actionName.substring(0, actionName.indexOf(".")), path);
        if (actionBean != null) {

            DIBean diBean = DITools.readXML(actionName.substring(0, actionName.indexOf(".")),realPath+"WEB-INF/classes/di.xml");
            System.out.println("diBean="+diBean);
            System.out.println("actionName="+actionName.substring(0, actionName.indexOf(".")));
            String actionClass = actionBean.getActionClass();
            String actionMethod = actionBean.getActionMethod();

            try {
                // 反射机制获得action类名及方法
                Class cl = Class.forName(actionClass);
                Method m = cl.getMethod(actionMethod, HttpServletRequest.class, HttpServletResponse.class, ActionBean.class);
                String result = null;
                System.out.println("class:"+cl);
                if (actionBean.getActionInterceptors() != null && !actionBean.getActionInterceptors().isEmpty()) {
                    // 创建代理实例
                    System.out.println("开启拦截器");
                    Object clc = CglibProxy.getProxy(cl);
                    if(diBean!=null&&diBean.getDiList().size()!=0){
                        DITools.di(clc,diBean,realPath+"WEB-INF/classes/di.xml");
                    }
                    // 调用代理方法
                    System.out.println("创建拦截器");
                    System.out.println("clc="+clc.toString());
                    result = (String) m.invoke(clc, request, response, actionBean);
                    System.out.println("result:" + result);
                } else {
                    result = (String) m.invoke(cl.newInstance(), request, response, actionBean);
                }
                String resT = actionBean.getActionResults().get("result" + result).get(1);
                String resV = actionBean.getActionResults().get("result" + result).get(2);

                if (resT.equals("forward")) {
                    System.out.println("forward");
                    if(resV.substring(resV.indexOf('.')).equals(".xml")){
                        resV = Xml2Html.getHtmlPath(realPath);
                    }
                    request.getRequestDispatcher(resV).forward(request, response);
                } else if (resT.equals("redirect")) {
                    response.sendRedirect(resV);
                }else if(resT.equals("script")) {
                    PrintWriter out = response.getWriter();
                    out.print("<html><head><meta charset='UTF-8'></head>");
                    out.print(resV);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        } else {
            PrintWriter out = response.getWriter();
            out.print("<html><head><meta charset='UTF-8'></head>");
            out.print("<script>alert('无法识别该请求!');window.location.href='Login.jsp'</script>");


        }
    }

}