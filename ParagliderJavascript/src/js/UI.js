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
  function GetURLParameter(sParam)
  {
      var sPageURL = window.location.search.substring(1);
      var sURLVariables = sPageURL.split('&');
      for (var i = 0; i < sURLVariables.length; i++) 
      {
          var sParameterName = sURLVariables[i].split('=');
          if (sParameterName[0] == sParam) 
          {
              return sParameterName[1];
          }
      }
  }