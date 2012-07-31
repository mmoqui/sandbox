<!doctype html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Upload a file into Semantica</title>
  </head>
  <body>
    <div id="upload">
      <g:uploadForm controller="fileUpload" action="upload">
        <input type="file" name="fileToUpload"/>
        <input type="hidden" name="fileName"/>
        <input type="submit" />
      </g:uploadForm>
      <span>${flash.message}</span>
    </div>
    <script type="text/javascript">
    $("#upload input[name='fileToUpload']").change(function() {
      $("#upload input[name='fileName']").val(this.files[0].name);
    })
  </script>
  </body>
</html>
