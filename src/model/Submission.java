package model;

import java.util.Date;

public class Submission {
	private int id;
	private int assignment_id;
	private int student_id;
	private String upload_file;
	private Date submit_date;
	public Submission(int id, int assignment_id, int student_id, String upload_file, Date submit_date) {
		this.id = id;
		this.assignment_id = assignment_id;
		this.student_id = student_id;
		this.upload_file = upload_file;
		this.submit_date = submit_date;
	}
	public Submission(int assignment_id, int student_id, String upload_file, Date submit_date) {
		this.assignment_id = assignment_id;
		this.student_id = student_id;
		this.upload_file = upload_file;
		this.submit_date = submit_date;
	}
	public Submission() {
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAssignment_id() {
		return assignment_id;
	}
	public void setAssignment_id(int assignment_id) {
		this.assignment_id = assignment_id;
	}
	public int getStudent_id() {
		return student_id;
	}
	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}
	public String getUpload_file() {
		return upload_file;
	}
	public void setUpload_file(String upload_file) {
		this.upload_file = upload_file;
	}
	public Date getSubmit_date() {
		return submit_date;
	}
	public void setSubmit_date(Date submit_date) {
		this.submit_date = submit_date;
	}
}
