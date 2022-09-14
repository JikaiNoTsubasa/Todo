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