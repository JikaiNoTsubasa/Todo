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
<script src="includes/js/person.js"></script>

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
            <div>
                <span class="sb-button" onclick="modalNewPerson();">New Person</span>
                <select onchange="filterPerson()" id="filter" class="sb-dropdown">
                    <option value="">Filter</option>
                    <option value="all">Show All</option>
                    <option value="admins">Show Admins</option>
                    <option value="mol">Show Edit Molecules</option>
                    <option value="date">Show Filled Dates</option>
                    <option value="fr">Show FR</option>
                </select>
                <input type="text" onkeyup="filterInput()" id="filterInput" class="sb-filter" placeholder="Search.."><span id="resultCount"></span>
            </div>
            <div id="persons" class="sb-margin-top">
                <s:iterator var="i" step="1" value="persons">
                    <div class="sb-person">
                        <span class="sb-person-username" onclick="modalEditPerson(<s:property value="id"></s:property>);"><s:property value="username"></s:property></span><br>
                        <s:property value="firstname"></s:property><br>
                        <s:property value="lastname"></s:property><br>
                        <s:property value="location"></s:property><br><br>
                        <s:date name="lastVisited" format="dd/MM/yyyy" />
                        <div class="sb-person-logo">
                            <s:if test="canEditMol">
                                <img class="img_tube" width="16px" height="16px" src="includes/img/icon_tube_24.png" alt="Can edit molecules"/>
                            </s:if>
                            <s:if test="admin">
                                <img class="img_admin" width="16px" height="16px" src="includes/img/icon_crown_24.png" alt="Is Admin"/>
                            </s:if>
                        </div>
                        <div class="sb-person-lang">
                            <s:property value="lang"></s:property>
                        </div>
                    </div>
                </s:iterator>
            </div>
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

    function filterInput(){
        let filter = $("#filterInput").val().toLowerCase();
        $('.sb-person').filter(function(){
            $(this).toggle($(this).text().toLowerCase().indexOf(filter) > -1);
            /*
            let regex = new RegExp(filter, 'ig');
            let text = $(this).text().replace(regex, (match, $1) =>{
                return '<span class="sb-highlight">' + match + '</span>';
            });
            $(this).html(text);

             */
        });
        displayResultCount();
    }

    function filterPerson(){
        let filter = $("#filter").val();
        switch (filter) {
            case "admins":
                $('.sb-person').each(function(i, obj){
                    if ($(this).find('.img_admin').length !== 0){
                        $(this).show();
                    }else{
                        $(this).hide();
                    }
                });
                break;
            case "mol":
                $('.sb-person').each(function(i, obj){
                    if ($(this).find('.img_tube').length !== 0){
                        $(this).show();
                    }else{
                        $(this).hide();
                    }
                });
                break;
            case "fr":
                $('.sb-person').each(function(i, obj){
                    if ($(this).find('.sb-person-lang:contains("FR")').length > 0){
                        $(this).show();
                    }else{
                        $(this).hide();
                    }
                });
                break;
            case "all":
                $('.sb-person').each(function(i, obj){
                    $(this).show();
                });
                break;
        }

        displayResultCount();
    }

    function displayResultCount(){
        let cnt = $('.sb-person:visible').length;
        $("#resultCount").html("Returned "+ cnt + " results");
    }

    function openModal(){
        $("#modal").show();
    }

    function closeModal(){
        $("#modal").hide();
    }

    function modalNewPerson(){
        $.ajax({
            url: 'ajaxpersons',
            method: 'get',
            data: {
                strutsAction: 'newPersonForm'
            },
            success: function(response){
                //console.log(response);
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

    function modalEditPerson(id){
        $.ajax({
            url: 'ajaxpersons',
            method: 'get',
            data: {
                strutsAction: 'editPersonForm',
                strutsPersonId: id
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
</script>
</body>
</html>
