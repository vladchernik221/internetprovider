$(document).ready(function() {
    var pagesCount = $("#pagesCount").html();
    var currentPage = get_param_value_from_url("page");
    if(currentPage === undefined) {
        currentPage = 1;
    }
    $("#pagination-holder").pagination({
        pages: pagesCount,
        currentPage: currentPage,
        hrefTextPrefix: "page",
        cssStyle: "light-theme"
    });
});