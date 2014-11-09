package info.source4code.jsf.primefaces.view;

import info.source4code.jsf.primefaces.model.User;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean
@SessionScoped
public class UserManager {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(UserManager.class);

    private String userName;
    private String password;
    private User user;

    public String login() {
        // lookup the user based on user name and password
        user = lookup(userName, password);

        if (user != null) {
            LOGGER.info("login successful for '{}'", userName);

            return "/secured/home.xhtml?faces-redirect=true";
        } else {
            LOGGER.info("login failed for '{}'", userName);
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Login failed", "Invalid or unknown credentials."));

            return null;
        }
    }

    public String logout() {
        String identifier = userName;

        // invalidate the session
        LOGGER.debug("invalidating session for '{}'", identifier);
        FacesContext.getCurrentInstance().getExternalContext()
                .invalidateSession();

        LOGGER.info("logout successful for '{}'", identifier);
        return "/logout.xhtml?faces-redirect=true";
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    private User lookup(String userName, String password) {
        User result = null;

        // code block to be replaced with actual retrieval of user
        if ("admin".equalsIgnoreCase(userName) && "1234".equals(password)) {
            result = new User("John", "Doe");
        }

        return result;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    // do not provide a setter for user!
}