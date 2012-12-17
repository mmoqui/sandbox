if (typeof jQuery !== 'undefined') {
  (function ($) {
    $('#spinner').ajaxStart(function () {
      $(this).fadeIn();
    }).ajaxStop(function () {
          $(this).fadeOut();
        });
  })(jQuery);
}

$(document).ready(function () {
  $(".message").each(function () {
    if ($(this).html().length == 0) {
      $(this).remove();
    }
  });
});

// renders the specified contents into the specified target HTML element.
function renderContents(contents, target) {
  var list = $("<ul>").hide().appendTo(target);
  $.each(contents, function (i, aContent) {
    $("<li>", {'id':aContent.id}).append($("<a>", {href:"/semantica/contentSearch/content/" + aContent.id}).append(aContent.name)).
        appendTo(list);
  });
  list.show();
}