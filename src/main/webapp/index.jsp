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
    <s:include value="/includes/menu.jspf"/>
    <script src="/includes/js/entry.js"></script>

    <div class="sb-container">

        <form id="form_new_entry">
            <div class="sb-margin-top">
                <table style="width: 100%;">
                    <tr>
                        <td>
                            <select id="select_project" class="sb-select">
                                <s:iterator value="projects">
                                    <option value="<s:property value="project_id"></s:property>"><s:property value="project_name"></s:property></option>
                                </s:iterator>
                            </select>
                        </td>
                        <td>
                            <input id="input_new_entry" class="sb-input" type="text" placeholder="New Entry">
                        </td>
                    </tr>
                </table>

            </div>
        </form>

        <div id="entries"></div>

    </div>
</body>
</html>
