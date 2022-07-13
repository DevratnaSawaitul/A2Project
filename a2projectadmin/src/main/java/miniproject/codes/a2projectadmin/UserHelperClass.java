package miniproject.codes.a2projectadmin;

public class UserHelperClass {
    String fullName, clgRollNo, email, gender, date, phoneNo;

    public UserHelperClass() {

    }

    public UserHelperClass(String fullName, String clgRollNo, String email, String gender, String date, String phoneNo) {
        this.fullName = fullName;
        this.clgRollNo = clgRollNo;
        this.email = email;
        this.gender = gender;
        this.date = date;
        this.phoneNo = phoneNo;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getClgRollNo() {
        return clgRollNo;
    }

    public void setClgRollNo(String clgRollNo) {
        this.clgRollNo = clgRollNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}

