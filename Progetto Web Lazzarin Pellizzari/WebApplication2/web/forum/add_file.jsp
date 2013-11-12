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
                    Posts
                </div>
                <div id="sidebar">
                    Men&ugrave;
                    <h:form>
                        - <h:commandLink action="back_to_posts">
                            <h:outputText value="#{msg.backto_posts}"/>
                        </h:commandLink><br>
                        - <h:outputLink value="/WebApplication2/logout"><h:outputText value="#{msg.logout}"/></h:outputLink>
                    </h:form>
                </div>


                <div id="content">
                    <table style="width: 100%;">
                        <tr>
                            <td style="text-align: center; font-size: 16px; color:white;">
                                <h:outputText value="#{msg.add_file}: "></h:outputText> <h:outputText value="#{posts.groupName}" style="text-transform:uppercase; color: white; font-weight: bold;"></h:outputText>
                                </td>
                            </tr>
                        </table>
                        <center>
                        <a4j:form>
                            <rich:fileUpload fileUploadListener="#{file.listener}"
                                             maxFilesQuantity="10"
                                             id="upload"
                                             immediateUpload="false">
                            </rich:fileUpload>
                        </a4j:form>
                    </center>
                </div>
                <div id="footer">

                </div>
            </div>
        </body>
    </html>
</f:view>