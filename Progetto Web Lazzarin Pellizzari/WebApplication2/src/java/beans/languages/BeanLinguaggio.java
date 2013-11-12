package beans.languages;

import java.util.Locale;
import java.util.Map;
import javax.faces.context.FacesContext;

public class BeanLinguaggio {

    private Locale locale;

    public BeanLinguaggio() {
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {

        this.locale = locale;
    }

    public String changeLanguage() {
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            Map<String, String> parameters = ctx.getExternalContext().getRequestParameterMap();
            String varLocale = parameters.get("locale");
            if (varLocale != null) {
                Locale locale1 = new Locale(varLocale);
                ctx.getViewRoot().setLocale(locale1);
                setLocale(locale1);
                return "success";
            } else {
                return "missingParam";
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            return "missingParam";
        }
    }
}