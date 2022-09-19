<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Persons</title>
    <head>
        <s:include value="/includes/header.jspf"/>
    </head>
</head>
<body>
<s:include value="/includes/menu.jsp"/>

<div class="sb-modal" id="modal">
    <div class="sb-modal-content">
        <span class="sb-close" id="modal-close">&times;</span>
        <div id="modal-content"></div>
    </div>
</div>
<div class="sb-container">
    <div class="sb-card sb-margin-top">
        <div class="sb-card-header">
            Persons
        </div>
        <div class="sb-card-body">
            <s:iterator var="i" step="1" value="persons">
                <div class="sb-person">
                    <a href="#" class="sb-person-username"><s:property value="username"></s:property></a><br>
                    <s:property value="firstname"></s:property><br>
                    <s:property value="lastname"></s:property><br><br>
                    <s:date name="lastVisited" format="dd/MM/yyyy" />
                    <div class="sb-person-logo">
                        <s:if test="canEditMol">
                            <img width="16px" height="16px" src="includes/img/icon_tube_24.png" />
                        </s:if>
                        <s:if test="admin">
                            <img width="16px" height="16px" src="includes/img/icon_crown_24.png" />
                        </s:if>
                    </div>
                </div>
            </s:iterator>
        </div>
    </div>
</div>
</body>
</html>
