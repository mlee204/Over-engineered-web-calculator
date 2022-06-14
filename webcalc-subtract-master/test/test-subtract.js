var expect = require('chai').expect;
var assert = require('chai').assert;
var sub = require('../subtract');
describe('#subtract', function () {
        context('with valid parameter', function () {

                it('should return 5', function () {
                        let x = 10;
                        let y = 5;
                        expect(sub.subtract(x, y)).to.equal(5);
                })

                it('should return 25', function () {
                        let x = -20;
                        let y = 5;
                        expect(sub.subtract(x, y)).to.equal(-25);
                })

                it('should return -50', function () {
                        let x = 5;
                        let y = 55;
                        expect(sub.subtract(x, y)).to.equal(-50);
                })

        })

        context('with invalid parameters Types', function () {

                it('should throw Type Error - x is not an Integer', function () {
                        let x = "bad";
                        let y = 5;
                        assert.throws( () => sub.subtract(x, y), TypeError, "Please enter valid Integers")
                })

                it('should throw Type Error - x and y are doubles', function () {
                        let x = 5.5
                        let y = 6.6;
                        assert.throws( () => sub.subtract(x, y), TypeError, "Please enter valid Integers")
                })

                it('should throw Type Error - x is an array', function () {
                        let x = [4,3,2]
                        let y = 5;
                        assert.throws( () => sub.subtract(x, y), TypeError, "Please enter valid Integers")
                })

                it('should throw Type Error - x is a object', function () {
                        let x = {"bad": 33}
                        let y = 5;
                        assert.throws( () => sub.subtract(x, y), TypeError, "Please enter valid Integers")
                })


        })

        context('with missing parameter(s)', function () {

                it('should throw Syntax Error - missing both x and y', function () {
                        assert.throws( () => sub.subtract(), SyntaxError, "missing formal parameter")
                })

                it('should throw Syntax Error - missing x', function () {
                        assert.throws( () => sub.subtract(), SyntaxError, "missing formal parameter")
                })

                it('should throw Syntax Error - missing y', function () {
                        assert.throws( () => sub.subtract(), SyntaxError, "missing formal parameter")
                })

                it('should throw Syntax Error - x and y are null', function () {
                        assert.throws( () => sub.subtract(null,null), SyntaxError, "missing formal parameter")
                })


        })

})