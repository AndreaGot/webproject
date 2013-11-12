<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="a4j" uri="http://richfaces.org/a4j"%>
<%@taglib prefix="rich" uri="http://richfaces.org/rich"%>

<f:view locale="#{language.locale}">
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title></title>
            <link rel="stylesheet" type="text/css" href="../css.css">
        </head>
        <body>
            <div id="container">
                <div id="header">

                </div>
                <div id="sidebar">
                    Men&ugrave;
                    <h:form>
                        - <h:commandLink action="add_group"><h:outputText value="#{msg.aggiungi_gruppo}"></h:outputText></h:commandLink><br>
                        - <h:outputLink value="/WebApplication2/logout"><h:outputText value="#{msg.logout}"></h:outputText></h:outputLink>
                    </h:form>
                </div>
                <div id="content">
                    <table style="text-align:center; width: 100%;">
                        <tr>
                            <td style="color: white; font-weight: bold; font-size: 16px;">
                                I GRUPPI DI QUESTO FORUM
                            </td>
                        </tr>
                    </table>
                    <h:form>
                        <h:commandButton action="reload" value=" Aggiorna "></h:commandButton>
                        <c:if test="${user.gruppi.size() == 0}">
                            Nessun gruppo in questo forum
                        </c:if>
                        <a4j:repeat value="#{user.gruppi}" var="g">
                            <div class="forum_main">
                                <h:commandLink action="#{posts.viewPosts}" actionListener="#{posts.setAttributeGroupId}" style="color: black; font-size: 13px;">
                                    <f:attribute name="id_gruppo" value="#{g.id_gruppo}"/>
                                    <jsp:useBean id="gruppo" scope="session" class="beans.Group" />
                                    <jsp:setProperty name="gruppo" property="id_gruppo" param="#{g.id_gruppo}"/>
                                    <h:outputText value="#{g.id_gruppo}"/>&nbsp;&nbsp;<h:outputText value="#{g.titolo}"/>
                                </h:commandLink><br>
                                <span style="font-style: italic;"><h:outputText value="#{msg.description}: #{g.description}"/></span><br>
                                <h:outputText value="Nuovi post: #{g.new_posts} "></h:outputText>
                                <h:outputText value="Nuovi file: #{g.new_files}"></h:outputText>
                            </div>
                            <div style="height:5px;"></div>
                        </a4j:repeat>
                    </h:form>
                    <a4j:form>
                        <rich:dataTable value="#{user.gruppi}" var="g">



                            <%--<rich:column>
                                <h:outputText value="#{g.id_gruppo}"/>
                            </rich:column>
                            <rich:column>
                                <h:commandLink action="#{posts.viewPosts}" actionListener="#{posts.setAttributeGroupId}" style="color: black">
                                    <f:attribute name="id_gruppo" value="#{g.id_gruppo}"/>
                                    <jsp:useBean id="gruppo" scope="session" class="beans.Group" />
                                    <jsp:setProperty name="gruppo" property="id_gruppo" param="#{g.id_gruppo}"/>
                                    <h:outputText value="#{g.titolo}"/>
                                    
                                </h:commandLink>
                            </rich:column>--%>
                        </rich:dataTable>
                    </a4j:form>
                    <br>
                </div>
                <div id="footer">

                </div>
            </div>
        </body>
    </html>
</f:view>
