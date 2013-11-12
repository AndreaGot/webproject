<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<f:view locale="#{languagesBean.locale}">
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
            <title>Forum Cooperativo</title>
            <link rel="stylesheet" type="text/css" href="css.css">
        </head>
        <body>
            <f:subview id="languages">
                <div id="lang">
                    <h:form>
                        <h:commandLink action="#{language.changeLanguage}" immediate="true">
                            <f:param name="locale" value="it"/>
                            <h:graphicImage url="#{msg.path_img_it}" alt="Italiano" title="Italiano" style="border:0px;"/>
                        </h:commandLink>&nbsp;
                        <h:commandLink action="#{language.changeLanguage}" immediate="true">
                            <f:param name="locale" value="en"/>
                            <h:graphicImage url="#{msg.path_img_en}" alt="English" title="English" style="border:0px;"/>
                        </h:commandLink>
                    </h:form>
                </div>
            </f:subview>
            <center>
                <h1><h:outputText value="Forum Cooperativo"/></h1>
                <div id="login_box">
                    <h:form>
                        <table>
                            <tr>
                                <td>
                                    Username:
                                </td>
                                <td>
                                    <h:inputText value="#{user.username}"></h:inputText>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        Password:
                                    </td>
                                    <td>
                                    <h:inputSecret value="#{user.passwd}"></h:inputSecret>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2" style="text-align: center;">
                                    <h:commandButton value="Login" action="#{user.login}"></h:commandButton>
                                    <h:commandButton value="#{msg.registrati}" action="register_user"></h:commandButton>
                                    </td>
                                </tr>
                            </table>
                    </h:form>
                </div>
            </center>
        </body>
    </html>
</f:view>