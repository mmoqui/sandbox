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
    addIntoMessageList(data);
  }).fail(function() {
    $('#status').removeClass('alert-success').addClass('alert-danger').text('The sending of your message has failed!');
  });
  return false;
}

function addIntoMessageList(message) {
  var items = $('#message-list li')
  if (items.length === 1 && $(items[0]).text().length === 0) {
    $(items[0]).attr('id', message.id).text(message.text);
  } else {
    var newItem = $(items[0]).clone();
    newItem.attr('id', message.id).text(message.text);
    $('#message-list').prepend(newItem);
  }
}

function changeLoggingLevel(module) {
  var level = $('#level-list option:selected').val();
  console.log('change logging level to: ' + level);
  $.ajax({
    type : 'PUT',
    url : 'services/logging/' + module,
    data : JSON.stringify({module : module, level: level}),
    contentType : 'application/json',
    dataType : 'json'
  }).done(function(data) {
    $('#status').removeClass('alert-danger').addClass('alert-success').text('The logging level has been successfully changed');
    $('#level-list option[value=' + data.level + ']').prop('selected', true);
  }).fail(function() {
    $('#status').removeClass('alert-success').addClass('alert-danger').text('The logging level change has failed!');
  });
  return false;
}

$(document).ready(function() {
  $.ajax({
    type : 'GET',
    url : 'services/messages',
    dataType : 'json'
  }).done(function(data) {
    for (var i = 0; i < data.length; i++) {
      addIntoMessageList(data[i]);
    }
  }).fail(function() {
    $('#status').removeClass('alert-success').addClass('alert-danger').text('Error while fetching all the messages!');
  });

  $.ajax({
    type : 'GET',
    url : 'services/logging/UserMessage',
    dataType : 'json'
  }).done(function(data) {
    $('#level-list option[value=' + data.level + ']').prop('selected', true);
  }).fail(function() {
    $('#status').removeClass('alert-success').addClass('alert-danger').text('Error while fetching all the messages!');
  });
});
