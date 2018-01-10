$(document).ready(function() {
    if($("input[name=isLimit]:checked").val() === "false") {
        $(".only-for-limit").css({ display: "none" });
    }
});

function change_is_limit(set_limit) {
    if(set_limit) {
        $("input[name=includedTraffic]").val(0);
        $("input[name=priceOverTraffic]").val(0);
        $(".only-for-limit").css({ display: "block" });
    } else {
        $("input[name=includedTraffic]").val("");
        $("input[name=priceOverTraffic]").val("");
        $(".only-for-limit").css({ display: "none" });
    }
}

function send(event) {
    event.preventDefault();
    var form = $("#tariff_plan_form");
    $.ajax({
        type: form.attr("method"),
        url: form.attr("action"),
        data: form.serialize(),
        success: function (result) {
            if(result === "") {
                window.location.reload(true);
            } else {
                redirect("/tariff-plan/" + result + "/edit");
            }
        },
        error: error_handler
    });
}