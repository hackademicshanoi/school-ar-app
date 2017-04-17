package com.example.daomy.uar2.model;

/**
 * Created by User on 4/6/2017.
 */

public class MSG {

    private Integer success;
    private String message;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private int idSchool;
    private int idSchool2;
    private String profilePicture;
    private String token;
    private String schoolName;
    private String schoolName2;

    /**
     * No args constructor for use in serialization
     */
    public MSG() {
    }

    /**
     * @param message
     * @param success
     */
    public MSG(Integer success, String message, String firstName, String lastName, String dateOfBirth,
               int idSchool, int idSchool2, String profilePicture, String token, String schoolName, String schoolName2) {
        super();
        this.success = success;
        this.message = message;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.idSchool = idSchool;
        this.idSchool2 = idSchool2;
        this.profilePicture = profilePicture;
        this.token = token;
        this.schoolName = schoolName;
        this.schoolName2 = schoolName2;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getIdSchool() {
        return idSchool;
    }

    public void setIdSchool(int idSchool) {
        this.idSchool = idSchool;
    }

    public int getIdSchool2() {
        return idSchool2;
    }

    public void setIdSchool2(int idSchool2) {
        this.idSchool2 = idSchool2;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolName2() {
        return schoolName2;
    }

    public void setSchoolName2(String schoolName2) {
        this.schoolName2 = schoolName2;
    }
}
