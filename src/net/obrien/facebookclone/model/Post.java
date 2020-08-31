package net.obrien.facebookclone.model;

public class Post {

	private int id;
	private String post;
	private User user ;
	private String created_at;
	private int numberOfLikes;
	private int numberOfComments;
	
	
	public Post(int id, String post, User user, String created_at, int numberOfLikes, int numberOfComments) {

		this.id = id;
		this.post = post;
		this.user = user;
		this.created_at = created_at;
		this.numberOfLikes = numberOfLikes;
		this.numberOfComments = numberOfComments;
	}
	


	public Post(String post, User user, String created_at, int numberOfLikes, int numberOfComments) {

		this.post = post;
		this.user = user;
		this.created_at = created_at;
		this.numberOfLikes = numberOfLikes;
		this.numberOfComments = numberOfComments;
	}




	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getPost() {
		return post;
	}


	public void setPost(String post) {
		this.post = post;
	}


	public User getUser_id() {
		return user;
	}


	public void setUser_id(User user) {
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


	public int getNumberOfComments() {
		return numberOfComments;
	}


	public void setNumberOfComments(int numberOfComments) {
		this.numberOfComments = numberOfComments;
	}
	
	
}
