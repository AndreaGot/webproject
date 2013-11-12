<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@taglib prefix="a4j" uri="http://richfaces.org/a4j"%>
<%@taglib prefix="rich" uri="http://richfaces.org/rich"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<f:view locale="#{language.locale}">
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title></title>
            <link rel="stylesheet" type="text/css" href="../css.css"  />
        </head>
        <body>
            <div id="container">
                <div id="header">
                </div>
                <div id="sidebar">
                    Men&ugrave;
                    <h:form>
                        - <h:commandLink action="backto_posts"><h:outputText value="#{msg.backto_posts}"/></h:commandLink><br/>
                        - <h:outputLink value="/WebApplication2/logout"><h:outputText value="#{msg.logout}"/></h:outputLink>
                    </h:form>
                </div>


                <div id="content">
                    <h:outputText value="#{posts.groupName}" style="text-transform:uppercase; color: white; font-weight: bold;"></h:outputText>
                        <center>
                        <a4j:form id="formInput">
                            <h:panelGroup style="height:320px;width:600px;" layout="block">
                                <rich:editor id="editor" width="600" height="300"
                                             viewMode="visual"
                                             value="#{posts.textnewpost}" theme="advanced">
                                    <f:param name="theme_advanced_toolbar_location" value="top"/>                               
                                    <f:param name="theme_advanced_toolbar_align" value="left"/>
                                </rich:editor><br>
                            </h:panelGroup>
                            <h:commandButton value="#{msg.salva_post}" action="#{posts.addPost}" actionListener="#{posts.setAttributeForAdd}">
                                <f:attribute name="identgruppo" value="#{group.id_gruppo}"/>
                                <f:attribute name="identuser" value="#{user.id}"/>
                            </h:commandButton>
                        </a4j:form>

                    </center>
                </div>
                <div id="footer">

                </div>
            </div>
        </body>
    </html>
</f:view>