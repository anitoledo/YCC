package yucatancountryclub.com.ycc.Model;

/**
 * Created by Zazu on 21/02/18.
 */

public class QR {
    private String pk;
    private String user;
    private String visitor_name;
    private String check_in;
    private String check_out;
    private String code;
    private String status;
    private String car;
    private String license_plate;
    private String car_color;

    public QR(String pk, String user, String visitor_name, String check_in, String check_out, String code, String status, String car, String license_plate, String car_color){
        this.pk = pk;
        this.user = user;
        this.visitor_name = visitor_name;
        this.check_in = check_in;
        this.check_out = check_out;
        this.code = code;
        this.status = status;
        this.car = car;
        this.license_plate = license_plate;
        this.car_color = car_color;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getVisitor_name() {
        return visitor_name;
    }

    public void setVisitor_name(String visitor_name) {
        this.visitor_name = visitor_name;
    }

    public String getCheck_in() {
        return check_in;
    }

    public void setCheck_in(String check_in) {
        this.check_in = check_in;
    }

    public String getCheck_out() {
        return check_out;
    }

    public void setCheck_out(String check_out) {
        this.check_out = check_out;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getLicense_plate() {
        return license_plate;
    }

    public void setLicense_plate(String license_plate) {
        this.license_plate = license_plate;
    }

    public String getCar_color() {
        return car_color;
    }

    public void setCar_color(String car_color) {
        this.car_color = car_color;
    }
}
