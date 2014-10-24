(function (root, factory) {
    if (typeof define === 'function' && define.amd) {
        define(['json3'], factory);
    } else {
        root.vanilla = factory(root.JSON);
    }
}(this, function (JSON) {
    var exports = {};
    // cross browser XMLHttpRequest
    var createXMLHttpRequest = function () {
        if (window.XMLHttpRequest) {
            return new XMLHttpRequest();
        }
        if (window.ActiveXObject) {
            return new ActiveXObject('Microsoft.XMLHTTP');
        }
        throw new Error('XMLHttpRequest not supported');
    };
    exports.createXMLHttpRequest = createXMLHttpRequest;
    // cross browser addEventListener
    exports.on = function (el, type, listener) {
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
    // cross browser XPath (very limited, but cross browser)
    exports.getNodes = function (xpathExpression, contextNode) {
        var TEXT_NODE, regex, match, nodes,
                isTextNode, isAttribute, nodeName, childNodes,
                i, node, childNode;
        if (!/^(?:\/\w+)+(?:\/text\(\)|\/@(?:xsi:nil|\w+))?$/.test(xpathExpression)) {
            throw new Error('Illegal argument "' + xpathExpression + '"');
        }
        TEXT_NODE = 3;
        regex = /\/(text\(\)|\w+|@(xsi:nil|\w+))/g;
        match = regex.exec(xpathExpression);
        nodes = [contextNode];
        while (match !== null && nodes.length !== 0) {
            isAttribute = (match[2] !== undefined);
            nodeName = isAttribute ? match[2] : match[1];
            isTextNode = (nodeName === 'text()');
            childNodes = [];
            for (i = 0; i < nodes.length; i++) {
                node = nodes[i];
                childNode;
                if (isAttribute) {
                    childNode = node.getAttribute(nodeName);
                    if (childNode !== null) {
                        childNodes.push(childNode);
                    }
                } else if (isTextNode) {
                    childNode = node.firstChild;
                    while (childNode !== null) {
                        if (childNode.nodeType === TEXT_NODE) {
                            childNodes.push(childNode.nodeValue);
                        }
                        childNode = childNode.nextSibling;
                    }
                } else {
                    childNode = node.firstChild;
                    while (childNode !== null) {
                        if (childNode.nodeName === nodeName) {
                            childNodes.push(childNode);
                        }
                        childNode = childNode.nextSibling;
                    }
                }
            }
            nodes = childNodes;
            match = regex.exec(xpathExpression);
        }
        return nodes;
    };
    exports.getNode = function (xpathExpression, contextNode) {
        var nodes = exports.getNodes(xpathExpression, contextNode);
        if (nodes.length !== 0) {
            return nodes[0];
        }
        return undefined;
    };
    // cross browser ajax
    exports.request = function (method, url, dataType, message, successCallback) {
        var xmlhttp = createXMLHttpRequest();
        xmlhttp.onreadystatechange = function () {
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
    exports.dateToISO = function (date) {
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
}));
