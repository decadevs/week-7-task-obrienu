<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c"%>


<!DOCTYPE html>
<html>
<head>
 <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
        integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z" crossorigin="anonymous">
    <link rel="stylesheet" href="/assets/stylesheets/main.css">
    <title>Sign In / Sign Up</title>
</head>
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
</style>
<body>

    <main class="login">
        <div class="container">
            <div class="row">
                <div class="col-md-7 left-side">
                    <h1 class="display-3 fbblue-color">
                        facebook
                    </h1>
                    <div class="text-left">
                        Facebook helps you connect and share <br> with the people in your life.
                    </div>
                    <c:set var = "reqStatusMessage" value = "${reqStatus}"/>
                    <c:if test="${not empty reqStatusMessage and reqStatusMessage.status}"> 
                    		<div class="text-center text-success">
                    			<c:out value = "${reqStatusMessage.getMessage()}"/>
                    		</div>
                    </c:if>
                </div>
                <div class="col-md-5">
                    <form method="POST" action="login">
                    	<c:set var = "reqStatusMessage" value = "${reqStatus}"/>
                    	<c:if test="${not empty reqStatusMessage and not reqStatusMessage.status}"> 
                    		<div class="text-center text-danger">
                    			<c:out value = "${reqStatusMessage.getMessage()}"/>
                    		</div>
                    	</c:if>
                    	
                        <div class="form-group">
                            <input type="email" placeholder="Email address or phone number"
                                class="form-control form-control-lg" name="login-email" id="login-email" aria-describedby="emailHelp">
                        </div>
                        <div class="form-group">
                            <input placeholder="Password" name="login-password" type="password" class="form-control form-control-lg"
                                id="login-password">
                        </div>

                        <button type="submit" class="btn btn-primary fbblue-bg btn-lg btn-block">Log In</button>
                        <div class="form-group">
                            <div class="text-center pt-2"><a href="">forgoten account ?</a> </div>
                        </div>
                        <hr>

                        <!-- Button trigger modal -->
                        <button type="button" class="btn btn-success btn-lg btn-block" data-toggle="modal"
                            data-target="#staticBackdrop">
                            Create New Account
                        </button>

                    </form>
                </div>
            </div>
        </div>

    </main>
    <!--Registration Modal -->
    <div class="modal fade" id="staticBackdrop" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
            <div class="modal-content">
                <div class="modal-header">
                    <h3 class="modal-title" id="exampleModalLabel">Sign Up</h3>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <form class="modal-body" action="signup" method="POST">
                    <div class="row">
                        <div class="col-6 form-group">
                            <input type="text" required name="firstname-reg" class="form-control" placeholder="First name">
                        </div>
                        <div class="col-6 form-group">
                            <input type="text" required name="lastname-reg" class="form-control" placeholder="Last name">
                        </div>
                        <div class="col-6 form-group">
                            <input type="email" required name="email-reg" class="form-control" placeholder="Email address">
                        </div>
                        <div class="col-6 form-group">
                            <input type="tel" required name="mobile-reg" class="form-control" placeholder="Mobile number">
                        </div>
                        <div class="col-6 form-group">
                            <input type="password" class="form-control" name="password-reg" required  placeholder="New password"
                                aria-describedby="passwordHelpBlock" minlength="8">
                        </div>
                        <div class="col-6 form-group">
                            <input type="password" class="form-control" required name="confirm-password-reg" placeholder="Confirm password"
                                aria-describedby="passwordHelpBlock" minlength="8">
                        </div>
                        <small class="col-12 "  id="passwordHelpBlock" class="form-text text-muted">
                                Your password must be 8-20 characters long, contain letters and numbers, and must not
                                contain spaces, special characters, or emoji.
                        </small>
                        <div class="col-12 form-group">
                            <label style="font-size: 0.8rem;" class="text-muted" for="reg-birthdate">Date of
                                Birth</label>
                            <input type="date" id="reg-birthdate" required class="form-control" name="birthdate-reg" placeholder="dd/mm/yyyy">
                        </div>

                        <div class="col-12">
                            <label style="font-size: 0.8rem;" class="text-muted" for="reg-date-birth">Gender</label>
                        </div>
                        <div class="col-4 form-group mr-2 custom-radio">
                            <label class="radio-label text-muted" for="genderMale">Male</label>
                            <input type="radio" value="Male" required id="genderMale" name="sex-reg" class="radio-button">
                        </div>
                        <div class="col-4 form-group custom-radio">
                            <label class="radio-label text-muted" for="genderFemale">Female</label>
                            <input type="radio" value="Female" required id="genderFemale" name="sex-reg" class="radio-button">
                        </div>

                    </div>
                    <small class="form-text text-muted">
                        By clicking Sign Up, you agree to our Terms, Data Policy and Cookie Policy. You may receive SMS
                        notifications from us and can opt out at any time.
                    </small>
                     <div class="modal-footer">
                    	<button type="submit" class="btn btn-success btn-block">Sign Up</button>
                	</div>
                </form>

               
            </div>
        </div>
    </div>



    <footer>

    </footer>
    <!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
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