package net.obrien.facebookclone.model;

public class Comment {

	private int id;
	private String comment;
	private User user ;
	private String created_at;
	private int numberOfLikes;
	private int post_id;
	
	public Comment(int id, String comment, User user, String created_at, int numberOfLikes, int post_id) {
		super();
		this.id = id;
		this.comment = comment;
		this.user = user;
		this.created_at = created_at;
		this.numberOfLikes = numberOfLikes;
		this.post_id = post_id;
	}

	public Comment(String comment, User user, String created_at, int numberOfLikes, int post_id) {
		super();
		this.comment = comment;
		this.user = user;
		this.created_at = created_at;
		this.numberOfLikes = numberOfLikes;
		this.post_id = post_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public int getNumberOfLikes() {
		return numberOfLikes;
	}

	public void setNumberOfLikes(int numberOfLikes) {
		this.numberOfLikes = numberOfLikes;
	}

	public int getPost_id() {
		return post_id;
	}

	public void setPost_id(int post_id) {
		this.post_id = post_id;
	}
	
	
	
	
	
	
	
}
