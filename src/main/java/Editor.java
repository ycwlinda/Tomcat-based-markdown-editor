import java.io.IOException;
import java.io.PrintWriter;
import java.sql.* ;
import java.util.*;
import java.util.Date;
import java.text.SimpleDateFormat;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

/**
 * ================== proj V6 ===============
 *
 */
public class Editor extends HttpServlet {
    /**
     * The Servlet constructor
     * 
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */
	private static final String dbUsername = "cs144";
	private static final String dbPw = "";
	
	private Connection con = null;
	private PreparedStatement statement = null;
	ResultSet rs = null;
    public Editor() {}

    public void init() throws ServletException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/CS144", dbUsername, dbPw);
		} catch(SQLException ex) {
			System.out.println(ex);
			return;
		} catch(ClassNotFoundException ex) {
			System.out.println(ex);
			return;
		}
    }
    
    public void destroy() {
		try { statement.close(); } catch (Exception e) {}
		try { con.close(); } catch (Exception e) {}
		try { rs.close(); } catch (Exception e) {}
    }

    /**
     * Handles HTTP GET requests
     * 
     * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request,
     *      HttpServletResponse response)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException 
    {
		String action = request.getParameter("action");
		
		// GET should only support open, preview, and list
		try {
			if (action.equals("open")) {
				openAction(request, response);
			} else if (action.equals("preview")) {
				previewAction(request, response);
			} else if (action.equals("list") || action.equals("close")) {
				listAction(request, response);
			} else {
				throw new ServletException("Action is invalid! " + action);
			}
		} catch(ServletException ex) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String msg = ex.toString();
			request.setAttribute("msg", msg);
			request.getRequestDispatcher("/error.jsp").forward(request, response);
		}
    }
    
    /**
     * Handles HTTP POST requests
     * 
     * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
     *      HttpServletResponse response)
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException 
    {
    	String action = request.getParameter("action");

    	// POST should support open, preview, and list, save and delete
		try {
			if (action.equals("open")) {
				openAction(request, response);
			} else if (action.equals("preview")) {
				previewAction(request, response);
			} else if (action.equals("list") || action.equals("close")) {
				listAction(request, response);
			} else if (action.equals("save")) {
				saveAction(request, response);
			} else if (action.equals("delete")) {
				deleteAction(request, response);
			} else {
				throw new ServletException("Action is invalid! " + action);
			}
		} catch(ServletException ex) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String msg = ex.toString();
			request.setAttribute("msg", msg);
			request.getRequestDispatcher("/error.jsp").forward(request, response);
		}
    }
    
    public void openAction(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		String username = request.getParameter("username");
		String id = request.getParameter("postid");
		String title = request.getParameter("title");
		String body = request.getParameter("body");

		if (username == null || id == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String msg = "Invalid parameters in open action";
			request.setAttribute("msg", msg);
			request.getRequestDispatcher("/error.jsp").forward(request, response);
			return;
		}

		int postid = Integer.parseInt(id);
		if (postid == 0 && title == null && body == null) {
			request.setAttribute("title", "");
			request.setAttribute("body", "");	
		} else if (title != null || body != null) {
			request.setAttribute("title", title);
			request.setAttribute("body", body);
		} else {

			try {
				statement = con.prepareStatement("SELECT * FROM Posts WHERE username = ? AND postid = ?");
				statement.setString(1, username);
				statement.setInt(2, postid);
				rs = statement.executeQuery();
				
				if (rs.next()) {
					title = rs.getString("title");
					body = rs.getString("body");
					request.setAttribute("title", title);
					request.setAttribute("body", body);
				} else {
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					String msg = "Such blog does not exist.";
					request.setAttribute("msg", msg);
					request.getRequestDispatcher("/error.jsp").forward(request, response);
					return;
				}
			} catch(SQLException ex) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				String msg = ex.toString();
				request.setAttribute("msg", msg);
				request.getRequestDispatcher("/error.jsp").forward(request, response);
				return;
			}
		}
		
		request.getRequestDispatcher("/edit.jsp").forward(request, response);
    }
    
    public void previewAction(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		String username = request.getParameter("username");
		String postid = request.getParameter("postid");
		String title = request.getParameter("title");
		String body = request.getParameter("body");

		if (username == null || postid == null || title == null || body == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String msg = "Invalid parameters in preview action";
			request.setAttribute("msg", msg);
			request.getRequestDispatcher("/error.jsp").forward(request, response);
			return;
		}
		
		Parser parser = Parser.builder().build();
		HtmlRenderer renderer = HtmlRenderer.builder().build();
		String titleHtml = renderer.render(parser.parse(title));
		String bodyHtml = renderer.render(parser.parse(body));
		
		request.setAttribute("titleHtml", titleHtml);
		request.setAttribute("bodyHtml", bodyHtml);
		request.setAttribute("body", body);
		request.setAttribute("title", title);
		request.getRequestDispatcher("/preview.jsp").forward(request, response);
    }
    
    public void listAction(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {   	
    	String username = request.getParameter("username");
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String msg = "Invalid parameter in list action";
			request.setAttribute("msg", msg);
			request.getRequestDispatcher("/error.jsp").forward(request, response);
			return;
		}

		try {
			statement = con.prepareStatement("SELECT * FROM Posts where username = ? ORDER BY postid");
			statement.setString(1, username);
			rs = statement.executeQuery();
			
			List<List<String>> posts = new ArrayList<List<String>>();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy' 'HH:mm:ss");
			while (rs.next()) {
				List<String> row = new ArrayList<String>();
				row.add(rs.getString("postid"));
				row.add(rs.getString("title"));
				Date created = rs.getTimestamp("created");
				row.add(simpleDateFormat.format(created));
				Date modified = rs.getTimestamp("modified");
				row.add(simpleDateFormat.format(modified));

			    posts.add(row);
			}			
			request.setAttribute("posts", posts);
			
		} catch(SQLException ex) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			String msg = ex.toString();
			request.setAttribute("msg", msg);
			request.getRequestDispatcher("/error.jsp").forward(request, response);
			return;
		}
		request.getRequestDispatcher("/list.jsp").forward(request, response);
    }


	public void saveAction(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException
	{
		String username = request.getParameter("username");
		String id = request.getParameter("postid");
		String title = request.getParameter("title");
		String body = request.getParameter("body");
		
		if (username == null || id == null || title == null || body == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String msg = "Invalid parameters in save action";
			request.setAttribute("msg", msg);
			request.getRequestDispatcher("/error.jsp").forward(request, response);
			return;
		}

		int postid = Integer.parseInt(id);
		try {
			if (postid <= 0) {
				statement = con.prepareStatement("SELECT MAX(postid) FROM Posts WHERE username = ?");
				statement.setString(1, username);
				rs = statement.executeQuery();
				String maxid = "";
				int newid = 1;
				if (rs.next()) {
					maxid = rs.getString("MAX(postid)");
					if(maxid != null) {
						newid = Integer.parseInt(maxid) + 1;
					}
				}
				statement.close();
				//save the content as a "new post"
				statement = con.prepareStatement("INSERT INTO Posts (username, postid, title, body, modified, created) VALUES (?, ?, ?, ?, ?, ?)");
				statement.setString(1, username);
				statement.setInt(2, newid);
				statement.setString(3, title);
				statement.setString(4, body);
				
				statement.setTimestamp(5, getCurrentTimeStamp());
				statement.setTimestamp(6, getCurrentTimeStamp());
				statement.executeUpdate();
			} else {
				statement = con.prepareStatement("SELECT * FROM Posts WHERE username = ? AND postid = ?");
				statement.setString(1, username);
				statement.setInt(2, postid);
				rs = statement.executeQuery();
				if (rs.next()) {  // if (username, postid) row exists in the database
					statement.close();
					// update 
					statement = con.prepareStatement(
						"UPDATE Posts SET title = ?, body = ?, modified = ? WHERE username = ? AND postid = ?"
					);	
					statement.setString(1, title);
					statement.setString(2, body);
					
					statement.setTimestamp(3, getCurrentTimeStamp());
					statement.setString(4, username);
					statement.setInt(5, postid);
					statement.executeUpdate();
				}  // if (username, postid) row does not exist, do nothing
			}
		}
		catch (SQLException ex) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			String msg = ex.toString();
			request.setAttribute("msg", msg);
			request.getRequestDispatcher("/error.jsp").forward(request, response);
			return;
		}
		
		listAction(request, response);
	}
	
	public void deleteAction(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException 
	{
		String username = request.getParameter("username");
		String id = request.getParameter("postid");
		
		if (username == null || id == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String msg = "Invalid parameters in delete action";
			request.setAttribute("msg", msg);
			request.getRequestDispatcher("/error.jsp").forward(request, response);
			return;
		}
		
		try {   // delete the corresponding post
			statement = con.prepareStatement("DELETE FROM Posts WHERE username = ? AND postid = ?");
			statement.setString(1, username);
			statement.setString(2, id);
			statement.executeUpdate();
		} catch (SQLException ex) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			String msg = ex.toString();
			request.setAttribute("msg", msg);
			request.getRequestDispatcher("/error.jsp").forward(request, response);
			return;
		}
		
		listAction(request, response);	
	}
	
	private static java.sql.Timestamp getCurrentTimeStamp() {
	    java.util.Date today = new java.util.Date();
	    return new java.sql.Timestamp(today.getTime());
	}            
}

