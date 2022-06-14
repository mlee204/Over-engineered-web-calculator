function isInteger(value) {
    return /^-?\d+$/.test(value);
}

module.exports = {
    subtract: function(x,y) {
        if(x == null || y == null) {
            throw new SyntaxError("missing formal parameter")
        }

        if(!isInteger(x) || !isInteger(y)) {
            throw new TypeError("Please enter valid Integers")
        }

        return x-y;
    }
}
