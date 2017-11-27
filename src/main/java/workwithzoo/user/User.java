
package workwithzoo.user;

/**
 *
 * @author DantalioNxxi
 */
public class User {
    public int id;
    private String firstname;
    private String lastname;

    public User(int id, String fname, String lname){
        this.id = id;
        firstname = fname;
        lastname = lname;
    }
    
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
