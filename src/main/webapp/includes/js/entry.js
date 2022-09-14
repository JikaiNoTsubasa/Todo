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