package com.rigai.rigeye.task.commit.bean.notice.weixin;

public class WeiXinRep {
	
    private Boolean success=false;
    
    private String message;
    
    private String invaliduser;

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getInvaliduser() {
		return invaliduser;
	}

	public void setInvaliduser(String invaliduser) {
		this.invaliduser = invaliduser;
	}
	
	 @Override
	    public String toString() {
	        return "WeiXinRep{" +
	                "success=" + success +
	                ", message='" + message + '\'' +
	                ", invaliduser=" + invaliduser +
	                '}';
	    }

}
