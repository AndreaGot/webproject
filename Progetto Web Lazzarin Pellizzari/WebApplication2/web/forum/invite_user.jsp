<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
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
            <title></title>
            <link rel="stylesheet" type="text/css" href="../css.css" >
        </head>
        <body>
            <div id="container">
                <div id="header">

                </div>
                <div id="sidebar">
                    Men&ugrave;
                    <h:form>
                        - <h:commandLink action="back_to_posts">
                            <h:outputText value="#{msg.backto_posts}"/>
                        </h:commandLink><br>
                    </h:form>
                </div>
                <div id="content">
                    Seleziona, uno alla volta, gli utenti da invitare al gruppo

                    <a4j:form>
                        <a4j:repeat value="#{group.invitable_users(posts.currentIdGroup)}" var="u">
                            <% String s = request.getServerName();%>
                            <h:commandLink action="#{group.invite_user(posts.currentIdGroup, u)}" value="#{u.username}"></h:commandLink><br>
                        </a4j:repeat>
                    </a4j:form>
                </div>
                <div id="footer">

                </div>
            </div>
        </body>
    </html>
</f:view>