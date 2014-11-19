/**
 * Helper function for element.addEventListener/attachEvent
 */
function addDomListener(element, eventName, listener) {
  if (element.addEventListener)
    element.addEventListener(eventName, listener, false);
  else if (element.attachEvent)
    element.attachEvent('on' + eventName, listener);
}

/**
 * Helper function to get the rectangle for the given HTML element.
 */
function getElementRect(element) {
  var left = element.offsetLeft;
  var top = element.offsetTop;
  
  var p = element.offsetParent;
  while (p && p != document.body.parentNode) {
    if (isFinite(p.offsetLeft) && isFinite(p.offsetTop)) {
      left += p.offsetLeft;
      top += p.offsetTop;
    }
    
    p = p.offsetParent;
  }
  
  return { left: left, top: top,
           width: element.offsetWidth, height: element.offsetHeight };
}


/**
 * Create a custom button using the IFRAME shim and CSS sprite technique
 * at the given x, y offset from the top left of the plugin container.
 */
function createNativeHTMLButton(x, y, width, height, imageName, event) {
  // create the button
  var button = document.createElement('a');
  button.href = '#';
  button.className = 'button';
  button.style.backgroundImage = 'url(img/' + imageName + '.png)';
  
  // create an IFRAME shim for the button
  var iframeShim = document.createElement('iframe');
  iframeShim.frameBorder = 0;
  iframeShim.scrolling = 'no';
  iframeShim.src = (navigator.userAgent.indexOf('MSIE 6') >= 0) ? '' : 'javascript:void(0);';

  // position the button and IFRAME shim
  var pluginRect = getElementRect(document.getElementById('map3d'));
  button.style.position = iframeShim.style.position = 'absolute';
  button.style.left = iframeShim.style.left = (pluginRect.left + x) + 'px';
  button.style.top = iframeShim.style.top = (pluginRect.top + y) + 'px';
  button.style.width = iframeShim.style.width = width + 'px';
  button.style.height = iframeShim.style.height = height + 'px';
  
  // set up z-orders
  button.style.zIndex = 10;
  iframeShim.style.zIndex = button.style.zIndex - 1;
  
  // set up click handler
  addDomListener(button, 'click', event);
  
  // add the iframe shim and button
  document.body.appendChild(button);
  document.body.appendChild(iframeShim);
}