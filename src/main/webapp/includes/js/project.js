function createProject(){
    $.ajax({
        url: 'ajaxprojects',
        method: 'post',
        data: {
            strutsAction: 'createProject',
            strutsProjectName: $("#i_name").val(),
            strutsProjectPriority: $("#i_prio").val()
        },
        success: function(response){
            closeModal();
            window.location.href = "admin";
        }
    });
}