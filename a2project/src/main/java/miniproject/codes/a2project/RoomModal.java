package miniproject.codes.a2project;

public class RoomModal {
    String room_no;
    String murl;
    String address;

    public RoomModal() {

    }

    public RoomModal(String room_no, String murl, String address) {
        this.room_no = room_no;
        this.murl = murl;
        this.address = address;
    }

    public String getRoom_no() {
        return room_no;
    }

    public void setRoom_no(String room_no) {
        this.room_no = room_no;
    }

    public String getMurl() {
        return murl;
    }

    public void setMurl(String murl) {
        this.murl = murl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
