package model;

import java.util.Date;

public class Assignment {
	private int id;
	private String title;
	private String subject;
	private int year;
	private int semester;
	private Date post_date;
	private Date due_date;
	private String upload_file;
	private int lecturer_id;
	public Assignment() {
	}
	public Assignment(int id, String title, String subject, int year, int semester, Date post_date, Date due_date, String upload_file,
			int lecturer_id) {
		this.id = id;
		this.title = title;
		this.subject = subject;
		this.year = year;
		this.semester = semester;
		this.post_date = post_date;
		this.due_date = due_date;
		this.upload_file = upload_file;
		this.lecturer_id = lecturer_id;
	}
	public Assignment(String title, String subject, int year, int semester, Date post_date, Date due_date, String upload_file, int lecturer_id) {
		this.title = title;
		this.subject = subject;
		this.year = year;
		this.semester = semester;
		this.post_date = post_date;
		this.due_date = due_date;
		this.upload_file = upload_file;
		this.lecturer_id = lecturer_id;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getSemester() {
		return semester;
	}
	public void setSemester(int semester) {
		this.semester = semester;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getPost_date() {
		return post_date;
	}
	public void setPost_date(Date post_date) {
		this.post_date = post_date;
	}
	public Date getDue_date() {
		return due_date;
	}
	public void setDue_date(Date due_date) {
		this.due_date = due_date;
	}
	public String getUpload_file() {
		return upload_file;
	}
	public void setUpload_file(String upload_file) {
		this.upload_file = upload_file;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public int getLecturer_id() {
		return lecturer_id;
	}
	public void setLecturer_id(int lecturer_id) {
		this.lecturer_id = lecturer_id;
	}
}
