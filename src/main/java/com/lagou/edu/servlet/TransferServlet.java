package com.lagou.edu.servlet;

import com.lagou.edu.Transaction.TransactionEnabledAnnotationProxyManager;
import com.lagou.edu.Transaction.TransactionManager;
import com.lagou.edu.factory.ClassPathXmlApplicationContext;
import com.lagou.edu.utils.JsonUtils;
import com.lagou.edu.pojo.Result;
import com.lagou.edu.service.TransferService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 应癫
 */
@WebServlet(name="transferServlet",urlPatterns = "/transferServlet")
public class TransferServlet extends HttpServlet {


    private TransferService transferService = null ;

    private TransactionManager transactionManager = null;


    @Override
    public void init() throws ServletException {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("com.lagou.edu");
        try {
            transferService = (TransferService) classPathXmlApplicationContext.getBean("transferService");
            transactionManager = (TransactionManager) classPathXmlApplicationContext.getBean("transactionManager");
            TransactionEnabledAnnotationProxyManager transactionEnabledAnnotationProxyManager = new TransactionEnabledAnnotationProxyManager(transactionManager);
            transferService = (TransferService) transactionEnabledAnnotationProxyManager.proxyFor(transferService);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 设置请求体的字符编码
        req.setCharacterEncoding("UTF-8");

        String fromCardNo = req.getParameter("fromCardNo");
        String toCardNo = req.getParameter("toCardNo");
        String moneyStr = req.getParameter("money");
        int money = Integer.parseInt(moneyStr);

        Result result = new Result();

        try {

            // 2. 调用service层方法
            transferService.transfer(fromCardNo,toCardNo,money);
            result.setStatus("200");
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus("201");
            result.setMessage(e.toString());
        }

        // 响应
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().print(JsonUtils.object2Json(result));
    }
}
