function change_archived(tariff_plan_id) {
    $.ajax({
        type: "POST",
        url: "/tariff_plan/" + tariff_plan_id + "/archived",
        success: function (result, status, xhr) {
            if(xhr.getResponseHeader("content-type").toLowerCase() === "text/html;charset=utf-8") {
                document.write(result);
            } else {
                location.reload(); //TODO наверное не очень хорошая идея (увеличивает нагрузку на сервер)
            }
        },
        error: function (xhr) {
            $("#modal_message").html("Ошибка " + xhr.status + ": " + xhr.responseText);
            show_modal();
        }
    });
}