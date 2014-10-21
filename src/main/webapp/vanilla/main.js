require(['json3', 'jquery', './vanilla', 'requirejs-domready'], function (JSON, jQuery, vanilla) {
    function log(message) {
        if (message !== null && typeof message === 'string') {
            if (message.charAt(0) === '+') {
                message = '<font color="#00cc00">' + message + '<\/font>';
            } else if (message.charAt(0) === '-') {
                message = '<font color="#cc0000">' + message + '<\/font>';
            }
        }
        document.getElementById('output').innerHTML += message + '\n';
    }

    function xmlRequestCallback(response) {
        // alternative
        //var who = response
        //        .getElementsByTagName('greeting')[0]
        //        .getElementsByTagName('who')[0]
        //        .firstChild
        //        .nodeValue;
        // with jQuery
        //var who = jQuery(response, 'greeting > who').text());
        var who = vanilla.getNode(
                '/greeting/who/text()', response);
        log('+ recieved ' + who);
    }
    function xmllistRequestCallback(response) {
        var names = vanilla.getNodes(
                '/greetings/greeting/who/text()', response);
        if (names.length === 0) {
            names = vanilla.getNodes(
                    '/collection/greeting/who/text()', response);
            if (names.length !== 0) {
                log('+ collection name is "collection" (is this RestEasy?)');
            }
        } else {
            log('+ collection name is "greetings"');
        }
        if (names.length === 0) {
            log('- what is this madness?');
        } else {
            log('+ recieved ' + names[0] + ' and ' + names[1]);
        }
    }
    function xmlcomplicatedRequestCallback(response) {
        log('recieved TODO');
        //aString should be xsi:nil
        //aDate should be iso?

        log('recieved complicated thing');
        var aStringNode = vanilla.getNode(
                '/complicatedGreeting/aString', response);
        var aStringNilAttribute = vanilla.getNode(
                '/complicatedGreeting/aString/@xsi:nil', response);
        if (aStringNode === undefined) {
            log('- null value isn\'t included in response');
        } else {
            log('+ null value is included in response');
            if (aStringNilAttribute === 'true') {
                log('+ null value is represented by xsi:nil');
            } else {
                log('- null value is not represented by xsi:nil');
            }
        }
        var aDate = vanilla.getNode(
                '/complicatedGreeting/aDate/text()', response);
        if (aDate === undefined) {
            log('- date value isn\'t included in response');
        } else {
            log('+ date value is included in response');
            if (typeof aDate === 'string') {
                var iso = /^-?\d{4}-\d\d-\d\dT\d\d:\d\d:\d\d(?:\.\d+)?(?:[-+]\d\d:\d\d|Z)?$/;
                var num = /^\d+$/;
                if (iso.test(aDate)) {
                    log('+ DateTime in ISO format');
                } else if (num.test(aDate)) {
                    if (Math.abs((+new Date()) - (+aDate)) < 5 * 1000) {
                        log('+ DateTime in epoch miliseconds number format');
                    } else if (Math.abs((+new Date()) / 1000 - (+aDate)) < 5) {
                        log('- DateTime in epoch seconds number format');
                    } else {
                        log('- DateTime in custom number format');
                        log('expected = ' + (+new Date()));
                        log('actual   = ' + aDate);
                    }
                } else {
                    log('- DateTime in custom string format');
                }
            } else {
                log('- what is this madness?');
            }
        }
        var aLong = vanilla.getNode(
                '/complicatedGreeting/aLong/text()', response);
        if (aLong === undefined) {
            log('- long value isn\'t included in response');
        } else {
            log('+ long value is included in response');
            if (typeof aLong === 'string') {
                if (aLong === '9223372036854775807') {
                    log('+ long is correct');
                } else {
                    log('- long = ' + aLong);
                }
            } else {
                log('- what is this madness?');
            }
        }
        var aNotherString = vanilla.getNode(
                '/complicatedGreeting/aNotherString/text()', response);
        if (aNotherString === undefined) {
            log('- aNotherString value isn\'t included in response');
        } else {
            log('+ aNotherString value is included in response');
            if (aNotherString === 'a:\u00E1\u00E2\u00E4\u00E0 c:\u00E7 e:\u00E9\u00EA\u00EB\u00E8 i:\u00ED\u00EE\u00EF\u00EC\u0131 I:\u0130 o:\u00F3\u00F4\u00F6\u00F2 u:\u00FA\u00FB\u00FC\u00F9 euro:\u20AC tree:\u2F4A') {
                log('+ aNotherString was correctly sent trough');
            } else {
                log('- what is this madness?');
                log('expected = a:\u00E1\u00E2\u00E4\u00E0 c:\u00E7 e:\u00E9\u00EA\u00EB\u00E8 i:\u00ED\u00EE\u00EF\u00EC\u0131 I:\u0130 o:\u00F3\u00F4\u00F6\u00F2 u:\u00FA\u00FB\u00FC\u00F9 euro:\u20AC tree:\u2F4A');
                log('actual   = ' + aNotherString);
            }
        }

    }
    function jsonRequestCallback(response) {
        // should be {"who":"JAX-RS"}
        // but we get {"greeting":{"who":"JAX-RS"}}
        // fixed with dropRootElement = true
        if (typeof response.greeting === 'object') {
            log('- extra root node detected (are you using CXF on TomEE?)');
            response = response.greeting;
        }
        log('+ recieved ' + response.who);
    }
    function jsonlistRequestCallback(response) {
        // should be [{"who":"JAX-RS zero"},{"who":"JAX-RS one"}]
        // but we get {"greeting":[{"who":"JAX-RS zero"},{"who":"JAX-RS one"}]}
        if (typeof response.greeting === 'object') {
            log('- extra root node detected (are you using CXF on TomEE?)');
            response = response.greeting;
        }
        log('+ recieved ' + response[0].who + ' and ' + response[1].who);
    }
    function jsoncomplicatedRequestCallback(response) {
        log('+ recieved complicated thing');
        if (!response.hasOwnProperty('aString')) {
            log('- null value isn\'t included in response');
            // CXF
            // this was forgotten on @XmlElement annotation
            // nillable = true
        } else {
            log('+ null value is included in response');
            if (response.aString === null) {
                log('+ null value is represented by null');
                // probably using JacksonJsonProvider
            } else {
                if (response.aString === '') {
                    log('- null value is represented by an empty string');
                    // CXF
                    // one of these was probably set
                    // ignoreNamespaces = true
                    // writeXsiType = false
                } else if (response.aString === 'null') {
                    log('- null value is represented by string "null"');
                } else if (typeof response.aString === 'string') {
                    log('- null value is represented by a non-empty string');
                } else if (typeof response.aString === 'object') {
                    log('- null value is represented by an object');
                    if (response.aString.hasOwnProperty('@xsi.nil')) {
                        log('- null is represented as {"@xsi.nil":"true"}');
                        // CXF
                        // none of these was probably set
                        // ignoreNamespaces = true
                        // writeXsiType = false
                    } else {
                        log('- what is this madness?');
                    }
                }
            }
        }
        if (!response.hasOwnProperty('aDate')) {
            log('- date value isn\'t included in response');
        } else {
            log('+ date value is included in response');
            if (typeof response.aDate === 'string') {
                var rx = /^-?\d{4}-\d\d-\d\dT\d\d:\d\d:\d\d(?:\.\d+)?(?:[-+]\d\d:\d\d|Z)?$/;
                if (rx.test(response.aDate)) {
                    // 2013-06-02T16:57:31.511+02:00
                    // default CXF
                    // or Jackson with WRITE_DATES_AS_TIMESTAMPS false
                    log('+ DateTime in ISO format');
                } else {
                    log('- DateTime in custom string format');
                }
            } else if (typeof response.aDate === 'number') {
                if (Math.abs((+new Date()) - response.aDate) < 5 * 1000) {
                    // default Jackson
                    log('+ DateTime in epoch miliseconds number format');
                } else if (Math.abs((+new Date()) / 1000 - (response.aDate)) < 5) {
                    log('- DateTime in epoch seconds number format');
                } else {
                    log('- DateTime in custom number format');
                    log('expected = ' + (+new Date()));
                    log('actual   = ' + response.aDate);
                }
            } else {
                log('- what is this madness?');
            }
        }
        if (!response.hasOwnProperty('aLong')) {
            log('- long value isn\'t included in response');
        } else {
            log('+ long value is included in response');
            if (typeof response.aLong === 'number') {
                if (response.aLong === 9007199254740992) {
                    log('- long is maximum acurate integer in javascript, which is incorrect');
                } else if (response.aLong === 9223372036854775807) {
                    log('+ long is correct (but not acurate because it can\'t be)');
                    log('expected = 9223372036854775807');
                    log('actual   = ' + 9223372036854775807);
                } else {
                    log('- long = ' + response.aLong);
                }
            } else {
                log('- what is this madness?');
            }
        }
        if (!response.hasOwnProperty('aNotherString')) {
            log('- aNotherString value isn\'t included in response');
        } else {
            log('+ aNotherString value is included in response');
            if (response.aNotherString === 'a:\u00E1\u00E2\u00E4\u00E0 c:\u00E7 e:\u00E9\u00EA\u00EB\u00E8 i:\u00ED\u00EE\u00EF\u00EC\u0131 I:\u0130 o:\u00F3\u00F4\u00F6\u00F2 u:\u00FA\u00FB\u00FC\u00F9 euro:\u20AC tree:\u2F4A') {
                log('+ aNotherString was correctly sent trough');
            } else {
                log('- what is this madness?');
                log('expected = a:\u00E1\u00E2\u00E4\u00E0 c:\u00E7 e:\u00E9\u00EA\u00EB\u00E8 i:\u00ED\u00EE\u00EF\u00EC\u0131 I:\u0130 o:\u00F3\u00F4\u00F6\u00F2 u:\u00FA\u00FB\u00FC\u00F9 euro:\u20AC tree:\u2F4A');
                log('actual   = ' + response.aNotherString);
            }
        }
    }
    function xmlButtonClick() {
        vanilla.request('GET', '../rest/helloworld', 'xml', null, xmlRequestCallback);
    }

    function xmllistButtonClick() {
        vanilla.request('GET', '../rest/helloworld/list', 'xml', null, xmllistRequestCallback);
    }
    function xmlcomplicatedButtonClick() {
        vanilla.request('GET', '../rest/helloworld/complicated', 'xml', null, xmlcomplicatedRequestCallback);
    }

    function jsonButtonClick() {
        vanilla.request('GET', '../rest/helloworld', 'json', null, jsonRequestCallback);
    }

    function jsonlistButtonClick() {
        vanilla.request('GET', '../rest/helloworld/list', 'json', null, jsonlistRequestCallback);
    }

    function jsoncomplicatedButtonClick() {
        vanilla.request('GET', '../rest/helloworld/complicated', 'json', null, jsoncomplicatedRequestCallback);
    }

    function jsonechoButtonClick() {
        vanilla.request('POST', '../rest/helloworld/echo', 'json', {
            "greetings": [
                {"who": "b"},
                {"who": "c"}],
            "aBigDecimal": "9999999999999999999999.1",
            "aBigInteger": "9999999999999999999999",
            "aBoolean": true,
            "aDate": vanilla.dateToISO(new Date()),
            "aDouble": 0.1,
            "aFloat": 0.1,
            "aGreeting": {"who": "a"},
            "aInteger": 1,
            //"aLong": 9007199254740992,
            //"aLong": 9223372036854775807,
            "aLong": "9223372036854775807",
            "aNotherString": "a:\u00E1\u00E2\u00E4\u00E0 c:\u00E7 e:\u00E9\u00EA\u00EB\u00E8 i:\u00ED\u00EE\u00EF\u00EC\u0131 I:\u0130 o:\u00F3\u00F4\u00F6\u00F2 u:\u00FA\u00FB\u00FC\u00F9 euro:\u20AC tree:\u2F4A",
            "aString": null //{"@xsi.nil":"true"}
        }, jsoncomplicatedRequestCallback);
    }

    // jQuery event handlers
    function xmljqueryButtonClick() {
        jQuery.ajax({
            type: 'GET',
            url: '../rest/helloworld',
            dataType: 'xml'
        }).done(xmlRequestCallback);
    }
    function xmllistjqueryButtonClick() {
        jQuery.ajax({
            type: 'GET',
            url: '../rest/helloworld/list',
            dataType: 'xml'
        }).done(xmllistRequestCallback);
    }

    function jsonjqueryButtonClick() {
        jQuery.ajax({
            type: 'GET',
            url: '../rest/helloworld',
            dataType: 'json'
        }).done(jsonRequestCallback);
    }
    function jsonlistjqueryButtonClick() {
        jQuery.ajax({
            type: 'GET',
            url: '../rest/helloworld/list',
            dataType: 'json'
        }).done(jsonlistRequestCallback);
    }
    function jsonechojqueryButtonClick() {
        jQuery.ajax({
            type: 'POST',
            url: '../rest/helloworld/echo',
            dataType: 'json',
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify({
                "greetings": [
                    {"who": "b"},
                    {"who": "c"}
                ],
                "aBigDecimal": "9999999999999999999999.1",
                "aBigInteger": "9999999999999999999999",
                "aBoolean": true,
                "aDate": vanilla.dateToISO(new Date()),
                "aDouble": 0.1,
                "aFloat": 0.1,
                "aGreeting": {"who": "a"},
                "aInteger": 1,
                //"aLong": 9007199254740992,
                //"aLong": 9223372036854775807,
                "aLong": "9223372036854775807",
                "aNotherString": "a:\u00E1\u00E2\u00E4\u00E0 c:\u00E7 e:\u00E9\u00EA\u00EB\u00E8 i:\u00ED\u00EE\u00EF\u00EC\u0131 I:\u0130 o:\u00F3\u00F4\u00F6\u00F2 u:\u00FA\u00FB\u00FC\u00F9 euro:\u20AC tree:\u2F4A",
                "aString": null //{"@xsi.nil":"true"}
            })
        }).done(jsoncomplicatedRequestCallback);
    }

    // initialise event handlers
    function main() {
        vanilla.on('xml', 'click', xmlButtonClick);
        vanilla.on('json', 'click', jsonButtonClick);
        vanilla.on('xmllist', 'click', xmllistButtonClick);
        vanilla.on('jsonlist', 'click', jsonlistButtonClick);
        vanilla.on('xmlcomplicated', 'click', xmlcomplicatedButtonClick);
        vanilla.on('jsoncomplicated', 'click', jsoncomplicatedButtonClick);
        vanilla.on('jsonecho', 'click', jsonechoButtonClick);

        jQuery('#xmljquery').on('click', xmljqueryButtonClick);
        jQuery('#jsonjquery').on('click', jsonjqueryButtonClick);
        jQuery('#xmllistjquery').on('click', xmllistjqueryButtonClick);
        jQuery('#jsonlistjquery').on('click', jsonlistjqueryButtonClick);
        jQuery('#jsonechojquery').on('click', jsonechojqueryButtonClick);
    }

    main();
});