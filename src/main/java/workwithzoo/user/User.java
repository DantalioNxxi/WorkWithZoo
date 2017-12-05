
package workwithzoo.user;

/**
 *
 * @author DantalioNxxi
 */
public class User {
    public double id;
    private String firstname;
    private String lastname;

    public User(double id, String fname, String lname){
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
        return "Id "+id+" firstname "+firstname+" lastname "+lastname;
    }
}
