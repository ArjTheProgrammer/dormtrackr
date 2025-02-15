package application.dormtrackr.model;

public class Dormer {
    private int dormerId;
    private String firstName;
    private String lastName;
    private String number;
    private String email;
    private int roomId;

    public Dormer(String firstName, String lastName, String number, String email, int roomId){
        this.firstName = firstName;
        this.lastName = lastName;
        this.number = number;
        this.email = email;
        this.roomId = roomId;
    }

    public Dormer(int dormerId, String firstName, String lastName, String number, String email, int roomId){
        this.dormerId = dormerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.number = number;
        this.email = email;
        this.roomId = roomId;
    }

    public int getDormerId() {
        return dormerId;
    }

    public void setDormerId(int dormerId) {
        this.dormerId = dormerId;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}


