package com.airogami.presentation.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.airogami.exception.AirogamiError;
import com.airogami.exception.AirogamiException;
import com.airogami.presentation.logic.DataManager;
import com.airogami.presentation.logic.ManagerUtils;
import com.airogami.presentation.logic.User;
import com.airogami.presentation.utilities.JSONUtils;
import com.airogami.presentation.utilities.SigGenerator;
import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;

public class DataServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	//for profile icon tokens
	private static final String ActionAccountIcon = "account.icon";
	//for plane message data tokens
	private static final String ActionMessageData = "message.data";

	/**
	 * Constructor of the object.
	 */
	public DataServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		User user = (User) request.getSession(true).getAttribute("user");
		if (user == null) {
			String json = JSONUtils.statusToJSONString(
					AirogamiError.Account_No_Signin_Status,
					AirogamiError.Account_No_Signin_Message);
			out.print(json);
		} else {
			String action = request.getParameter("action");		
			boolean succeed = true;
			if (action == null || action.length() == 0 || ActionAccountIcon.equals(action)) {
				out.print(ManagerUtils.dataManager.accountIcon(user.getAccountId()));
			}else if(ActionMessageData.equals(action)){
			}
			else {
				succeed = false;
			}
            if(succeed == false){
            	String json = JSONUtils.statusToJSONString(
						AirogamiError.Application_Input_Status,
						AirogamiError.Application_Input_Message);
				out.print(json);
            }
		}

		out.close();
	}
	

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the POST method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
	}

}
