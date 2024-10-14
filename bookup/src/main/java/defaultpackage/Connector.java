package defaultpackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Connector {
	private String dburl = "jdbc:mysql://localhost:3306/bookup?autoReconnect=true&userSSL=false";
	private String dbuname = "root";
	private String dbpassword = "Marlon1209@";
	private String dbdriver = "com.mysql.jdbc.Driver";
	
	public void loadDriver(String dbDriver) {
		try {
			Class.forName(dbDriver);
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		Connection con = null;
		try {
			con = DriverManager.getConnection(dburl, dbuname, dbpassword);
		} catch(SQLException e) {
			
		}
		return con;
	}
	
	public String insert(User user) {
		loadDriver(dbdriver);
		
		Connection con = getConnection();
		String sql = "insert into user values(?, ?, ?, ?)";
		String result = "New User Registered";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getEmail());
			ps.setString(4, user.getPhone());
			ps.executeUpdate();
		} catch(SQLException e) {
			result = "An error occured in insertion";
			e.printStackTrace();
		}
		return result;
	}
	
	public User authUser(String username, String password) {
		loadDriver(dbdriver);
		Connection con = getConnection();
		User user = null;
		
		String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
		
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()){
				user = new User();
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try{
				if(con != null)
					con.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		return user;
	}
	
	public Books getBookByName(String name) {
		if(name.equalsIgnoreCase("Sample Book")) {
			return new Books("Sample Book", "John Doe", "1234567", "Fiction", "$9.99");
		}
		return null;
	}
}
