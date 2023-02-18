// 字符串格式化
String.prototype.format = function () {
    const args = arguments;
    if (args.length <= 0) {
        return this;
    }
    return this.replace(/\{(\d+)\}/g, (m, i) => args[i]);
};

String.format = function () {
    if (arguments.length === 0)
        return null;
    if (arguments.length === 1) {
        return arguments[0];
    }
    let str = arguments[0];
    return str.format(arguments.slice(1));
};

// 判断是否为数字
String.prototype.isNumber = function () {
    if (this === "") {
        return false;
    }
    return !isNaN(this);
};

const utils = {
    compactQueue(queue) {
        var obj;

        while (queue.length) {
            var item = queue.pop();
            obj = item.obj[item.prop];

            if (Array.isArray(obj)) {
                var compacted = [];

                for (var j = 0; j < obj.length; ++j) {
                    if (typeof obj[j] !== 'undefined') {
                        compacted.push(obj[j]);
                    }
                }

                item.obj[item.prop] = compacted;
            }
        }

        return obj;
    },
    decode(str) {
        try {
            return decodeURIComponent(str.replace(/\+/g, ' '));
        } catch (e) {
            return str;
        }
    },
    assign(target, source) {
        return Object.keys(source).reduce(function (acc, key) {
            acc[key] = source[key];
            return acc;
        }, target);
    },
    arrayToObject(source, options) {
        var obj = options && options.plainObjects ? Object.create(null) : {};
        for (var i = 0; i < source.length; ++i) {
            if (typeof source[i] !== 'undefined') {
                obj[i] = source[i];
            }
        }

        return obj;
    },
    isBuffer(obj) {
        if (obj === null || typeof obj === 'undefined') {
            return false;
        }

        return !!(obj.constructor && obj.constructor.isBuffer && obj.constructor.isBuffer(obj));
    },
    isRegExp(obj) {
        return Object.prototype.toString.call(obj) === '[object RegExp]';
    },
    compact(value) {
        var queue = [{obj: {o: value}, prop: 'o'}];
        var refs = [];

        for (var i = 0; i < queue.length; ++i) {
            var item = queue[i];
            var obj = item.obj[item.prop];

            var keys = Object.keys(obj);
            for (var j = 0; j < keys.length; ++j) {
                var key = keys[j];
                var val = obj[key];
                if (typeof val === 'object' && val !== null && refs.indexOf(val) === -1) {
                    queue.push({obj: obj, prop: key});
                    refs.push(val);
                }
            }
        }
        return utils.compactQueue(queue);
    },
    encode(str) {
        // This code was originally written by Brian White (mscdex) for the io.js core querystring library.
        // It has been adapted here for stricter adherence to RFC 3986
        if (str.length === 0) {
            return str;
        }

        var string = typeof str === 'string' ? str : String(str);

        var out = '';
        for (var i = 0; i < string.length; ++i) {
            var c = string.charCodeAt(i);

            if (
                c === 0x2D // -
                || c === 0x2E // .
                || c === 0x5F // _
                || c === 0x7E // ~
                || (c >= 0x30 && c <= 0x39) // 0-9
                || (c >= 0x41 && c <= 0x5A) // a-z
                || (c >= 0x61 && c <= 0x7A) // A-Z
            ) {
                out += string.charAt(i);
                continue;
            }

            if (c < 0x80) {
                out = out + utils.hexTable[c];
                continue;
            }

            if (c < 0x800) {
                out = out + (utils.hexTable[0xC0 | (c >> 6)] + utils.hexTable[0x80 | (c & 0x3F)]);
                continue;
            }

            if (c < 0xD800 || c >= 0xE000) {
                out = out + (utils.hexTable[0xE0 | (c >> 12)] + utils.hexTable[0x80 | ((c >> 6) & 0x3F)] + utils.hexTable[0x80 | (c & 0x3F)]);
                continue;
            }

            i += 1;
            c = 0x10000 + (((c & 0x3FF) << 10) | (string.charCodeAt(i) & 0x3FF));
            out += utils.hexTable[0xF0 | (c >> 18)]
                + utils.hexTable[0x80 | ((c >> 12) & 0x3F)]
                + utils.hexTable[0x80 | ((c >> 6) & 0x3F)]
                + utils.hexTable[0x80 | (c & 0x3F)];
        }

        return out;
    },
    formatter(value) {
        return value;
    },
    generateArrayPrefix(prefix, key) { // eslint-disable-line func-name-matching
        return prefix + '[' + key + ']';
    },
    hexTable: (function () {
        var array = [];
        for (var i = 0; i < 256; ++i) {
            array.push('%' + ((i < 16 ? '0' : '') + i.toString(16)).toUpperCase());
        }

        return array;
    }()),
    merge(target, source, options) {
        if (!source) {
            return target;
        }

        if (typeof source !== 'object') {
            if (Array.isArray(target)) {
                target.push(source);
            } else if (typeof target === 'object') {
                if (options.plainObjects || options.allowPrototypes || !has.call(Object.prototype, source)) {
                    target[source] = true;
                }
            } else {
                return [target, source];
            }

            return target;
        }

        if (typeof target !== 'object') {
            return [target].concat(source);
        }

        var mergeTarget = target;
        if (Array.isArray(target) && !Array.isArray(source)) {
            mergeTarget = utils.arrayToObject(target, options);
        }

        if (Array.isArray(target) && Array.isArray(source)) {
            source.forEach(function (item, i) {
                if (has.call(target, i)) {
                    if (target[i] && typeof target[i] === 'object') {
                        target[i] = utils.merge(target[i], item, options);
                    } else {
                        target.push(item);
                    }
                } else {
                    target[i] = item;
                }
            });
            return target;
        }

        return Object.keys(source).reduce(function (acc, key) {
            var value = source[key];

            if (has.call(acc, key)) {
                acc[key] = utils.merge(acc[key], value, options);
            } else {
                acc[key] = value;
            }
            return acc;
        }, mergeTarget);
    }
}
Date.prototype.format = function (format) {
    var o = {
        "M+": this.getMonth() + 1, //month
        "d+": this.getDate(), //day
        "h+": this.getHours(), //hour
        "m+": this.getMinutes(), //minute
        "s+": this.getSeconds(), //second
        "q+": Math.floor((this.getMonth() + 3) / 3), //quarter
        "S": this.getMilliseconds() //millisecond
    };
    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
};


var has = Object.prototype.hasOwnProperty;

var defaults = {
    allowDots: false,
    allowPrototypes: false,
    arrayLimit: 20,
    decoder: utils.decode,
    delimiter: '&',
    depth: 5,
    parameterLimit: 1000,
    plainObjects: false,
    strictNullHandling: false
};

var parseValues = function(str, options) {
    var obj = {};
    var cleanStr = options.ignoreQueryPrefix ? str.replace(/^\?/, '') : str;
    var limit = options.parameterLimit === Infinity ? undefined : options.parameterLimit;
    var parts = cleanStr.split(options.delimiter, limit);

    for (var i = 0; i < parts.length; ++i) {
        var part = parts[i];

        var bracketEqualsPos = part.indexOf(']=');
        var pos = bracketEqualsPos === -1 ? part.indexOf('=') : bracketEqualsPos + 1;

        var key, val;
        if (pos === -1) {
            key = options.decoder(part, defaults.decoder);
            val = options.strictNullHandling ? null : '';
        } else {
            key = options.decoder(part.slice(0, pos), defaults.decoder);
            val = options.decoder(part.slice(pos + 1), defaults.decoder);
        }
        if (has.call(obj, key)) {
            obj[key] = [].concat(obj[key]).concat(val);
        } else {
            obj[key] = val;
        }
    }

    return obj;
};

var parseObject = function (chain, val, options) {
    var leaf = val;

    for (var i = chain.length - 1; i >= 0; --i) {
        var obj;
        var root = chain[i];

        if (root === '[]') {
            obj = [];
            obj = obj.concat(leaf);
        } else {
            obj = options.plainObjects ? Object.create(null) : {};
            var cleanRoot = root.charAt(0) === '[' && root.charAt(root.length - 1) === ']' ? root.slice(1, -1) : root;
            var index = parseInt(cleanRoot, 10);
            if (
                !isNaN(index)
                && root !== cleanRoot
                && String(index) === cleanRoot
                && index >= 0
                && (options.parseArrays && index <= options.arrayLimit)
            ) {
                obj = [];
                obj[index] = leaf;
            } else {
                obj[cleanRoot] = leaf;
            }
        }

        leaf = obj;
    }

    return leaf;
};

var parseKeys = function parseQueryStringKeys(givenKey, val, options) {
    if (!givenKey) {
        return;
    }

    // Transform dot notation to bracket notation
    var key = options.allowDots ? givenKey.replace(/\.([^.[]+)/g, '[$1]') : givenKey;

    // The regex chunks

    var brackets = /(\[[^[\]]*])/;
    var child = /(\[[^[\]]*])/g;

    // Get the parent

    var segment = brackets.exec(key);
    var parent = segment ? key.slice(0, segment.index) : key;

    // Stash the parent if it exists

    var keys = [];
    if (parent) {
        // If we aren't using plain objects, optionally prefix keys
        // that would overwrite object prototype properties
        if (!options.plainObjects && has.call(Object.prototype, parent)) {
            if (!options.allowPrototypes) {
                return;
            }
        }

        keys.push(parent);
    }

    // Loop through children appending to the array until we hit depth

    var i = 0;
    while ((segment = child.exec(key)) !== null && i < options.depth) {
        i += 1;
        if (!options.plainObjects && has.call(Object.prototype, segment[1].slice(1, -1))) {
            if (!options.allowPrototypes) {
                return;
            }
        }
        keys.push(segment[1]);
    }

    // If there's a remainder, just add whatever is left

    if (segment) {
        keys.push('[' + key.slice(segment.index) + ']');
    }

    return parseObject(keys, val, options);
};

const parse = function (str, opts) {
    var options = opts ? utils.assign({}, opts) : {};

    if (options.decoder !== null && options.decoder !== undefined && typeof options.decoder !== 'function') {
        throw new TypeError('Decoder has to be a function.');
    }

    options.ignoreQueryPrefix = options.ignoreQueryPrefix === true;
    options.delimiter = typeof options.delimiter === 'string' || utils.isRegExp(options.delimiter) ? options.delimiter : defaults.delimiter;
    options.depth = typeof options.depth === 'number' ? options.depth : defaults.depth;
    options.arrayLimit = typeof options.arrayLimit === 'number' ? options.arrayLimit : defaults.arrayLimit;
    options.parseArrays = options.parseArrays !== false;
    options.decoder = typeof options.decoder === 'function' ? options.decoder : defaults.decoder;
    options.allowDots = typeof options.allowDots === 'boolean' ? options.allowDots : defaults.allowDots;
    options.plainObjects = typeof options.plainObjects === 'boolean' ? options.plainObjects : defaults.plainObjects;
    options.allowPrototypes = typeof options.allowPrototypes === 'boolean' ? options.allowPrototypes : defaults.allowPrototypes;
    options.parameterLimit = typeof options.parameterLimit === 'number' ? options.parameterLimit : defaults.parameterLimit;
    options.strictNullHandling = typeof options.strictNullHandling === 'boolean' ? options.strictNullHandling : defaults.strictNullHandling;

    if (str === '' || str === null || typeof str === 'undefined') {
        return options.plainObjects ? Object.create(null) : {};
    }

    var tempObj = typeof str === 'string' ? parseValues(str, options) : str;
    var obj = options.plainObjects ? Object.create(null) : {};

    // Iterate over the keys and setup the new object

    var keys = Object.keys(tempObj);
    for (var i = 0; i < keys.length; ++i) {
        var key = keys[i];
        var newObj = parseKeys(key, tempObj[key], options);
        obj = utils.merge(obj, newObj, options);
    }

    return utils.compact(obj);
};
const stringify = function(object, options) {
    let option =  {
        prefix : "",
        generateArrayPrefix : utils.generateArrayPrefix,
        strictNullHandling: null,
        skipNulls: null,
        encoder : utils.encode,
        filter: null,
        sort: null,
        allowDots : true,
        serializeDate: null,
        formatter : utils.formatter,
        encodeValuesOnly: true
    }
    Object.assign(option, options);
    let {prefix, generateArrayPrefix, strictNullHandling, skipNulls, encoder, filter,
        sort, allowDots, serializeDate, formatter, encodeValuesOnly} = option;

    var obj = object;
    if (typeof filter === 'function') {
        obj = filter(prefix, obj);
    } else if (obj instanceof Date) {
        obj = serializeDate(obj);
    } else if (obj === null) {
        obj = '';
    }
    var values = [];

    if (!obj) {
        return values;
    }

    if (typeof obj === 'string' || typeof obj === 'number' || typeof obj === 'boolean' || utils.isBuffer(obj)) {
        if (encoder) {
            var keyValue = encodeValuesOnly ? prefix : encoder(prefix, utils.encoder);
            if(allowDots){
                keyValue = keyValue.substring(1);
            }else{
                const arr =keyValue.match(/\[\w+\]/g);
                keyValue = arr[0].substring(1,arr[0].length-1) + keyValue.substring(arr[0].length);
            }
            return [keyValue + '=' + formatter(encoder(obj, utils.encoder))];
        }
        return [formatter(prefix) + '=' + formatter(String(obj))];
    }


    var objKeys;
    if (Array.isArray(filter)) {
        objKeys = filter;
    } else {
        var keys = Object.keys(obj);
        objKeys = sort ? keys.sort(sort) : keys;
    }

    for (var i = 0; i < objKeys.length; ++i) {
        var key = objKeys[i];

        if (skipNulls && obj[key] === null) {
            continue;
        }

        if (Array.isArray(obj)) {
            values = values.concat(this.stringify(
                obj[key],
                {prefix:generateArrayPrefix(prefix, key),
                generateArrayPrefix,
                strictNullHandling,
                skipNulls,
                encoder,
                filter,
                sort,
                allowDots,
                serializeDate,
                formatter,
                encodeValuesOnly}
            ));
        } else {
            values = values.concat(this.stringify(
                obj[key],
                {prefix:prefix + (allowDots ? '.' + key : '[' + key + ']'),
                generateArrayPrefix,
                strictNullHandling,
                skipNulls,
                encoder,
                filter,
                sort,
                allowDots,
                serializeDate,
                formatter,
                encodeValuesOnly}
            ));
        }
    }

    return values.join("&");
}

/* 这里是axios 请求是设置的基础URL*/
// axios.defaults.baseURL = "http://api.sa.com/stary-animals";
axios.defaults.baseURL = "http://127.0.0.1/stary-animals";
axios.defaults.timeout = 5000;
axios.defaults.withCredentials = true;
/*设置axios拦截器,为每一个axios请求都加上token到header中，这里拦截的是request请求*/
axios.interceptors.request.use(
    config => {
        let token = localStorage.getItem("token");
        if (token) {  // 判断是否存在token，如果存在的话，则每个http header都加上token
            config.headers.token = `${token}`;
        }
        return config;
    },
    err => {
        return Promise.reject(err);
    });

/*统一处理401权限问题，跳转到登录额页面*/
axios.interceptors.response.use(
    response => {
        return response
    },
    error => {
        if (error.response) {
            switch (error.response.status) {
                case 401:
                    // 返回 401 清除token信息并跳转到登录页面
                    //alert("未登录");
                   window.location="/login.html";
                case 404:
                    // //
                    // alert("服务错误！");
                    // window.location="/login.html";
            }
        }
        return Promise.reject(error.response.data)   // 返回接口返回的错误信息
    });

// 配置对象
const ly = leyou = {
    /**
     * 对encodeURI()编码过的 URI 进行解码。并且获取其中的指定参数
     * @param name
     * @returns {*}
     */
    getUrlParam(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) {
            return decodeURI(r[2]);
        }
        return "";
    },
    /**
     * 发起ajax请求工具，底层依然是axios
     */
    // http: axios,
    http: axios,
    store: {
        set(key, value) {
            //localStorage.setItem(key, JSON.stringify(value));
            localStorage.setItem(key, value);
        },
        setJson(key, value) {
            localStorage.setItem(key, JSON.stringify(value));
        },
        get(key) {
            //return JSON.parse(localStorage.getItem(key));
            return localStorage.getItem(key);
        },
        getJson(key) {
            return JSON.parse(localStorage.getItem(key));
        },
        del(key) {
            return localStorage.removeItem(key);
        }
    },
    /**
     * 将整数价格变为小数
     * @param val
     * @returns {*}
     */
    formatPrice(val) {
        if(typeof val === 'string'){
            if(isNaN(val)){
                return null;
            }
            // 价格转为整数
            const index = val.lastIndexOf(".");
            let p = "";
            if(index < 0){
                // 无小数
                p = val + "00";
            }else if(index === p.length - 2){
                // 1位小数
                p = val.replace("\.","") + "0";
            }else{
                // 2位小数
                p = val.replace("\.","")
            }
            return parseInt(p);
        }else if(typeof val === 'number'){
            if(val == null){
                return null;
            }
            const s = val + '';
            if(s.length === 0){
                return "0.00";
            }
            if(s.length === 1){
                return "0.0" + val;
            }
            if(s.length === 2){
                return "0." + val;
            }
            const i = s.indexOf(".");
            if(i < 0){
                return s.substring(0, s.length - 2) + "." + s.substring(s.length-2)
            }
            const num = s.substring(0,i) + s.substring(i+1);
            if(i === 1){
                // 1位整数
                return "0.0" + num;
            }
            if(i === 2){
                return "0." + num;
            }
            if( i > 2){
                return num.substring(0,i-2) + "." + num.substring(i-2)
            }
        }
    },
    /**
     * 将日期格式化为指定格式
     * @param val
     * @param pattern
     * @returns {null}
     */
    formatDate(val, pattern) {
        if (!val) {
            return null;
        }
        if (!pattern) {
            pattern = "yyyy-MM-dd hh:mm:ss"
        }
        return new Date(val).format(pattern);
    },
    /**
     * 将js对象格式化为字符串参数对
     * @param object
     * @returns {*}
     */
    stringify,
    /**
     * 将请求参数字符串格式化为js对象
     */
    parse,
}
