const utils = {
    /**
     * 对象转url参数
     * @param {*} data
     * @param {*} isPrefix
     */
    queryParams (data, isPrefix = false) {
        let prefix = isPrefix ? '?' : ''
        let _result = []
        for (let key in data) {
            let value = data[key]
            // 去掉为空的参数
            if (['', undefined, null].includes(value)) {
                continue
            }
            if (value.constructor === Array) {
                value.forEach(_value => {
                    _result.push(encodeURIComponent(key) + '[]=' + encodeURIComponent(_value))
                })
            } else {
                _result.push(encodeURIComponent(key) + '=' + encodeURIComponent(value))
            }
        }

        return _result.length ? prefix + _result.join('&amp;') : ''
    },


}

export default utils
