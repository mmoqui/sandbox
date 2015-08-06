function sendMessage() {
  var message = $('#message').val();
  console.log('send message: ' + message);
  $.ajax({
    type : 'POST',
    url : 'services/messages',
    data : JSON.stringify({text : message}),
    contentType : 'application/json',
    dataType : 'json'
  }).done(function(data) {
    $('#status').removeClass('alert-danger').addClass('alert-success').text('Your message has been sent');
  }).fail(function() {
    $('#status').removeClass('alert-success').addClass('alert-danger').text('The sending of your message has failed!');
  });
  return false;
}
