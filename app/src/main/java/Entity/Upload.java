package Entity;

public class Upload {
    private String petName;
    private String petType;
    private String petAge;
    private String petDescription;
    private String imgName;
    private String currentUser;
    private String address;

    public Upload(String petName, String petType, String petAge, String petDescription, String imgName, String currentUser, String address) {
        if (petName.trim().equals("")) {
            petName = "No Name";
        }
        this.petName = petName;
        this.petType = petType;
        this.petAge = petAge;
        this.petDescription = petDescription;
        this.imgName = imgName;
        this.currentUser = currentUser;
        this.address = address;
    }

    public Upload(String petName, String petType, String petAge, String petDescription, String imgName, String currentUser, String address, String id) {
        if (petName.trim().equals("")) {
            petName = "No Name";
        }
        this.petName = petName;
        this.petType = petType;
        this.petAge = petAge;
        this.petDescription = petDescription;
        this.imgName = imgName;
        this.currentUser = currentUser;
        this.address = address;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public Upload() {

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public String getPetDescription() {
        return petDescription;
    }

    public void setPetDescription(String petDescription) {
        this.petDescription = petDescription;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public String getPetAge() {
        return petAge;
    }

    public void setPetAge(String petAge) {
        this.petAge = petAge;
    }

    public String getDescription() {
        return petDescription;
    }

    public void setDescription(String description) {
        this.petDescription = description;
    }

}
