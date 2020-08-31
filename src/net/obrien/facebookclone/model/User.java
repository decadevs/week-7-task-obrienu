package net.obrien.facebookclone.model;

public class User {

    private String birthdate;
    private String email;
    private String firstname;
    private String lastname;
    private String mobile;
    private String password;
    private String sex;
    private String about;

    
    

    public User() {
	
	}

	public User(String birthdate, String email, String firstname, String lastname, String mobile, String password,
                      String sex) {
        this.birthdate = birthdate;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.mobile = mobile;
        this.sex = sex;
        this.password = password; 
        this.setBirthdate(birthdate);
    }

    public User(String about, String birthdate, String email, String firstname, String lastname, String mobile, String password,
                String sex) {
        this.about = about;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.mobile = mobile;
        this.sex = sex;
        this.password = password;       
        this.setBirthdate(birthdate);
    }
    
    public String getBirthdate() {
        return birthdate;
    }
    public void setBirthdate(String birthdate) {
    	if(birthdate.contains("/")) {
    		String[] dates = birthdate.split("/");
            this.birthdate = dates[2] + "-" + dates[1] + "-" + dates[0];
    	}else
    		this.birthdate = birthdate;
    	
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
