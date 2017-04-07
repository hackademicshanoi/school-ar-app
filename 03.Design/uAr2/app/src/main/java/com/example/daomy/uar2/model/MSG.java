package com.example.daomy.uar2.model;

/**
 * Created by User on 4/6/2017.
 */

public class MSG {

    private Integer success;
    private String message;

    /**
     * No args constructor for use in serialization
     */
    public MSG() {
    }

    /**
     * @param message
     * @param success
     */
    public MSG(Integer success, String message) {
        super();
        this.success = success;
        this.message = message;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
