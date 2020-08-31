package net.obrien.facebookclone.model;

public class CommentLike {
	private int user_id;
	private int comment_id;
	public CommentLike(int user_id, int comment_id) {
		super();
		this.user_id = user_id;
		this.comment_id = comment_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getComment_id() {
		return comment_id;
	}
	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}
	
	
}
