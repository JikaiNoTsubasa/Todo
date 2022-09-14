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
    <script src="includes/js/entry.js"></script>

    <div class="sb-modal" id="modal">
        <div class="sb-modal-content">
            <span class="sb-close" id="modal-close">&times;</span>
            <div id="modal-content"></div>
        </div>
    </div>

    <div class="sb-container">
        <form id="form_new_entry">
            <div class="sb-margin-top">
                <table style="width: 100%;">
                    <tr>
                        <td>
                            <select id="select_project" class="sb-select">
                                <s:iterator var="i" step="1" value="projects">
                                    <option value="<s:property value="id"></s:property>"><s:property value="name"></s:property></option>
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

        <div id="entries">

        </div>

    </div>

    <script>
        $(function(){
            $("#form_new_entry").submit(function(e){
                e.preventDefault();
                let entry_name = $('#input_new_entry').val();
                let project_id = $('#select_project').val();
                $('#input_new_entry').val("");
                ajaxNewEntry(entry_name, project_id);
            });

            $("#modal-close").click(function(){
                closeModal();
            });

            var modal = document.getElementById("modal");
            window.onclick = function(event) {
                if (event.target == modal) {
                    closeModal();
                }
            }

            ajaxRefreshEntries();
        });

        function openModalShowEntry(id){
            $.ajax({
                url: 'ajaxentries',
                method: 'get',
                data: {
                    strutsAction: 'showEntry',
                    strutsEntryId: id
                },
                success: function(response){
                    console.log(response);
                    $("#modal-content").html(response);
                    $("#modal").show();
                },
                error: function(xhr, status){
                    console.log("["+status+"]: "+xhr.responseText);
                    $("#modal-content").html("["+status+"]: "+xhr.responseText);
                    $("#modal").show();
                }
            });
        }

        function openModalDelete(id){
            $.ajax({
                url: 'ajaxentries',
                method: 'get',
                data: {
                    strutsAction: 'askConfirmDelete',
                    strutsEntryId: id
                },
                success: function(response){
                    console.log(response);
                    $("#modal-content").html(response);
                    $("#modal").show();
                },
                error: function(xhr, status){
                    console.log("["+status+"]: "+xhr.responseText);
                    $("#modal-content").html("["+status+"]: "+xhr.responseText);
                    $("#modal").show();
                }
            });
        }

        function callbackDelete(){
            ajaxRefreshEntries();
        }

        function callbackChangeStatus(){
            ajaxRefreshEntries();
        }

        function closeModal(){
            $("#modal").hide();
        }
    </script>
</body>
</html>
