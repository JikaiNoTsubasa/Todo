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
<s:include value="/includes/menu.jsp"/>
<script src="includes/js/project.js"></script>

<div class="sb-modal" id="modal">
    <div class="sb-modal-content">
        <span class="sb-close" id="modal-close">&times;</span>
        <div id="modal-content"></div>
    </div>
</div>

<div class="sb-container">
    <div class="sb-card sb-margin-top">
        <div class="sb-card-header">
            Projects
        </div>
        <div class="sb-card-body">
            <span class="sb-button" onclick="openModalNewProject();">New Project</span>
            <table class="sb-table sb-w50">
                <thead>
                    <tr>
                        <th>#ID</th>
                        <th>Name</th>
                        <th>Priority</th>
                    </tr>
                </thead>
                <tbody>
                    <s:iterator step="1" var="i" value="projects">
                        <tr>
                            <td><s:property value="id"></s:property></td>
                            <td><s:property value="name"></s:property></td>
                            <td><s:property value="priority"></s:property></td>
                        </tr>
                    </s:iterator>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script>
    $(function(){

        $("#modal-close").click(function(){
            closeModal();
        });

        var modal = document.getElementById("modal");
        window.onclick = function(event) {
            if (event.target == modal) {
                closeModal();
            }
        }

    });

    function openModalNewProject(){
        $.ajax({
            url: 'ajaxprojects',
            method: 'get',
            data: {
                strutsAction: 'newProjectForm'
            },
            success: function(response){
                $("#modal-content").html(response);
                openModal();
            },
            error: function(xhr, status){
                console.log("["+status+"]: "+xhr.responseText);
                $("#modal-content").html("["+status+"]: "+xhr.responseText);
                openModal();
            }
        });
    }

    function openModal(){
        $("#modal").show();
    }

    function closeModal(){
        $("#modal").hide();
    }
</script>
</body>
</html>

