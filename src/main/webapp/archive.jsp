<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Archive</title>
    <head>
        <s:include value="/includes/header.jspf"/>
    </head>
</head>
<body>
    <s:include value="/includes/menu.jsp"/>
    <script src="includes/js/entry.js"></script>

    <div class="sb-modal" id="modal">
        <div class="sb-modal-content">
            <span class="sb-close" id="modal-close">&times;</span>
            <div id="modal-content"></div>
        </div>
    </div>
    <div class="sb-container">
        <div class="sb-card sb-margin-top">
            <div class="sb-card-header">
                Archives
            </div>
            <div class="sb-card-body">
                <table class="sb-table sb-w100">
                    <thead>
                        <th>Name</th>
                        <th>Priority</th>
                        <th>Description</th>
                        <th>Project</th>
                        <th>Actions</th>
                    </thead>
                    <tbody>
                        <s:iterator var="i" step="1" value="archivedEntries">
                            <tr>
                                <td><s:property value="name"></s:property></td>
                                <td><s:property value="priority"></s:property></td>
                                <td><s:property value="description"></s:property></td>
                                <td><s:property value="project.name"></s:property></td>
                                <td>
                                    <span class="sb-link2" onclick="ajaxChangeStatus(<s:property value="id"></s:property>, 1);">Re-Open</span>
                                    <span class="sb-link2" onclick="openModalDelete(<s:property value="id"></s:property>);">Delete</span>
                                </td>
                            </tr>
                        </s:iterator>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
