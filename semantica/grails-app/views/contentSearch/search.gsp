<!doctype html>
<html>
<head>
  <meta name="layout" content="main"/>
  <title>Welcome to Semantica</title>
</head>

<body>
<h1>Search some contents</h1>

<div id="search">
  <form action="javascript: search();">
    <input type="text" name="query"/>
    <input type="submit"/>
  </form>
</div>
<br/>

<div id="results">
</div>
<g:javascript>
  function search() {
    var query = $("#search input[name='query']").val()
    $.ajax("${request.contextPath}/contentSearch/search?query=" + query, {
      type:'GET',
      success:function (contents) {
        $("#results").children().remove();
        if (contents.length != 0) {
          var results = $("<ul>").hide().appendTo("#results");
          $.each(contents, function (i, aContent) {
                $("<li>", {'id':aContent.id}).append($("<a>", {href:"${request.contextPath}/contentSearch/get?id=" + aContent.id}).append(aContent.name)
                ).
                    appendTo(results);
              }
          )
          ;
          results.show();
        } else {
          $("<div>").addClass("message").html("No contents match").appendTo($("#results"));
        }
      },
      error:function (jqXHR, textStatus, errorThrown) {
        alert(errorThrown);
        $("body").children().remove();
        $("body").html(jqXHR.responseText);
      }
    });
  }
</g:javascript>
</body>
</html>
