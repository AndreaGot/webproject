<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@taglib prefix="a4j" uri="http://richfaces.org/a4j"%>
<%@taglib prefix="rich" uri="http://richfaces.org/rich"%>
<f:view locale="#{language.locale}">
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title><h:outputText value="#{msg.registrazione}" /></title>
            <link rel="stylesheet" type="text/css" href="css.css" />
        </head>
        <body>

            <center>
                <h1>
                    <h:outputText value="#{msg.registrazione}" />
                </h1>
                <h:outputText value="#{parseRegistration.errorMessages}" escape="false" />
                <a4j:form>
                    <table>
                        <tr>
                            <td>
                                <h:outputText value="#{msg.username}"/>
                            </td>
                            <td>
                                <h:inputText value="#{parseRegistration.username}"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <h:outputText value="#{msg.password}"/>
                            </td>
                            <td>
                                <h:inputSecret value="#{parseRegistration.passw1}"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <h:outputText value="#{msg.ripeti_password}"/>
                            </td>
                            <td>
                                <h:inputSecret value="#{parseRegistration.passw2}"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <h:outputText value="#{msg.email}"/>
                            </td>
                            <td>
                                <h:inputText value="#{parseRegistration.email}"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <h:outputText value="#{msg.via}"/>
                            </td>
                            <td>
                                <h:inputText value="#{parseRegistration.indirizzo}"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <h:outputText value="#{msg.citta}"/>
                            </td>
                            <td>
                                <h:inputText value="#{parseRegistration.citta}"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <h:outputText value="#{msg.provincia}"/>
                            </td>
                            <td>
                                <h:inputText value="#{parseRegistration.provincia}"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <h:outputText value="#{msg.stato}"/>
                            </td>
                            <td>
                                <h:inputText value="#{parseRegistration.stato}"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <h:outputText value="#{msg.nascita}"/>
                            </td>
                            <td>
                                <rich:calendar value="#{parseRegistration.nascita}"
                                               datePattern="dd/MM/yyyy"
                                               enableManualInput="true"
                                               cellWidth="24px"
                                               cellHeight="22px"
                                               style="width:200px"
                                               />
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <h:commandButton value="#{msg.registrati}" action="#{parseRegistration.validate}">
                                    <f:setPropertyActionListener target="#{parseRegistration.userNotUnique}" value="#{msg.error_username_not_unique}" />
                                    <f:setPropertyActionListener target="#{parseRegistration.userEmpty}" value="#{msg.error_username_empty}" />
                                    <f:setPropertyActionListener target="#{parseRegistration.emailInvalid}" value="#{msg.error_email_invalid}" />
                                    <f:setPropertyActionListener target="#{parseRegistration.indirizzoError}" value="#{msg.error_indirizzo}" />
                                    <f:setPropertyActionListener target="#{parseRegistration.cittaError}" value="#{msg.error_citta}" />
                                    <f:setPropertyActionListener target="#{parseRegistration.provinciaError}" value="#{msg.error_provincia}" />
                                    <f:setPropertyActionListener target="#{parseRegistration.statoError}" value="#{msg.error_stato}" />
                                    <f:setPropertyActionListener target="#{parseRegistration.nascitaError}" value="#{msg.error_data}" />
                                    <f:setPropertyActionListener target="#{parseRegistration.passNotEqual}" value="#{msg.error_no_match}" />
                                </h:commandButton>
                            </td>
                        </tr>
                    </table>
                </a4j:form>
            </center>
        </body>
    </html>
</f:view>