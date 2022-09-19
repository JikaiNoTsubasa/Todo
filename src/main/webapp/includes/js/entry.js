function ajaxRefreshEntries(){
    $.ajax({
        url: 'ajaxentries',
        method: 'get',
        data: {
            strutsAction: 'refreshEntries'
        },
        success: function(response){
            $("#entries").html(response);
        }
    });
}

function ajaxNewEntry(name, project_id){
    $.ajax({
        url: 'ajaxentries',
        method: 'post',
        data: {
            strutsAction: 'newEntry',
            strutsEntryName: name,
            strutsProjectId: project_id
        },
        success: function(response){
            //console.log(response);
            ajaxRefreshEntries();
        },
        error: function(xhr, status){
            console.log("["+status+"]: "+xhr.responseText);
        }
    });
}

function ajaxDeleteEntry(id){
    $.ajax({
        url: 'ajaxentries',
        method: 'post',
        data: {
            strutsAction: 'deleteEntry',
            strutsEntryId: id
        },
        success: function(response){
            //ajaxRefreshEntries();
            callbackDelete();
        },
        error: function(xhr, status){
            console.log("["+status+"]: "+xhr.responseText);
        }
    });
    $("#modal").hide();
}

function ajaxChangeStatus(id,status_id){
    if (status_id<0){
        return;
    }
    $.ajax({
        url: 'ajaxentries',
        method: 'post',
        data: {
            strutsAction: 'changeStatus',
            strutsStatusId: status_id,
            strutsEntryId: id
        },
        success: function(response){
            callbackChangeStatus();
        },
        error: function(xhr, status){
            console.log("["+status+"]: "+xhr.responseText);
        }
    });
}