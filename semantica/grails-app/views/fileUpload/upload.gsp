<!doctype html>
<html>
<head>
  <meta name="layout" content="main"/>
  <title>Upload a file into Semantica</title>
</head>

<body>
<h1>Upload a content</h1>

<div id="upload">
  <g:uploadForm controller="fileUpload" action="upload">
    <input type="file" name="fileToUpload"/>
    <input type="hidden" name="fileName"/>
    <input type="submit"/>
  </g:uploadForm>
  <div class="message">${flash.message}</div>
</div>
<g:javascript>
  $("#upload input[name='fileToUpload']").change(function () {
    $("#upload input[name='fileName']").val(this.files[0].name);
  });
</g:javascript>
</body>
</html>
