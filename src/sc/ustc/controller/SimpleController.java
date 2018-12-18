package sc.ustc.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sc.ustc.bean.ActionBean;
import sc.ustc.tool.MyTools;

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

        PrintWriter out = response.getWriter();
        String actionName = request.getServletPath().toString();
        String[] actionUrl = actionName.split("/");
        actionName = actionUrl[actionUrl.length - 1];
        String path = this.getServletContext().getRealPath("WEB-INF/classes/controller.xml");
        MyTools mt = new MyTools();
        ActionBean actionBean = mt.readXml_dom(actionName.substring(0, actionName.indexOf(".")), path);
        if (actionBean != null) {

            String actionClass = actionBean.getActionClass();
            String actionMethod = actionBean.getActionMethod();


            try {
                // 反射机制获得action类名及方法

                out.print("<html>");
                out.print("<head>");
                out.print("<title>SimpleController</title>");
                out.print("</head>");
                out.print("<body>"+ actionMethod +"</body>");
                Class cl = Class.forName(actionClass);
                out.print("<body>2</body>");
                Method m = cl.getMethod(actionMethod, HttpServletRequest.class, HttpServletResponse.class);
                out.print("<body>3</body>");
                String result = null;
                out.print("<body>4</body>");
                result = (String) m.invoke(cl.newInstance(), request,response);
                out.print("<body>5</body>");
                out.print("</html>");

                String resT = actionBean.getActionResults().get("result" + result).get(1);
                String resV = actionBean.getActionResults().get("result" + result).get(2);

                if (resT.equals("foward")) {
                    request.getRequestDispatcher(resV).forward(request, response);
                } else if (resT.equals("redirect")) {
                    response.sendRedirect(resV);
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
            response.sendRedirect("/Login.jsp");
        }
    }

}