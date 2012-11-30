<!doctype html>
<html>
<head>
  <meta name="layout" content="main"/>
  <title>Thesaurus</title>
  <r:require module="jsTree"/>
</head>

<body>
<h1>Explore a thesaurus</h1>

<div class="nav">
  <div id="upload">
    <g:uploadForm controller="thesaurus" action="upload">
      <label for="fileToUpload">Upload a thesaurus from a comma-separated CSV file:</label>
      <input type="file" name="fileToUpload" id="fileToUpload"/>
      <input type="hidden" name="fileName"/>
      <label for="language">Language of the thesaurus:</label>
      <g:select name="language" from="${semantica.SupportedLanguage.languages()}" optionKey="code"
                optionValue="label"/>
      <input type="submit"/>
    </g:uploadForm>
    <div class="message">${flash.message}</div>

    <div class="error">${flash.error}</div>
  </div>

  <p><g:link class="create" controller="thesaurusTerm">Edit the thesaurus</g:link></p>
</div>

<div id="thesaurus">
</div>

<div id="documents">
  <p>No documents</p>
</div>

<g:javascript>
  function fetchThesaurusTermsById(termIds) {
    var terms = [];
    for (var i in termIds) {
      $.ajax({
            url:"/semantica/thesaurus/terms/" + termIds[i],
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
    if (parent.vocabulary) {
      terms = fetchThesaurusTermsById(parent.vocabulary)
    } else {
      terms = fetchThesaurusTermsById(parent.specificTerms)
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
    $("#upload input[name='fileName']").val(this.files[0].name);
  });

  $("#thesaurus").jstree({
    "plugins":["themes", "json_data", "ui"],
    "json_data":{
      "ajax":{
        "type":'GET',
        "url":function (node) {
          var nodeId = "";
          var url = ""
          if (node == -1) {
            url = "/semantica/thesaurus/vocabulary";
          }
          else {
            nodeId = node.attr('id');
            url = "/semantica/thesaurus/terms/" + nodeId;
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
            if ($("#thesaurus").children().length == 0) {
              $("<p>").text("No thesaurus").appendTo($("#thesaurus"));
            }
            return [];
          }
          return asJsTree(terms);
        }
      }
    }
  });
</g:javascript>
</body>
</html>
