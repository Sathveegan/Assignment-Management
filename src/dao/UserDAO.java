package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.User;
import util.DBConnection;

public class UserDAO {

	public static boolean login(String email, String password) {
		boolean result = false;
		try {
			Connection conn = DBConnection.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement("SELECT * FROM user WHERE email = ? AND password = ?");
			st.setString(1, email);
			st.setString(2, password);
			ResultSet rs = st.executeQuery();
			
			if(rs.next()) {
	            if(rs.getString("email").equalsIgnoreCase(email) && rs.getString("password").equals(password)) {
	            	result = true;
	            }
	        }
			conn.close();
		} catch (SQLException e) {
			result = false;
		}
		return result;
	}
	
	public static User getUserByEmail(String email) throws SQLException {
		Connection conn = DBConnection.getInstance().getConnection();
		PreparedStatement st = conn.prepareStatement("SELECT * FROM user WHERE email = ?");
		st.setString(1, email);
		ResultSet rs = st.executeQuery();
		User u = null;
		if(rs.next()) {
			u = new User(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("role"));
		}
		conn.close();
		return u;
	}
	
	public static User getUserById(int id) throws SQLException {
		Connection conn = DBConnection.getInstance().getConnection();
		PreparedStatement st = conn.prepareStatement("SELECT * FROM user WHERE id = ?");
		st.setInt(1, id);
		ResultSet rs = st.executeQuery();
		User u = null;
		if(rs.next()) {
			u = new User(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("role"));
		}
		conn.close();
		return u;
	}
}
