function refreshPersons(){
    $.ajax({
        url: 'ajaxpersons',
        method: 'get',
        data: {
            strutsAction: 'refreshPersons'
        },
        success: function(response){
            $("#persons").html(response);
        }
    });
}

function createPerson(){
    $.ajax({
        url: 'ajaxpersons',
        method: 'post',
        data: {
            strutsAction: 'createPerson',
            strutsPersonUsername: $("#i_username").val(),
            strutsPersonFirstname: $("#i_firstname").val(),
            strutsPersonLastname: $("#i_lastname").val(),
            strutsPersonLocation: $("#i_location").val(),
            strutsPersonInfo: $("#i_info").val(),
            strutsPersonLang: $("#i_lang").val(),
            strutsPersonIsAdmin: $("#i_admin").is(":checked"),
            strutsPersonCanEditMol: $("#i_canEditMol").is(":checked")
        },
        success: function(response){
            closeModal();
            window.location.href = "person";
        }
    });
}

function updatePerson(id){
    $.ajax({
        url: 'ajaxpersons',
        method: 'post',
        data: {
            strutsAction: 'updatePerson',
            strutsPersonId: id,
            strutsPersonUsername: $("#i_username").val(),
            strutsPersonFirstname: $("#i_firstname").val(),
            strutsPersonLastname: $("#i_lastname").val(),
            strutsPersonLocation: $("#i_location").val(),
            strutsPersonInfo: $("#i_info").val(),
            strutsPersonLang: $("#i_lang").val(),
            strutsPersonLastVisited: $("#i_lastVisited").val(),
            strutsPersonIsAdmin: $("#i_admin").is(":checked"),
            strutsPersonCanEditMol: $("#i_canEditMol").is(":checked")
        },
        success: function(response){
            closeModal();
            window.location.href = "person";
        }
    });
}

function deletePerson(id){
    $.ajax({
        url: 'ajaxpersons',
        method: 'post',
        data: {
            strutsAction: 'deletePerson',
            strutsPersonId: id
        },
        success: function(response){
            closeModal();
            window.location.href = "person";
        }
    });
}