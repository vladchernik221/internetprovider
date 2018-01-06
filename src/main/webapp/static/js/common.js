function redirect(name) {
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