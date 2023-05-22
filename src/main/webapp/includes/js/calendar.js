function createUpdateAjaxEvent(){
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
            year: $("#i_year").is(":checked"),
            notify: $("#i_notify").is(":checked"),
            id: $("#i_id").val()
        },
        success: function(response){
            //ajaxFillEvents();
        },
        error: function(xhr, status){
            console.log("["+status+"]");
        }
    });
    //$("#modal").hide();
    window.location.href = "calendar";
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

function ajaxDeleteEvent(id){
    $.ajax({
        url: 'calendar/deleteevent',
        method: 'get',
        data: {
            id: id
        },
        success: function(response){
        },
        error: function(xhr, status){
            console.log("["+status+"]");
        }
    });
    window.location.href = "calendar";
}