function createPerson(){
    $.ajax({
        url: 'person/createperson',
        method: 'post',
        data: {
            personUsername: $("#i_username").val(),
            personFirstname: $("#i_firstname").val(),
            personLastname: $("#i_lastname").val(),
            personLocation: $("#i_location").val(),
            personInfo: $("#i_info").val(),
            personLang: $("#i_lang").val(),
            personIsAdmin: $("#i_admin").is(":checked"),
            personCanEditMol: $("#i_canEditMol").is(":checked")
        },
        success: function(response){
            closeModal();
            window.location.href = "person";
        }
    });
}

function updatePerson(id){
    $.ajax({
        url: 'person/updateperson',
        method: 'post',
        data: {
            personId: id,
            personUsername: $("#i_username").val(),
            personFirstname: $("#i_firstname").val(),
            personLastname: $("#i_lastname").val(),
            personLocation: $("#i_location").val(),
            personInfo: $("#i_info").val(),
            personLang: $("#i_lang").val(),
            personLastVisited: $("#i_lastVisited").val(),
            personIsAdmin: $("#i_admin").is(":checked"),
            personCanEditMol: $("#i_canEditMol").is(":checked")
        },
        success: function(response){
            closeModal();
            window.location.href = "person";
        }
    });
}

function deletePerson(id){
    $.ajax({
        url: 'person/deleteperson',
        method: 'post',
        data: {
            personId: id
        },
        success: function(response){
            closeModal();
            window.location.href = "person";
        }
    });
}