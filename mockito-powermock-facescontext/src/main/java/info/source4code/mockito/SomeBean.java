package info.source4code.mockito;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class SomeBean {

    public void addMessage(Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(severity, summary, detail));
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext()
                .invalidateSession();

        return "logout?faces-redirect=true";
    }
}
