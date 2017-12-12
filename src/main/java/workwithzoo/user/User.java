
package workwithzoo.user;

/**
 * Class of any user.
 * @author DantalioNxxi
 * @since 26.11.2017
 * @version 1.0.3
 */
public class User {
    public int id;
    private String firstname;
    private String lastname;

    protected User(int id, String fname, String lname){
        this.id = id;
        firstname = fname;
        lastname =  lname;
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
    
    @Override
    public String toString(){
        return "{Id "+id+" firstname "+firstname+" lastname "+lastname+"}";
    }
}
