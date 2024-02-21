package api.data;

import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

import api.enuns.GroupEnum;
import api.enuns.StatusEnum;
import api.util.JsonConvertible;

public class User extends Base<User> implements JsonConvertible<User> {
    private int idUser;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private GroupEnum group;
    private StatusEnum status;
    private Date createDate;
    private Date updateDate;

    public User(int idUser, String email, String firstName, String lastName, String password, GroupEnum group, StatusEnum status, Date createDate, Date updateDate) {
        this.idUser = idUser;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.group = group;
        this.status = status;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public User() {
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String userName) {
        this.email = userName;
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

    public GroupEnum getGroup() {
        return group;
    }

    public void setGroup(GroupEnum group) {
        this.group = group;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public JsonObjectBuilder toJson() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        JsonObjectBuilder brandBuilder = Json.createObjectBuilder()
                .add("id", idUser)
                .add("email", email)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("password", password)
                .add("group", group.getValue())
                .add("status", status.getValue())
                .add("createDate", dateFormat.format(createDate))
                .add("updateDate", dateFormat.format(updateDate));

        return brandBuilder;
    }

    @Override
    public User toObject(JsonObject json) {

        User user = new User();
        
        if (json.containsKey("id")) {
            user.setIdUser(json.getInt("id"));
        }
    
        if (json.containsKey("email")) {
            user.setEmail(json.getString("email"));
        }

        if (json.containsKey("firstName")) {
            user.setFirstName(json.getString("firstName"));
        }

        if (json.containsKey("lastName")) {
            user.setLastName(json.getString("lastName"));
        }

        if (json.containsKey("password")) {
            user.setPassword(json.getString("password"));
        }

        if (json.containsKey("group")) {
            GroupEnum group;
            try {
                group = GroupEnum.fromValue(json.getString("group"));
            } catch (Exception e) {
                group = GroupEnum.CLIENT;
            }
            
            user.setGroup(group);
        }
    
        if (json.containsKey("status")) {
            StatusEnum status;
            try {
                status = StatusEnum.fromValue(json.getString("status"));
            } catch (Exception e) {
                status = StatusEnum.ACTIVE;
            }
            
            user.setStatus(status);
        }
    
        return user;
    }
}
