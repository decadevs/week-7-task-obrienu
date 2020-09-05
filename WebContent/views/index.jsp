<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="java.util.*, net.obrien.facebookclone.model.Post, net.obrien.facebookclone.model.User, net.obrien.facebookclone.utils.DataBaseResponse" %>

<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c"%>

<%

	
	DataBaseResponse queryData = (DataBaseResponse) request.getAttribute("queryData");
	ArrayList<Post> posts = null;
	
	if(queryData != null && queryData.isStatus()){
		posts	= (ArrayList<Post>)  queryData.getData();
	}
	
	pageContext.setAttribute("posts", posts);
 	
%>





<!doctype html>
<html lang="en">

<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
        integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z" crossorigin="anonymous">
    <link rel="stylesheet" href="./assets/stylesheets/main.css">
    <link rel="stylesheet" href="./index_style.css">
    <script src="https://kit.fontawesome.com/36a5ffd25f.js" crossorigin="anonymous"></script>
    <title>Hello, world!</title>

    <style>

.fbblue-color, .fa-facebook, .nav-icons:hover, .active div {
    color: #1877f2 !important;
  }
  
  .fbblue-bg {
    background-color: #1877f2 !important;
  }
  
  body {
    min-height: 100vh;
    background-color: #f0f2f5;
  }
  
  /* NAV UTILS */
  nav {
    background-color: #fff;
  }
  
  .bottom-boder {
    border-bottom: solid 1px grey;
  }
  
  /* FACEBOOK ICON */
  .fa-facebook {
    font-size: 3rem;
  }
  
  .nav-icons {
    font-size: 2rem;
    padding: 0 2rem;
  }
  
  /* NAV LINK */
  .nav-link {
    color: #222222;
    cursor: pointer;
  }
  
  .active {
    border-bottom: solid 2px #1877f2;
  }
  
  main.login form {
    padding: 1rem;
    background-color: #fff;
    border-radius: 10px;
  }
  
  main.login .container {
    padding-top: 8rem;
  }
  
  main.login .left-side {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-orient: vertical;
    -webkit-box-direction: normal;
        -ms-flex-flow: column;
            flex-flow: column;
    -webkit-box-pack: center;
        -ms-flex-pack: center;
            justify-content: center;
    font-weight: bold;
  }
  
  main.login .left-side .text-left {
    font-size: 1.5rem;
    font-weight: 500;
  }
  
  .custom-radio {
    width: 100%;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-orient: horizontal;
    -webkit-box-direction: normal;
        -ms-flex-flow: row;
            flex-flow: row;
    margin: 0 15px;
    -webkit-box-pack: justify;
        -ms-flex-pack: justify;
            justify-content: space-between;
    background-clip: padding-box;
    border: 1px solid #ced4da;
    border-radius: .25rem;
    -webkit-transition: border-color .15s ease-in-out,-webkit-box-shadow .15s ease-in-out;
    transition: border-color .15s ease-in-out,-webkit-box-shadow .15s ease-in-out;
    transition: border-color .15s ease-in-out,box-shadow .15s ease-in-out;
    transition: border-color .15s ease-in-out,box-shadow .15s ease-in-out,-webkit-box-shadow .15s ease-in-out;
    -webkit-box-align: center;
        -ms-flex-align: center;
            align-items: center;
    line-height: 1.5rem;
  }
  
  .radio-label {
    line-height: 2rem;
    margin-bottom: 0;
  }
  
  /* Profile page */
  main.profile {
    padding-top: 5rem;
  }
  
  .top-profile-section {
    background-color: #fff;
  }
  
  .profile-bg {
    height: 20rem;
    background-position: center;
    background-size: cover;
    position: relative;
    padding-top: 10rem;
  }
  
  .profile-bg .profile-image {
    height: 10rem;
    width: 10rem;
    border-radius: 50%;
    border: solid 3px #fff;
    background-position: center;
    background-size: cover;
    position: relative;
    margin: 0 auto;
    position: relative;
    top: 1rem;
  }
  
  .about .profile-name {
    font-size: 1.8rem;
    font-weight: 500;
  }
  
  /* ***** POST STYLING***** */
  .post-header, .comment .comment-header {
    height: 3rem;
  }
  
  .post-header .post-profile-image, .comment .comment-header .post-profile-image, .comment .comment-header .comment-profile-image {
    max-width: 3rem;
    height: 3rem;
    border-radius: 50%;
    border: 2px solid #fff;
    background-position: center;
    background-size: cover;
  }
  
  .post-header .user-name, .comment .comment-header .user-name {
    margin: 0;
  }
  
  .post-header .post-date, .comment .comment-header .post-date {
    margin: 0;
  }
  
  /* *****COMMENTS STYLING***** */
  .comment .comment-header .comment-date {
    margin: 0;
  }
  /*# sourceMappingURL=main.css.map */
  
  
  
@media (max-width: 576px) {
    .user-name{
        font-size: 1rem;
    }
    
}

    </style>
</head>

<body>
    <nav class="navbar navbar-expand-lg navbar-light fixed-top">
        <a class="navbar-brand" href="/"><i class="fab fa-facebook"></i></a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <form class="form-inline my-2 my-lg-0 mr-auto">
                <input class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">
                <button disabled class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
            </form>


            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link" href="profile?user_id=<c:out value="${sessionScope.user_id}"/>"><i class="nav-icons fas fa-user"></i></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="logout"><i class="nav-icons fas fa-sign-out-alt"></i></a>
                </li>
            </ul>
        </div>
    </nav>

    <main class="profile">

        <section class="container bottom-profile-section mt-1">
            <div class="row">
               
                <div class="card col-12 col-md-8 mt-2 mr-auto ml-auto" style="width: 30rem;">
                    <div class="card-body">
                        <h5 class="card-title">Whats on your mind ?</h5>
                        <form class="row" action="posts/post" method="post">
                            <div class="form-group col-12">
                                <textarea name="post"  minlength="2" maxlength="250" class="form-control" id="post-input" rows="3"></textarea>
                            </div>
                            <button type="submit" class="btn btn-primary btn-block">
                                Post
                            </button>
                        </form>
                        
                    </div>
                </div>
                
                 
                    <c:if test="${not empty queryData and queryData.status}"> 
                  		
                     <c:forEach var="post" items="${posts}">
						
            			<section id="posts" class="col-12 col-md-8 mx-auto">
		                    <div class="card  mt-2" style="width: 100%; padding: 1rem">
		                        <div class="card-body">
		                            <!-- POST HEADER -->
		                            <div class="post-header row">
		                                <div class="post-profile-image col-3"
		                                    style="background-image: url(https://i.ibb.co/PM3Gx27/2027366.png);">
		                                </div>
		                                <div class="col-7 mr-0 mr-md-auto">
		                                   <a href="profile?user_id=<c:out value="${post.user.id}"/>"> 
		                                   	<h5 class="user-name"><c:out value="${post.user.firstname}"/> <c:out value="${post.user.lastname}"/></h5> 
		                                   </a>
		                                    <p class="post-date"> <c:out value="${post.created_at}"/></p>
		                                </div>
		                                
		                                 <c:if test="${sessionScope.user_id == post.user.id}"> 
				                                <div class="dropdown col-2">
				                                    <!-- EDIT DROP DOWN BUTTON -->
				                                    <button class="btn dropdown-toggle" type="button" id="dropdownMenu<c:out value="${ post.id}"/>"
				                                        data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
				                                        ...
				                                    </button>
				                                    <div class="dropdown-menu" aria-labelledby="dropdownMenu<c:out value="${ post.id}"/>">
				                                    
				                                        <button class="dropdown-item " data-toggle="modal" data-target="#postEditModal<c:out value="${ post.id}"/>" type="button" >Edit</button>
				                                   
				                                        <form  method="POST" action="posts/delete?post_id=<c:out value="${ post.id}"/>">
				                                        <button class="dropdown-item" type="submit">Delete</button>
				                                        </form>
				                                    </div>
				                                </div>
		                                 </c:if> 
		                            </div>
		                            <hr>
		                            <!-- POST TEXT? CONTENT -->
		                            <div class="text-justify">
		                                <c:out value="${post.post}"/>
		                            </div>
		                            <div class="post-stat row mt-1">
		                             <c:choose>
		                            <c:when test = "${post.liked}">
		                                <form method="POST" action="posts/unlike?post_id=<c:out value="${ post.id}"/>" class="col-auto mr-auto text-muted">
		                                <button class="btn dropdown-toggle" type="submit">
		                                    <span> <c:out value="${post.numberOfLikes}"/>  Likes </span> 
		                                    <span ><i style="color: #1877f2;"class="fas fa-thumbs-up"></i></span>
		                                  </button>
		                                </form>
		                             </c:when>
		                             <c:otherwise>
		                             <form method="POST" action="posts/like?post_id=<c:out value="${ post.id}"/>" class="col-auto mr-auto text-muted">
		                                 <button class="btn dropdown-toggle" type="submit">
		                                    <span> <c:out value="${post.numberOfLikes}"/>  Likes </span> 
		                                    <span ><i class="fas fa-thumbs-up"></i></span>
		                                  </button>
		                                </form>
		                             </c:otherwise>
		                              </c:choose>
		                                <div class="col-auto text-muted">
		                                      <a class="" href="post?post_id=<c:out value="${post.id}"/>&page=1"
		                                       aria-expanded="false" aria-controls="postIdCollapse">
		                                        <c:out value="${post.numberOfComments}"/>  Comments
		                                    </a>
		                                </div>
		
		                            </div>
		                            <hr>
		                        </div>
		                        <form method="POST" action="comments/comment?post_id=<c:out value="${post.id}"/> ">
		                            <div class="form-group col-12 mr-auto ml-auto comment-textarea">
		                                <textarea name="comment" class="form-control" id="exampleFormControlTextarea1" rows="2">Comment</textarea>
		                            </div>
		                            <button type="submit" class="btn btn-primary ml-3">Comment</button>
		                        </form>
		                        
		                    </div>
               			 </section>
            			
            			<c:if test="${sessionScope.user_id == post.user.id}">
            			  <!--POST EDIT Modal -->
							    <div class="modal fade" id="postEditModal<c:out value="${post.id}"/>" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
							        <div class="modal-dialog modal-dialog-centered">
							          <div class="modal-content">
							            <div class="modal-header">
							              <h5 class="modal-title" id="exampleModalLabel">Edit Post</h5>
							              <button type="button" class="close" data-dismiss="modal" aria-label="Close">
							                <span aria-hidden="true">&times;</span>
							              </button>
							            </div>
							            <div class="modal-body">
							                <form action="posts/edit?post_id=<c:out value="${ post.id}"/>" method="POST">
							                  <div class="form-group">
							                    <label for="message-text" class="col-form-label">Message:</label>
							                    <textarea name="post" class="form-control" id="message-text"><c:out value="${ post.post}"/></textarea>
							                  </div>
							                  <button type="submit" class="btn btn-primary">Save changes</button>
							                </form>
							              </div>
							          
							          </div>
							        </div>
							      </div>
						</c:if>
        				</c:forEach>
                    		
                    </c:if>

     	</div>



        </section>

    </main>




  

 


    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"
        integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj"
        crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"
        integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN"
        crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"
        integrity="sha384-B4gt1jrGC7Jh4AgTPSdUtOBvfO8shuf57BaghqFfPlYxofvL8/KUEfYiJOMMV+rV"
        crossorigin="anonymous"></script>
</body>

</html>