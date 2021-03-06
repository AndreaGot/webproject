<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>

<f:view locale="#{language.locale}">
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title></title>
            <link rel="stylesheet" type="text/css" href="../css.css" />
        </head>
        <body>
            <div id="container">
                <div id="header">

                </div>
                <div id="sidebar">
                    Men&ugrave;
                    <h:form>

                    </h:form>
                </div>
                <div id="content">
                    <h:outputText value="#{msg.add_group}"></h:outputText><br>
                    <h:form>
                        <table>
                            <tr>
                                <td>
                                    <h:outputText value="#{msg.group_name}"></h:outputText>:
                                </td>
                                <td>
                                    <h:inputText value="#{user.grouptitle}"></h:inputText>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <h:outputText value="#{msg.description}"></h:outputText>:
                                </td>
                                <td>
                                    <h:inputText value="#{user.description}" style="width: 400px;"></h:inputText>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <h:commandButton action="#{user.addGroup}" value="#{msg.add_group}"/>
                                </td>
                            </tr>
                        </table>
                    </h:form>
                </div>
                <div id="footer">

                </div>
            </div>
        </body>
    </html>
</f:view>
