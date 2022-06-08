//https://codebeautify.org/string-builder
var partial_source =  
'  <p>{{{data.panel0.heading}}}</p>  '  + 
'  <p>Some value</p>  ' ;

var displayText = '{{{displayData}}}'
Handlebars.registerPartial('displayText', displayText);

var panel_component = 
	 '   <div class="panel panel-default">  '  + 
	 '              <div class="panel-body">  '  + 
	 '   			<div class="panel-heading">  '  + 
	 '                  	<h3 class="panel-title-medium">{{{data}}}</h3>  '  + 
	 '                  </div>  '  + 
	 '                  <div class="panel-body">  '  + 
	 '                  	<div class="row">  '  + 
	 '                  		<div class="col-md-12">  '  + 
	 '                  			{{> template6 data}}  '  + 
	 '                  		</div>'  + 
	 '                  	</div>'  + 
	 '                  	  '  + 
	 '                  </div>  '  + 
	 '              </div>  '  + 
	 '     </div>  ' ; 
Handlebars.registerPartial('template6', partial_source);

Handlebars.registerHelper("x", function(expression, options) {
	var result;
  
	// you can change the context, or merge it with options.data, options.hash
	var context = this;
  
	// yup, i use 'with' here to expose the context's properties as block variables
	// you don't need to do {{x 'this.age + 2'}}
	// but you can also do {{x 'age + 2'}}
	// HOWEVER including an UNINITIALIZED var in a expression will return undefined as the result.
	with(context) {
	  result = (function() {
		try {
		  return eval(expression);
		} catch (e) {
		  console.warn('•Expression: {{x \'' + expression + '\'}}\n•JS-Error: ', e, '\n•Context: ', context);
		}
	  }).call(context); // to make eval's lexical this=context
	}
	return result;
  });
  
  Handlebars.registerHelper("xif", function(expression, options) {
	return Handlebars.helpers["x"].apply(this, [expression, options]) ? options.fn(this) : options.inverse(this);
  });
  
  Handlebars.registerHelper('stringify', function(context) {
      var html = context;
      // context variable is the HTML you will pass into the helper
      // Strip the script tags from the html, and return it as a Handlebars.SafeString
      return JSON.stringify(html);
    });

  var navPartial = 
  '   <div class="row">  '  + 
  '   		<div class="col-md-10 col-md-offset-1" style="padding-top: 30px">  '  + 
  '   			<!-- menu header -->  '  + 
  '   			<nav class="navbar-xs">  '  + 
  '   				<div class="container-fluid"  '  + 
  '   					style="padding-left: 0px; padding-right: 0px">  '  + 
  '   					<div class="navbar-header">  '  + 
  '   						<button type="button" class="navbar-toggle collapsed"  '  + 
  '   							data-toggle="collapse" data-target="#navbar">  '  + 
  '   							<span class="sr-only">Toggle navigation</span> <span  '  + 
  '   								class="icon-bar"></span> <span class="icon-bar"></span> <span  '  + 
  '   								class="icon-bar"></span>  '  + 
  '   						</button>  '  + 
  '   						<a class="navbar-brand"> <span  '  + 
  '   							style="color: #FDCC08 !important">OPTUS</span>  '  + 
  '   						</a>  '  + 
  '   					</div>  '  + 
  '     '  + 
  '   					<div id="navbar" class="navbar-collapse collapse">  '  + 
  '   						<ul class="nav navbar-nav navbar-left">  '  + 
  '   							{{#data.navigation.level0}}  '  + 
  '   								{{#xif \'active == true\'}}  '  + 
  '   									<li class="uxactive"><a class="active"  '  + 
  '   										href="{{url}}">{{name}}</a></li>  '  + 
  '   								{{else}}  '  + 
  '   									<li><a href="{{url}}">{{name}}</a></li>  '  + 
  '   								{{/xif}}  '  + 
  '   							{{/data.navigation.level0}}  '  + 
  '   						</ul>  '  + 
  '   					</div>  '  + 
  '     '  + 
  '   					<div id="navbar" class="navbar-collapse"  '  + 
  '   						style="background-color: white;">  '  + 
  '   						<ul class="nav navbar-nav navbar-nav-2nd-level" id="one">  '  + 
  '   							{{#data.navigation.level1}}  '  + 
  '   								{{#xif \'active == true\'}}  '  + 
  '   									<li id="navengdash"><a class="activelink"  '  + 
  '   										href="{{url}}" style="color: black;">{{name}}</a>  '  + 
  '   									</li>  '  + 
  '   								{{else}}  '  + 
  '   									<li id="navenglist"><a href="{{url}}"  '  + 
  '   										style="color: black;">{{name}}</a></li>  '  + 
  '   								{{/xif}}  '  + 
  '   							{{/data.navigation.level1}}  '  + 
  '   						</ul>  '  + 
  '   					</div>  '  + 
  '     '  + 
  '   				</div>  '  + 
  '   			</nav>  '  + 
  '   			  '  + 
  '   			<div class="row">  '  + 
  '   				<div class="col-md-12" style="height: 25px;"></div>  '  + 
  '   			</div>  '  + 
  '     '  + 
  '   			<div id="form"></div>  '  + 
  '   		</div>  '  + 
  '  	</div>  ' ; 
  
    
  var onlyText = document.createElement("div");
  onlyText.innerHTML = ' \
	  <script type="text/x-handlebars-template" id="onlyText"> \
	  	<span>{{{data}}}</span> \
	  </script>';
  document.getElementById('templateContainer').append(onlyText);
    
  var styleRadio = document.createElement("div");
  styleRadio.innerHTML = ' \
	  <script type="text/x-handlebars-template" id="styleRadio"> \
	  	<div id="custRadioBtn" class="btn-group {{{data.id}}}"> \
	  		<label class="btn btn-info notActive"  onclick="javascript:styleRadioOptioClick(this, {{stringify data}} )">\
	    		<input type="radio" name="radio-{{{data.values.[0]}}}" value="{{{data.values.[0]}}}" >{{{data.values.[0]}}} \
	  		</label> \
	  		<label class="btn btn-info notActive"  onclick="javascript:styleRadioOptioClick(this, {{stringify data}} )"> \
	  			<input type="radio" name="radio-{{{data.values.[1]}}}"  value="{{{data.values.[1]}}}">{{{data.values.[1]}}} \
	  		</label> \
	  		<label class="btn btn-info notActive"  onclick="javascript:styleRadioOptioClick(this, {{stringify data}} )"> \
	  			<input type="radio" name="radio-{{{data.values.[2]}}}"  value="{{{data.values.[2]}}}">{{{data.values.[2]}}} \
	  		</label> \
	  	</div>\
	  </script>';
  
  function styleRadioOptioClick(rControl, rContext) {
	  //console.log(rControl);
	  $('.'+rContext.id).find(".active").each(function (index, element) {
		  $(element).removeClass('active');
      });
	  $(rControl).addClass('active');
	  rContext.selectedValue = "Yes"
	  console.log(rContext);
  }
  
  
  document.getElementById('templateContainer').append(styleRadio);
  
  $.alpaca.Fields.Custom1Field = $.alpaca.Fields.AnyField.extend({
	    getFieldType: function() {
	        return "custom1";
	    },
	    setValue: function(val) {
	    	if (!val) {
	    		return;
	    	}
//	        var words = val.split(" ");
//	        for (var i = 0; i < words.length; i++) {
//	            var newWord = words[i].substring(0, 1).toUpperCase();
//	            if (words[i].length > 1) {
//	                newWord += words[i].substring(1).toLowerCase();
//	            }
//	            words[i] = newWord;
//	        }
//	        this.base(words.join(" "));
	    },
	    afterRenderContainer: function(model, callback)
        {
	    	console.log(model);
        },
	    onKeyPress: function(e) {
	        this.base(e);
	        var self = this;
	        Alpaca.later(0, this, function() {
	            var v = self.getValue();
	            self.setValue(v);
	        });
	    }
	});
Alpaca.registerFieldClass("custom1", Alpaca.Fields.Custom1Field);  
  
  
