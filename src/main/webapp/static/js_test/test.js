$(document).ready(function () {
    var form = $('#form1');
    form.submit(function (event) {
        $.ajax({
            type: form.attr('method'),
            url: form.attr('action'),
            data: form.serialize(),
            success: function (msg) {
                alert(msg);
                window.location.href = msg;
            },
            error : function (xhr, ajaxOptions, thrownError) {
                alert(xhr.status);
                alert(xhr.responseText);
            }
        });
        return false;
    });
});
