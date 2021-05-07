package Model;

public class Consumer {
    private String login;
    private String hashed;
    private String salt;
    private String email;
    private String address;
    private String telephone;

    public Consumer(String login, String hashed, String salt, String email, String address, String telephone) {
        this.login = login;
        this.hashed = hashed;
        this.salt = salt;
        this.email = email;
        this.address = address;
        this.telephone = telephone;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getHashed() {
        return hashed;
    }

    public void setHashed(String hashed) {
        this.hashed = hashed;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
