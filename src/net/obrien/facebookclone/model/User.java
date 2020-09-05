package net.obrien.facebookclone.model;

public class User {

	private int id;
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
    
    

	public User(int id, String birthdate, String email, String firstname, String lastname, String mobile,
			String password, String sex, String about) {
		super();
		this.id = id;
		this.birthdate = birthdate;
		this.email = email;
		this.firstname = firstname;
		this.lastname = lastname;
		this.mobile = mobile;
		this.password = password;
		this.sex = sex;
		this.about = about;
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

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getMobile() {
        return mobile;
    }
 
    public String getPassword() {
        return password;
    }

    public String getSex() {
        return sex;
    }
  

    public String getAbout() {
        return about;
    }

  

	public int getId() {
		return id;
	}


    
	 public static class UserBuilder 
	    {
		 	private int id;
		    private String birthdate;
		    private String email;
		    private String firstname;
		    private String lastname;
		    private String mobile;
		    private String password;
		    private String sex;
		    private String about;
	 
	        public UserBuilder() {
	          
	        }
	        
	    
	        
	        public UserBuilder setBirthdate(String birthdate) {
	        	if(birthdate.contains("/")) {
	        		String[] dates = birthdate.split("/");
	                this.birthdate = dates[2] + "-" + dates[1] + "-" + dates[0];
	        	}else
	        		this.birthdate = birthdate;
	        	return this;
	        }
	    
	        public UserBuilder setEmail(String email) {
	            this.email = email;
	            return this;
	        }
	     
	        public UserBuilder setFirstname(String firstname) {
	            this.firstname = firstname;
	            return this;
	        }
	   
	        public UserBuilder setLastname(String lastname) {
	            this.lastname = lastname;
	            return this;
	        }
	    
	        public UserBuilder setMobile(String mobile) {
	            this.mobile = mobile;
	            return this;
	        }
	     
	        public UserBuilder setPassword(String password) {
	            this.password = password;
	            return this;
	        }
	    
	        public UserBuilder setSex(String sex) {
	            this.sex = sex;
	            return this;
	        }


	        public UserBuilder setAbout(String about) {
	            this.about = about;
	            return this;
	        }

	   
	    	public UserBuilder setId(int id) {
	    		this.id = id;
	    		return this;
	    	}
	        
	        //Return the finally consrcuted User object
	        public User build() {
	           return  new User(id, birthdate,  email, firstname, lastname, mobile,
	        			 password, sex, about);
	       
	        }
	      
	    }
}
