function ajaxRefreshEntries(){
    $.ajax({
        url: 'home/refresh',
        method: 'get',
        success: function(response){
            $("#entries").html(response);
        }
    });
}

function ajaxNewEntry(name, project_id){
    $.ajax({
        url: 'home/newentry',
        method: 'post',
        data: {
            entryName: name,
            projectId: project_id
        },
        success: function(response){
            ajaxRefreshEntries();
        },
        error: function(xhr, status){
            console.log("["+status+"]");
        }
    });
}

function ajaxDeleteEntry(id){
    $.ajax({
        url: 'home/deleteentry',
        method: 'post',
        data: {
            entryId: id
        },
        success: function(response){
            callbackDelete();
        },
        error: function(xhr, status){
            console.log("["+status+"]");
        }
    });
    $("#modal").hide();
}

function ajaxChangeStatus(id,status_id){
    if (status_id<0){
        return;
    }
    $.ajax({
        url: 'home/changestatus',
        method: 'post',
        data: {
            statusId: status_id,
            entryId: id
        },
        success: function(response){
            callbackChangeStatus();
        },
        error: function(xhr, status){
            console.log("["+status+"]");
        }
    });
}

function updateEntry(id){
    $.ajax({
        url: 'home/updateentry',
        method: 'post',
        data: {
            entryName: $("#i_name").val(),
            entryDesc: $("#i_desc").val(),
            priority: $("#i_prio").val(),
            projectId: $("#i_proj").val(),
            entryId: id
        },
        success: function(response){
            ajaxRefreshEntries();
        },
        error: function(xhr, status){
            console.log("["+status+"]");
        }
    });
    $("#modal").hide();
}