$("#nav-1 a").on("click", function() {
    var position = $(this)
        .parent()
        .position();
    var width = $(this)
        .parent()
        .width();
    $("#nav-1 .slide1").css({ opacity: 1, left: +position.left, width: width });
});

$("#nav-1 a").on("mouseover", function() {
    var position = $(this)
        .parent()
        .position();
    var width = $(this)
        .parent()
        .width();
    $("#nav-1 .slide2")
        .css({
            opacity: 1,
            left: +position.left,
            width: width
        })
        .addClass("squeeze");
});

$("#nav-1 a").on("mouseout", function() {
    $("#nav-1 .slide2")
        .css({ opacity: 0 })
        .removeClass("squeeze");
});

var currentWidth = $("#nav-1 .nav")
    .find(".active")
    .parent("li")
    .width();
var current = $(".nav .active").position();
$("#nav-1 .slide1").css({ left: +current.left, width: currentWidth });