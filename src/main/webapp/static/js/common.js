function redirect(name, event) {
    if(event !== undefined) {
        event.stopPropagation();
    }
    window.location.href = name;
}

// Slider

$(document).ready(function() {
    $(".slider").each(function () {
        var slider = $(this);
        var slider_width = slider.width();
        var slide_count = $(slider).find("li").length;
        $(slider).find("ul").css({ width: slider_width * slide_count + 1 });

        if(slide_count > 1) {
            $(slider).append("<div class='nav'></div>");
            $(slider).find("li").each(function () {
                var slide = $(this);
                $(slider).find(".nav").append("<span rel='" + slide.index() + "'></span>");
                slide.addClass("slider" + slide.index());
            });
            $(slider).find("span").first().addClass("on");
        }
    });
});

function change_slide (slide_index, slider) {
    var speed = 500;
    var slider_list = $(slider).find("ul");
    var slide = $(slider).find("li.slider" + slide_index);
    var step = $(slide).width();
    $(slider_list).animate({ marginLeft: "-" + step * slide_index }, speed);
}

$(document).on("click", ".slider .nav span", function() {
    var slider = $(this).closest(".slider");
    $(slider).find("span").removeClass("on");
    $(this).addClass("on");
    var slide_index = $(this).attr("rel");
    change_slide(slide_index, slider);
});

// Modal

function show_modal() {
    var modal = $("#modal");
    var overlay = $("#overlay");
    modal.css({opacity: 1, visibility: "visible"});
    overlay.css({opacity: 1, visibility: "visible"});
}

function close_modal() {
    var modal = $("#modal");
    var overlay = $("#overlay");
    modal.css({opacity: 0, visibility: "hidden"});
    overlay.css({opacity: 0, visibility: "hidden"});
}

// AJAX

error_handler = function (xhr) {
    if(xhr.getResponseHeader("content-type") !== null && xhr.getResponseHeader("content-type") === "text/html;charset=UTF-8") {
        document.write(xhr.responseText);
    } else {
        $("#modal_message").html("Ошибка " + xhr.status + ": " + xhr.responseText);
        show_modal();
    }
};

function add_param_to_url(param_array) {
    var url_params = window.location.href.split("?");
    var params = url_params[1];
    param_array.forEach(function(item) {
        var param = item[0], value = item[1];
        var new_param = param + "=" + value;
        if (params !== undefined) {
            var regex = new RegExp(param + "=[^&]*");
            if (regex.test(params)) {
                params = params.replace(regex, new_param);
            } else {
                params += "&" + new_param;
            }
        } else {
            params = new_param;
        }
    });
    return url_params[0] + "?" + params;
}

function get_param_value_from_url(param) {
    var regex = new RegExp("(" + param + ")=([^&]*)");
    var result = window.location.href.match(regex);
    if(result != null) {
        return result[2];
    }
}

function delete_param_from_url(param) {
    var url_params = window.location.href.split("?");
    var params = url_params[1];
    var regex = new RegExp(param + "=[^&]*");
    if (regex.test(params)) {
        params = params.replace(regex, "");
    }
    if(params === "") {
        return url_params[0];
    }
    return url_params[0] + "?" + params;
}