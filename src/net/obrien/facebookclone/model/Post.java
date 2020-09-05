package net.obrien.facebookclone.model;

public class Post {

	private int id;
	private String post;
	private User user ;
	private String created_at;
	private int numberOfLikes;
	private int numberOfComments;
	private boolean liked = false;
	
	
	public Post(int id, String post, User user, String created_at, int numberOfLikes, int numberOfComments, boolean liked) {

		this.id = id;
		this.post = post;
		this.user = user;
		this.created_at = created_at;
		this.numberOfLikes = numberOfLikes;
		this.numberOfComments = numberOfComments;
		this.liked = liked;
	}

	public int getId() {
		return id;
	}



	public String getPost() {
		return post;
	}




	public String getCreated_at() {
		return created_at;
	}



	public int getNumberOfLikes() {
		return numberOfLikes;
	}




	public int getNumberOfComments() {
		return numberOfComments;
	}


	public User getUser() {
		return user;
	}

	
	 public boolean isLiked() {
		return liked;
	}


		public static class PostBuilder {
	 		private int id;
	 		private String post;
	 		private User user ;
	 		private String created_at;
	 		private int numberOfLikes;
	 		private int numberOfComments;
	 		private boolean liked;

	 		public PostBuilder setId(int id) {
	 			this.id = id;
	 			return this;
	 		}



	 		public PostBuilder setPost(String post) {
	 			this.post = post;
	 			return this;
	 		}




	 		public PostBuilder setCreated_at(String created_at) {
	 			this.created_at = created_at;
	 			return this;
	 		}





	 		public PostBuilder setNumberOfLikes(int numberOfLikes) {
	 			this.numberOfLikes = numberOfLikes;
	 			return this;
	 		}


	 		public PostBuilder setNumberOfComments(int numberOfComments) {
	 			this.numberOfComments = numberOfComments;
	 			return this;
	 		}




	 		public PostBuilder setUser(User user) {
	 			this.user = user;
	 			return this;
	 		}
	 		
	 		
	 		
		 
		 
	 		public PostBuilder setLiked(boolean liked) {
				this.liked = liked;
				return this;
			}

	 		public PostBuilder setPost(Post post) {
	 			this.id = post.id;
	 			this.post = post.post;
	 			this.user = post.user;
	 			this.created_at = post.created_at;
	 			this.numberOfLikes = post.numberOfLikes;
	 			this.numberOfComments = post.numberOfComments;
	 			this.liked = post.liked;
	 			return this;
			}


			public Post build() {
	 			return new Post( id, post, user, created_at, numberOfLikes,  numberOfComments, liked);
	 		}
	 }
	
}
