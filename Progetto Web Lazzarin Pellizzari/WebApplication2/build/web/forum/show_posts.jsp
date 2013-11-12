<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
            <link rel="stylesheet" type="text/css" href="../css.css">
        </head>
        <body>
            <div id="container">
                <div id="header"> 
                </div>
                <div id="sidebar">
                    Men&ugrave;
                    <h:form>
                        - <h:commandLink action="add_post"><h:outputText value="#{msg.aggiungi_post}"></h:outputText></h:commandLink><br>
                        - <h:commandLink action="add_file"><h:outputText value="#{msg.aggiungi_file}"></h:outputText></h:commandLink><br>
                        <c:if test="${user.isAdminGroup(posts.currentIdGroup)}">
                            - <h:commandLink action="edit_group"><h:outputText value="#{msg.modifica_titolo}"></h:outputText></h:commandLink><br>
                            - <a onclick="Richfaces.showModalPanel('delete_grp');" href="#"><h:outputText value="#{msg.elimina_gruppo}"></h:outputText></a><br>
                            - <h:commandLink action="invite_user"><h:outputText value="#{msg.invite_user}"></h:outputText></h:commandLink><br>
                        </c:if>
                        - <h:commandLink action="back_to_grp_list"><h:outputText value="#{msg.backto_grp_list}"></h:outputText></h:commandLink><br>
                        - <h:outputLink value="/WebApplication2/logout"><h:outputText value="#{msg.logout}"></h:outputText></h:outputLink>

                        <br>
                        <br>
                    </h:form>
                    <a style="color: black;" onclick="Richfaces.showModalPanel('pnl');"><h:outputText value="#{msg.show_files_attached}"></h:outputText></a><br><br>
                    <a style="color: black;" onclick="Richfaces.showModalPanel('users_in_grp');" href="#"><h:outputText value="#{msg.utenti_del_gruppo}"></h:outputText></a><br><br>


                    <a4j:form>
                        <rich:modalPanel id="delete_grp" width="300" height="150" resizeable="false">
                            <f:facet name="controls">
                                <h:graphicImage value="../../imgs/close.png" style="cursor:pointer" onclick="Richfaces.hideModalPanel('delete_grp')" />
                            </f:facet>
                            <f:facet name="header">
                                <h:outputText value="#{msg.title_delete_grp}"></h:outputText>
                            </f:facet>

                            <table style="width: 100%; height: 100%;">
                                <tr>
                                    <td colspan="3" style="text-align: center;">
                                        <h:outputText value="#{msg.sure_to_delete_grp}"></h:outputText><br>
                                        <h:outputText value="#{msg.not_undable}"></h:outputText>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="text-align: center;">
                                        <a4j:commandButton action="#{user.deleteGroup(posts.currentIdGroup)}" value="#{msg.si}"></a4j:commandButton>
                                    </td>
                                    <td>

                                    </td>
                                    <td style="text-align: center;">
                                        <h:commandButton onclick="Richfaces.hideModalPanel('delete_grp')" value="#{msg.no}"></h:commandButton>
                                    </td>
                                </tr>
                            </table>

                        </rich:modalPanel>
                    </a4j:form>             

                    <a4j:form>
                        <rich:modalPanel id="pnl" width="500" height="300" resizeable="false">
                            <f:facet name="controls">
                                <h:graphicImage value="../../imgs/close.png" style="cursor:pointer" onclick="Richfaces.hideModalPanel('pnl')" />
                            </f:facet>
                            <f:facet name="header">
                                <h:outputText value="#{msg.grp_files}" />
                            </f:facet>
                            <div style="overflow: scroll; width: 480px; height: 265px;">
                                <a4j:repeat value="#{posts.files}" var="f">
                                    - <a style="color: black;" href="files/<h:outputText value="#{posts.currentIdGroup}"></h:outputText>/<h:outputText value="#{f.name}"></h:outputText>"><h:outputText value="#{f.name} (#{f.owner}) "></h:outputText></a><h:commandLink style="color: black;" action="#{user.deleteFile(f.id)}" value="elimina file" rendered="#{user.canDeleteFile(posts.currentIdGroup, f.id_owner)}"></h:commandLink><br>
                                </a4j:repeat>
                                <c:if test="${posts.num_of_files == 0}">
                                    <h:outputText value="#{msg.no_files}"></h:outputText>
                                </c:if>
                            </div>
                        </rich:modalPanel>
                    </a4j:form>

                    <a4j:form>
                        <rich:modalPanel id="users_in_grp" width="300" height="150" resizeable="false">
                            <f:facet name="controls">
                                <h:graphicImage value="../../imgs/close.png" style="cursor:pointer" onclick="Richfaces.hideModalPanel('users_in_grp')" />
                            </f:facet>
                            <f:facet name="header">
                                <h:outputText value="#{msg.users_grp}"></h:outputText>
                            </f:facet>

                            <div style="overflow: scroll; width: 480px; height: 265px;">
                                <a4j:repeat value="#{group.users(posts.currentIdGroup)}" var="u">
                                    <h:outputText value="#{u.username}"></h:outputText>
                                    <h:outputText rendered="#{u.isAdminGroup(posts.currentIdGroup)}"> (Admin L1) </h:outputText> 
                                    <h:outputText style="color: black;" rendered="#{u.isAdminLevel2(posts.currentIdGroup)}"> (Admin L2) </h:outputText>
                                    <h:commandLink style="color: black;" action="#{u.deInvite(posts.currentIdGroup)}" rendered="#{(! u.admin_group) && user.isAdminGroup(posts.currentIdGroup)}"> (elimina utente) </h:commandLink>
                                    <h:commandLink style="color: black;" action="#{u.promoteAdminL2(posts.currentIdGroup)}" rendered="#{(user.isAdminGroup(posts.currentIdGroup) || user.isAdminLevel2(posts.currentIdGroup)) && (! u.isAdminLevel2(posts.currentIdGroup)) && ! u.isAdminGroup(posts.currentIdGroup)}"> (promuovi Admin L2) </h:commandLink>
                                    <h:commandLink style="color: black;" action="#{u.dePromoteAdminL2(posts.currentIdGroup)}" rendered="#{u.isAdminLevel2(posts.currentIdGroup) && user.isAdminGroup(posts.currentIdGroup)}"> (rimuovi privilegi) </h:commandLink>

                                    <br>
                                </a4j:repeat>
                            </div>

                        </rich:modalPanel>
                    </a4j:form>

                </div>


                <div id="content">
                    <table style="width: 100%; text-align: center;">
                        <tr>
                            <td>
                                <h:outputText value="#{posts.groupName}" style="text-transform:uppercase; text-align:center; color: white; font-weight: bold; font-size: 16px;"></h:outputText><br>
                            </td>
                        </tr>
                    </table>

                    <c:if test="${posts.posts.size() == 0}">
                        <h:outputText value="#{msg.no_posts}"></h:outputText>
                    </c:if>
                    <a4j:repeat value="#{posts.posts}" var="p" id="i_post">
                        <div style="width: 100%; background: #b0e0e6; border: solid 2px #0000cd;" class="rounder_corners">
                            <div style="float: left;">

                            </div>
                            <div style="float: left;">

                            </div>
                            <table cellspacing="0" style="width: 100%;"> 
                                <tr>
                                    <td style="width:150px; padding-left: 5px;">
                                        IMG<br>
                                        User: <a href="/WebApplication2/weather?localita=<h:outputText value="#{p.localita}"></h:outputText>"><h:outputText value="#{p.username}"></h:outputText></a><br>
                                        <h:outputText value="#{p.pubDate}" style="font-style: italic;">
                                            <f:convertDateTime pattern="#{msg.datestyle}"/>
                                        </h:outputText><br>
                                    </td>
                                    <td style="padding-right: 5px;">
                                        <h:outputText value="#{p.testo}" escape="false"></h:outputText>
                                    </td>
                                </tr>
                            </table>
                        </div><br>
                    </a4j:repeat>
                </div>
                <div id="footer">

                </div>
            </div>
        </body>
    </html>
</f:view>