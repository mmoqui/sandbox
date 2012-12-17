<!doctype html>
<html>
<head>
  <meta name="layout" content="main"/>
  <title>Plan of Classification</title>
  <r:require module="jsTree"/>
</head>

<body>
<h1>Explore the plan of classification</h1>

<div class="nav">
  <div id="upload">
    <g:uploadForm controller="planOfClassification" action="upload">
      <label for="fileToUpload">Upload a taxonomy into the plan from a comma-separated CSV file:</label>
      <input type="file" name="fileToUpload" id="fileToUpload"/>
      <input type="hidden" name="fileName"/>
      <label for="language">Language of the taxonomy:</label>
      <g:select name="language" from="${semantica.SupportedLanguage.languages()}" optionKey="code"
                optionValue="label"/>
      <input type="submit"/>
    </g:uploadForm>
    <div class="message">${flash.message}</div>

    <div class="error">${flash.error}</div>
  </div>

  <p><g:link class="create" controller="taxonomyTerm">Edit the plan of classification</g:link></p>
</div>

<div id="classification" class="empty">
</div>

<div id="documents" class="nav">
  <p>No documents</p>
</div>

<g:javascript>
  function fetchTaxonomyTermsById(termIds) {
    var terms = [];
    for (var i in termIds) {
      $.ajax({
            url:"/semantica/planOfClassification/terms/" + termIds[i],
            dataType:'json',
            async:false,
            success:function (aTerm) {
              terms.push(aTerm);
            },
            error:function (jqXHR, textStatus, errorThrown) {
              alert(errorThrown);
            }
          }
      );
    }
    return terms;
  }

  function fetchChildTerms(parent) {
    var terms;
    if (parent.taxonomies) {
      terms = fetchTaxonomyTermsById(parent.taxonomies)
    } else {
      terms = fetchTaxonomyTermsById(parent.specificTerms)
    }
    return terms;
  }

  function asJsTreeNode(aTerm) {
    return {
      data:aTerm.label,
      attr:{ id:aTerm.id },
      state:"closed"
    };
  }

  function asJsTree(terms) {
    var tree = [];
    for (var i in terms) {
      tree[i] = asJsTreeNode(terms[i]);
    }
    return tree;
  }

  $("#upload input[name='fileToUpload']").change(function () {
    this.val(this.files[0].name);
  });

  $("#classification").jstree({
    "plugins":["themes", "json_data", "ui"],
    "json_data":{
      "ajax":{
        "type":'GET',
        "url":function (node) {
          var nodeId = "";
          var url = ""
          if (node == -1) {
            url = "/semantica/planOfClassification/taxonomies";
          }
          else {
            nodeId = node.attr('id');
            url = "/semantica/planOfClassification/terms/" + nodeId;
          }

          return url;
        },
        "success":function (data) {
          var terms;
          if (data instanceof Array) {
            terms = [];
            for (var i in data) {
              if (data[i].moreGeneral)
                terms.push(data[i]);
            }
          } else {
            terms = fetchChildTerms(data);
          }
          if (terms.length == 0) {
            if ($("#classification").hasClass("empty"))
              $("#classification").append($("<p>").html("No taxonomies"));
            return [];
          } else if ($("#classification").hasClass("empty")) {
            $("#classification").removeClass("empty");
          }
          return asJsTree(terms);
        }
      }
    }
  }).bind("select_node.jstree", function (event, data) {
        var termId = $(data.rslt.obj).attr("id");
        $.ajax("/semantica/contentSearch/category/" + termId, {
          type:"GET",
          dataType:"json",
          success:function (contents) {
            $("#documents").children().remove();
            if (contents.length > 0) {
              renderContents(contents, ${"documents"});
            } else {
              $("<p>").text("No documents").appendTo($("#documents"));
            }
          },
          error:function (jqXHR, textStatus, errorThrown) {
            alert(errorThrown);
          }
        });
      });
</g:javascript>
</body>
</html>
