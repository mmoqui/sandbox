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