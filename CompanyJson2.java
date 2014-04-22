
import java.io.IOException;
import java.io.PrintWriter;
import java.util. * ;
import java.lang. * ;

import javax.servlet. * ;
import javax.servlet.http. * ;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql. * ;
import com.google.gson. * ;


public class CompanyJson2 extends HttpServlet {
	
	String operations="";
	String companyJson = "";
	//String sqlstr = "";
	String sqlString="";
	//String sqlTotal = "";
	//String rtotal = "";
	String orderKey="no";
	String orderSort="DESC";

	public void creatJson() {

		//联接数据库验证


		String driverName = "com.mysql.jdbc.Driver";
		String connName = "jdbc:mysql://115.28.178.166:3306/test";
		String username = "root";
		String password = "198123x";


		//用于装公司的Arrylist
		ArrayList < Company > Companys = new ArrayList < Company > ();

		//多个条件组合：行业+状态

		//sqlTotal="SELECT COUNT(*) FROM company WHERE NO IN(SELECT NO FROM company WHERE notes='"+companyNotes+"' and  status='"+companyStatus+"')";

		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		ResultSet rt = null;
		try {
			Class.forName(driverName);
			connection = DriverManager.getConnection(connName, username,
					password);
			stmt = connection.createStatement();
			
			rs = stmt.executeQuery(sqlString);
			while (rs.next()) {
				//循环创建并实例化公司
				Company company = new Company();
				company.setNo(rs.getString("no"));
				company.setName(rs.getString("name"));
				company.setIsStore(rs.getString("isstore"));
				company.setNotes(rs.getString("notes"));
				company.setStatus(rs.getString("status"));
				company.setProjectstate(rs.getString("projectstate"));
				company.setModifydatetime(rs.getString("modifydatetime"));		
				company.setClassifyName(rs.getString("classifyName"));
				
				//company.setStringAbstract(rs.getStirng("abstract");


				//向Arraylist 增加内容
				Companys.add(company);
			}

			//调用json包进行转换
			Gson gson = new Gson();
			companyJson = gson.toJson(Companys);

		} catch (Exception ex) {
			// 异常处理逻辑
			System.out.println("insert Error!!");

		}
		finally {
			try {
				if (connection != null) {
					connection.close();
					//out.println("<br><hr>"+"数据录入成功");
				}
			} catch (Exception e) {
				//throw e;
			}
		}
	}

	
	
	private static final long serialVersionUID = 1L;
	 @ Override
	public void doGet(HttpServletRequest request,	HttpServletResponse response)	throws IOException,	ServletException{
		
		String arg_name ="";		
		
		request.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession(true);
		 java.util.Enumeration arg_names = request.getParameterNames();
		sqlString="";
		while (arg_names.hasMoreElements()) {
					arg_name = String.valueOf(arg_names.nextElement());

					if (request.getParameter(arg_name).equals("")) {
							//valstr.append("NULL");					
					}else{
							//valstr.append(request.getParameter(arg_name));
							//valstr.append(",");
							sqlString+=arg_name+"='"+(request.getParameter(arg_name))+"'"+" and ";
		
			}
			
			

		}
		
		session.setAttribute("C", sqlString);
		sqlString=sqlString.substring(0,(sqlString.length()-4));
		sqlString= "select  no,name,comurl,isstore,notes,status,abstract,manager,projectstate,collectinfo,comborndate,comstage,comfundstatus,(SELECT IFNULL((DATE_FORMAT(modifydatetime,'%m-%d %H:%i')),'未更新'))  modifydatetime,classifyName from company where "+sqlString+" order by "+orderKey+" "+orderSort;
	
		

		
		creatJson();

		//session.setAttribute("A", colstr.toString());
		//session.setAttribute("B", valstr.toString());
		session.setAttribute("E", sqlString);

		response.setContentType("text/html;pageEncoding=utf-8;charset=utf-8");
		response.setCharacterEncoding("UTF-8");

		PrintWriter out = response.getWriter();
		//out.println("{\"compayJson\":"+companyJson+"}");
		out.println(companyJson);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException,	IOException{
		doGet(request, response);
	}
}




























