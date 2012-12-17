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
  // renders the specified clusters of contents into the specified target HTML element.
  function renderClusters(clusters, target) {
    $.each(clusters, function (i, aCluster) {
       var div = $("<fieldset>").addClass("cluster").hide().
         append($("<legend>").html(aCluster.label)).appendTo(target);
       renderContents(aCluster.contents, div);
       div.show();
    });
  }

  // searches the contents that match the the user query.
  function search() {
    var query = $("#search input[name='query']").val()
    $.ajax("${request.contextPath}/contentSearch/search?query=" + query, {
      type:'GET',
      success:function (searchResult) {
        $("#results").children().remove();
        if (searchResult.contents.length != 0) {
          $("#results").addClass("nav");
          if (searchResult.clusters.length == 0) {
            renderContents(searchResult.contents, $("#results"));
          } else {
            renderClusters(searchResult.clusters, $("#results"));
          }
        } else {
          $("#results").removeClass("nav");
          $("<div>").addClass("message").html("No contents match").appendTo($("#results"));
        }
      },
      error:function (jqXHR, textStatus, errorThrown) {
        $("body").children().remove();
        $("body").html(jqXHR.responseText);
      }
    });
  }
</g:javascript>
</body>
</html>
