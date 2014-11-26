$(function() {
    $( "#slider-left" ).slider({
      orientation: "vertical",
      range: "max",
      min: 0,
      max: 34,
      value: 34,
      step: 1,
      slide: function( event, ui ) {
        $( "#amount" ).val( ui.value );
        var max = $( "#slider-left" ).slider( "option", "max" );
  	  	var value = $( "#slider-left" ).slider( "value" );
  	  	leftBreak((max-value));
      }
    });
    $( "#amount" ).val( $( "#slider-left" ).slider( "value" ) );
  });
  $(function() {
	    $( "#slider-right" ).slider({
	      orientation: "vertical",
	      range: "max",
	      min: 0,
	      max: 34,
	      value: 34,
	      step: 1,
	      slide: function( event, ui ) {
	        $( "#amount" ).val( ui.value );
	        var max1 = $( "#slider-right" ).slider( "option", "max" );
	  	  	var value1 = $( "#slider-right" ).slider( "value" );
	  	  	rightBreak((max1-value1));
	      }
	    });
	    $( "#amount" ).val( $( "#slider-right" ).slider( "value" ) );
	  });
  $(function() {
	    $( "#slider-both" ).slider({
	      orientation: "vertical",
	      range: "max",
	      min: 0,
	      max: 34,
	      value: 34,
	      step: 1,
	      slide: function( event, ui ) {
	        $( "#amount" ).val( ui.value );
	        var max2 = $( "#slider-both" ).slider( "option", "max" );
	  	  	var value2 = $( "#slider-both" ).slider( "value" );
	  	  	bothBreaks((max2-value2));
	      }
	    });
	    $( "#amount" ).val( $( "#slider-both" ).slider( "value" ) );
	  });
  function setCompass(degree)
  {
	  $('#compasscanvas').css({  
          '-webkit-transform': 'rotate(' + degree + 'deg)',  //Safari 3.1+, Chrome  
          '-moz-transform': 'rotate(' + degree + 'deg)',     //Firefox 3.5-15  
          '-ms-transform': 'rotate(' + degree + 'deg)',      //IE9+  
          '-o-transform': 'rotate(' + degree + 'deg)',       //Opera 10.5-12.00  
          'transform': 'rotate(' + degree + 'deg)',
      })  
  }
  