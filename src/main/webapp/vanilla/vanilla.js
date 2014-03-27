// namespace for helper methods
if (typeof vanilla !== 'object') {
    vanilla = (function() {
        var exports = {};
        // cross browser XMLHttpRequest
        exports.createXMLHttpRequest = function() {
            if (window.XMLHttpRequest) {
                return new XMLHttpRequest();
            }
            if (window.ActiveXObject) {
                return new ActiveXObject('MSXML2.XMLHTTP.3.0');
            }
            throw new Error('XMLHttpRequest not supported');
        };
        // cross browser addEventListener
        exports.on = function(el, type, listener) {
            if (typeof el === 'string') {
                el = document.getElementById(el);
            }
            if (el.addEventListener) {
                el.addEventListener(type, listener, false);
            } else if (el.attachEvent) {
                el.attachEvent('on' + type, listener);
            } else {
                throw new Error('could listen to event ' + type + ': addEventListener and attachEvent  are not supported');
            }
        };
        // cross browser XPath (very very limited)
        exports.evaluateXPath = function(aNode, aExpr) {
            var str = aExpr;
            var regex = /\/(text\(\)|\w+)/g;
            var match = regex.exec(str);
            var nodes = [aNode];
            while (match !== null && nodes.length !== 0) {
                var nodeName = match[1];
                var childNodes = [];
                for (var i = 0; i<nodes.length; i++){
                    var childNode = nodes[i].firstChild;
                    while (childNode !== null) {
                        if ((nodeName === 'text()' &&
                                childNode.nodeType === 3 /* Text */) ||
                                childNode.nodeName === nodeName) {
                            childNodes.push(childNode);
                        }
                        childNode = childNode.nextSibling;
                    }
                }
                nodes = childNodes;
                match = regex.exec(str);
            }
            return nodes;
        };
        // cross browser ajax
        exports.request = function(method, url, dataType, message, successCallback) {
            var xmlhttp = vanilla.createXMLHttpRequest();
            xmlhttp.onreadystatechange = function() {
                if (this.readyState === 4) {
                    if (this.status >= 200 && this.status < 300) {
                        switch (dataType) {
                            case 'xml':
                                successCallback.call(this, this.responseXML);
                                break;
                            case 'json':
                                successCallback.call(this, JSON.parse(this.responseText));
                                break;
                            default:
                                throw new Error('unsupported dataType ' + dataType);
                        }
                    } else {
                        throw new Error('unsupported status ' + this.status);
                    }
                }
            };
            xmlhttp.open(method, url, true);
            xmlhttp.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
            switch (dataType) {
                case 'xml':
                    if (message !== null && message !== undefined) {
                        xmlhttp.setRequestHeader('Content-Type', 'application/xml; charset=UTF-8');
                    }
                    xmlhttp.setRequestHeader('Accept', 'application/xml, text/xml');
                    break;
                case 'json':
                    if (message !== null && message !== undefined) {
                        xmlhttp.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
                    }
                    xmlhttp.setRequestHeader('Accept', 'application/json');
                    break;
                default:
                    throw new Error('unsupported dataType ' + dataType);
            }
            if (dataType === 'json' && typeof message === 'object') {
                message = JSON.stringify(message);
            }
            xmlhttp.send(message);
        };
        // the right and precise way to format dates
        exports.dateToISO = function(date) {
            function f(n) {
                // Format integers to have at least two digits.
                return n < 10 ? '0' + n : n;
            }
            function f3(n) {
                // Format integers to have at least three digits.
                return n < 10 ? '00' + n : (n < 100 ? '0' + n : n);
            }
            var ms = date.getUTCMilliseconds();
            return date.getUTCFullYear() + '-' +
                    f(date.getUTCMonth() + 1) + '-' +
                    f(date.getUTCDate()) + 'T' +
                    f(date.getUTCHours()) + ':' +
                    f(date.getUTCMinutes()) + ':' +
                    f(date.getUTCSeconds()) +
                    (ms === 0 ? '' : '.' + f3(ms)) + 'Z';
        };
        return exports;
    }());
}
