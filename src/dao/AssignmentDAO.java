package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Assignment;
import util.DBConnection;

public class AssignmentDAO {
	
	public static String insertAssignment(Assignment assignment) throws SQLException{
		String res = null;
		try {
			Connection conn = DBConnection.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement("INSERT INTO assignment(title, subject, year, semester, post_date, due_date, upload_file, lecturer_id) VALUES (?,?,?,?,?,?,?,?);");
			st.setString(1, assignment.getTitle());
			st.setString(2, assignment.getSubject());
			st.setInt(3, assignment.getYear());
			st.setInt(4, assignment.getSemester());
			st.setDate(5, new java.sql.Date(assignment.getPost_date().getTime()));
			st.setDate(6, new java.sql.Date(assignment.getDue_date().getTime()));
			st.setString(7, assignment.getUpload_file());
			st.setInt(8, assignment.getLecturer_id());
			st.executeUpdate();
			conn.close();
			res = "Assignment is inserted";
		}
		catch(Exception e) {
			e.printStackTrace();
			res = "Assignment is not inserted";
		}
		return res;
	}
	
	public static ArrayList<Assignment> getLecturerAssignment(int lecturer_id) throws SQLException {
		Connection conn = DBConnection.getInstance().getConnection();
		PreparedStatement st = conn.prepareStatement("SELECT * FROM assignment WHERE lecturer_id = ? ORDER BY due_date ASC;");
		st.setInt(1, lecturer_id);
		ResultSet rs = st.executeQuery();
		ArrayList<Assignment> array = new ArrayList<>();
		while(rs.next()) {
			Assignment assign = new Assignment(rs.getInt("id"), rs.getString("title"), rs.getString("subject"), rs.getInt("year"), rs.getInt("semester"), rs.getDate("post_date"), rs.getDate("due_date"), rs.getString("upload_file"), rs.getInt("lecturer_id"));
			array.add(assign);
		}
		conn.close();
		return array; 
	}
	
	public static ArrayList<Assignment> getAssignment() throws SQLException {
		Connection conn = DBConnection.getInstance().getConnection();
		PreparedStatement st = conn.prepareStatement("SELECT * FROM assignment ORDER BY due_date ASC;");
		ResultSet rs = st.executeQuery();
		ArrayList<Assignment> array = new ArrayList<>();
		while(rs.next()) {
			Assignment assign = new Assignment(rs.getInt("id"), rs.getString("title"), rs.getString("subject"), rs.getInt("year"), rs.getInt("semester"), rs.getDate("post_date"), rs.getDate("due_date"), rs.getString("upload_file"), rs.getInt("lecturer_id"));
			array.add(assign);
		}
		conn.close();
		return array; 
	}
	
	public static String deleteAssignment(int id) throws SQLException {
		String res = null;
		try {
			Connection conn = DBConnection.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement("DELETE FROM assignment WHERE id = ?;");
			st.setInt(1, id);
			st.executeUpdate();
			conn.close();
			res = "Assignment is deleted";
		} 
		catch(Exception e) {
			e.printStackTrace();
			res = "Assignment is not deleted";
		}
		return res;
	}
	
	public static Assignment getAssignment(int id) throws SQLException {
		Connection conn = DBConnection.getInstance().getConnection();
		PreparedStatement st = conn.prepareStatement("SELECT * FROM assignment WHERE id = ?;");
		st.setInt(1, id);
		ResultSet rs = st.executeQuery();
		Assignment assign = null;
		if(rs.next()) {
			assign = new Assignment(rs.getInt("id"), rs.getString("title"), rs.getString("subject"), rs.getInt("year"), rs.getInt("semester"), rs.getDate("post_date"), rs.getDate("due_date"), rs.getString("upload_file"), rs.getInt("lecturer_id"));
		}
		conn.close();
		return assign; 
	}
	
	public static String updateAssignment(Assignment assignment) {
		try {
			Connection conn = DBConnection.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement("UPDATE assignment SET title = ?, subject = ?, year = ?, semester = ?, due_date = ?, upload_file = ? WHERE id = ?;");
			st.setString(1, assignment.getTitle());
			st.setString(2, assignment.getSubject());
			st.setInt(3, assignment.getYear());
			st.setInt(4, assignment.getSemester());
			st.setDate(5, new java.sql.Date(assignment.getDue_date().getTime()));
			st.setString(6, assignment.getUpload_file());
			st.setInt(7, assignment.getId());
			st.execute();
			conn.close();
			return "Assignment is updated.";
		} catch (SQLException e) {
			e.printStackTrace();
			return "Assignment is not updated.";
		}
	}
}
