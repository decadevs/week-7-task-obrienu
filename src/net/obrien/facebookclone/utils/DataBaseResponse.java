package net.obrien.facebookclone.utils;

/**
 * Class serves as a return class for DAO method operations
 * the status field tells if the operation was successful or not
 * the code field is the status code of the operation
 * the data serves as the returned object for read operations 
 * @author decagon
 *
 */
public class DataBaseResponse {
    private boolean status;
    private String message;
    private int code;
    private Object data;

    public DataBaseResponse(boolean status, String message, int code) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
    
    public DataBaseResponse(boolean status, String message, int code, Object data) {
        this.status = status;
        this.message = message;
        this.code = code;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
    
    
}
