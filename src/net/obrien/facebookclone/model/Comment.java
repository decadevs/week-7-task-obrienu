package net.obrien.facebookclone.model;

public class Comment {

	private int id;
	private String comment;
	private User user ;
	private String created_at;
	private int numberOfLikes;
	private int post_id;
	private boolean liked;
	
	public Comment(int id, String comment, User user, String created_at, int numberOfLikes, int post_id, boolean liked) {
	
		this.id = id;
		this.comment = comment;
		this.user = user;
		this.created_at = created_at;
		this.numberOfLikes = numberOfLikes;
		this.post_id = post_id;
		this.liked = liked;
	}


	public int getId() {
		return id;
	}



	public String getComment() {
		return comment;
	}


	public User getUser() {
		return user;
	}


	public String getCreated_at() {
		return created_at;
	}


	public int getNumberOfLikes() {
		return numberOfLikes;
	}

	public int getPost_id() {
		return post_id;
	}

	
	public boolean isLiked() {
		return liked;
	}




	public static class CommentBuilder {
		private int id;
		private String comment;
		private User user ;
		private String created_at;
		private int numberOfLikes;
		private int post_id;
		private boolean liked;
		
		public CommentBuilder() {
			
		}

		public CommentBuilder setId(int id) {
			this.id = id;
			return this;
		}

		public CommentBuilder setComment(String comment) {
			this.comment = comment;
			return this;
		}

		public CommentBuilder setUser(User user) {
			this.user = user;
			return this;
		}

		public CommentBuilder setCreated_at(String created_at) {
			this.created_at = created_at;
			return this;
			
		}

		public CommentBuilder setNumberOfLikes(int numberOfLikes) {
			this.numberOfLikes = numberOfLikes;
			return this;
		}

		public CommentBuilder setPost_id(int post_id) {
			this.post_id = post_id;
			return this;
		}

		public CommentBuilder setLiked(boolean liked) {
			this.liked = liked;
			return this;
		}
		
		public Comment build() {
			return new Comment( id, comment, user,  created_at, numberOfLikes, post_id, liked);
		}
		
		
		
	}
	
	
	
	
}
