// polyfill for XPath
// will probably fail in ie10
// http://stackoverflow.com/questions/183369/cross-browser-xpath-implementation-in-javascript
(function(globalScope) {
    'use strict';
    var expr = function(xpathText, namespaces) {
        var prefix;
        this.xpathText = xpathText;
        this.namespaces = namespaces || null;
        if (document.createExpression) {
            this.xpathExpr = true;
        }
        else {
            this.namespaceString = "";
            if (namespaces !== null) {
                for (prefix in namespaces) {
                    if (this.namespaceString.length > 1)
                        this.namespaceString += ' ';
                    this.namespaceString += 'xmlns:' + prefix + '="' +
                            namespaces[prefix] + '"';
                }
            }
        }
    };
    expr.prototype.getNodes = function(xmlDomCtx) {
        var self = this, a, i,
                doc = xmlDomCtx.ownerDocument;
        if (doc === null)
            doc = xmlDomCtx;
        if (this.xpathExpr) {
            var result = doc.evaluate(this.xpathText,
                    xmlDomCtx,
                    function(prefix) {
                        return self.namespaces[prefix];
                    },
                    XPathResult.ORDERED_NODE_SNAPSHOT_TYPE,
                    null);
            a = [];
            for (i = 0; i < result.snapshotLength; i++) {
                a.push(result.snapshotItem(i));
            }
            return a;
        }
        else {
            try {
                doc.setProperty("SelectionLanguage", "XPath");
                doc.setProperty("SelectionNamespaces", this.namespaceString);
                if (xmlDomCtx === doc)
                    xmlDomCtx = doc.documentElement;
                return xmlDomCtx.selectNodes(this.xpathText);
            }
            catch (e2) {
                throw "XPath is not supported by this browser.";
            }
        }
    };
    expr.prototype.getNode = function(xmlDomCtx) {
        var self = this,
                doc = xmlDomCtx.ownerDocument;
        if (doc === null)
            doc = xmlDomCtx;
        if (this.xpathExpr) {
            var result = doc.evaluate(this.xpathText,
                    xmlDomCtx,
                    function(prefix) {
                        return self.namespaces[prefix];
                    },
                    XPathResult.FIRST_ORDERED_NODE_TYPE,
                    null);
            return result.singleNodeValue;
        }
        else {
            try {
                doc.setProperty("SelectionLanguage", "XPath");
                doc.setProperty("SelectionNamespaces", this.namespaceString);
                if (xmlDomCtx === doc)
                    xmlDomCtx = doc.documentElement;
                return xmlDomCtx.selectSingleNode(this.xpathText);
            }
            catch (e) {
                throw "XPath is not supported by this browser.";
            }
        }
    };
    var getNodes = function(context, xpathExpr, namespaces) {
        return(new globalScope.XPath.Expression(xpathExpr, namespaces)).getNodes(context);
    };
    var getNode = function(context, xpathExpr, namespaces) {
        return(new globalScope.XPath.Expression(xpathExpr, namespaces)).getNode(context);
    };
    globalScope.XPath = {
        Expression: expr,
        getNodes: getNodes,
        getNode: getNode
    };
}(this));


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
        // cross browser XPath
        exports.evaluateXPath = function(aNode, aExpr) {
            var result = XPath.getNodes(aNode, aExpr);
            var found = [];
            for (var i = 0, len = result.length; i < len; i++) {
                found.push(result[i]);
            }
            return found;
            /*
             if (typeof window.XPathEvaluator !== 'undefined') {
             // standards
             var xpe = new XPathEvaluator();
             var nsResolver = xpe.createNSResolver(aNode.ownerDocument === null ?
             aNode.documentElement : aNode.ownerDocument.documentElement);
             var result = xpe.evaluate(aExpr, aNode, nsResolver, 0, null);
             var found = [];
             var res = result.iterateNext();
             while (res !== null) {
             found.push(res);
             res = result.iterateNext();
             }
             return found;
             }
             if (typeof aNode.selectNodes !== 'undefined') {
             // msie
             // what about namespaces?
             var result = aNode.selectNodes(aExpr);
             var found = [];
             for (var i = 0, len = result.length; i < len; i++) {
             found.push(result[i]);
             }
             return found;
             }
             throw new Error('XPath unsupported');
             */
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
