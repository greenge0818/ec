$(document).ready(function(){
    load();

    $("#account").bind('keydown',function(e){
        if(e.keyCode==13){
            adduser();
        }
    });

    $("button").click(function(){
        adduser()
    });
});

function adduser() {
    $.ajax({
        type : "POST",
        url : PATH + '/demo/add.html?t=0.31572216271',
        data : {
            account : $('#account').val(),
            password : "abc123456",
            email:"121@ss.com"
        },
        dataType : "json",
        success : function(response, textStatus, xhr) {
            if (textStatus=="success") {
                load();
            } else {
                var msg = response.data;
                var html = "";
                for(var i=0; i<msg.length; i++) {
                    html += msg[i].defaultMessage;
                }
                $("body").append("<p>"+html+"</p>")
            }
        },
        error : function(xhr, textStatus, errorThrown) {}
    });
}

function deluser(id) {
    $.ajax({
        type : "POST",
        url : PATH + '/demo/del.html?t=0.31572216271',
        data : {
            id : id
        },
        dataType : "json",
        success : function(response, textStatus, xhr) {
            if (textStatus=="success") {
                load();
            } else {

            }
        },
        error : function(xhr, textStatus, errorThrown) {}
    });
}

function edituser(id) {
    $.ajax({
        type : "POST",
        url : PATH + '/demo/edit.html?t=0.31572216271',
        data : {
            id : id,
            account:random()
        },
        dataType : "json",
        success : function(response, textStatus, xhr) {
            if (textStatus=="success") {
                load();
            } else {

            }
        },
        error : function(xhr, textStatus, errorThrown) {}
    });
}

function random(len) {
    len = len || 10;
    var $chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';
    var maxPos = $chars.length;
    var pwd = '';
    for (i = 0; i < len; i++) {
        pwd += $chars.charAt(Math.floor(Math.random() * maxPos));
    }
    return pwd;
}

function load() {
    $.ajax({
        type : "POST",
        url : PATH + '/demo/load.html',
        data : {
            //account:"3re7tnMxYi",
            pageNum:1,
            pageSize:10,
            orderBys:"id"
        },
        dataType : "json",
        success : function(response, textStatus, xhr) {
            if (textStatus=="success") {
                var datas = response.data;
                $('#list').empty();
                for ( var i in datas) {
                    $('#list').append("<li>UserId: "+datas[i].id+", Account: "+datas[i].account+" <a href='javascript:deluser("+datas[i].id+")'>del</a>"+" <a href='javascript:edituser("+datas[i].id+")'>edit</a>"+"</li>");
                }
            }
        },
        error : function(xhr, textStatus, errorThrown) {}
    });
}