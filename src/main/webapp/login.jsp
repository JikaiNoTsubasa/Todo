<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Todo</title>
    <head>
        <s:include value="/includes/header.jspf"/>
    </head>
</head>
<body>
    <div class="sb-container">
        <s:form action="register" method="post">
            <s:textfield name="strutsLoginName" label="User Name"/>
            <s:password name="strutsLoginPassword" label="Password"/>
            <s:hidden name="strutsAction" value="login"></s:hidden>
            <s:submit value="Login"/>
        </s:form>
    </div>
</body>