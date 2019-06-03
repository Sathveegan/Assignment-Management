package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Submission;
import util.DBConnection;

public class SubmissionDAO {

	public static String insertSubmision(Submission submission) throws SQLException{
		String res = null;
		try {
			Connection conn = DBConnection.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement("INSERT INTO submission(assignment_id, student_id, upload_file, submit_date) VALUES (?,?,?,?);");
			st.setInt(1, submission.getAssignment_id());
			st.setInt(2, submission.getStudent_id());
			st.setString(3, submission.getUpload_file());
			st.setDate(4, new java.sql.Date(submission.getSubmit_date().getTime()));
			st.executeUpdate();
			conn.close();
			res = "Submission Success.";
		}
		catch(Exception e) {
			e.printStackTrace();
			res = "Submission Failure.";
		}
		return res;
	}
	
	public static ArrayList<Submission> getSubmission(int student_id) throws SQLException {
		Connection conn = DBConnection.getInstance().getConnection();
		PreparedStatement st = conn.prepareStatement("SELECT * FROM submission WHERE student_id = ? ORDER BY submit_date ASC;");
		st.setInt(1, student_id);
		ResultSet rs = st.executeQuery();
		ArrayList<Submission> array = new ArrayList<>();
		while(rs.next()) {
			Submission submission = new Submission(rs.getInt("id"), rs.getInt("assignment_id"), rs.getInt("student_id"), rs.getString("upload_file"), rs.getDate("submit_date"));
			array.add(submission);
		}
		conn.close();
		return array; 
	}
	
	public static Submission getSubmissionById(int id) throws SQLException {
		Connection conn = DBConnection.getInstance().getConnection();
		PreparedStatement st = conn.prepareStatement("SELECT * FROM submission WHERE id = ?;");
		st.setInt(1, id);
		ResultSet rs = st.executeQuery();
		Submission submission = null;
		if(rs.next()) {
			submission = new Submission(rs.getInt("id"), rs.getInt("assignment_id"), rs.getInt("student_id"), rs.getString("upload_file"), rs.getDate("submit_date"));
		}
		conn.close();
		return submission; 
	}
	
	public static ArrayList<Submission> getSubmissionByAssignment(int id) throws SQLException {
		Connection conn = DBConnection.getInstance().getConnection();
		PreparedStatement st = conn.prepareStatement("SELECT * FROM submission WHERE assignment_id = ?;");
		st.setInt(1, id);
		ResultSet rs = st.executeQuery();
		ArrayList<Submission> array = new ArrayList<>();
		while(rs.next()) {
			Submission submission = new Submission(rs.getInt("id"), rs.getInt("assignment_id"), rs.getInt("student_id"), rs.getString("upload_file"), rs.getDate("submit_date"));
			array.add(submission);
		}
		conn.close();
		return array; 
	}
	
	public static String deleteSubmission(int id) throws SQLException {
		String res = null;
		try {
			Connection conn = DBConnection.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement("DELETE FROM submission WHERE id = ?;");
			st.setInt(1, id);
			st.executeUpdate();
			conn.close();
			res = "Submission is deleted";
		} 
		catch(Exception e) {
			e.printStackTrace();
			res = "Submission is not deleted";
		}
		return res;
	}
	
	public static String updateSubmission(Submission submission) {
		try {
			Connection conn = DBConnection.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement("UPDATE submission SET upload_file = ?, submit_date = ? WHERE id = ?;");
			st.setString(1, submission.getUpload_file());
			st.setDate(2, new java.sql.Date(submission.getSubmit_date().getTime()));
			st.setInt(3, submission.getId());
			st.execute();
			conn.close();
			return "Submission is updated.";
		} catch (SQLException e) {
			e.printStackTrace();
			return "Submission is not updated.";
		}
	}
}
