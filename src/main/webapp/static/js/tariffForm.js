var selected_discount_ids = [];

$(document).ready(function() {
    if($("input[name=isLimit]:checked").val() === "false") {
        $(".only-for-limit").css({ display: "none" });
    }

    $(".selected-discounts .selected-item").each(function() {
        var value = $("#discount").find("option:contains(" + $(this).text() + ")").val();
        selected_discount_ids.push(value);
    });
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
    var data = form.serialize();
    if(!selected_discount_ids.empty) {
        data += "&discounts=" + selected_discount_ids.join(";");
    }
    $.ajax({
        type: form.attr("method"),
        url: form.attr("action"),
        data: data,
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

function select_discount(select_elem) {
    if($(select_elem).val() !== "" && selected_discount_ids.indexOf(select_elem.value) === -1) {
        var discount_container = $(".selected-discounts");
        var new_selected_discount = "<div class='selected-item'>" + $(select_elem).find(":selected").text() + "<div class='icon small fa-remove' onclick='delete_selected_discount(this)'></div></div>"
        discount_container.append(new_selected_discount);

        selected_discount_ids.push(select_elem.value);
        $(select_elem).val("");
    }
}

function delete_selected_discount(button) {
    var selected_discount = $(button).parent();
    
    var value = $("#discount").find("option:contains(" + selected_discount.text() + ")").val();
    selected_discount_ids.splice(selected_discount_ids.indexOf(value), 1);
    selected_discount.remove();
}