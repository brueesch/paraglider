function forward()
{
	var name = unescape(GetURLParameter('Name'));
	var latitude = parseFloat(GetURLParameter('Latitude'));
	var longitude = parseFloat(GetURLParameter('Longitude'));
	
	
	if(!isNaN(latitude) && !isNaN(longitude) && name !== 'undefined')
	{
		 window.location = "../index.html?Name=" + escape(name) + "&Latitude=" + escape(latitude) + "&Longitude=" + escape(longitude);
	}
	else
	{
	  	 window.location = "../index.html";
	}
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