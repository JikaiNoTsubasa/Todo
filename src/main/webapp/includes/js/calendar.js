function createAjaxEvent(){
    $.ajax({
        url: 'calendar/createevent',
        method: 'post',
        data: {
            title: $("#i_title").val(),
            description: $("#i_description").val(),
            color: $("#i_color").val(),
            date: $("#i_date").val(),
            badge: $("#i_badge").val(),
            type: $("#i_type").val(),
            year: $("#i_year").val(),
            notify: $("#i_notify").val()
        },
        success: function(response){
            ajaxFillEvents();
        },
        error: function(xhr, status){
            console.log("["+status+"]");
        }
    });
    $("#modal").hide();
}

function ajaxSelectEvent(id){
    $.ajax({
        url: 'calendar/showevent',
        method: 'get',
        data: {
            id: id
        },
        success: function(response){
            $("#content-event").html(response);
        },
        error: function(xhr, status){
            console.log("["+status+"]");
        }
    });
}