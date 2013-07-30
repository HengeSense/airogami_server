
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;

/**
 * Servlet implementation class createTempCredential
 */
public class createTempCredential extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public createTempCredential() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String username = request.getParameter("username");
		String filename = "policy.txt";
        
        // First get the file InputStream using ServletContext.getResourceAsStream() method.
		ServletContext context = getServletContext();
        InputStream is = context.getResourceAsStream(filename);
        // Convert InputStream to String
        String text = "";
        if (is != null) {
        	BufferedReader br = null;
    		StringBuilder sb = new StringBuilder();
    		String line;
    		try {
    			br = new BufferedReader(new InputStreamReader(is));
    			while ((line = br.readLine()) != null) {
    				sb.append(line);
    			}
    		} catch (IOException e) {
    			e.printStackTrace();
    		} finally {
    			if (br != null) {
    				try {
    					br.close();
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
    			}
    		}
    		text=sb.toString();
        }
        String uploaddir="";
        // Convert String to JSON format
        try {
			JSONObject jsonObj = new JSONObject(text);
			//generate a new timestamp with one extra hour(credential expirese in one hour...)
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR, 12);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String dateWithoutTime = sdf.format(cal.getTime());
			SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss.SSS");
			String timeWithoutDate = stf.format(cal.getTime());
			String dateNtime=dateWithoutTime+"T"+timeWithoutDate+"Z";
			jsonObj.put("expiration",dateNtime);
			//modify upload directory based on username
			JSONArray jsonArray=jsonObj.getJSONArray("conditions");
			jsonArray.getJSONArray(2).put(2,"uploads/"+username);
			text=jsonObj.toString();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
    	String key = "+9/DARod2Gr8npmNqEAPY6UGjxLW5hcFUAH68DnK";
    	String policy=null; String signature=null;    	
    	try {
			String[] result= SigGenerator.calculateRFC2104HMAC(text, key);
			policy= result[0];
			signature= result[1];
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Set the response MIME type of the response message
		response.setContentType("application/json");
	      // Write the response message in JSON format
	      JSONObject json = new JSONObject();
	      try
	      {
	             json.put("Policy",policy);
	             json.put("Signature",signature);
	             json.put("text",text);
	      }
	      catch (JSONException e)
	      { 
	    	  e.printStackTrace();
	      }
	      response.getWriter().write(json.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
