$(document).ready(function() {
    var pagesCount = $("#pagesCount").html();
    var currentPage = window.location.href.split("page=")[1];
    if(currentPage === undefined) {
        currentPage = 1;
    }
    $("#pagination-holder").pagination({
        pages: pagesCount,
        currentPage: currentPage,
        hrefTextPrefix: "?page=",
        cssStyle: "light-theme"
    });
});