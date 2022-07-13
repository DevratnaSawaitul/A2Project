package miniproject.codes.a2projectadmin;

import com.google.firebase.database.Exclude;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoticeModal {
    private String imageUrl;

    @Exclude
    public String getKey() {
        return mKey;
    }

    @Exclude
    public void setKey(String mKey) {
        this.mKey = mKey;
    }

    private String mKey;

    public String getOn() {
        return On;
    }

    public void setOn(String on) {
        On = on;
    }

    private String On;

    public NoticeModal() {

    }

    public NoticeModal(String imageUrl) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        this.On = formatter.format(date);
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
