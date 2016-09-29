$(document).ready(function(){
    $("button").click(function(){
        send()
    });
});

function send() {
    $.ajax({
        type : "POST",
        url : PATH + '/demo/send.html?t=0.31572216223',
        data : {
            text : $('#message').val(),
            count : $('#count').val()
        },
        dataType : "json",
        success : function(response, textStatus, xhr) {
            if (textStatus=="success") {
                fetch();
            }
        },
        error : function(xhr, textStatus, errorThrown) {}
    });
}

function fetch() {
    $.ajax({
        type : "GET",
        url : PATH + '/demo/fetch.html?t=0.31572216223',
        //data : {
        //    message : $('#message').val(),
        //    count : $('#count').val()
        //},
        dataType : "json",
        success : function(response, textStatus, xhr) {
            if (textStatus=="success") {
                console.log(response);
            }
        },
        error : function(xhr, textStatus, errorThrown) {}
    });
}